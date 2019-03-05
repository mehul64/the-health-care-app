package com.thc.app.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.thc.app.BaseFragment;
import com.thc.app.R;
import com.thc.app.activities.DashboardActivity;
import com.thc.app.receiver.OnBootReceiver;
import com.thc.app.utils.DataPreference;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aabid-personal on 9/21/16.
 */
public class SettingsFragment extends BaseFragment {

    @Bind(R.id.checkBox)
    CheckBox checkBox;

    View view;
    TextView bf,lunch,dinner;
    private DataPreference preference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        bf=(TextView) view.findViewById(R.id.bf_time);
        lunch=(TextView) view.findViewById(R.id.lunch_time);
        dinner=(TextView) view.findViewById(R.id.dinner_time);
        preference=new DataPreference(getActivity());
        bf.setText(getProperTime(preference.getBreakfastTime()));
        lunch.setText(getProperTime(preference.getLunchTime()));
        dinner.setText(getProperTime(preference.getDinnerTime()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preference = new DataPreference(getActivity());
        checkBox.setChecked(preference.showInKiloMeter());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preference.setShowInKiloMeter(checkBox.isChecked());
            }
        });

        view.findViewById(R.id.bf_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                    b.setTitle("Set breakfast time");
                    final String[] types=new String[24];
                    for(int i=1;i<=24;i++)
                        types[i-1]=""+i;
                    b.setItems(types, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new DataPreference(getActivity()).setBreakfastTime(types[which]);
                            bf.setText(getProperTime(types[which]));
                            dialog.dismiss();
                        }
                    });
                    b.show();
            }
        });

        view.findViewById(R.id.lunch_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setTitle("Set lunch time");
                final String[] types=new String[24];
                for(int i=1;i<=24;i++)
                    types[i-1]=""+i;
                b.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DataPreference(getActivity()).setLunchTime(types[which]);
                        lunch.setText(getProperTime(types[which]));
                        dialog.dismiss();
                    }
                });
                b.show();
            }
        });

        view.findViewById(R.id.dinner_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setTitle("Set dinner time");
                final String[] types=new String[24];
                for(int i=1;i<=24;i++)
                    types[i-1]=""+i;
                b.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DataPreference(getActivity()).setDinnerTime(types[which]);
                        dinner.setText(getProperTime(types[which]));
                        dialog.dismiss();
                    }
                });
                b.show();
            }
        });
    }

    @Override
    public String getFragmentTitle() {
        return "Settings";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        OnBootReceiver.setOrResetAllReceivers(getActivity());
    }

    public String getProperTime(String time)
    {
        if(Integer.parseInt(time)>12)
            return (Integer.parseInt(time)-12)+" P.M";
        else
            return time+" A.M.";
    }

}
