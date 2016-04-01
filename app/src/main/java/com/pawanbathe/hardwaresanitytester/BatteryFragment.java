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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pbathe on 8/3/16.
 */
public class BatteryFragment extends Fragment {

    private TextView batteryInfo;
    private View batteryFragmentView;
    private ArrayList<String> arrBattInfoList=null;
    ListView biList=null;
    List<String> battInfo=null;
    StableArrayAdapter adapter;
    String battHealth[]={"UNKNOWN","UNKNOWN","GOOD","OVERHEAT","DEAD","VOLTAGE","UNSPECIFIED","FAILURE,BATTERY_HEALTH_COLD"};

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
        getActivity().registerReceiver(this.BatteryInfoReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        arrBattInfoList=new ArrayList<String>();
        biList=(ListView) batteryFragmentView.findViewById(R.id.batteryinfo_list);
        adapter=new <String> StableArrayAdapter(getActivity(),R.layout.listview,arrBattInfoList);
        biList.setAdapter(adapter);
        return batteryFragmentView;
    }

    @Override
    public void onPause() {
        super.onPause();
 //       getActivity().
    }

    private BroadcastReceiver BatteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String chargingStatus[]={"Not Charging","Charging,AC Power Supply","Charging, PC USB / Power Bank"};
            arrBattInfoList.clear();
            int  health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
            int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            int  plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            boolean  present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
            int  scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
            int  status= intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            String  technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
            int  temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
            int  voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);

            arrBattInfoList.add("Present: "+present+"");
            arrBattInfoList.add("Health: "+battHealth[health]+"");
            arrBattInfoList.add("Scale: "+scale+"");
            arrBattInfoList.add("Level: "+level+"");
            arrBattInfoList.add("Charging Status: "+chargingStatus[plugged]+"");
            arrBattInfoList.add("Status: "+status+"");
            arrBattInfoList.add("Technology: "+technology+"");
            arrBattInfoList.add("Temperature: "+temperature+"");
            arrBattInfoList.add("Voltage: "+voltage+" mV");
            adapter.notifyDataSetChanged();

        }
    };


    // Adapter
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            //  String item = getItem(position);
            //    return mIdMap.get(item);
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
