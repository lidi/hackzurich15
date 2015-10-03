package com.hackzurich.homegate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RatingRequest {

    @JsonProperty("id")
    long mId;
    @JsonProperty("rating")
    double mRating;
    @JsonProperty("message")
    String mComment;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        mRating = rating;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }
}
