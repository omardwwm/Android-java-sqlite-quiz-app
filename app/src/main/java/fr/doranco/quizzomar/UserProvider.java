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
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class UserProvider extends ContentProvider {
    private static final String TAG = UserProvider.class.getSimpleName();

    private static SQLiteDatabase db;
//    public SQLiteDatabase getDb(){
//        return db;
//    }
    static final String TABLE_USERS_NAME = "Users";
    static final String PROVIDER_NAME = "fr.doranco.quizzomar.UserProvider";
    static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/" + TABLE_USERS_NAME);

    static final int URICODE_ALLUSERS = 1;
    static final int URICODE_SINGLEUSER = 2;
    static final int LOGIN_TOCHECK= 3;
    static final UriMatcher uriMatcher;
    private static HashMap<String, String> values;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "users", URICODE_ALLUSERS);
        uriMatcher.addURI(PROVIDER_NAME, "users/*", URICODE_SINGLEUSER);
//        uriMatcher.addURI(PROVIDER_NAME, "users/*", LOGIN_TOCHECK);
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
        qb.setTables(TABLE_USERS_NAME);

        switch (uriMatcher.match(uri)) {
            case URICODE_ALLUSERS:
                qb.setProjectionMap(values);
                break;
            case URICODE_SINGLEUSER:
//                String id = uri.getPathSegments().get(1);
                String test = uri.getLastPathSegment();
                qb.appendWhere(MyDataBaseSQLite.KEY_LOGIN + "=" + test);
                break;
//            case LOGIN_TOCHECK:
//                String loginToCheck = uri.getPathSegments().get(1);
//                qb.appendWhere(KEY_LOGIN + "=" + loginToCheck);
//                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = MyDataBaseSQLite.KEY_ROWID;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URICODE_ALLUSERS:
                return "vnd.android.cursor.dir/users";
            case URICODE_SINGLEUSER:
                return "vnd.android.cursor.item/users";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(TABLE_USERS_NAME, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLiteException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case URICODE_ALLUSERS:
                count = db.delete(TABLE_USERS_NAME, selection, selectionArgs);
                break;
            case URICODE_SINGLEUSER:
                String id = uri.getPathSegments().get(1);
                count = db.delete(TABLE_USERS_NAME, MyDataBaseSQLite.KEY_ROWID + " = " + id +
                        (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case URICODE_ALLUSERS:
                count = db.update(TABLE_USERS_NAME, values, selection, selectionArgs);
                break;
            case URICODE_SINGLEUSER:
                count = db.update(TABLE_USERS_NAME, values,
                        MyDataBaseSQLite.KEY_ROWID + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ?
                                        " AND (" +selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public boolean checkLoginExists(String loginToCheck){
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_USERS_NAME);
//        Uri uri = Uri.parse("content://fr.doranco.quizzomar.UserProvider/users/logintoto");
//        String loginToCheck = uri.getPathSegments().get(1).trim();
//        String[] projection = {MyDataBaseSQLite.KEY_LASTNAME, MyDataBaseSQLite.KEY_FIRSTNAME};
        String where = MyDataBaseSQLite.KEY_LOGIN + "=?";
        String[] whereParam = {loginToCheck};
        Cursor c = qb.query(db, null, where, whereParam, null, null, null);
        Log.i(TAG,"CURSOR FROM USERPRROVIDER CHERCK METHOD" + c);
        if (c!= null && c.moveToFirst()){
            return true;
        }
            return false;
    }

}
