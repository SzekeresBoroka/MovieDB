package com.example.moviedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.moviedb.models.Result;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MovieDB";

    private static final String TABLE_USERS = "Users";
    private static final String USERS_COLUMN_USERNAME = "UserName";
    private static final String USERS_COLUMN_PASSWORD = "Password";

    private static final String TABLE_FAVOURITES = "Favourites";
    private static final String FAVOURITES_COLUMN_ID = "ID";
    private static final String FAVOURITES_COLUMN_USERNAME = "UserName";
    private static final String FAVOURITES_COLUMN_MOVIE_ID = "MovieId";
    private static final String FAVOURITES_COLUMN_MOVIE_TITLE = "MovieTitle";
    private static final String FAVOURITES_COLUMN_MOVIE_DATE = "MovieDate";
    private static final String FAVOURITES_COLUMN_MOVIE_DESCRIPTION = "MovieDescription";
    private static final String FAVOURITES_COLUMN_MOVIE_POSTER = "MoviePoster";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                        USERS_COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                        USERS_COLUMN_PASSWORD + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_FAVOURITES + " (" +
                FAVOURITES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FAVOURITES_COLUMN_USERNAME + " TEXT, " +
                FAVOURITES_COLUMN_MOVIE_ID + " INTEGER, " +
                FAVOURITES_COLUMN_MOVIE_TITLE + " TEXT, " +
                FAVOURITES_COLUMN_MOVIE_DATE + " TEXT, " +
                FAVOURITES_COLUMN_MOVIE_DESCRIPTION + " TEXT," +
                FAVOURITES_COLUMN_MOVIE_POSTER + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        onCreate(db);
    }

    public boolean insertUser(String userName, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_USERNAME, userName);
        contentValues.put(USERS_COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS,null, contentValues);
        if (result == -1)
            return  false;
        else
            return true;
    }

    public boolean userExists(String userName){
        SQLiteDatabase db = this.getWritableDatabase();

        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);

        db.execSQL("CREATE TABLE " + TABLE_FAVOURITES + " (" +
                FAVOURITES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FAVOURITES_COLUMN_USERNAME + " TEXT, " +
                FAVOURITES_COLUMN_MOVIE_ID + " INTEGER, " +
                FAVOURITES_COLUMN_MOVIE_TITLE + " TEXT, " +
                FAVOURITES_COLUMN_MOVIE_DATE + " TEXT, " +
                FAVOURITES_COLUMN_MOVIE_DESCRIPTION + " TEXT," +
                FAVOURITES_COLUMN_MOVIE_POSTER + " TEXT);");*/

        Cursor res = db.rawQuery("select 1 from " + TABLE_USERS + " where " + USERS_COLUMN_USERNAME + " = \"" + userName + "\"",null);
        if(res.getCount()==0) {
            return false;
        }
        else {
            return true;
        }

    }

    public String getPassword(String userName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + USERS_COLUMN_PASSWORD +" from " + TABLE_USERS + " where " + USERS_COLUMN_USERNAME + " = \"" + userName + "\"",null);
        if(res.getCount()==0) {
            return "";
        }
        else {
            res.moveToNext();
            return res.getString(0);
        }
    }

    public boolean changeUsername(String oldUsername, String newUsername){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_USERNAME, newUsername);

        long result = db.update(TABLE_USERS, contentValues, USERS_COLUMN_USERNAME + " = \"" + oldUsername + "\"", null);
        if (result == -1)
            return  false;
        else
            return true;
    }

    public boolean changePassword(String username, String newPassword){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_PASSWORD, newPassword);

        long result = db.update(TABLE_USERS, contentValues, USERS_COLUMN_USERNAME + " = \"" + username + "\"", null);
        if (result == -1)
            return  false;
        else
            return true;
    }

    public boolean saveMovie(String userName, Result movie){
        SQLiteDatabase db = this.getWritableDatabase();

        if(movieIsSaved(userName, movie.getId())){
            Log.d("ellenorzes: ", "movie deleted");
            db.execSQL("delete from " + TABLE_FAVOURITES + " where " + FAVOURITES_COLUMN_USERNAME + " = \"" + userName + "\" and " + FAVOURITES_COLUMN_MOVIE_ID + " = " + movie.getId());
            return true;
        }

        Log.d("ellenorzes: ", "movie saved");

        ContentValues contentValues = new ContentValues();
        contentValues.put(FAVOURITES_COLUMN_USERNAME, userName);
        contentValues.put(FAVOURITES_COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(FAVOURITES_COLUMN_MOVIE_TITLE, movie.getTitle());
        contentValues.put(FAVOURITES_COLUMN_MOVIE_DATE, movie.getReleaseDate());
        contentValues.put(FAVOURITES_COLUMN_MOVIE_DESCRIPTION, movie.getOverview());
        contentValues.put(FAVOURITES_COLUMN_MOVIE_POSTER, movie.getBackdropPath());

        long result = db.insert(TABLE_FAVOURITES,null, contentValues);
        if (result == -1)
            return  false;
        else
            return true;
    }

    public boolean movieIsSaved(String userName, int movieId){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select 1 from " + TABLE_FAVOURITES + " where " + FAVOURITES_COLUMN_USERNAME + " = \"" + userName + "\" and " + FAVOURITES_COLUMN_MOVIE_ID + " = " + movieId,null);
        if(res.getCount()==0) {
            return false;
        }
        else {
            return true;
        }
    }

    public ArrayList<Result> getFavourites(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Result> favourites = new ArrayList<>();
        Cursor res = db.rawQuery("select * from " + TABLE_FAVOURITES + " where " + FAVOURITES_COLUMN_USERNAME + " = \"" + username + "\"",null);
        while(res.moveToNext()){
            Result movie = new Result();
            movie.setId(res.getInt(2));
            movie.setTitle(res.getString(3));
            movie.setReleaseDate(res.getString(4));
            movie.setOverview(res.getString(5));
            movie.setBackdropPath(res.getString(6));
            favourites.add(movie);
        }
        return favourites;
    }
}
