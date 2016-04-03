package com.pawanbathe.hardwaresanitytester;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    private SensorManager mSensorManager;
    private View viewSensorFragment;
    private Sensor mSensor[],testSensor;
    ArrayList<String> sensorslist=null;
    ArrayList<String> sensorslistindex=null;
    ListView sList=null;
    String itemSensor=null;
    List<Sensor> deviceSensors;
    StableArrayAdapter adapter;
    DecimalFormat df;
    final float alpha = 0.8f;
    float gravity[]={9.81f,9.81f,9.81f};
    float linear_acceleration[]= new float[3];

    public SensorsFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewSensorFragment =inflater.inflate(R.layout.fragment_sensors, container, false);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        mSensor=new Sensor[deviceSensors.size()+2];
        sList = (ListView) viewSensorFragment.findViewById(R.id.sensors_list);

        //Float format
        df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.CEILING);
        sensorslist= new ArrayList<String>();
        sensorslistindex= new ArrayList<String>();
        //Event Listener and Initialization

    try {
        registerListeners();
        updateSensorsRawData();
    }catch(Exception e)
    {
        sensorslist.clear();
        sensorslist.add("Error in getting sensors details, Please close app and retry");
    }

        adapter=new <String> StableArrayAdapter(getActivity(),R.layout.listview,sensorslist);
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
            //sensorslist.clear();
        try {
            updateSensorsLiveData(event);
            adapter.notifyDataSetChanged();
        }catch(Exception e)
        {
            sensorslist.clear();
            sensorslist.add("Error in getting sensors details, Please close app and retry");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    void  updateSensorsLiveData(SensorEvent event)
    {
            int index=sensorslistindex.indexOf(event.sensor.getName());
            testSensor=event.sensor;
            switch (testSensor.getType())
            {
                case Sensor.TYPE_ACCELEROMETER:
                    itemSensor = String.valueOf(testSensor.getName()) + " : \n     X:  " + String.valueOf(df.format(event.values[0])) + " m/s^2 \n     Y:  " + String.valueOf(df.format(event.values[1])) + " m/s^2 \n     Z:  " + String.valueOf(df.format(event.values[2])) + " m/s^2";
                    sensorslist.set(index, "\n    "+itemSensor+"\n ");
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    itemSensor = String.valueOf(testSensor.getName()) + " : \n     "+event.values[0];
                    sensorslist.set(index, "\n    "+itemSensor+"\n ");
                    break;
                case Sensor.TYPE_GRAVITY:
                    itemSensor = String.valueOf(testSensor.getName()) + " : \n     Force of gravity along the X axis:  " + String.valueOf(df.format(event.values[0])) + " m/s^2 \n     Force of gravity along the Y axis:  " + String.valueOf(df.format(event.values[1])) + " m/s^2 \n     Force of gravity along the Z axis:  " + String.valueOf(df.format(event.values[2])) + " m/s^2";
                    sensorslist.set(index, "\n    "+itemSensor+"\n ");
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    itemSensor = String.valueOf(testSensor.getName()) + " : \n     X: "+ String.valueOf(df.format(event.values[0])) + " m/s^2 \n     Y:  " + String.valueOf(df.format(event.values[1])) + " m/s^2 \n     Z: " + String.valueOf(df.format(event.values[2])) + " m/s^2";
                    sensorslist.set(index, "\n    "+itemSensor+"\n ");
                    break;
                case Sensor.TYPE_LIGHT:
                    itemSensor = String.valueOf(testSensor.getName()) + " : \n     "+String.valueOf(event.values[0]+" lux");
                    sensorslist.set(index, "\n    "+itemSensor+"\n ");
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    itemSensor = String.valueOf(testSensor.getName()) + " : \n     X: "+ String.valueOf(df.format(event.values[0])) + " m/s^2 \n     Y: " + String.valueOf(df.format(event.values[1])) +"m/s^2 \n     Z: " + String.valueOf(df.format(event.values[2])) +" m/s^2";
                    sensorslist.set(index, "\n    "+itemSensor+"\n ");
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    itemSensor = String.valueOf(testSensor.getName()) + " : \n     "+String.valueOf(event.values[0])+" \u03BCT";
                    sensorslist.set(index, "\n    "+itemSensor+"\n ");
                    break;
                case Sensor.TYPE_PRESSURE:
                    itemSensor = String.valueOf(testSensor.getName()) + " : \n     "+String.valueOf(event.values[0]+" hPa");
                    sensorslist.set(index, "\n    "+itemSensor+"\n ");
                    break;
                case Sensor.TYPE_PROXIMITY:
                    itemSensor = String.valueOf(testSensor.getName()) + " : \n     "+String.valueOf(event.values[0]);
                    sensorslist.set(index, "\n    "+itemSensor+"\n ");
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    itemSensor = String.valueOf(testSensor.getName()) + " : \n     "+String.valueOf(event.values[0]);;
                    sensorslist.set(index, "\n    "+itemSensor+"\n ");
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    itemSensor = String.valueOf(testSensor.getName()) + " : \n     Rotation vector component along X axis:  " + String.valueOf(df.format(event.values[0])) + "\n     Rotation vector component along Y axis:  " + String.valueOf(df.format(event.values[1])) + " \n     Rotation vector component along Z axis:  " + String.valueOf(df.format(event.values[2])) + "";
                    sensorslist.set(index, "\n    "+itemSensor+"\n ");
                    break;
                case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                    itemSensor = String.valueOf(testSensor.getName()) + " : \n     Rotation around X axis:  " + String.valueOf(df.format(event.values[0])) + " rad/s \n     Rotation around Y axis : " + String.valueOf(df.format(event.values[1])) + " rad/s \n     Rotation around Z axis:  " + String.valueOf(df.format(event.values[2])) + " rad/s \n"+
                            "     Estimated drift around X axis:  " + String.valueOf(df.format(event.values[0])) + " rad/s \n     Estimated drift around Y axis : " + String.valueOf(df.format(event.values[1])) + " rad/s \n     Estimated drift around Z axis:  " + String.valueOf(df.format(event.values[2])) + " rad/s";
                    sensorslist.set(index, "\n    "+itemSensor+"\n ");
                    break;
                default:
                    itemSensor = String.valueOf(testSensor.getName()) +" :";
                    sensorslist.set(index, "\n    "+itemSensor+"\n ");
            }
    }


    void  updateSensorsRawData( )
    {

//        Log.d("RAWAN","Sensors available"+deviceSensors.size());
        for(int i=0; i<deviceSensors.size(); i++) {
            mSensor[i]=deviceSensors.get(i);

           switch (mSensor[i].getType())
            {
                case Sensor.TYPE_ACCELEROMETER:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n";
                    sensorslist.add("\n    "+itemSensor+"\n ");
                    sensorslistindex.add(mSensor[i].getName());
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n Ambient Temperature = ";
                    sensorslist.add("\n    "+itemSensor+"\n ");
                    sensorslistindex.add(mSensor[i].getName());
                    break;
                case Sensor.TYPE_GRAVITY:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n X=0.0 m/s2 Y=0.0 m/s2 Z=0.0 m/s2";
                    sensorslist.add("\n    "+itemSensor+"\n ");
                    sensorslistindex.add(mSensor[i].getName());
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n X=0.0 m/s2 Y=0.0 m/s2 Z=0.0 m/s2";
                    sensorslist.add("\n    "+itemSensor+"\n ");
                    sensorslistindex.add(mSensor[i].getName());
                    break;
                case Sensor.TYPE_LIGHT:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n 0.0 lux";
                    sensorslist.add("\n    "+itemSensor+"\n ");
                    sensorslistindex.add(mSensor[i].getName());
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n X=0.0 m/s2 Y=0.0 m/s2 Z=0.0 m/s2";
                    sensorslist.add("\n    "+itemSensor+"\n ");
                    sensorslistindex.add(mSensor[i].getName());
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n 0.0 micro Tesla"+" \u03BCT";
                    sensorslist.add("\n    "+itemSensor+"\n ");
                    sensorslistindex.add(mSensor[i].getName());
                    break;
                case Sensor.TYPE_PRESSURE:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n Pressure: 0 ";
                    sensorslist.add("\n    "+itemSensor+"\n ");
                    sensorslistindex.add(mSensor[i].getName());
                    break;
                case Sensor.TYPE_PROXIMITY:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n Near/Far 0.0 cm";
                    sensorslist.add("\n    "+itemSensor+"\n ");
                    sensorslistindex.add(mSensor[i].getName());
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n Relative Humidity";
                    sensorslist.add("\n    "+itemSensor+"\n ");
                    sensorslistindex.add(mSensor[i].getName());
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n Azimuth=0.0 Pitch=0.0 Roll=0.0 ";
                    sensorslist.add("\n    "+itemSensor+"\n ");
                    sensorslistindex.add(mSensor[i].getName());
                    break;
                case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                    itemSensor=String.valueOf(mSensor[i].getName()) + " : \n";
                    sensorslist.add("\n    "+itemSensor+"\n ");
                    sensorslistindex.add(mSensor[i].getName());
                    break;
                default:
                    itemSensor = String.valueOf(mSensor[i].getName()) +" : \n";
                    sensorslist.add("\n    "+itemSensor+"\n ");
                    sensorslistindex.add(mSensor[i].getName());
            }
        }
        sensorslist.add("\n");
        sensorslist.add("\n");

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
