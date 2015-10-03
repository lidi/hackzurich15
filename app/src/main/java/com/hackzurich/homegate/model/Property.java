package com.hackzurich.homegate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcel
public class Property {

    @JsonProperty("picFilename1Medium")
    String mIconUrl;
    @JsonProperty("advertismentId")
    private long mId;

    @JsonProperty("title")
    String mTitle;
    @JsonProperty("street")
    String mStreet;
    @JsonProperty("sellingPrice")
    String mPrice;
    @JsonProperty("currency")
    String mCurrency;
    @JsonProperty("geoLocation")
    String mGeoLocation;

    public String getIconUrl() {
        return mIconUrl;
    }

    public void setIconUrl(String iconUrl) {
        mIconUrl = iconUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getStreet() {
        return mStreet;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setStreet(String street) {
        mStreet = street;
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

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public double getLatitude() {
        String[] tokens = mGeoLocation.split(",");
        return Double.valueOf(tokens[1]);
    }

    public double getLongitute() {
        String[] tokens = mGeoLocation.split(",");
        return Double.valueOf(tokens[0]);
    }
}
