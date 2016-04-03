package com.pawanbathe.hardwaresanitytester;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by pbathe on 8/3/16.
 */
public class SOCFragment extends Fragment {

    //Variable Declerations

    File[] cpuFiles;
    private TextView cpuInfoStatic,cpuInfoDynamic;
    private ArrayList<String> arrSOCInfoList=null;
    ListView siList=null;
    List<String> socInfo=null;
    StableArrayAdapter adapter;


    private View socFragmentView;

    int availableProcessors;
    int maxFreq,minFreq;
    int activeCores;
    private int mInterval = 2000; // 1 seconds
    private Handler mHandler;
    private String[] freqInfo=new String[]{"off","off","off","off","off","off","off","off","off","off"};
    private String freqInfoText;
    private String chipName=null;
    private String socVendor=null;
    private String gpuInfo=null;
    final String PREFIX_CPU_PATH="/sys/devices/system/cpu/cpu";
    final String SUFIX_MIN="/cpufreq/cpuinfo_min_freq";
    final String SUFIX_MAX="/cpufreq/cpuinfo_max_freq";
    String PATH_MAX1=null;
    String PATH_MAX2=null;
    String PATH_MIN=null;

    public SOCFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Views Mapping
        socFragmentView = inflater.inflate(R.layout.fragment_soc, container, false);
        freqInfo=readCpuFreqNow();
        PATH_MIN=PREFIX_CPU_PATH+"0"+SUFIX_MIN;
        PATH_MAX1=PREFIX_CPU_PATH+"0"+SUFIX_MAX;
        PATH_MAX2=PREFIX_CPU_PATH+cpuFiles.length+SUFIX_MAX;
        minFreq=Integer.parseInt(cmdCat(PATH_MIN).trim());
        maxFreq=Integer.parseInt(cmdCat(PATH_MAX1).trim());
        minFreq=minFreq/1000;
        maxFreq=maxFreq/1000;

        arrSOCInfoList=new ArrayList<String>();
        siList=(ListView) socFragmentView.findViewById(R.id.socinfo_list);
        adapter=new <String> StableArrayAdapter(getActivity(),R.layout.listview,arrSOCInfoList);
        siList.setAdapter(adapter);
        arrSOCInfoList.clear();

        chipName=InfoManager.getProp("ro.chipname").replace("\n"," ").toUpperCase();
        socVendor=InfoManager.getProp("ro.hardware").replace("\n"," ").toUpperCase();


        arrSOCInfoList.add("\n    Model : " + socVendor+" "+ chipName+"\n");

        arrSOCInfoList.add("\n    Total CPU Cores: " + availableProcessors + "\n");
        arrSOCInfoList.add("\n    Clock Speed: " + minFreq + "-" + maxFreq + " MHz"+"\n");

        Runtime runtime = Runtime.getRuntime();
        availableProcessors = runtime.availableProcessors();
        for(int i=0; i<cpuFiles.length; i++){
            arrSOCInfoList.add(" \t     CPU "+i+": "+ freqInfo[i].replace("\n"," "+"\n"));
        }

        arrSOCInfoList.add("\n    Supported " + getCPUFeatures().replace("\n", " ").trim().replace(" ", "\n      ")+"\n");
        arrSOCInfoList.add("\n    CPU Governor : "+cmdCat("sys/devices/system/cpu/cpu0/cpufreq/scaling_governor").toUpperCase());
        SharedPreferences prefs = getActivity().getSharedPreferences("GPUinfo", Context.MODE_PRIVATE);
        String vendor = prefs.getString("VENDOR", null);
        String renderer = prefs.getString("RENDERER", null);
//        String version = prefs.getString("VERSION", null);
//        String extensions = prefs.getString("EXTENSIONS", null);

        arrSOCInfoList.add("\n    GPU Vendor: "+vendor+"\n");
        arrSOCInfoList.add("\n    GPU Renderer: "+renderer+"\n");
        //arrSOCInfoList.add("Open GL ES Version: "+configurationInfo.reqGlEsVersion);
        arrSOCInfoList.add("\n");

        adapter.notifyDataSetChanged();
        mHandler = new Handler();
        mHandler.post(periodicCPUChecker);
        return socFragmentView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(periodicCPUChecker);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.post(periodicCPUChecker);
    }

    //Run Periodic CPU Info Checker

    private Runnable periodicCPUChecker = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
            arrSOCInfoList.clear();
            arrSOCInfoList.add("\n    Model : " + socVendor + " " + chipName+"\n");
            arrSOCInfoList.add("\n    Total CPU Cores: " + availableProcessors + "\n" );
            arrSOCInfoList.add("\n    Clock Speed: " + minFreq + "-" + maxFreq + " MHz"+"\n");

            freqInfo=readCpuFreqNow();
            freqInfoText="";
            for(int i=0; i<cpuFiles.length; i++){
                try {
                    arrSOCInfoList.add( "\n\t \t     CPU " + i + ": " + String.valueOf(Float.parseFloat(freqInfo[i].replace("\n", " ").trim()) / 1000.0) + "\n");
                }catch (NumberFormatException nfe)
                {
                    arrSOCInfoList.add("\n\t \t      CPU " + i + ": " + " off " + "\n");
                }
            }

            arrSOCInfoList.add("\n    Supported " + getCPUFeatures().replace("\n", " ").trim().replace(" ","\n      ")+"\n");
            arrSOCInfoList.add("\n    CPU Governor : "+cmdCat("sys/devices/system/cpu/cpu0/cpufreq/scaling_governor").toUpperCase());
            SharedPreferences prefs = getActivity().getSharedPreferences("GPUinfo", Context.MODE_PRIVATE);
            String vendor = prefs.getString("VENDOR", null);
            String renderer = prefs.getString("RENDERER", null);
//            String version = prefs.getString("VERSION", null);
//            String extensions = prefs.getString("EXTENSIONS", null);
            arrSOCInfoList.add("\n    GPU Vendor: " + vendor + "\n");
            arrSOCInfoList.add("\n    GPU Renderer: " + renderer + "\n");
//            arrSOCInfoList.add("Open GL ES Version: "+configurationInfo.reqGlEsVersion);
            arrSOCInfoList.add("\n");

            adapter.notifyDataSetChanged();
            //cpuInfoDynamic.setText(freqInfoText);
            mHandler.postDelayed(periodicCPUChecker, 500);
        }
    };
    // End Run Periodic CPU Info Checker
// Start CPU Information

    private String[] readCpuFreqNow(){
        cpuFiles = getCPUs();
        activeCores=cpuFiles.length;
        String strFileList[] =new String[]{"off","off","off","off","off","off","off","off","off","off"};
        for(int i=0; i<cpuFiles.length; i++){
            String path_scaling_cur_freq =
                    cpuFiles[i].getAbsolutePath()+"/cpufreq/scaling_cur_freq";
            String scaling_cur_freq = cmdCat(path_scaling_cur_freq);
/*
        strFileList +=
               "CPU "+ i + ": "
                        + path_scaling_cur_freq + "\n"
                        + scaling_cur_freq
                        + "\n";
 */
            strFileList[i]=scaling_cur_freq;
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

    /*
     * Get file list of the pattern
     * /sys/devices/system/cpu/cpu[0..9]
     */
    private File[] getCPUs(){

        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                if(Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        File dir = new File("/sys/devices/system/cpu/");
        File[] files = dir.listFiles(new CpuFilter());
        return files;
    }

    private String getCPUFeatures()
    {
        String cpuinfo="/proc/cpuinfo";
        String[] command = {"cat", cpuinfo};
        String cmdReturn = null;

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = null;
            while ((line = inputStream.readLine()) != null) {
                if(line.contains("Features"))
                {
                    cmdReturn=line;
                }
            }
            return cmdReturn;

        } catch (IOException e) {
            e.printStackTrace();
            return "0";
        }

    }

//End CPU Information


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
