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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import com.bumptech.glide.Glide;
import com.hackzurich.homegate.DetailActivity;
import com.hackzurich.homegate.R;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import static com.hackzurich.homegate.geofence.Constants.TAG;

/**
 * Listens for geofence transition changes.
 */
public class GeofenceTransitionsIntentService extends IntentService
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public GeofenceTransitionsIntentService() {
        super(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Handles incoming intents.
     *
     * @param intent The Intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geoFenceEvent = GeofencingEvent.fromIntent(intent);
        if (geoFenceEvent.hasError()) {
            int errorCode = geoFenceEvent.getErrorCode();
            Log.e(TAG, "Location Services error: " + errorCode);
        } else {

            int transitionType = geoFenceEvent.getGeofenceTransition();
            if (Geofence.GEOFENCE_TRANSITION_ENTER == transitionType) {
                Log.e(TAG, getString(R.string.entering_geofence));
                // Connect to the Google Api service in preparation for sending a DataItem.
                // Get the geofence id triggered. Note that only one geofence can be triggered at a
                // time in this example, but in some cases you might want to consider the full list
                // of geofences triggered.
                String triggeredGeoFenceId = geoFenceEvent.getTriggeringGeofences().get(0)
                        .getRequestId();
                // Create a DataItem with this geofence's id. The wearable can use this to create
                // a notification.
                SimpleGeofenceStore mGeofenceStorage = new SimpleGeofenceStore(this);
                showNotification(this, mGeofenceStorage.getGeofence(triggeredGeoFenceId));
            } else if (Geofence.GEOFENCE_TRANSITION_EXIT == transitionType) {
                // Delete the data item when leaving a geofence region.
            }
        }
    }

    /**
     * Showing a toast message, using the Main thread
     */
    private void showNotification(final Context context, SimpleGeofence geoFence) {
        Bitmap icon = null;
        try {
            icon = Glide.
                    with(this).
                    load(geoFence.getImgUrl()).
                    asBitmap().
                    into(200, 200). // Width and height
                    get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_ID, Long.valueOf(geoFence.getId()));
        intent.putExtra(DetailActivity.EXTRA_IMAGE_URL, geoFence.getImgUrl());
        intent.putExtra(DetailActivity.EXTRA_TITLE, geoFence.getTitle());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(DetailActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("We found an apartment nearby")
                        .setContentText(geoFence.getTitle())
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setLargeIcon(icon);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(42, mBuilder.build());
    }

    @Override
    public void onConnected(Bundle connectionHint) {
    }

    @Override
    public void onConnectionSuspended(int cause) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    }

}
