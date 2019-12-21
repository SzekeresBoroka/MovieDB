package com.example.moviedb.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.moviedb.MainActivity;
import com.example.moviedb.R;
import com.example.moviedb.models.Result;

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
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        return view;
    }

}
