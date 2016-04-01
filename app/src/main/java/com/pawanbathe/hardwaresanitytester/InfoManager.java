package com.pawanbathe.hardwaresanitytester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by pbathe on 8/3/16.
 */
public class InfoManager {

    public InfoManager()
    {

    }
    public static String cmdCat(String f){

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

    public static String getProp(String property)
    {
        String[] command={"getprop",property};
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


    public static String ShellExecuter(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String response = output.toString();
        return response;

    }
    public static String grepDumpSys(String param)
    {
        String[] command={"dumpsys"," | ","grep", "GLES" ,">" ,"/data/local/tmp/a.txt "};
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


}
