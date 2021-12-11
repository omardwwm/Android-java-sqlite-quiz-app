package fr.doranco.quizzomar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class MyDataBaseSQLite extends SQLiteOpenHelper {

//    CREATION DE LA BASE DE DONNEES DE TTE L'APPLICATION
    static final String DATABASE_NAME = "QuizDB";
    static final int DATABASE_VERSION = 2;
    private SQLiteDatabase dbase;

// CREATION DE LA TABLE DES USERS
    public static final String TABLE_USERS_NAME = "Users";
    public static final String KEY_ROWID = "id";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_SCORE = "score";

    static final String SCRIPT_CREATE_TABLE_USERS =
            "CREATE TABLE if not exists " + TABLE_USERS_NAME + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_FIRSTNAME + " TEXT NOT NULL, " +
                    KEY_LASTNAME + " TEXT NOT NULL, " +
                    KEY_LOGIN + " TEXT NOT NULL, " +
                    KEY_PASSWORD + " TEXT NOT NULL, " +
                    KEY_SCORE + " INTEGER DEFAULT NULL);";

//    CREATION DE LA TABLE DES QUESTIONS
    public static final String TABLE_QUESTIONS_NAME = "Questions";
    public static final String KEY_ID = "question_id";
    public static final String KEY_QUESTION = "question";
    public static final String KEY_OPTION1 = "option1";
    public static final String KEY_OPTION2 = "option2";
    public static final String KEY_OPTION3 = "option3";
    public static final String KEY_OPTION4 = "option4";
    public static final String KEY_CORRECT_ANSWER = "answer_nbr";

    static final String SCRIPT_CREATE_TABLE_QUESTIONS =
            "CREATE TABLE if not exists " + TABLE_QUESTIONS_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_QUESTION + " TEXT NOT NULL, " +
                    KEY_OPTION1 + " TEXT, " +
                    KEY_OPTION2 + " TEXT, " +
                    KEY_OPTION3 + " TEXT, " +
                    KEY_OPTION4 + " TEXT, " +
                    KEY_CORRECT_ANSWER + " INTEGER NOT NULL);";

    protected MyDataBaseSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.dbase = db;
        db.execSQL(SCRIPT_CREATE_TABLE_USERS);
        db.execSQL(SCRIPT_CREATE_TABLE_QUESTIONS);
//        test other way to do (autre que contentProvider)
        insertQuestions();
    }
//    ================================================================>>>>>>
private void insertQuestions()
{
    Question q1=new Question("Pour empêcher de redéfinir une méthode on la déclare","exclusive", "static ", "final", "private",3);
    this.addQuestion(q1);
    Question q2=new Question("Quel opérateur sert pour la concaténation des chaines de caractères ?", "&&", "AND", "+", "&",3);
    this.addQuestion(q2);
    Question q3=new Question("Quel mot clé utiliser pour empêcher d'instancier une classe mère tout en permettant l'héritage ?","Abstract", "Protected","Private", "Global",1 );
    this.addQuestion(q3);
    Question q4=new Question("Dane une classe, un attribut declare sans preciser son niveau de visibilite est visible_____", "Uniquement dans cette classe", "Dans cette classe et celles qui en heritent", "Partout","Dans les classes du meme package",4);
    this.addQuestion(q4);
    Question q5=new Question("Dans une class, u attribut 'static'-----","Est constant","Ne peut etre herite","Ne peut etre accede depuis une autre classe","Possede la mm valeur pour toute les instance de la classe",4);
    this.addQuestion(q5);
        Question q6=new Question("Que peut-on ajouter a un bloc 'try-catch'","After","Finally","Whatever","Aucun des trois",2);
    this.addQuestion(q6);
    Question q7=new Question("Par convention, le nom d'un package","commence par une majuscule","commence par une minuscule","est en minuscules","est en majuscules",3);
    this.addQuestion(q7);
    Question q8=new Question("Dans \"throws ClasseA\", ClasseA","perd son héritage","change de portée","gère une exception","est détruite",3);
    this.addQuestion(q8);
    Question q9=new Question("Le mot clé \"super\" permet ","de définir une classe prioritaire","d'accéder aux membres d'une classe mère","de définir une méthode prioritaire","de donner les droits d'accès super-user à un fichier",2);
    this.addQuestion(q9);
    Question q10=new Question("Le constructeur d'une classe doit porter le même nom que la classe","non, c'est interdit","pas obligatoirement","c'est déconseillé","oui, impérativement",4);
    this.addQuestion(q10);
}
//======================================================================>>>>>>>>>>>
// Adding new question
public void addQuestion(Question quest) {
    //SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(KEY_QUESTION, quest.getQuestion());
    values.put(KEY_OPTION1, quest.getOption1());
    values.put(KEY_OPTION2, quest.getOption2());
    values.put(KEY_OPTION3, quest.getOption3());
    values.put(KEY_OPTION4, quest.getOption4());
    values.put(KEY_CORRECT_ANSWER, quest.getCorrectAnswerNb());
    // Inserting Row
    dbase.insert(TABLE_QUESTIONS_NAME, null, values);
}

    public List<Question> getAllQuestions() {
        List<Question> quesList = new ArrayList<Question>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTIONS_NAME;
        dbase=this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Question quest = new Question();
                quest.setQuestionId(cursor.getInt(0));
                quest.setQuestion(cursor.getString(1));
                quest.setOption1(cursor.getString(2));
                quest.setOption2(cursor.getString(3));
                quest.setOption3(cursor.getString(4));
                quest.setOption4(cursor.getString(5));
                quest.setCorrectAnswerNb(cursor.getInt(6));
                quesList.add(quest);
            } while (cursor.moveToNext());
        }
        // return quest list
        return quesList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS_NAME);
        onCreate(db);
    }
}
