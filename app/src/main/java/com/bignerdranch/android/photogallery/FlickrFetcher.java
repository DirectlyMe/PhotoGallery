package com.bignerdranch.android.photogallery;

import android.net.Uri;
import android.util.Log;
import android.widget.Gallery;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 2/13/2018.
 */

public class FlickrFetcher {

    private static final String TAG = "FlickrFetcher";

    private static final String API_KEY = "ec1014de589f799bbfa048579478bda4";

    private int pageNumber = 1;

    public List<GalleryItem> fetchItems(int page) {

        List<GalleryItem> items = new ArrayList<>();

        pageNumber = page;

        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .appendQueryParameter("page", String.valueOf(page))
                    .build().toString();

            String jsonString = getURLString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        }
        catch (IOException ioe) {
            Log.e(TAG , "Failed to fetch items", ioe);
        }
        catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return items;
    }

    public byte[] getURLBytes(String urlSpec) throws IOException {

        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        }
        finally {
            connection.disconnect();
        }
    }

    public String getURLString(String urlSpec) throws IOException {
        return new String(getURLBytes(urlSpec));
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonBody) throws IOException,
            JSONException {

        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<GalleryItem>>() {}.getType();

        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

        String jsonPhotoList = photoJsonArray.toString();

        List<GalleryItem> galleryItemList = gson.fromJson(jsonPhotoList, listType);

        items.addAll(galleryItemList);

    }
}
