package me.drakeet.demo

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import me.drakeet.library.CrashWoodpecker

/**
 * Created by drakeet on 8/31/15.
 */
public class App : Application() {

    override fun onCreate() {
        super.onCreate()
//        CrashWoodpecker2(this).fly()
        CrashWoodpecker.fly().to(this)
        LeakCanary.install(this);
    }
}
