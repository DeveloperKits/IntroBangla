package com.example.introbangla;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.introbangla.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Retrofit retrofit;
    private JsonCall jsonCall;
    private Random random;
    private List<ImageModel> images;

    public static final String url = "https://picsum.photos/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        random = new Random();
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonCall = retrofit.create(JsonCall.class);

        // after clicking button firstly check have internet then fetch image via API
        binding.button.setOnClickListener(view1 -> {
            if (IsNetworkAvailable()) {
                randomImage();
            }else {
                snakeBar("No Internet. Check and try again...");
            }
        });

        // load image if cache image are not null
        SharedPreferences sharedPreferences = getSharedPreferences("Intro", MODE_PRIVATE);
        String lastImageUrl = sharedPreferences.getString("lastImage", null);
        if (lastImageUrl != null) {
            Glide.with(MainActivity.this)
                    .load(lastImageUrl)
                    .placeholder(R.drawable.baseline_image_24)
                    .into(binding.imageView);
        }
    }

    private void randomImage() {
        binding.progressBar.setVisibility(View.VISIBLE);

        Call<List<ImageModel>> call = jsonCall.fetchImage();
        call.enqueue(new Callback<List<ImageModel>>() {
            @Override
            public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {
                if (response.isSuccessful()){
                    images = response.body();
                    addImage();
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    snakeBar("Failed! try again.");
                }
            }

            @Override
            public void onFailure(Call<List<ImageModel>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                snakeBar("Failed! try again.");
            }
        });
    }

    private void addImage() {
        if (images != null && !images.isEmpty()){
            ImageModel image = images.get(random.nextInt(images.size()));
            Glide.with(this)
                    .load(image.getDownloadUrl())
                    .placeholder(R.drawable.baseline_image_24)
                    .into(binding.imageView);

            SharedPreferences sharedPreferences = getSharedPreferences("Intro", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("lastImage", image.getDownloadUrl());
            editor.apply();
        } else {
            randomImage();
        }
    }

    private void snakeBar(String string) {
        Snackbar
                .make(binding.wrapContent, string, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    private boolean IsNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}