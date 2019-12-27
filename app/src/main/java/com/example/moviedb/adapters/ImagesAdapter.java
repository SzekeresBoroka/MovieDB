package com.example.moviedb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb.MainActivity;
import com.example.moviedb.R;
import com.example.moviedb.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.moviedb.constant.Constant.IMAGE_SIZE;
import static com.example.moviedb.constant.Constant.IMAGE_URL;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder>{
    private List<ImageResult> images;
    private Context context;
    private View rootView;

    public ImagesAdapter(List<ImageResult> images, Context context, View rootView) {
        this.images = images;
        this.context = context;
        this.rootView = rootView;
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

        final String imageUrl = IMAGE_URL + IMAGE_SIZE + image.getFilePath();
        Picasso.get().load(imageUrl).into(viewHolder.image);

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImageButton imgBtn_closeImage = rootView.findViewById(R.id.imgBtn_closeImage);
                final ImageView iv_fullImage = rootView.findViewById(R.id.iv_fullImage);

                ((MainActivity) context).getSupportActionBar().hide();
                imgBtn_closeImage.setVisibility(View.VISIBLE);
                iv_fullImage.setVisibility(View.VISIBLE);
                Picasso.get().load(imageUrl).into(iv_fullImage);

                imgBtn_closeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgBtn_closeImage.setVisibility(View.GONE);
                        iv_fullImage.setVisibility(View.GONE);
                        ((MainActivity) context).getSupportActionBar().show();
                    }
                });
            }
        });
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
