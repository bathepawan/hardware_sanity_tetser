package com.pawanbathe.hardwaresanitytester;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by pbathe on 8/3/16.
 */

public class DeviceFragment extends Fragment {

    private View deviceFragmentView;
    private TextView deviceTextView;
    public DeviceFragment()
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
        String model,brand,board,sSize,sResolution,sDensity,sMemory,sMemoryNow,TotalRam,RamNow;

        deviceFragmentView = inflater.inflate(R.layout.fragment_device, container, false);
        deviceTextView =(TextView) deviceFragmentView.findViewById(R.id.textViewDevice);

        model=InfoManager.getProp("ro.product.model").replace("\n", " ");
        brand=InfoManager.getProp("ro.product.brand").replace("\n", " ");
        board= InfoManager.getProp("ro.product.board").replace("\n", " ");
        String[] screenData = getScreenDimension();
        sSize= screenData[2];
        sDensity= screenData[3];
        sResolution=screenData[4]+"x"+screenData[5];
        String[] memData= getRAMInfo();
        TotalRam =memData[0];
        RamNow= memData[1];
        sMemory=TotalRam;
        sMemoryNow= RamNow;
        deviceTextView.setText(Html.fromHtml(
                "<B>Model:</B> "+model+"<br>"+
                        "<B>Brand:</B> \t"+brand+"<br>"+
                        "<B>Board: </B>\t"+board+"<br>"+
                        "<B>Screen Size:</B> \t "+sSize+" <br>"+
                        "<B>Screen Resolution:</B> \t "+sResolution+"<br>"+
                        "<B>Screen Density :</B> \t"+sDensity+"<br>"+
                        "<B>Total RAM:</B> \t"+TotalRam+"<br>"+
                        "<B>Available RAM:</B> \t"+RamNow+"<br>"+
                        "<B>Internal Storage: </B>\t"+sMemory+"<br>"+
                        "<B>Available Storage:</B>\t"+sMemoryNow+"<br>"));



        return deviceFragmentView;
    }

    private String[] getScreenDimension(){
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double)width / (double)dens;
        double hi = (double)height / (double)dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x+y);

        String[] screenInformation = new String[6];
        screenInformation[0] = String.valueOf(width) + " px";
        screenInformation[1] = String.valueOf(height) + " px" ;
        screenInformation[2] = String.format("%.2f", screenInches) + " inches" ;
        screenInformation[3] = String.valueOf(dens);
        screenInformation[4] = String.valueOf(width);
        screenInformation[5] = String.valueOf(height);
        return screenInformation;
    }

    public String[] getRAMInfo() {

        RandomAccessFile reader = null;
        String loadtotal,loadnow = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0,ramNow;
        String lastValue = "",lastNowValue="";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            loadtotal = reader.readLine();
            loadnow = reader.readLine();
            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m1 = p.matcher(loadtotal);
            Matcher m2 = p.matcher(loadnow);
            String valuetotal = "",valuenow="";
            while (m1.find()) {
                valuetotal = m1.group(1);
                // System.out.println("Ram : " + value);
            }
            while (m2.find()) {
                valuenow = m2.group(1);
                // System.out.println("Ram : " + value);
            }

            reader.close();

            totRam = Double.parseDouble(valuetotal);
            ramNow = Double.parseDouble(valuenow);
            // totRam = totRam / 1024;

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            double mbnow = ramNow / 1024.0;
            double gbnow = ramNow / 1048576.0;
            double tbnow = ramNow / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
                lastNowValue = twoDecimalForm.format(tbnow).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
                lastNowValue = twoDecimalForm.format(gbnow).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
                lastNowValue = twoDecimalForm.format(mbnow).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
                lastNowValue = twoDecimalForm.format(ramNow).concat(" KB");
            }



        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        String[] ramInfo = new String[2];
        ramInfo[0]= String.valueOf(lastValue);
        ramInfo[1]= String.valueOf(lastNowValue);
        return ramInfo;
    }
}
