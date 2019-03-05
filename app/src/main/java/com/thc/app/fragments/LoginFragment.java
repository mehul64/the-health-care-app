package com.thc.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.thc.app.BaseFragment;
import com.thc.app.R;
import com.thc.app.activities.DashboardActivity;
import com.thc.app.activities.LoginActivity;
import com.thc.app.activities.OTPActivity;
import com.thc.app.utils.AppSharedPreference;
import com.thc.app.utils.DataPreference;
import com.thc.app.utils.Urls;
import com.thc.app.utils.Utils;
import com.thc.app.utils.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment {

    @Bind(R.id.emailEditText)
    EditText emailEditText;
    @Bind(R.id.passwordEditText)
    EditText passwordEditText;
    @Bind(R.id.forgotPassTextView)
    TextView forgotPassTextView;
    @Bind(R.id.buttonSubmit)
    Button buttonSubmit;
    @Bind(R.id.registerTextView)
    TextView registerTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public String getFragmentTitle() {
        return "Login";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.buttonSubmit)
    public void onButtonSubmitClick() {
        if (doValidation()) {
            Utils.showProgressBar(getActivity(), "Validating...", false, null);
            sendLoginRequest();
/*
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference questionsListDatabaseRef = firebaseDatabase.getReference().child("users");

            questionsListDatabaseRef.orderByChild("email").equalTo(emailEditText.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Utils.hideProgressDialog();
                    if (dataSnapshot.getChildrenCount() > 0) {
                        DataSnapshot userSnapShot = null;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            userSnapShot = snapshot;
                        }
                        if (userSnapShot != null) {
                            UserData userData = userSnapShot.getValue(UserData.class);
                            if (passwordEditText.getText().toString().trim().equals(userData.getPassword())) {
                                AppSharedPreference preference = new AppSharedPreference(getActivity());
                                preference.setEmail(userData.getEmail());
                                preference.setFullName(userData.getName());
                                preference.setMobile(userData.getMobile());
                                preference.setLoggedIn(true);

                                preference.setAge(userData.getAge());
                                preference.setHeight(userData.getHeight());
                                preference.setGender(userData.getGender());

                                preference.setWeight(userData.getWeight());
                                preference.setBaseUrlIP(userSnapShot.getKey());
                                startActivity(new Intent(getActivity(), DashboardActivity.class));
                                getActivity().finish();
                            } else {
                                Utils.showThisMsg(getActivity(), "Error:", "Invalid Credentials", null, null);
                            }
                        } else {
                            Utils.showThisMsg(getActivity(), "Error:", "No user found", null, null);
                        }
                    } else {
                        Utils.showThisMsg(getActivity(), "Error:", "No user found", null, null);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Utils.hideProgressDialog();
                    Utils.showThisMsg(getActivity(), "Error:", "Something went wrong", null, null);
                }
            });
*/
        }
    }

    private void sendLoginRequest() {

        StringRequest stringRequset=new StringRequest(Request.Method.POST, Urls.login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login resp",response);
                try
                {
                    response=response.substring(3);
                    if(new JSONObject(response).getString("status").equals("success"))
                    {
                        JSONObject userData=new JSONObject(response);

                        AppSharedPreference preference = new AppSharedPreference(getActivity());
                        preference.setEmail(userData.getString("email"));
                        preference.setFullName(userData.getString("name"));
                        preference.setMobile(userData.getString("phoneno"));
                        preference.setLoggedIn(false);
                        preference.setAge(userData.getString("age"));
                        preference.setHeight(userData.getString("height"));
                        preference.setGender(userData.getString("gender"));
                        preference.setWeight(userData.getString("weight"));
                        preference.setBaseUrlIP(userData.getString("email"));

                        DataPreference dataPreference=new DataPreference(getActivity());
                        dataPreference.setDailyKilometersGoal(Double.valueOf(userData.getString("kmsrun")).longValue());
                        dataPreference.setDailyStepsGoal(Double.valueOf(userData.getString("steps")).longValue());
                        dataPreference.setDailyCaloriesGoal(Double.valueOf(userData.getString("dailycal")).longValue());

                        new AppSharedPreference(getActivity()).setLoggedIn(true);
                        Intent intent=new Intent(getActivity(),DashboardActivity.class);
                        intent.putExtra("phone",userData.getString("phoneno"));
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Incorrect email or password",Toast.LENGTH_SHORT).show();
                    }
                    Utils.hideProgressDialog();
                }
                catch (Exception e)
                {
                    Log.e("EXCC",e.getMessage());
                    Utils.hideProgressDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("login error",error.toString());
                Utils.hideProgressDialog();
                Toast.makeText(getActivity(),"Connection Problem",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("phoneno",emailEditText.getText().toString());
                map.put("password",passwordEditText.getText().toString());
                return map;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequset);
    }

    private boolean doValidation() {
        if (emailEditText.getText().toString().trim().length() == 0) {
            emailEditText.requestFocus();
            emailEditText.setError("Enter Phone No");
            return false;
        } else if (emailEditText.getText().toString().trim().length() != 10) {
            emailEditText.requestFocus();
            emailEditText.setError("Enter Mobile No of 10 digits");
            return false;
        } else if (passwordEditText.getText().toString().trim().length() == 0) {
            passwordEditText.requestFocus();
            passwordEditText.setError("Enter Password");
            return false;
        }

        return true;
    }

    @OnClick(R.id.registerTextView)
    public void onRegisterTextViewClick() {
        ((LoginActivity) getActivity()).showRegisterFragment();
    }

    @OnClick(R.id.forgotPassTextView)
    public void onForgotPassTextViewClick() {
        Utils.showToast(getActivity(), "Pending..");
    }
}
