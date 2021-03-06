package com.example.moviedb.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.moviedb.DbHelper;
import com.example.moviedb.MainActivity;
import com.example.moviedb.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ProfileFragment extends Fragment {

    private Context context;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        ((MainActivity) context).setTitle("Profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        final EditText et_userName = view.findViewById(R.id.et_userName);
        final ImageButton imgBtn_saveUserName = view.findViewById(R.id.imgBtn_saveUsername);
        final ImageButton imgBtn_changeUsername = view.findViewById(R.id.imgBtn_changeUserName);
        final ImageButton imgBtn_cancel = view.findViewById(R.id.imgBtn_cancel);
        ImageButton imgBtn_changeProfilePicture = view.findViewById(R.id.imgBtn_changeProfilePicture);
        Button btn_changePassword = view.findViewById(R.id.btn_changePassword);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        final String oldUsername = sharedPref.getString(getString(R.string.active_user),"Active User");
        et_userName.setText(oldUsername);
        et_userName.setFocusable(false);
        et_userName.setFocusableInTouchMode(false);

        DbHelper db = new DbHelper(context);
        String imagePath = db.getProfilePicture(oldUsername);
        if(!imagePath.equals("")){
            try {
                File f=new File(imagePath);
                Bitmap selectedImage = BitmapFactory.decodeStream(new FileInputStream(f));
                ImageView iv_profilePicture = view.findViewById(R.id.img_profilePicture);
                iv_profilePicture.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d("hiba", e.getMessage());
            }
        }
        else{
            Log.d("hiba", "nincs kep");
        }

        imgBtn_changeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_userName.setFocusableInTouchMode(true);
                et_userName.setFocusable(true);
                et_userName.setText("");
                et_userName.requestFocus();

                imgBtn_saveUserName.setVisibility(View.VISIBLE);
                imgBtn_cancel.setVisibility(View.VISIBLE);
                imgBtn_changeUsername.setVisibility(View.GONE);
            }
        });

        imgBtn_saveUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = et_userName.getText().toString();
                if(newUsername.isEmpty()){
                    et_userName.setError("Please enter a username");
                    return;
                }

                DbHelper db = new DbHelper(context);
                if(!db.changeUsername(oldUsername, newUsername)){
                    Toast.makeText(context, "Cannot update username", Toast.LENGTH_LONG).show();
                    return;
                }

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.active_user), newUsername);
                editor.apply();

                et_userName.setFocusable(false);
                et_userName.setFocusableInTouchMode(false);
                imgBtn_saveUserName.setVisibility(View.GONE);
                imgBtn_cancel.setVisibility(View.GONE);
                imgBtn_changeUsername.setVisibility(View.VISIBLE);
            }
        });

        imgBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_userName.setText(oldUsername);
                et_userName.setError(null);
                et_userName.setFocusable(false);
                et_userName.setFocusableInTouchMode(false);
                imgBtn_saveUserName.setVisibility(View.GONE);
                imgBtn_cancel.setVisibility(View.GONE);
                imgBtn_changeUsername.setVisibility(View.VISIBLE);
            }
        });

        btn_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                frag_trans.replace(R.id.fragment_container_without_menu,new ChangePasswordFragment());
                frag_trans.commit();
            }
        });

        imgBtn_changeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                ((MainActivity) context).startActivityForResult(photoPickerIntent, 1);
            }
        });

        return view;
    }

}
