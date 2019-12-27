package com.example.moviedb.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb.R;
import com.example.moviedb.adapters.TopMoviesAdapter;
import com.example.moviedb.models.Result;
import com.example.moviedb.viewmodels.MainViewModel;
import com.example.moviedb.viewmodels.MainViewModelFactory;

public class NowPlayingMoviesFragment extends Fragment {

    private Context context;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_playing_movies, container, false);

        String sort_criteria = "now_playing";
        MainViewModel viewModel = ViewModelProviders.of(this, new MainViewModelFactory(sort_criteria)).get(MainViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.rv_nowPlayingMovies);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        final TopMoviesAdapter adapter = new TopMoviesAdapter(context);
        recyclerView.setAdapter(adapter);

        viewModel.getListLiveData().observe(this, new Observer<PagedList<Result>>() {
            @Override
            public void onChanged(PagedList<Result> results) {
                if(results != null){
                    adapter.submitList(results);
                }
            }
        });

        return view;
    }

}
