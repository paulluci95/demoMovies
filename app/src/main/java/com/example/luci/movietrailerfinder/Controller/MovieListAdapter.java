package com.example.luci.movietrailerfinder.Controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luci.movietrailerfinder.Data.MovieResult;
import com.example.luci.movietrailerfinder.MainActivity;
import com.example.luci.movietrailerfinder.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for RecyclerView (list view type)
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ItemViewHolder> {

    /** The list of movies */
    private List<MovieResult.Movie> movieList;

    /** The listener for item clicked and last element reached */
    private CommonActionCallbacks commonActionCallbacks;

    /**
     * The view holder
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.movie_item_image)
        ImageView movieIcon;

        @BindView(R.id.movie_item_name)
        TextView movieTitle;

        @BindView(R.id.list_item_container)
        ConstraintLayout container;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * Constructor
     *
     * @param movieList The list of movies
     * @param commonActionCallbacks The listener for item clicked and last element reached
     *
     */
    public MovieListAdapter(List<MovieResult.Movie> movieList, CommonActionCallbacks commonActionCallbacks) {
        this.movieList = movieList;
        this.commonActionCallbacks = commonActionCallbacks;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.movie_list_item, viewGroup, false);
        return new ItemViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        final MovieResult.Movie movie = movieList.get(i);
        itemViewHolder.movieTitle.setText(movie.getTitle().concat("\n("+movie.getId()+")"));
        Picasso.get().load(MainActivity.IMAGE_BASE_URL + movie.getPosterPath()).into(itemViewHolder.movieIcon);
        if (getItemCount() - 1 <= i ){
            commonActionCallbacks.onLastElementReached();
        }
        itemViewHolder.container.setOnClickListener(view -> commonActionCallbacks.onItemClicked(movie));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
