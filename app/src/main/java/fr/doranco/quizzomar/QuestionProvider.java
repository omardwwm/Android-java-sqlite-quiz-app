package fr.doranco.quizzomar;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class QuestionProvider extends ContentProvider {
    private static SQLiteDatabase db;

    static final String TABLE_QUESTIONS_NAME = "Questions";
    static final String PROVIDER_NAME = "fr.doranco.quizzomar.QuestionProvider";
    static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/" + TABLE_QUESTIONS_NAME);

    static final int URICODE_ALLQUESTIONS = 4;
    static final UriMatcher uriMatcher;
    private static HashMap<String, String> values;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "questions", URICODE_ALLQUESTIONS);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        MyDataBaseSQLite dbHelper = new MyDataBaseSQLite(context);
        db = dbHelper.getWritableDatabase();
        if (db != null) {
            return true;
        }
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_QUESTIONS_NAME);
        switch (uriMatcher.match(uri)) {
            case URICODE_ALLQUESTIONS:
                qb.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = MyDataBaseSQLite.KEY_ID;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }


    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URICODE_ALLQUESTIONS:
                return "vnd.android.cursor.dir/questions";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Question q1=new Question("If permissions are missing the application will get this at runtime","Parser", "SQLiteOpenHelper ", "Security Exception", "Other",2);
//        ContentValues values = new ContentValues();
        values.put(MyDataBaseSQLite.KEY_QUESTION, q1.getQuestion());
        values.put(MyDataBaseSQLite.KEY_CORRECT_ANSWER, q1.getCorrectAnswerNb());
        values.put(MyDataBaseSQLite.KEY_OPTION1, q1.getOption1());
        values.put(MyDataBaseSQLite.KEY_OPTION2, q1.getOption2());
        values.put(MyDataBaseSQLite.KEY_OPTION3, q1.getOption3());
        values.put(MyDataBaseSQLite.KEY_OPTION4, q1.getOption4());
        long rowID = db.insert(TABLE_QUESTIONS_NAME, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLiteException("Failed to add a record into " + uri);
    }

    @Override
    public int delete( Uri uri,  String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
