package com.sureshcs50.popularmovies_p1.ui.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sureshcs50.popularmovies_p1.Models.Movie;
import com.sureshcs50.popularmovies_p1.R;
import com.sureshcs50.popularmovies_p1.adapter.MovieAdapter;
import com.sureshcs50.popularmovies_p1.helpers.DataManager;
import com.sureshcs50.popularmovies_p1.ui.activity.DetailActivity;
import com.sureshcs50.popularmovies_p1.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sureshkumar on 05/11/16.
 */

public class MovieGridFragment extends Fragment {
    public static MovieGridFragment mInstance;
    public static String sortOrder = "popularity.desc", params = "";
    public static boolean setting_cached = false;
    public View mViewFragment;
    public ArrayList<Movie> mMovies = new ArrayList<Movie>();
    public MovieAdapter mMovieAdapter;
    public boolean isDualPane = false;
    public int gridPos = -1;
    GridView mGridview;
    private RequestQueue mRequestQueue;

    public MovieGridFragment() {
        mInstance = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewFragment = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        mRequestQueue = Volley.newRequestQueue(getContext());

        // setup adapters
        mMovieAdapter = new MovieAdapter(getContext(), mMovies);
        mGridview = (GridView) mViewFragment.findViewById(R.id.gridMovies);
        mGridview.setAdapter(mMovieAdapter);

        renderUI(setting_cached);
        mGridview.setOnItemClickListener(new GridClickListener());
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            setGridColCount(3);
        else
            setGridColCount(2);

        return mViewFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isDualPane = getPaneLayout();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("GRIDVIEW_POSITION", mGridview.getFirstVisiblePosition());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            gridPos = savedInstanceState.getInt("GRIDVIEW_POSITION");
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

    public void renderUI(boolean cached) {
        mMovies.clear();
        mMovieAdapter.clearItems();
        setting_cached = cached;
        if (!cached)
            getMovies(sortOrder, params);
        else
            getFavorites();
    }

    public void getMovies(String sortOrder, String moreParams) {
        final DataManager dataManager = new DataManager();
        String url = "http://api.themoviedb.org/3/discover/movie?sort_by=" + sortOrder + "&" + moreParams
                + "&api_key=" + Constants.API_KEY;
        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("results");
                            JSONObject movieObj;
                            for (int i = 0; i < items.length(); i++) {
                                movieObj = items.getJSONObject(i);
                                String movieId = movieObj.getString("id");
                                Movie movie = dataManager.getMovieById(movieId);
                                if(movie == null)
                                    movie = new Movie();
                                movie.setMovieId(movieId);
                                movie.setTitle(movieObj.getString("title"));
                                movie.setOriginalTitle(movieObj.getString("original_title"));
                                movie.setOverview(movieObj.getString("overview"));
                                movie.setImageUrl(movieObj.getString("poster_path"));
                                movie.setReleaseDate(movieObj.getString("release_date"));
                                movie.setVoteAverage(String.valueOf(movieObj.getDouble("vote_average")));
                                movie.setPopularity(String.valueOf(movieObj.getDouble("popularity")));
                                movie.save();
                                mMovies.add(movie);
                                // Add image to adapter
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mMovieAdapter.setItems(mMovies);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mGridview.setAdapter(mMovieAdapter);
                                if (gridPos > -1)
                                    mGridview.setSelection(gridPos);
                                gridPos = -1;
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("", "Network error");
            }
        });

        mRequestQueue.add(req);
    }

    public void getFavorites() {
        mMovies.clear();
        mMovies.addAll(new DataManager().getFavMovies());
        mMovieAdapter.setItems(mMovies);
        mGridview.setAdapter(mMovieAdapter);
        if (gridPos > -1)
            mGridview.setSelection(gridPos);
        gridPos = -1;
    }

    public void updateFavoritesGrid() {
        if (setting_cached) {
            int p = mGridview.getLastVisiblePosition();
            renderUI(true);
            mGridview.smoothScrollToPosition(p);
        }
    }

    public void setGridColCount(int n) {
        ((GridView) mViewFragment.findViewById(R.id.gridMovies)).setNumColumns(n);
    }

    public boolean getPaneLayout() {
        return (getActivity().findViewById(R.id.detailContainer) != null);
    }

    class GridClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (isDualPane) {
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                DetailsFragment mDetailsFragment = DetailsFragment.newInstance(mMovies.get(position).getMovieId());
                ft.replace(R.id.detailContainer, mDetailsFragment);
                ft.commit();
            } else {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(Constants.KEY_MOVIE, mMovies.get(position).getMovieId());
                startActivity(intent);
            }
        }
    }

}