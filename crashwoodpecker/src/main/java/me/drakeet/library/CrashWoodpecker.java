package me.drakeet.library;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import me.drakeet.library.model.CrashCause;
import me.drakeet.library.model.CrashLogs;
import me.drakeet.library.ui.CatchActivity;

/**
 * Created by drakeet(http://drakeet.me)
 * Date: 8/31/15 22:35
 */
public class CrashWoodpecker implements Thread.UncaughtExceptionHandler {

    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private Thread.UncaughtExceptionHandler mOriginHandler;
    private CrashLogs mCrashLogs = new CrashLogs();
    private Context mContext;
    private String mVersion;

    public static CrashWoodpecker fly() {
        return new CrashWoodpecker();
    }

    public void to(Context context) {
        mContext = context;
        try {
            PackageInfo info =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            mVersion = info.versionName + "(" + info.versionCode + ")";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CrashWoodpecker() {
        mOriginHandler = Thread.currentThread().getUncaughtExceptionHandler();
        Thread.currentThread().setUncaughtExceptionHandler(this);
    }

    private boolean handleException(Throwable throwable) {
        boolean success = saveToFile(throwable);
        try {
            startCatchActivity(throwable);
            byeByeLittleWood();
        } catch (Exception e) {
            success = false;
        }
        return success;
    }

    private void byeByeLittleWood() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override public void uncaughtException(Thread thread, Throwable throwable) {
        boolean isHandle = handleException(throwable);
        if (!isHandle && mOriginHandler != null) {
            mOriginHandler.uncaughtException(thread, throwable);
        }
    }

    private boolean saveToFile(Throwable throwable) {
        String time = mFormatter.format(new Date());
        String fileName = "Crash-" + time + ".log";

        String rootPath = Environment.getExternalStorageDirectory().getPath();
        String crashDir = rootPath + "/CrashWoodpecker/";
        String crashPath = crashDir + fileName;

        String androidVersion = Build.VERSION.RELEASE;
        String deviceModel = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;

        File file = new File(crashPath);
        if (file.exists()) {
            file.delete();
        }
        else {
            try {
                new File(crashDir).mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }

        PrintWriter writer;
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            return false;
        }
        writer.write("Device: " + manufacturer + ", " + deviceModel + "\n");
        writer.write("Android Version: " + androidVersion + "\n");
        if (mVersion != null) writer.write("App Version: " + mVersion + "\n");
        writer.write("---------------------\n\n");
        throwable.printStackTrace(writer);
        writer.close();

        return true;
    }

    private void startCatchActivity(Throwable throwable) {
        CrashCause crashCause = new CrashCause();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        crashCause.stackTrace = writer.toString();
        mCrashLogs.crashCauses.add(crashCause);

        Throwable next = throwable.getCause();
        while (next != null) {
            CrashCause nextCrashCause = new CrashCause();
            writer = new StringWriter();
            printWriter = new PrintWriter(writer);
            next.printStackTrace(printWriter);
            printWriter.close();
            nextCrashCause.stackTrace = writer.toString();
            mCrashLogs.crashCauses.add(crashCause);
            next = next.getCause();
        }

        Intent intent = new Intent();
        intent.setClass(mContext, CatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String value = new Gson().toJson(mCrashLogs);
        intent.putExtra(CatchActivity.EXTRA_CRASH_LOGS, value);
        mContext.startActivity(intent);
    }
}
