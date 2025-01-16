package com.northcoders.pigliotech_frontend.ui.fragments.profile;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.databinding.BookItemBinding;
import com.northcoders.pigliotech_frontend.model.Book;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<Book> books;
    private final ProfileViewModel viewModel;

    public UserAdapter(List<Book> books, ProfileViewModel viewModel) {
        this.books = books;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        BookItemBinding binding = BookItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        Book book = books.get(position);
        holder.bookItemBinding.setBook(book);

        String bookCoverUrl = book.getThumbnail();
        Log.i("UserAdapter", "Book Cover URL: " + bookCoverUrl);
        Glide.with(holder.bookCoverImageView.getContext())
                .load(bookCoverUrl)
                .placeholder(R.drawable.blank_book)
                .error(R.drawable.blank_book)
                .into(holder.bookCoverImageView);

        holder.deleteBookButton.setOnClickListener(view -> {
            viewModel.deleteBook(book.getIsbn());
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        private final BookItemBinding bookItemBinding;
        private final ImageView bookCoverImageView;
        private final Button deleteBookButton;

        public UserViewHolder(BookItemBinding bookItemBinding) {
            super(bookItemBinding.getRoot());
            this.bookItemBinding = bookItemBinding;
            this.bookCoverImageView = bookItemBinding.pfpImage;
            this.deleteBookButton = bookItemBinding.buttonDeleteBook;
        }
    }
}