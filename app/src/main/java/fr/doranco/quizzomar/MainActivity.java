package fr.doranco.quizzomar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements IConstants{
    private static final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonCreateCompte = findViewById(R.id.buttonCreateCompte);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        sharedPreferences = getSharedPreferences("currentUser", MODE_PRIVATE);
        boolean isUserLoggedIn = sharedPreferences.getBoolean(PREF_USER_LOGIN_STATUS,false);
        if (isUserLoggedIn){
            Intent intent = new Intent(MainActivity.this, QuizLauncherActivity.class);
            startActivity(intent);
        }

        buttonCreateCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "button page creation compte a ete clique");
                Intent intent = new Intent(MainActivity.this, CompteActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "button page login a ete clique");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}