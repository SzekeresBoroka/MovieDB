package com.example.moviedb.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.moviedb.MainActivity;
import com.example.moviedb.R;
import com.example.moviedb.api.ApiClient;
import com.example.moviedb.api.ApiService;
import com.example.moviedb.constant.Constant;
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

    public MovieDetailsFragment(Result movie) {
        this.movie = movie;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        ((MainActivity) context).setTitle("Movie Details");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        int movie_id = movie.getId();
        String api_key = Constant.API_KEY;
        ApiService service = ApiClient.getInstance().getApiService();
        Call<VideoResponse> call = service.getVideos(movie_id, api_key);
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                List<VideoResult> videos = response.body().getResults();
                String key = "", site = "", videoUrl = "";
                for (VideoResult video: videos) {
                    if(video.getType().equals("Trailer")){
                        key = video.getKey();
                        site = video.getSite();
                        break;
                    }
                }

                switch (site){
                    case "YouTube":{
                        videoUrl = "https://www.youtube.com/watch?v=" + key;
                        break;
                    }
                    case "Vimeo":{
                        videoUrl = "https://vimeo.com/" + key;
                        break;
                    }
                }
                VideoView videoView = view.findViewById(R.id.vv_trailer);
                /*MediaController mediacontroller = new MediaController(context);
                mediacontroller.setAnchorView(videoView);
                //videoView.setVideoPath(videoUrl);
                videoView.setVideoURI(Uri.parse(videoUrl));
                //videoView.start();*/
                final String finalVideoUrl = videoUrl;
                videoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(finalVideoUrl)));
                    }
                });
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {

            }
        });

        TextView tv_title = view.findViewById(R.id.tv_title);
        String title = movie.getTitle() + " (" + movie.getReleaseDate().substring(0,4) + ")";
        tv_title.setText(title);

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

}
