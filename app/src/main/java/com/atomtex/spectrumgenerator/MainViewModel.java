package com.atomtex.spectrumgenerator;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class MainViewModel extends AndroidViewModel {

    private int spectrumTime = 10;
    private int RequiredTime;
    private String pathForAts;
    private int displayMode;
    private boolean iCanGenerate;


    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public boolean isiCanGenerate() {
        return iCanGenerate;
    }

    public void setiCanGenerate(boolean iCanGenerate) {
        this.iCanGenerate = iCanGenerate;
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
}
