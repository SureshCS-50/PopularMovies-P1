package com.sureshcs50.popularmovies_p1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by sureshkumar on 14/03/16.
 */
public class Movie extends SugarRecord implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    public int isFavourite = 0;
    private String movieId;
    private String originalTitle;
    private String title;
    private String imageUrl;
    private String overview;
    private String voteAverage;
    private String releaseDate;
    private String popularity;

    public Movie() {
    }

    public Movie(Parcel in) {
        originalTitle = in.readString();
        title = in.readString();
        imageUrl = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
        popularity = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(originalTitle);
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeString(overview);
        dest.writeString(voteAverage);
        dest.writeString(releaseDate);
        dest.writeString(popularity);
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }
}