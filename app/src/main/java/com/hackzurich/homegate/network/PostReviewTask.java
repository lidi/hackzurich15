package com.hackzurich.homegate.network;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackzurich.homegate.model.RatingRequest;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PostReviewTask extends AsyncTask<RatingRequest, Void, Void> {

    public static final String URL
            = "http://46.101.209.165:5000/api/v1.0/ratings/rating/";
    private ObjectMapper mMapper;
    private Listener mListener;

    public PostReviewTask(Listener listener) {
        mListener = listener;
    }

    @Override
    protected Void doInBackground(RatingRequest... params) {
        mMapper = new ObjectMapper();
        java.net.URL url;
        HttpURLConnection mURLConnection = null;
        OutputStream os = null;
        BufferedWriter writer = null;
        try {
            RatingRequest param = params[0];
            String uri = URL + param.getId();
            url = new URL(uri);
            mURLConnection = (HttpURLConnection) url.openConnection();
            mURLConnection.setDoInput(true);
            mURLConnection.setDoOutput(true);
            mURLConnection.setRequestMethod("POST");
            mURLConnection.setRequestProperty("Content-Type", "application/json");
            os = mURLConnection.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getBody(param));
            writer.flush();
            if (mURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(mURLConnection.getInputStream());
                ResultSuccess response = readStream(in);
            }
            writer.close();
            os.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            mURLConnection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void value) {
        super.onPostExecute(value);
        mListener.onDataPosted();
    }

    public interface Listener {

        void onDataPosted();
    }

    private ResultSuccess readStream(InputStream in) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        try {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                json.append(line);
            }

            return mMapper.readValue(json.toString(), ResultSuccess.class);
        } catch (IOException e) {
            throw e;
        } finally {
            r.close();
            in.close();
        }
    }

    public String getBody(RatingRequest request) {
        try {
            return mMapper.writeValueAsString(request);
        } catch (JsonProcessingException exception) {
            /* You have a bug! */
            throw new IllegalArgumentException("Unable to parse the body to json.", exception);
        }
    }

    public static class ResultSuccess {

        @JsonProperty("result")
        String mResult;

        public String getResult() {
            return mResult;
        }

        public void setResult(String result) {
            mResult = result;
        }
    }
}
