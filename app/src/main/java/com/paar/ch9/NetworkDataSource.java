package com.paar.ch9;

import com.paar.ch9.model.Query;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class NetworkDataSource extends DataSource {

    protected static final int MAX = 1000;
    protected static final int READ_TIMEOUT = 10000;
    protected static final int CONNECT_TIMEOUT = 10000;

    protected List<Marker> markersCache = null;

    public abstract List<Marker> parse(Query query);

    public void setMarkersCache(List<Marker> markersCache) {
        this.markersCache = markersCache;
    }

    @Override
    public List<Marker> getMarkers() {
        return markersCache;
    }

    protected static InputStream getHttpGETInputStream(String urlStr) {
        if (urlStr == null)
            throw new NullPointerException();

        InputStream is = null;
        URLConnection conn = null;

        try {
            if (urlStr.startsWith("file://"))
                return new FileInputStream(urlStr.replace("file://", ""));

            URL url = new URL(urlStr);
            conn = url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECT_TIMEOUT);

            is = conn.getInputStream();

            return is;
        } catch (Exception ex) {
            try {
                is.close();
            } catch (Exception e) {
                // Ignore
            }
            try {
                if (conn instanceof HttpURLConnection)
                    ((HttpURLConnection) conn).disconnect();
            } catch (Exception e) {
                // Ignore
            }
            ex.printStackTrace();
        }

        return null;
    }

    protected String getHttpInputString(InputStream is) {
        if (is == null)
            throw new NullPointerException();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is),
                8 * 1024);
        StringBuilder sb = new StringBuilder();

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}