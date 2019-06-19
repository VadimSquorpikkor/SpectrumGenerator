package com.atomtex.spectrumgenerator.domain;


import android.support.annotation.NonNull;

/**
 * Represents an energy line of a {@link Nuclide}.
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class EnergyLine {

    /**
     * The energy line of a nuclide (kev)
     */
    private float mEnergy;          //энергетическая линия нуклида в килоэлектронвольтах(кэВ)

    /**
     * The value relative height of the peak of the convolution of the energy line without protection
     */
    private int mFactorsNoShield;   //параметр относительной высоты пика свертки энергетической линии без защиты

    /**
     * The value relative height of the peak of the convolution of the energy line with protection
     */
    private int mFactorsShield;     //параметр относительной высоты пика свертки энергетической линии за защитой

    /**
     * The quantum yield in fractions * 32000
     */
    private int mQuantumYield;      //квантовый выход в долях * 32000

    /**
     * The correction factor in fractions * 128
     * <p>
     * if value <code>0</code> using as reference for calculation of protection thickness
     */
    private int mCorrection;        //коэффициент коррекции в долях * 128, если =0 использование в качестве опорной для расчета толщины защиты

    /**
     * Radionuclide activity along the energy line
     */
    private float mActivity;        //CALC: активность радионуклида по энергетической линии

    /**
     * Statistical error of radionuclide activity on the energy line
     */
    private float mActivityError;     //CALC: статистическая ошибка активности радионуклида по энергетической линии

    /**
     * The proximity of the peak to the energy line
     * <p>
     * the smaller the value modulo the closer the peak to the energy line.
     */
    private int mCloseness;       //CALC: близость пика к энергетической линии, чем меньше значение по модулю тем ближе пик к э.линии

    /**
     * The indices of the peaks corresponding to lines of energy
     * <p>
     * if value <code>255</code> than there is no  matching.
     */
    private int mIndex;           //CALC: индексы пиков соответствующие энергетическим линиям, =255 значит, что соответствия нет.

    public EnergyLine() {

    }

    public EnergyLine(float energies, int factorsNoShield, int factorsShield,
                      int quantumYield, int correction) {
        mEnergy = energies;
        mFactorsNoShield = factorsNoShield;
        mFactorsShield = factorsShield;
        mQuantumYield = quantumYield;
        mCorrection = correction;
    }

    public float getEnergy() {
        return mEnergy;
    }

    public void setEnergy(float energy) {
        mEnergy = energy;
    }

    public int getFactorsNoShield() {
        return mFactorsNoShield;
    }

    public void setFactorsNoShield(int factorsNoShield) {
        mFactorsNoShield = factorsNoShield;
    }

    public int getFactorsShield() {
        return mFactorsShield;
    }

    public void setFactorsShield(int factorsShield) {
        mFactorsShield = factorsShield;
    }

    public int getQuantumYield() {
        return mQuantumYield;
    }

    public void setQuantumYield(int quantumYield) {
        mQuantumYield = quantumYield;
    }

    public int getCorrection() {
        return mCorrection;
    }

    public void setCorrection(int correction) {
        mCorrection = correction;
    }

    public float getActivity() {
        return mActivity;
    }

    public void setActivity(float activity) {
        mActivity = activity;
    }

    public float getActivityError() {
        return mActivityError;
    }

    public void setActivityError(float activityError) {
        mActivityError = activityError;
    }

    public int getCloseness() {
        return mCloseness;
    }

    public void setCloseness(int closeness) {
        mCloseness = closeness;
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnergyLine)) return false;

        EnergyLine that = (EnergyLine) o;

        if (Float.compare(that.mEnergy, mEnergy) != 0) return false;
        if (mFactorsNoShield != that.mFactorsNoShield) return false;
        if (mFactorsShield != that.mFactorsShield) return false;
        if (mQuantumYield != that.mQuantumYield) return false;
        if (mCorrection != that.mCorrection) return false;
        if (Float.compare(that.mActivity, mActivity) != 0) return false;
        if (Float.compare(that.mActivityError, mActivityError) != 0) return false;
        if (mCloseness != that.mCloseness) return false;
        return mIndex == that.mIndex;
    }

    @Override
    public int hashCode() {
        int result = (mEnergy != +0.0f ? Float.floatToIntBits(mEnergy) : 0);
        result = 31 * result + mFactorsNoShield;
        result = 31 * result + mFactorsShield;
        result = 31 * result + mQuantumYield;
        result = 31 * result + mCorrection;
        result = 31 * result + (mActivity != +0.0f ? Float.floatToIntBits(mActivity) : 0);
        result = 31 * result + (mActivityError != +0.0f ? Float.floatToIntBits(mActivityError) : 0);
        result = 31 * result + mCloseness;
        result = 31 * result + mIndex;
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "EnergyLine{" +
                "mEnergies=" + mEnergy +
                ", mFactorsNoShield=" + mFactorsNoShield +
                ", mFactorsShield=" + mFactorsShield +
                ", mQuantumYield=" + mQuantumYield +
                ", mCorrection=" + mCorrection +
                ", mActivity=" + mActivity +
                ", mActivityError=" + mActivityError +
                ", mCloseness=" + mCloseness +
                ", mIndexes=" + mIndex +
                '}';
    }
}
