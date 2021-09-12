package com.example.flixter.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.flixter.MovieDetailActivity;
import com.example.flixter.R;
import com.example.flixter.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    Context context;
    List<Movie> movies;
    String TAG = "MovieAdapter";

    private final int POPULAR_MOVIE = 1;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");

        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder viewHolder;

        if (viewType == POPULAR_MOVIE) {
            View popularMovieView = inflater.inflate(R.layout.item_popular_movie, parent, false);
            viewHolder = new ViewHolder(popularMovieView);
        } else {
            View regularMovieView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder = new ViewHolder(regularMovieView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Position bound is " + position);
        Movie movie = movies.get(position);
        if (holder.getItemViewType() == POPULAR_MOVIE) {
            // Popular movie bind
            holder.bindPopularMovie(movie);
        } else {
            // Regular movie bind
            holder.bindRegularMovie(movie);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (movies.get(position).isPopularMovie()) {
            return POPULAR_MOVIE;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle, tvOverview;
        CardView cardContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            cardContainer = itemView.findViewById(R.id.cardContainer);
        }

        public void bindPopularMovie(Movie movie) {
            int placeholderScaleWidth, placeholderScaleHeight;
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            // Getting backdrop path for popular movies
            String imageUrl = movie.getBackdropPath();
            // Setting image placeholder size based on orientation
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                placeholderScaleWidth = 3000;
                placeholderScaleHeight = 1000;
            } else {
                placeholderScaleWidth = 1000;
                placeholderScaleHeight = 800;
            }
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .override(placeholderScaleWidth, placeholderScaleHeight)
                    .dontAnimate()
                    .into(ivPoster);
            // Adding click listener on movie item to view more movie details
            cardContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show();
                    // Sending user to movie detail screen
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    // Passing movie data using Parceler
                    intent.putExtra("movie", Parcels.wrap(movie));
                    // Setting shared element transition
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, (View)tvTitle, "movieTitle");
                    context.startActivity(intent, options.toBundle());
                }
            });
        }

        public void bindRegularMovie(Movie movie) {
            String imageUrl;
            int placeholderScaleWidth, placeholderScaleHeight;
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            // Setting image url based on orientation
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
                placeholderScaleWidth = 1500;
                placeholderScaleHeight = 500;
            } else {
                imageUrl = movie.getPosterPath();
                placeholderScaleWidth = 500;
                placeholderScaleHeight = 600;
            }
            Glide.with(context)
                    .load(imageUrl)
                    .transform(new RoundedCorners(35))
                    .placeholder(R.drawable.ic_placeholder)
                    .override(placeholderScaleWidth, placeholderScaleHeight)
                    .dontAnimate()
                    .into(ivPoster);
            // Adding click listener on movie item to view more movie details
            cardContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show();
                    // Sending user to movie detail screen
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    // Passing movie data using Parceler
                    intent.putExtra("movie", Parcels.wrap(movie));
                    // Setting shared element transition
                    Pair<View, String> titleTransitionPair = Pair.create((View)tvTitle, "movieTitle");
                    Pair<View, String> overviewTransitionPair = Pair.create((View)tvOverview, "movieOverview");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) context, titleTransitionPair, overviewTransitionPair);
                    context.startActivity(intent, options.toBundle());
                }
            });
        }
    }
}
