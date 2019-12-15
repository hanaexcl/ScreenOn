package com.sobored.ScreenOn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

public class TileSrv extends TileService {
    SharedPreferences mSharedPreferences = null;
    Intent intent = new Intent();

    @Override
    public void onCreate() {
        Log.d("TileSrv", "onCreate");
        super.onCreate();

        intent.setClass(this, KeepScreenOnService.class);
    }

    @Override
    public void onClick() {
        Log.d("TileSrv", "onClick");

        super.onClick();
        Intent broadcastIntent = new  Intent("com.sobored.screenOn.intent");

        if (getQsTile().getState() == Tile.STATE_INACTIVE) {
            //啟動
            getQsTile().setState(Tile.STATE_ACTIVE);
            getQsTile().updateTile();
            broadcastIntent.putExtra("setting", 1);
            setSetting(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        } else {
            //關閉
            getQsTile().setState(Tile.STATE_INACTIVE);
            getQsTile().updateTile();
            broadcastIntent.putExtra("setting", 0);
            setSetting(false);

            stopService(intent);
        }
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onStartListening() {
        Log.d("TileSrv", "onStartListening");

        if (getSetting() == true) {
            getQsTile().setState(Tile.STATE_ACTIVE);
            getQsTile().updateTile();
        } else {
            getQsTile().setState(Tile.STATE_INACTIVE);
            getQsTile().updateTile();
        }
        super.onStartListening();
    }

    private void setSetting(boolean sw) {
        if (mSharedPreferences == null)
            mSharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
            mSharedPreferences.edit()
                .putBoolean("Screen", sw)
                .apply();
    }

    private boolean getSetting() {
        if (mSharedPreferences == null) mSharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean("Screen", false);
    }
}
