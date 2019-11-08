package com.weiey.app.utils;


import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.weiey.app.R;

import java.io.File;

public class ImageLoader {
    private ImageLoader() {

    }

    private static ImageLoader instance;

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    public void displayImageWithPlaceHolder(ImageView imageView, String url, int placeHolderId) {
//        Glide.with(imageView.getContext()).load(url)
//                .placeholder(placeHolderId).error(placeHolderId).crossFade().into(imageView);

        Glide.with(imageView.getContext()).load(url).apply(new RequestOptions().placeholder(placeHolderId).error(placeHolderId).dontAnimate()).into(imageView);
    }


    public void displayImageResource(Context context, int resId, ImageView imageView) {
//        Glide.with(context).load(resId).error(resId).placeholder(resId).into(imageView);
        Glide.with(imageView.getContext()).load(resId).into(imageView);
    }

    public void displayImage(Context context, String url, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url).apply(new RequestOptions().placeholder(R.drawable.pgl_ic_place_holder_light).error(R.drawable.pgl_ic_place_holder_light).dontAnimate()).into(imageView);
//        Glide.with(context).load(url).error(R.drawable.img_loading)
//                .placeholder(R.drawable.img_loading).into(imageView);
    }

    public void displayImage(Context context, String url, int placeId, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url).apply(new RequestOptions().placeholder(placeId).error(placeId).dontAnimate()).into(imageView);

    }

    public void loadImageFile(File file, ImageView imageView, int placeId) {
//        Glide.with(imageView.getContext()).load(file).placeholder(placeHolderId)
//                .error(R.drawable.img_loading).into(imageView);
        Glide.with(imageView.getContext()).load(file).apply(new RequestOptions().placeholder(placeId).error(placeId).dontAnimate()).into(imageView);
    }


    public void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }
}
