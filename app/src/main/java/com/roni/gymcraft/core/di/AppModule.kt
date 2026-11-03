package com.roni.gymcraft.core.di

import android.content.Context
import android.util.Log
import com.roni.gymcraft.BuildConfig
import com.roni.gymcraft.core.game.DebugClock
import com.roni.gymcraft.core.game.GameClock
import com.roni.gymcraft.core.game.GameRepository
import com.roni.gymcraft.core.game.PhoneClock
import com.roni.gymcraft.core.game.TimeTravel
import com.roni.gymcraft.core.logging.AppLogger
import com.roni.gymcraft.core.save.SaveStore
import com.roni.gymcraft.core.system.AppRescue
import com.roni.gymcraft.core.system.DebugTools
import com.roni.gymcraft.core.system.FailurePolicy
import com.roni.gymcraft.core.system.Rescue
import com.roni.gymcraft.engine.content.DefaultContent
import com.roni.gymcraft.engine.content.DefaultMatchup
import com.roni.gymcraft.engine.model.GameContent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers

/** The app's singletons (architecture.md §3 core/di). Everything that differs between debug and release is decided here. */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun failurePolicy() = FailurePolicy(loud = BuildConfig.DEBUG)

    @Provides @Singleton
    fun debugTools() = DebugTools(enabled = BuildConfig.DEBUG)

    @Provides @Singleton
    fun logger(@ApplicationContext context: Context) =
        AppLogger(File(context.filesDir, "logs/gymcraft.log")) { Log.i("GymCraft", it) }

    @Provides @Singleton
    fun content(): GameContent = DefaultContent

    /** Debug: the phone's time plus the debug offset; release: the phone's time. */
    @Provides @Singleton
    fun timeTravel(@ApplicationContext context: Context): TimeTravel =
        if (BuildConfig.DEBUG) DebugClock(File(context.filesDir, "debug_clock.txt")) else PhoneClock()

    @Provides
    fun clock(travel: TimeTravel): GameClock = travel

    @Provides @Singleton
    fun saveStore(@ApplicationContext context: Context) = SaveStore(context.filesDir)

    @Provides @Singleton
    fun gameRepository(store: SaveStore, clock: GameClock, content: GameContent) =
        GameRepository(store, clock, content, DefaultMatchup, Dispatchers.IO)

    @Provides
    fun rescue(rescue: AppRescue): Rescue = rescue
}
