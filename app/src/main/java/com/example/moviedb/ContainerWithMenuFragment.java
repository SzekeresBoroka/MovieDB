package com.example.moviedb;


import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ContainerWithMenuFragment extends Fragment {

    private Context context;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_container_with_menu, container, false);

        final BottomNavigationView bottomNavBar = view.findViewById(R.id.bottomNavBar);
        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            /**
             * Getting selected menu item, every item has it's own fragment
             * which will be replaced
             * @param item
             * @return
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_now_playing: {
                        bottomNavBar.getMenu().getItem(0).setChecked(true);
                        bottomNavBar.getMenu().getItem(1).setChecked(false);
                        bottomNavBar.getMenu().getItem(2).setChecked(false);
                        bottomNavBar.getMenu().getItem(3).setChecked(false);
                       /* FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                        frag_trans.replace(R.id.fragment_container_with_menu,new NowPlayingFragment());
                        frag_trans.commit();*/
                        break;
                    }
                    case R.id.action_top_100: {
                        bottomNavBar.getMenu().getItem(0).setChecked(false);
                        bottomNavBar.getMenu().getItem(1).setChecked(true);
                        bottomNavBar.getMenu().getItem(2).setChecked(false);
                        bottomNavBar.getMenu().getItem(3).setChecked(false);
                       /* FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                        frag_trans.replace(R.id.fragment_container_with_menu,new TopMoviesFragment());
                        frag_trans.commit();*/
                        break;
                    }
                    case R.id.action_favourites: {
                        bottomNavBar.getMenu().getItem(0).setChecked(false);
                        bottomNavBar.getMenu().getItem(1).setChecked(false);
                        bottomNavBar.getMenu().getItem(2).setChecked(true);
                        bottomNavBar.getMenu().getItem(3).setChecked(false);
                       /* FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                        frag_trans.replace(R.id.fragment_container_with_menu,new FavouritesFragment());
                        frag_trans.commit();*/
                        break;
                    }
                    case R.id.action_profile: {
                        bottomNavBar.getMenu().getItem(0).setChecked(false);
                        bottomNavBar.getMenu().getItem(1).setChecked(false);
                        bottomNavBar.getMenu().getItem(2).setChecked(false);
                        bottomNavBar.getMenu().getItem(3).setChecked(true);
                        FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                        frag_trans.replace(R.id.fragment_container_with_menu,new ProfileFragment());
                        frag_trans.commit();
                        break;
                    }
                }
                return true;
            }
        });

        bottomNavBar.setSelectedItemId(R.id.action_top_100);
        bottomNavBar.getMenu().getItem(1).setChecked(true);

        //if keyboard is showing set bottomNavigationBar's visibility to GONE
        final ConstraintLayout constraintLayout = view.findViewById(R.id.rootView);
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                constraintLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = constraintLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    bottomNavBar.setVisibility(View.GONE);
                } else {
                    bottomNavBar.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

}
