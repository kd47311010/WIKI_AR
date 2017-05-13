package com.paar.ch9;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;

import com.paar.ch9.model.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WikipediaDataSource extends NetworkDataSource {
    private static final String BASE_URL = "http://ws.geonames.org/findNearbyWikipediaJSON";

    private static Bitmap icon = null;

    public WikipediaDataSource(@NonNull Resources res) {
        createIcon(res);
    }

    protected void createIcon(Resources res) {
        if (res == null) throw new NullPointerException();

        icon = BitmapFactory.decodeResource(res, R.drawable.wikipedia);
    }

    @Override
    public List<Marker> parse(Query query) {
        if (query == null) {
            Log.e("WikipediaDataSource", "Query is null");
            return null;
        }

        String url = BASE_URL +
                "?lat=" + query.getLat() +
                "&username=demo" +
                "&lng=" + query.getLon() +
                "&radius=" + query.getRadius() +
                "&maxRows=40" +
                "&lang=" + query.getLocale();

        if (url == null)
            throw new NullPointerException();

        InputStream stream = null;
        stream = getHttpGETInputStream(url);
        if (stream == null)
            throw new NullPointerException();

        String string = null;
        string = getHttpInputString(stream);
        if (string == null)
            throw new NullPointerException();

        JSONObject json = null;
        try {
            json = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json == null)
            throw new NullPointerException();

        List<Marker> markers = parse(json);

        setMarkersCache(markers);

        return markers;
    }

    private List<Marker> parse(JSONObject root) {
        if (root == null) return null;

        JSONObject jo = null;
        JSONArray dataArray = null;
        List<Marker> markers = new ArrayList<Marker>();

        try {
            if (root.has("geonames")) dataArray = root.getJSONArray("geonames");
            if (dataArray == null) return markers;
            int top = Math.min(MAX, dataArray.length());
            for (int i = 0; i < top; i++) {
                jo = dataArray.getJSONObject(i);
                Marker ma = processJSONObject(jo);
                if (ma != null) markers.add(ma);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return markers;
    }

    private Marker processJSONObject(JSONObject jo) {
        if (jo == null) return null;

        Marker ma = null;

        try {
            ma = new IconMarker(
                    jo.getString("title"),
                    jo.getDouble("lat"),
                    jo.getDouble("lng"),
                    0,
                    Color.WHITE,
                    icon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ma;
    }
}