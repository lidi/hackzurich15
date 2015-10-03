package com.hackzurich.homegate.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackzurich.homegate.model.PropertyDetail;
import com.hackzurich.homegate.model.RatingResponse;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class LoadDetailsTask extends AsyncTask<Long, Void, PropertyDetail> {

    public static final String TOKEN = "20c7209eda57e82b828027be7fca0e02";
    public static final String URL
            = "https://api-2445581357976.apicast.io:443/rs/real-estates/";
    public static final String RATING_URL
            = "http://46.101.209.165:5000/api/v1.0/ratings/rating/";

    Listener mListener;
    protected final ObjectMapper mMapper;

    public LoadDetailsTask(Listener listener) {
        mMapper = new ObjectMapper();
        mListener = listener;
    }

    @Override
    protected PropertyDetail doInBackground(Long... params) {
        java.net.URL url = null;
        PropertyDetail response = null;
        Long param = params[0];
        response = getPropertyDetail(response, param);
        RatingResponse ratingResponse = getRating(param);
        response.setRatingResponse(ratingResponse);
        return response;
    }

    private RatingResponse getRating(Long param) {
        java.net.URL url;
        HttpURLConnection mURLConnection = null;
        try {
            String uri = RATING_URL + param;
            url = new URL(uri);
            mURLConnection = (HttpURLConnection) url.openConnection();
            mURLConnection.setDoInput(true);
            mURLConnection.setRequestMethod("GET");
            mURLConnection.setRequestProperty("auth", TOKEN);
            if (mURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(mURLConnection.getInputStream());
                return readRatingStream(in);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            mURLConnection.disconnect();
        }
        return null;
    }

    private RatingResponse readRatingStream(InputStream in) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        try {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                json.append(line);
            }

            RatingResponse value = mMapper.readValue(json.toString(), RatingResponse.class);
            return value;
        } catch (IOException e) {
            throw e;
        } finally {
            r.close();
            in.close();
        }
    }

    private PropertyDetail getPropertyDetail(PropertyDetail response, Long param) {
        java.net.URL url;
        HttpsURLConnection mURLConnection = null;
        try {
            String uri = URL + param + "?language=en";
            url = new URL(uri);
            mURLConnection = (HttpsURLConnection) url.openConnection();
            mURLConnection.setDoInput(true);
            mURLConnection.setRequestMethod("GET");
            mURLConnection.setRequestProperty("auth", TOKEN);
            if (mURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(mURLConnection.getInputStream());
                response = readStream(in);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            mURLConnection.disconnect();
        }
        return response;
    }

    private PropertyDetail readStream(InputStream inputStream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        try {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                json.append(line);
            }

            PropertyDetail value = mMapper.readValue(json.toString(), PropertyDetail.class);
            return value;
        } catch (IOException e) {
            throw e;
        } finally {
            r.close();
            inputStream.close();
        }
    }

    @Override
    protected void onPostExecute(PropertyDetail propertyDetail) {
        super.onPostExecute(propertyDetail);
        mListener.onDataLoaded(propertyDetail);
    }

    public interface Listener {

        void onDataLoaded(PropertyDetail data);
    }
}
