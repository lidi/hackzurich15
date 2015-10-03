package com.hackzurich.homegate;

import com.bumptech.glide.Glide;
import com.hackzurich.homegate.adapter.RatingsAdapter;
import com.hackzurich.homegate.model.PropertyDetail;
import com.hackzurich.homegate.model.Rating;
import com.hackzurich.homegate.network.LoadDetailsTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoadDetailsTask.Listener {

    public static final java.lang.String EXTRA_ID = "extraID";
    public static final java.lang.String EXTRA_IMAGE_URL = "extraUrl";
    public static final java.lang.String EXTRA_TITLE = "extraTitle";
    private String mImageUrl;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private RatingBar mRatingBar;
    private RecyclerView mRecyclerview;
    private LinearLayoutManager mLayouManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        long id = extras.getLong(EXTRA_ID);
        mImageUrl = extras.getString(EXTRA_IMAGE_URL);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadBackdrop();

        LoadDetailsTask task = new LoadDetailsTask(this);
        task.execute(id);

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        String name = extras.getString(EXTRA_TITLE);
        mCollapsingToolbar.setTitle(name);

        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRatingBar.setRating(3.67F);

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mLayouManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLayouManager);
        loadRating();
    }

    private void loadRating() {
        List<Rating> ratings = new ArrayList<>();
        Rating ratingOne = new Rating();
        ratingOne.setRating(3F);
        ratingOne.setComment("Decent flat, nothing fancy");

        Rating ratingTwo = new Rating();
        ratingTwo.setRating(4F);
        ratingTwo.setComment("Loved the view, but balcony sucks...");
        ratings.add(ratingOne);
        ratings.add(ratingTwo);

        RatingsAdapter adapter = new RatingsAdapter(ratings);
        mRecyclerview.setAdapter(adapter);
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(mImageUrl).centerCrop().into(imageView);
    }

    @Override
    public void onDataLoaded(PropertyDetail data) {
        TextView description = (TextView) findViewById(R.id.description);
        description.setText(data.getDescription());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
