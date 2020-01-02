package com.example.moviedb.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb.DbHelper;
import com.example.moviedb.MainActivity;
import com.example.moviedb.R;
import com.example.moviedb.fragments.MovieDetailsFragment;
import com.example.moviedb.models.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.moviedb.constant.Constant.IMAGE_SIZE;
import static com.example.moviedb.constant.Constant.IMAGE_URL;

public class FavouriteMoviesAdapter extends RecyclerView.Adapter<FavouriteMoviesAdapter.MyViewHolder>{
    private List<Result> movies;
    private Context context;

    public FavouriteMoviesAdapter(List<Result> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public FavouriteMoviesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.items_rv_top_movies, viewGroup, false);
        return new FavouriteMoviesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavouriteMoviesAdapter.MyViewHolder viewHolder, final int i) {
        final Result movie = movies.get(i);

        String imageUrl = IMAGE_URL + IMAGE_SIZE + movie.getBackdropPath();
        Picasso.get().load(imageUrl).into(viewHolder.iv_poster);

        String title =  (i + 1) + ". " + movie.getTitle() + " (" + movie.getReleaseDate().substring(0,4) + ")";
        viewHolder.tv_title.setText(title);

        String shortDescription;
        try {
            shortDescription = movie.getOverview().substring(0, 100) + "...";
        } catch (Exception e){
            shortDescription = movie.getOverview();
        }
        viewHolder.tv_description.setText(shortDescription);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                frag_trans.replace(R.id.fragment_container_without_menu, new MovieDetailsFragment(movie, "Saved"));
                frag_trans.commit();
            }
        });

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        final String username = sharedPref.getString(context.getString(R.string.active_user),"Active User");
        final DbHelper db = new DbHelper(context);

        if(db.movieIsSaved(username, movie.getId())){
            viewHolder.imgBtn_like.setColorFilter(Color.rgb(0,0,0));
        }
        else{
            viewHolder.imgBtn_like.setColorFilter(Color.rgb(192,192,192));
        }

        viewHolder.imgBtn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.movieIsSaved(username, movie.getId())){
                    viewHolder.imgBtn_like.setColorFilter(Color.rgb(192,192,192));

                    db.saveMovie(username, movie);
                    movies.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, movies.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_poster;
        TextView tv_title, tv_description;
        ImageButton imgBtn_like;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_poster = itemView.findViewById((R.id.iv_poster));
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);
            imgBtn_like = itemView.findViewById(R.id.imgBtn_like);
        }
    }
}
