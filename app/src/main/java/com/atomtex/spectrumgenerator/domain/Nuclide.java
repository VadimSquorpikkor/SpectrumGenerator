package com.atomtex.spectrumgenerator.domain;


import android.support.annotation.NonNull;

import java.util.Arrays;

/**
 * Entity that represents a nuclide
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class Nuclide {

    /**
     * The pattern is used for making string from nuclide to save in spectrum file.
     */
    public static final String PATTERN_SPEC_NUCLIDE = "%1$s-%2$s-%3$s [%4$d]; ";

    /**
     * The pattern is used for making string from nuclide to show in UI.
     */
    public static final String PATTERN_UI_NUCLIDE = "%1$s-%2$s; ";

    /**
     * The name of nuclide (i.e. <b>Cs</b>)
     */
    private String mName;        //имя нуклида например Cs

    /**
     * The number of the nuclide (i.e. <b>137</b>)
     */
    private String mNumStr;      //номер нуклида например 137

    /**
     * The category of the nuclide
     *
     * @see Category
     */
    private Category mCategory;  //категория нуклида, символы: N,M,I,U,P

    /**
     * The state of the nuclide
     *
     * @see State
     */
    private State mState;        //CALC: признак отбора нуклида: VALID (прошел условие), INVALID (не прошел условие), UNIDENTIFIED (не идентифицирован), UNKNOWN (под вопросом), IDENTIFIED (идентифицирован)

    /**
     * The number of energy lines
     */
    private int mLinesNum;       //количество энергетических линий и соответственно других параметров

    /**
     * The weight parameter of the nuclide.
     * <p>
     * The greater the value, the more weighty the nuclide in a competition with another
     */
    private int mWeight;         //CALC: весовые параметры, чем больше значение, тем более весомы нуклид в споре с другим

    /**
     * A sound for nuclide.
     *
     * @see SoundEvent
     */
    private SoundEvent mSound;        //appropriate sound


    private int mWeightM;


    private int confidence;

    /**
     * The array of energy lines belonging to nuclide
     *
     * @see EnergyLine
     */
    private EnergyLine[] mEnergyLines;

    public Nuclide() {

    }

    public Nuclide(String name, String numStr, Category category, State state, int linesNum,
                   int weight, int weightM, EnergyLine[] energyLines) {
        mName = name;
        mNumStr = numStr;
        mCategory = category;
        mState = state;
        mLinesNum = linesNum;
        mWeight = weight;
        mWeightM = weightM;
        mEnergyLines = energyLines;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getNumStr() {
        return mNumStr;
    }

    public void setNumStr(String numStr) {
        mNumStr = numStr;
    }

    public Category getCategory() {
        return mCategory;
    }

    public void setCategory(Category category) {
        mCategory = category;
    }

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        mState = state;
    }

    public int getLinesNum() {
        return mLinesNum;
    }

    public void setLinesNum(int linesNum) {
        mLinesNum = linesNum;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }

    public int getWeightM() {
        return mWeightM;
    }

    public void setWeightM(int weightM) {
        mWeightM = weightM;
    }

    public SoundEvent getSound() {
        return mSound;
    }

    public void setSound(SoundEvent sound) {
        mSound = sound;
    }

    public EnergyLine[] getEnergyLines() {
        return mEnergyLines;
    }

    public void setEnergyLines(EnergyLine[] energyLines) {
        mEnergyLines = energyLines;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nuclide)) return false;

        Nuclide nuclide = (Nuclide) o;

        if (mLinesNum != nuclide.mLinesNum) return false;
        if (mWeight != nuclide.mWeight) return false;
        if (mWeightM != nuclide.mWeightM) return false;
        if (confidence != nuclide.confidence) return false;
        if (mName != null ? !mName.equals(nuclide.mName) : nuclide.mName != null) return false;
        if (mNumStr != null ? !mNumStr.equals(nuclide.mNumStr) : nuclide.mNumStr != null)
            return false;
        if (mCategory != nuclide.mCategory) return false;
        if (mState != nuclide.mState) return false;
        if (mSound != nuclide.mSound) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(mEnergyLines, nuclide.mEnergyLines);
    }

    @Override
    public int hashCode() {
        int result = mName != null ? mName.hashCode() : 0;
        result = 31 * result + (mNumStr != null ? mNumStr.hashCode() : 0);
        result = 31 * result + (mCategory != null ? mCategory.hashCode() : 0);
        result = 31 * result + (mState != null ? mState.hashCode() : 0);
        result = 31 * result + mLinesNum;
        result = 31 * result + mWeight;
        result = 31 * result + (mSound != null ? mSound.hashCode() : 0);
        result = 31 * result + mWeightM;
        result = 31 * result + confidence;
        result = 31 * result + Arrays.hashCode(mEnergyLines);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "Nuclide{" +
                "mName='" + mName + '\'' +
                ", mNumStr='" + mNumStr + '\'' +
                ", mCategory=" + mCategory +
                ", mState=" + mState +
                ", mLinesNum=" + mLinesNum +
                ", mWeight=" + mWeight +
                ", mWeightM=" + mWeightM +
                ", confidence=" + confidence +
                ", mEnergyLines=" + Arrays.toString(mEnergyLines) +
                '}';
    }

    /**
     * The enum contains the category of the specific nuclide
     */
    public enum Category {
        N(SoundEvent.NATURAL),
        M(SoundEvent.MEDICAL),
        I(SoundEvent.INDUSTRIAL),
        U(SoundEvent.NUCLEAR),
        P(SoundEvent.PROMPT_GAMMA);

        /**
         * The sound for voice category
         */
        private SoundEvent mSound;

        Category(SoundEvent sound) {
            mSound = sound;
        }

        public SoundEvent getSound() {
            return mSound;
        }
    }


    /**
     * The state of the nuclide.
     */
    public enum State {
        //CALC: признак отбора нуклида: VALID (прошел условие), INVALID (не прошел условие)
        //UNIDENTIFIED (не идентифицирован), UNKNOWN (под вопросом), IDENTIFIED (идентифицирован)

        /**
         * The nuclide passed condition
         */
        VALID,

        /**
         * The nuclide failed condition
         */
        INVALID,

        /**
         * The nuclide is unidentified
         */
        UNIDENTIFIED,

        /**
         * The nuclide is under question
         */
        UNKNOWN,

        /**
         * The nuclide is identified
         */
        IDENTIFIED
    }
}
