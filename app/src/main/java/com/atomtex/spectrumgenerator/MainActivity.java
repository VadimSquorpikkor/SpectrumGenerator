package com.atomtex.spectrumgenerator;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "TAGGG!!!";

    FragmentManager manager;
    SpecDTO dto;
    float[] peaks;
    float[] peakEnergies;
    String[] lineOwners;
    int mode;
    String pathForAtsFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //todo затычки
        dto = new SpecDTO();
        dto.setSpectrum(new int[]{0,0,0});
        dto.setMeasTim(new int[]{0});
        dto.setEnergy(new float[]{0});

        mode = 0;
        findViewById(R.id.mode_button).setOnClickListener(this);

        manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container1, createFragment("Эталонный спектр")).commit();
            manager.beginTransaction().replace(R.id.fragment_container2, createFragment("Сгенерированный спектр")).commit();

    }




    protected Fragment createFragment() {
        return SpectrumFragment.newInstance(dto, peaks, peakEnergies, lineOwners);
    }

    protected Fragment createFragment(String ID) {
        return SpectrumFragment.newInstance(dto, peaks, peakEnergies, lineOwners, ID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mode_button:
                Log.e(TAG, "onClick: ");
                toggleMode();
                break;
            case R.id.open_ats:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 1);//TODO сделать psf
        }
    }

    private void toggleMode() {
        switch (mode) {
            case 0:
                findViewById(R.id.fragment_container1).setVisibility(View.VISIBLE);
                findViewById(R.id.fragment_container2).setVisibility(View.GONE);
                mode=1;
                break;
            case 1:
                findViewById(R.id.fragment_container1).setVisibility(View.GONE);
                findViewById(R.id.fragment_container2).setVisibility(View.VISIBLE);
                mode=2;
                break;
            case 2:
                findViewById(R.id.fragment_container1).setVisibility(View.VISIBLE);
                findViewById(R.id.fragment_container2).setVisibility(View.VISIBLE);
                mode=0;
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (1 == requestCode) { //TODO==============================
            if (Activity.RESULT_OK == resultCode) {
                Uri uri = data.getData();
                if (uri != null) {
                    String pathHolder = uri.toString();
                    pathForAtsFile = pathHolder;  //todo save ats path for specGenerator
                    if (pathHolder.endsWith(".ats")) {
                        openAtsFile(pathHolder);
//                        makeToast(getString(R.string.toast_opening));
                    } else {
//                        makeToast(getString(R.string.toast_wrong_type));
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openAtsFile(String pathHolder) {

    }

}
