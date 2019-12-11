package com.sobored.ScreenOn;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;

public class KeepScreenOnService extends Service {
    IBinder mBinder;
    SharedPreferences mSharedPreferences = null;
    PowerManager mPowerManager = null;
    PowerManager.WakeLock mWakeLock = null;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mPowerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK  | PowerManager.ON_AFTER_RELEASE , "ScreenOn:WakeLock");
        mWakeLock.setReferenceCounted(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        switch (getSetting()) {
            case 0:
                mWakeLock.release();
                break;
            case 1:
                mWakeLock.acquire();
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private int getSetting() {
        if (mSharedPreferences == null) mSharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        return mSharedPreferences.getInt("Screen", 0);
    }

    private void setSetting(int sw) {
        if (mSharedPreferences == null) mSharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        mSharedPreferences.edit()
                .putInt("Screen", sw)
                .apply();
    }
}
