package com.example.luci.movietrailerfinder.Controller;

import com.example.luci.movietrailerfinder.Data.MovieResult;

public interface CommonActionCallbacks {
    void onLastElementReached();
    void onItemClicked(MovieResult.Movie movie);
}
