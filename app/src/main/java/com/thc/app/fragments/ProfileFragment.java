package com.thc.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thc.app.BaseFragment;
import com.thc.app.R;
import com.thc.app.activities.DashboardActivity;
import com.thc.app.models.UserData;
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

/**
 * Created by aabid-personal on 9/21/16.
 */
public class ProfileFragment extends BaseFragment {

    @Bind(R.id.nameEditText)
    AppCompatEditText nameEditText;
    @Bind(R.id.mobileEditText)
    AppCompatEditText mobileEditText;
    @Bind(R.id.emailEditText)
    AppCompatEditText emailEditText;
    @Bind(R.id.buttonUpdate)
    Button buttonUpdate;
    @Bind(R.id.weightEditText)
    AppCompatEditText weightEditText;
    @Bind(R.id.heightEditText)
    AppCompatEditText heightEditText;
    @Bind(R.id.ageEditText)
    AppCompatEditText ageEditText;
    @Bind(R.id.genderSpinner)
    Spinner genderSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppSharedPreference appSharedPreference = new AppSharedPreference(getActivity());
        nameEditText.setText(appSharedPreference.getFullName());
        emailEditText.setText(appSharedPreference.getEmail());
        mobileEditText.setText(appSharedPreference.getMobile());

    }

    @Override
    public String getFragmentTitle() {
        return "Profile";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.buttonUpdate)
    public void onClick() {

        if (doValidation()) {

            Utils.showProgressBar(getActivity(), "Updating..", false, null);

/*
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference questionsListDatabaseRef = firebaseDatabase.getReference().child("users");
            DatabaseReference dataDatabaseRef1 = questionsListDatabaseRef.child(new AppSharedPreference(getActivity()).getBaseUrlIP());
            dataDatabaseRef1.child("name").setValue(nameEditText.getText().toString().trim());
            dataDatabaseRef1.child("mobile").setValue(mobileEditText.getText().toString().trim());
            dataDatabaseRef1.child("weight").setValue(weightEditText.getText().toString().trim());
            dataDatabaseRef1.child("age").setValue(ageEditText.getText().toString().trim());
            dataDatabaseRef1.child("gender").setValue(genderSpinner.getSelectedItemPosition() == 0 ? "male" : "female");
            dataDatabaseRef1.child("height").setValue(heightEditText.getText().toString().trim());
*/
            final StringRequest stringRequset=new StringRequest(Request.Method.POST, Urls.update_profile, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("signup resp",response);
                    try
                    {
                        if(new JSONObject(response).getString("status").equals("success"))
                        {
                            UserData userData = new UserData();
                            userData.setMobile(mobileEditText.getText().toString().trim());
                            userData.setName(nameEditText.getText().toString().trim());
                            userData.setEmail(emailEditText.getText().toString().trim());
                            userData.setWeight(weightEditText.getText().toString().trim());
                            userData.setAge(ageEditText.getText().toString().trim());
                            userData.setHeight(heightEditText.getText().toString().trim());
                            userData.setGender(genderSpinner.getSelectedItemPosition() == 0 ? "male" : "female");

                            AppSharedPreference preference = new AppSharedPreference(getActivity());
                            preference.setEmail(userData.getEmail());
                            preference.setFullName(userData.getName());
                            preference.setMobile(userData.getMobile());
                            preference.setWeight(userData.getWeight());
                            preference.setAge(userData.getAge());
                            preference.setHeight(userData.getHeight());
                            preference.setGender(userData.getGender());

                            DataPreference dataPreference=new DataPreference(getActivity());
                            dataPreference.setDailyKilometersGoal(Double.valueOf(new JSONObject(response).getString("kmsrun")).longValue());
                            dataPreference.setDailyStepsGoal(Double.valueOf(new JSONObject(response).getString("steps")).longValue());
                            dataPreference.setDailyCaloriesGoal(Double.valueOf(new JSONObject(response).getString("dailycal")).longValue());

                            preference.setBaseUrlIP(userData.getEmail());
                            preference.setLoggedIn(true);

                            Toast.makeText(getActivity(),"Updated Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(),DashboardActivity.class));
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Some error occurred",Toast.LENGTH_SHORT).show();
                        }
                        Utils.hideProgressDialog();
                    }
                    catch (Exception e)
                    {
                        Utils.hideProgressDialog();
                        Log.e("EXCC",e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("signup error",error.toString());
                    Utils.hideProgressDialog();
                    Toast.makeText(getActivity(),"Connection Problem",Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map=new HashMap<>();
                    map.put("name",nameEditText.getText().toString());
                    map.put("email",emailEditText.getText().toString());
                    map.put("weight",weightEditText.getText().toString());
                    map.put("age",ageEditText.getText().toString());
                    map.put("mobile",mobileEditText.getText().toString());
                    map.put("height",heightEditText.getText().toString());
                    map.put("gender",genderSpinner.getSelectedItemPosition() == 0 ? "male" : "female");
                    return map;
                }
            };
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequset);
        }
    }


    private boolean doValidation() {
        if (nameEditText.getText().toString().trim().length() == 0) {
            nameEditText.setError("Enter Name");
            return false;
        } else if (weightEditText.getText().toString().trim().length() == 0) {
            weightEditText.setError("Enter Weight");
            return false;
        } else if (weightEditText.getText().toString().trim().length() == 0) {
            weightEditText.setError("Enter Height");
            return false;
        } else if (weightEditText.getText().toString().trim().length() == 0) {
            weightEditText.setError("Enter Age");
            return false;
        } else if (mobileEditText.getText().toString().trim().length() == 0) {
            mobileEditText.setError("Enter Mobile");
            return false;
        } else if (emailEditText.getText().toString().trim().length() == 0) {
            emailEditText.setError("Enter Email");
            return false;
        }
        return true;
    }

}
