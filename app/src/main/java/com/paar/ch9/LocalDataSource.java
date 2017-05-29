package com.paar.ch9;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;


public class LocalDataSource extends DataSource{
    private List<Marker> cachedMarkers = new ArrayList<Marker>();
    private static Bitmap icon = null;
    private static Bitmap gywanghwamunimg = null;
    private static Bitmap saejongdaewangimg = null;
    private static Bitmap isunshinimg = null;


    public LocalDataSource(Resources res) {
        if (res==null) throw new NullPointerException();

        createIcon(res);
    }

    protected void createIcon(Resources res) {
        if (res==null) throw new NullPointerException();

        icon=BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
        gywanghwamunimg = BitmapFactory.decodeResource(res, R.mipmap.gywanghwamunimg);
        saejongdaewangimg = BitmapFactory.decodeResource(res, R.mipmap.saejongdaewangimg);
        isunshinimg = BitmapFactory.decodeResource(res, R.mipmap.isunshinimg);
    }


    /* 커스텀 마커*/

    public List<Marker> getMarkers() {
        Marker starbucks = new IconMarker("광화문 스타벅스", 37.571247, 126.976327, 0, Color.GREEN, starbucksimg);
        cachedMarkers.add(starbucks);

        Marker gywanghwamun = new IconMarker("광화문", 37.575988, 126.977159, 0, Color.GREEN, icon);
        cachedMarkers.add(gywanghwamun);

        Marker saejongdaewang = new IconMarker("세종대왕 동상", 37.572971, 126.976881, 0, Color.GREEN, saejongdaewangimg);
        cachedMarkers.add(saejongdaewang);

        Marker isunshin = new IconMarker("이순신 장군 동상",37.571054, 126.976944, 0, Color.GREEN, isunshinimg);
        cachedMarkers.add(isunshin);

        Marker atl = new IconMarker("ATL", 39.931269, -75.051261, 0, Color.DKGRAY, icon);
        cachedMarkers.add(atl);

        Marker home = new Marker("Mt Laurel", 39.95, -74.9, 0, Color.YELLOW);
        cachedMarkers.add(home);

        return cachedMarkers;
    }
}