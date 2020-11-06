package com.example.mylibrary;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.mylibrary.Model.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class AllBooksActivity extends AppCompatActivity implements AddNewBookDialog.AddNewBook, BooksRecViewAdapter.DeleteBook {
    private static final String TAG = "AllBooksActivity";

    @Override
    public void onAddingNewBookResult(Book book) {
        Log.d(TAG, "onAddingNewBookResult: new Book " + book.getName());

        try {
            databaseHelper.insert(database, book);
            DatabaseAsyncTask databaseAsyncTask = new DatabaseAsyncTask();
            databaseAsyncTask.execute();
        }catch (SQLException e){
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeletingResult(int bookId) {
        Log.d(TAG, "onDeletingResult: " + bookId);

        try {
            databaseHelper.delete(database, bookId);
            DatabaseAsyncTask databaseAsyncTask = new DatabaseAsyncTask();
            databaseAsyncTask.execute();
        }catch (SQLException e){
            e.printStackTrace();
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    private RecyclerView recyclerView;
    private BooksRecViewAdapter adapter;
    private ProgressBar progressBar;
    private View contentLayout;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private Cursor cursor;

    private ArrayList<Book> allBooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Books");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            AddNewBookDialog addNewBookDialog = new AddNewBookDialog();
            addNewBookDialog.show(getSupportFragmentManager(),"Add new Book");
        });

        contentLayout = findViewById(R.id.contentLayout);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recView);

        adapter = new BooksRecViewAdapter(this,"AllBooksActivity");
        recyclerView.setLayoutManager(new GridLayoutManager(this,2,RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        allBooks = new ArrayList<>();

        DatabaseAsyncTask databaseAsyncTask = new DatabaseAsyncTask();
        databaseAsyncTask.execute();
    }

    private class DatabaseAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            adapter.clearAdapter();
            databaseHelper = new DatabaseHelper(AllBooksActivity.this);
            database = databaseHelper.getReadableDatabase();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                cursor = database.query("books",null,null,null,null,null,null);

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

                        allBooks.add(book);
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
            adapter.setBooks(allBooks);
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