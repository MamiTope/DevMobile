package com.mamitope.projects.devmobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mamitope.projects.devmobile.R;
import com.mamitope.projects.devmobile.models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView picture;
        public TextView title;
        public TextView date;
        public ImageView authorPicture;
        public TextView authorName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.picture);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            authorPicture = itemView.findViewById(R.id.authorPicture);
            authorName = itemView.findViewById(R.id.authorName);
        }
    }

    private Context context;
    private List<Book> books;

    public BookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        Book book = books.get(position);

        holder.title.setText(book.getTitle());
        holder.date.setText(String.valueOf("Published on " + book.getDateOfPublication()));
        holder.authorName.setText(book.getAuthorName());
        Glide.with(context).load(book.getPictureUrl()).into(holder.picture);
        Glide.with(context).load(book.getAuthorPictureUrl()).into(holder.authorPicture);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
