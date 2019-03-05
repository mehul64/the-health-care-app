package com.thc.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thc.app.BaseFragment;
import com.thc.app.R;
import com.thc.app.models.OneDayData;
import com.thc.app.models.UserData;
import com.thc.app.utils.AppConstants;
import com.thc.app.utils.AppSharedPreference;
import com.thc.app.utils.DataPreference;
import com.thc.app.utils.Urls;
import com.thc.app.utils.Utils;
import com.thc.app.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aabid-personal on 9/21/16.
 */
public class HistoryFragment extends BaseFragment {

    @Bind(R.id.listView)
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Utils.showProgressBar(getActivity(), "Validating...", false, null);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Urls.history,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            response=response.substring(3);
                            Log.e("history res",response);
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("status").equals("success"))
                            {
                                HashMap<String,OneDayData> activities=new HashMap<>();
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    OneDayData oneDayData=new Gson().fromJson(jsonArray.getJSONObject(i).toString(),OneDayData.class);
                                    activities.put(""+i,oneDayData);
                                }
                                updateList(activities);
                            }
                            Utils.hideProgressDialog();
                        }
                        catch (Exception e) {
                            Log.e("history res excc",e.getMessage());
                            Utils.hideProgressDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("history err",error.toString());
                Utils.hideProgressDialog();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("email",new AppSharedPreference(getActivity()).getEmail());
                map.put("height",new AppSharedPreference(getActivity()).getHeight());
                map.put("weight",new AppSharedPreference(getActivity()).getWeight());
                return map;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


/*
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference questionsListDatabaseRef = firebaseDatabase.getReference().child("users");

        questionsListDatabaseRef.orderByChild("email").equalTo(new AppSharedPreference(getActivity()).getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
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

                        updateList(userData.getActivities());

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

    private void updateList(HashMap<String, OneDayData> activities) {

        ArrayList<OneDayData> list = new ArrayList<>(activities.values());
        listView.setAdapter(new HistoryBaseAdapter(list, getActivity()));
    }

    @Override
    public String getFragmentTitle() {
        return "History";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    class HistoryBaseAdapter extends BaseAdapter {

        private final LayoutInflater mLayoutInflater;
        ArrayList<OneDayData> travelDataModels = new ArrayList<>();
        Context context;

        public HistoryBaseAdapter(ArrayList<OneDayData> travelDataModels, Context context) {
            this.travelDataModels = travelDataModels;
            this.context = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return travelDataModels.size();
        }

        @Override
        public OneDayData getItem(int position) {
            return travelDataModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.layout_history, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            DataPreference dataPreference=new DataPreference(getActivity());
            viewHolder.runningValue.setText(AppConstants.getRunningValue(new DataPreference(getActivity()).showInKiloMeter(), getItem(position).getRunning()) + "/"+dataPreference.getKilometersGoal());
            viewHolder.walkingValue.setText(AppConstants.getWalking(getItem(position).getWalking())+ "/"+dataPreference.getStepsGoal());
            viewHolder.idleValue.setText(AppConstants.getOtherActivityTime(getItem(position).getOther())+"");
            viewHolder.mealBreakfast.setText(getItem(position).getBreakFast());
            viewHolder.mealBreakfastCal.setText(getItem(position).getBreakFastCal() + "");
            viewHolder.mealLunch.setText(getItem(position).getLunch());
            viewHolder.mealLunchCal.setText(getItem(position).getLunchCal() + "");
            viewHolder.mealDinner.setText(getItem(position).getDinner());
            viewHolder.mealDinnerCal.setText(getItem(position).getDinnerCal() + "");
            viewHolder.dateString.setText(getItem(position).getDateString());
            viewHolder.result.setText("Status: "+getItem(position).getResult());
            return convertView;
        }
    }

    public static class ViewHolder {
        @Bind(R.id.runningValue)
        TextView runningValue;
        @Bind(R.id.walkingValue)
        TextView walkingValue;
        @Bind(R.id.idleValue)
        TextView idleValue;
        @Bind(R.id.mealBreakfast)
        TextView mealBreakfast;
        @Bind(R.id.mealBreakfastCal)
        TextView mealBreakfastCal;
        @Bind(R.id.mealLunch)
        TextView mealLunch;
        @Bind(R.id.mealLunchCal)
        TextView mealLunchCal;
        @Bind(R.id.mealDinner)
        TextView mealDinner;
        @Bind(R.id.mealDinnerCal)
        TextView mealDinnerCal;
        @Bind(R.id.dateString)
        TextView dateString;

        TextView result;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            result=(TextView)view.findViewById(R.id.result);
        }
    }

}
