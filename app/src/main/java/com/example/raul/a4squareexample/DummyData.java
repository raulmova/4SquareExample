package com.example.raul.a4squareexample;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Raul on 17/09/2017.
 */

public class DummyData {

    private static final String CLIENT_ID = "GD0VVCZCQQENFQ5CFJX0JZIGASX4IYTGVENUE3OZXBPGA1ZT";
    private static final String CLIENT_SECRET = "W0TON35DDZITHSGWTR4AUMUVZKE034OEWK5PL3BI0SQFEHR5";
    ArrayList<String> titleArray = new ArrayList<>();
    private Context c;
    private String lat;
    private String lon;

    public DummyData(Context c, String lat, String lon) {
        this.c = c;
        this.lat = lat;
        this.lon = lon;
    }

    public void getTitles() {

        String url = "https://api.foursquare.com/v2/venues/search?client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&v=20130815&ll="+lat+","+lon+"";
        RequestQueue queue = Volley.newRequestQueue(c);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONObject temp = jObj.getJSONObject("response");
                            JSONArray jArray = temp.getJSONArray("venues");
                            Log.d("Array Venues: ",jArray.toString());
                            Log.d("Array Size: ",""+jArray.length());
                            for (int i=0; i<jArray.length();i++){
                                JSONObject temp2 = jArray.getJSONObject(i);
                                String nameTemp = temp2.get("name").toString();
                                titleArray.add(nameTemp);
                            }

                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON:" + response);
                            Toast.makeText(c, " Error retrieving data " , Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", "Bad");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
     //   return titleArray;
    }

    public ArrayList<String> getTitleArray() {
        return titleArray;
    }
}
