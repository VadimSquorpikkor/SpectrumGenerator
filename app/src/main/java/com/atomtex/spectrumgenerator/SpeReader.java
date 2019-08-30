package com.atomtex.spectrumgenerator;

import android.net.Uri;

import com.balsikandar.crashreporter.CrashReporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.balsikandar.crashreporter.CrashReporter.getContext;

/**
 * This class was designed to read spectrum data file.
 *
 * @author stanislav.kleinikov@gmail.com
 */
class SpeReader {

    /**
     * Read data from a file according the given path using different encoding
     *
     * @param path to a file
     * @return the object representation of a data from the file or null in case of error
     */
    static SpecDTO parseFile(String path) {

        List<Charset> charsets = new ArrayList<>();

        //charsets to parse data
        charsets.add(Charset.forName("UTF-16LE"));
        charsets.add(Charset.forName("windows-1251"));

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
                dto = parseSpectrumSpeDTO(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (dto == null) {
                    continue;
                }
                dto.setFileName(path);
                break;

        }

        return dto;
    }

    /**
     * Performs the parsing of data from file and returns the data in form of {@link SpecDTO} object
     *
     * @param reader object that is used to read data from file
     * @return {@link SpecDTO} object that contains data from the file
     * @throws IOException           in case of error while reading
     * @throws NumberFormatException in case of error while parsing number from the file
     *                               due to wrong format
     */
    private static SpecDTO parseSpectrumSpeDTO(BufferedReader reader)
            throws IOException, NumberFormatException {
        SpecDTO dto = new SpecDTO();

        String line;
        dto.setTemperature(-129);
        while ((line = reader.readLine()) != null) {
            line = line.trim();

            switch (line) {
                case "$MCA_166_ID:":
                    dto.setlMca166Id(parseMcaID(reader));
                    break;
                case "$SPEC_REM:":
                    dto.setSpecRem(parseSpecRem(reader));
                    break;
                case "$DATE_MEA:":
                    dto.setDateMea(reader.readLine().trim());
                    break;
                case "$MEAS_TIM:":
                    dto.setMeasTim(parseMeasTim(reader));
                    break;
                case "$CPS:":
                    dto.setCPS(Float.parseFloat(reader.readLine().trim()));
                    break;
                case "$NEUTRON_CPS:":
                    dto.setNeutronCPS(Float.parseFloat(reader.readLine().trim()));
                    break;
                case "$NEUTRON_DOSERATE:":
                    dto.setNeutronDoseRate(Float.parseFloat(reader.readLine().trim()));
                    break;
                case "$NEUTRON_COUNT:":
                    dto.setNeutronCount(Long.parseLong(reader.readLine().trim()));
                    break;
                case "$DATA:":
                    dto.setSpectrum(parseFDataMass(reader));
                    break;
                case "$ROI:":
                    dto.setRoi(Integer.parseInt(reader.readLine().trim()));
                    break;
                case "$ENER_TABLE:":
                    dto.setEnergy(parseFEnerTableMass(reader));
                    break;
                case "$SIGM_DATA:":
                    dto.setSigma(parseSigmDataMass(reader));
                    break;
                case "$TEMPERATURE:":
                    dto.setTemperature(Float.parseFloat(reader.readLine().trim()));
                    break;
                case "$SCALE_MODE:":
                    dto.setShScaleMode(Short.parseShort(reader.readLine().trim()));
                    break;
                case "$DOSE_RATE:":
                    dto.setDoseRate(Float.parseFloat(reader.readLine().trim()));
                    break;
                case "$CPSOUTOFRANGE:":
                    dto.setCPSOutOfRange(Float.parseFloat(reader.readLine().trim()));
                    break;
                case "$DU_NAME:":
                    dto.setDUName(reader.readLine());
                    break;
                case "$INSTRUMENT_TYPE:":
                    dto.setInstrumentType(reader.readLine());
                    break;
                case "$RADIONUCLIDES:":
                    dto.setRadioNuclides(reader.readLine());
                    break;
                case "$ACTIVITYRESULT:":
                    dto.setActivityResult(reader.readLine());
                    break;
                case "$EFFECTIVEACTIVITYRESULT:":
                    dto.setEffectiveActivityResult(reader.readLine());
                    break;
                case "$GEOMETRY:":
                    dto.setGeometry(reader.readLine());
                    break;
                case "$MIX:":
                    dto.setMix(reader.readLine());
                    break;
                case "$SPECTRUMPROCESSED:":
                    dto.setIsSpectrumProcessed(Integer.parseInt(reader.readLine().trim()));
                    break;
                case "$BGNDSUBTRACTED:":
                    dto.setIsBGNDSubtracted(Integer.parseInt(reader.readLine().trim()));
                    break;
                case "$ENCRYPTED:":
                    dto.setIsEncrypted(Integer.parseInt(reader.readLine().trim()));
                    break;
                case "$DATE_MANUFACT:":
                    dto.setDateManufact(reader.readLine());
                    break;
                case "$GPS:":
                    dto.setGps(parseGPS(reader));
                    break;
                case "$STATUS_OF_HEALTH:":
                    dto.setStatusOfHealth(reader.readLine());
                    break;
                default:
                    if (line.length() > 2 && line.charAt(0) == '$'
                            && line.charAt(line.length() - 1) == ':') {
                        if (dto.getListPersonParam() == null) {
                            dto.setListPersonParam(new HashMap<String, String>());
                        }
                        Map<String, String> map = dto.getListPersonParam();
                        map.put(line, reader.readLine().trim());
                    }
                    break;
            }
        }

        return dto;
    }

    //-------------------Methods to parse specific data from file---------------------


    /*
      The method is not used current version

      @since 1.0
     */
/*    @Deprecated
    private static float[] parseEnerFit(BufferedReader reader) throws IOException {
        float[] result = new float[2];
        String[] splittedLine = reader.readLine().trim().split(" ");
        result[0] = Float.parseFloat(splittedLine[0]);
        result[1] = Float.parseFloat(splittedLine[1]);
        return result;
    }*/

    /**
     * Read information about spectrum accumulation time
     *
     * @param reader for reading file
     * @return array containing data [real_spectrum_time_accumulation, astronomic_spectrum_time_accumulation]
     * @throws IOException in case of error while reading
     */
    private static int[] parseMeasTim(BufferedReader reader) throws IOException {
        int[] result = new int[2];
        String[] splittedLine = reader.readLine().trim().split(" ");
        result[0] = Integer.parseInt(splittedLine[0]);
        result[1] = Integer.parseInt(splittedLine[1]);
        return result;
    }

    /**
     * Read information about keyword that used to make the file
     *
     * @param reader for reading file
     * @return array contains the following data - [key_word,empty_string]
     * @throws IOException in case of error while reading
     */
    private static String[] parseSpecRem(BufferedReader reader) throws IOException {
        String[] result = new String[2];
        result[0] = reader.readLine();
        result[1] = reader.readLine();
        return result;
    }

    /**
     * Parse serial number of adapters , hardWare and firmWare numbers
     *
     * @param reader for reading file
     * @return array containing data [serial number of adapter, hardware number,firmWare number]
     * @throws IOException in case of error while reading
     */
    private static int[] parseMcaID(BufferedReader reader) throws IOException {
        int[] result = new int[3];
        for (int i = 0; i < 3; i++) {
            String[] splittedLine = reader.readLine().trim().split(" ");
            result[i] = Integer.parseInt(splittedLine[1]);
        }
        return result;
    }

    /**
     * Parse gps data
     *
     * @param reader for reading file
     * @return float array with GPS data
     * @throws IOException in case of error while reading
     */
    private static float[] parseGPS(BufferedReader reader) throws IOException {
        float[] result = new float[6];
        for (int i = 0; i < 6; i++) {
            String[] splittedLine = reader.readLine().trim().split(" ");
            result[i] = Float.parseFloat(splittedLine[1]);
        }
        return result;
    }

    /**
     * Parse spectrum array
     *
     * @param reader for reading file
     * @return spectrum array
     * @throws IOException in case of error while reading
     */
    private static int[] parseFDataMass(BufferedReader reader) throws IOException {
        String[] splittedLine = reader.readLine().trim().split(" ");
        int startIndex = Integer.parseInt(splittedLine[0]);
        int endIndex = Integer.parseInt(splittedLine[1]);
        int[] mass = new int[endIndex - startIndex + 1];
        for (int i = 0; i < mass.length; i++) {
            String value = reader.readLine().trim();
            mass[i] = Integer.parseInt(value);
        }
        return mass;
    }

    /**
     * Parse energy array
     *
     * @param reader for reading file
     * @return energy array
     * @throws IOException in case of error while reading
     */
    private static float[] parseFEnerTableMass(BufferedReader reader) throws IOException {
        String[] splittedLine;
        int resultIndex = Integer.parseInt(reader.readLine().trim());
        float[] mass = new float[resultIndex];
        for (int i = 0; i < resultIndex; i++) {
            splittedLine = reader.readLine().trim().split(" ");
            mass[i] = Float.parseFloat(splittedLine[1]);
        }
        return mass;
    }

    /**
     * Parse sigma array
     *
     * @param reader for reading file
     * @return sigma array
     * @throws IOException in case of error while reading
     */
    private static float[] parseSigmDataMass(BufferedReader reader) throws IOException {
        String[] splittedLine;
        int resultIndex = Integer.parseInt(reader.readLine().trim());
        float[] mass = new float[resultIndex];
        for (int i = 0; i < resultIndex; i++) {
            splittedLine = reader.readLine().trim().split(" ");
            mass[i] = Float.parseFloat(splittedLine[1]);
        }
        return mass;
    }
}