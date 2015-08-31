package me.drakeet.library

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Looper
import android.util.Log
import android.widget.Toast
import me.drakeet.library.ui.CatchActivity
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap

/**
 * Created by drakeet on 8/31/15.
 */
public class CrashWoodpecker: Thread.UncaughtExceptionHandler {

    val TAG = "CrashWoodpecker"
    val EXTRA_CRASH_LOGS = "EXTRA_CRASH_LOGS"

    var mContext: Context? = null
    var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    val mInfos = HashMap<String, String>()
    val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
    val mCrashLogs = CrashLogs()

    constructor(context: Context) {
        mContext = context
    }

    public fun fly() {
        mDefaultHandler = Thread.currentThread().getUncaughtExceptionHandler()
        Thread.currentThread().setUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread?, ex: Throwable?) {

        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler?.uncaughtException(thread, ex)
        } else {
            var crashCause = CrashCause()
            crashCause.stackTraceElements = ex?.getStackTrace()
            mCrashLogs.crashCauses?.plus(crashCause)

            var nextCause: Throwable? = ex?.getCause()
            while (nextCause != null) {
                var nextCrashCause = CrashCause()
                nextCrashCause.stackTraceElements = nextCause.getStackTrace()
                mCrashLogs.crashCauses?.plus(crashCause)
                nextCause = nextCause.getCause()
            }

            val intent = Intent()
            intent.setClass(mContext, javaClass<CatchActivity>())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(EXTRA_CRASH_LOGS, mCrashLogs)
            mContext!!.startActivity(intent)
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }

    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }

        object: Thread() {
            override fun run() {
                Looper.prepare()
                Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出。", Toast.LENGTH_LONG).show()
                Looper.loop()
            }
        }.start()

        collectDeviceInfo(mContext!!)
        saveCrashInfo2File(ex)
        return true
    }

    public fun collectDeviceInfo(context: Context) {
        try {
            val pm = context.getPackageManager()
            val pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES)

            if (pi != null) {
                val versionName = if (pi.versionName == null) "null" else pi.versionName
                val versionCode = "" + pi.versionCode
                mInfos.put("versionName", versionName)
                mInfos.put("versionCode", versionCode)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "an error occured when collect package info", e)
        }

        val fields = javaClass<Build>().getDeclaredFields()
        for (field in fields) {
            try {
                field.setAccessible(true)
                mInfos.put(field.getName(), field.get(null).toString())
                Log.d(TAG, field.getName() + " : " + field.get(null))
            } catch (e: Exception) {
                Log.e(TAG, "an error occured when collect crash info", e)
            }
        }
    }

    private fun saveCrashInfo2File(ex: Throwable): String? {
        val sb = StringBuffer()
        for (entry in mInfos.entrySet()) {
            val key = entry.getKey()
            val value = entry.getValue()
            sb.append(key + "=" + value + "\n")
        }

        val writer = StringWriter()
        val printWriter = PrintWriter(writer)

        ex.printStackTrace(printWriter)
        var cause: Throwable? = ex.getCause()
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.getCause()
        }

        printWriter.close()

        val result = writer.toString()
        sb.append(result)
        try {
            val timestamp = System.currentTimeMillis()
            val time = formatter.format(Date())
            val fileName = "crash-" + time + "-" + timestamp + ".log"

            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val path = "/sdcard/CrashWoodpecker/"
                val dir = File(path)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                val fos = FileOutputStream(path + fileName)
                fos.write(sb.toString().getBytes())
                fos.close()
            }

            return fileName
        } catch (e: Exception) {
            Log.e(TAG, "an error occured while writing file...", e)
        }

        return null
    }

}


