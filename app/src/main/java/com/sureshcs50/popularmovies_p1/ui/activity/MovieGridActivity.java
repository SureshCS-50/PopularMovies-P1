package com.sureshcs50.popularmovies_p1.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sureshcs50.popularmovies_p1.R;
import com.sureshcs50.popularmovies_p1.ui.fragment.MovieGridFragment;

public class MovieGridActivity extends AppCompatActivity {

    static int activeId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_grid, menu);
        if (activeId == 0) {
            activeId = R.id.menuSortPopular;
        } else {
            menu.findItem(activeId).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        MovieGridFragment fragment = MovieGridFragment.mInstance;

        if (id == R.id.menuSortRating) {
            fragment.isRatingUrl = true;
        } else if (id == R.id.menuSortPopular) {
            fragment.isRatingUrl = false;
        }
        item.setChecked(true);
        if (id == R.id.menuSortPopular || id == R.id.menuSortRating) {
            fragment.renderUI(false);
            activeId = id;
        } else if (id == R.id.menuSortFavorites) {
            fragment.renderUI(true);
            activeId = id;
        }
        return super.onOptionsItemSelected(item);
    }

}