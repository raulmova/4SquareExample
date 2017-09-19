package com.example.raul.a4squareexample;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class RecycledList extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String CLIENT_ID = "GD0VVCZCQQENFQ5CFJX0JZIGASX4IYTGVENUE3OZXBPGA1ZT";
    private static final String CLIENT_SECRET = "W0TON35DDZITHSGWTR4AUMUVZKE034OEWK5PL3BI0SQFEHR5";
    private GoogleApiClient mGoogleApiClient;
    private Location location;
    private final int REQUEST_LOCATION = 1;
    private TextView tvLat;
    private  TextView tvLon;
    private RecyclerView rv;
    private LinearLayoutManager linearLayoutManager;
    private DummyData dd;
    public ArrayList<String> myArray = new ArrayList<String>();
    public ArrayList<String> myArray2 = new ArrayList<String>();
    public ArrayList<String> myArray3 = new ArrayList<String>();
    public ArrayList<String> myArray4 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycled_list);
        tvLat = (TextView) findViewById(R.id.tvLat);
        tvLon = (TextView) findViewById(R.id.tvLon);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        processLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (location != null) {
                    updateLocationUI();
                    Log.d("Permision Result", "---------");
                }
                else
                    Toast.makeText(this, "Ubicación no encontrada", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permisos no otorgados", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void processLocation() {
        // Se trata de obtener la última ubicación registrada
        getLocation();
        // Si ubicación es diferente de nulo se actualizan los campos para escribir la ubicación
        if (location != null) {
           // Log.d("Location: ",location.toString());
            updateLocationUI();
            Log.d("Process Location", "---------");
        }
    }

    private void getLocation() {
    // Se valida que haya permisos garantizados
            if (isLocationPermissionGranted()) {
    // Si los hay se regresa un objeto con información de ubicacion
                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                //Log.d("Location: ",location.toString());
            } else {
                requestPermission();
        }
    }

    private boolean isLocationPermissionGranted() {
    // Se obtiene el permiso de ACCESS_FINE_LOCATION
        int permission = ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "No quisiste dar acceso a tu ubicación", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(
                    this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }

    private void updateLocationUI() {
        tvLat.setText("LAT: "+String.valueOf(location.getLatitude()));
        tvLon.setText("LON: "+String.valueOf(location.getLongitude()));

        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);

        String url = "https://api.foursquare.com/v2/venues/search?client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&v=20130815&ll="+String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude())+"&limit=30";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
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
                                JSONObject temp3 = temp2.getJSONObject("stats");

                                JSONArray jsonArray2 = temp2.getJSONArray("categories");
                                JSONObject icon = jsonArray2.getJSONObject(0).getJSONObject("icon");

                                Log.d("ICON:",icon.toString());
                                String photo = icon.getString("prefix")+"bg_88"+icon.getString("suffix");
                                Log.d("PHOTOURL:",photo);
                                String id = temp2.get("id").toString();
                                String nameTemp = temp2.get("name").toString();
                                String idTemp = temp2.get("id").toString();
                                String checkins  = temp3.get("checkinsCount").toString();

                                Log.d("Name:",nameTemp );
                                Log.d("ID: ",idTemp);


                                myArray2.add(nameTemp);
                                myArray.add(checkins);
                                myArray3.add(photo);
                                myArray4.add(id);

                            }
                            RecycleViewCustomAdapter adapter = new RecycleViewCustomAdapter(getApplicationContext(),myArray2,myArray,myArray3, new com.example.raul.a4squareexample.RecyclerViewClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    //Toast.makeText(RecycledList.this, "Elemento " + position, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RecycledList.this,ScrollingActivity.class);
                                    intent.putExtra("position",position);
                                    intent.putStringArrayListExtra("ids",myArray4);
                                    intent.putStringArrayListExtra("names",myArray2);
                                    intent.putStringArrayListExtra("checkins",myArray);
                                    startActivity(intent);
                                    myArray2.clear();
                                    myArray.clear();
                                    myArray3.clear();
                                    myArray4.clear();
                                }
                            }
                            );
                            rv.setAdapter(adapter);
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
