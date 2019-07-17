package com.atomtex.spectrumgenerator;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;


public class MainViewModel extends AndroidViewModel {

    public static final String REFERENCE_SPECTRUM = "Эталонный спектр";
    public static final String GENERATED_SPECTRUM = "Сгенерированный спектр";


//    private SpecDTO tempDTO = new SpecDTO(1024);//todo сделать для любой длины, не только 1024
    private SpecDTO tempDTO = new SpecDTO(1024);

    private SpecDTO referenceDTO;
    private int spectrumTime = 10;
    private int RequiredTime = 100;
    private String pathForAts = null;
    private int delay = 1;
    private boolean isSecMode = true;
    public boolean isFirstTime = true;
    SpecDTO emptyDto = new SpecDTO(1024);
    LineChart mSpectrumChart;

    public LineChart getmSpectrumChart() {
        return mSpectrumChart;
    }

    public void setmSpectrumChart(LineChart mSpectrumChart) {
        this.mSpectrumChart = mSpectrumChart;
    }

    public SpecDTO getEmptyDto() {
        return emptyDto;
    }

    public void setEmptyDto(SpecDTO emptyDto) {
        this.emptyDto = emptyDto;
    }


//----------------FRAGMENTS-------------------------------------------------------------------------
    private SpectrumFragment referenceFragment;
    private SpectrumFragment generatedFragment;

    public SpectrumFragment getReferenceFragment() {
        return referenceFragment;
    }

    public void setReferenceFragment(SpectrumFragment referenceFragment) {
        this.referenceFragment = referenceFragment;
    }

    public SpectrumFragment getGeneratedFragment() {
        return generatedFragment;
    }

    public void setGeneratedFragment(SpectrumFragment generatedFragment) {
        this.generatedFragment = generatedFragment;
    }

//----------------VISIBILITIES----------------------------------------------------------------------

    private boolean refVisibility = true;
    private boolean mixerVisibility = true;
    private boolean timeVisibility = true;
    private boolean buttonVisibility = true;
    private boolean matrixVisibility = true;

    public boolean isRefVisible() {
        return refVisibility;
    }

    public void setRefVisibility(boolean refVisibility) {
        this.refVisibility = refVisibility;
    }

    public boolean isMixerVisible() {
        return mixerVisibility;
    }

    public void setMixerVisibility(boolean mixerVisibility) {
        this.mixerVisibility = mixerVisibility;
    }

    public boolean isTimeVisible() {
        return timeVisibility;
    }

    public void setTimeVisibility(boolean timeVisibility) {
        this.timeVisibility = timeVisibility;
    }

    public boolean isButtonVisible() {
        return buttonVisibility;
    }

    public void setButtonVisibility(boolean buttonVisibility) {
        this.buttonVisibility = buttonVisibility;
    }

    public boolean isMatrixVisible() {
        return matrixVisibility;
    }

    public void setMatrixVisibility(boolean matrixVisibility) {
        this.matrixVisibility = matrixVisibility;
    }

//----------------MODES-----------------------------------------------------------------------------

    private int mixerMode = 0;
    private int timeLayoutMode = 0;
    private boolean fragmentReqFullMode = false;
    private boolean fragmentGenFullMode = false;

    public boolean getFragmentGenFullMode() {
        return fragmentGenFullMode;
    }

    public void setFragmentGenFullMode(boolean fragmentGenFullMode) {
        this.fragmentGenFullMode = fragmentGenFullMode;
    }


    public boolean getFragmentReqFullMode() {
        return fragmentReqFullMode;
    }

    public void setFragmentReqFullMode(boolean fragmentReqFullMode) {
        this.fragmentReqFullMode = fragmentReqFullMode;
    }



    public int getMixerMode() {
        return mixerMode;
    }

    public void setMixerMode(int mixerMode) {
        this.mixerMode = mixerMode;
    }

    public int getTimeLayoutMode() {
        return timeLayoutMode;
    }

    public void setTimeLayoutMode(int timeLayoutMode) {
        this.timeLayoutMode = timeLayoutMode;
    }



//----------------MIXER PARCEL----------------------------------------------------------------------

    private List<SpecMixerParcel> sourceList = new ArrayList<>();

    public List<SpecMixerParcel> getSourceList() {
        return sourceList;
    }

    public void addNewSpectrum(SpecDTO dto, String name) {
        sourceList.add(new SpecMixerParcel(dto, name));
    }

    public void setPercentParcel(int position, int percent) {
        sourceList.get(position).setPercent(percent);
//        ((MixerListFragment) fragment4).updateAdapter();
    }

    public void setIsCheckedParcel(int position, boolean isChecked) {
        sourceList.get(position).setChecked(isChecked);
    }

//--------------------------------------------------------------------------------------------------

    public SpecDTO getReferenceDTO() {
        return referenceDTO;
    }

    public void setReferenceDTO(SpecDTO referenceDTO) {
        this.referenceDTO = referenceDTO;
    }

    public boolean isSecMode() {
        return isSecMode;
    }

    public void setSecMode(boolean secMode) {
        isSecMode = secMode;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }


    private boolean genButtonIsPressed = false;

    public boolean isGenButtonIsPressed() {
        return genButtonIsPressed;
    }

    public void setGenButtonIsPressed(boolean genButtonIsPressed) {
        this.genButtonIsPressed = genButtonIsPressed;
    }

    public SpecDTO getTempDTO() {
        return tempDTO;
    }

    public void setTempDTO(SpecDTO tempDTO) {
        this.tempDTO = tempDTO;
    }

    public int getSpectrumTime() {
        return spectrumTime;
    }

    public void setSpectrumTime(int spectrumTime) {
        this.spectrumTime = spectrumTime;
    }

    public int getRequiredTime() {
        return RequiredTime;
    }

    public void setRequiredTime(int requiredTime) {
        RequiredTime = requiredTime;
    }

    public String getPathForAts() {
        return pathForAts;
    }

    public void setPathForAts(String pathForAts) {
        this.pathForAts = pathForAts;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
    }




}


