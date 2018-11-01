package com.example.luci.movietrailerfinder.Controller;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luci.movietrailerfinder.Data.MovieResult;
import com.example.luci.movietrailerfinder.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.luci.movietrailerfinder.MainActivity.IMAGE_BASE_URL;

/**
 * Adapter for PageView
 */
public class CarouselAdapter extends PagerAdapter {

    /** The list of movies */
    private List<MovieResult.Movie> movieList;

    /** The listener for item clicked and last element reached */
    private CommonActionCallbacks commonActionCallbacks;

    /**
     * The constructor
     *
     * @param movieList The list of movies
     * @param commonActionCallbacks The listener for item clicked and last element reached
     */
    public CarouselAdapter(List<MovieResult.Movie> movieList, CommonActionCallbacks commonActionCallbacks){
        this.movieList = movieList;
        this.commonActionCallbacks = commonActionCallbacks;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final MovieResult.Movie movie = movieList.get(position);
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View movieView = inflater.inflate(R.layout.carousel_movie_item, container, false);
        TextView title = movieView.findViewById(R.id.carousel_title);
        ImageView icon = movieView.findViewById(R.id.carousel_image);
        title.setText(movie.getTitle());
        Picasso.get().load(IMAGE_BASE_URL + movie.getPosterPath()).into(icon);
        container.addView(movieView);
        if (getCount() - 1 <= position ) {
            commonActionCallbacks.onLastElementReached();
        }
        movieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonActionCallbacks.onItemClicked(movie);
            }
        });
        return movieView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
