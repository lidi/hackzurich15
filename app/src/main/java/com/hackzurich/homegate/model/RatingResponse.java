package com.hackzurich.homegate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RatingResponse {

    @JsonProperty("avg")
    long mAvg;
    @JsonProperty("ratings")
    List<Rating> mRatings;

    public long getAvg() {
        return mAvg;
    }

    public void setAvg(long avg) {
        mAvg = avg;
    }

    public List<Rating> getRatings() {
        return mRatings;
    }

    public void setRatings(List<Rating> ratings) {
        mRatings = ratings;
    }
}
