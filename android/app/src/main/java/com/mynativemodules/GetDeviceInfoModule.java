package com.mynativemodules;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;
import android.app.Activity;
import androidx.core.content.ContextCompat;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import java.math.BigInteger;
import java.util.Date;
import java.text.DateFormat;
import android.app.ActivityManager;
import android.os.Debug;
import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;

import static android.provider.Settings.Secure.getString;

public class GetDeviceInfoModule extends ReactContextBaseJavaModule {

    private static ReactApplicationContext reactContext;

    GetDeviceInfoModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    public String getName() {
        return "DeviceStorage";
    }

    @ReactMethod
    public void checkReadPermissionReadExternalStorage(Promise promise) {
        int permissionStatus = ContextCompat.checkSelfPermission(reactContext, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            promise.resolve("granted");
        } else if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            promise.resolve("denied");
        } else {
            promise.resolve("unknown");
        }
    }

    @ReactMethod
    public void requestReadPermissionReadExternalStorage(Promise promise) {
        ActivityCompat.requestPermissions(getCurrentActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        promise.resolve("requested");
    }

    @ReactMethod
    public void getTotalStorage(Promise promise) {
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            WritableMap totalStorage = Arguments.createMap();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                totalStorage.putDouble("getBlockCount", statFs.getBlockCountLong());
                totalStorage.putDouble("getBlockSize", statFs.getBlockSizeLong());
                long total = statFs.getBlockCountLong() * statFs.getBlockSizeLong();
                totalStorage.putDouble("total", total);
            } else {
                totalStorage.putDouble("getBlockCount", statFs.getBlockCount());
                totalStorage.putDouble("getBlockSize", statFs.getBlockSize());
                long total = statFs.getBlockCount() * statFs.getBlockSize();
                totalStorage.putDouble("total", total);
            }
            promise.resolve(totalStorage);
        } catch (Exception e) {
            promise.reject("ERR_UNEXPECTED_EXCEPTION", e);
        }
    }

    @ReactMethod
    public void getUsedStorage(Promise promise) {
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            WritableMap totalStorage = Arguments.createMap();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                totalStorage.putDouble("getAvailableBlocks", statFs.getAvailableBlocksLong());
                totalStorage.putDouble("getBlockSize", statFs.getBlockSizeLong());
                long total = statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
                totalStorage.putDouble("total", total);
            } else {
                totalStorage.putDouble("getAvailableBlocks", statFs.getAvailableBlocks());
                totalStorage.putDouble("getBlockSize", statFs.getBlockSize());
                long total = statFs.getAvailableBlocks() * statFs.getBlockSize();
                totalStorage.putDouble("total", total);
            }
            promise.resolve(totalStorage); 
        } catch (Exception e) {
            promise.reject("ERR_UNEXPECTED_EXCEPTION", e);
        }
    }

    @ReactMethod
    public void getInstallationTime(Promise promise) {
      try {
        WritableMap day = Arguments.createMap();
        PackageInfo packageInfo = getReactApplicationContext().getPackageManager()
        .getPackageInfo(getReactApplicationContext().getPackageName(), 0);
        long lastTime = packageInfo.firstInstallTime;
        day.putDouble("date", lastTime);
        promise.resolve(day);
      } catch (NameNotFoundException e) {
        promise.reject("ERROR", "Package name not found");
      }
    }

    @ReactMethod
    public void getDeviceRAM(Promise promise) {
        ActivityManager actManager = (ActivityManager) reactContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        double totalMemory = memInfo.totalMem;
        promise.resolve(totalMemory);
    }

    @ReactMethod
    public void getCPUDetails(Promise promise) {
        ProcessBuilder processBuilder;
        String cpuDetails = "";
        String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
        InputStream is;
        Process process;
        byte[] bArray;
        bArray = new byte[1024];
        try {
            processBuilder = new ProcessBuilder(DATA);
            process = processBuilder.start();
            is = process.getInputStream();
            while (is.read(bArray) != -1) {
                cpuDetails = cpuDetails + new String(bArray);
            }
            is.close();
            promise.resolve(cpuDetails);
        } catch (IOException ex) {
            promise.reject("Erro", ex);
        }
    }

    @ReactMethod
    public void getCPUFrequency(Promise promise) {
        try {
            RandomAccessFile reader = new RandomAccessFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq", "r");
            String freq = reader.readLine();
            reader.close();
            promise.resolve(freq);
        } catch (Exception e) {
            promise.reject("ERR_UNEXPECTED_EXCEPTION", e);
        }
    }

    @ReactMethod
    public void getDeviceModel(Promise promise) {
        String deviceModel = Build.MODEL;
        promise.resolve(deviceModel);
    }

    @ReactMethod
    public void getSystemVersion(Promise promise) {
        String systemVersion = Build.VERSION.RELEASE;
        promise.resolve(systemVersion);
    }

    @ReactMethod
    public void getUniqueIdSync(Promise promise) { 
        String id = getString(getReactApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        promise.resolve(id); 
    }

    @ReactMethod
    public String getDeviceIMEI(Promise promise) {
        try {
            String deviceUniqueIdentifier = null;
            TelephonyManager tm = (TelephonyManager) reactContext.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != tm) {
                deviceUniqueIdentifier = tm.getDeviceId();
            }
            if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
                deviceUniqueIdentifier =  getString(getReactApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            promise.resolve(deviceUniqueIdentifier);
        } catch (Exception e) {
            promise.reject("IMEI_ERROR", e);
        }
        return null; 
    }

}
