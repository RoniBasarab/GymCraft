package com.roni.gymcraft.feature.home

import com.roni.gymcraft.core.game.GameRepository
import com.roni.gymcraft.core.game.TimeTravel
import com.roni.gymcraft.core.logging.AppLogger
import com.roni.gymcraft.core.save.SaveStore
import com.roni.gymcraft.core.system.DebugTools
import com.roni.gymcraft.core.system.FailurePolicy
import com.roni.gymcraft.core.system.Failures
import com.roni.gymcraft.core.system.ProblemAction
import com.roni.gymcraft.engine.content.DefaultContent
import com.roni.gymcraft.engine.content.DefaultMatchup
import com.roni.gymcraft.engine.content.Outpost
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Drives Home through onAction on a real repository (temp folder, a fake clock, everything on one thread). */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @Before fun mainOnThisThread() = Dispatchers.setMain(UnconfinedTestDispatcher())
    @After fun resetMainDispatcher() = Dispatchers.resetMain()

    private var now = LocalDateTime.of(2026, 11, 1, 9, 0)   // a Sunday morning: the season starts today
    private val dir: File = Files.createTempDirectory("home").toFile()
    private val rescued = ArrayList<ProblemAction>()
    private val travel = object : TimeTravel {
        override val enabled = true
        override var offsetMinutes = 0L
        override fun forward(minutes: Long) { offsetMinutes += minutes; now = now.plusMinutes(minutes) }
        override fun reset() = Unit
        override fun now(): LocalDateTime = now
    }
    private val logs: File = Files.createTempDirectory("logs").toFile()
    private val failures = Failures(AppLogger(File(logs, "gymcraft.log")), FailurePolicy(loud = false))

    private fun vm(store: SaveStore = SaveStore(dir)) = HomeViewModel(
        GameRepository(store, travel, DefaultContent, DefaultMatchup, Dispatchers.Unconfined, seed = { 3L }),
        DefaultContent, failures, { rescued += it }, travel, DebugTools(enabled = true),
    )

    @Test
    fun opens_ready_withTheBase() {
        val s = vm().state.value
        assertEquals(HomePhase.Ready, s.phase)
        assertEquals(100, s.game?.supplies)
        assertTrue(s.debugTools)
        assertFalse(s.problem)
    }

    @Test
    fun iWent_paysOnce_andTheSergeantAnswers() {
        val vm = vm()
        vm.onAction(HomeAction.Went)
        assertEquals("Good work, recruit. +100 supplies.", vm.state.value.reply)
        assertEquals(200, vm.state.value.game?.supplies)
        assertTrue(vm.state.value.game?.wentToday == true)
        vm.onAction(HomeAction.Went)
        assertEquals("That's done for today, recruit.", vm.state.value.reply)
        vm.onAction(HomeAction.DismissReply)
        assertNull(vm.state.value.reply)
    }

    @Test
    fun theSheet_picksTrainsAndUpgrades() {
        val vm = vm()
        vm.onAction(HomeAction.Went)                          // 200
        vm.onAction(HomeAction.OpenBuilding(Outpost.Barracks))
        vm.onAction(HomeAction.Pick(Outpost.Rifleman, 2))
        assertEquals(100, vm.state.value.sheet?.units?.get(0)?.total)
        vm.onAction(HomeAction.Train(Outpost.Rifleman))
        assertEquals("2 Riflemen ready.", vm.state.value.reply)
        assertEquals(100, vm.state.value.game?.supplies)
        assertEquals(2, vm.state.value.sheet?.units?.get(0)?.ready)   // the open sheet follows the game
        assertEquals(2, vm.state.value.sheet?.units?.get(0)?.count)   // and keeps the pick
        vm.onAction(HomeAction.Upgrade)
        assertEquals("Not enough supplies. A gym session brings 100.", vm.state.value.reply)
        vm.onAction(HomeAction.CloseSheet)
        assertNull(vm.state.value.sheet)
    }

    @Test
    fun leave_andBack_fromTheMenu() {
        val vm = vm()
        vm.onAction(HomeAction.ShowMenu(true))
        assertTrue(vm.state.value.menuOpen)
        vm.onAction(HomeAction.StartLeave)
        assertFalse(vm.state.value.menuOpen)
        assertTrue(vm.state.value.game?.onLeave == true)
        assertNull(vm.state.value.reply)
        vm.onAction(HomeAction.EndLeave)
        assertFalse(vm.state.value.game?.onLeave == true)
        assertEquals("Welcome back, recruit. The gym's waiting.", vm.state.value.reply)
    }

    @Test
    fun timeTravel_fightsTheBattles_andTheAwayCardClears() {
        val vm = vm()
        vm.onAction(HomeAction.TimeForward(12 * 60L))          // Sunday 21:00: the 20:00 raid was fought
        assertEquals(1, vm.state.value.game?.away?.size)
        vm.onAction(HomeAction.DismissAway)
        assertEquals(0, vm.state.value.game?.away?.size)
        now = now.plusDays(1)
        vm.onAction(HomeAction.Resumed)
        assertEquals(1, vm.state.value.game?.away?.size)
    }

    @Test
    fun aFailedSave_showsTheErrorCard_andItsButtonsReachRescue() {
        val vm = vm()
        dir.deleteRecursively(); dir.writeText("not a folder")   // every save from now on fails
        vm.onAction(HomeAction.Went)
        assertTrue(vm.state.value.problem)
        assertEquals(100, vm.state.value.game?.supplies)   // what's shown is what's on disk
        vm.onAction(HomeAction.Problem(ProblemAction.SendLog))
        assertEquals(listOf<ProblemAction>(ProblemAction.SendLog), rescued)
        assertTrue(File(logs, "gymcraft.log").readText().contains("failed: home.session"))
    }
}
