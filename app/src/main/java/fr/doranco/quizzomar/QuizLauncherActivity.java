package fr.doranco.quizzomar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizLauncherActivity extends AppCompatActivity implements IConstants{
    private static final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_launcher);

        sharedPreferences = getSharedPreferences("currentUser",MODE_PRIVATE);
        String lastScore = String.valueOf(sharedPreferences.getInt(PREF_USER_SCORE,0));

        TextView text_view_highscore = findViewById(R.id.text_view_highscore);
        Button buttonBackAccount = findViewById(R.id.buttonBackAccount);
        Button button_start_quiz = findViewById(R.id.button_start_quiz);
        Button buttonDeconnexionQuizLayout = findViewById(R.id.buttonDeconnexionQuizLayout);

        text_view_highscore.setText("Votre Dernier Score: " + lastScore + "/10");
        button_start_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizLauncherActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });

        buttonBackAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizLauncherActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        buttonDeconnexionQuizLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPref(v);
//                Utils util = new Utils();
//                util.clearPref(v);
//                Intent intent = new Intent(QuizLauncherActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });
    }

    public void clearPref(View view){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(QuizLauncherActivity.this, MainActivity.class);
        startActivity(intent);
    }
}