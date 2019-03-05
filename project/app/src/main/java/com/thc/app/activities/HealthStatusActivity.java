package com.thc.app.activities;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.fitness.data.DataPoint;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.thc.app.Interfaces.MyDataPoint;
import com.thc.app.R;
import com.thc.app.utils.AppSharedPreference;
import com.thc.app.utils.DataPreference;
import com.thc.app.utils.Urls;
import com.thc.app.utils.Utils;
import com.thc.app.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HealthStatusActivity extends AppCompatActivity{

    String[] dates=null;
    MyDataPoint[] points=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_status);

        getHealthStatus();

    }

    public void getHealthStatus()
    {
        Utils.showProgressBar(this,"Analyzing health",false,null);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Urls.health,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            Log.e("health res",response);
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("status").equals("success"))
                            {
                                ((TextView)findViewById(R.id.status))
                                        .setText("HEALTH STATUS: "+jsonObject.getString("health").toUpperCase());

                                JSONArray jsonArray=jsonObject.getJSONArray("graph_data");
                                dates=new String[jsonArray.length()];
                                points=new MyDataPoint[jsonArray.length()];
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    dates[i]=jsonArray.getJSONObject(i).getString("date");
                                    points[i]=new MyDataPoint(i,jsonArray.getJSONObject(i).getString("health"));
                                }

                                createGraph();

                                inflateButtonLayout(response);

                                if(!jsonObject.getString("walking_required").equals("-1"))
                                {
                                    ((TextView)findViewById(R.id.exercise))
                                            .setText(jsonObject.getString("walking_required"));
//                                    ((TextView)findViewById(R.id.exercise))
//                                            .setText("Walking Required: "+jsonObject.getString("walking_required"));
//                                    ((TextView)findViewById(R.id.exercise))
//                                            .append("\nRunning Required: "+jsonObject.getString("running_required"));
                                }
                                else
                                {
                                    ((TextView)findViewById(R.id.exercise))
                                            .setText("No extra exercise required");
                                }
                            }
                            else
                            {
                                ((TextView)findViewById(R.id.status))
                                        .setText("Not enough data, please try after 1 day");
                            }
                            Utils.hideProgressDialog();
                        }
                        catch (Exception e) {
                            Log.e("health res excc",e.getMessage());
                            Utils.hideProgressDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("health err",error.toString());
                Utils.hideProgressDialog();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("email",new AppSharedPreference(HealthStatusActivity.this).getEmail());
                return map;
            }
        };
        VolleySingleton.getInstance(HealthStatusActivity.this).addToRequestQueue(stringRequest);

    }

    private void inflateButtonLayout(final String response) throws Exception {

        final JSONArray jsonArray=new JSONObject(response).getJSONArray("health_data");

        findViewById(R.id.btn_layout).setVisibility(View.VISIBLE);

        findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer(jsonArray);
                findViewById(R.id.btn_layout).setVisibility(View.GONE);
            }
        });

        findViewById(R.id.not_ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(HealthStatusActivity.this);
                b.setTitle("What is your correct condition");
                final String[] types = {"good","average","bad"};
                b.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try
                        {
                            JSONArray jsonArray=new JSONObject(response).getJSONArray("health_data");
                            jsonArray.put(jsonArray.length()-1,types[which]);
                            sendDataToServer(jsonArray);
                        }
                        catch (Exception e)
                        {
                            Log.e("dialog exc",e.getMessage());
                        }
                        dialog.dismiss();
                        findViewById(R.id.btn_layout).setVisibility(View.GONE);
                    }
                });
                b.show();
            }
        });

    }

    private void sendDataToServer(final JSONArray jsonArray) {

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.insert, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("insert res", response);
                    Toast.makeText(HealthStatusActivity.this,"Thank you for the feedback",Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("insert err", error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    try {
                        Map<String, String> map = new HashMap<>();
                        map.put("val1", jsonArray.getString(0));
                        map.put("val2", jsonArray.getString(1));
                        map.put("val3", jsonArray.getString(2));
                        map.put("val4", jsonArray.getString(3));
                        map.put("val5", jsonArray.getString(4));
                        map.put("val6", jsonArray.getString(5));
                        map.put("val7", jsonArray.getString(6));
                        return map;
                    }
                    catch (Exception e)
                    {
                        Log.e("map exc",e.getMessage());
                        return null;
                    }
                }
            };

            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }
        catch (Exception e)
        {
            Log.e("insert exc",e.getMessage());
        }
    }

    public void createGraph()
    {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<MyDataPoint> series = new LineGraphSeries<>(points);
/*
        LineGraphSeries<MyDataPoint> series = new LineGraphSeries<>(new MyDataPoint[] {
                new MyDataPoint(0, "GOOD"),
                new MyDataPoint(1, "AVERAGE"),
                new MyDataPoint(2, "POOR"),
                new MyDataPoint(3, "GOOD"),
                new MyDataPoint(4, "POOR"),
        });
*/
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(20);
        series.setThickness(8);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(points.length);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.addSeries(series);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(dates);
        staticLabelsFormatter.setVerticalLabels(new String[] {"BAD", "AVG", "GOOD"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getGridLabelRenderer().setLabelsSpace(1);
        graph.getViewport().setScrollable(true);
    }
}
