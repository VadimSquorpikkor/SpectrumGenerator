package com.atomtex.spectrumgenerator;


import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.balsikandar.crashreporter.CrashReporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import androidx.annotation.RequiresApi;

import static com.atomtex.spectrumgenerator.SpectrumFragment.TAG;
import static com.balsikandar.crashreporter.CrashReporter.getContext;

/**
 * Reads the data from a file of type <code>.ats</code>
 * <p>
 * The class isn't implemented
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class AtsReader {


    public static SpecDTO parseFile(String path) {
        Log.e("TAG", "parseFile PATH: " + path);
        List<Charset> charsets = new ArrayList<>();

        //charsets to parse data
        charsets.add(Charset.forName("UTF-16LE"));
//        charsets.add(Charset.forName("windows-1251"));

        Uri uri = Uri.parse(path);
        String scheme = uri.getScheme();

        InputStream inputStream = null;
        SpecDTO dto = null;

        for (Charset charset : charsets) {
            try {
                if (scheme != null && scheme.equals("content")) {
                    inputStream = getContext().getContentResolver().openInputStream(uri);
                } else {
                    File file = new File(path);
                    inputStream = new FileInputStream(file);
                }
            } catch (FileNotFoundException e) {
                CrashReporter.logException(e);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, charset));
            try {
                dto = parseSpecDTO(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (dto != null) {
                dto.setFileName(path);
            }
        }

        return dto;
    }

//    static Map<String,String> map = new HashMap<>();

    private static SpecDTO parseSpecDTO(BufferedReader reader)
            throws IOException, NumberFormatException {
        SpecDTO dto = new SpecDTO();
        String s;
        String[] splittedLine;

//        dto.setMeasTim(new int[]{0,0,0});//todo затычка


        while ((s = reader.readLine()) != null) {
            s = s.trim();


//            Log.e("TAGGG", "parseSpecDTO: " + s);

            if (s.startsWith("ECALIBRATION")) {
                splittedLine = s.split("=");
                if (splittedLine.length > 1) {
                    int size = Integer.parseInt(splittedLine[1].trim());
                    float[] energy = new float[size];
                    for (int i = 0; i < size; i++) {
                        s = reader.readLine();
                        energy[i] = Float.parseFloat(s);
                    }
                    dto.setEnergy(energy);
                }
            } else if (s.startsWith("RCALIBRATION")) {
                splittedLine = s.split("=");
                if (splittedLine.length > 1) {
                    int size = Integer.parseInt(splittedLine[1].trim());
                    float[] sigma = new float[size];
                    for (int i = 0; i < size; i++) {
                        s = reader.readLine();
                        float value = Float.parseFloat(s);
                        if (value == 0) {
                            sigma[i] = 0.1f;
                        } else {
                            sigma[i] = value;
                        }
                    }
                    dto.setSigma(sigma);
                }
            } else if (s.startsWith("SPECTR")) {
                splittedLine = s.split("=");
                if (splittedLine.length > 1) {
                    int size = Integer.parseInt(splittedLine[1].trim());
                    int[] spectrum = new int[size];
                    for (int i = 0; i < size; i++) {
                        s = reader.readLine();
                        spectrum[i] = (int) Float.parseFloat(s);
                    }
                    dto.setSpectrum(spectrum);
                }
            } else if(s.startsWith("TIME")){
                splittedLine = s.split("=");
                int time = 0;
                if (splittedLine.length == 2 ) time = Integer.parseInt(splittedLine[1].trim()); //(splittedLine.length == 2) т.е. в map записывать только не пустые переменные (только те, которые чему-нибудь равны)
                dto.setMeasTim(new int[]{time, 1}); //todo вторая переменная просто затычка
            }
//            Log.e(TAG, "--------------MAP SIZE = " + map.size() );
        }



/*        else {
            splittedLine = s.split("=");
            if (splittedLine.length == 2 ) map.put(splittedLine[0], splittedLine[1]); //(splittedLine.length == 2) т.е. в map записывать только не пустые переменные (только те, которые чему-нибудь равны)
//                 else map.put(splittedLine[0], "0");
        }
        Log.e(TAG, "--------------MAP SIZE = " + map.size() );*/



//        if(map.containsKey("ACTIVITYRESULT"))dto.setActivityResult(map.get("ACTIVITYRESULT"));
//        if(map.containsKey("NEUTRON_COUNT"))dto.setNeutronCount(Long.parseLong(map.get("NEUTRON_COUNT")));
//        if(map.containsKey("TIME"))dto.setMeasTim(new int[]{Integer.parseInt(map.get("TIME")),0});
//        int[] arr = new int[]{5,5};


        ///////////////////////////////////////////////////////////if(map.containsKey("TIME"))dto.setMeasTim(new int[]{Integer.parseInt(map.get("TIME")), 1});


//        if(map.containsKey("TIME"))dto.setIsBGNDSubtracted(Integer.parseInt(map.get("TIME")));//todo remove

//        Log.e("TAGGG", "parseSpecDTO: arr[0] = " + arr[0]);

//        Log.e("TAGGG", "parseSpecDTO: time = " + dto.getMeasTim()[0]);
//        Log.e("TAGGG", "parseSpecDTO: time2 = " + dto.getMeasTim()[1]);
        return dto;
    }
}
