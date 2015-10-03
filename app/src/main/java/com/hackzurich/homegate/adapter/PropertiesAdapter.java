package com.hackzurich.homegate.adapter;

import com.bumptech.glide.Glide;
import com.hackzurich.homegate.DetailActivity;
import com.hackzurich.homegate.R;
import com.hackzurich.homegate.model.Property;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.ViewHolder> {

    private final Activity mActivity;

    public PropertiesAdapter(List<Property> values, Activity activity) {
        mValues = values;
        mActivity = activity;
    }

    private List<Property> mValues;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Property property = mValues.get(position);
        Glide.with(holder.mImageView.getContext())
                .load(property.getIconUrl())
                .fitCenter()
                .into(holder.mImageView);
        holder.mTitle.setText(property.getTitle());
        holder.mDescription.setText(property.getStreet());
        holder.mPrice.setText(property.getPrice() + " " + property.getCurrency());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                holder.mImageView.setTransitionName("rent");
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_ID, property.getId());
                intent.putExtra(DetailActivity.EXTRA_IMAGE_URL, property.getIconUrl());
                intent.putExtra(DetailActivity.EXTRA_TITLE, property.getTitle());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(mActivity, holder.mImageView, "profile");
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final ImageView mImageView;
        public final TextView mTitle;
        public final TextView mDescription;
        public final TextView mPrice;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.icon);
            mTitle = (TextView) view.findViewById(R.id.title);
            mDescription = (TextView) view.findViewById(R.id.short_description);
            mPrice = (TextView) view.findViewById(R.id.price);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText();
        }
    }
}
