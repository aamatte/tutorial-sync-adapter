package com.example.andres.myapplication.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.andres.myapplication.Authenticator.AccountAuthenticator;

/**
 * Este servicio permite a otros procesos enlazarse (bind) a el y permite que se puedan comunicar
 * con {@link AccountAuthenticator}. Lo único que se debe hacer es llamar al método
 * {@link AccountAuthenticator#getIBinder()}.
 */
public class AuthenticatorService extends Service {
    private AccountAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new AccountAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
