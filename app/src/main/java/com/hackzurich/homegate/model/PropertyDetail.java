package com.hackzurich.homegate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertyDetail {

    @JsonProperty("picFilename1Medium")
    private String mIconUrl;
    @JsonProperty("advertismentId")
    private long mId;

    @JsonProperty("title")
    private String mTitle;
    @JsonProperty("adDescription")
    private String mDescription;
    @JsonProperty("sellingPrice")
    private String mPrice;
    @JsonProperty("currency")
    private String mCurrency;

    RatingResponse mRatingResponse;

    @JsonProperty("pictures")
    private List<String> mImages;

    public String getIconUrl() {
        return mIconUrl;
    }

    public RatingResponse getRatingResponse() {
        return mRatingResponse;
    }

    public void setRatingResponse(RatingResponse ratingResponse) {
        mRatingResponse = ratingResponse;
    }

    public void setIconUrl(String iconUrl) {
        mIconUrl = iconUrl;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
    }

    public List<String> getImages() {
        return mImages;
    }

    public void setImages(List<String> images) {
        mImages = images;
    }
}
