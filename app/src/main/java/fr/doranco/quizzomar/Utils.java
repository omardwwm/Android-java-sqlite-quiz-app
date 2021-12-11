package fr.doranco.quizzomar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class Utils extends AppCompatActivity implements Serializable {

    public Utils(){}
    public void clearPref(SharedPreferences sharedPreferences){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

//    public boolean checkLoginExists(String loginToCheck){
//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//        qb.setTables(UserProvider.TABLE_USERS_NAME);
////        Uri uri = Uri.parse("content://fr.doranco.quizzomar.UserProvider/users/logintoto");
////        String loginToCheck = uri.getPathSegments().get(1).trim();
////        String[] projection = {MyDataBaseSQLite.KEY_LASTNAME, MyDataBaseSQLite.KEY_FIRSTNAME};
//        String where = MyDataBaseSQLite.KEY_LOGIN + "=?";
//        String[] whereParam = {loginToCheck};
//        Cursor c = qb.query(UserProvider., null, where, whereParam, null, null, null);
//        if (!(c == null)){
//            return true;
//        }
//        return false;
//    }
}
