package com.sureshcs50.popularmovies_p1.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.sureshcs50.popularmovies_p1.Models.Movie;
import com.sureshcs50.popularmovies_p1.Models.Review;
import com.sureshcs50.popularmovies_p1.Models.Trailer;
import com.sureshcs50.popularmovies_p1.R;
import com.sureshcs50.popularmovies_p1.adapter.TrailerAdapter;
import com.sureshcs50.popularmovies_p1.helpers.DataManager;
import com.sureshcs50.popularmovies_p1.utils.Constants;
import com.sureshcs50.popularmovies_p1.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sureshkumar on 05/11/16.
 */

public class DetailsFragment extends Fragment {

    public static DetailsFragment mInstance;
    public Movie movie;
    public FloatingActionButton fab;
    public TrailerAdapter mTrailerAdapter;
    public LinearLayout mLytTrailerList, mLytReviewList;
    RequestQueue mRequestQueue;
    private View mContentView;
    private DataManager mDataManager;
    private List<Trailer> mTrailers;
    private List<Review> mReviews;
    private ShareActionProvider mShareActionProvider;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private String movieId = "";

    public DetailsFragment() {
        mInstance = this;
    }

    public static DetailsFragment newInstance(String movieId) {
        Bundle args = new Bundle();
        DetailsFragment fragment = new DetailsFragment();
        args.putString(Constants.KEY_MOVIE, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null)
            movieId = getArguments().getString(Constants.KEY_MOVIE);
        mDataManager = new DataManager();
        mTrailers = new ArrayList<>();
        mReviews = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_details, container, false);
        movie = mDataManager.getMovieById(movieId);
        //  get UI components
        mLytTrailerList = (LinearLayout) mContentView.findViewById(R.id.lytTrailers);
        mLytReviewList = (LinearLayout) mContentView.findViewById(R.id.lytReviews);
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new FabOnClick());
        mTrailerAdapter = new TrailerAdapter(getActivity(), mTrailers);
        mRequestQueue = Volley.newRequestQueue(getActivity());
        updateUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    public void updateUI() {
        mToolbar = (Toolbar) mContentView.findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(Utils.getColoredDrawable(getActivity(), R.drawable.abc_ic_ab_back_mtrl_am_alpha, Color.WHITE));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mCollapsingToolbar = (CollapsingToolbarLayout) mContentView.findViewById(R.id.collapsingToolbar);
        // bind view and data..
        if (movie != null) {
            mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
            mCollapsingToolbar.setExpandedTitleColor(Color.WHITE);
            mCollapsingToolbar.setTitle(movie.getTitle());
        }

        if (movie.isFavourite == 1)
            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_star_big_on));
        else
            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_star_big_off));

        String backdropUrl = Constants.POSTER_BASE_URL + "w300" + movie.getImageUrl();
        Picasso.with(getContext()).load(backdropUrl).into((ImageView) mContentView.findViewById(R.id.imgBackdrop));
        String posterUrl = Constants.POSTER_BASE_URL + "w185" + movie.getImageUrl();
        Picasso.with(getContext()).load(posterUrl).into((ImageView) mContentView.findViewById(R.id.imgPoster));
        ((TextView) mContentView.findViewById(R.id.txtTitle)).setText(movie.getOriginalTitle());
        ((TextView) mContentView.findViewById(R.id.txtRating)).setText("User Rating : " + movie.getVoteAverage());
        ((TextView) mContentView.findViewById(R.id.txtRelease)).setText("Release Date : " + movie.getReleaseDate());
        ((TextView) mContentView.findViewById(R.id.txtSynopsis)).setText(movie.getOverview());

        getTrailers(movie.getMovieId());
        getReviews(movie.getMovieId());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        if (mShareActionProvider != null) {
            if (mTrailerAdapter.mTrailers.size() > 0)
                mShareActionProvider.setShareIntent(createVideoShareIntent(Constants.YOUTUBE_URL_BASE +
                        mTrailerAdapter.mTrailers.get(0).url));
            else
                mShareActionProvider.setShareIntent(createVideoShareIntent("<No Videos Found>"));
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void getTrailers(String movieId) {
        String url = "http://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + Constants.API_KEY;
        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("results");
                            JSONObject trailerObj;
                            for (int i = 0; i < items.length(); i++) {
                                trailerObj = items.getJSONObject(i);
                                Trailer trailer = new Trailer();
                                trailer.trailerId = trailerObj.getString("id");
                                trailer.url = trailerObj.getString("key");
                                trailer.label = trailerObj.getString("name");
                                mTrailerAdapter.addItem(trailer);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < mTrailerAdapter.getCount(); i++) {
                            mLytTrailerList.addView(mTrailerAdapter.getView(i, null, null));
                        }
                        // update share intent
                        if (mTrailerAdapter.mTrailers.size() > 0) {
                            try {
                                mShareActionProvider.setShareIntent(createVideoShareIntent(Constants.YOUTUBE_URL_BASE +
                                        mTrailerAdapter.mTrailers.get(0).url));
                            } catch (NullPointerException e) {
                                Log.v("", "Share Action Provider not defined");
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("", "Error in JSON Parsing");
            }
        });

        mRequestQueue.add(req);
    }

    public void getReviews(String movieId) {
        String url = "http://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=" + Constants.API_KEY;
        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("results");
                            JSONObject reviewObj;
                            View view;
                            for (int i = 0; i < items.length(); i++) {
                                reviewObj = items.getJSONObject(i);
                                Review review = new Review();
                                review.author = reviewObj.getString("author");
                                review.url = reviewObj.getString("url");
                                review.content = reviewObj.getString("content");
                                mLytReviewList.addView(view = createReviewView(review, i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("", "Error in JSON Parsing");
            }
        });

        mRequestQueue.add(req);
    }

    public View createReviewView(Review review, int i) {
        View view;
        view = View.inflate(getContext(), R.layout.lyt_review, null);
        ((TextView) view.findViewById(R.id.txtAuthorName)).setText(review.author);
        ((TextView) view.findViewById(R.id.txtReviewContent)).setText(review.content);
        view.setId(2000 + i);
        return view;
    }

    public void watchYoutubeVideo(String id) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_URL_BASE + id));
            startActivity(intent);
        }
    }

    private Intent createVideoShareIntent(String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        return shareIntent;
    }

    class FabOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            mDataManager.toggleFavourite(movie.getMovieId());
            String message;
            if (mDataManager.isMovieFavourited(movie.getMovieId())) {
                message = "Removed from Favorites";
                fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_star_big_off));
            } else {
                message = "Added to favorites";
                fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_star_big_on));
            }
            (MovieGridFragment.mInstance).updateFavoritesGrid();
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
