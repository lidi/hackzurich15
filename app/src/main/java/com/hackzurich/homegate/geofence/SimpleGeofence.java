/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hackzurich.homegate.geofence;

import com.google.android.gms.location.Geofence;

import com.hackzurich.homegate.model.Property;

/**
 * A single Geofence object, defined by its center and radius.
 */
public class SimpleGeofence {

    // Instance variables
    private final String mId;
    private final double mLatitude;
    private final double mLongitude;
    private final float mRadius;
    private final String mImgUrl;
    private long mExpirationDuration;
    private int mTransitionType;
    private Property mProperty;
    private String mTitle;

    /**
     * @param geofenceId The Geofence's request ID.
     * @param latitude Latitude of the Geofence's center in degrees.
     * @param longitude Longitude of the Geofence's center in degrees.
     * @param radius Radius of the geofence circle in meters.
     * @param expiration Geofence expiration duration.
     * @param transition Type of Geofence transition.
     * @param property
     */
    public SimpleGeofence(float radius,
            long expiration, int transition, Property property) {
        // Set the instance fields from the constructor.
        this.mId = String.valueOf(property.getId());
        this.mLatitude = property.getLatitude();
        this.mLongitude = property.getLongitute();
        this.mRadius = radius;
        this.mExpirationDuration = expiration;
        this.mTransitionType = transition;
        this.mImgUrl = property.getIconUrl();
        mTitle = property.getTitle();
        mProperty = property;
    }

    public SimpleGeofence(String id, double lat, double lng, float radius, long expirationDuration,
            int transitionType, String imgUrl, String title) {
        this.mId = id;
        this.mLatitude = lat;
        this.mLongitude = lng;
        this.mRadius = radius;
        this.mExpirationDuration = expirationDuration;
        this.mTransitionType = transitionType;
        this.mImgUrl = imgUrl;
        mTitle = title;
    }

    // Instance field getters.
    public String getId() {
        return mId;
    }
    public double getLatitude() {
        return mLatitude;
    }
    public double getLongitude() {
        return mLongitude;
    }
    public float getRadius() {
        return mRadius;
    }
    public long getExpirationDuration() {
        return mExpirationDuration;
    }
    public int getTransitionType() {
        return mTransitionType;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    /**
     * Creates a Location Services Geofence object from a SimpleGeofence.
     * @return A Geofence object.
     */
    public Geofence toGeofence() {
        // Build a new Geofence object.
        return new Geofence.Builder()
                .setRequestId(mId)
                .setTransitionTypes(mTransitionType)
                .setCircularRegion(mLatitude, mLongitude, mRadius)
                .setExpirationDuration(mExpirationDuration)
                .build();
    }

    public String getTitle() {
        return mTitle;
    }
}
