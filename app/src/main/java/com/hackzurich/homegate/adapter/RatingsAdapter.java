package com.hackzurich.homegate.adapter;

import com.bumptech.glide.Glide;
import com.hackzurich.homegate.R;
import com.hackzurich.homegate.model.Rating;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.ViewHolder> {

    List<Rating> mValues;

    public RatingsAdapter(List<Rating> values) {
        mValues = values;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(holder.mImageView.getContext())
                .load(R.drawable.ic_person_red_300_48dp)
                .fitCenter()
                .into(holder.mImageView);
        Rating rating = mValues.get(position);
        holder.mTextView.setText(rating.getComment());
        holder.mRatingBar.setRating(rating.getRating());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final ImageView mImageView;
        public final TextView mTextView;
        public final RatingBar mRatingBar;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.avatar);
            mTextView = (TextView) view.findViewById(android.R.id.text1);
            mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }
}
