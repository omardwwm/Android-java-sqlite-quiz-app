package fr.doranco.quizzomar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CompteActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    Button buttonValider, buttonRetourHome, buttonAfficherList;
    EditText editTextNom, editTextPrenom, editTextLogin, editTextPassword, editTextConfirmPassword;
    ListView listViewUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);

        //        On Initialise les buttons
        buttonValider = findViewById(R.id.buttonValider);
        buttonRetourHome = findViewById(R.id.buttonRetourHome);
        buttonAfficherList = findViewById(R.id.buttonAfficherList);
        listViewUsers = findViewById(R.id.listView);
//        On initialise les EditText
        editTextNom = findViewById(R.id.editTextNom);
        editTextPrenom = findViewById(R.id.editTextPrenom);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "button valider l'inscription/creation compte a ete clique");
                validerInscription(view);
            }
        });

        buttonRetourHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "button Retour to homePage a ete clique");
                Intent intent = new Intent(CompteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttonAfficherList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                afficherUsers(view);
            }
        });
    }

    public void validerInscription(View view) {
        String firstName = editTextPrenom.getText().toString().trim();
        String lastName = editTextNom.getText().toString().trim();
        String login = editTextLogin.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
//        int scroe =0;
//        check if user_login exists in preferedShare
        //        On initialise les sharedPreferences
        if (firstName.isEmpty()
                || lastName.isEmpty()
                || login.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()
        ) {
            Toast.makeText(this, "tous les champs sont obligatoires", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() < 4 || password.length() > 16) {
            Toast.makeText(this, "Le mot de passe doit etre compris entre 4 et 16 caracteres", Toast.LENGTH_LONG).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Les mots de passes et sa confirmation doivent etres identiques", Toast.LENGTH_LONG).show();
        }
//        CHECK IF THE LOGIN EXISTS IN DB BEFORE
        UserProvider userProvider = new UserProvider();
        if (userProvider.checkLoginExists(login)){
            Toast.makeText(this, "Ce login existe deja, connectez vous ou suivre avec un autre ", Toast.LENGTH_LONG).show();
            return;
        }
        User user = new User(firstName, lastName, login, password);
        ContentValues values = new ContentValues();
        values.put(MyDataBaseSQLite.KEY_FIRSTNAME, user.getFirstName());
        values.put(MyDataBaseSQLite.KEY_LASTNAME, user.getLastName());
        values.put(MyDataBaseSQLite.KEY_LOGIN, user.getLogin());
        values.put(MyDataBaseSQLite.KEY_PASSWORD, user.getPassword());
        values.put(MyDataBaseSQLite.KEY_SCORE, user.getScore());
        getContentResolver().insert(UserProvider.CONTENT_URI, values);
        Toast.makeText(getBaseContext(),"User ajoute avec succes !", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(CompteActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    public void afficherUsers(View view){
//        test=====================================
//        String loginToCheck = editTextLogin.getText().toString().trim();
        String loginToCheck = "logintoto";

        Uri uri = Uri.parse("content://fr.doranco.quizzomar.UserProvider/users");
//        String[] projection = {MyDataBaseSQLite.KEY_LASTNAME, MyDataBaseSQLite.KEY_FIRSTNAME};
//        String where = MyDataBaseSQLite.KEY_LOGIN + "=?";
//        String[] whereParam = {loginToCheck};
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        List<User> listUsers = new ArrayList<User>();
        if (cursor.moveToFirst()) {
//            User user = new User();
            while(!cursor.isAfterLast()) {
                User user = new User();
//                String nom = cursor.getString(cursor.getColumnIndexOrThrow(UserProvider.first_name));
                user.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_ROWID)));
                user.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_FIRSTNAME)));
                user.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_LASTNAME)));
                user.setLogin(cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_LOGIN)));
//                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_PASSWORD)));
                user.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_SCORE)));
                listUsers.add(user);
                cursor.moveToNext();
            }
        }
//        else{
////            User userTest = new User("test", "test", "testlogin", "test123");
//            listUsers.add("userTest");
//        }
        ArrayAdapter<User> arrayAdapter = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listUsers);
        listViewUsers.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        if (cursor != null && !cursor.isClosed())
            cursor.close();
    }
}