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


    public void setRefLayoutMode(int mode) {
        toggleStuff(R.id.fragment_container1, R.id.ref_spec_layout, mode);
    }

    public void toggleMode() {
        int mode = 0;
        if (mViewModel.getDisplayMode() == 0) mode = 1;
        if (mViewModel.getDisplayMode() == 1) mode = 2;
        mViewModel.setDisplayMode(mode);
        setDisplayMode(mode);
    }

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

    public void setButtonMode(int mode) {
        switch (mode) {
            case 0:
                ((Activity)context).findViewById(R.id.time_layout).setVisibility(View.VISIBLE);
                ((Activity)context).findViewById(R.id.button_layout).setVisibility(View.VISIBLE);
                break;
            case 1:
                ((Activity)context).findViewById(R.id.time_layout).setVisibility(View.GONE);
                ((Activity)context).findViewById(R.id.button_layout).setVisibility(View.GONE);
                break;
        }
    }

    public void setTimeLayoutMode(int mode) {
        toggleStuff(R.id.time_layout_small, R.id.time_layout_big, mode);
    }


}
