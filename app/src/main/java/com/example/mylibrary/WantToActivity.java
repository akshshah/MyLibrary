package com.example.mylibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mylibrary.Model.Book;

import java.util.ArrayList;

public class WantToActivity extends AppCompatActivity implements BooksRecViewAdapter.ChangeStatus {
    private static final String TAG = "WantToActivity";

    @Override
    public void onChangeStatusResult(Book book, String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("status",status);
        try{
            int rowAffected = database.update("books",contentValues,"id=?",new String[]{String.valueOf(book.getId())});
            if(rowAffected > 0){
                Toast.makeText(this, book.getName() + " has been removed from want to read list", Toast.LENGTH_SHORT).show();
                DatabaseAsyncTask databaseAsyncTask = new DatabaseAsyncTask();
                databaseAsyncTask.execute();
            }
        }catch (SQLException e){
            e.printStackTrace();
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    private RecyclerView recyclerView;
    private BooksRecViewAdapter adapter;
    private ProgressBar progressBar;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private Cursor cursor;

    private ArrayList<Book> wanToReadBooks;

    @Override
    protected void onStart() {
        super.onStart();

        adapter = new BooksRecViewAdapter(this,"WantToActivity");
        recyclerView.setLayoutManager(new GridLayoutManager(this,2,RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        wanToReadBooks = new ArrayList<>();

        DatabaseAsyncTask databaseAsyncTask = new DatabaseAsyncTask();
        databaseAsyncTask.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_to);
        getSupportActionBar().setTitle("Want To Read Books");

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recView);

    }


    private class DatabaseAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            adapter.clearAdapter();
            databaseHelper = new DatabaseHelper(WantToActivity.this);
            database = databaseHelper.getReadableDatabase();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                cursor = database.query("books", null, "status=?", new String[] {"wantTo"},
                        null, null,null);

                if(cursor.moveToFirst()){
                    for(int i=0; i<cursor.getCount(); i++){
                        Book book = new Book();
                        for (int j=0; j<cursor.getColumnCount(); j++){
                            switch (cursor.getColumnName(j)){
                                case "id":
                                    book.setId(cursor.getInt(j));
                                    break;
                                case "name":
                                    book.setName(cursor.getString(j));
                                    break;
                                case "author":
                                    book.setAuthor(cursor.getString(j));
                                    break;
                                case "language":
                                    book.setLanguage(cursor.getString(j));
                                    break;
                                case "pages":
                                    book.setPages(cursor.getInt(j));
                                    break;
                                case "description":
                                    book.setDescription(cursor.getString(j));
                                    break;
                                case "imageURL":
                                    book.setImageURL(cursor.getString(j));
                                    break;
                                case "isFavourite":
                                    int isFav = cursor.getInt(j);
                                    if(isFav == 1)
                                        book.setFavourite(true);
                                    else
                                        book.setFavourite(false);
                                    break;
                                case "status":
                                    book.setStatus(cursor.getString(j));
                                    break;
                                default:
                                    break;
                            }
                        }

                        wanToReadBooks.add(book);
                        cursor.moveToNext();
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.setBooks(wanToReadBooks);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        database.close();
    }

}