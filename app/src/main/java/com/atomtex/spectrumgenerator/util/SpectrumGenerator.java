package com.atomtex.spectrumgenerator.util;

import android.util.Log;

import static java.lang.Math.abs;
import static java.lang.Math.random;

public class SpectrumGenerator {
    private static final int RAND_MAX = 32767;
    private static final int MAX_CHANNELS = 1024;
    private int channels;
    //todo **************************************
    private long spectrum_for_rand[];
    private double spectrum[];




    public int[] generatedSpectrum(
            int[] pSpectrum,             //входной спектр
            int spectrumTime,             //время набора входного спектра всегда >0
            int requiredTime              //требуемое время к которому будет приведен выходной спектр, может быть =0, тогда ко времени     spectrumTime
    ) {
        int[] pRetSpectrum = new int[pSpectrum.length];
        channels = pSpectrum.length; //сюда надо как то получить количество элементов в pSpectrum
        double dbl;
        if (requiredTime == 0)
            requiredTime = spectrumTime; //todo

        Log.e("TAGGG", "generatedSpectrum: required = " + requiredTime + "; spectrum = " + spectrumTime);

        double timecoef = (double) requiredTime / (double) spectrumTime;
        int f;
        int tmp;
        for (int i = 0; i < channels; i++) {
            tmp = pSpectrum[i];
            /*dbl = pSpectrum[i] * timecoef;                 //приводим каждый эелемент ко времени  //todo что за spectrum????
            if (dbl < 0) {
                f = - (poissonRandomGenerator(abs(dbl)));
            } else
                f = (poissonRandomGenerator(dbl));*/
            f = poissonRandomGenerator(pSpectrum[i] * timecoef);
            pRetSpectrum[i] = (f);                                   //сохраняем в результат
            Log.e("TAGGG", "generatedSpectrum: OLD = " + tmp + "; NEW = " + f);
        }
        return pRetSpectrum;
    }

    /** old one*/
    public float[] generatedSpectrum(
            float[] pSpectrum,             //входной спектр
            int spectrumTime,             //время набора входного спектра всегда >0
            int requiredTime              //требуемое время к которому будет приведен выходной спектр, может быть =0, тогда ко времени     spectrumTime
            //float[] pRetSpectrum           //сюда сохраним выходной спектр
    ) {
        float[] pRetSpectrum = new float[MAX_CHANNELS];  //todo какой размер??? max_chan??
        channels = pSpectrum.length; //сюда надо как то получить количество элементов в pSpectrum
        if (channels > MAX_CHANNELS)  //MAX_CHANNELS - это константа говорящая сколько элементов мы поддерживаем
        {//количество каналов за пределами
            return null;    //unexpected size of spectrum  //todo
        }
        double dbl;

        if (requiredTime == 0)
            requiredTime = spectrumTime; //todo

        double timecoef = (double) requiredTime / (double) spectrumTime;
        float f;
        for (int i = 0; i < channels; i++) {
            dbl = (double) pSpectrum[i] * timecoef;                 //приводим каждый эелемент ко времени  //todo что за spectrum????
            if (dbl < 0) {
                f = -(float) (poissonRandomGenerator(abs(dbl)));
            } else
                f = (float) (poissonRandomGenerator(dbl));
            pRetSpectrum[i] = f;                                   //сохраняем в результат
        }

        return pRetSpectrum;
    }

    private double specRandom() {
        return random() * 32767 / (double) RAND_MAX;    //rand - это библиотечная функция генерации случайного числа в диапазоне 0-32767, RAND_MAX=32767
    }

    //ret -1 if error calculation
    //генератор случайных чисел Пуасоновского распределения
    private int poissonRandomGenerator(double Lambda) {
        int X;
        double S, A, FRand;
        // Инициализация переменных
        X = 0;
        A = -Lambda;
        if (A == 0.0) return 0;
        FRand = specRandom();
        if (FRand > 0) S = Math.log(FRand);  //натуральный логарифм
        else S = 0;
        // Цикл вычисления произведения
        while (S >= A) {
            X++;
            FRand = specRandom();
            if (FRand > 0) S = S + Math.log(FRand);
        }
        return (X);
    }

    private long generateValue(double fMiddle) {  //todo что за STDMETHODIMP??
        return (long) poissonRandomGenerator(fMiddle);
    }


    // get random channel number according to distribution as in "spectrum_for_rand"
//ret -1 if can not randomize because of no counts in spectrum_for_rand
//every get will decrease number of counts of spectrum_for_rand in generated channel
    private int randBySpectrumDistribution() {
        //calc total counts
        long totalcount_for_rand = 0;
        for (int i = 0; i < channels; i++) {
            totalcount_for_rand += spectrum_for_rand[i];
        }
        if (totalcount_for_rand < 1) return -1;
        long rnd = (long) ((double) (totalcount_for_rand) * (Math.random() + 1) / (double) (RAND_MAX + 1));
        long count = 0;
        int j = -1;    //number of last channel with non zero value
        for (int i = 0; i < channels; i++) {
            if (spectrum_for_rand[i] == 0) continue;
            count += spectrum_for_rand[i];
            if (count > rnd) {
                if (j == -1)
                    j = i;
                spectrum_for_rand[j]--;    //decrease number of pulses
                return j;
            }
            j = i;
        }
        if (j == -1)
            j = channels - 1;
        if (spectrum_for_rand[j] > 0)
            spectrum_for_rand[j]--;    //decrease number of pulses
        return j;
    }

    // return t, randomized by exponential distribution
    private double getExpRnd(double lambda){
        double rnd;
        do rnd = Math.random() / (double) RAND_MAX;
        while (rnd==0);
        return -1.0 / lambda * Math.log(1.0 - rnd);
    }
}