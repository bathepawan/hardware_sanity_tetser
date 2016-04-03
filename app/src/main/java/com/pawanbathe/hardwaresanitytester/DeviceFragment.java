package com.pawanbathe.hardwaresanitytester;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by pbathe on 8/3/16.
 */

public class DeviceFragment extends Fragment {

    private View deviceFragmentView;
    private TextView deviceTextView;
    private ArrayList<String> arrDeviceInfoList=null;
    private String chipName=null;
    private String socVendor=null;

    ListView diList=null;
    List<String> deviceInfo=null;
    StableArrayAdapter adapter;

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
        String model,brand,board,sSize,sResolution,sDensity,sMemory,sMemoryNow,TotalRam,RamNow,esMemory,esMemoryNow;

        deviceFragmentView = inflater.inflate(R.layout.fragment_device, container, false);
        arrDeviceInfoList=new ArrayList<String>();
        diList=(ListView) deviceFragmentView.findViewById(R.id.deviceinfo_list);
        adapter=new <String> StableArrayAdapter(getActivity(),R.layout.listview,arrDeviceInfoList);
        diList.setAdapter(adapter);
        arrDeviceInfoList.clear();
        chipName=InfoManager.getProp("ro.chipname").replace("\n"," ").toUpperCase();
        socVendor=InfoManager.getProp("ro.hardware").replace("\n", " ").toUpperCase();

        arrDeviceInfoList.add("\n        SOC Details :" + socVendor+chipName+"\n");

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

        String[] storageData= getStorageInfo();

        sMemory=storageData[1];
        sMemoryNow=storageData[0];
//        esMemory=storageData[3];
//        esMemoryNow=storageData[2];

        arrDeviceInfoList.add("\n        Model : "+model+"\n ");
        arrDeviceInfoList.add("\n        Hardware : "+brand.toUpperCase()+board+"\n ");
//        arrDeviceInfoList.add("\n        Board : "+board+"\n");
        arrDeviceInfoList.add("\n        Screen Size : "+sSize+"\n");
        arrDeviceInfoList.add("\n        Screen Resolution : "+sResolution+" Pixels\n");
        arrDeviceInfoList.add("\n        Screen Density : "+sDensity+" dpi\n");
        arrDeviceInfoList.add("\n        Total RAM : "+TotalRam+"\n");
        arrDeviceInfoList.add("\n        Available RAM : "+RamNow+"\n");
        arrDeviceInfoList.add("\n        Internal Storage : "+sMemory+"\n");
        arrDeviceInfoList.add("\n        Available Storage : "+sMemoryNow+"\n");
//        arrDeviceInfoList.add("\n        External Storage :"+esMemory+"\n");
//        arrDeviceInfoList.add("\n        External Available Storage : "+esMemoryNow+"\n");
        arrDeviceInfoList.add("\n");

        adapter.notifyDataSetChanged();
        return deviceFragmentView;
    }

    private String[] getScreenDimension(){
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double)width / (double) dm.xdpi;
        double hi = (double)height / (double) dm.ydpi;
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


    public String[] getStorageInfo()
    {
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        String storageInfo[]=new String[4];
        StatFs isstat = new StatFs(Environment.getDataDirectory().getPath());

        float bytesAvailable = (float)isstat.getFreeBlocks() * (float)isstat.getBlockSize();
        float megAvailable = bytesAvailable / 1048576;
        float totalbytesAvailable = (float)isstat.getBlockSize() *(float)isstat.getBlockCount();
        float totalmegAvailable = totalbytesAvailable / 1048576;

        float gbAvailable=megAvailable/1024;
        float gbTotal=totalmegAvailable/1024;
        storageInfo[0]=String.valueOf(twoDecimalForm.format(gbAvailable).concat(" GB"));
        storageInfo[1]=String.valueOf(twoDecimalForm.format(gbTotal).concat(" GB"));

/*        StatFs esstat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        float esbytesAvailable = (float)esstat.getFreeBlocks() * (float)esstat.getBlockSize();
        float esmegAvailable = esbytesAvailable / 1048576;

        float estotalbytesAvailable = (float)esstat.getBlockSize() *(float)esstat.getBlockCount();
        float estotalmegAvailable = estotalbytesAvailable / 1048576;

        storageInfo[2]=String.valueOf(esmegAvailable/1024);
        storageInfo[3]=String.valueOf(estotalmegAvailable/1024);
*/
        return storageInfo;
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
