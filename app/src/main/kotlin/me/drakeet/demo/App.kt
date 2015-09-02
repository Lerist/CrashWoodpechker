package me.drakeet.demo

import android.app.Application
import me.drakeet.library.CrashWoodpecker

/**
 * Created by drakeet on 8/31/15.
 */
public class App : Application() {

    override fun onCreate() {
        super.onCreate()
        CrashWoodpecker.fly().to(this)
    }
}
