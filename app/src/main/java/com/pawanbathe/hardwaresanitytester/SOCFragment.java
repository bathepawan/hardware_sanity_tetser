package com.pawanbathe.hardwaresanitytester;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

/**
 * Created by pbathe on 8/3/16.
 */
public class SOCFragment extends Fragment {

    //Variable Declerations

    File[] cpuFiles;
    private TextView cpuInfoStatic,cpuInfoDynamic,socInfo;

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

        cpuInfoStatic= (TextView) socFragmentView.findViewById(R.id.textViewCpuInfoStatic);
        cpuInfoDynamic= (TextView) socFragmentView.findViewById(R.id.textViewCpuInfoDynamic);
        socInfo=(TextView) socFragmentView.findViewById(R.id.textViewSOC);

        Runtime runtime = Runtime.getRuntime();
        availableProcessors = runtime.availableProcessors();

        freqInfo=readCpuFreqNow();
        for(int i=0; i<cpuFiles.length; i++){
            freqInfoText += " \t \t CPU "+i+": "+ freqInfo[i].replace("\n"," "+"\n");
        }
//        freqInfoText+="CPU Load:"+ readUsage()*100;
//        cpuInfoDynamic.setText(freqInfoText);

        PATH_MIN=PREFIX_CPU_PATH+"0"+SUFIX_MIN;
        PATH_MAX1=PREFIX_CPU_PATH+"0"+SUFIX_MAX;
        PATH_MAX2=PREFIX_CPU_PATH+cpuFiles.length+SUFIX_MAX;
        minFreq=Integer.parseInt(cmdCat(PATH_MIN).trim());
        maxFreq=Integer.parseInt(cmdCat(PATH_MAX1).trim());
        minFreq=minFreq/1000;
        maxFreq=maxFreq/1000;
        Log.d("HST", PATH_MAX1);
        Log.d("HST", PATH_MIN);

        cpuInfoStatic.setText(Html.fromHtml(
                "<B>Supported </B>" + getCPUFeatures().replace("\n", " ").trim() + "<br>" +
                        "<B> Cores: </B>" + availableProcessors + "<br>" +
                        "<B>Clock Speed:</B> " + minFreq + "-" + maxFreq + " MHz"));

        //SOC Info
        chipName=InfoManager.getProp("ro.chipname").replace("\n"," ").toUpperCase();
        socVendor=InfoManager.getProp("ro.hardware").replace("\n"," ").toUpperCase();

        socInfo.setText(Html.fromHtml("<B>Model :</B>" + socVendor+" "+ chipName ));
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
            freqInfo=readCpuFreqNow();
            freqInfoText="";
            for(int i=0; i<cpuFiles.length; i++){
                try {
                    freqInfoText += "\t \t CPU " + i + ": " + String.valueOf(Float.parseFloat(freqInfo[i].replace("\n", " ").trim()) / 1000.0) + "\n";
                }catch (NumberFormatException nfe)
                {
                    freqInfoText += "\t \t CPU " + i + ": " + " " + "\n";
                }
            }
            cpuInfoDynamic.setText(freqInfoText);
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

}
