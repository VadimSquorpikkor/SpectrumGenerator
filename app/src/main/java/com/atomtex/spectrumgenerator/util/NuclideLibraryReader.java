package com.atomtex.spectrumgenerator.util;


import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.atomtex.spectrumgenerator.domain.EnergyLine;
import com.atomtex.spectrumgenerator.domain.Nuclide;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import static com.atomtex.spectrumgenerator.MainActivity.TAG;
import static com.atomtex.spectrumgenerator.domain.Nuclide.State.UNIDENTIFIED;
import static com.atomtex.spectrumgenerator.domain.Nuclide.State.VALID;
import static com.balsikandar.crashreporter.CrashReporter.getContext;

public final class NuclideLibraryReader {

//    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static List<Nuclide> getLibrary(String path) throws IOException {
        Log.e(TAG, "попытка загрузки библиотеки по адресу: " + path);
        List<Nuclide> list = new ArrayList<>();
        List<Charset> charsets = new ArrayList<>();

        //The charsets to parse data
        charsets.add(Charset.forName("UTF-16LE"));
        charsets.add(Charset.forName("windows-1251"));

        Uri uri = Uri.parse(path);
        InputStream inputStream = getContext().getContentResolver().openInputStream(uri);

        //for Encrypter
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        InputStream inputByteStream = getContext().getContentResolver().openInputStream(uri);
        while ((nRead = inputByteStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }


        list = libraryParser(charsets, data);
        if(list.size()==0) {
            list = libraryParser(charsets, Encrypter.convertData(data));
            Log.e(TAG, "**************ENCRYPTER STARTS************************");
        }
        return list;
    }

    private static List<Nuclide> libraryParser(List<Charset> charsets, byte[] data) throws IOException {
        List<Nuclide> list = new ArrayList<>();
        for (Charset charset : charsets) {
            try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
                BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data), charset));

                String firstLine = reader.readLine();
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
                // этот стринг добавлен для: если будет не та кодировка, list.get(0).getName()
                // не сможет нормально прочитать имя и выкинет эксепшн и сработает следующий за
                // строкой кэтч. Так будет загружаться библиотека только в правильной кодировке
                //noinspection unused
                String proverka = list.get(0).getName();
            } catch (NumberFormatException e) {
                Log.e(TAG, "       кодировка " + charset.displayName() + " не подходит");
            }
        }
    return list;
    }

}


