package com.mamitope.projects.devmobile.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mamitope.projects.devmobile.R;
import com.mamitope.projects.devmobile.adapters.BookAdapter;
import com.mamitope.projects.devmobile.database.SessionManager;
import com.mamitope.projects.devmobile.models.Book;
import com.mamitope.projects.devmobile.payloads.BookResponse;
import com.mamitope.projects.devmobile.utils.ConnectivityManager;
import com.mamitope.projects.devmobile.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private SessionManager sessionManager;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private List<Book> books;
    private BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = SessionManager.getInstance(getApplicationContext());
        sessionManager.checkLoggedIn();
        if (!sessionManager.isLoggedIn()) {
            finish();
            return;
        }

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(R.string.title_main);
        setSupportActionBar(toolbar);

        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);

        initSwipeRefresh();
        initRecyclerView();

        // subscribe to loading state
        viewModel.isLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    refreshLayout.setRefreshing(aBoolean);
                }
            }
        });

        // subscribe to subjects
        viewModel.getBookResponse().observe(this, new Observer<BookResponse>() {
            @Override
            public void onChanged(@Nullable BookResponse response) {
                if (response != null && response.getBooks() != null) {
                    insertData(response.getBooks());
                }
            }
        });

        // init
        if (!ConnectivityManager.checkInternetConnection(MainActivity.this)) {
            showToast(getString(R.string.internet));
            return;
        }
        viewModel.loadBooks(MainActivity.this);
    }

    private void initSwipeRefresh() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        if (!ConnectivityManager.checkInternetConnection(MainActivity.this)) {
            refreshLayout.setRefreshing(false);
            showToast(getString(R.string.internet));
            return;
        }

        books.clear();
        adapter.notifyDataSetChanged();
        viewModel.loadBooks(MainActivity.this);
    }


    private void initRecyclerView() {
        books = new ArrayList<>();
        adapter = new BookAdapter(this, books);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, BookDetailsActivity.class);
                intent.putExtra("BOOK", books.get(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void insertData(List<Book> books) {
        this.books.addAll(books);
        if (this.books.size() == 0) {
            showToast(getString(R.string.no_data_found));
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            sessionManager.signOut();
        }

        return super.onOptionsItemSelected(item);
    }
}
