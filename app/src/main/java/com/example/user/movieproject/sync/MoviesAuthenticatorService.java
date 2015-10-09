package com.example.user.movieproject.sync;

import android.content.Intent;
import android.os.IBinder;
import android.app.Service;
/**
 * Created by USER on 10/5/2015.
 */


/**
 * The service which allows the sync adapter framework to access the authenticator.
 */
public class MoviesAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private MoviesAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new MoviesAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
