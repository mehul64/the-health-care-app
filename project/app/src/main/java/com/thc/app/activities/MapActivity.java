package com.thc.app.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.thc.app.BaseActivity;
import com.thc.app.R;
import com.thc.app.database.GpsLocationTable;
import com.thc.app.utils.DataPreference;
import com.thc.app.utils.Utils;
import com.thc.app.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private GoogleMap googleMap;
    private ArrayList<LatLng> positionList = new ArrayList<LatLng>();
    private List<GpsLocationTable> gpsLocationModelList;
    private Activity activity;
    private GoogleApiClient mGoogleLocationApiClient;
    private String TAG = "MapActivity";

    float dist=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        activity = this;

        gpsLocationModelList = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        mGoogleLocationApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleLocationApiClient.connect();

//        initUIType();
    }

    private void initUIType() {
        String journeyId = new DataPreference(activity).getJourneyId();
        Log.d(TAG, journeyId);
        gpsLocationModelList = GpsLocationTable.getAllGpsLocation(journeyId);
        Log.d(TAG, "Found: Count " + gpsLocationModelList.size());
        if (googleMap != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Utils.showToast(activity, "Permission Error");
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            if (gpsLocationModelList.size() > 0) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(gpsLocationModelList.get(0).getLatitude(), gpsLocationModelList.get(0).getLongitude())));

                try {
                    zoomMapType();
                } catch (Exception ex) {
                    Utils.showToast(MapActivity.this, "Exception while Zooming Map");
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(gpsLocationModelList.get(0)
                            .getLatitude(), gpsLocationModelList.get(0).getLongitude())));
                }
                drawPath();
            }
        } else {
            Utils.showToast(activity, "Error while loading Maps, please try again later!");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void drawPath() {
        int lowSpeedColor = getResources().getColor(R.color.low_speed);
        if (gpsLocationModelList.size() > 0) {
            for (int i = 0; i < gpsLocationModelList.size() - 2; i++) {
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(lowSpeedColor);
                polylineOptions.width(10);
                polylineOptions.add(new LatLng(gpsLocationModelList.get(i).getLatitude(), gpsLocationModelList.get(i).getLongitude()));
                polylineOptions.add(new LatLng(gpsLocationModelList.get(i + 1).getLatitude(), gpsLocationModelList.get(i + 1).getLongitude()));
                googleMap.addPolyline(polylineOptions);
            }
        }
    }

    private void zoomMapType() {
        LatLng start = new LatLng(gpsLocationModelList.get(0).getLatitude(), gpsLocationModelList.get(0).getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, null);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            startLocationUpdates();
            Log.d(TAG, "INIT 2");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleLocationApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleLocationApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {

//        Toast.makeText(this,location.getAccuracy()+"",Toast.LENGTH_SHORT).show();

        if(((int)location.getAccuracy())<=20)
        {
            if(gpsLocationModelList.size()<2)
            {
                plotParks(location.getLatitude()+","+location.getLongitude());
                plotRestaurants(location.getLatitude()+","+location.getLongitude());
                gpsLocationModelList.add(new GpsLocationTable("",location.getLatitude()
                        ,location.getLongitude(),System.currentTimeMillis()+""));
            }
            else
            {
                double d=distance(gpsLocationModelList.get(gpsLocationModelList.size()-1).getLatitude()
                        ,location.getLatitude(),
                        gpsLocationModelList.get(gpsLocationModelList.size()-1).getLongitude()
                        ,location.getLongitude(),0,0)/1000;

                int lowSpeedColor = getResources().getColor(R.color.low_speed);
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(lowSpeedColor);
                polylineOptions.width(10);
                polylineOptions.add(new LatLng(gpsLocationModelList.get(gpsLocationModelList.size()-1).getLatitude(),
                        gpsLocationModelList.get(gpsLocationModelList.size()-1).getLongitude()));
                gpsLocationModelList.add(new GpsLocationTable("",location.getLatitude(),location.getLongitude(),System.currentTimeMillis()+""));
                polylineOptions.add(new LatLng(gpsLocationModelList.get(gpsLocationModelList.size()-1).getLatitude(),
                        gpsLocationModelList.get(gpsLocationModelList.size()-1).getLongitude()));
                googleMap.addPolyline(polylineOptions);


                Log.e("D",d+"");
                dist=dist+((float) d);
                Log.e("DIST",dist+"");


                ((TextView)findViewById(R.id.dist)).setText(dist+" kms travelled");

                Log.e("LOC","line drawn");
            }
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),20));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleLocationApiClient.disconnect();
    }

    private void plotRestaurants(String loc) {

        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+loc+"&radius=5000&type=restaurant&key=AIzaSyDaelheRmAL2yu6qshlSz4yH0xsic7_-E0";

        StringRequest stringrequest=new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
//                            Log.e("RESTRES",response);

                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("results");
                            for(int i=0;i<3;i++)
                            {
                                JSONObject location=jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                                double lat=location.getDouble("lat");
                                double lon=location.getDouble("lng");
                                MarkerOptions mo=new MarkerOptions();
                                googleMap.addMarker(mo.position(new LatLng(lat,lon)).title(jsonArray.getJSONObject(i).getString("name")));
                            }
                        }
                        catch (Exception e)
                        {
                            Log.e("RESTEXCC",e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.e("RESTERR",error.getMessage());
                    }
                }){
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringrequest);
    }

    private void plotParks(String loc) {

        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+loc+"&radius=5000&keyword=parks&key=AIzaSyDaelheRmAL2yu6qshlSz4yH0xsic7_-E0";

        StringRequest stringrequest=new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
//                            Log.e("RESTRES",response);

                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("results");
                            for(int i=0;i<3;i++)
                            {
                                JSONObject location=jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                                double lat=location.getDouble("lat");
                                double lon=location.getDouble("lng");
                                MarkerOptions mo=new MarkerOptions();
                                mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                googleMap.addMarker(mo.position(new LatLng(lat,lon)).title(jsonArray.getJSONObject(i).getString("name")));
                            }
                        }
                        catch (Exception e)
                        {
                            Log.e("RESTEXCC",e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.e("RESTERR",error.getMessage());
                    }
                }){
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringrequest);
    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

}
