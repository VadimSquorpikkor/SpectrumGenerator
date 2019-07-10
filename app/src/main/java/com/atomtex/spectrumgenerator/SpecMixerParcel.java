package com.atomtex.spectrumgenerator;

public class SpecMixerParcel {

    private SpecDTO referenceDTO;
    private int percent = 100;
    private String name = "Empty";
    private boolean isChecked = true;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    SpecMixerParcel(SpecDTO dto, String name) {
        this.referenceDTO = dto;
        this.isChecked = true;
        this.name = name;
        this.percent = 100;
    }




    public SpecDTO getReferenceDTO() {
        return referenceDTO;
    }

    public int getPercent() {
        return percent;
    }
    public void addPercent(int percent) {
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

}
