package com.example.myapplication.api;

import retrofit2.http.GET;
import java.util.List;
import retrofit2.Call;
public interface apiinterface {


    static final String BASE_URL = "https://corona.lmao.ninja/v2/";

    @GET("countries")
    Call<List<CountryData>> getCountryData();
}
