package com.atomtex.spectrumgenerator.util;


import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.atomtex.spectrumgenerator.SpecDTO;
import com.atomtex.spectrumgenerator.domain.EnergyLine;
import com.atomtex.spectrumgenerator.domain.Nuclide;
import com.balsikandar.crashreporter.CrashReporter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.atomtex.spectrumgenerator.MainActivity.TAG;
import static com.atomtex.spectrumgenerator.domain.Nuclide.State.UNIDENTIFIED;
import static com.balsikandar.crashreporter.CrashReporter.getContext;

public final class NuclideLibraryReader {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static List<Nuclide> getLibrary(String path) throws IOException {
        Log.e(TAG, "getLibrary: STARTS " + path);
        List<Nuclide> list = new ArrayList<>();
        List<Charset> charsets = new ArrayList<>();

        //The charsets to parse data
        charsets.add(Charset.forName("UTF-16LE"));
//        charsets.add(Charset.forName("windows-1251"));
//        charsets.add(Charset.forName("UTF-16LE"));

        Uri uri = Uri.parse(path);
        String scheme = uri.getScheme();
        InputStream inputStream = null;

        for (Charset charset : charsets) {
            try {
                if (scheme != null && scheme.equals("content")) {
                    inputStream = getContext().
                            getContentResolver().
                            openInputStream(uri);
                } else {
                    File file = new File(path);
                    inputStream = new FileInputStream(file);
                }
            } catch (FileNotFoundException e) {
                CrashReporter.logException(e);
            }


            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(
                    inputStream, charset));

                String firstLine = reader.readLine();
//            int nuclNum = Integer.parseInt(reader.readLine().trim());
            int nuclNum = Integer.parseInt(firstLine);
            for (int i = 0; i < nuclNum; i++) {
                Nuclide nuclide = new Nuclide();
                String[] string = reader.readLine().trim().split("\t");
                String[] id = string[0].trim().split("-");
                nuclide.setCategory(Nuclide.Category.valueOf(id[0]));
                String name = id[1];
                String num = id[2];
                nuclide.setName(name);
                nuclide.setNumStr(num);
                String res = name.toUpperCase() + "_" + num;
//                    nuclide.setSound(SoundEvent.valueOf(res));

                nuclide.setState(UNIDENTIFIED);//todo добавил
                int linesNum = Integer.parseInt(string[1]);
                nuclide.setLinesNum(linesNum);

                EnergyLine[] lines = new EnergyLine[linesNum];

                int appender = 0;
                for (int j = 0; j < linesNum; j++) {
                    lines[j] = new EnergyLine(
                            Float.parseFloat(string[2 + appender]),
                            Integer.parseInt(string[3 + appender]),
                            Integer.parseInt(string[4 + appender]),
                            Integer.parseInt(string[5 + appender]),
                            Integer.parseInt(string[6 + appender]));
                    appender += 5;
                }
                nuclide.setEnergyLines(lines);
                list.add(nuclide);
            }

        }
//        Nuclide first = list.get(0);
//        Log.e(TAG, "********NAME -- " + first.getName());
//        Log.e(TAG, "********numSTR -- " + first.getNumStr());
//        Log.e(TAG, "********linesNum -- " + first.getLinesNum());
//        Log.e(TAG, "********weight -- " + first.getWeight());
//        Log.e(TAG, "********ENERGY line size -- " + first.getEnergyLines().length);
//        Log.e(TAG, "********ENERGY line [0] energy -- " + first.getEnergyLines()[0].getEnergy());

        return list;
    }

}


