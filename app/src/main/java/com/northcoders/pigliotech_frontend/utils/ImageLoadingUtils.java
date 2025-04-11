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

    private ImageLoadingUtils() {
        // Private constructor to hide implicit public one
    }

    private static void loadImageWithFallback(Context context, ImageView imageView, String imageUrl,
            int fallbackResource, String errorPrefix, String fallbackIdentifier) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(fallbackResource);
            return;
        }

        Glide.with(context)
                .load(imageUrl)
                .placeholder(fallbackResource)
                .error(fallbackResource)
                .listener(new RequestListener<android.graphics.drawable.Drawable>() {
                    @Override
                    public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model,
                            Target<android.graphics.drawable.Drawable> target,
                            boolean isFirstResource) {
                        if (e != null) {
                            Log.e(TAG, errorPrefix + " load failed: " + e.getMessage());
                            // Only show error toast for non-default images
                            if (!imageUrl.contains(fallbackIdentifier)) {
                                Toast.makeText(context, "Failed to load " + errorPrefix.toLowerCase(),
                                        Toast.LENGTH_SHORT).show();
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

    public static void loadImage(Context context, ImageView imageView, String imageUrl) {
        loadImageWithFallback(context, imageView, imageUrl, R.drawable.blank_pfp, "Image", "blank_pfp");
    }

    public static void loadBookCover(Context context, ImageView imageView, String imageUrl) {
        loadImageWithFallback(context, imageView, imageUrl, R.drawable.blank_book, "Book cover", "blank_book");
    }
}