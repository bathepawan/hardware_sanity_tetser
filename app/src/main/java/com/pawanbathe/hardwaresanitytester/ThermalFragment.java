package com.pawanbathe.hardwaresanitytester;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by pbathe on 2/4/16.
 */
public class ThermalFragment extends Fragment {
    View viewThermalFragment=null;
    ArrayList<String> arrThermalInfoList=null;
    ListView thermalList=null;
    StableArrayAdapter adapter;

    File[] thermalFiles;
    String[] tz_types;
    String[] tz_temps;
    public ThermalFragment()
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
        viewThermalFragment = inflater.inflate(R.layout.fragment_thermal, container, false);
        arrThermalInfoList=new ArrayList<String>();
        thermalList=(ListView) viewThermalFragment.findViewById(R.id.thermalinfo_list);
        adapter=new <String> StableArrayAdapter(getActivity(),R.layout.listview,arrThermalInfoList);
        thermalList.setAdapter(adapter);
        tz_types=readThermalZoneTypes();
        tz_temps=readThermalZoneValues();
        arrThermalInfoList.clear();
        arrThermalInfoList.add("Available Thermal Zones: ");
        for (int i=0;i<thermalFiles.length;i++)
        arrThermalInfoList.add("    "+tz_types[i].trim().replace("\n"," ")+":        "+tz_temps[i].trim().replace("\n"," ")+"  C");

        arrThermalInfoList.add("\n");

        adapter.notifyDataSetChanged();
        return viewThermalFragment;
    }


    /*
 * Get file list of the pattern
 * /sys/class/thermal/thermal_zone[0..9]
 */
    private File[] getThermalFiles(){

        class ThermalZoneFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                if(Pattern.matches("thermal_zone[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        File dir = new File("/sys/class/thermal/");
        File[] files = dir.listFiles(new ThermalZoneFilter());
        return files;
    }

    private String[] readThermalZoneTypes(){
        thermalFiles = getThermalFiles();
        String strFileList[] =new String[thermalFiles.length];
        for(int i=0; i<thermalFiles.length; i++){
            String path_thermal_zone_type =
                    thermalFiles[i].getAbsolutePath()+"/type";
            String thermal_zone_type = cmdCat(path_thermal_zone_type);
            strFileList[i]=thermal_zone_type;
        }
        return strFileList;
    }

    private String[] readThermalZoneValues(){
        thermalFiles = getThermalFiles();
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");

        String strFileList[] =new String[thermalFiles.length];
        for(int i=0; i<thermalFiles.length; i++){
            String path_thermal_zone_type =
                    thermalFiles[i].getAbsolutePath()+"/temp";
            String thermal_zone_temp = cmdCat(path_thermal_zone_type);
            double temp=Double.parseDouble(thermal_zone_temp);

            while(temp>150.00)
            {
                temp=temp/10.00;
            }
            strFileList[i]=String.valueOf(twoDecimalForm.format(temp));
        }
        return strFileList;
    }

    private String cmdCat(String f){

        String[] command = {"cat", f};
        StringBuilder cmdReturn = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            int c;

            while ((c = inputStream.read()) != -1) {
                cmdReturn.append((char) c);
            }

            return cmdReturn.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "0";
        }

    }
    //Adapter
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
