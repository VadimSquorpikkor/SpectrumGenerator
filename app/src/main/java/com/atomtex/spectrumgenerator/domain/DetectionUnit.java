package com.atomtex.spectrumgenerator.domain;


import static com.atomtex.spectrumgenerator.domain.DetectionUnit.DetectionUnitType.BDKG03;
import static com.atomtex.spectrumgenerator.domain.DetectionUnit.DetectionUnitType.BDKG04;
import static com.atomtex.spectrumgenerator.domain.DetectionUnit.DetectionUnitType.BDKG11;
import static com.atomtex.spectrumgenerator.domain.DetectionUnit.DetectionUnitType.BDKG11M;
import static com.atomtex.spectrumgenerator.domain.DetectionUnit.DetectionUnitType.BDKG19M;
import static com.atomtex.spectrumgenerator.domain.DetectionUnit.DetectionUnitType.BDKG28;
import static com.atomtex.spectrumgenerator.domain.DetectionUnit.DetectionUnitType.BDKG34;
import static com.atomtex.spectrumgenerator.domain.DetectionUnit.DetectionUnitType.BDKG35;
import static com.atomtex.spectrumgenerator.domain.DetectionUnit.DetectionUnitType.BDKN05;
import static com.atomtex.spectrumgenerator.util.Constant.COUNT_DU;
import static com.atomtex.spectrumgenerator.util.Constant.GAMMA_DU;
import static com.atomtex.spectrumgenerator.util.Constant.HIGH_DOSE_RATE_DU;
import static com.atomtex.spectrumgenerator.util.Constant.ID_BDKG03;
import static com.atomtex.spectrumgenerator.util.Constant.ID_BDKG04;
import static com.atomtex.spectrumgenerator.util.Constant.ID_BDKG11;
import static com.atomtex.spectrumgenerator.util.Constant.ID_BDKG11M;
import static com.atomtex.spectrumgenerator.util.Constant.ID_BDKG19M;
import static com.atomtex.spectrumgenerator.util.Constant.ID_BDKG28;
import static com.atomtex.spectrumgenerator.util.Constant.ID_BDKG34;
import static com.atomtex.spectrumgenerator.util.Constant.ID_BDKG35;
import static com.atomtex.spectrumgenerator.util.Constant.ID_BDKN05;
import static com.atomtex.spectrumgenerator.util.Constant.NEUTRON_DU;

/**
 * This class represents the detection unit.
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class DetectionUnit {

    /**
     * Serial number of the detection unit.
     */
    private int serialNumber;

    /**
     * Date manufactures of the detection unit.
     */
    private String dateManufacture = "";

    /**
     * Spectrum data from the detection unit.
     */
    private int[] spectrum;

    /**
     * The time of accumulation of the spectrum.
     */
    private int timeAccSpec;

    /**
     * The spectrum that keeps a dependency channel - energy (Kev)
     */
    private float[] spectrumEnergy;

    /**
     * The spectrum that keeps dependency channel -  the Sigma Gauss function in channels.
     */
    private float[] spectrumSigma;

    /**
     * The number of version ROM.
     *
     * <p>
     * The number is counted with following way:
     * (Last 2 digits of the year) * 400 + (the number of the month) * 31 + (day of month).
     */
    private int device_rom;

    /**
     * The stabilization gain code of the detection unit.
     */
    private float stabGainCode;

    /**
     * The value of the high voltage of the detection unit.
     */
    private int highVoltage;

    /**
     * Concrete type of the detection unit.
     */
    private DetectionUnitType mUnitType;

    public DetectionUnit(short id) {
        mUnitType = getDetectionUnitType(id);
    }

    /**
     * Get id of the detection unit and return concrete type of a detection unit according that id.
     *
     * @param id device id of a detection unit
     * @return concrete type of the detection unit according to id.
     */
    public static DetectionUnitType getDetectionUnitType(short id) {

        switch (id) {
            case ID_BDKN05:
                return BDKN05;

            case ID_BDKG04:
                return BDKG04;

            case ID_BDKG11:
                return BDKG11;

            case ID_BDKG11M:
                return BDKG11M;

            case ID_BDKG19M:
                return BDKG19M;

            case ID_BDKG28:
                return BDKG28;

            case ID_BDKG34:
                return BDKG34;

            case ID_BDKG35:
                return BDKG35;

            case ID_BDKG03:
                return BDKG03;

            default:
                return null;
        }
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDateManufacture() {
        return dateManufacture;
    }

    public void setDateManufacture(String dateManufacture) {
        this.dateManufacture = dateManufacture;
    }

    public int[] getSpectrum() {
        return spectrum;
    }

    public void setSpectrum(int[] spectrum) {
        this.spectrum = spectrum;
    }

    public int getTimeAccSpec() {
        return timeAccSpec;
    }

    public void setTimeAccSpec(int timeAccSpec) {
        this.timeAccSpec = timeAccSpec;
    }

    public float[] getSpectrumEnergy() {
        return spectrumEnergy;
    }

    public void setSpectrumEnergy(float[] spectrumEnergy) {
        this.spectrumEnergy = spectrumEnergy;
    }

    public float[] getSpectrumSigma() {
        return spectrumSigma;
    }

    public void setSpectrumSigma(float[] spectrumSigma) {
        this.spectrumSigma = spectrumSigma;
    }

    public int getDevice_rom() {
        return device_rom;
    }

    public void setDevice_rom(int device_rom) {
        this.device_rom = device_rom;
    }

    public float getStabGainCode() {
        return stabGainCode;
    }

    public void setStabGainCode(float fStabGainCode) {
        this.stabGainCode = fStabGainCode;
    }

    public int getHighVoltage() {
        return highVoltage;
    }

    public void setHighVoltage(int highVoltage) {
        this.highVoltage = highVoltage;
    }

    public DetectionUnitType getUnitType() {
        return mUnitType;
    }

    /**
     * This enum contains constant values of detection units.
     * <p>
     * This enum contains all the detection units that application can determine.
     *
     * @author stanislav.kleinikov@gmail.com
     */
    public enum DetectionUnitType {

        BDKN05(
                ID_BDKN05,       //short id;
                "BDKN-05",       //String name;
                NEUTRON_DU,      //The type of the detection unit
                false,           //boolean isSpectrometric;
                false,           //boolean isDosimetric;
                true,            //boolean isRadiometric;
                false,           //boolean canSayTemperature;
                false,           //boolean needSpectrometricCalibrations;
                2400,            //int unfreezingTime;
                1,               //int channels         //Количество каналов
                (byte) 0x02,     //byte acqMask;	    //маска определения набирается ли спектр
                0,               //float maxDoserate;	//максимальная мощность дозы
                40000,           //float maxCps;	    //максимальная загрузка
                40000,           //float maxIDCps;	//максимальная загрузка для спектрометрии
                0,               //float minIDCps;	//минимальная загрузка для спектрометрии
                0,               //int maxDiagramCPS;	//максимальное значение СС выше которой начнется масштабирование диаграммы
                0,               //float middleCPSonBKG;	//средняя скорость счета на фоне
                0,               //int maxGainCode;	//max gain code
                0                //int startGain;	//gain code for the case we will see K40 peak
        ),

        BDKG11M(
                ID_BDKG11M,      //short id;
                "BDKG-11M",      //String name;
                GAMMA_DU,        //The type of the detection unit
                true,            //boolean isSpectrometric;
                true,            //boolean isDosimetric;
                false,           //boolean isRadiometric;
                true,            //boolean canSayTemperature;
                true,            //boolean needSpectrometricCalibrations;
                24000,           //int unfreezingTime;
                1024,            //int channels
                (byte) 0x02,     //byte acqMask;	    //маска определения набирается ли спектр
                150000,          //float maxDoserate;	//максимальная мощность дозы
                290000,          //float maxCps;	    //максимальная загрузка
                120000,          //float maxIDCps;	//максимальная загрузка для спектрометрии
                1000,            //float minIDCps;	//минимальная загрузка для спектрометрии
                800,             //int maxDiagramCPS;	//максимальное значение СС выше которой начнется масштабирование диаграммы
                250,             //float middleCPSonBKG;	//средняя скорость счета на фоне
                2047,            //int maxGainCode;	//max gain code
                1024             //int startGain;	//gain code for the case we will see K40 peak
        ),

        BDKG19M(
                ID_BDKG19M,      //short id;
                "BDKG-19M",      //String name;
                GAMMA_DU,        //The type of the detection unit
                true,            //boolean isSpectrometric;
                true,            //boolean isDosimetric;
                false,           //boolean isRadiometric;
                true,            //boolean canSayTemperature;
                true,            //boolean needSpectrometricCalibrations;
                24000,           //int unfreezingTime;
                1024,            //int channels
                (byte) 0x02,     //byte acqMask;	    //маска определения набирается ли спектр
                50000,           //float maxDoserate;	//максимальная мощность дозы
                250000,          //float maxCps;	    //максимальная загрузка
                80000,           //float maxIDCps;	//максимальная загрузка для спектрометрии
                2000,            //float minIDCps;	//минимальная загрузка для спектрометрии
                2000,            //int maxDiagramCPS;	//максимальное значение СС выше которой начнется масштабирование диаграммы
                625,             //float middleCPSonBKG;	//средняя скорость счета на фоне
                2047,            //int maxGainCode;	//max gain code
                1024             //int startGain;	//gain code for the case we will see K40 peak
        ),

        BDKG28(
                ID_BDKG28,       //short id;
                "BDKG-28",       //String name;
                GAMMA_DU,        //The type of the detection unit
                true,            //boolean isSpectrometric;
                true,            //boolean isDosimetric;
                false,           //boolean isRadiometric;
                true,            //boolean canSayTemperature;
                true,            //boolean needSpectrometricCalibrations;
                24000,           //int unfreezingTime;
                1024,            //int channels
                (byte) 0x02,     //byte acqMask;	    //маска определения набирается ли спектр
                7000,            //float maxDoserate;	//максимальная мощность дозы
                290000,          //float maxCps;	    //максимальная загрузка
                120000,          //float maxIDCps;	//максимальная загрузка для спектрометрии
                5000,            //float minIDCps;	//минимальная загрузка для спектрометрии
                6000,            //int maxDiagramCPS;	//максимальное значение СС выше которой начнется масштабирование диаграммы
                3500,            //float middleCPSonBKG;	//средняя скорость счета на фоне
                2047,            //int maxGainCode;	//max gain code
                1024             //int startGain;	//gain code for the case we will see K40 peak
        ),

        BDKG11(
                ID_BDKG11,       //short id;
                "BDKG-11",       //String name;
                GAMMA_DU,        //The type of the detection unit
                true,            //boolean isSpectrometric;
                true,            //boolean isDosimetric;
                false,           //boolean isRadiometric;
                true,            //boolean canSayTemperature;
                true,            //boolean needSpectrometricCalibrations;
                24000,           //int unfreezingTime;
                512,             //int channels
                (byte) 0x02,     //byte acqMask;	    //маска определения набирается ли спектр
                100000,          //float maxDoserate;	//максимальная мощность дозы
                200000,          //float maxCps;	    //максимальная загрузка
                90000,           //float maxIDCps;	//максимальная загрузка для спектрометрии
                1000,            //float minIDCps;	//минимальная загрузка для спектрометрии
                800,             //int maxDiagramCPS;	//максимальное значение СС выше которой начнется масштабирование диаграммы
                250,             //float middleCPSonBKG;	//средняя скорость счета на фоне
                255,             //int maxGainCode;	//max gain code
                100              //int startGain;	//gain code for the case we will see K40 peak
        ),

        BDKG04(
                ID_BDKG04,       //short id;
                "BDKG-04",       //String name;
                HIGH_DOSE_RATE_DU,//The type of the detection unit
                false,           //boolean isSpectrometric;
                true,            //boolean isDosimetric;
                false,           //boolean isRadiometric;
                false,           //boolean canSayTemperature;
                false,           //boolean needSpectrometricCalibrations;
                2400,            //int unfreezingTime;
                1,               //int channels
                (byte) 0x02,     //byte acqMask;	    //маска определения набирается ли спектр
                10000000000f,    //float maxDoserate;	//максимальная мощность дозы
                0,               //float maxCps;	    //максимальная загрузка
                0,               //float maxIDCps;	//максимальная загрузка для спектрометрии
                0,               //float minIDCps;	//минимальная загрузка для спектрометрии
                0,               //int maxDiagramCPS;	//максимальное значение СС выше которой начнется масштабирование диаграммы
                0,               //float middleCPSonBKG;	//средняя скорость счета на фоне
                0,               //int maxGainCode;	//max gain code
                0                //int startGain;	//gain code for the case we will see K40 peak
        ),

        BDKG34(
                ID_BDKG34,       //short id;
                "BDKG-34",       //String name;
                GAMMA_DU,        //The type of the detection unit
                true,            //boolean isSpectrometric;
                true,            //boolean isDosimetric;
                false,           //boolean isRadiometric;
                true,            //boolean canSayTemperature;
                true,            //boolean needSpectrometricCalibrations;
                24000,           //int unfreezingTime;
                1024,            //int channels
                (byte) 0x02,     //byte acqMask;	    //маска определения набирается ли спектр
                10000,           //float maxDoserate;	//максимальная мощность дозы
                290000,          //float maxCps;	    //максимальная загрузка
                120000,          //float maxIDCps;	//максимальная загрузка для спектрометрии
                5000,            //float minIDCps;	//минимальная загрузка для спектрометрии
                6000,            //int maxDiagramCPS;	//максимальное значение СС выше которой начнется масштабирование диаграммы
                3000,            //float middleCPSonBKG;	//средняя скорость счета на фоне
                2047,            //int maxGainCode;	//max gain code
                1024             //int startGain;	//gain code for the case we will see K40 peak
        ),

        BDKG35(
                ID_BDKG35,       //short id;
                "BDKG-35",       //String name;
                COUNT_DU,        //The type of the detection unit
                false,           //boolean isSpectrometric;
                true,            //boolean isDosimetric;
                false,           //boolean isRadiometric;
                false,           //boolean canSayTemperature;
                false,           //boolean needSpectrometricCalibrations;
                24000,           //int unfreezingTime;
                1,               //int channels
                (byte) 0x02,     //byte acqMask;	    //маска определения набирается ли спектр
                10000000000f,    //float maxDoserate;	//максимальная мощность дозы
                500000,          //float maxCps;	    //максимальная загрузка
                120000,          //float maxIDCps;	//максимальная загрузка для спектрометрии
                5000,            //float minIDCps;	//минимальная загрузка для спектрометрии
                6000,            //int maxDiagramCPS;	//максимальное значение СС выше которой начнется масштабирование диаграммы
                3000,            //float middleCPSonBKG;	//средняя скорость счета на фоне
                2047,            //int maxGainCode;	//max gain code
                1024             //int startGain;	//gain code for the case we will see K40 peak
        ),

        BDKG03(
                ID_BDKG03,       //short id;
                "BDKG-03",       //String name;
                GAMMA_DU,        //The type of the detection unit
                true,            //boolean isSpectrometric;
                true,            //boolean isDosimetric;
                false,           //boolean isRadiometric;
                true,            //boolean canSayTemperature;
                true,            //boolean needSpectrometricCalibrations;
                24000,           //int unfreezingTime;
                512,             //int channels
                (byte) 0x02,     //byte acqMask;	    //маска определения набирается ли спектр
                300000,          //float maxDoserate;	//максимальная мощность дозы
                200000,          //float maxCps;	    //максимальная загрузка
                90000,           //float maxIDCps;	//максимальная загрузка для спектрометрии
                200,             //float minIDCps;	//минимальная загрузка для спектрометрии
                200,             //int maxDiagramCPS;	//максимальное значение СС выше которой начнется масштабирование диаграммы
                80,              //float middleCPSonBKG;	//средняя скорость счета на фоне
                255,             //int maxGainCode;	//max gain code
                100              //int startGain;	//gain code to see K40 peak
        );

        /**
         * Detection unit id
         */
        public final short id;   //Detection unit id

        /**
         * Detection unit name
         */
        public final String name; //Detection unit name

        /**
         * The type of the detection unit
         */
        public final char type;      //The type of the detection unit

        /**
         * Whether the unit can show spectrum
         */
        public final boolean isSpectrometric;    //может показать спектр

        /**
         * Whether the unit can show dose rate
         */
        public final boolean isDosimetric;       //может выдать мощность дозы

        /**
         * Whether the unit can measure the flow density
         */
        public final boolean isRadiometric;      //может мерить плотность потока

        /**
         * Whether the unit can show temperature
         */
        public final boolean canSayTemperature;  //может показывать температуру

        /**
         * Whether the unit can be calibrated for energy resolution and can have library of nuclides
         */
        public final boolean needSpectrometricCalibrations;    //может иметь калибровки по энергии разрешению и библиотеку нуклидов

        /**
         * Warm up time of the detection unit.
         */
        public final int unfreezingTime; // время прогрева

        /**
         * The number of channels
         */
        public final int channels;

        /**
         * The mask to determine whether the spectrum is accumulating
         */
        public final byte acqMask;               //маска определения набирается ли спектр

        /**
         * The maximum value of dose rate
         */
        public final float maxDoserate;          //максимальная мощность дозы

        /**
         * Maximum load
         */
        public final float maxCps;               //максимальная загрузка

        /**
         * The maximum load for spectrometry
         */
        public final float maxIDCps;             //максимальная загрузка для спектрометрии

        /**
         * The minimum load for spectrometry
         */
        public final float minIDCps;             //минимальная загрузка для спектрометрии

        /**
         * The maximum count rate above which the chart will start scaling
         */
        public final int maxDiagramCPS;          //максимальное значение скорости счёта выше которой начнется масштабирование диаграммы

        /**
         * The average count rate in the background
         */
        public final float middleCPSonBKG;       //средняя скорость счета на фоне

        /**
         * The max gain code
         */
        public final int maxGainCode;

        /**
         * Gain code to see K40 peak
         */
        public final int startGain;

        DetectionUnitType(short id, String name, char type, boolean isSpectrometric, boolean isDosimetric,
                          boolean isRadiometric, boolean canSayTemperature,
                          boolean needSpectrometricCalibrations, int unfreezingTime, int channels,
                          byte acqMask, float maxDoserate, float maxCps, float maxIDCps,
                          float minIDCps, int maxDiagramCPS, float middleCPSonBKG, int maxGainCode,
                          int startGain) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.isSpectrometric = isSpectrometric;
            this.isDosimetric = isDosimetric;
            this.isRadiometric = isRadiometric;
            this.canSayTemperature = canSayTemperature;
            this.needSpectrometricCalibrations = needSpectrometricCalibrations;
            this.unfreezingTime = unfreezingTime;
            this.channels = channels;
            this.acqMask = acqMask;
            this.maxDoserate = maxDoserate;
            this.maxCps = maxCps;
            this.maxIDCps = maxIDCps;
            this.minIDCps = minIDCps;
            this.maxDiagramCPS = maxDiagramCPS;
            this.middleCPSonBKG = middleCPSonBKG;
            this.maxGainCode = maxGainCode;
            this.startGain = startGain;
        }
    }
}


