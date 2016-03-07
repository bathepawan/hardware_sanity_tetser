package com.pawanbathe.hardwaresanitytester;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by pbathe on 8/3/16.
 */
public class BatteryFragment extends Fragment {

    private TextView batteryInfo;
    private View batteryFragmentView;
    public BatteryFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        batteryFragmentView = inflater.inflate(R.layout.fragment_battery, container, false);
        batteryInfo =(TextView) batteryFragmentView.findViewById(R.id.textViewBatteryInfo);
        getActivity().registerReceiver(this.BatteryInfoReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return batteryFragmentView;
    }

    private BroadcastReceiver BatteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int  health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
            int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            int  plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            boolean  present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
            int  scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
            int  status= intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            String  technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
            int  temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
            int  voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);



            batteryInfo.setText(
                    "Health: "+health+"\n"+
                            "Level: "+level+"\n"+
                            "Plugged: "+plugged+"\n"+
                            "Present: "+present+"\n"+
                            "Scale: "+scale+"\n"+
                            "Status: "+status+"\n"+
                            "Technology: "+technology+"\n"+
                            "Capacity: "+
                            "Temperature: "+temperature+"\n"+
                            "Voltage: "+voltage+"\n");

        }
    };

}
