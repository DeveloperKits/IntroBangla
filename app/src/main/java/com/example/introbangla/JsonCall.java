package com.example.introbangla;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonCall {
    @GET("/v2/list")
    Call<List<ImageModel>> fetchImage();
}
