package com.hackzurich.homegate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Property {

    @JsonProperty("picFilename1Medium")
    private String mIconUrl;
    @JsonProperty("advertismentId")
    private long mId;

    @JsonProperty("title")
    private String mTitle;
    @JsonProperty("street")
    private String mStreet;
    @JsonProperty("sellingPrice")
    private String mPrice;
    @JsonProperty("currency")
    private String mCurrency;

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
}
