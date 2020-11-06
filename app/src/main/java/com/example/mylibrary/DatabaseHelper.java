package com.example.mylibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mylibrary.Model.Book;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bookStore";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: started");

        String sqlCommand = "Create Table books(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "name Text," +
                "author Text," +
                "language Text," +
                "pages INTEGER," +
                "description Text,"+
                "imageURL Text,"+
                "isFavourite INTEGER,"+
                "status Text);";

        db.execSQL(sqlCommand);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void insert(SQLiteDatabase db,Book book){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",book.getName());
        contentValues.put("author",book.getAuthor());
        contentValues.put("language",book.getLanguage());
        contentValues.put("pages",book.getPages());
        contentValues.put("description",book.getDescription());
        contentValues.put("imageURL",book.getImageURL());
        contentValues.put("status",book.getStatus());

        if(book.isFavourite()){
            contentValues.put("isFavourite",1);
        }
        else{
            contentValues.put("isFavourite",0);
        }

        db.insert("books",null,contentValues);
    }

    public void delete(SQLiteDatabase db, int bookId){
        db.delete("books","id=?",new String[] {String.valueOf(bookId)});
    }
}
