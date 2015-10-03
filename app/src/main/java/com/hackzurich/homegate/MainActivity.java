package com.hackzurich.homegate;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;

import com.hackzurich.homegate.adapter.PropertiesAdapter;
import com.hackzurich.homegate.geofence.GeofenceTransitionsIntentService;
import com.hackzurich.homegate.geofence.SimpleGeofence;
import com.hackzurich.homegate.geofence.SimpleGeofenceStore;
import com.hackzurich.homegate.model.Property;
import com.hackzurich.homegate.network.LoadPropertiesTask;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.hackzurich.homegate.geofence.Constants.ANDROID_BUILDING_RADIUS_METERS;
import static com.hackzurich.homegate.geofence.Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST;
import static com.hackzurich.homegate.geofence.Constants.GEOFENCE_EXPIRATION_TIME;

public class MainActivity extends AppCompatActivity implements LoadPropertiesTask.Listener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getName();
    public static final String EXTRA_PRICE_MIN = "extraPriceMin";
    public static final String EXTRA_PRICE_MAX = "extraPriceMax";
    public static final String EXTRA_ROOM_MIN = "extraRoomMin";
    // Internal List of Geofence objects. In a real app, these might be provided by an API based on
    // locations within the user's proximity.
    List<Geofence> mGeofenceList;

    // These will store hard-coded geofences in this sample app.

    // Persistent storage for geofences.
    private SimpleGeofenceStore mGeofenceStorage;

    private LocationServices mLocationService;
    // Stores the PendingIntent used to request geofence monitoring.
    private PendingIntent mGeofenceRequestIntent;
    private GoogleApiClient mApiClient;
    private List<Property> mData;

    // Defines the allowable request types (in this example, we only add geofences).
    private enum REQUEST_TYPE {
        ADD
    }

    private REQUEST_TYPE mRequestType;

    private RecyclerView mRecyclerview;
    private LinearLayoutManager mLayouManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Geofence Initialized", Snackbar.LENGTH_SHORT).show();
                initGeofence(mData);
            }
        });

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mLayouManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLayouManager);
        LoadPropertiesTask task = new LoadPropertiesTask(this);
        Bundle extras = getIntent().getExtras();

        String string = extras.getString(EXTRA_PRICE_MIN);
        String string1 = extras.getString(EXTRA_PRICE_MAX);
        task.execute(string, string1, extras.getString(EXTRA_ROOM_MIN));

    }

    private void initGeofence(List<Property> data) {
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();

        // Instantiate a new geofence storage area.
        mGeofenceStorage = new SimpleGeofenceStore(this);
        // Instantiate the current List of geofences.
        mGeofenceList = new ArrayList<Geofence>();
        createGeofences(data);
    }

    private void createGeofences(List<Property> data) {

        for (Property property : data) {
            SimpleGeofence geofence = new SimpleGeofence(
                    ANDROID_BUILDING_RADIUS_METERS,
                    GEOFENCE_EXPIRATION_TIME,
                    Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT,
                    property);
            mGeofenceStorage.setGeofence(String.valueOf(property.getId()), geofence);
            mGeofenceList.add(geofence.toGeofence());

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataLoaded(List<Property> data) {
        mData = data;
        PropertiesAdapter adapter = new PropertiesAdapter(data,this);
        mRecyclerview.setAdapter(adapter);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mGeofenceRequestIntent = getGeofenceTransitionPendingIntent();
        LocationServices.GeofencingApi.addGeofences(mApiClient, mGeofenceList,
                mGeofenceRequestIntent);
        Toast.makeText(this, getString(R.string.start_geofence_service), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // If the error has a resolution, start a Google Play services activity to resolve it.
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, "Exception while resolving connection error.", e);
            }
        } else {
            int errorCode = connectionResult.getErrorCode();
            Log.e(TAG, "Connection to Google Play services failed with error code " + errorCode);
        }
    }

    private PendingIntent getGeofenceTransitionPendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
