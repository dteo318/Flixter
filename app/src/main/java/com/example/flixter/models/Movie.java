package com.example.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {
    String posterPath;
    String backdropPath;
    String title;
    String overview;
    double averageRating;
    int id;

    // Empty constructor needed for Parceler library
    public Movie() {}

    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        averageRating = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");
    }

    public int getId() {
        return id;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public double getAverageRating() {
        return averageRating;
    }

    public boolean isPopularMovie() {
        return averageRating >= 8;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public static List<Movie> fromJsonArray(JSONArray moviesJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<Movie>();
        for (int i = 0; i < moviesJsonArray.length(); i++) {
            movies.add(new Movie(moviesJsonArray.getJSONObject(i)));
        }
        return movies;
    }
}
