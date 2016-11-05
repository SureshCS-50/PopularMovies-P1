package com.sureshcs50.popularmovies_p1.helpers;

import com.sureshcs50.popularmovies_p1.Models.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sureshkumar on 05/11/16.
 */

public class DataManager {

    public List<Movie> getFavMovies() {
        List<Movie> movies = Movie.find(Movie.class, "is_favourite = ?", new String[]{"1"}, null, null, null);
        if (movies != null && movies.size() > 0) {
            return movies;
        }
        return new ArrayList<Movie>();
    }

    public Movie getMovieById(String movieId){
        List<Movie> movies = Movie.find(Movie.class, "is_favourite = ?", new String[]{"1"}, null, null, null);
        if (movies != null && movies.size() > 0) {
            return movies.get(0);
        }
        return null;
    }

    public boolean isMovieFavourited(String movieId) {
        List<Movie> movies = Movie.find(Movie.class, "is_favourite = ?", new String[]{"1"}, null, null, "1");
        if (movies != null && movies.size() > 0) {
            Movie movie = movies.get(0);
            if(movie.isFavourite){
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void toggleFavourite(String movieId) {
        Movie movie = getMovieById(movieId);
        if(movie != null){
            movie.isFavourite = !movie.isFavourite;
            movie.save();
        }
    }
}
