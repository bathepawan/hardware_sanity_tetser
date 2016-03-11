package com.pawanbathe.hardwaresanitytester;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by pbathe on 8/3/16.
 */
public class SoftwareFragment extends Fragment {


    View viewSoftwareFragment;
    TextView textViewSW;
    String swInfo[] = new String[10];
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
        textViewSW= (TextView) viewSoftwareFragment.findViewById(R.id.textViewSW);
        // Android Version , API Level , Boorloader , Build Id , , CPU ABI List
        swInfo=getSWInfo();
        textViewSW.setText(Html.fromHtml(
                "<B>Android Version :</B> " + swInfo[0] + "<br>" +
                        "<B>API Level :</B> " + swInfo[1] + "<br>" +
                        "<B>Bootloader :</B>" + swInfo[2] + "<br>" +
                        "<B>Build ID : </B>" + swInfo[3] + "<br>" +

                        "<B>CPU ABI : </B>" + swInfo[6] + "<br>" +
                        "<B>Kernel :</B> " + swInfo[7] + "<br>" +
                        "<B>Runtime : </B>" + swInfo[8] + "<br>"));


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

}
