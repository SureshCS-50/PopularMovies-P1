package com.sureshcs50.popularmovies_p1.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sureshcs50.popularmovies_p1.R;
import com.sureshcs50.popularmovies_p1.models.Trailer;
import com.sureshcs50.popularmovies_p1.ui.fragment.DetailsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sureshkumar on 05/11/16.
 */

public class TrailerAdapter extends BaseAdapter {

    public List<Trailer> mTrailers = new ArrayList<Trailer>();
    private Context mContext;

    public TrailerAdapter(Context context, List<Trailer> trailers) {
        this.mContext = context;
        this.mTrailers = trailers;
    }

    @Override
    public int getCount() {
        return mTrailers.size();
    }

    @Override
    public Object getItem(int i) {
        return mTrailers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View trailerRow;
        if (convertView == null) {
            trailerRow = View.inflate(mContext, R.layout.item_trailer, null);
        } else {
            trailerRow = convertView;
        }
        trailerRow.setId(1000 + i);
        ((TextView) trailerRow.findViewById(R.id.txtTrailerName)).setText(mTrailers.get(i).label);
        Picasso.with(mContext).load("http://img.youtube.com/vi/" + mTrailers.get(i).url + "/default.jpg")
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) trailerRow.findViewById(R.id.imgTrailer));

        final String url = mTrailers.get(i).url;
        trailerRow.findViewById(R.id.imgTrailer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsFragment.mInstance.watchYoutubeVideo(url);
            }
        });
        return trailerRow;
    }

    public void addItem(Trailer trailer) {
        mTrailers.add(trailer);
    }

}
