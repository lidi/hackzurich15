package com.hackzurich.homegate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertiesResponse {

    @JsonProperty("items")
    List<Property> mItems;

    public List<Property> getItems() {
        return mItems;
    }

    public void setItems(List<Property> items) {
        mItems = items;
    }
}
