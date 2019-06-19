package com.atomtex.spectrumgenerator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FragmentManager manager;
    SpecDTO dto;
    float[] peaks;
    float[] peakEnergies;
    String[] lineOwners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //todo затычки
        dto = new SpecDTO();
        dto.setSpectrum(new int[]{0,0,0});
        dto.setMeasTim(new int[]{0});
        dto.setEnergy(new float[]{0});

        manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container1, createFragment()).commit();
            manager.beginTransaction().replace(R.id.fragment_container2, createFragment()).commit();
    }


    protected Fragment createFragment() {
        return SpectrumFragment.newInstance(dto, peaks, peakEnergies, lineOwners);
    }

    @Override
    public void onClick(View v) {

    }
}
