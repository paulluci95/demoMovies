package com.example.luci.movietrailerfinder.Controller;

import com.example.luci.movietrailerfinder.Data.MovieResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RESTClientInterface {

    @GET("popular")
    Call<MovieResult> getMoviesList(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

}
