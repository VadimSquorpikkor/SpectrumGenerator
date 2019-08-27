package com.atomtex.spectrumgenerator;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.atomtex.spectrumgenerator.domain.EnergyLine;
import com.atomtex.spectrumgenerator.domain.NucIdent;
import com.atomtex.spectrumgenerator.domain.Nuclide;
import com.atomtex.spectrumgenerator.exception.ProcessException;
import com.atomtex.spectrumgenerator.util.NuclideLibraryException;
import com.atomtex.spectrumgenerator.util.NuclideLibraryReader;
import com.atomtex.spectrumgenerator.util.SpectrumGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.atomtex.spectrumgenerator.MainActivity.TAG;
import static com.atomtex.spectrumgenerator.domain.NucIdent.BAD_INDEX;
import static com.atomtex.spectrumgenerator.domain.Nuclide.State.IDENTIFIED;

public class IdentificateAndGenerate {

    private MainViewModel mViewModel;
    private Context context;
    private Toggler toggler;
    private Button genButton;

    public IdentificateAndGenerate(MainViewModel mViewModel, Toggler toggler, Context context, Button genButton) {//todo сделать нормальный конструктор без лишних параметров
        this.mViewModel = mViewModel;
        this.toggler = toggler;//todo можно и через контекст создать, не нужный параметр конструктора
        this.context = context;
        this.genButton = genButton;//todo можно и через контекст создать, не нужный параметр конструктора

    }

    private float[] mPeakChannels;
    private float[] mPeakEnergies;
    private String[] mLineOwners;
    private int mPrefIdenThreshold = 3;

    void setPrefIdenThreshold(int mPrefIdenThreshold) {
        this.mPrefIdenThreshold = mPrefIdenThreshold;
    }

    void identificateNucl(SpecDTO dto) {
//        dto.setMeasTim(new int[]{count, 1});
        if (dto != null) {
            int[] spectrum = dto.getSpectrum();
            float[] energy = dto.getEnergy();
            float[] sigma = dto.getSigma();

            NucIdent nuc = null;
            try {
                nuc = nuclidesIdent(spectrum.length, spectrum, sigma, energy, mPrefIdenThreshold);
            } catch (ProcessException e) {
                e.printStackTrace();
            }
            processIdenResult(nuc);
        }
    }

    private NucIdent nuclidesIdent(int channelNumber, int[] spectrum,  //todo was private
                                   float[] sigma, float[] energy, int threshold) throws ProcessException {

        Log.e(TAG, "---------------nuclidesIdent");
        NucIdent nuc = new NucIdent(channelNumber,
                16, 20, 32, 24,
                20, 20, 0, true);

        nuc.setSpectrum(spectrum);
        nuc.setSpectrumSigma(sigma);
        nuc.setSpectrumEnergy(energy);

        nuc.detectLines(threshold);

        nuc.makeNuclideIdentification(threshold);

        return nuc;
    }

/*    public List<Nuclide> anyNuclideLibrary() {
        List<Nuclide> nucList;
        List<Nuclide> innerList = AllNuclidesList.getAllNuclides();//библиотека, встроенная в приложение//todo при старте загружать иннерБиблиотеку в мэйнВью, и при генерации данные будут браться из мВ, а не из класса (как АутерЛайбрари)

        List<Nuclide> savedList = new ArrayList<>();//сохраненная (загруженная ранее) библиотека

        try {
            savedList = NuclideLibraryReader.getLibrary(mViewModel.getPathForLibrary());
        } catch (RuntimeException e) {
            Log.e(TAG, "***************КОСЯК!!!!" + mViewModel.getPathForLibrary());
        } catch (NuclideLibraryException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "**************LOADED");



        if (savedList == null || savedList.size() == 0) {
            Log.e(TAG, "*********INNER LIBRARY LOADED*********");
            nucList = innerList;
        } else {
            Log.e(TAG, "*********SAVED LIBRARY LOADED*********");
            nucList = savedList;
        }
        NucIdent.setNuclides(nucList);
        return nucList;
    }*/

    private void processIdenResult(NucIdent nuc) {

//        List<Nuclide> nuclides = anyNuclideLibrary();
        List<Nuclide> nuclides = NucIdent.getNuclides();
//        List<Nuclide> nuclides = AllNuclidesList.getAllNuclides();

        int numLines = nuc.getnLine();

        if (numLines > 0) {
            float[] peaks = nuc.getNiChannels();
            int[] energies = nuc.getEnergies();
            float[] peakChannels = new float[numLines];
            float[] peakEnergies = new float[numLines];
            String[] lineOwners = new String[numLines];

            int count = 0;

            for (int i = 0; i < peaks.length; i++) {
                if (peaks[i] > 0) {
                    peakChannels[count] = peaks[i];
                    peakEnergies[count] = energies[i];
                    count++;
                }
            }

            for (Nuclide nuclide : nuclides) {
                Log.e(TAG, "----------------------------nuclide name - " + nuclide.getNumStr());
                if (nuclide.getState() == IDENTIFIED) {
                    Log.e(TAG, "----------------------------nuclide IDENTIFIED - " + nuclide.getNumStr());
                    EnergyLine[] lines = nuclide.getEnergyLines();
                    for (EnergyLine line : lines) {
                        int index = line.getIndex();
                        if (index != BAD_INDEX
                                && line.getFactorsNoShield() > 0
                                && line.getFactorsShield() > 0) {
                            lineOwners[index] = nuclide.getName() + "-" + nuclide.getNumStr();
                            if (mViewModel.getNameForMixer().equals(""))
                                mViewModel.setNameForMixer(lineOwners[index]);
                        }
                    }
                }
            }
            mPeakChannels = peakChannels;
            mPeakEnergies = peakEnergies;
            mLineOwners = lineOwners;
            Log.e(TAG, "identMixer: ch = " + mPeakChannels.length + ", ener = " + mPeakEnergies.length + ", lines = " + mLineOwners.length);
        } else {
            mPeakChannels = null;//todo было включено, но с включенной не определяется нуклид в методе identificateNucl, но определяется при генерации. нужно разобраться  и сделать так, чтобы работало при включенной
        }

//        return nuclides;
    }


//--------------------------ГЕНЕРАТОРЫ--------------------------------------------------------------


    //todo remove path
    private void generateSpectrumTeak(SpecDTO refDto, SpecDTO tempDto, int spTime, int rqTime, int count, boolean isDrawable) {
        tempDto.setMeasTim(new int[]{count, 1});//todo здесь вторая переменная перезаписывается, можно сделать, чтобы сохранялась, но пока не надо -- она все равно всегда 0
        SpectrumGenerator mSpectrumGenerator = new SpectrumGenerator();
//        dto.addSpectrumToCurrent(mSpectrumGenerator.generatedSpectrum(refDto.getSpectrum(), spTime, rqTime));
        tempDto.addSpectrumToCurrent(mSpectrumGenerator.generatedSpectrum(refDto.getSpectrum(), spTime, rqTime));
        int[] spectrum = tempDto.getSpectrum();
        float[] energy = tempDto.getEnergy();
        float[] sigma = tempDto.getSigma();

        NucIdent nuc = null;
        try {
            nuc = nuclidesIdent(spectrum.length, spectrum, sigma, energy, mPrefIdenThreshold);
        } catch (ProcessException e) {
            e.printStackTrace();
        }
        processIdenResult(nuc);

        if (isDrawable) {
            mViewModel.getGeneratedFragment().setNewValues(tempDto, mPeakChannels, mPeakEnergies, mLineOwners);
            mViewModel.getGeneratedFragment().update();
        }
    }

    private final Handler handler = new Handler();
    private Runnable runnable;

    void startMixer() {
        handler.postDelayed(runnable = new Runnable() {
            int count = 0;//cnt;

            //            int reqTime = mViewModel.getRequiredTime(); // если черезх переменные -- тогда нельзя будет изменить параметры во время генерации
//            int delay = mViewModel.getDelay(); // если черезх переменные -- тогда нельзя будет изменить параметры во время генерации
            public void run() {
                if (count++ < mViewModel.getRequiredTime()) {
                    mixerPreTeak2(count, 1, true);
                    genButton.setText(("Остановить ( " + count * 100 / mViewModel.getRequiredTime() + "% )"));
                    handler.postDelayed(runnable, mViewModel.getDelay());
                } else {
                    toggler.setGenButtonMode(0, genButton);
                }
            }
        }, 1);

    }

    //старая версия для раздельного складывания спектров -- в новой версии складываются доли уже сложенного спектра
    /*private void mixerPreTeak(int count, int rqTimeOne, boolean isDrawable) {
///////        if(mViewModel.getSourceList().size()==0)manager.beginTransaction().replace(R.id.fragment_container2, SpectrumFragment.newInstance(REFERENCE_SPECTRUM)).commitAllowingStateLoss();
        for (SpecMixerParcel parcel:mViewModel.getSourceList()) {
            if(parcel.isChecked()) {
                SpecDTO dto = parcel.getReferenceDTO();
                generateSpectrumTeak(dto, mViewModel.getTempDTO(), dto.getMeasTim()[0] * 100 / parcel.getPercent(), rqTimeOne, count, isDrawable);
            }
        }
    }*/

    private void mixerPreTeak2(int count, int rqTimeOne, boolean isDrawable) {
        generateSpectrumTeak(mViewModel.getEmptyDto(), mViewModel.getTempDTO(), mViewModel.getEmptyDto().getMeasTim()[0], rqTimeOne, count, isDrawable);
    }

    private void updateGeneratedSpectrum() {
        mViewModel.getGeneratedFragment().setNewValues(mViewModel.getTempDTO(), mPeakChannels, mPeakEnergies, mLineOwners);
        mViewModel.getGeneratedFragment().update();
    }

    void preferenceMixer() {
        mViewModel.setEmptyDto(new SpecDTO(1024));
        for (SpecMixerParcel parcel : mViewModel.getSourceList()) {
            if (parcel.isChecked()) {
                SpecDTO dto = parcel.getReferenceDTO();
                mViewModel.getEmptyDto().addSpectrumToCurrent(dto.getSpectrum(), parcel.getPercent());
                if (dto.getMeasTim()[0] > mViewModel.getEmptyDto().getMeasTim()[0])
                    mViewModel.getEmptyDto().setMeasTim(dto.getMeasTim());//в meas сохраняется максимальное время
                //emptyDto.setMeasTim(new int[]{emptyDto.getMeasTim()[0]+dto.getMeasTim()[0],1});//в meas сохраняется сумма времен всех спектров
            }
        }
        identificateNucl(mViewModel.getEmptyDto());
        mViewModel.getReferenceFragment().setNewValues(mViewModel.getEmptyDto(), mPeakChannels, mPeakEnergies, mLineOwners);
        mViewModel.getReferenceFragment().update();
    }

    void startQuickMixer() {
        for (int count = 0; count < mViewModel.getRequiredTime(); count++) {
            mixerPreTeak2(count, 1, false);
        }
        toggler.setGenButtonMode(0, genButton);//todo ??? работает и без этой строчки
        mViewModel.getGeneratedFragment().setNewValues(mViewModel.getTempDTO(), mPeakChannels, mPeakEnergies, mLineOwners);
        mViewModel.getGeneratedFragment().update();
    }

    void stop() {
        handler.removeCallbacks(runnable);
    }

}
