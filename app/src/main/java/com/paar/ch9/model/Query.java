package com.paar.ch9.model;

import com.paar.ch9.TwitterDataSource;

public class Query {
    private double lat;
    private double lon;
    private double alt;
    private float radius;
    private String locale;

    public Query(double lat, double lon, double alt, float radius, String locale) {
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
        this.radius = radius;
        this.locale = locale;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getAlt() {
        return alt;
    }

    public float getRadius() {
        return radius;
    }

    public String getLocale() {
        return locale;
    }

    public twitter4j.Query toTwitterQuery() {
        return TwitterDataSource.createQuery(lat, lon, radius, locale);
    }
}
