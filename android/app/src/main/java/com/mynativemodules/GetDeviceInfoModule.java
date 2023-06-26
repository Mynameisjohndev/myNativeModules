package com.mynativemodules;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import java.math.BigInteger;


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
    public void getTotalStorage(Promise promise) {
        if (ContextCompat.checkSelfPermission(reactContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
        } else {
            ActivityCompat.requestPermissions(getCurrentActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @ReactMethod
    public void getUsedStorage(Promise promise) {
        if (ContextCompat.checkSelfPermission(reactContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
        } else {
            ActivityCompat.requestPermissions(getCurrentActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @ReactMethod
      public void getInstallationTime(Promise promise) {
      try {
        PackageInfo packageInfo = getReactApplicationContext().getPackageManager()
        .getPackageInfo(getReactApplicationContext().getPackageName(), 0);
        promise.resolve(ackageInfo.firstInstallTime);
      } catch (NameNotFoundException e) {
        promise.reject("ERROR", "Package name not found");
      }
    }

}
