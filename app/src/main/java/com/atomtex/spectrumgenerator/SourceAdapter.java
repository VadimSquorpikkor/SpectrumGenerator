package com.atomtex.spectrumgenerator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import static com.atomtex.spectrumgenerator.MainActivity.TAG;

public class  SourceAdapter extends ArrayAdapter<SpecMixerParcel>{

    private LayoutInflater inflater;
    private int layout;
    private List<SpecMixerParcel> sourceList;
    private MainViewModel mViewModel;
    private Fragment fragment;
//    private SpectrumFragment prefFragment;

    private Switch sw;
    private TextView tw;
    private SeekBar sb;
    private ImageButton ib;

    SourceAdapter(Context context, int resource, List<SpecMixerParcel> sourceList, MainViewModel mViewModel, Fragment fragment/*, SpectrumFragment prefFragment*/) {
        super(context, resource, sourceList);
        this.sourceList = sourceList;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.mViewModel = mViewModel;
        this.fragment = fragment;
//        this.prefFragment = prefFragment;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        @SuppressLint("ViewHolder")
        View view = inflater.inflate(this.layout, parent, false);

        sw = view.findViewById(R.id.mixer_item_switch);
        tw = view.findViewById(R.id.mixer_item_text);
        sb = view.findViewById(R.id.mixer_item_seek);
        ib = view.findViewById(R.id.mixer_item_delete);
        final TextView et = view.findViewById(R.id.mixer_item_edit);

        SpecMixerParcel state = sourceList.get(position);

        tw.setText(state.getName());
        sw.setChecked(state.isChecked());
        et.setText("" + state.getPercent());

        sb.setProgress(state.getPercent());
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                et.setText("" + seekBar.getProgress());
                mViewModel.setPercentParcel(position, Integer.parseInt(toString().valueOf(seekBar.getProgress())));
                ((MixerListFragment)fragment).updateRefFragment();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                et.setText("" + seekBar.getProgress());
                mViewModel.setPercentParcel(position, Integer.parseInt(toString().valueOf(seekBar.getProgress())));
                ((MixerListFragment)fragment).updateRefFragment();
            }
        });

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mViewModel.setIsCheckedParcel(position, isChecked);
                ((MixerListFragment)fragment).updateRefFragment();
            }
        });

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MixerListFragment)fragment).deleteDialog(position);
            }
        });

        return view;
    }


}