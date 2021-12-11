package fr.doranco.quizzomar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements IConstants{
    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText editTextTextLoginConnexion, editTextTextPasswordLogin;
    private Button buttonSeConnecter, buttonFromLoginPageToCreate;
//    LES SHAREDPREFERENCES A IMPLEMENTER A LA FI DU PROJET
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonSeConnecter = findViewById(R.id.buttonSeConnecter);
        buttonFromLoginPageToCreate = findViewById(R.id.buttonFromLoginPageToCreate);

        //        On recupere les champs saisis
        editTextTextLoginConnexion = findViewById(R.id.editTextTextLoginConnexion);
        editTextTextPasswordLogin = findViewById(R.id.editTextTextPasswordLogin);

        buttonSeConnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "button Se Connecter a ete clique");
                login(view);
            }
        });

        buttonFromLoginPageToCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "button Redirection to page Create compte a ete clique");
                Intent intent = new Intent(LoginActivity.this, CompteActivity.class);
                startActivity(intent);
            }
        });
    }


    public void login(View view){
        UserProvider userProvider = new UserProvider();
        //        on check les champs saisis lors de la connexion
        String loginToConnect = editTextTextLoginConnexion.getText().toString().trim();
        String passwordToConnect = editTextTextPasswordLogin.getText().toString().trim();

        if (loginToConnect.isEmpty() || passwordToConnect.isEmpty()) {
            Toast.makeText(this, "Tous les champs sont obligatoires !", Toast.LENGTH_LONG).show();
            return;
        }
        if (!(userProvider.checkLoginExists(loginToConnect))){
            Toast.makeText(this, "Aucun utilisateur avec ce login !", Toast.LENGTH_LONG).show();
            Log.i(TAG,"=========>" + userProvider.checkLoginExists(loginToConnect));
            Log.i(TAG,"------------------>>>>>>> logintoconnect is : " + loginToConnect);
            return;
        }
//        else {
//            Toast.makeText(this, "cool tu peux continuer l'identification !", Toast.LENGTH_LONG).show();
//            Log.i(TAG,"=========>" + userProvider.checkLoginExists(loginToConnect));
//            Log.i(TAG,"------------------>>>>>>> logintoconnect is : " + loginToConnect);
//        }

//         IMPLEMENTER APRES LA CREATION DU COMPTE
//        Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
//        intent.putStringArrayListExtra("mesDatas", mesDatas);
//        startActivity(intent);
        Uri uri = Uri.parse("content://fr.doranco.quizzomar.UserProvider/users");
        String where = MyDataBaseSQLite.KEY_LOGIN + "=? AND " + MyDataBaseSQLite.KEY_PASSWORD + "=?";
        String[] whereParam = {loginToConnect, passwordToConnect};
        Cursor cursor = getContentResolver().query(uri, null, where, whereParam, null);
        if (cursor.moveToFirst()){
            User user = new User();
            while(!cursor.isAfterLast()) {
//                modif
                user.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_ROWID)));
                user.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_FIRSTNAME)));
                user.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_LASTNAME)));
                user.setLogin(cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_LOGIN)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_PASSWORD)));
                user.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_SCORE)));
                cursor.moveToNext();
                Log.i(TAG,"User after identification======>>>" + user);
            }
            sharedPreferences = getSharedPreferences("currentUser", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PREF_USER_FIRSTNAME, user.getFirstName());
            editor.putString(PREF_USER_LASTNAME, user.getLastName());
            editor.putString(PREF_USER_LOGIN, user.getLogin());
            editor.putString(PREF_USER_PASSWORD, user.getPassword());
            editor.putInt(PREF_USER_SCORE, user.getScore());
            editor.putBoolean(PREF_USER_LOGIN_STATUS, true);
            editor.apply();
            Intent intent = new Intent(LoginActivity.this, QuizLauncherActivity.class);
//            intent.putExtra("MyUser", user);
            startActivity(intent);
        }else {
            Toast.makeText(this, "Login et/ou mot de passe incoccerts !", Toast.LENGTH_LONG).show();
        }
    }
}