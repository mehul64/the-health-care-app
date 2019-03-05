package com.thc.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.thc.app.R;
import com.thc.app.utils.AppSharedPreference;
import com.thc.app.utils.VolleySingleton;

import java.util.Random;

public class OTPActivity extends AppCompatActivity {

    int rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        sendOTP();

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=((EditText)findViewById(R.id.otp)).getText().toString();
                Log.e("OTP",text);
                if(text.isEmpty())
                    Toast.makeText(OTPActivity.this,"Enter OTP",Toast.LENGTH_SHORT).show();
                else
                {
                    if(text.equals(""+rand))
                    {
                        new AppSharedPreference(OTPActivity.this).setLoggedIn(true);
                        Toast.makeText(OTPActivity.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OTPActivity.this,DashboardActivity.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(OTPActivity.this,"Wrong OTP",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void sendOTP() {

        rand= (int)(Math.random()*9000)+1000;

        String url="http://manage.staticking.net/index.php/smsapi/httpapi/?uname=ankit01&password=info@3030&sender=PFAITH&receiver="+getIntent().getStringExtra("phone")+"&route=TA&msgtype=1&sms=Your%20OTP%20for%20registration%20on%20THC%20is%20"+rand+"%20please%20do%20not%20share%20it%20with%20anyone";

        Log.e("URL",url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("OTP res",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("OTP err",error.getMessage());
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        Toast.makeText(OTPActivity.this,"OTP sent",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        AppSharedPreference preference = new AppSharedPreference(this);
        preference.setLoggedIn(false);

        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
