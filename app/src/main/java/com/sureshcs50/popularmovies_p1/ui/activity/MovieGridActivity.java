package com.sureshcs50.popularmovies_p1.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sureshcs50.popularmovies_p1.R;
import com.sureshcs50.popularmovies_p1.adapter.MovieAdapter;
import com.sureshcs50.popularmovies_p1.model.Movie;
import com.sureshcs50.popularmovies_p1.ui.common.BaseActivity;
import com.sureshcs50.popularmovies_p1.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieGridActivity extends BaseActivity {

    private List<Movie> mMovies;
    private GridView mGridMovies;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);
        mMovies = new ArrayList<>();
        mGridMovies = (GridView) findViewById(R.id.gridMovies);
        mMovieAdapter = new MovieAdapter(this, mMovies);
        mGridMovies.setAdapter(mMovieAdapter);

        getMovies(true);

        mGridMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MovieGridActivity.this, DetailActivity.class);
                intent.putExtra(Constants.KEY_MOVIE, mMovies.get(position));
                startActivity(intent);
            }
        });
    }

    private void getMovies(boolean isPopularity) {
        String url = Constants.MOVIES_URL;

        if (isPopularity) {
            url += "?sort_by=popularity.desc&api_key=" + Constants.API_KEY;
        } else {
            url += "?sort_by=vote_average.desc&api_key=" + Constants.API_KEY;
        }

        mMovies = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject resultItem = results.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setOriginalTitle(resultItem.getString("original_title"));
                        movie.setTitle(resultItem.getString("title"));
                        movie.setImageUrl(resultItem.getString("poster_path"));
                        movie.setOverview(resultItem.getString("overview"));
                        movie.setReleaseDate(resultItem.getString("release_date"));
                        movie.setVoteAverage(String.valueOf(resultItem.getDouble("vote_average")));
                        mMovies.add(movie);
                    }

                    mMovieAdapter.setItems(mMovies);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MovieGridActivity.this, R.string.failed_to_get_movies, Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_movie_grid, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSort:
                return super.onOptionsItemSelected(item);
            case R.id.menuSortPopular:
                getMovies(true);
                return true;
            case R.id.menuSortRating:
                getMovies(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}