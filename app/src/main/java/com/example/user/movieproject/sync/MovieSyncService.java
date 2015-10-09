package com.example.user.movieproject.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by USER on 10/3/2015.
 */
public class MovieSyncService extends Service {
    private static final Object syncAdapterLock = new Object();
    private static MovieSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (syncAdapterLock){
            if (sSyncAdapter == null){
                sSyncAdapter = new MovieSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
