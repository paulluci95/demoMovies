package com.example.luci.movietrailerfinder.Controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luci.movietrailerfinder.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.luci.movietrailerfinder.MainActivity.YOUTUBE_API_KEY;

/**
 * The adapter for RecyclerView which contains the trailers of movies
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ItemViewHolder> {

    /**
     * Interface for thumbnail click listener
     */
    public interface ThumbnailCallback {
        /**
         * Callback for thumbnail click event
         *
         * @param movieURLId The id from URL of youtube video
         */
        void onThumbnailClick(String movieURLId);
    }

    /** The list of movies url id */
    private List<String> movieURLList = Arrays.asList("u9Mv98Gr5pY", "9cBN9_9oK4A", "ek1ePFp-nBI", "pzD9zGcUNrw", "iPxGl3B2I4A", "X6waHtSgCTc", "eJU6S5KOsNI");

    /**
     * The listener for thumbnail tap action
     */
    private ThumbnailCallback thumbnailCallback;

    /**
     * Constructor.
     *
     * @param thumbnailCallback The listener for thumbnail tap action
     */
    public TrailerAdapter(ThumbnailCallback thumbnailCallback) {
        this.thumbnailCallback = thumbnailCallback;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.trailer_item, viewGroup, false);
        return new ItemViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, int i) {
        final int position = i;
        itemViewHolder.thumbnailView.setOnClickListener(view -> thumbnailCallback.onThumbnailClick(movieURLList.get(position)));
        if (itemViewHolder.readyForLoadingYoutubeThumbnail) {
            itemViewHolder.readyForLoadingYoutubeThumbnail = false;
            itemViewHolder.thumbnailView.initialize(YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(movieURLList.get(position));
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {

                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView childYouTubeThumbnailView, String s) {
                            youTubeThumbnailLoader.release();
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                            youTubeThumbnailLoader.release();
                        }
                    });
                    itemViewHolder.readyForLoadingYoutubeThumbnail = true;
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    itemViewHolder.readyForLoadingYoutubeThumbnail = true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return movieURLList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trailer_item)
        YouTubeThumbnailView thumbnailView;

        boolean readyForLoadingYoutubeThumbnail = true;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
