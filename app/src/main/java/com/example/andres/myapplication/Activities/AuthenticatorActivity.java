package com.example.andres.myapplication.Activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.andres.myapplication.R;

/**
 * Created by andres on 18-04-15.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public static final String ARG_ACCOUNT_TYPE = "1";
    public static final String ARG_AUTH_TYPE = "2";
    public static final String ARG_IS_ADDING_NEW_ACCOUNT = "3";
    public static final String PARAM_USER_PASS = "4";
    AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);
        mAccountManager = AccountManager.get(this);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    public void submit() {
        final String userName = ((TextView) findViewById(R.id.account_name)).getText().toString();
        final String userPass = ((TextView) findViewById(R.id.account_password)).getText().toString();
        new AsyncTask<Void, Void, Intent>() {
            @Override
            protected Intent doInBackground(Void... params) {
                // Acá se debería pedir a la web
                String authtoken = "1MQK9QG5as8nu6yw9ENKJAtt";
                final Intent res = new Intent();
                res.putExtra(AccountManager.KEY_ACCOUNT_NAME, userName);
                res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, "com.example.andres.myapplication");
                res.putExtra(AccountManager.KEY_AUTHTOKEN, authtoken);
                res.putExtra(PARAM_USER_PASS, userPass);
                return res;
            }
            @Override
            protected void onPostExecute(Intent intent) {
                finishLogin(intent);
            }
        }.execute();
    }

    private void finishLogin(Intent intent) {
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = "normal";
            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)

            mAccountManager.addAccountExplicitly(account, accountPassword, null);

            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        }
        else {
            // Si no está añadiendo, solo está pidiendo la contraseña de nuevo, seteamos contraseña
            mAccountManager.setPassword(account, accountPassword);

        }
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

}
