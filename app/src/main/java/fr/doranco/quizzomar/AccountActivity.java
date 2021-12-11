package fr.doranco.quizzomar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AccountActivity extends AppCompatActivity implements IConstants{
    private static final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        EditText editTextDynamicName = findViewById(R.id.editTextDynamicName);
        EditText editTextDynamicPrenom = findViewById(R.id.editTextDynamicPrenom);
        EditText editTextDynamicLogin = findViewById(R.id.editTextDynamicLogin);
        EditText editTextDynamicScore = findViewById(R.id.editTextDynamicScore);

        Button buttonGoToQuiz = findViewById(R.id.buttonGoToQuiz);
        Button buttonDeleteAccount = findViewById(R.id.buttonDeleteAccount);
        Button buttonUpdateInfoUser = findViewById(R.id.buttonUpdateInfoUser);

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

//        Navigation : retout a la page accueil
        buttonUpdateInfoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfos();
//                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });

        buttonGoToQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, QuizLauncherActivity.class);
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

    public void deleteAccount(View view){
//        Intent intent = new Intent(AccountActivity.this, MainActivity.class);
//        startActivity(intent);
//        delete user from database (using userprovider, getContentResolver().delete)
        Toast.makeText(this,"LA SUPPRESSION EST EN COURS D'IMPLEMENTATION...", Toast.LENGTH_LONG).show();
    }

    public void updateUserInfos(){
        Toast.makeText(this,"CETTE METHODE EST EN COURS D'IMPLEMENTATION...", Toast.LENGTH_LONG).show();
    }
}