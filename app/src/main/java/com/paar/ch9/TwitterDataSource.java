package com.paar.ch9;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;

import com.paar.ch9.model.Query;

import java.util.ArrayList;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterDataSource extends NetworkDataSource {

    private static final String CONSUMER_KEY = "Lehict8oCdVgQpz6yxJh51sCU";

    private static final String CONSUMER_SECRET = "wJGzMPmJRGoFgLZH9QLz2l7zZo61FuXvO994pOodypI1mjWw9w";

    private static final String URL = "https://api.twitter.com/1.1/search/tweets.json";

    private static Bitmap icon = null;

    public TwitterDataSource(@NonNull Resources resources) {
        createIcon(resources);
    }

    protected void createIcon(Resources res) {
        if (res == null) throw new NullPointerException();

        icon = BitmapFactory.decodeResource(res, R.drawable.twitter);
    }

    public static twitter4j.Query createQuery(double lat, double lon, float radius, String lang) {
        GeoLocation location = new GeoLocation(lat, lon);

        return new twitter4j.Query()
                .geoCode(location, radius, twitter4j.Query.KILOMETERS.name())
                .lang(lang);
    }

    @Override
    public List<Marker> parse(Query query) {
        if (query == null) {
            Log.e("TwitterDataSource", "Query is null");
            return null;
        }

        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

        AccessToken accessToken = null;
        RequestToken requestToken = null;
        QueryResult result = null;

        try {
            requestToken = twitter.getOAuthRequestToken();

            Log.i("TwitterDataSource", requestToken.getAuthorizationURL());

            accessToken = twitter.getOAuthAccessToken(requestToken);
            result = twitter.search(query.toTwitterQuery());
        } catch (TwitterException e) {
            Log.e("TwitterDataSource", "Unable to get the access token", e);
            return null;
        }

        if (result == null) return null;

        List<Marker> markers = new ArrayList<>();
        for (Status status : result.getTweets()) {
            User user = status.getUser();
            GeoLocation location = status.getGeoLocation();

            IconMarker marker = new IconMarker(
                    user.getScreenName(),
                    location.getLatitude(),
                    location.getLongitude(),
                    0,
                    Color.RED,
                    icon
            );

            markers.add(marker);
        }

        // Caching markers
        setMarkersCache(markers);

        return markers;
    }

//    private Marker processJSONObject(JSONObject jo) {
//        if (jo == null) throw new NullPointerException();
//
//        if (!jo.has("geo")) throw new NullPointerException();
//
//        Marker ma = null;
//        try {
//            Double lat = null, lon = null;
//
//            if (!jo.isNull("geo")) {
//                JSONObject geo = jo.getJSONObject("geo");
//                JSONArray coordinates = geo.getJSONArray("coordinates");
//                lat = Double.parseDouble(coordinates.getString(0));
//                lon = Double.parseDouble(coordinates.getString(1));
//            } else if (jo.has("location")) {
//                Pattern pattern = Pattern.compile("\\D*([0-9.]+),\\s?([0-9.]+)");
//                Matcher matcher = pattern.matcher(jo.getString("location"));
//
//                if (matcher.find()) {
//                    lat = Double.parseDouble(matcher.group(1));
//                    lon = Double.parseDouble(matcher.group(2));
//                }
//            }
//            if (lat != null) {
//                String user = jo.getString("from_user");
//
//                ma = new IconMarker(
//                        user + ": " + jo.getString("text"),
//                        lat,
//                        lon,
//                        0,
//                        Color.RED,
//                        icon);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ma;
//    }
}