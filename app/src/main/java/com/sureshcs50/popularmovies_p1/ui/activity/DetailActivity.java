package com.sureshcs50.popularmovies_p1.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.sureshcs50.popularmovies_p1.R;
import com.sureshcs50.popularmovies_p1.ui.common.BaseActivity;
import com.sureshcs50.popularmovies_p1.ui.fragment.DetailsFragment;
import com.sureshcs50.popularmovies_p1.utils.Constants;

public class DetailActivity extends BaseActivity {

    private FragmentManager fm = null;
    private FragmentTransaction fragmentTransaction = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        String movieId = extras.getString(Constants.KEY_MOVIE);

        fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.layout_frame, DetailsFragment.newInstance(movieId), "movie_details");
        fragmentTransaction.commit();
    }
}
