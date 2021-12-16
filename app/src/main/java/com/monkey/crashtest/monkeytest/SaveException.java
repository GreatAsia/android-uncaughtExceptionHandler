package com.monkey.crashtest.monkeytest;


import java.io.File;

import java.io.FileOutputStream;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

public class SaveException implements UncaughtExceptionHandler {

    private Context mContext;
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;
    private static SaveException mException;

    public static final String SDPATH = "/sdcard/crash/";// 存放位置
    public static String fileName = "monkeycrash";
    private static String TAG = "monkeycrashlog";

    private SaveException() {
    }

    public static SaveException getInstance() {
        if (mException == null) {
            mException = new SaveException();
        }
        return mException;
    }

    public void init(Context context) {
        mContext = context;
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        handleException(thread, ex);
        Log.e(TAG, "uncaughtException: "+thread.getName()+" "+ex.getMessage());
        Log.e(TAG,"kill process==" + android.os.Process.myPid());
        android.os.Process.killProcess(android.os.Process.myPid());

    }


    private boolean handleException(Thread thread, Throwable ex) {
        StringBuilder sb = new StringBuilder();

        long startTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
        Date firstDate = new Date(System.currentTimeMillis()); // 第一次创建文件，也就是开始日期
        String str = formatter.format(firstDate);

        sb.append("\n");
        sb.append(str + "----------------------------->"); // 把当前的日期写入到字符串中
        sb.append("\n");

        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);

        ex.printStackTrace(pw);

        String errorresult = writer.toString();
        sb.append(errorresult);
        sb.append("\n");
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.i(TAG,"Sdcard可用");
            } else {
                Log.e(TAG,"Sdcard不可用");
            }
            File fileDir = new File("/sdcard/crash/");
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            Log.i(TAG,"fileDir==" + fileDir);
            File files = new File(fileDir, fileName + startTime + ".log");
            if (!files.exists()) {
                files.createNewFile();
            }
            Log.i(TAG,"filesexists==" + files.exists());
            FileOutputStream fileOutputStream = new FileOutputStream(files, true);
            fileOutputStream.write(sb.toString().getBytes());
            fileOutputStream.close();
            Log.e(TAG,"crashString=="  + sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"Exception==" + e.getLocalizedMessage());
        }

        return true;
    }




    /**
     * 获取手机信息
     */
    private String appendPhoneInfo() throws PackageManager.NameNotFoundException
    {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        StringBuilder sb = new StringBuilder();
        //App版本
        sb.append("App Version: ");
        sb.append(pi.versionName);
        sb.append("_");
        sb.append(pi.versionCode + "\n");

        //Android版本号
        sb.append("OS Version: ");
        sb.append(Build.VERSION.RELEASE);
        sb.append("_");
        sb.append(Build.VERSION.SDK_INT + "\n");

        //手机制造商
        sb.append("Vendor: ");
        sb.append(Build.MANUFACTURER + "\n");

        //手机型号
        sb.append("Model: ");
        sb.append(Build.MODEL + "\n");

        //CPU架构
        sb.append("CPU: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            sb.append(Arrays.toString(Build.SUPPORTED_ABIS));
        } else
        {
            sb.append(Build.CPU_ABI);
        }
        return sb.toString();
    }

}
