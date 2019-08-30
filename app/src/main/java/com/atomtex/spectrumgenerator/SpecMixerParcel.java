package com.atomtex.spectrumgenerator;

public class SpecMixerParcel {

    private SpecDTO referenceDTO;
    private int percent;
    private String name;
    private boolean isChecked;

//----------------SETTERS & GETTERS ----------------------------------------------------------------

    void setPercent(int percent) {
        this.percent = percent;
    }

    int getPercent() {
        return percent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    void setChecked(boolean checked) {
        isChecked = checked;
    }

    boolean isChecked() {
        return isChecked;
    }

    SpecDTO getReferenceDTO() {
        return referenceDTO;
    }

//----------------CONSTRUCTOR-----------------------------------------------------------------------

    SpecMixerParcel(SpecDTO dto, String name) {
        this.referenceDTO = dto;
        this.isChecked = true;
        this.name = name;
        this.percent = 100;
    }
}
