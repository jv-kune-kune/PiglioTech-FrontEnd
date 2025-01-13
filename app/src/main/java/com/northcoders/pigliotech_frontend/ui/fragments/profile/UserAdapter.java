package com.northcoders.pigliotech_frontend.ui.fragments.profile;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.databinding.BookItemBinding;
import com.northcoders.pigliotech_frontend.model.Book;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<Book> books;

    public UserAdapter(List<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        BookItemBinding binding = BookItemBinding.inflate(
//                LayoutInflater.from(parent.getContext()),
//                parent,
//                false
//        );

        // TODO test the different bindings

        BookItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.book_item,
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

        Glide.with(holder.bookCoverImageView.getContext())
                .load(bookCoverUrl)
                .placeholder(R.drawable.blank_book)
                .error(R.drawable.blank_book)
                .into(holder.bookCoverImageView);

        // TODO Implement the clicked album item
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        private final BookItemBinding bookItemBinding;
        private final ImageView bookCoverImageView;

        public UserViewHolder(BookItemBinding bookItemBinding) {
            super(bookItemBinding.getRoot());
            this.bookItemBinding = bookItemBinding;
            this.bookCoverImageView = bookItemBinding.pfpImage;
        }
    }
}