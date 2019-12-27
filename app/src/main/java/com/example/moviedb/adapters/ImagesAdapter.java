package com.example.moviedb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb.R;
import com.example.moviedb.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.moviedb.constant.Constant.IMAGE_SIZE;
import static com.example.moviedb.constant.Constant.IMAGE_URL;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder>{
    private List<ImageResult> images;
    private Context context;

    public ImagesAdapter(List<ImageResult> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ImagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.items_rv_images, viewGroup, false);
        return new ImagesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImagesAdapter.MyViewHolder viewHolder, int i) {
        ImageResult image = images.get(i);

        String imageUrl = IMAGE_URL + IMAGE_SIZE + image.getFilePath();
        Picasso.get().load(imageUrl).into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById((R.id.image));
        }
    }
}
