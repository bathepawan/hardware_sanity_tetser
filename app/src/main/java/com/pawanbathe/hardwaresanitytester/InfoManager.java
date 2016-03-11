package com.pawanbathe.hardwaresanitytester;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pbathe on 8/3/16.
 */
public class InfoManager {

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

}
