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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thc.app.BaseFragment;
import com.thc.app.R;
import com.thc.app.activities.DashboardActivity;
import com.thc.app.activities.LoginActivity;
import com.thc.app.activities.OTPActivity;
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
import timber.log.Timber;

public class RegisterFragment extends BaseFragment {

    @Bind(R.id.nameEditText)
    EditText nameEditText;
    @Bind(R.id.mobileEditText)
    EditText mobileEditText;
    @Bind(R.id.emailEditText)
    EditText emailEditText;
    @Bind(R.id.passwordEditText)
    EditText passwordEditText;
    @Bind(R.id.weightEditText)
    EditText weightEditText;


    @Bind(R.id.heightEditText)
    EditText heightEditText;
    @Bind(R.id.ageEditText)
    EditText ageEditText;
    @Bind(R.id.genderSpinner)
    Spinner spinnerGender;

    @Bind(R.id.buttonSubmit)
    Button buttonSubmit;
    @Bind(R.id.loginTextView)
    TextView loginTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public String getFragmentTitle() {
        return "Register";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.buttonSubmit)
    public void onButtonSubmitClick() {
        if (doValidation()) {

            Utils.showProgressBar(getActivity(), "Registering..", false, null);
            sendSignupRequest();
/*
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference questionsListDatabaseRef = firebaseDatabase.getReference().child("users");

            questionsListDatabaseRef.orderByChild("email").equalTo(emailEditText.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Utils.hideProgressDialog();
                    if (dataSnapshot.getChildrenCount() > 0) {
                        Utils.showThisMsg(getActivity(), "Error:", "Email already present", null, null);
                    } else {

                        UserData userData = new UserData();
                        userData.setMobile(mobileEditText.getText().toString().trim());
                        userData.setName(nameEditText.getText().toString().trim());
                        userData.setEmail(emailEditText.getText().toString().trim());
                        userData.setPassword(passwordEditText.getText().toString().trim());
                        userData.setWeight(weightEditText.getText().toString().trim());

                        userData.setAge(ageEditText.getText().toString().trim());
                        userData.setHeight(heightEditText.getText().toString().trim());
                        userData.setGender(spinnerGender.getSelectedItemPosition() == 0 ? "male" : "female");

                        AppSharedPreference preference = new AppSharedPreference(getActivity());
                        preference.setEmail(userData.getEmail());
                        preference.setFullName(userData.getName());
                        preference.setMobile(userData.getMobile());
                        preference.setWeight(userData.getWeight());
                        preference.setAge(userData.getAge());
                        preference.setHeight(userData.getHeight());
                        preference.setGender(userData.getGender());

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference questionsListDatabaseRef = firebaseDatabase.getReference().child("users");
                        DatabaseReference reference = questionsListDatabaseRef.push();
                        String userId = reference.getKey();
                        Timber.e("********* Created: " + userId);
                        reference.setValue(userData);

                        preference.setBaseUrlIP(userId);
                        preference.setLoggedIn(true);


                        startActivity(new Intent(getActivity(), DashboardActivity.class));
                        getActivity().finish();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Utils.hideProgressDialog();
                    Utils.showToast(getActivity(), "Something went wrong");
                }
            });
*/

        }
    }

    private void sendSignupRequest() {

        final StringRequest stringRequset=new StringRequest(Request.Method.POST, Urls.signup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("signup resp",response);
                try
                {
                    response=response.substring(3);
                    if(new JSONObject(response).getString("status").equals("success"))
                    {
                        UserData userData = new UserData();
                        userData.setMobile(mobileEditText.getText().toString().trim());
                        userData.setName(nameEditText.getText().toString().trim());
                        userData.setEmail(emailEditText.getText().toString().trim());
                        userData.setPassword(passwordEditText.getText().toString().trim());
                        userData.setWeight(weightEditText.getText().toString().trim());
                        userData.setAge(ageEditText.getText().toString().trim());
                        userData.setHeight(heightEditText.getText().toString().trim());
                        userData.setGender(spinnerGender.getSelectedItemPosition() == 0 ? "male" : "female");

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
                        preference.setLoggedIn(false);

                        new AppSharedPreference(getActivity()).setLoggedIn(true);
                        Intent intent=new Intent(getActivity(),DashboardActivity.class);
                        intent.putExtra("phone",userData.getMobile());
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Email already exists",Toast.LENGTH_SHORT).show();
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
                map.put("password",passwordEditText.getText().toString());
                map.put("weight",weightEditText.getText().toString());
                map.put("age",ageEditText.getText().toString());
                map.put("mobile",mobileEditText.getText().toString());
                map.put("height",heightEditText.getText().toString());
                map.put("gender",spinnerGender.getSelectedItemPosition() == 0 ? "male" : "female");
                return map;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequset);

    }


    private boolean doValidation() {
        if (nameEditText.getText().toString().trim().length() == 0) {
            nameEditText.requestFocus();
            nameEditText.setError("Enter Name");
            return false;
        } else if (weightEditText.getText().toString().trim().length() == 0) {
            weightEditText.requestFocus();
            weightEditText.setError("Enter Weight");
            return false;
        } else if (Integer.parseInt(weightEditText.getText().toString())==0) {
            weightEditText.requestFocus();
            weightEditText.setError("Weight must be greater than 0");
            return false;
        } else if (heightEditText.getText().toString().trim().length() == 0) {
            heightEditText.requestFocus();
            heightEditText.setError("Enter Height");
            return false;
        } else if (Integer.parseInt(heightEditText.getText().toString())==0) {
            heightEditText.requestFocus();
            heightEditText.setError("Weight must be greater than 0");
            return false;
        } else if (ageEditText.getText().toString().trim().length() == 0) {
            ageEditText.requestFocus();
            ageEditText.setError("Enter Age");
            return false;
        } else if (Integer.parseInt(ageEditText.getText().toString())==0) {
            ageEditText.requestFocus();
            ageEditText.setError("Age must be greater than 0");
            return false;
        } else if (mobileEditText.getText().toString().trim().length() == 0) {
            mobileEditText.setError("Enter Mobile");
            return false;
        } else if (mobileEditText.getText().toString().trim().length() != 10) {
            mobileEditText.requestFocus();
            mobileEditText.setError("Enter Mobile No of 10 digits");
            return false;
        } else if (emailEditText.getText().toString().trim().length() == 0) {
            emailEditText.requestFocus();
            emailEditText.setError("Enter Email");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString().trim()).matches()) {
            emailEditText.requestFocus();
            emailEditText.setError("Enter Valid Email");
            return false;
        } else if (passwordEditText.getText().toString().trim().length() == 0) {
            passwordEditText.requestFocus();
            passwordEditText.setError("Enter Password");
            return false;
        }
        return true;
    }

    @OnClick(R.id.loginTextView)
    public void onLoginTextViewClick() {
        ((LoginActivity) getActivity()).showLoginFragment();
    }
}
