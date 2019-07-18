package com.atomtex.spectrumgenerator;

public class SpecMixerParcel {

    private SpecDTO referenceDTO;
    private int percent = 100;
    private String name = "Empty";
    private boolean isChecked = true;
    private String path;

//----------------SETTERS & GETTERS ----------------------------------------------------------------

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getPercent() {
        return percent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public SpecDTO getReferenceDTO() {
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
