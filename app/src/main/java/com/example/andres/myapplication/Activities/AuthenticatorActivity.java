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

import com.example.andres.myapplication.Authenticator.AccountAuthenticator;
import com.example.andres.myapplication.R;

/**
 * Actividad llamada por AccountAuthenticator para que el usuario ingrese a su cuenta o se registre.
 * Esta debe interactuar con el servidor para obtener el token y retornarlo al AccountAuthenticator.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public static final String ARG_ACCOUNT_TYPE = "1";
    public static final String ARG_AUTH_TYPE = "2";
    public static final String ARG_IS_ADDING_NEW_ACCOUNT = "3";
    public static final String PARAM_USER_PASS = "4";
    private AccountManager mAccountManager;

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

    /**
     * Método llamado cuando se aprieta el botón de login. Manda al servidor el usuario y contraseña
     * y este le retorna el token necesario para utilizar la API.
     */
    private void submit() {
        // Se obtiene el usuario y contrasena ingresados
        final String userName = ((TextView) findViewById(R.id.account_name))
                .getText().toString();
        final String userPass = ((TextView) findViewById(R.id.account_password))
                .getText().toString();

        // Se loguea de forma asincronica para no entorpecer el UI thread
        new AsyncTask<Void, Void, Intent>() {
            @Override
            protected Intent doInBackground(Void... params) {
                // Se loguea en el servidor y retorna token
                String authtoken = logIn(userName, userPass);
                // Informacion necesaria para enviar al authenticator
                final Intent res = new Intent();
                res.putExtra(AccountManager.KEY_ACCOUNT_NAME, userName);
                res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AccountAuthenticator.ACCOUNT_TYPE);
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

    /**
     * Crea cuenta en el AccountManager si es que aún no existe y setea token asociado a esa cuenta.
     * Si cuenta ya existe, solo setea nueva password. Luego, finaliza esta actividad.
     * @param intent
     */
    private void finishLogin(Intent intent) {

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName,
                intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        // Si es que se esta anadiendo una nueva cuenta
        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {

            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = AccountAuthenticator.AUTHTOKEN_TYPE;
            // Creando cuenta en el dispositivo y seteando el token que obtuvimos.
            mAccountManager.addAccountExplicitly(account, accountPassword, null);

            // Ojo: hay que setear el token explicitamente si la cuenta no existe,
            // no basta con mandarlo al authenticator
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        }
        // Si no se está añadiendo cuenta, el token estaba antiguo invalidado.
        // Seteamos contraseña nueva por si la cambio.
        else {
            // Solo seteamos contraseña.  Aca no es necesario setear el token explicitamente,
            // basta con enviarlo al Authenticator
            mAccountManager.setPassword(account, accountPassword);
        }
        // Setea el resultado para que lo reciba el Authenticator
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        // Cerramos la actividad
        finish();
    }

    /**
     * Pide al servidor el token asociado al usuario y contraseña lo retorna.
     * @param user Username.
     * @param pass Password.
     * @return Token necesario para interactuar con la API.
     */
    private String logIn(String user, String pass){
        // Método para fines demostrativos :)
        return "tokentokentokentoken";
    }
}
