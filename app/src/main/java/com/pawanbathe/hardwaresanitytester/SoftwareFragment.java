package com.pawanbathe.hardwaresanitytester;

import android.content.Context;
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
public class SoftwareFragment extends Fragment {


    View viewSoftwareFragment;
    TextView textViewSW;
    String swInfo[] = new String[10];
    private ArrayList<String> arrSWInfoList=null;
    ListView sList=null;
    List<String> sInfo=null;
    StableArrayAdapter adapter;

    public SoftwareFragment()
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
        viewSoftwareFragment = inflater.inflate(R.layout.fragment_software, container, false);
        // Android Version , API Level , Boorloader , Build Id , , CPU ABI List
        swInfo=getSWInfo();
        arrSWInfoList=new ArrayList<String>();
        sList=(ListView) viewSoftwareFragment.findViewById(R.id.swinfo_list);
        adapter=new <String> StableArrayAdapter(getActivity(),R.layout.listview,arrSWInfoList);
        sList.setAdapter(adapter);

        arrSWInfoList.clear();
        arrSWInfoList.add("\n    Android Version : " + swInfo[0] + "\n");
        arrSWInfoList.add("\n    API Level : " + swInfo[1]+"\n");
        arrSWInfoList.add("\n    Bootloader : " + swInfo[2]+"\n");
        arrSWInfoList.add("\n    Build ID  : " + swInfo[3]+"\n");
        arrSWInfoList.add("\n    Build Description :\n      " + swInfo[4].replace(" ","\n      ")+"\n");
        arrSWInfoList.add("\n    Build Date : " + swInfo[5]+"\n");
        arrSWInfoList.add("\n    CPU ABI : " + swInfo[6]+"\n");
        arrSWInfoList.add("\n    Kernel Version: " + swInfo[7]+"\n");
        arrSWInfoList.add("\n");

        adapter.notifyDataSetChanged();
        return viewSoftwareFragment;
    }

    public String [] getSWInfo()
    {
        // Android Version , API Level , Boorloader , Build Id , Build Desc , CPU ABI List
        String[] swInfo = new String[10];
        swInfo[0]= InfoManager.getProp("ro.build.version.release").replace("\n", " ").trim();
        swInfo[1]= InfoManager.getProp("ro.build.version.sdk").replace("\n", " ").trim();
        swInfo[2]= InfoManager.getProp("ro.bootloader").replace("\n", " ").trim();
        swInfo[3]= InfoManager.getProp("ro.build.display.id").replace("\n", " ").trim();
        swInfo[4]= InfoManager.getProp("ro.build.description").replace("\n", " ").trim();
        swInfo[5]= InfoManager.getProp("ro.build.date").replace("\n", " ").trim();
        swInfo[6]= InfoManager.getProp("ro.product.cpu.abilist").replace("\n", " ").trim();
        swInfo[7]= System.getProperty("os.version").replace("\n", " ").trim();
        swInfo[8]= "NA";
        return swInfo;
    }

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
