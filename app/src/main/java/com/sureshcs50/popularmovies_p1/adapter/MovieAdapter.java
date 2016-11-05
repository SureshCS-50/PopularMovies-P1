package com.sureshcs50.popularmovies_p1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sureshcs50.popularmovies_p1.R;
import com.sureshcs50.popularmovies_p1.Models.Movie;
import com.sureshcs50.popularmovies_p1.utils.Constants;

import java.util.List;

/**
 * Created by sureshkumar on 14/03/16.
 */
public class MovieAdapter extends BaseAdapter {

    private Context mContext;
    private List<Movie> mMovies;
    private LayoutInflater mInflater;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.mContext = context;
        this.mMovies = movies;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.grid_item_movie, null);
            viewHolder.txtMovieName = (TextView) view.findViewById(R.id.txtTitle);
            viewHolder.imgPoster = (ImageView) view.findViewById(R.id.imgPoster);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Movie movie = getItem(position);
        viewHolder.txtMovieName.setText(movie.getTitle());
        String imageURL = Constants.POSTER_BASE_URL+"w185/"+ movie.getImageUrl();
        Picasso.with(mContext).load(imageURL).into(viewHolder.imgPoster);

        return view;
    }

    public void setItems(List<Movie> movies) {
        if (movies != null) {
            mMovies = movies;
            notifyDataSetChanged();
        }
    }

    public void addItem(Movie movie){
        mMovies.add(movie);
    }

    public void clearItems() {
        mMovies.clear();
    }

    public static class ViewHolder {
        public TextView txtMovieName;
        public ImageView imgPoster;
    }
}
