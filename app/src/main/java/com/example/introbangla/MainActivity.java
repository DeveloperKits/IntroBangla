package com.example.introbangla;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.introbangla.databinding.ActivityMainBinding;

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
        binding.button.setOnClickListener(view1 -> randomImage());
    }

    private void randomImage() {
        //Toast.makeText(MainActivity.this, "Un success", Toast.LENGTH_SHORT).show();
        Call<List<ImageModel>> call = jsonCall.fetchImage();
        call.enqueue(new Callback<List<ImageModel>>() {
            @Override
            public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {
                if (response.isSuccessful()){
                    images = response.body();
                    addImage();
                } else {
                    Toast.makeText(MainActivity.this, "Un success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ImageModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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
        } else {
            randomImage();
        }
    }
}