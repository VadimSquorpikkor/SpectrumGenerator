package com.atomtex.spectrumgenerator;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainViewModel extends AndroidViewModel {

    public static final String REFERENCE_SPECTRUM = "Эталонный спектр";
    public static final String GENERATED_SPECTRUM = "Сгенерированный спектр";


    private SpecDTO tempDTO;
    private SpecDTO referenceDTO;
    private int spectrumTime = 10;
    private int RequiredTime = 100;
    private String pathForAts = null;
    private int displayMode = 0;
    private int buttonMode = 0;
    private ArrayList<SpecDTO> specList = new ArrayList<SpecDTO>(){};


    private SpecDTO[] specArr = new SpecDTO[2];
//    private ArrayList<SpecDTO> tempSpecList = new ArrayList<>();


    public int getSourcesItemsCount() {
        return sourcesItemsCount;
    }

    public void setSourcesItemsCount(int sourcesItemsCount) {
        this.sourcesItemsCount = sourcesItemsCount;
    }

    private int sourcesItemsCount = 2; //размер ListView для источников

    private int mixerMode = 0;
    private int delay = 250;
    private boolean isSecMode = true;
    private ArrayList<SpecMixerParcel> specMixerParcel = new ArrayList<>();
    private SparseArray<SpecMixerParcel> specParcel = new SparseArray<>();

    public SpecDTO[] getSpecArr() {
        return specArr;
    }

 /*   public ArrayList<SpecDTO> getSpecList() {
        return specList;
    }*/

/*    public ArrayList<SpecDTO> getTempSpecList() {
        return tempSpecList;
    }*/

/*    public void addSpecToList(SpecDTO dto) {
        specList.add(dto);
    }*/

    public void addSpecToList(int position, SpecDTO dto) {
        specArr[position] = dto;
    }

    public SpecDTO getSpecFromList(int position) {
        return specArr[position];
    }

/*
    public void addTempSpecToList(SpecDTO dto) {
        tempSpecList.add(dto);
    }
*/

    public int getMixerMode() {
        return mixerMode;
    }

    public void setMixerMode(int mixerMode) {
        this.mixerMode = mixerMode;
    }

    public void addSpecToMixer(int position, SpecDTO dto) {
        specParcel.put(position, new SpecMixerParcel(dto));
    }

    public SparseArray<SpecMixerParcel> getMixerParcel() {
        return specParcel;
    }
/*
    public void addSpecToMixer(SpecDTO dto, double percent) {
        specMixerParcel.add(new SpecMixerParcel(dto, percent));
    }

    public ArrayList<SpecMixerParcel> specMixerParcel() {
        return specMixerParcel;
    }*/

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

    public int getTimeLayoutMode() {
        return timeLayoutMode;
    }

    public void setTimeLayoutMode(int timeLayoutMode) {
        this.timeLayoutMode = timeLayoutMode;
    }

    private int timeLayoutMode = 0;
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


    public int getButtonMode() {
        return buttonMode;
    }

    public void setButtonMode(int buttonMode) {
        this.buttonMode = buttonMode;
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

    public int getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
    }




}


