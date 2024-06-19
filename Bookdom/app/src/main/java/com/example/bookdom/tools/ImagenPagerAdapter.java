package com.example.bookdom.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.bookdom.views.LibroActivity;

import java.util.List;

public class ImagenPagerAdapter extends PagerAdapter {

    private List<Bitmap> images;
    private Context context;

    public ImagenPagerAdapter(Context context, List<Bitmap> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(images.get(position));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof LibroActivity) {
                    ((LibroActivity) context).mostrarPopupGaleriaImagenes(images);
                }
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    public void updateImages(List<Bitmap> newImages) {
        this.images = newImages;
        notifyDataSetChanged();
    }
}
