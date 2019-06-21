package com.atomtex.spectrumgenerator.domain;


import com.atomtex.spectrumgenerator.domain.Nuclide.State;
import com.atomtex.spectrumgenerator.exception.ProcessException;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

import static com.atomtex.spectrumgenerator.domain.Nuclide.State.IDENTIFIED;
import static com.atomtex.spectrumgenerator.domain.Nuclide.State.INVALID;
import static com.atomtex.spectrumgenerator.domain.Nuclide.State.UNIDENTIFIED;
import static com.atomtex.spectrumgenerator.domain.Nuclide.State.UNKNOWN;
import static com.atomtex.spectrumgenerator.domain.Nuclide.State.VALID;
import static com.atomtex.spectrumgenerator.util.Constant.GSHPSIGM;
import static com.atomtex.spectrumgenerator.util.Constant.SEARCHSHAPE;
import static com.atomtex.spectrumgenerator.util.Constant.SRCHGSHPNUM;

/**
 * This class was designed to make identification of nuclides.
 *
 * @author stanislav.kleinikov@gmail.com
 */
public final class NucIdent {

    public static final int BAD_INDEX = 255;
    private static final int SIGMA_THIN_FACTOR_M = 1;
    private static final int SIGMA_THIN_FACTOR_D = 1;
    private static final int ENERGY_THRESHOLD = 250;
    private static final int MAX_LINE_NUM = 32;
    private static final float SMOOTH_DEGREE = 2;
    private static final int K_ENERGY = 1461;

    private int channelNumber;
    private float middleCPSonBKG;
    private int[] spectrum;
    private int timeAccSpec;
    private int astrTimeAccSpec;
    private float[] spectrumSigma;
    private float[] spectrumEnergy;
    private int[] convolution;
    private int[] convolutionDisp;
    private int sigmaSearchPeaks;
    private int sigmaPeakCheck;
    private int sigmaPeakCheckLeft;
    private int sigmaPeakCheckRight;
    private int[] peakExtremum;
    private int maxEnergy;
    private int minEnergy;
    private int scaleInstability;
    private boolean testNullPeak;
    private int leftBorder;
    private int chanK;
    private int chanB;
    private float[] niChannels;
    private int[] deltas;
    private int[] energies;
    private int[] exEnergies;
    private float[] efficiency;
    private float niCr;
    private int uranium;
    private float fltAverBgndCPS;
    private static List<Nuclide> nuclides;

    /**
     * The number of the peaks over the spectrum
     */
    private int nLine;


    /**
     * index to adjust the value of the Sigma spectrum.Gets the value '1' in the constructor.
     * Can be changed with {@link #setCorSigmaIndex(float)}
     */
    private float corSigmaIndex;

    //todo ненужный конструктор
    /*public NucIdent(int chanelNumber, float middleCPSonBKG, int sigmaSearchPeaks,
                    int sigmaPeakCheck, int sigmaPeakCheckLeft, int sigmaPeakCheckRight,
                    int minEnergy, int scaleInstability, float fltAverBgndCPS, boolean testNullPeak) {
        this.channelNumber = chanelNumber;
        this.middleCPSonBKG = middleCPSonBKG;
        convolution = new int[chanelNumber];
        convolutionDisp = new int[chanelNumber];
        this.sigmaSearchPeaks = sigmaSearchPeaks;
        this.sigmaPeakCheck = sigmaPeakCheck;
        this.sigmaPeakCheckLeft = sigmaPeakCheckLeft;
        this.sigmaPeakCheckRight = sigmaPeakCheckRight;
        peakExtremum = new int[chanelNumber];
        this.scaleInstability = scaleInstability;
        this.testNullPeak = testNullPeak;
        this.minEnergy = minEnergy;
        corSigmaIndex = 1;
        nLine = 0;
        niCr = 0;
        uranium = 0;
        niChannels = new float[MAX_LINE_NUM];
        deltas = new int[MAX_LINE_NUM];
        energies = new int[MAX_LINE_NUM];
        exEnergies = new int[MAX_LINE_NUM];
        efficiency = new float[chanelNumber];
        this.fltAverBgndCPS = fltAverBgndCPS;
    }*/

    //todo сделал конструктор без параметра middleCPSonBKG -- он мне не нужен, при этом тянет за собой ненужный класс Adapter
    public NucIdent(int chanelNumber, int sigmaSearchPeaks,
                    int sigmaPeakCheck, int sigmaPeakCheckLeft, int sigmaPeakCheckRight,
                    int minEnergy, int scaleInstability, float fltAverBgndCPS, boolean testNullPeak) {
        this.channelNumber = chanelNumber;
//        this.middleCPSonBKG = middleCPSonBKG;
        convolution = new int[chanelNumber];
        convolutionDisp = new int[chanelNumber];
        this.sigmaSearchPeaks = sigmaSearchPeaks;
        this.sigmaPeakCheck = sigmaPeakCheck;
        this.sigmaPeakCheckLeft = sigmaPeakCheckLeft;
        this.sigmaPeakCheckRight = sigmaPeakCheckRight;
        peakExtremum = new int[chanelNumber];
        this.scaleInstability = scaleInstability;
        this.testNullPeak = testNullPeak;
        this.minEnergy = minEnergy;
        corSigmaIndex = 1;
        nLine = 0;
        niCr = 0;
        uranium = 0;
        niChannels = new float[MAX_LINE_NUM];
        deltas = new int[MAX_LINE_NUM];
        energies = new int[MAX_LINE_NUM];
        exEnergies = new int[MAX_LINE_NUM];
        efficiency = new float[chanelNumber];
        this.fltAverBgndCPS = fltAverBgndCPS;
    }

    public int[] getSpectrum() {
        return spectrum;
    }

    public void setSpectrum(int[] spectrum) {
        this.spectrum = IntBuffer.allocate(channelNumber * 4 / 3).put(spectrum).array();
    }

    public int getTimeAccSpec() {
        return timeAccSpec;
    }

    public void setTimeAccSpec(int timeAccSpec) {
        this.timeAccSpec = timeAccSpec;
    }

    public int getAstrTimeAccSpec() {
        return astrTimeAccSpec;
    }

    public void setAstrTimeAccSpec(int astrTimeAccSpec) {
        this.astrTimeAccSpec = astrTimeAccSpec;
    }

    public int getChanelNumber() {
        return channelNumber;
    }

    public void setChanelNumber(int chanelNumber) {
        this.channelNumber = chanelNumber;
    }

    public float[] getSpectrumSigma() {
        return spectrumSigma;
    }

    public void setSpectrumSigma(float[] spectrumSigma) {
        for (int i = 0; i < spectrumSigma.length; i++) {
            spectrumSigma[i] = (((int) (spectrumSigma[i] * 256))
                    / SIGMA_THIN_FACTOR_M * SIGMA_THIN_FACTOR_D) / 256.0f;
            if (spectrumSigma[i] == 0) {
                spectrumSigma[i] = 0.01f;
            }
        }

        this.spectrumSigma = spectrumSigma;
    }

    public float[] getSpectrumEnergy() {
        return spectrumEnergy;
    }

    public void setSpectrumEnergy(float[] spectrumEnergy) {
        this.spectrumEnergy = spectrumEnergy;
    }

    public float getMiddleCPSonBKG() {
        return middleCPSonBKG;
    }

    public void setMiddleCPSonBKG(float middleCPSonBKG) {
        this.middleCPSonBKG = middleCPSonBKG;
    }

    public int[] getConvolution() {
        return convolution;
    }

    public void setConvolution(int[] convolution) {
        this.convolution = convolution;
    }

    public int[] getConvolutionDisp() {
        return convolutionDisp;
    }

    public void setConvolutionDisp(int[] convolutionDisp) {
        this.convolutionDisp = convolutionDisp;
    }

    public int getSigmaSearchPeaks() {
        return sigmaSearchPeaks;
    }

    public void setSigmaSearchPeaks(int sigmaSearchPeaks) {
        this.sigmaSearchPeaks = sigmaSearchPeaks;
    }

    public int getSigmaPeakCheck() {
        return sigmaPeakCheck;
    }

    public int getSigmaPeakCheckLeft() {
        return sigmaPeakCheckLeft;
    }

    public void setSigmaPeakCheckLeft(int sigmaPeakcheckLeft) {
        this.sigmaPeakCheckLeft = sigmaPeakcheckLeft;
    }

    public int getSigmaPeakCheckRight() {
        return sigmaPeakCheckRight;
    }

    public void setSigmaPeakCheckRight(int sigmaPeakcheckRight) {
        this.sigmaPeakCheckRight = sigmaPeakcheckRight;
    }

    public void setSigmaPeakCheck(int sigmaPeakCheck) {
        this.sigmaPeakCheck = sigmaPeakCheck;
    }

    public int getScaleInstability() {
        return scaleInstability;
    }

    public void setScaleInstability(int scaleInstability) {
        this.scaleInstability = scaleInstability;
    }

    public float getCorSigmaIndex() {
        return corSigmaIndex;
    }

    public void setCorSigmaIndex(float corSigmaIndex) {
        this.corSigmaIndex = corSigmaIndex;
    }

    public int[] getPeakExtremum() {
        return peakExtremum;
    }

    public void setPeakExtremum(int[] peakExtremum) {
        this.peakExtremum = peakExtremum;
    }

    public float[] getNiChannels() {
        return niChannels;
    }

    public int[] getEnergies() {
        return energies;
    }

    public int[] getExEnergies() {
        return exEnergies;
    }

    public int getnLine() {
        return nLine;
    }

    public static List<Nuclide> getNuclides() {
        return nuclides;
    }

    public static void setNuclides(List<Nuclide> nuclides) {
        NucIdent.nuclides = nuclides;
    }

    public void detectLines(int threshold) throws ProcessException {
        if (nuclides.size() == 0) {
            return;
        }
        calcMaxEnergy();
        calcEnergyCoef();
        leftBorder = channelFromEnergy(minEnergy);
        applyFilter(); //filters spectrum  and places result in the convolution array and convolutionDisp array
        smoothing(); //smooths convolution for nicer look (averaging filter with window equal to sigma)
        analyzePeaks(threshold);//Fills Peak array with data, basing on the analysis of convolution filtered spectrum
    }

    public int[] getPeaksPositions() {

        int stabPosition = channelFromEnergy(K_ENERGY);
        int leftBorder = (int) (stabPosition * 0.5);
        int rightBorder = (int) (stabPosition * 1.3);
        int currentPosition = -1;

        for (int i = leftBorder; i < rightBorder; i++) {
            if (peakExtremum[i] == 1) {
                if (Math.abs(i - stabPosition) < Math.abs(currentPosition - stabPosition)) {
                    currentPosition = i;
                }
            }
        }
        if (currentPosition == -1) {
            return null;
        } else {
            return new int[]{currentPosition, stabPosition};
        }
    }

    private void applyFilter() throws ProcessException {
        int i1;
        int n, dn;
        int d, index;
        long rr1, rr2, k;


        for (int i = 0; i < channelNumber; i++) {
            i1 = (sigmaSearchPeaks * (int) (spectrumSigma[i] * 256) + 128) / 1024;
            index = i1;
            i1 = i - index;
            if (i1 < 0) {
                convolution[i] = 0;
                convolutionDisp[i] = 1;
                continue;
            }

            dn = (GSHPSIGM * 256 / ((int) (spectrumSigma[i] * 256)));
            n = index;
            n *= dn;
            dn = -dn;
            rr1 = 0;
            rr2 = 0;
            index = index * 2 + 1;
            while (index != 0) {
                if (n >= SRCHGSHPNUM) {
                    throw new ProcessException("sigma");
                }
                d = SEARCHSHAPE[n];
                k = spectrum[i1++] * d;
                rr1 += k;
                k *= d;
                rr2 += k;

                index--;
                n += dn;
                if (n == 0) {
                    dn = -dn;
                }
            }
            if (rr2 >= 0) {
                convolution[i] = (int) (-rr1 / 256);
                convolutionDisp[i] = (int) (rr2 / 65536 + 1);
            } else {
                convolution[i] = 0;
                convolutionDisp[i] = 1;
            }
        }
    }

    private void smoothing() {
        int[] temp = new int[convolution.length];
        smoothingEx(temp, SMOOTH_DEGREE);
        convolution = Arrays.copyOf(temp, temp.length);
    }

    private void smoothingEx(int[] temp, float sigmas) {

        int buf, x1 = 0, x2 = 0;
        int i1, i2, j = -1, i02 = -1;
        int sigma;
        boolean noSum = false;
        buf = 0;
        for (int i = 0; i < channelNumber; i++) {

            sigma = (int) (spectrumSigma[i] / sigmas);
            if (sigma == 0)
                sigma = 1;
            i1 = i - sigma;
            i2 = i + sigma;
            if (i1 < 0) {
                temp[i] = 0;
                continue;
            }// i1=0;
            if (i2 >= channelNumber) {
                i2 = channelNumber - 1;
                noSum = true;
            }
            if (j < 0) {//первое суммирование
                for (j = i1; j <= i2; j++)
                    buf += convolution[j];
            } else {
                if (i1 > j) {
                    buf -= x1;
                    j++;
                    if (i1 > j)
                        buf -= x2;
                }

                if (i2 > i02 && !noSum) {
                    buf += convolution[i2];
                    i02++;
                    if (i2 > i02)
                        buf += convolution[i2 - 1];
                }
            }
            i02 = i2;
            j = i1;
            x2 = x1;
            x1 = convolution[j];

            temp[i] = buf / (i2 - i1 + 1);
        }
    }

    private void analyzePeaks(int threshold) {
        int i, en;
        int bbegin = 0, bcenter = 0;
        int bmean1 = 0, bmean2 = 0;
        int pmean;

        int mean1 = convolution[0];
        int mean2;
        int porog;
        peakExtremum[0] = 0;
        for (i = 1; i < channelNumber; i++) {
            peakExtremum[i] = 0;

            mean2 = convolution[i];
            porog = mean2 - mean1;
            mean1 = mean2;

            if (porog < 0) {
                bmean1 = -1;
            } else if (porog > 0) {
                bmean1 = 1;
            } else if (i < channelNumber - 1) {
                continue;
            }

            pmean = bmean1 - bmean2;
            bmean2 = bmean1;
            if (bbegin == 0) {
                if (pmean >= 1) {//впадина
                    bbegin = i - 1;
                }
            } else if (bcenter == 0) {
                if (pmean == -2) {//вершина
                    bcenter = i - 1;
                }
            } else {
                en = i - bcenter;
                if ((pmean == 2 || (pmean == 0 && i == channelNumber - 1)) && (en > 1))//впадина
                {
                    if (en > 2) {
                        mean2 = convolution[bcenter];
                        porog = (int) (Math.sqrt((float) convolutionDisp[bcenter]) * threshold);
                        en = (int) spectrumEnergy[bcenter];
                        if (en <= maxEnergy && mean2 > (en > ENERGY_THRESHOLD ? porog : porog * 2)) {
                            porog /= 2;
                            if (mean2 - convolution[bbegin] > porog &&
                                    mean2 - convolution[i - 1] > porog) {
                                if (checkPeak(bbegin, bcenter, i - 1)) {
                                    peakExtremum[bcenter] = 1;
                                }
                            }
                        }
                        bcenter = 0;
                        bbegin = i - 1;
                    } else if (spectrumEnergy[bcenter] < ENERGY_THRESHOLD) {//условие введено для того чтобы широкие высокоэнергетичные пики все же обрабатывались
                        //но так как это условие сброса пика было, то оно видимо нужно, не помню в каких ситуациях
                        bcenter = 0;
                        bbegin = i - 1;
                    }
                }
            }
        }
    }

    private boolean checkPeak(int jm, int j, int i) {
        // выполняет проверку формы пика на соответствие пику полного поглощения
        // проверка осуществляется по свертке
        // jm - номер канала левого локального минимума
        // j номер канала локального максимума,
        // i - номер канала правого локального минимума

        boolean c = false;
        int cc, w, w1, dl, dr;
        float r1;
        w = (int) (spectrumSigma[j] * 256);
        cc = (w * sigmaPeakCheck * SIGMA_THIN_FACTOR_M / SIGMA_THIN_FACTOR_D / 10 + 128) >> 7;

        if (spectrumEnergy[j] < ENERGY_THRESHOLD) {
            dr = cc & 1;
            cc >>= 1;
            cc += dr;
        }
        if (cc == 0) {
            cc = 1;
        }
        dl = (sigmaPeakCheckLeft * SIGMA_THIN_FACTOR_M
                / SIGMA_THIN_FACTOR_D * w / 10 + 128) >> 8;

        dr = (sigmaPeakCheckRight * SIGMA_THIN_FACTOR_M
                / SIGMA_THIN_FACTOR_D * w / 10 + 128) >> 8;

        w = j - jm;
        w1 = i - j;
        if (Math.abs(dr - w1) < cc) { // проверка по расстоянию локальных минимумов от максимума
            if (Math.abs(dl - w) < cc) {
                //19/02/2008 для пиков ниже порога оба минимума должны быть <=0
                if (((spectrumEnergy[j] >= ENERGY_THRESHOLD) &&
                        (convolution[jm] <= 0 || convolution[i] <= 0)) ||
                        (convolution[jm] <= 0 && convolution[i] <= 0))
                    c = true;
            } else {
                jm = j - dl;
                if (jm < 0)
                    jm = 0;
                c = convolution[jm] <= 0;
            }
        }
        if (c) {//дополнительная проверка
            if (j < leftBorder) {//проверка что это на самом деле пик для крайних левых каналов
                if (spectrum[j] != 0) {
                    r1 = spectrum[j] - spectrum[j + dr];
                    if (r1 > 0) {
                        r1 *= r1;
                        r1 /= spectrum[j] + 1;
                        if (r1 < 25) {
                            c = false;
                        }
                    } else {
                        c = false;
                    }
                }
            } else if (testNullPeak) {//для остальных пиков проверка на ненулевой интеграл
                dl >>= 1;
                if (dl == 0)
                    dl = 1;
                dr >>= 2;
                if (dr == 0)
                    dr = 1;
                jm = j - dl;
                i = j + dr;
                if (i >= channelNumber)
                    i = channelNumber - 1;
                for (w = jm; w <= i; w++) {
                    r1 = (float) spectrum[w];
                    if (r1 <= 0) {
                        c = false;
                        break;
                    }
                }
            }
        }
        return c;
    }

    private int channelFromEnergy(int energy) {
        int k = (chanK * energy + chanB) >> 8;
        if (k < 0) k = 0;
        else if (k >= channelNumber) k = channelNumber - 1;
        if (spectrumEnergy[k] > energy) {
            while (spectrumEnergy[k] > energy) {
                if (k > 0)
                    k--;
                else
                    break;
            }
            if (Math.abs(spectrumEnergy[k + 1] - energy) < Math.abs(energy - spectrumEnergy[k]))
                k++;
        } else {
            while (spectrumEnergy[k] < energy) {
                if (k < channelNumber - 1)
                    k++;
                else
                    break;
            }
            if (Math.abs(energy - spectrumEnergy[k - 1]) <= Math.abs(spectrumEnergy[k] - energy))
                k--;
        }
        return k;
    }

    private void calcMaxEnergy() {
        int max = channelNumber - (int) spectrumSigma[channelNumber - 1];
        if (max >= channelNumber) {
            max = channelNumber - 1;
        }
        maxEnergy = (int) spectrumEnergy[max];
    }

    private void calcEnergyCoef() {
        int i, j;
        i = channelNumber / 2 - 50;
        j = (int) (spectrumEnergy[i + 100] - spectrumEnergy[i]);
        if (j == 0) {
            return;
        }
        chanK = 25600 / j;
        chanB = i * 256 - (int) (chanK * spectrumEnergy[i]);
    }

    private int calcDeltaEnergyFromChannel(int channel) {
        int en = (int) spectrumEnergy[channel];
        int ss = 6;
        //!!!!!!!!	int s=(((en>320 && en<390)?scaleInstability*1.5:scaleInstability)*en+500)/1000);
        int s = (scaleInstability * en + 500) / 1000;
        if (s < ss) {
            s = ((en < 34) ? (ss - ss / 3) : (ss));
        }
        return s;
    }


    private int checkChannel(int channel) {//searches position of maximum near given channel (l+-SCALEINSTABILITY*l)
        int l = channelFromEnergy(channel);
        int di = calcDeltaEnergyFromChannel(l);
        int s;
        //энергия справа
        int e = channel + di;
        if (e < maxEnergy) {
            //разница в каналах между заданной позицией и
            //позицией энергии справа
            s = channelFromEnergy(e) - l;
        } else {
            //если позиция справа более максимальной энергии то
            //перерасчитываем позицию справа как позицию слева
            e = channel - di;
            //разница в каналах между заданной позицией и
            //позицией энергии слева
            s = l - channelFromEnergy(e);
        }
        //s на выходе есть разница в каналах между заданной пизицией и
        //смещением по нестабильности
        di = (convolution[l] > convolution[l + 1]) ? -1 : 1;

        if (di > 0) {
            s = l + s < channel ? s : channel - l - 1;
        } else {
            s = l - s > 0 ? s : l;
        }

        while (s > 0) {
            if (convolution[l] > convolution[l + di]) {
                break;
            }
            l += di;
            s--;
        }
        return l;
    }

    //поиск энергетических центров найденных пиков по средневзвешенному свертки
    //расчет энергетических окон линий, т.н. нестабильности
    //подсчет количества найденных линий
    private void getRightEnergy() {
        int chanel;
        int b, b1;
        int chanel2;
        long lnVal, lnVal2;
        float temp, chantempd, fltVal;
        for (chanel = 0; chanel < channelNumber; chanel++) { //calculate number of detected lines
            if (peakExtremum[chanel] > 0) {
                lnVal = convolution[chanel] / 2;
                temp = 0;
                chantempd = 0;
                chanel2 = chanel;
                b1 = (int) ((spectrumSigma[chanel] + 0.5f) * 2);
                b = b1;

                do {
                    lnVal2 = convolution[chanel2];
                    if (lnVal2 < 0) break;
                    fltVal = (float) lnVal2;
                    fltVal *= fltVal;
                    temp += fltVal;
                    chantempd += fltVal * chanel2;
                    chanel2--;
                } while (lnVal2 > lnVal && chanel2 >= 0 && --b > 0);
                chanel2 = chanel + 1;
                do {
                    lnVal2 = convolution[chanel2];
                    if (lnVal2 < 0) break;
                    fltVal = (float) lnVal2;
                    fltVal *= fltVal;
                    temp += fltVal;
                    chantempd += fltVal * chanel2;
                    chanel2++;
                } while (lnVal2 > lnVal && chanel2 < channelNumber && --b1 > 0);

                chantempd = chantempd / temp;
                niChannels[nLine] = chantempd;    //номера каналов в плав виде
                deltas[nLine] = calcDeltaEnergyFromChannel((int) (chantempd + 0.5f)); //энергетическое окно линии
                energies[nLine] = (int) (getEnergyFromChannel(spectrumEnergy, chantempd) + 0.5f);//энергия линии в плав виде
                exEnergies[nLine] = 1;    //линия найдена
                nLine++;

                if (nLine == MAX_LINE_NUM) {
                    break;
                } //maximum maxNumLines lines in spectrum
            }
        }
    }

    //начальная инициализация
    //расчет весов линий, в зависимости от их близости к библиотечным
    //отсев линий по нестабильности
    //в формировании участвуют только не нулевые линии
    private void nucInit(Nuclide nuclide) {
        int lnVal2;
        int lnVal;
        int btInd;
        int btInd2;
        int btStored = 0;
        EnergyLine[] energyLines = nuclide.getEnergyLines();

        nuclide.setState(UNKNOWN); //clear unselect all nuclides
        nuclide.setWeight(0);
        nuclide.setWeightM(0);
        for (btInd = 0; btInd < nuclide.getLinesNum(); btInd++) {
            energyLines[btInd].setCloseness(0);
            energyLines[btInd].setActivity(0);
            energyLines[btInd].setActivityError(0);

            if ((energyLines[btInd].getEnergy() > maxEnergy) ||
                    (energyLines[btInd].getEnergy() < minEnergy)) { //remove all nuclide lines for energy greater than "maxEnergy" or less than "minEnergy"
                energyLines[btInd].setFactorsNoShield(0);
                energyLines[btInd].setFactorsShield(0);
            }
            //поиск ближайшей найденной линии к данной библиотечной
            lnVal2 = 0xFFFFFFF;
            energyLines[btInd].setIndex(BAD_INDEX);

            for (btInd2 = 0; btInd2 < nLine; btInd2++) {
                lnVal = Math.abs((int) (energyLines[btInd].getEnergy() - energies[btInd2]));
                lnVal = (lnVal << 8) / deltas[btInd2];
                if (lnVal < lnVal2) {
                    lnVal2 = lnVal;
                    btStored = btInd2;
                }
            }
            if (lnVal2 <= 256) //(lnVal<(2*256), that is difference does not exceed two sigma) mozhno vinesti v parametri
            {//линия подходит по условию нестабильности
                //менее двух энергетических окон для данной найденной линии
                energyLines[btInd].setCloseness(lnVal2);
                //сравнение с уже обработанными линиями соотнесенными с одними и теми же найденными линиями
                //предполагается не более двух библиотечных линий соотнесенных с одной найденной
                //библиотечная линия которая дальше текущей обрабатываемой отсеивается
                //иначе текущая отсеивается
                for (btInd2 = 0; btInd2 < btInd; btInd2++) {

                    if (energyLines[btInd2].getIndex() == btStored) {
                        lnVal = energyLines[btInd2].getCloseness();
                        if (lnVal < lnVal2)
                            btStored = BAD_INDEX; //there is a line, which is closer than that line
                        else
                            energyLines[btInd2].setIndex(BAD_INDEX);//that line is closer than already found line, so discard the previous line
                        break;
                    }
                }
                nuclide.getEnergyLines()[btInd].setIndex(btStored);
            }
        }
    }

    public int makeNuclideIdentification(int threshold) {
        boolean b1;
        int chan, chan2;
        int btFlag = 0, btInd, btInd2, btStored, btNucl;
        for (btInd = 0; btInd < MAX_LINE_NUM; btInd++) {
            exEnergies[btInd] = 0;
            energies[btInd] = 0;
            niChannels[btInd] = 0;
        }
        threshold <<= 8;
        getRightEnergy();

        //noinspection ConstantConditions
        do {
            if (nLine == 0) { //if no lines
                for (btNucl = 0; btNucl < nuclides.size(); btNucl++) {
                    nuclides.get(btNucl).setState(UNIDENTIFIED);
                }
                break;
            } else if (nuclides.size() == 0) {
                break;    //no nuclides in library, but there are detected lines.
            }
            //Initialize
            for (btNucl = 0; btNucl < nuclides.size(); btNucl++) {
                nucInit(nuclides.get(btNucl));
                nuclides.get(btNucl).setConfidence(0);    //clear confidence
            }

            //поиск и отсев нуклидов чьи все строгие линии принадлежат идентифицированным нуклидам
            //поиск и отсев нуклидов чьи строгие и не строгие линии просто не присутствуют на спектре
            b1 = true;
            while (true) {
                //Search and destroy nuclides, all whose lines belongs to identified nuclides
                //поиск и отсев нуклидов чьи все строгие линии принадлежат идентифицированным нуклидам
                for (btNucl = 0; btNucl < nuclides.size(); btNucl++) {
                    if (nuclides.get(btNucl).getState() == UNKNOWN) {//смотрим нуклиды под вопросом
                        testNoStrongLines(nuclides.get(btNucl));
                    }
                }
                if (!b1) {
                    break;//выход из цикла, все линии рассмотрены
                }
                b1 = false;
                //поиск и отсев нуклидов чьи строгие линии просто не присутствуют на спектре
                //Search and destroy nuclides, which should have strong lines in the spectrum,
                //but whose lines are not present in the spectrum
                for (btNucl = 0; btNucl < nuclides.size(); btNucl++) {
                    if (nuclides.get(btNucl).getState() == UNKNOWN) {
                        //очень опасно выходить из цикла, сразу свободные линии занимаются
                        //другими нуклидами, которые еще не прошли отсев по высотам,
                        //получается некая лотерея, повезло последним в списке проверяемых
                        //это уже исправлено
                        b1 = testForLostPeak(nuclides.get(btNucl), threshold);
                    }
                }
		/*
		сделать нуклид идентифицируемым если он имеет
		определенную строгую линию как уникальную
		среди линий нулидов под вопросом.
		Это нужно чтобы находить нуклиды по линиям, которые остаются
		после процедуры исключения линий найденных нуклидов
		*/
		/*
		тут есть явный недостаток:
		идет перебор линий с низко до высоко энергетических
		по первой уникальной определяем нуклид, который забирает линии других нуклидов или который
		имел бы меньший вес по сравнению с другим нуклидом у которого есть тоже уникальная линия,
		но другая, которая следует после этой по списку.
		*/
                //Identify nuclides, which has unique lines

                for (btInd = 0; btInd < nLine; btInd++) {
                    boolean skip = false;

                    if (exEnergies[btInd] == 0) {//already busy
                        continue;
                    }
                    btStored = 255;
                    l1:
                    for (btNucl = 0; btNucl < nuclides.size(); btNucl++) {
                        if (nuclides.get(btNucl).getState() == UNKNOWN ||
                                nuclides.get(btNucl).getState() == INVALID) {   //сравнение идет как по вопросным так и по вопросным не готовым по строгим линиям
                            //поиск только в линиях спорных нуклидов

                            for (btInd2 = 0; btInd2 < nuclides.get(btNucl).getLinesNum(); btInd2++) {
                                if (nuclides.get(btNucl).getEnergyLines()[btInd2].getIndex()
                                        == btInd) {
                                    if (btStored != 255) {
								/*
								линия уже занята
								переходим на следующую
								*/
                                        skip = true;
                                        break l1;
                                    }
                                    btStored = btNucl;
                                    btFlag = btInd2;
                                    break;
                                }
                            }
                        }
                    }
                    if (skip) {
                        continue;
                    }
                    if (btStored != 255) { //for that line only one nuclide is found
                        Nuclide nuclide = nuclides.get(btStored);
                        if (nuclide.getEnergyLines()[btFlag].getFactorsNoShield() != 0
                                && nuclide.getEnergyLines()[btFlag].getFactorsShield() > 1
                                && nuclide.getState() != INVALID)    //!!!!!!!отсеиваем нуклиды которые пока не готовы быть выбранными по одной строгой линии
                        { //line is detective
                            //проверка на то что у нуклида есть еще строгие линии которые должны были быть найдены
                            btStored = 0;
                            btNucl = 0;
                            for (btInd2 = 0; btInd2 < nuclide.getLinesNum(); btInd2++) {
                                EnergyLine line = nuclide.getEnergyLines()[btInd2];
                                if (btFlag != btInd2 && line.getFactorsShield() != 0) {
                                    if (line.getFactorsShield() > 1) {
                                        btStored++;
                                    }    //have strong line, count it
                                    if (line.getIndex() != BAD_INDEX && exEnergies[line.getIndex()] != 0) {
                                        btNucl++;
                                    }  //have this line as free, count it
                                }
                            }
                            if (btStored == 0 || btNucl > 0)//!!!!!!!!!!!!!
                            {//have strong lines and free lines
                                //строгая линия
                                nuclide.setState(IDENTIFIED);
                                b1 = true;
                                //исключение линий нуклида по правилам отсева линий
                                //подходящих по калибровочным высотам, но не выше в 2,5 раза
                                //калибровочных высот, ниже пожалуйста
                                excludeLines(nuclide);
                            }
                        }
                    }
                }


		/*
		розыгрыш по весам нуклидов в зависимости от количества линий и их значимости
		выигрывает тот нуклид у которого вес более 3 (т.е. две строгих, или одна строгая и две не строгих
		или нулевых и который имеет максимальный вес среди других
		причем хотябы одно сравнение должно было быть
		*/
                if (!b1) {
                    //идем по всем нуклидам подвопросным
                    for (btNucl = 0; btNucl < nuclides.size(); btNucl++) {
                        Nuclide nuclide = nuclides.get(btNucl);
                        if (nuclides.get(btNucl).getState() != UNKNOWN) {
                            continue;//!!!!!!!!!!!! выигрышными не могут быть нуклиды которые не прошли проверку на статистику в областях строгих линий если это было нужно
                        }
                        btFlag = 1;
                        //расчет веса текущего нуклида
                        chan = checkWeight(nuclide);
                        btStored = 0;

                        for (btInd2 = 0; btInd2 < nuclides.size(); btInd2++) {
                            Nuclide nuc = nuclides.get(btInd2);
                            if (nuc.getState() == UNKNOWN || nuc.getState() == INVALID)//!!!!!!!!!!!!!! сравнение со всеми нуклидами под вопросом
                            {
                                if (btInd2 != btNucl) {//нуклиды различны, т.е. не один и тот же
                                    if (!nuclsDifferOnly(nuc, nuclide)) {//если хотя бы одна строгая линия перекрывается
                                        btStored = 1;    //было сравнение
                                        chan2 = checkWeight(nuc);
                                        if (chan2 >= chan || nuc.getState() == INVALID) {
                                            //все! нуклид верхнего цикла так и не будет определен по этому условию
                                            //так как есть более строго подходящий нуклид
                                            btFlag = 0;
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        if (btFlag != 0 && chan > 3 && btStored != 0) {//нуклида верхнего цикла так и не побил никто
                            nuclide.setState(IDENTIFIED);
                            b1 = true;
                            excludeLines(nuclide);
                            //выход из верхнего цикла, для дальнейшей оценки следующих нуклидов
                            break;
                        }
                    }
                }

                //!!!!!!!!!!!!!
                //возвращаем нуклидам их select если они INVALID (которые не должны рассматриваться на строгие линии)

                for (Nuclide nuc : nuclides) {
                    if (nuc.getState() == INVALID) {
                        nuc.setState(UNKNOWN);
                    }
                }
            }

            //Now check that all detected lines correspondent to identified nuclides or nuclides
            //under question (identified lines)
            for (btInd = 0; btInd < nLine; btInd++) {
                exEnergies[btInd] = 0;
            }
        } while (false);

        btFlag = 0;    //уран еще не обработан

        for (btNucl = 0; btNucl < nuclides.size(); btNucl++) {
            Nuclide nuclide = nuclides.get(btNucl);
            State state = nuclide.getState();
            if (state == UNKNOWN || state == IDENTIFIED) {
                for (btInd = 0; btInd < nuclide.getLinesNum(); btInd++) {
                    EnergyLine line = nuclide.getEnergyLines()[btInd];
                    btInd2 = line.getIndex();
                    if (btInd2 != BAD_INDEX) {
                        exEnergies[btInd2] = 1;

                        if (state == IDENTIFIED) {//идентифицирован строго
                            calcNucActivity(nuclide, btInd, niChannels[btInd2]);
                        }

                        if (nuclide.getName().charAt(0) == 'U'
                                && (nuclide.getNumStr().charAt(2) == '5'
                                || nuclide.getNumStr().charAt(2) == '8') && btFlag == 0) {
                            calcUranium();
                            btFlag = 1;    //уран уже обработан
                        }
                        if (nuclide.getName().charAt(0) == 'B'
                                && nuclide.getName().charAt(1) == 'a'
                                && (nuclide.getNumStr().charAt(1) == '3'
                                && nuclide.getNumStr().charAt(2) == '3')) {
                            testForPuBa(nuclide);
                        }
                    }
                }

            } else if (nuclide.getLinesNum() == 1
                    && nuclide.getEnergyLines()[0].getEnergy() == 0) {//обработка стронция-90
                if ((nLine == 0 || (nLine == 1 && (Math.abs(energies[0] - 1461) < 28 || Math.abs(energies[0] - 78) < 6)) ||
                        (nLine == 2 && (Math.abs(energies[1] - 1461) < 28 && Math.abs(energies[0] - 78) < 6)))
                        && timeAccSpec > 30) {
                    niCr = 0;
                    for (chan = 0; chan < channelNumber; chan++) {
                        niCr += (float) spectrum[chan];
                    }
                    niCr /= (float) timeAccSpec;
                    if (niCr > fltAverBgndCPS * 4) {
                        nuclide.setState(IDENTIFIED);
                    }
                }
            }
        }


        calcConfidence(threshold);

        btInd = 0;
        for (btInd2 = 0; btInd2 < nLine; btInd2++) {
            btInd += exEnergies[btInd2]; //calculate number of identified lines
        }
        if (btInd < nLine) {
            return -1;
        } else if (nLine > 0) {
            return 1;
        } else {
            return 0;
        }
        //On output energies array contains energies of detected lines
        //ex_energy contain 0, if line does not have correspondent nuclide
        //and 1, if line corresponds to detected nuclide or nuclide under question
    }


    //special test for mixture Pu+Ba
    private void testForPuBa(Nuclide checkNuclide) {//if calls then we have already Ba

        int j, i, en, to, foundKev = 0;
        int bbegin = 0, bcenter = 0;
        int bmean1 = 0, bmean2 = 0;
        int pmean;
        int mean1;
        int mean2;
        int porog;

        i = channelFromEnergy(400);//start finding peak from 400 kev
        to = channelFromEnergy(500);//stop finding peak at 500 kev
        mean1 = convolution[i];
        for (; i < to; i++) {
            mean2 = convolution[i + 1];
            porog = mean2 - mean1;
            mean1 = mean2;
            if (porog < 0) {
                bmean1 = -1;
            } else if (porog > 0) {
                bmean1 = 1;
            } else if (i < channelNumber - 1) {
                continue;
            }
            pmean = bmean1 - bmean2;
            bmean2 = bmean1;
            if (bbegin == 0) {
                if (pmean >= 1) {//впадина{
                    bbegin = i - 1;
                }
            } else if (bcenter == 0) {
                if (pmean == -2) {//вершина
                    bcenter = i;
                }
            } else {
                en = i - bcenter;
                if ((pmean == 2 || (pmean == 0 && i == channelNumber - 1)) && (en > 1))//впадина
                {
                    if (en > 2) {
                        mean2 = convolution[bcenter];
                        j = 1;
                        for (; j <= 10; j++) {
                            if (convolution[bcenter + j] <= mean2)
                                mean2 = convolution[bcenter + j];
                            else
                                break;
                        }
                        if (j >= 6) {
                            //found, check its energy
                            foundKev = (int) getEnergyFromChannel(spectrumEnergy, bcenter);
                            break;
                        }
                    }
                }
            }
        }
        if (foundKev > 400 && foundKev < 435) {//we have Pu
            for (Nuclide nuclide : nuclides) {
                String name = nuclide.getName();
                if (name.charAt(0) == 'R'
                        && name.charAt(1) == 'P'
                        && name.charAt(2) == 'u') {
                    nuclide.setState(checkNuclide.getState());//select RPu
                    nuclide.setConfidence(6);
                    break;
                }
            }
        }
    }


    private void testNoStrongLines(Nuclide nuclide) {
        int k, num, j;
        num = nuclide.getLinesNum();
        EnergyLine[] lines = nuclide.getEnergyLines();
        for (k = 0; k < num; k++) { //high energy lines go first
            j = lines[k].getIndex();
            if ((j != BAD_INDEX) && ((lines[k].getFactorsShield() > 0) &&
                    (lines[k].getFactorsNoShield() > 0))) {//обязательное наличие строгой линии
                //определение нуклида по не строгой линии не будет
                if (exEnergies[j] != 0) {//если есть линии не исключенные, то этот нуклид может рассматриваться дальше
                    return;
                } //if one of important nuclide's line is not excluded, then break
            }
        }
        //все строгие линии уже исключены, значит исключаем нуклид
        nuclide.setState(UNIDENTIFIED);//all lines of that nuclide belongs to other nuclide
    }

    private boolean testForLostPeak(Nuclide nuclide, long threshold) {
        //проверка всех нуклидов под вопросом по отношению высот пиков
        int num;
        int k;
        int j = 255;
        int n = 0;
        int n1 = 0;
        int n2 = 0;
        int ind;
        int btFlag = 0;
        boolean b1 = false;
        int channel;
        int sh, nsh;
        float temp = 0, temp1 = 0, tempd = 0, tempd1 = 0;

        num = nuclide.getLinesNum();
        EnergyLine[] lines = nuclide.getEnergyLines();

        for (k = 0; k < num; k++) { //high energy lines go first
            ind = lines[k].getIndex();
            nsh = lines[k].getFactorsNoShield();
            if (ind != BAD_INDEX) {
                channel = (int) (niChannels[ind] + 0.5f);// lnVal2=0x7FFFFFFF; lnVal=0x7FFFFFFF;
                if (j == 255 && nsh != 0)
                    j = k; //запоминание индекса линии самой высокоэнергетичной не нулевой
                sh = lines[k].getFactorsShield();
                if (nsh != 0 && sh != 0) {
                    //////!!!!!!! вернули деления//11/10/2008 закоментированы деления, считаем что они вроде как не нужны
                    temp += (float) convolution[channel] * (float) nsh / (float) convolutionDisp[channel];
                    tempd += (float) nsh * (float) nsh / (float) convolutionDisp[channel];
                    temp1 += (float) convolution[channel] * (sh == 1 ? (float) 0 : (float) sh) / (float) convolutionDisp[channel];
                    tempd1 += (sh == 1 ? (float) 0 : (float) sh * (float) sh) / (float) convolutionDisp[channel];
                    n++;
                    if (sh > 0) n1++;
                    if (sh > 1) n2++;
                }
            } else {
                if (nsh != 0) {
                    btFlag = 1;
                }
            }
        }
        if (btFlag != 0) {//есть хотя бы одна ненулевая линия в библиотеке, которая не определена
            if (n == 0) {//ни одна линия не определена
                //нуклид исключается
                nuclide.setState(UNIDENTIFIED);
                b1 = true;
            } else {
                if (n2 != 0)//определена хотя бы одна строгая линия, но не спорная
                    temp1 /= tempd1;
                //определена хотя бы одна ненулевая линия
                temp /= tempd;
                for (k = 0; k < num; k++) {
                    ind = lines[k].getIndex();
                    nsh = lines[k].getFactorsNoShield();
                    sh = lines[k].getFactorsShield();
                    //before 24/08/2010
                    // if((ind==BAD_INDEX)&&(nsh)&&(sh>1))
                    //after 24/08/2010 it helps to exclude RPu on Cs
                    if (nsh != 0 && sh > 1) {
                        //есть линия ненулевая и неспорная
                        if (k > j) {//if checked line has higher energy, then process it as if there is no shielding, else as if there is shielding
                            //это не самая высоко энергетическая линия
                            if (n1 == 0) {//ни одна строгая или спорная линия не определена
                                //исключаем нуклид
                                nuclide.setState(UNIDENTIFIED);
                                b1 = true;
                                break;
                            }
                            tempd = temp1 * (float) sh;
                        } else {//самая высокоэнергетическая линия
                            tempd = temp * (float) nsh;
                        }
                        sh = (int) lines[k].getEnergy();
                        channel = checkChannel(sh); //find closest maximum
                        tempd1 = (float) Math.sqrt(convolutionDisp[channel]);
                        tempd = 256.0f * tempd / tempd1; //calculate amplitude of line in units of standard deviation
                        //!!!!!!!!!!!в ниже следующем условии есть ньюанс, если нуклид не проходит порог
                        //ниже для определения наличия линии, то этот нуклид никак не может быть
                        //определен далее по одной какой то основной линии
                        //!!!!!!!!!!!если мы дошли до этого условия и если в него ни разу не попали
                        //для данного нуклида, то этот нуклид не должен быть определен по одной линии
                        if (nuclide.getState() == UNKNOWN && sh >= 90)    //условие разрешение спора для основных линий ниже 90 кэВ
                            nuclide.setState(INVALID);   //этот нуклид не может быть выбран, т.к. еще не прошел условие статистики
                        if (tempd > threshold/*+768.0f*/)//убрано изза того что никак не проходился порог при явном отсутсвии пиков и хорошей статистики
                        { //768 = 3*sigma . If calculated amplitude is higher then threshold by three sigma
                            //!!!!!!!!!!есть вопрос: здесь мы можем убрать пометку нуклида как нерозыгрышного, но ведь для других линий он мог остаться нерозыгрышным?
                            if (nuclide.getState() == INVALID)
                                nuclide.setState(VALID);    //а этот нуклид уже может быть выбран т.к. прошел условие статистики
                            //пик по библиотечной высоте из расчета текущей дисперсии проходит порог
                            tempd1 = 256.0f * convolution[channel] / tempd1;

                            if (tempd1/*+768.0f*/ < threshold / 2) {
                                //!!!!!!! убрано 768 из-за того что явное отсутствие пиков пропускалось
                                //check it for missed peak
                                //пик спектра не прошел порог
                                //т.е. это некий пстрик вместо пика, или вообще ничего
                                //определяем не размыта ли эта линия близ лежащими
                                //если размыта то нуклид не исключается
                                // before 24/08/2010if(!findOtherStrong(sh))
                                // after 24/08/2010 it helps to exclude Rpu on Cs

                                if ((ind != BAD_INDEX) || !findOtherStrong(sh)) {
                                    //нуклид исключается
                                    nuclide.setState(UNIDENTIFIED);
                                    b1 = true;
                                    break;
                                }
                            } else {
                                //пик спектра прошел порог
                                //before 24/08/2010
                                //if((tempd>(float)(threshold<<2))&&(tempd-tempd1>(10.0f*256)))
                                //after 24/08/2010 it helps to exclude Rpu on Cs
                                if ((k != j) /*not for the max energy lines*/
                                        && (tempd > (float) (threshold << 2)) && (tempd - tempd1 > (10.0f * 256))) { //very large peak here but we see very small
                                    //пик библиотечный высок, но видим низкий пик на спектре
                                    //определяем не размыта ли эта линия близ лежащими
                                    //если размыта то нуклид не исключается

                                    //before 24/08/2010				if(!findOtherStrong(sh))
                                    //after 24/08/2010 it helps to exclude Rpu on Cs
                                    if ((ind != BAD_INDEX) || !findOtherStrong(sh)) {
                                        nuclide.setState(UNIDENTIFIED);
                                        b1 = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (nuclide.getState() == VALID) {
                    nuclide.setState(UNKNOWN);
                }
            }
        }
        return b1;
    }


    //поиск других близлежащих линий, которые моглибы размазать эту линию
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean findOtherStrong(int en) {
        int i;
        int chan, chanPrim, si;

        chanPrim = channelFromEnergy(en); //find closest maximum
        for (i = 0; i < nLine; i++) {
            chan = (int) (niChannels[i] + 0.5f);
            si = (int) (spectrumSigma[chan] * 2 + 0.5f);
            if (chanPrim > (chan - si) && chanPrim < (chan + si)) {//есть такой пик
                return true;    //нуклид исключать не нужно
            }
        }
        return false;
    }

    private void excludeLines(Nuclide nuclide) {
        int btInd, btInd2;
        int chan;
        int lnVal;
        int btFlag = 255;
        float temp, temp1;
        float tempd = 0, tempd1 = 0;

        EnergyLine[] lines = nuclide.getEnergyLines();

        for (btInd = 0; btInd < nuclide.getLinesNum(); btInd++) {
            btInd2 = lines[btInd].getIndex();
            if (btInd2 != BAD_INDEX && exEnergies[btInd2] != 0) {
                lnVal = lines[btInd].getFactorsNoShield();
                if (lnVal != 0 && lines[btInd].getFactorsShield() != 0) {
                    chan = channelFromEnergy((int) lines[btInd].getEnergy()); //find closest maximum
                    //flt = sqrt((float)SDs[chan]);
                    temp1 = 256.0f * (float) convolution[chan];
                    temp = 256.0f * (float) lnVal;
                    exEnergies[btInd2] = 0;
                    tempd1 = temp1;
                    tempd = temp;
                    btFlag = btInd;
                    break;
                }
            }
        }
        if (btFlag != 255) {
            for (btInd = 0; btInd < nuclide.getLinesNum(); btInd++) {
                if (btInd != btFlag) {
                    btInd2 = lines[btInd].getIndex();
                    if (btInd2 != BAD_INDEX && exEnergies[btInd2] != 0) {
                        //исключение всех линий (строгих и нулевых) кроме не строгих, принадлежащих нуклиду со строгой уникальной линией
                        //нужно посмотреть на высоты этой линии
                        //если она нулевая, то исключаем
                        //если она строгая или не строгая, то смотрим на то чтобы ее высота не была большей чем по калибровке
                        lnVal = lines[btInd].getFactorsNoShield();
                        if (lnVal != 0) {
                            chan = channelFromEnergy((int) lines[btInd].getEnergy()); //find closest maximum
                            temp1 = 256.0f * (float) convolution[chan];
                            temp = 256.0f * (float) lnVal;///flt;
                            //будем проверять по высоте
                            temp = temp / tempd;
                            temp1 = temp1 / tempd1;
                            //temp - отношение высот проверяемого пика к высоко энергетичному по библиотечным высотам, т.е. эталон
                            //temp1 - отношение высот проверяемого пика к высоко энергетичному по спектровым высотам
                            //noinspection StatementWithEmptyBody
                            if (temp1 / temp < 2.5) {//пик не выделяется, т.е. удаляем
                                exEnergies[btInd2] = 0;
                            } else {//пик выше эталонного в 2,5 раза
                                //его не отсеиваем, т.к. он может участвовать далее
                                //ex_energy[btInd2]=ex_energy[btInd2];
                            }
                        } else {
                            exEnergies[btInd2] = 0;
                        }
                    }
                }
            }
        }
    }

    private int checkWeight(Nuclide nuclide) {
        int btInd, btInd2;
        int cnt = 0;
        EnergyLine[] lines = nuclide.getEnergyLines();
        for (btInd = 0; btInd < lines.length; btInd++) {
            btInd2 = lines[btInd].getIndex();
            if (btInd2 != BAD_INDEX && exEnergies[btInd2] != 0) {
                cnt++;
//!!!!!!			if(Nuc->factors_noshield[btInd])
//!!!!!!				cnt++;
                if (lines[btInd].getFactorsShield() != 0)
                    cnt++;
            }
        }
        return cnt;
    }

    private boolean nuclsDifferOnly(Nuclide nuclide, Nuclide nucMain) {
        int i, j, n;
        boolean btFlag = true;
        for (i = 0; i < nucMain.getLinesNum(); i++) {
            n = nucMain.getEnergyLines()[i].getIndex();
            if (n != BAD_INDEX) {
                //нужно определять одинаковость для тестируемого нуклида только строгие линии
                if (exEnergies[n] != 0) {
                    for (j = 0; j < nuclide.getLinesNum(); j++) {
                        if (nuclide.getEnergyLines()[j].getIndex() == n) {
                            btFlag = false;
                            break;
                        }
                    }
                }
            }
            if (!btFlag) {
                break;
            }    //есть перекрывающаяся линия
        }
        //false, если хотя бы одна строгая линия нуклидов перекрывается
        //true, если все оставшиеся линии не перекрываются
        return btFlag;
    }

    private void calcUranium() {
        int en186 = checkChannel(186);
        int en1001 = checkChannel(1001);
        double fMean = (float) convolution[en1001];
        if (fMean > 0) {
            fMean = convolution[en186] / fMean;
            fMean = (100.0 * (0.027 * Math.pow(fMean, 0.52) - 0.038));
            if (fMean > 90) {
                fMean = 90;
            } else if (fMean < 1) {
                fMean = 0;
            }
        } else {
            fMean = 90;
        }
        uranium = (int) fMean;
    }

    private void calcConfidence(int threshold) {
        //int btStored;
        int chan, chan2;
        int btInd, btInd2, btNucl;
        int lval;
        for (btNucl = 0; btNucl < nuclides.size(); btNucl++) {
            Nuclide nuclide = nuclides.get(btNucl);
            if (nuclide.getState() == IDENTIFIED) {
                //вероятности: в переменной 0-255 = значение 0-1
                //start check for amount of strongline and calc max line probability

                /*
                btInd2 = 0;
                for(btInd=0; btInd<Nuc->num; btInd++)
                {
                    btInd2 += (nuclide.getEnergyLines().[btInd].getFactorsNoShield()
                     && (nuclide.getEnergyLines()[btInd].getFactorsShield());
                }
                btStored = 255/btInd2;	//максимальная вероятность на одну линию
                 */
                //end check

                //start check closeness of each line and calc its probability
                chan = 0;
                //btStored = 0;
                for (btInd = 0; btInd < nuclide.getLinesNum(); btInd++) {
                    EnergyLine line = nuclide.getEnergyLines()[btInd];
                    if (line.getFactorsNoShield() != 0 && line.getFactorsShield() != 0) {
                        btInd2 = line.getIndex();
                        if (btInd2 != BAD_INDEX && exEnergies[btInd2] != 0) {//только для найденных линий
                            //вер линии = (1-дальность линии)*(свертка/(порог*СКО))
                            chan2 = (int) niChannels[btInd2];
                            chan2 = (int) ((convolution[chan2] / (Math.sqrt(convolutionDisp[chan2])
                                    * threshold / 256)));
                            if (chan2 > 5) chan2 = 5;
                            chan2 = chan2 * 51;
                            lval = (((((256 - Math.abs(line.getCloseness())) * 255) >> 8) * chan2) >> 8);
                            if (lval > 255) {
                                lval = 255;
                            }
                            chan += lval;
                            //btStored++;
                        }
                    }
                }
                //chan/=btStored;
                if (chan > 255) {
                    chan = 255;
                }
                nuclide.setConfidence(chan / 30 + 1);//приводим от 0 до 9
            }
        }
    }

    private void calcNucActivity(Nuclide nuclide, int btInd, float fchan) {
        //	BYTE sc;
        EnergyLine line = nuclide.getEnergyLines()[btInd];
        int left = channelFromEnergy((int) line.getEnergy());
        int cor = line.getCorrection();
        float eff = (efficiency[left] * line.getQuantumYield()
                / (float) 250.0 / (cor == 0 ? 128 : cor));
        float mean;
        if (eff == 0 || timeAccSpec == 0) {
            return;
        }
        /*	msum = (float)(spectrumSigma[(int)(fchan+0.5f)].w)/128.0f;
            mean = fchan-msum;
            left = (short)mean;
            meanF = fchan+msum;
            chan = (int)meanF;//fchan+msum;
            if(chan>=channelNumber-1)
                chan = channelNumber-2;
            msum = mean-left;
            fchan = meanF-chan;
        //	msum = msum-(int)msum;
            mean = (float)spectrum[left];
            left++;
            mean = mean+((float)spectrum[left]-mean)*msum;
            meanF = (float)spectrum[chan];
            chan++;
            mean += meanF+((float)spectrum[chan]-meanF)*fchan;
        //	left++;
            meanF = mean*(chan-left+msum+fchan)/2.0f/(float)timeAccSpectrum;

            mean2 = 0;
            for(;left<chan;left++)
            {
                mean+=(float)spectrum[left];
                if(fSubtractBackground)
                    mean2+=(float)background[left];
            }
        */
        mean = (float) convolution[(int) (fchan + 0.5f)];

        mean /= timeAccSpec;

        /*	if(fSubtractBackground)
                mean2/=(float)backgroundTime*(float)backgroundTime;

            msum = mean-meanF;
            if(msum<=(float)0)
                return;*/
        line.setActivity(mean / eff * 1000000.0f);
        /*	if(msum>0f)
                msum = 200.0f * Math.sqrt((mean+meanF)/(float)timeAccSpectrum + mean2)/msum;
            if(msum>200f)
                msum=200f;*/
        line.setActivityError(0);
    }

    public static float getEnergyFromChannel(float[] spectrumEnergy, float value) {
        int ch = (int) value;
        int maxLength = spectrumEnergy.length - 1;
        if (ch >= maxLength) {
            return spectrumEnergy[maxLength];
        }
        float en1 = spectrumEnergy[ch];
        float en2 = spectrumEnergy[ch + 1];
        value -= ch;
        return en1 + (en2 - en1) * value;
    }
}

