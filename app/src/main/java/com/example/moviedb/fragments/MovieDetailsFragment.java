package com.example.moviedb.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb.MainActivity;
import com.example.moviedb.R;
import com.example.moviedb.adapters.ImagesAdapter;
import com.example.moviedb.adapters.SimilarMoviesAdapter;
import com.example.moviedb.api.ApiClient;
import com.example.moviedb.api.ApiService;
import com.example.moviedb.constant.Constant;
import com.example.moviedb.models.ImageResponse;
import com.example.moviedb.models.ImageResult;
import com.example.moviedb.models.MovieResponse;
import com.example.moviedb.models.Result;
import com.example.moviedb.models.VideoResponse;
import com.example.moviedb.models.VideoResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsFragment extends Fragment {
    private Result movie;
    private Context context;
    private String title;

    public MovieDetailsFragment(Result movie) {
        this.movie = movie;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        title = movie.getTitle() + " (" + movie.getReleaseDate().substring(0,4) + ")";
        ((MainActivity) context).setTitle(title);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        TextView tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText(title);

        TextView tv_description = view.findViewById(R.id.tv_description);
        tv_description.setText(movie.getOverview());

        int movie_id = movie.getId();
        String api_key = Constant.API_KEY;
        ApiService service = ApiClient.getInstance().getApiService();

        Call<VideoResponse> call = service.getVideos(movie_id, api_key);
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                List<VideoResult> videos = response.body().getResults();
                String key = "";
                for (VideoResult video: videos) {
                    if(video.getType().equals("Trailer")){
                        key = video.getKey();
                        break;
                    }
                }

                WebView displayYoutubeVideo = view.findViewById(R.id.vv_trailer);
                displayYoutubeVideo.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }
                });
                WebSettings webSettings = displayYoutubeVideo.getSettings();
                webSettings.setJavaScriptEnabled(true);
                displayYoutubeVideo.loadUrl("https://www.youtube.com/embed/" + key);

            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {

            }
        });

        final RecyclerView rv_images = view.findViewById(R.id.rv_images);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        rv_images.setLayoutManager(layoutManager);
        rv_images.setHasFixedSize(true);

        Call<ImageResponse> call_images = service.getImages(movie_id, api_key);
        call_images.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                List<ImageResult> images = response.body().getBackdrops();

                ImagesAdapter adapter = new ImagesAdapter(images, context, view);
                rv_images.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {

            }
        });

        final RecyclerView rv_similar_movies = view.findViewById(R.id.rv_similarMovies);
        LinearLayoutManager layoutManager_similar_movies = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        rv_similar_movies.setLayoutManager(layoutManager_similar_movies);
        rv_similar_movies.setHasFixedSize(true);

        Call<MovieResponse> call_similar_movies = service.getSimilarMovies(movie_id, api_key);
        call_similar_movies.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Result> movies = response.body().getResults();

                SimilarMoviesAdapter adapter = new SimilarMoviesAdapter(movies, context);
                rv_similar_movies.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });

        ImageButton imgBtn_close = view.findViewById(R.id.imgBtn_close);
        imgBtn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContainerWithMenuFragment containerWithMenuFragment = new ContainerWithMenuFragment();
                Bundle args = new Bundle();
                args.putString("fragmentToOpen", "top_movies");
                containerWithMenuFragment.setArguments(args);

                FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                frag_trans.replace(R.id.fragment_container_without_menu, containerWithMenuFragment);
                frag_trans.commit();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_details_top_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        ((MainActivity) context).setTitle(title);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.close: {
                ContainerWithMenuFragment containerWithMenuFragment = new ContainerWithMenuFragment();
                Bundle args = new Bundle();
                args.putString("fragmentToOpen", "top_movies");
                containerWithMenuFragment.setArguments(args);

                FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                frag_trans.replace(R.id.fragment_container_without_menu, containerWithMenuFragment);
                frag_trans.commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
