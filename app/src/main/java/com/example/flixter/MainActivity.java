package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.adapters.MovieAdapter;
import com.example.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing";
    // TODO: Enter API key here
    private static final String MOVIE_DB_API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";

    List<Movie> movies;
    RecyclerView rvMovies;
    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movies = new ArrayList<>();
        rvMovies = findViewById(R.id.rvMovies);

        // Creating movie adapter
        movieAdapter = new MovieAdapter(this, movies);
        // Binding movie adapter to recyclerView
        rvMovies.setAdapter(movieAdapter);
        // Setting layout manager for recyclerView
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        // Creating client to make movie database GET request
        AsyncHttpClient client = new AsyncHttpClient();
        // Setting parameters for movie database API GET request
        RequestParams params = new RequestParams();
        params.put("api_key", MOVIE_DB_API_KEY);
        // Making a GET request to movie database API
        client.get(NOW_PLAYING_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    // Using static class in Movie to parse JSON response from movie database
                    // Adding parsed array of movies to movie list
                    movies.addAll(Movie.fromJsonArray(results));
                    // Notifying recyclerView movieAdapter of change to dataset
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies size: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON exception: ", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

    }
}