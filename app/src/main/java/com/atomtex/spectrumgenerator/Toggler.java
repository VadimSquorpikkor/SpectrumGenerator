package com.atomtex.spectrumgenerator;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

class Toggler {

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
//--------------------------------------------------------------------------------------------------
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

    void setTimeLayoutMode(int mode) {
        toggleStuff(R.id.time_layout_small, R.id.time_layout_big, mode);
    }

    void setMixerLayoutMode(int mode) {
        toggleStuff(R.id.mixer_fragment_list_view_big, R.id.mixer_fragment_list_view_small, mode);
    }

//--------------------------------------------------------------------------------------------------

    void setReferenceFragmentMode(boolean isOn) {
        toggleStuff(R.id.fragment_container1, isOn);
        mViewModel.setRefVisibility(isOn);
    }

    void setMixerMode(boolean isOn) {
        toggleStuff(R.id.mixer_fragment_list_view, isOn);
        mViewModel.setMixerVisibility(isOn);
    }

    void setTimeMode(boolean isOn) {
        toggleStuff(R.id.time_layout, isOn);
        mViewModel.setTimeVisibility(isOn);
    }

    void setButtonsMode(boolean isOn) {
        toggleStuff(R.id.button_layout, isOn);
        mViewModel.setButtonVisibility(isOn);
    }

    void setAllVisibilities() {
        toggleStuff(R.id.fragment_container1, mViewModel.isRefVisible());
        toggleStuff(R.id.mixer_fragment_list_view, mViewModel.isMixerVisible());
        toggleStuff(R.id.time_layout, mViewModel.isTimeVisible());
        toggleStuff(R.id.button_layout, mViewModel.isButtonVisible());
//        toggleStuff(R.id.fragment_container1, mViewModel.isRefVisible());
    }

//--------------------------------------------------------------------------------------------------

    void setGenButtonMode(int mode, Button genButton) {//0 - gray; 1 - orange
        if (mode == 0) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Drawable drawable = context.getResources().getDrawable(R.drawable.button_shape_selector);
                genButton.setBackground(drawable);
            }
            genButton.setText("Генератор");
            mViewModel.setGenButtonIsPressed(false);
        }
        if (mode == 1) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Drawable drawable = context.getResources().getDrawable(R.drawable.my_button_shape_pressed);
                genButton.setBackground(drawable);
            }
            genButton.setText("Остановить");
            mViewModel.setGenButtonIsPressed(true);
        }

    }

}
