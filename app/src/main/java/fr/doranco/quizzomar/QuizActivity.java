package fr.doranco.quizzomar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements IConstants{
    private static final String TAG = QuizActivity.class.getSimpleName();

    private SharedPreferences sharedPreferences;

    private TextView textViewQuestion;
    private TextView text_view_score;
    private TextView text_view_question_count;
    private TextView textViewCheckResponse;
    private TextView text_view_countdown;
    private TextView textViewMyResponse;
    private RadioGroup radio_group;
    private RadioButton radio_button1;
    private RadioButton radio_button2;
    private RadioButton radio_button3;
    private RadioButton radio_button4;
    private Button button_confirm_next;

    private ColorStateList textColorDefaultRadioButton;

    private List<Question> questionsList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.text_view_question);
        text_view_score = findViewById(R.id.text_view_score);
        text_view_question_count = findViewById(R.id.text_view_question_count);
        textViewCheckResponse = findViewById(R.id.textViewCheckResponse);
        text_view_countdown = findViewById(R.id.text_view_countdown);
        textViewMyResponse = findViewById(R.id.textViewMyResponse);
        radio_group = findViewById(R.id.radio_group);
        radio_button1 = findViewById(R.id.radio_button1);
        radio_button2 = findViewById(R.id.radio_button2);
        radio_button3 = findViewById(R.id.radio_button3);
        radio_button4 = findViewById(R.id.radio_button4);
        button_confirm_next = findViewById(R.id.button_confirm_next);

        textColorDefaultRadioButton = radio_button1.getHintTextColors();

        Uri uri = Uri.parse("content://fr.doranco.quizzomar.QuestionProvider/questions");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        Log.i(TAG,"CURSOR ISIDE QUIZACTIVITY======>>>> " + cursor);
//        if (cursor!= null && cursor.moveToFirst()) {
//            while(!cursor.isAfterLast()) {
//                Question question = new Question();
//                question.setQuestionId(cursor.getInt(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_ID)));
//                question.setQuestion(cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_QUESTION)));
//                question.setOption1(cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_OPTION1)));
//                question.setOption2(cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_OPTION2)));
//                question.setOption3(cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_OPTION3)));
//                question.setOption4(cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_OPTION4)));
//                question.setCorrectAnswerNb(cursor.getInt(cursor.getColumnIndexOrThrow(MyDataBaseSQLite.KEY_CORRECT_ANSWER)));
//                allQuestionsList.add(question);
//                cursor.moveToNext();
//            }
////            Log.i(TAG,"LIST FROM QUEY FROM QUIZACTIVITY=====> "+ allQuestionsList);
//        }
//        Log.i(TAG,"LIST FROM QUEY FROM QUIZACTIVITY=====> "+ allQuestionsList);

        MyDataBaseSQLite db = new MyDataBaseSQLite(this);
        questionsList = db.getAllQuestions();
        Log.i(TAG,"QUESTLIST FROM MYDATABASESQLITE======>>>> " + questionsList.get(0).getOption2());
        questionCountTotal =questionsList.size();
        Collections.shuffle(questionsList);
        Log.i(TAG,"TEST SHUFFLED QUESTIONSLIST RESULT=====>" + questionsList.toString());
        showNextQuestion();
        button_confirm_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered){
                    if (radio_button1.isChecked() || radio_button2.isChecked()|| radio_button3.isChecked() || radio_button4.isChecked()){
                        checkAnswer();
                    }else {
                        Toast.makeText(QuizActivity.this,"Vous devez choisir une reponse !", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    showNextQuestion();
                }
            }
        });

    }

    private void showNextQuestion(){
        radio_button1.setTextColor(textColorDefaultRadioButton);
        radio_button2.setTextColor(textColorDefaultRadioButton);
        radio_button3.setTextColor(textColorDefaultRadioButton);
        radio_button4.setTextColor(textColorDefaultRadioButton);
        textViewMyResponse.setText("");
        textViewMyResponse.setCompoundDrawablesWithIntrinsicBounds( 0,0, 0,0);

        if (questionCounter < questionCountTotal){
            currentQuestion = questionsList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());
            radio_button1.setText(currentQuestion.getOption1());
            radio_button2.setText(currentQuestion.getOption2());
            radio_button3.setText(currentQuestion.getOption3());
            radio_button4.setText(currentQuestion.getOption4());

            questionCounter++;
            text_view_question_count.setText("Question: " + questionCounter + "/" + questionCountTotal );
            answered = false;
            button_confirm_next.setText("Valider");
            textViewCheckResponse.setText("");
        }else{
            finishQuiz();
            updateUserScore();
        }
    }

    private void checkAnswer(){
        answered = true;

        RadioButton selectedRadioButton = findViewById(radio_group.getCheckedRadioButtonId());
        int answerdNumber = radio_group.indexOfChild(selectedRadioButton)+1;

        if (answerdNumber == currentQuestion.getCorrectAnswerNb()){
            score++;
            text_view_score.setText("Score : " + score);
            textViewMyResponse.setText("Bonne Reponse");
            textViewMyResponse.setTextColor(Color.GREEN);
            textViewMyResponse.setCompoundDrawablesWithIntrinsicBounds( 0,0, R.drawable.ic_round_check_circle_24,0);
        } else {
            textViewMyResponse.setText("Mauvaise Reponse");
            textViewMyResponse.setTextColor(Color.RED);
            textViewMyResponse.setCompoundDrawablesWithIntrinsicBounds( 0,0, R.drawable.ic_baseline_highlight_off_24,0);
        }
        showSolution();
    }

    private void showSolution(){
        radio_button1.setTextColor(Color.RED);
        radio_button2.setTextColor(Color.RED);
        radio_button3.setTextColor(Color.RED);
        radio_button4.setTextColor(Color.RED);

        switch (currentQuestion.getCorrectAnswerNb()){
            case 1:
                radio_button1.setTextColor(Color.GREEN);
                textViewCheckResponse.setText("Reponse 1 est correcte");
                break;
            case 2:
                radio_button2.setTextColor(Color.GREEN);
                textViewCheckResponse.setText("Reponse 2 est correcte");
                break;
            case 3:
                radio_button3.setTextColor(Color.GREEN);
                textViewCheckResponse.setText("Reponse 3 est correcte");
                break;
            case 4:
                radio_button4.setTextColor(Color.GREEN);
                textViewCheckResponse.setText("Reponse 4 est correcte");
                break;
        }

        if (questionCounter < questionCountTotal){
            button_confirm_next.setText("Suivant");
        }else {
            button_confirm_next.setText("Finish");
        }
    }

    private void updateUserScore(){
        sharedPreferences = getSharedPreferences("currentUser", MODE_PRIVATE);
        Uri uri = Uri.parse("content://fr.doranco.quizzomar.UserProvider/users");
        ContentValues values = new ContentValues();
        values.put(MyDataBaseSQLite.KEY_SCORE, score);
        String where = MyDataBaseSQLite.KEY_LOGIN + "=?";
        String currentUser = sharedPreferences.getString(PREF_USER_LOGIN, "");
        String[] whereParam = {currentUser};
        getContentResolver().update(uri, values, where, whereParam);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_USER_SCORE, score);
        editor.apply();
//        QuizLauncherActivity.
    }

    private void finishQuiz(){
        finish();
    }
}