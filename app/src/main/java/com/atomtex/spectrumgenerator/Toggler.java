package com.atomtex.spectrumgenerator;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class Toggler {

    private Context context;

    Toggler(Context context) {
        this.context = context;
    }

    public void setRefLayoutMode(int mode) {
        switch (mode) {
            case 0:
                ((Activity)context).findViewById(R.id.fragment_container1).setVisibility(View.VISIBLE);
                ((Activity)context).findViewById(R.id.ref_spec_layout).setVisibility(View.GONE);
                break;
            case 1:
                ((Activity)context).findViewById(R.id.fragment_container1).setVisibility(View.GONE);
                ((Activity)context).findViewById(R.id.ref_spec_layout).setVisibility(View.VISIBLE);
                break;
        }
    }

}
