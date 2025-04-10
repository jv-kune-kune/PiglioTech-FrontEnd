package com.northcoders.pigliotech_frontend.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.northcoders.pigliotech_frontend.R;

public class ImageLoadingUtils {
    private static final String TAG = "ImageLoadingUtils";

    public static void loadImage(Context context, ImageView imageView, String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.blank_pfp);
            return;
        }

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.blank_pfp)
                .error(R.drawable.blank_pfp)
                .listener(new RequestListener<android.graphics.drawable.Drawable>() {
                    @Override
                    public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model,
                            Target<android.graphics.drawable.Drawable> target,
                            boolean isFirstResource) {
                        if (e != null) {
                            Log.e(TAG, "Image load failed: " + e.getMessage());
                            // Only show error toast for non-default images
                            if (!imageUrl.contains("blank_pfp")) {
                                Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model,
                            Target<android.graphics.drawable.Drawable> target,
                            com.bumptech.glide.load.DataSource dataSource,
                            boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }

    public static void loadBookCover(Context context, ImageView imageView, String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.blank_book);
            return;
        }

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.blank_book)
                .error(R.drawable.blank_book)
                .listener(new RequestListener<android.graphics.drawable.Drawable>() {
                    @Override
                    public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model,
                            Target<android.graphics.drawable.Drawable> target,
                            boolean isFirstResource) {
                        if (e != null) {
                            Log.e(TAG, "Book cover load failed: " + e.getMessage());
                            // Only show error toast for non-default covers
                            if (!imageUrl.contains("blank_book")) {
                                Toast.makeText(context, "Failed to load book cover", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model,
                            Target<android.graphics.drawable.Drawable> target,
                            com.bumptech.glide.load.DataSource dataSource,
                            boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }
}