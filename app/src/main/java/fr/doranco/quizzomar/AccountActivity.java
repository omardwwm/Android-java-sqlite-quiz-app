package fr.doranco.quizzomar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AccountActivity extends AppCompatActivity implements IConstants{
    private static final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;

    EditText editTextDynamicPrenom, editTextDynamicName, editTextDynamicLogin, editTextDynamicScore;

    Button buttonGoToQuiz, buttonDeleteAccount, buttonUpdateInfoUser,buttonLogOutAccountLayout, buttonValidUpdate;
    ToggleButton toggleButtonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Utils util = new Utils();

        editTextDynamicName = findViewById(R.id.editTextDynamicName);
        editTextDynamicPrenom = findViewById(R.id.editTextDynamicPrenom);
        editTextDynamicLogin = findViewById(R.id.editTextDynamicLogin);
        editTextDynamicScore = findViewById(R.id.editTextDynamicScore);

        buttonGoToQuiz = findViewById(R.id.buttonGoToQuiz);
        buttonDeleteAccount = findViewById(R.id.buttonDeleteAccount);
//        buttonUpdateInfoUser = findViewById(R.id.buttonUpdateInfoUser);
        buttonLogOutAccountLayout = findViewById(R.id.buttonLogOutAccountLayout);
        buttonValidUpdate = findViewById(R.id.buttonValidUpdate);
//        ==============================================================
        toggleButtonUpdate = findViewById(R.id.toggleButtonUpdate);

//        Intent intent = getIntent();
//        User user = (User)intent.getSerializableExtra("MyUser");
        sharedPreferences = getSharedPreferences("currentUser", MODE_PRIVATE);
        String currentLogin = sharedPreferences.getString(PREF_USER_LOGIN, "");
        String currentPassword = sharedPreferences.getString(PREF_USER_PASSWORD,"");
        String currentFirstName = sharedPreferences.getString(PREF_USER_FIRSTNAME, "");
        String currentLastName = sharedPreferences.getString(PREF_USER_LASTNAME, "");
        String currentScore = String.valueOf(sharedPreferences.getInt(PREF_USER_SCORE,0));
        editTextDynamicPrenom.setText(currentFirstName);
        editTextDynamicName.setText(currentLastName);
        editTextDynamicLogin.setText(currentLogin);
        editTextDynamicScore.setText(currentScore);

        buttonGoToQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, QuizLauncherActivity.class);
                startActivity(intent);
            }
        });

//        Navigation : retout a la page accueil
//        buttonUpdateInfoUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editTextDynamicName.setEnabled(true);
//                editTextDynamicPrenom.setEnabled(true);
//                editTextDynamicLogin.setTextColor(Color.GRAY);
//                editTextDynamicScore.setTextColor(Color.GRAY);
//                buttonValidUpdate.setVisibility(View.VISIBLE);
//
////                updateUserInfos();
//            }
//        });

        toggleButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((ToggleButton) v).isChecked();
                if (checked){
                    editTextDynamicName.setEnabled(true);
                    editTextDynamicPrenom.setEnabled(true);
                    editTextDynamicLogin.setTextColor(Color.GRAY);
                    editTextDynamicScore.setTextColor(Color.GRAY);
                    buttonValidUpdate.setVisibility(View.VISIBLE);
                    toggleButtonUpdate.setBackgroundTintList(ContextCompat.getColorStateList(AccountActivity.this,R.color.backTintToggleBtnChecked));
                }else {
                    backToOriginalfields();
                    toggleButtonUpdate.setBackgroundTintList(ContextCompat.getColorStateList(AccountActivity.this,R.color.backTintToggleBtnNotChecked));
                }
            }
        });

        buttonValidUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validUpdatedUserInfos();
                backToOriginalfields();
                toggleButtonUpdate.setChecked(false);
                toggleButtonUpdate.setBackgroundTintList(ContextCompat.getColorStateList(AccountActivity.this,R.color.backTintToggleBtnNotChecked));
            }
        });

        buttonLogOutAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                util.clearPref(sharedPreferences);
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttonDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount(v);
            }
        });

    }
//Methode pour initialiser les champs pre-remplis de user infos
    public void backToOriginalfields(){
        buttonValidUpdate.setVisibility(View.GONE);
        editTextDynamicName.setEnabled(false);
        editTextDynamicPrenom.setEnabled(false);
        editTextDynamicLogin.setTextColor(Color.WHITE);
        editTextDynamicScore.setTextColor(Color.WHITE);
    }

    public void deleteAccount(View view){
//        Intent intent = new Intent(AccountActivity.this, MainActivity.class);
//        startActivity(intent);
//        delete user from database (using userprovider, getContentResolver().delete)
        Toast.makeText(this,"LA SUPPRESSION EST EN COURS D'IMPLEMENTATION...", Toast.LENGTH_LONG).show();
    }

    public void validUpdatedUserInfos(){
        String newFirstName = editTextDynamicPrenom.getText().toString().trim();
        String newLastName = editTextDynamicName.getText().toString().trim();
//        String newPassword = editTextDynamicPassword.getText().toString().trim();

        Uri uri = Uri.parse("content://fr.doranco.quizzomar.UserProvider/users");
        ContentValues values = new ContentValues();
        values.put(MyDataBaseSQLite.KEY_FIRSTNAME, newFirstName);
        values.put(MyDataBaseSQLite.KEY_LASTNAME, newLastName);
        String where = MyDataBaseSQLite.KEY_LOGIN + "=?";
        String currentUser = sharedPreferences.getString(PREF_USER_LOGIN, "");
        String[] whereParam = {currentUser};
        getContentResolver().update(uri, values, where, whereParam);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_USER_FIRSTNAME, newFirstName);
        editor.putString(PREF_USER_LASTNAME, newLastName);
        editor.apply();

        Toast.makeText(this,"Vos Infos ont ete modifiees avec succes ", Toast.LENGTH_LONG).show();
    }
}