package com.example.andres.myapplication.Authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.andres.myapplication.Activities.AuthenticatorActivity;

/**
 * Created by andres on 18-04-15.
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {

    Context mContext;

    public AccountAuthenticator(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }


    /**
     * Called when user wants to log-in and add a new account to device.
     * @return bundle with intent to start AuthenticatorActivity.
     */
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {

        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);

        intent.putExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE, accountType);

        intent.putExtra(AuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);

        intent.putExtra(AuthenticatorActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);

        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();

        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }



    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {

        // Extrae username y pass del account manager
        final AccountManager am = AccountManager.get(mContext);
        Log.i("Authenticator", "Getting auth token");

        //pide el authtoken
        String authToken=am.peekAuthToken(account, authTokenType);

        //Otro intento para autenticar al usuario
        if (TextUtils.isEmpty(authToken)){
            final String password = am.getPassword(account);
            if (password != null) {
                // Acá se debería pedir el token a la api, pero se la doy altiro
                authToken = "1MQK9QG5as8nu6yw9ENKJAtt";
            }
        }

        // Si obtenemos un authToken, lo retornamos
        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);

            return result;
        }

        //Si llegamos acá no pudimos acceder a la contraseña del usuario, necesitamos pedirle de nuevo las credenciales

        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(AuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;

    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
