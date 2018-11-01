package com.example.luci.movietrailerfinder.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luci.movietrailerfinder.Controller.TrailerAdapter;
import com.example.luci.movietrailerfinder.Data.MovieResult;
import com.example.luci.movietrailerfinder.R;
import com.squareup.picasso.Picasso;


import static com.example.luci.movietrailerfinder.MainActivity.IMAGE_BASE_URL;

/**
 * The fragment contains movies details
 */
public class DetailMovieFragment extends Fragment {

    /** Custom listener for thumbnail click */
    private TrailerAdapter.ThumbnailCallback thumbnailCallback;

    /** The taped movie */
    private MovieResult.Movie movie;

    public void setMovie(MovieResult.Movie movie) {
        this.movie = movie;
    }

    public void setThumbnailCallback(TrailerAdapter.ThumbnailCallback thumbnailCallback) {
        this.thumbnailCallback = thumbnailCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_movie_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView posterImg = view.findViewById(R.id.detail_fragment_poster);
        TextView titleMovie = view.findViewById(R.id.detail_fragment_title);
        TextView idMovie = view.findViewById(R.id.detail_fragment_id);
        RecyclerView recyclerView = view.findViewById(R.id.detail_page_trailers);
        TrailerAdapter trailerAdapter = new TrailerAdapter(thumbnailCallback);
        recyclerView.setAdapter(trailerAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        Picasso.get().load(IMAGE_BASE_URL + movie.getPosterPath()).into(posterImg);
        titleMovie.setText(movie.getTitle());
        idMovie.setText(String.valueOf(movie.getId()));
    }
}
