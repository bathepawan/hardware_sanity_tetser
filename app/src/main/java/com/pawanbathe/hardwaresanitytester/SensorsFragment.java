package com.pawanbathe.hardwaresanitytester;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by pbathe on 8/3/16.
 */
public class SensorsFragment extends Fragment implements SensorEventListener{

    public SensorsFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private SensorManager mSensorManager;
    private View viewSensorFragment;
    private Sensor mSensor[]=new Sensor[20];
    private ArrayList<String> sensorslist=null;
    ListView sList=null;
    String itemSensor=null;
    List<Sensor> deviceSensors;
    StableArrayAdapter adapter;
    DecimalFormat df;
    final float alpha = 0.8f;
    float gravity[]={9.81f,9.81f,9.81f};
    float linear_acceleration[]= new float[3];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewSensorFragment =inflater.inflate(R.layout.fragment_sensors, container, false);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        sensorslist= new ArrayList<String>();
        sList = (ListView) viewSensorFragment.findViewById(R.id.sensors_list);

        //Float format
        df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.CEILING);

        //Event Listener and Initialization
        registerListeners();
        updateSensorsRawData();

        adapter=new <String> StableArrayAdapter(getActivity(),R.layout.sensors_listview,sensorslist);
        sList.setAdapter(adapter);

        return viewSensorFragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        sensorslist.clear();
        for(int i=0; i<deviceSensors.size(); i++) {
            mSensor[i]=deviceSensors.get(i);
            switch (mSensor[i].getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    itemSensor = String.valueOf(mSensor[i].getName()) + " : \n X:  " + String.valueOf(df.format(event.values[0])) + " m/s^2 \n Y:  " + String.valueOf(df.format(event.values[1])) + " m/s^2 \n Z:  " + String.valueOf(df.format(event.values[2])) + " m/s^2";
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    itemSensor = String.valueOf(mSensor[i].getName()) + " : \n "+String.valueOf(event);
                    break;
                case Sensor.TYPE_GRAVITY:
                    itemSensor = String.valueOf(mSensor[i].getName()) + " : \n X:  " + String.valueOf(df.format(event.values[0])) + " m/s^2 \n Y:  " + String.valueOf(df.format(event.values[1])) + " m/s^2 \n Z:  " + String.valueOf(df.format(event.values[2])) + " m/s^2";
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    itemSensor = String.valueOf(mSensor[i].getName()) + " : \n "+String.valueOf(event);
                    break;
                case Sensor.TYPE_LIGHT:
                    itemSensor = String.valueOf(mSensor[i].getName()) + " : \n "+String.valueOf(event);
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    itemSensor = String.valueOf(mSensor[i].getName()) + " : \n "+String.valueOf(event);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    itemSensor = String.valueOf(mSensor[i].getName()) + " : \n "+String.valueOf(event);
                    break;
                case Sensor.TYPE_PRESSURE:
                    itemSensor = String.valueOf(mSensor[i].getName()) + " : \n "+ String.valueOf(df.format(event.values[0]));
                    break;
                case Sensor.TYPE_PROXIMITY:
                    itemSensor = String.valueOf(mSensor[i].getName()) + " : \n "+String.valueOf(event);
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    itemSensor = String.valueOf(mSensor[i].getName()) + " : \n "+String.valueOf(df.format(event.values[0]));
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    itemSensor = String.valueOf(mSensor[i].getName()) + " : \n X:  " + String.valueOf(df.format(event.values[0])) + " m/s^2 \n Y:  " + String.valueOf(df.format(event.values[1])) + " m/s^2 \n Z:  " + String.valueOf(df.format(event.values[2])) + " m/s^2";
                    break;
                default:
                    itemSensor = String.valueOf(mSensor[i].getName()) +" : \n";
            }
            Log.d("FUCK", "Called it");
            sensorslist.add(itemSensor);
            itemSensor=null;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    void  updateSensorsRawData( )
    {

        for(int i=0; i<deviceSensors.size(); i++) {
            mSensor[i]=deviceSensors.get(i);
            switch (mSensor[i].getType())
            {
                case Sensor.TYPE_ACCELEROMETER:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n";
                    sensorslist.add(itemSensor);
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n Ambient Temperature = ";
                    sensorslist.add(itemSensor);
                    break;
                case Sensor.TYPE_GRAVITY:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n X=0.0 m/s2 Y=0.0 m/s2 Z=0.0 m/s2";
                    sensorslist.add(itemSensor);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n X=0.0 m/s2 Y=0.0 m/s2 Z=0.0 m/s2";
                    sensorslist.add(itemSensor);
                    break;
                case Sensor.TYPE_LIGHT:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n 0.0 Lux";
                    sensorslist.add(itemSensor);
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n X=0.0 m/s2 Y=0.0 m/s2 Z=0.0 m/s2";
                    sensorslist.add(itemSensor);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n 0.0 micro Tesla";
                    sensorslist.add(itemSensor);
                    break;
                case Sensor.TYPE_PRESSURE:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n Pressure: 0 ";
                    sensorslist.add(itemSensor);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n Near/Far 0.0 cm";
                    sensorslist.add(itemSensor);
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n Relative Humidity";
                    sensorslist.add(itemSensor);
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n Azimuth=0.0 Pitch=0.0 Roll=0.0 ";
                    sensorslist.add(itemSensor);
                    break;
                default:
                    itemSensor = String.valueOf(mSensor[i]) +" : \n";
            }

        }

    }



    void registerListeners()
    {
        for(int i=0; i<deviceSensors.size(); i++) {
            mSensor[i] = deviceSensors.get(i);
            mSensorManager.registerListener(this, mSensor[i], SensorManager.SENSOR_DELAY_GAME);
        }
    }


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
