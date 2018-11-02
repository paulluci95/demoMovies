package com.example.luci.movietrailerfinder;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.luci.movietrailerfinder.Controller.CarouselAdapter;
import com.example.luci.movietrailerfinder.Controller.CommonActionCallbacks;
import com.example.luci.movietrailerfinder.Controller.MovieListAdapter;
import com.example.luci.movietrailerfinder.Controller.RESTClientInterface;
import com.example.luci.movietrailerfinder.Controller.TrailerAdapter;
import com.example.luci.movietrailerfinder.Data.MovieResult;
import com.example.luci.movietrailerfinder.Data.ViewType;
import com.example.luci.movietrailerfinder.Views.DetailMovieFragment;
import com.example.luci.movietrailerfinder.Views.DisplaySettings;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Handles the user gestures and provides the user interface
 */
public class MainActivity extends AppCompatActivity {

    /** The tag for logs in current class*/
    private static final String TAG = MainActivity.class.getName();

    /** The base url of used API */
    public static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";

    /** The base url for downloading images  */
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/";

    /** The key for API */
    public static final String API_KEY_TMDB = "1b430fab82377155e112c91eea9df99f";

    /** The key for youtube API */
    public static final String YOUTUBE_API_KEY = "AIzaSyA1iiKIRDbBvr4jsr77hVNncM4_7Pr6azU";

    /** Current page provided by API (20 items limitation) */
    private int currentPage = 0;

    /** The list of movies */
    private List<MovieResult.Movie> moviesList = new ArrayList<>();

    /** Interface for REST requests */
    private RESTClientInterface restClientInterface;

    /** The adapter of recycler view  */
    private MovieListAdapter movieListAdapter;

    /** The adapter of page view */
    private CarouselAdapter carouselAdapter;

    /** The current view type selected by user */
    private ViewType currentViewType;

    /** The reference of youtube player */
    private YouTubePlayer player;

    /** The container for views */
    @BindView(R.id.main_view)
    FrameLayout flContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /** The reference of LayoutInflater */
    private LayoutInflater inflater;

    /** The listener for starting a video when a thumbnail from movie detail page is clicked */
    private final TrailerAdapter.ThumbnailCallback thumbnailCallback = movieURLId -> {
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        Objects.requireNonNull(MainActivity.this.getSupportActionBar()).hide();
        FragmentTransaction transaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_view, youTubePlayerFragment).addToBackStack(null).commit();

        youTubePlayerFragment.initialize(API_KEY_TMDB, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, boolean wasRestored) {

                if (!wasRestored) {
                    MainActivity.this.player = player;
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    player.setFullscreen(true);
                    player.loadVideo(movieURLId);
                    player.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                String errorMessage = error.toString();
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);
            }

        });
    };

    /** The listener for both display types */
    private final CommonActionCallbacks commonActionCallbacks = new CommonActionCallbacks() {
        @Override
        public void onLastElementReached() {
            getMoviesList();
        }

        @Override
        public void onItemClicked(MovieResult.Movie movie) {
            DetailMovieFragment detailMovieFragment = new DetailMovieFragment();
            detailMovieFragment.setMovie(movie);
            detailMovieFragment.setThumbnailCallback(thumbnailCallback);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().addToBackStack(null);
            ft.replace(R.id.main_view, detailMovieFragment);
            ft.commitAllowingStateLoss();
            Objects.requireNonNull(getSupportActionBar()).setTitle(movie.getTitle());
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.toolbar_title);
        setSupportActionBar(toolbar);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(MOVIE_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        restClientInterface = retrofit.create(RESTClientInterface.class);
        inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getMoviesList();
        showSetDisplayDialog();
    }

    /**
     * Requests the list of movies contained by a page
     */
    private void getMoviesList() {
        Call<MovieResult> call = restClientInterface.getMoviesList(API_KEY_TMDB, currentPage + 1);
        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.toolbar_title);
                MovieResult movieResult = response.body();
                if (movieResult != null) {
                    moviesList.addAll(movieResult.getResults());
                    if (movieListAdapter != null) {
                        movieListAdapter.notifyDataSetChanged();
                    }
                    if (carouselAdapter != null) {
                        carouselAdapter.notifyDataSetChanged();
                    }
                    currentPage = movieResult.getPage();
                }
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                Log.e(TAG, "onResponse: ");
            }
        });
    }

    /**
     * Shows the dialog where the user will select wished display type
     */
    private void showSetDisplayDialog() {
        DisplaySettings dialogFragment = new DisplaySettings();
        dialogFragment.setSelectViewTypeCallback(viewType -> {
            currentViewType = viewType;
            loadDefaultView(viewType);
        });
        dialogFragment.show(getSupportFragmentManager(), "display_settings");
    }

    /**
     * Reload the main view (home)
     *
     * @param viewType The display type selected by user
     */
    private void loadDefaultView(ViewType viewType) {
        flContainer.removeAllViews();
        if (viewType == ViewType.LIST) {
            inflater.inflate(R.layout.list_movies, flContainer);
            RecyclerView recyclerView = flContainer.findViewById(R.id.movies_recycler_view);
            movieListAdapter = new MovieListAdapter(moviesList, commonActionCallbacks);
            recyclerView.setAdapter(movieListAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        } else {
            inflater.inflate(R.layout.carousel_movies, flContainer);
            ViewPager viewPager = flContainer.findViewById(R.id.carousel_movies);
            carouselAdapter = new CarouselAdapter(moviesList, commonActionCallbacks);
            viewPager.setAdapter(carouselAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handleBackAction();
    }

    /**
     * Handles the back action (toolbar and system)
     */
    private void handleBackAction(){
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        loadDefaultView(currentViewType);
        if (player != null)
            player.release();
        getSupportActionBar().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_display:
                showSetDisplayDialog();
                return true;
            case android.R.id.home:
               handleBackAction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
