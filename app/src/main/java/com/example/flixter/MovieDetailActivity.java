package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailActivity extends YouTubeBaseActivity {
    TextView tvDetailTitle, tvDetailOverview;
    RatingBar ratingBarDetail;
    YouTubePlayerView player;

    Movie movie;

    // TODO: Enter YouTube API key here
    public static final String YOUTUBE_API_KEY = "XXX";
    public static final String MOVIE_DB_API_KEY = "XXX";
    public static final String MOVIE_VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos";

    public static final String TAG = "MovieDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // TODO: can try making background a tinted version of the movie poster
        // TODO: can add additional details such as release date, etc
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailOverview = findViewById(R.id.tvDetailOverview);
        ratingBarDetail = findViewById(R.id.ratingBarDetail);
        player = findViewById(R.id.player);

        // Extracting movie passed from MainActivity
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        // Set values for each view component
        tvDetailTitle.setText(movie.getTitle());
        tvDetailOverview.setText(movie.getOverview());
        ratingBarDetail.setRating((float) movie.getAverageRating());

        // Getting videos related to movie
        // Client to perform GET request to movie database API for videos related to movie
        AsyncHttpClient client = new AsyncHttpClient();
        // Setting API key parameters
        RequestParams params = new RequestParams();
        params.put("api_key", MOVIE_DB_API_KEY);
        // Making get request
        client.get(String.format(MOVIE_VIDEOS_URL, movie.getId()), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length() != 0) {
                        String youtubeVideoKey = results.getJSONObject(0).getString("key");
                        Log.d(TAG, "YouTube key: " + youtubeVideoKey);
                        initializeYoutubePlayer(youtubeVideoKey);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON exception: ", e);;
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    private void initializeYoutubePlayer(final String youtubeVideoKey) {
        // Initializing the YouTube player
        player.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onInitializationSuccess");
                // Loading movie YouTube video if movie has rating above 8
                if (movie.getAverageRating() >= 8) {
                    youTubePlayer.loadVideo(youtubeVideoKey);
                } else {
                    youTubePlayer.cueVideo(youtubeVideoKey);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onInitializationFailure");
            }
        });
    }
}