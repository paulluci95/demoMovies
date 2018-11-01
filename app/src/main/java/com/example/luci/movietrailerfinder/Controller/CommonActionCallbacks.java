package com.example.luci.movietrailerfinder.Controller;

import com.example.luci.movietrailerfinder.Data.MovieResult;

public interface CommonActionCallbacks {
    /**
     * Callback which trigger when last element is reached
     */
    void onLastElementReached();

    /**
     * Callback for element click
     *
     * @param movie The movie has been clicked
     */
    void onItemClicked(MovieResult.Movie movie);
}
