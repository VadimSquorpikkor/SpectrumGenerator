package com.atomtex.spectrumgenerator;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Objects;

import static com.atomtex.spectrumgenerator.MainActivity.TAG;

public class MixerFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private static MainViewModel mViewModel;

    public static MixerFragment newInstance() {
        return new MixerFragment();
    }


    EditText ed1;
    EditText ed2;
    Switch sw1;
    Switch sw2;
    TextView tw1;
    TextView tw2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mixer, null);

        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        ed1 = v.findViewById(R.id.mixer_edit1);
        ed2 = v.findViewById(R.id.mixer_edit2);
        tw1 = v.findViewById(R.id.mixer_text1);
        tw2 = v.findViewById(R.id.mixer_text2);

        v.findViewById(R.id.mixer_button1).setOnClickListener(this);
        v.findViewById(R.id.mixer_button2).setOnClickListener(this);

        final SeekBar seekBar1 = v.findViewById(R.id.mixer_seek1);
        seekBar1.setOnSeekBarChangeListener(this);

        final SeekBar seekBar2 = v.findViewById(R.id.mixer_seek2);
        seekBar2.setOnSeekBarChangeListener(this);

        sw1 = v.findViewById(R.id.mixer_switch1);
        if (sw1 != null) sw1.setOnCheckedChangeListener(this);
        sw1.setChecked(true);
        sw2 = v.findViewById(R.id.mixer_switch2);
        if (sw2 != null) sw2.setOnCheckedChangeListener(this);
        sw2.setChecked(true);

        seekBar1.setProgress(mViewModel.getPercentArr()[0]);
        seekBar2.setProgress(mViewModel.getPercentArr()[1]);

        return v;

    }

    public void updateText() {
        Log.e(TAG, "updateText: " + mViewModel.getNameArr().length);
        tw1.setText(mViewModel.getNameArr()[0]);
        tw2.setText(mViewModel.getNameArr()[1]);
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_DEVICE_ACTIVITY == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                mViewModel.connectDevice(data.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
            } else*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mixer_button1: ((MainActivity)getActivity()).openAts(0) ;break;
            case R.id.mixer_button2: ((MainActivity)getActivity()).openAts(1) ;break;

        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(seekBar.getId()==R.id.mixer_seek1) ed1.setText(String.valueOf(seekBar.getProgress()));
        if(seekBar.getId()==R.id.mixer_seek2) ed2.setText(String.valueOf(seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar.getId()==R.id.mixer_seek1) mViewModel.addPercent(0, Integer.parseInt(String.valueOf(seekBar.getProgress())));
        if(seekBar.getId()==R.id.mixer_seek2) mViewModel.addPercent(1, Integer.parseInt(String.valueOf(seekBar.getProgress())));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId()==R.id.mixer_switch1) {
            if (isChecked) mViewModel.addIsCheched(0, true);
            else mViewModel.addIsCheched(0, false);
        }
        if(buttonView.getId()==R.id.mixer_switch2){
            if (isChecked) mViewModel.addIsCheched(1, true);
            else mViewModel.addIsCheched(1, false);
        }
    }
}
