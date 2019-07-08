package com.atomtex.spectrumgenerator;

public class SpecMixerParcel {

    private SpecDTO referenceDTO;
    private double percent;

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    SpecMixerParcel(SpecDTO dto, double percent, String name) {
        this.referenceDTO = dto;
        this.percent = percent;
        this.name = name;

    }

    public SpecMixerParcel(SpecDTO referenceDTO, double percent) {
        this.referenceDTO = referenceDTO;
        this.percent = percent;
    }

    public SpecMixerParcel(SpecDTO referenceDTO) {
        this.referenceDTO = referenceDTO;
    }

    public SpecDTO getReferenceDTO() {
        return referenceDTO;
    }

    public double getPercent() {
        return percent;
    }

    public String getName() {
        return name;
    }

}
