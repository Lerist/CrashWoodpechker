/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 drakeet (drakeet.me@gmail.com)
 * http://drakeet.me
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.drakeet.library;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import me.drakeet.library.ui.CatchActivity;

/**
 * Created by drakeet(http://drakeet.me)
 * Date: 8/31/15 22:35
 */
public class CrashWoodpecker implements Thread.UncaughtExceptionHandler {

    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private Thread.UncaughtExceptionHandler mOriginHandler;
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
        String traces = getStackTrace(throwable);
        Intent intent = new Intent();
        intent.setClass(mContext, CatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String[] strings = traces.split("\n");
        String[] newStrings = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            newStrings[i] = strings[i].trim();
        }
        intent.putExtra(CatchActivity.EXTRA_PACKAGE, mContext.getPackageName());
        intent.putExtra(CatchActivity.EXTRA_CRASH_LOGS, newStrings);
        mContext.startActivity(intent);
    }

    private String getStackTrace(Throwable throwable) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return writer.toString();
    }
}
