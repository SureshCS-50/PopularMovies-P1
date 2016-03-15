package com.sureshcs50.popularmovies_p1.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sureshcs50.popularmovies_p1.R;
import com.sureshcs50.popularmovies_p1.model.Movie;
import com.sureshcs50.popularmovies_p1.ui.common.BaseActivity;
import com.sureshcs50.popularmovies_p1.utils.Constants;
import com.sureshcs50.popularmovies_p1.utils.Utils;

public class DetailActivity extends BaseActivity {

    private Movie mMovie;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // setting up toolbar..
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(Utils.getColoredDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha, Color.WHITE));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);

        Bundle extras = getIntent().getExtras();
        mMovie = extras.getParcelable(Constants.KEY_MOVIE);

        // bind view and data..
        if (mMovie != null) {
            String backdropUrl = Constants.POSTER_BASE_URL + "w300" + mMovie.getImageUrl();
            Picasso.with(this).load(backdropUrl ).into((ImageView) findViewById(R.id.imgBackdrop));
            String posterUrl = Constants.POSTER_BASE_URL + "w185" + mMovie.getImageUrl();
            Picasso.with(this).load(posterUrl).into((ImageView) findViewById(R.id.imgPoster));
            ((TextView) findViewById(R.id.txtTitle)).setText(mMovie.getOriginalTitle());
            ((TextView) findViewById(R.id.txtRating)).setText("User Rating : " + mMovie.getVoteAverage());
            ((TextView) findViewById(R.id.txtRelease)).setText("Release Date : " + mMovie.getReleaseDate());
            ((TextView) findViewById(R.id.txtSynopsis)).setText(mMovie.getOverview());
            mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
            mCollapsingToolbar.setExpandedTitleColor(Color.WHITE);
            mCollapsingToolbar.setTitle(mMovie.getTitle());
        }

    }
}
