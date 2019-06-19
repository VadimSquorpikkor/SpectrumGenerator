package com.atomtex.spectrumgenerator;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Map;

/**
 * Holds the information to write into spectrum file
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class SpecDTO implements Parcelable {

    /**
     * The pattern is used to make name of a spectrum file
     */
    public static final String PATTERN_DATE = "MM/dd/yyyy HH:mm:ss";

    /**
     * The pattern is used to make string containing diagnostic data
     */
    public static final String PATTERN_STATUS_HEALTH = "%1$d, %2$d, %3$d, %4$d";

    /**
     * The date of the writing of the file
     */
    private Date mDate;               // Date of the writing

    /**
     * The file path
     */
    private String fileName;          // The file path

    /**
     * Spectrum data
     */
    private int[] mSpectrum;          // Spectrum data

    /**
     * Consolidated "Instantaneous" neutron radiation counting rate, s-1, 32 bit
     */
    private long mNeutronCount;       //n_momcps

    /**
     * Priority-averaged Average dose rate of neutron radiation in floating point format, 32bit. Dimension nSv/h
     */
    private float mNeutronDoseRate;   //n_dr

    /**
     * Priority Average neutron counting rate in floating point format, s-1, 32 bit
     */
    private float mNeutronCPS;        //n_cps

    /**
     * Priority Average count rate of gamma-radiation spectrometric detection unit in the format
     * of floating point, s-1, 32-bit
     */
    private float mCPS;               //g_cps

    /**
     * The energy spectrum
     */
    private float[] mEnergy;          //Energy

    /**
     * The sigma spectrum
     */
    private float[] mSigma;           //Sigma

    /**
     * The value of the battery temperature monitor in degrees. The greatest byte is the integer part,
     * the smallest byte – fractional part
     */
    private float mTemperature;       //temperatureHL
    private int shScaleMode;          //1

    /**
     * Priority-averaged Average dose rate of gamma radiation of a spectrometric detection unit
     * in a floating point format, 32 bits. Dimension nSv/h
     */
    private float mDoseRate;          //g_dr

    /**
     * Is not used currently
     *
     * @since 1.0
     */
    private float mCPSOutOfRange;     //0

    /**
     * The name of device
     */
    private String mDUName;

    /**
     * The name of device
     */
    private String mInstrumentType;

    /**
     * Identified nuclides
     */
    private String mRadioNuclides;

    /**
     * Is not used currently
     *
     * @since 1.0
     */
    private String mActivityResult;

    /**
     * Is not used currently
     *
     * @since 1.0
     */
    private String mEffectiveActivityResult;

    /**
     * Is not used currently
     *
     * @since 1.0
     */
    private String mGeometry;

    /**
     * Is not used currently
     *
     * @since 1.0
     */
    private String mMix;

    /**
     * Is not used currently
     *
     * @since 1.0
     */
    private int mIsSpectrumProcessed; //0

    /**
     * Is not used currently
     *
     * @since 1.0
     */
    private int mIsBGNDSubtracted;    //0

    /**
     * Whether the data is encrypted
     */
    private int mIsEncrypted;

    /**
     * Gps data [Longitude,Latitude,Altitude,Speed,Direction,Validity]
     */
    private float[] mGps;             // [Lon,Lat,Alt,Speed,Dir,Valid]

    /**
     * String containing diagnostic data
     */
    private String mStatusOfHealth;

    /**
     * Contains data [serial number of adapter, hardware number,firmWare number]
     */
    private int[] lMca166Id;

    /**
     * Array contains the following data - [key_word,empty_string]
     */
    private String[] mSpecRem;        // "key_word,empty_string"

    /**
     * The date of creation of the dto
     */
    private String mDateMea;

    /**
     * Array contains data [real_spectrum_time_accumulation, astronomic_spectrum_time_accumulation]
     */
    private int[] mMeasTim;           // [time_accumulation, astronomic_time_accumulation]

    /**
     * Is not used currently
     *
     * @since 1.0
     */
    private int mRoi;                 //0

    /**
     * Is not used currently
     *
     * @since 1.0
     */
    private float[] mEnerFit;

    /**
     * The date of manufacture
     */
    private String mDateManufact;     //The date of manufacture

    /**
     * Custom properties
     */
    private Map<String, String> mListPersonParam; //Custom properties

    public SpecDTO() {

    }

    private SpecDTO(Parcel in) {
        fileName = in.readString();
        mSpectrum = in.createIntArray();
        mNeutronCount = in.readLong();
        mNeutronDoseRate = in.readFloat();
        mNeutronCPS = in.readFloat();
        mCPS = in.readFloat();
        mEnergy = in.createFloatArray();
        mSigma = in.createFloatArray();
        mTemperature = in.readFloat();
        shScaleMode = in.readInt();
        mDoseRate = in.readFloat();
        mCPSOutOfRange = in.readFloat();
        mDUName = in.readString();
        mInstrumentType = in.readString();
        mRadioNuclides = in.readString();
        mActivityResult = in.readString();
        mEffectiveActivityResult = in.readString();
        mGeometry = in.readString();
        mMix = in.readString();
        mIsSpectrumProcessed = in.readInt();
        mIsBGNDSubtracted = in.readInt();
        mIsEncrypted = in.readInt();
        mGps = in.createFloatArray();
        mStatusOfHealth = in.readString();
        lMca166Id = in.createIntArray();
        mSpecRem = in.createStringArray();
        mDateMea = in.readString();
        mMeasTim = in.createIntArray();
        mRoi = in.readInt();
        mEnerFit = in.createFloatArray();
        mDateManufact = in.readString();
    }

    public static final Creator<SpecDTO> CREATOR = new Creator<SpecDTO>() {
        @Override
        public SpecDTO createFromParcel(Parcel in) {
            return new SpecDTO(in);
        }

        @Override
        public SpecDTO[] newArray(int size) {
            return new SpecDTO[size];
        }
    };

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int[] getSpectrum() {
        return mSpectrum;
    }

    public void setSpectrum(int[] spectrum) {
        mSpectrum = spectrum;
    }

    public long getNeutronCount() {
        return mNeutronCount;
    }

    public void setNeutronCount(long neutronCount) {
        mNeutronCount = neutronCount;
    }

    public float getNeutronDoseRate() {
        return mNeutronDoseRate;
    }

    public void setNeutronDoseRate(float neutronDoseRate) {
        mNeutronDoseRate = neutronDoseRate;
    }

    public double getNeutronCPS() {
        return mNeutronCPS;
    }

    public void setNeutronCPS(float neutronCPS) {
        mNeutronCPS = neutronCPS;
    }

    public double getCPS() {
        return mCPS;
    }

    public void setCPS(float CPS) {
        mCPS = CPS;
    }

    public float[] getEnergy() {
        return mEnergy;
    }

    public void setEnergy(float[] energy) {
        mEnergy = energy;
    }

    public float[] getSigma() {
        return mSigma;
    }

    public void setSigma(float[] sigma) {
        mSigma = sigma;
    }

    public float getTemperature() {
        return mTemperature;
    }

    public void setTemperature(float temperature) {
        mTemperature = temperature;
    }

    public int getShScaleMode() {
        return shScaleMode;
    }

    public void setShScaleMode(int shScaleMode) {
        this.shScaleMode = shScaleMode;
    }

    public float getDoseRate() {
        return mDoseRate;
    }

    public void setDoseRate(float doseRate) {
        mDoseRate = doseRate;
    }

    public float getCPSOutOfRange() {
        return mCPSOutOfRange;
    }

    public void setCPSOutOfRange(float CPSOutOfRange) {
        mCPSOutOfRange = CPSOutOfRange;
    }

    public String getDUName() {
        return mDUName;
    }

    public String getInstrumentType() {
        return mInstrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        mInstrumentType = instrumentType;
    }

    public void setDUName(String DUName) {
        mDUName = DUName;
    }

    public String getRadioNuclides() {
        return mRadioNuclides;
    }

    public void setRadioNuclides(String radioNuclides) {
        mRadioNuclides = radioNuclides;
    }

    public String getActivityResult() {
        return mActivityResult;
    }

    public void setActivityResult(String activityResult) {
        mActivityResult = activityResult;
    }

    public String getEffectiveActivityResult() {
        return mEffectiveActivityResult;
    }

    public void setEffectiveActivityResult(String effectiveActivityResult) {
        mEffectiveActivityResult = effectiveActivityResult;
    }

    public String getGeometry() {
        return mGeometry;
    }

    public void setGeometry(String geometry) {
        mGeometry = geometry;
    }

    public String getMix() {
        return mMix;
    }

    public void setMix(String mix) {
        mMix = mix;
    }

    public int getIsSpectrumProcessed() {
        return mIsSpectrumProcessed;
    }

    public void setIsSpectrumProcessed(int isSpectrumProcessed) {
        mIsSpectrumProcessed = isSpectrumProcessed;
    }

    public int getIsBGNDSubtracted() {
        return mIsBGNDSubtracted;
    }

    public void setIsBGNDSubtracted(int isBGNDSubtracted) {
        mIsBGNDSubtracted = isBGNDSubtracted;
    }

    public int getIsEncrypted() {
        return mIsEncrypted;
    }

    public void setIsEncrypted(int isEncrypted) {
        mIsEncrypted = isEncrypted;
    }

    public float[] getGps() {
        return mGps;
    }

    public void setGps(float[] gps) {
        mGps = gps;
    }

    public String getStatusOfHealth() {
        return mStatusOfHealth;
    }

    public void setStatusOfHealth(String statusOfHealth) {
        mStatusOfHealth = statusOfHealth;
    }

    public int[] getlMca166Id() {
        return lMca166Id;
    }

    public void setlMca166Id(int[] lMca166Id) {
        this.lMca166Id = lMca166Id;
    }

    public String[] getSpecRem() {
        return mSpecRem;
    }

    public void setSpecRem(String[] specRem) {
        mSpecRem = specRem;
    }

    public String getDateMea() {
        return mDateMea;
    }

    public void setDateMea(String dateMea) {
        mDateMea = dateMea;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int[] getMeasTim() {
        return mMeasTim;
    }

    public void setMeasTim(int[] measTim) {
        mMeasTim = measTim;
    }

    public int getRoi() {
        return mRoi;
    }

    public void setRoi(int roi) {
        mRoi = roi;
    }

    public float[] getEnerFit() {
        return mEnerFit;
    }

    public void setEnerFit(float[] enerFit) {
        mEnerFit = enerFit;
    }

    public String getDateManufact() {
        return mDateManufact;
    }

    public void setDateManufact(String dateManufact) {
        mDateManufact = dateManufact;
    }

    public Map<String, String> getListPersonParam() {
        return mListPersonParam;
    }

    public void setListPersonParam(Map<String, String> listPersonParam) {
        mListPersonParam = listPersonParam;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeIntArray(mSpectrum);
        dest.writeLong(mNeutronCount);
        dest.writeFloat(mNeutronDoseRate);
        dest.writeFloat(mNeutronCPS);
        dest.writeFloat(mCPS);
        dest.writeFloatArray(mEnergy);
        dest.writeFloatArray(mSigma);
        dest.writeFloat(mTemperature);
        dest.writeInt(shScaleMode);
        dest.writeFloat(mDoseRate);
        dest.writeFloat(mCPSOutOfRange);
        dest.writeString(mDUName);
        dest.writeString(mInstrumentType);
        dest.writeString(mRadioNuclides);
        dest.writeString(mActivityResult);
        dest.writeString(mEffectiveActivityResult);
        dest.writeString(mGeometry);
        dest.writeString(mMix);
        dest.writeInt(mIsSpectrumProcessed);
        dest.writeInt(mIsBGNDSubtracted);
        dest.writeInt(mIsEncrypted);
        dest.writeFloatArray(mGps);
        dest.writeString(mStatusOfHealth);
        dest.writeIntArray(lMca166Id);
        dest.writeStringArray(mSpecRem);
        dest.writeString(mDateMea);
        dest.writeIntArray(mMeasTim);
        dest.writeInt(mRoi);
        dest.writeFloatArray(mEnerFit);
        dest.writeString(mDateManufact);
    }
}