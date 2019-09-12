package com.mamitope.projects.devmobile.payloads;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mamitope.projects.devmobile.models.Book;

import java.util.List;

public class BookResponse {

    @SerializedName("docs")
    @Expose
    List<Book> books;

    public BookResponse() {
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
