package com.example.mylibrary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mylibrary.Model.Book;

public class BookActivity extends AppCompatActivity {
    private static final String TAG = "BookActivity";

    private TextView bookName,author,language,pages,description;
    private ImageView emptyStar,filledStar,bookImage;
    private Button btnAddCurrent,btnAddWantTo,btnAddAlready;

    private Book incomingBook;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private Cursor cursor;

    private boolean likeHasChanged = false, statusHasChanged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        getSupportActionBar().setTitle("Book");

        initializeViews();
        try {
            Intent intent = getIntent();
            int id = intent.getIntExtra("bookId",-1);
            if(id != -1){
                GetBookByAsyncTask getBookByAsyncTask = new GetBookByAsyncTask();
                getBookByAsyncTask.execute(id);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show();
        }

    }

    private class GetBookByAsyncTask extends AsyncTask<Integer, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: started");
            databaseHelper = new DatabaseHelper(BookActivity.this);
            database = databaseHelper.getReadableDatabase();
            incomingBook = new Book();

        }

        @Override
        protected Void doInBackground(Integer... integers) {
            Log.d(TAG, "doInBackground: started");
            cursor = database.query("books", null, "id=?", new String[] {String.valueOf(integers[0])},
                    null, null,null);
            if(cursor.moveToFirst()){
                for(int i=0; i< cursor.getColumnCount(); i++){
                    switch (cursor.getColumnName(i)){
                        case "id":
                            incomingBook.setId(cursor.getInt(i));
                            break;
                        case "name":
                            incomingBook.setName(cursor.getString(i));
                            break;
                        case "author":
                            incomingBook.setAuthor(cursor.getString(i));
                            break;
                        case "language":
                            incomingBook.setLanguage(cursor.getString(i));
                            break;
                        case "pages":
                            incomingBook.setPages(cursor.getInt(i));
                            break;
                        case "description":
                            incomingBook.setDescription(cursor.getString(i));
                            break;
                        case "imageURL":
                            incomingBook.setImageURL(cursor.getString(i));
                            break;
                        case "isFavourite":
                            int isFav = cursor.getInt(i);
                            if(isFav == 1)
                                incomingBook.setFavourite(true);
                            else
                                incomingBook.setFavourite(false);
                            break;
                        case "status":
                            incomingBook.setStatus(cursor.getString(i));
                            break;
                        default:
                            break;
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "onPostExecute: started");
            super.onPostExecute(aVoid);

            bookName.setText(incomingBook.getName());
            author.setText(incomingBook.getAuthor());
            language.setText(incomingBook.getLanguage());
            pages.setText(String.valueOf(incomingBook.getPages()));
            description.setText(incomingBook.getDescription());

            Glide.with(BookActivity.this)
                    .asBitmap()
                    .load(incomingBook.getImageURL())
                    .into(bookImage);

            if(incomingBook.isFavourite()){
                emptyStar.setVisibility(View.GONE);
                filledStar.setVisibility(View.VISIBLE);
                filledStar.setOnClickListener(v -> {
                    Log.d(TAG, "onPostExecute: filled click");
                    UpdateLikeAsyncTask updateLikeAsyncTask = new UpdateLikeAsyncTask();
                    updateLikeAsyncTask.execute(false);
                });
            }
            else{
                emptyStar.setVisibility(View.VISIBLE);
                filledStar.setVisibility(View.GONE);
                emptyStar.setOnClickListener(v -> {
                    Log.d(TAG, "onPostExecute: empty click");
                    UpdateLikeAsyncTask updateLikeAsyncTask = new UpdateLikeAsyncTask();
                    updateLikeAsyncTask.execute(true);
                });
            }

            btnAddCurrent.setOnClickListener(v -> {
                if(incomingBook.getStatus().equals("current")){
                    AlertDialog.Builder build = new AlertDialog.Builder(BookActivity.this);
                    build.setMessage("Already added in your current reading list");
                    build.setPositiveButton("I Know", (dialogInterface, i) -> {

                    });
                    build.setCancelable(false);
                    build.create().show();
                }
                else if(incomingBook.getStatus().equals("wantTo")){
                    AlertDialog.Builder build = new AlertDialog.Builder(BookActivity.this);
                    build.setMessage("Are you going to start reading this book?");
                    build.setNegativeButton("No", (dialogInterface, i) -> {

                    });
                    build.setPositiveButton("Yes", (dialogInterface, i) -> {
                        UpdateStatusAsyncTask updateStatusAsyncTask = new UpdateStatusAsyncTask();
                        updateStatusAsyncTask.execute("current");
                    });

                    build.setCancelable(false);
                    build.create().show();
                }
                else if(incomingBook.getStatus().equals("alreadyRead")){
                    AlertDialog.Builder build = new AlertDialog.Builder(BookActivity.this);
                    build.setMessage("Do you want to read this book again?");
                    build.setNegativeButton("No", (dialogInterface, i) -> {

                    });
                    build.setPositiveButton("Yes", (dialogInterface, i) -> {
                        UpdateStatusAsyncTask updateStatusAsyncTask = new UpdateStatusAsyncTask();
                        updateStatusAsyncTask.execute("current");
                    });
                    build.setCancelable(false);
                    build.create().show();

                }
                else{
                    UpdateStatusAsyncTask updateStatusAsyncTask = new UpdateStatusAsyncTask();
                    updateStatusAsyncTask.execute("current");
                }
            });

            btnAddWantTo.setOnClickListener(v -> {
                if(incomingBook.getStatus().equals("wantTo")){
                    AlertDialog.Builder build = new AlertDialog.Builder(BookActivity.this);
                    build.setMessage("Already added in your Want To Read list");
                    build.setPositiveButton("I Know", (dialogInterface, i) -> {

                    });
                    build.setCancelable(false);
                    build.create().show();
                }
                else if(incomingBook.getStatus().equals("current")){
                    AlertDialog.Builder build = new AlertDialog.Builder(BookActivity.this);
                    build.setMessage("You are currently reading this book");
                    build.setTitle("Error");
                    build.setPositiveButton("I Know", (dialogInterface, i) -> {

                    });
                    build.setCancelable(false);
                    build.create().show();
                }
                else if(incomingBook.getStatus().equals("alreadyRead")){
                    AlertDialog.Builder build = new AlertDialog.Builder(BookActivity.this);
                    build.setMessage("Do you want to read again and add this book to Want To Read list?");
                    build.setNegativeButton("No", (dialogInterface, i) -> {

                    });
                    build.setPositiveButton("Yes", (dialogInterface, i) -> {
                        UpdateStatusAsyncTask updateStatusAsyncTask = new UpdateStatusAsyncTask();
                        updateStatusAsyncTask.execute("wantTo");
                    });
                    build.setCancelable(false);
                    build.create().show();

                }
                else{
                    UpdateStatusAsyncTask updateStatusAsyncTask = new UpdateStatusAsyncTask();
                    updateStatusAsyncTask.execute("wantTo");
                }
            });

            btnAddAlready.setOnClickListener(v -> {
                if(incomingBook.getStatus().equals("alreadyRead")){
                    AlertDialog.Builder build = new AlertDialog.Builder(BookActivity.this);
                    build.setMessage("Already added in your Already Read list");
                    build.setPositiveButton("I Know", (dialogInterface, i) -> {

                    });
                    build.setCancelable(false);
                    build.create().show();
                }
                else if(incomingBook.getStatus().equals("current")){
                    AlertDialog.Builder build = new AlertDialog.Builder(BookActivity.this);
                    build.setMessage("Have you finish reading this book?");
                    build.setTitle("Error");
                    build.setNegativeButton("No", (dialogInterface, i) -> {

                    });
                    build.setPositiveButton("Yes", (dialogInterface, i) -> {
                        UpdateStatusAsyncTask updateStatusAsyncTask = new UpdateStatusAsyncTask();
                        updateStatusAsyncTask.execute("alreadyRead");
                    });
                    build.setCancelable(false);
                    build.create().show();
                }
                else if(incomingBook.getStatus().equals("wantTo")){
                    AlertDialog.Builder build = new AlertDialog.Builder(BookActivity.this);
                    build.setMessage("You have this book in Want To Read list");
                    build.setTitle("Error");
                    build.setPositiveButton("I Know", (dialogInterface, i) -> {

                    });
                    build.setCancelable(false);
                    build.create().show();

                }
                else{
                    UpdateStatusAsyncTask updateStatusAsyncTask = new UpdateStatusAsyncTask();
                    updateStatusAsyncTask.execute("alreadyRead");
                }
            });

        }
    }

    private class UpdateLikeAsyncTask extends AsyncTask<Boolean, Void, Void>{

        @Override
        protected Void doInBackground(Boolean... booleans) {
            ContentValues contentValues = new ContentValues();
            if(booleans[0]){
                contentValues.put("isFavourite",1);
            }
            else{
                contentValues.put("isFavourite",0);
            }

            try{
                int rowAffected = database.update("books",contentValues,"id=?",new String[]{String.valueOf(incomingBook.getId())});
                if(rowAffected > 0){
                    likeHasChanged = true;
                }
            }catch (SQLException e){
                e.printStackTrace();
                Toast.makeText(BookActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(likeHasChanged){
                GetBookByAsyncTask getBookByAsyncTask = new GetBookByAsyncTask();
                getBookByAsyncTask.execute(incomingBook.getId());
            }
        }
    }

    private class UpdateStatusAsyncTask extends AsyncTask<String, Void, Void>{


        @Override
        protected Void doInBackground(String... strings) {
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("status",strings[0]);
            try{
                int affected = database.update("books",contentValues2,"id=?",new String[]{String.valueOf(incomingBook.getId())});
                if(affected > 0){
                    statusHasChanged = true;
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(statusHasChanged){
                Toast.makeText(BookActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                GetBookByAsyncTask bookByAsyncTask = new GetBookByAsyncTask();
                bookByAsyncTask.execute(incomingBook.getId());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        database.close();
    }

    private void initializeViews() {
        bookName = findViewById(R.id.bookName);
        author = findViewById(R.id.author);
        language = findViewById(R.id.language);
        pages = findViewById(R.id.pages);
        description = findViewById(R.id.description);
        emptyStar = findViewById(R.id.emptyStar);
        filledStar = findViewById(R.id.filledStar);
        bookImage = findViewById(R.id.bookImage);
        btnAddAlready = findViewById(R.id.btnAddAlready);
        btnAddCurrent = findViewById(R.id.btnAddCurrent);
        btnAddWantTo = findViewById(R.id.btnAddWantTo);
    }
}