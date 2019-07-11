package com.atomtex.spectrumgenerator;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class Toggler {

    private Context context;
    private MainViewModel mViewModel;

    Toggler(Context context, MainViewModel mViewModel) {
        this.context = context;
        this.mViewModel = mViewModel;
    }

    private void toggleStuff(int res_1, int res_2, int mode) {
        switch (mode) {
            case 0:
                ((Activity)context).findViewById(res_1).setVisibility(View.VISIBLE);
                ((Activity)context).findViewById(res_2).setVisibility(View.GONE);
                break;
            case 1:
                ((Activity)context).findViewById(res_1).setVisibility(View.GONE);
                ((Activity)context).findViewById(res_2).setVisibility(View.VISIBLE);
                break;
        }
    }

    private void toggleStuff(int res_1, boolean isOn) {
        if (isOn) ((Activity) context).findViewById(res_1).setVisibility(View.VISIBLE);
        else ((Activity) context).findViewById(res_1).setVisibility(View.GONE);
    }

/*    public void setRefLayoutMode(int mode) {
        toggleStuff(R.id.fragment_container1, R.id.ref_spec_layout, mode);
    }*/

/*    public void toggleMode() {
        int mode = 0;
        if (mViewModel.getDisplayMode() == 0) mode = 1;
        if (mViewModel.getDisplayMode() == 1) mode = 2;
        mViewModel.setDisplayMode(mode);
        setDisplayMode(mode);
    }*/

    public void setDisplayMode(int mode) {
        switch (mode) {
            case 0:
                ((Activity)context).findViewById(R.id.fragment_container1).setVisibility(View.VISIBLE);
                ((Activity)context).findViewById(R.id.fragment_container2).setVisibility(View.VISIBLE);
                break;
            case 1:
                ((Activity)context).findViewById(R.id.fragment_container1).setVisibility(View.VISIBLE);
                ((Activity)context).findViewById(R.id.fragment_container2).setVisibility(View.GONE);
                break;
            case 2:
                ((Activity)context).findViewById(R.id.fragment_container1).setVisibility(View.GONE);
                ((Activity)context).findViewById(R.id.fragment_container2).setVisibility(View.VISIBLE);
                break;
        }
    }


    public void setTimeLayoutMode(int mode) {
        toggleStuff(R.id.time_layout_small, R.id.time_layout_big, mode);
    }

//--------------------------------------------------------------------------------------------------

    public void setReferenceFragmentMode(boolean isOn) {
        toggleStuff(R.id.fragment_container1, isOn);
        mViewModel.setRefVisibility(isOn);
    }

    public void setMixerMode(boolean isOn) {
        toggleStuff(R.id.mixer_fragment_list_view, isOn);
        mViewModel.setMixerVisibility(isOn);
    }

    public void setTimeMode(boolean isOn) {
        toggleStuff(R.id.time_layout, isOn);
        mViewModel.setTimeVisibility(isOn);
    }

    public void setButtonsMode(boolean isOn) {
        toggleStuff(R.id.button_layout, isOn);
        mViewModel.setButtonVisibility(isOn);
    }

    public void setMatrixMode(boolean isOn) {

    }

    public void setAllVisibilities() {
        toggleStuff(R.id.fragment_container1, mViewModel.isRefVisible());
        toggleStuff(R.id.mixer_fragment_list_view, mViewModel.isMixerVisible());
        toggleStuff(R.id.time_layout, mViewModel.isTimeVisible());
        toggleStuff(R.id.button_layout, mViewModel.isButtonVisible());
//        toggleStuff(R.id.fragment_container1, mViewModel.isRefVisible());
    }

//--------------------------------------------------------------------------------------------------

}
