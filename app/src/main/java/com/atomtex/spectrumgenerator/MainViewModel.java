package com.atomtex.spectrumgenerator;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class MainViewModel extends AndroidViewModel {

    public static final String REFERENCE_SPECTRUM = "Эталонный спектр";
    public static final String GENERATED_SPECTRUM = "Сгенерированный спектр";


    private SpecDTO tempDTO;
    private int spectrumTime = 10;
    private int RequiredTime = 100;
    private String pathForAts = null;
    private int displayMode = 0;
    private int buttonMode = 0;
    private int delay = 250;
    private boolean isSecMode = true;

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
