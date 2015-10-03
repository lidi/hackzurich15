package com.hackzurich.homegate.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackzurich.homegate.model.PropertiesResponse;
import com.hackzurich.homegate.model.Property;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class LoadPropertiesTask extends AsyncTask<Object, Void, List<Property>> {

    public static final String TOKEN= "20c7209eda57e82b828027be7fca0e02";
    public static final String URL= "https://api-2445581357976.apicast.io:443/rs/real-estates?language=en&chooseType=rentflat";

    Listener mListener;
    protected final ObjectMapper mMapper;

    public LoadPropertiesTask(Listener listener) {
        mListener = listener;
        mMapper = new ObjectMapper();
    }

    @Override
    protected List<Property> doInBackground(Object[] params) {
        java.net.URL url = null;
            HttpsURLConnection mURLConnection = null;
        try {
            url = new URL(URL);
            mURLConnection = (HttpsURLConnection) url.openConnection();
            mURLConnection.setDoInput(true);
            mURLConnection.setRequestMethod("GET");
            mURLConnection.setRequestProperty("auth", TOKEN);
            if (mURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(mURLConnection.getInputStream());
                PropertiesResponse response =  readStream(in);
                return response.getItems();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            mURLConnection.disconnect();
        }
        return null;
    }

    private PropertiesResponse readStream(InputStream inputStream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        try {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                json.append(line);
            }

            PropertiesResponse value = mMapper.readValue(json.toString(), PropertiesResponse.class);
            return value;
        } catch (IOException e) {
            throw e;
        } finally {
            r.close();
            inputStream.close();
        }
    }

    @Override
    protected void onPostExecute(List<Property> o) {
        super.onPostExecute(o);
        mListener.onDataLoaded(o);
    }

    public interface Listener{
        void onDataLoaded(List<Property> data);
    }
}
