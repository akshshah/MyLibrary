package com.example.mylibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button seeAll,current,wantTo,alreadyRead,favourites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        onClick();
    }

    private void onClick(){

        seeAll.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,AllBooksActivity.class);
            startActivity(intent);
        });

        current.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,CurrentlyReadingActivity.class);
            startActivity(intent);
        });

        alreadyRead.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,AlreadyReadActivity.class);
            startActivity(intent);
        });

        wantTo.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,WantToActivity.class);
            startActivity(intent);
        });

        favourites.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,FavouritesActivity.class);
            startActivity(intent);
        });

    }

    private void initialize()
    {
        seeAll = findViewById(R.id.seeAll);
        current = findViewById(R.id.current);
        wantTo = findViewById(R.id.wantTo);
        alreadyRead = findViewById(R.id.alreadyRead);
        favourites = findViewById(R.id.favourites);
    }
}