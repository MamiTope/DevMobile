package com.mamitope.projects.devmobile.activities;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mamitope.projects.devmobile.R;
import com.mamitope.projects.devmobile.components.CircularImageView;
import com.mamitope.projects.devmobile.models.Book;

public class BookDetailsActivity extends AppCompatActivity {

    private Book book;

    private CollapsingToolbarLayout toolbarLayout;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ImageView picture;
    private TextView description;
    private TextView date;
    private CircularImageView authorPicture;
    private TextView authorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        book = intent.getParcelableExtra("BOOK");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        toolbarLayout = findViewById(R.id.toolbarLayout);
        toolbarLayout.setTitleEnabled(true);

        picture = findViewById(R.id.picture);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
        authorPicture = findViewById(R.id.authorPicture);
        authorName = findViewById(R.id.authorName);

        updateUI();
    }

    private void updateUI() {
        if (book != null) {
            toolbarLayout.setTitle(book.getTitle());
            toolbarTitle.setText(book.getTitle());
            description.setText(book.getDescription());
            date.setText(String.valueOf("Published on " + book.getDateOfPublication()));
            authorName.setText(book.getAuthorName());
            Glide.with(BookDetailsActivity.this).load(book.getPictureUrl()).into(picture);
            Glide.with(BookDetailsActivity.this).load(book.getAuthorPictureUrl()).into(authorPicture);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
