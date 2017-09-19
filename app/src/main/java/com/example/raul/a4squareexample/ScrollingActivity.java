package com.example.raul.a4squareexample;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScrollingActivity extends AppCompatActivity {

    ArrayList<String> ids = new ArrayList<String>();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> checkins = new ArrayList<String>();
    int position;
    private static final String CLIENT_ID = "GD0VVCZCQQENFQ5CFJX0JZIGASX4IYTGVENUE3OZXBPGA1ZT";
    private static final String CLIENT_SECRET = "W0TON35DDZITHSGWTR4AUMUVZKE034OEWK5PL3BI0SQFEHR5";
    private ImageView ivPhoto;
    private TextView tvAddress;
    private TextView tvNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        ids = getIntent().getStringArrayListExtra("ids");
        position = getIntent().getIntExtra("position",0);
        names = getIntent().getStringArrayListExtra("names");
        checkins = getIntent().getStringArrayListExtra("checkins");
        ivPhoto = (ImageView) findViewById(R.id.imageView2);
        tvAddress = (TextView)findViewById(R.id.tvDetail);
        tvNum = (TextView)findViewById(R.id.tvNum);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(names.get(position));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Check Ins : "+checkins.get(position), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        String url = "https://api.foursquare.com/v2/venues/"+ids.get(position)+"?client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&v=20130815";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONObject temp2 = jObj.getJSONObject("response").getJSONObject("venue").getJSONObject("photos");
                            Log.d("Photos",temp2.toString());
                            if(temp2.getInt("count") == 0){
                                //ivPhoto.setImageResource(R.drawable.default);
                                ivPhoto.setImageResource(R.drawable.c);
                                tvAddress.setText("Information Not Available");
                                tvNum.setText("Information Not Available");
                            }
                            else{
                                Log.d("JSON:",jObj.toString());
                                JSONObject temp = jObj.getJSONObject("response").getJSONObject("venue").getJSONObject("location");
                                //JSONObject temp2 = jObj.getJSONObject("response").getJSONObject("venue").getJSONObject("contact");
                                Log.d("RESPONSE",temp.toString());
                                tvAddress.setText(temp.getString("address"));
                                tvNum.setText(temp.getString("city"));
                                //JSONArray jAr = temp2.getJSONArray("groups").getJSONObject(0);
                                String prefix = temp2.getJSONArray("groups").getJSONObject(0).getJSONArray("items").getJSONObject(0).getString("prefix");
                                String suffix = temp2.getJSONArray("groups").getJSONObject(0).getJSONArray("items").getJSONObject(0).getString("suffix");
                                String width = temp2.getJSONArray("groups").getJSONObject(0).getJSONArray("items").getJSONObject(0).getString("width");
                                Log.d("Url:",prefix+"width"+width+suffix);
                                String url = prefix+"width"+width+suffix;
                                Picasso.with(getApplicationContext()).load(url).skipMemoryCache().fit().into(ivPhoto);
                            }

                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON:" + response);
                            Toast.makeText(getApplicationContext(), " Error retrieving data " , Toast.LENGTH_LONG).show();
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

    }
}
