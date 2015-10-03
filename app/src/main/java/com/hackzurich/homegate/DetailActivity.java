package com.hackzurich.homegate;

import com.bumptech.glide.Glide;
import com.hackzurich.homegate.adapter.RatingsAdapter;
import com.hackzurich.homegate.adapter.ScreenSlidePagerAdapter;
import com.hackzurich.homegate.model.PropertyDetail;
import com.hackzurich.homegate.model.Rating;
import com.hackzurich.homegate.model.RatingRequest;
import com.hackzurich.homegate.model.RatingResponse;
import com.hackzurich.homegate.network.LoadDetailsTask;
import com.hackzurich.homegate.network.PostReviewTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoadDetailsTask.Listener,
        View.OnClickListener, PostReviewTask.Listener {

    public static final java.lang.String EXTRA_ID = "extraID";
    public static final java.lang.String EXTRA_IMAGE_URL = "extraUrl";
    public static final java.lang.String EXTRA_TITLE = "extraTitle";
    private String mImageUrl;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private RatingBar mRatingBar;
    private RecyclerView mRecyclerview;
    private LinearLayoutManager mLayouManager;
    private EditText mComment;
    private RatingBar mRatingBarUser;
    private long mAddId;
    private Button mSubmit;
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private ImageView mBackdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        mAddId = extras.getLong(EXTRA_ID);
        mImageUrl = extras.getString(EXTRA_IMAGE_URL);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadBackdrop();

        fetchDetails();

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        String name = extras.getString(EXTRA_TITLE);
        mCollapsingToolbar.setTitle(name);

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mLayouManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLayouManager);

        mComment = (EditText) findViewById(R.id.textComment);
        mSubmit = (Button) findViewById(R.id.submit);
        mSubmit.setOnClickListener(this);
        mRatingBarUser = (RatingBar) findViewById(R.id.ratingBarUser);
        mPager = (ViewPager) findViewById(R.id.pager);
    }

    private void fetchDetails() {
        LoadDetailsTask task = new LoadDetailsTask(this);
        task.execute(mAddId);
    }

    private void loadRating(RatingResponse ratingResponse) {
        long avg = ratingResponse.getAvg();
        if (ratingResponse != null && avg > -1) {
            List<Rating> values = null;
            List<Rating> ratings = ratingResponse.getRatings();
            if (ratings.size() > 2) {
                values = ratings.subList(0, 2);
            } else {
                values = ratings;
            }
            RatingsAdapter adapter = new RatingsAdapter(values);
            mRecyclerview.setAdapter(adapter);
            mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
            TextView ratingValue = (TextView) findViewById(R.id.ratingValue);
            TextView voters = (TextView) findViewById(R.id.nrOfVoters);
            voters.setText("(" + ratings.size() + ")");
            NumberFormat format = NumberFormat.getNumberInstance();
            format.setMinimumFractionDigits(2);
            ratingValue.setText(format.format(avg));
            mRatingBar.setRating(avg);
        } else {
            findViewById(R.id.ratingCard).setVisibility(View.GONE);
        }

    }

    private void loadBackdrop() {
        mBackdrop = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(mImageUrl).centerCrop().into(mBackdrop);
    }

    @Override
    public void onDataLoaded(PropertyDetail data) {
        TextView description = (TextView) findViewById(R.id.description);
        description.setText(data.getDescription());
        loadRating(data.getRatingResponse());
        List<String> images = data.getImages();
        if (images.size() > 0) {
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), images);
            mPager.setAdapter(mPagerAdapter);
            mPager.setPageTransformer(true, new ZoomOutPageTransformer());
            mPager.setVisibility(View.VISIBLE);
            mBackdrop.setVisibility(View.GONE);
        }
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

    @Override
    public void onClick(View v) {
        PostReviewTask task = new PostReviewTask(this);
        float rating = mRatingBarUser.getRating();
        if (rating > 0) {
            RatingRequest request = new RatingRequest();
            request.setComment(mComment.getText().toString());
            request.setRating(rating);
            request.setId(mAddId);
            task.execute(request);
        }
    }

    @Override
    public void onDataPosted() {
        Snackbar.make(mComment, "Review Posted", Snackbar.LENGTH_SHORT).show();
        mSubmit.setEnabled(false);
        mRatingBarUser.setEnabled(false);
        mRatingBarUser.setIsIndicator(true);
        mComment.setEnabled(false);
        fetchDetails();
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}