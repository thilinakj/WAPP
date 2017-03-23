package com.thilinas.twallpapers.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thilinas.twallpapers.R;
import com.thilinas.twallpapers.models.Photo;

import java.util.List;

/**
 * Created by Thilina on 06-Feb-17.
 */

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.MyViewHolder> {

    private List<Photo> photos;
    private Context context;
    private Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        Button btn;
        ImageButton buttonSetWallpaper,btn_like;
        ImageView imageView,img_hidden;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.img);
            buttonSetWallpaper = (ImageButton)view.findViewById(R.id.set);
            img_hidden = (ImageView) view.findViewById(R.id.img_hidden);
            btn_like = (ImageButton)view.findViewById(R.id.like);
         /*   title = (TextView) view.findViewById(R.id.title);
           *//* genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
            btn = (Button) view.findViewById(R.id.btn);*/
        }
    }

    public PhotoListAdapter(List<Photo> games, Context context) {
        this.photos = games;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_photo, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Photo photo = photos.get(position);
        final String url = photo.getUrl();
        Glide
                .with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .placeholder(R.drawable.ic_panorama_black_24dp)
                .crossFade()
                .into(holder.imageView);

        holder.buttonSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onIconsClickedListener != null) {
                    onIconsClickedListener.onSetWallClicked(url);
                }
            }
        });

        holder.btn_like.setClickable(true);
        holder.btn_like.setVisibility(View.VISIBLE);
        holder.btn_like.setImageResource(R.drawable.ic_lover);

        holder.btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onIconsClickedListener != null) {
                    onIconsClickedListener.onItemLiked(photo);
                    holder.btn_like.setClickable(false);
                   // holder.btn_like.setAlpha(5);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public interface OnIconsClickedListener {
        void onSetWallClicked(String deviceAddress);
        void onItemLiked(Photo photo);
    }

    private OnIconsClickedListener onIconsClickedListener;

    public void setOnIconsClickedListener(OnIconsClickedListener l) {
        onIconsClickedListener = l;
    }

}