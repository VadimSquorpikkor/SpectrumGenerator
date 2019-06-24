package com.atomtex.spectrumgenerator;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atomtex.spectrumgenerator.domain.EnergyLine;
import com.atomtex.spectrumgenerator.domain.NucIdent;
import com.atomtex.spectrumgenerator.domain.Nuclide;
import com.atomtex.spectrumgenerator.exception.ProcessException;

import java.util.List;

import static com.atomtex.spectrumgenerator.domain.NucIdent.BAD_INDEX;
import static com.atomtex.spectrumgenerator.domain.Nuclide.State.IDENTIFIED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "TAGGG!!!";

    float[] mPeakChannels;
    float[] mPeakEnergies;
    String[] mLineOwners;


    FragmentManager manager;
    SpecDTO dto;
    float[] peaks;
    float[] peakEnergies;
    String[] lineOwners;
    int mode;
    String pathForAtsFile = "content://com.android.externalstorage.documents/document/primary%3ACs-137-UNI.ats";
    private int mPrefIdenThreshold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //todo затычки
        dto = new SpecDTO();
        dto.setSpectrum(new int[]{0,0,0});
        dto.setMeasTim(new int[]{0});
        dto.setEnergy(new float[]{0});
        mPrefIdenThreshold = 4;

        mode = 0;
        findViewById(R.id.mode_button).setOnClickListener(this);
        findViewById(R.id.open_ats).setOnClickListener(this);
        findViewById(R.id.gen_button).setOnClickListener(this);

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

    protected Fragment createFragment(SpecDTO mdto, float[] mpeaks, float[] mpeakEnergies, String[] mlineOwners, String mID) {
        return SpectrumFragment.newInstance(mdto, mpeaks, mpeakEnergies, mlineOwners, mID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mode_button:
                Log.e(TAG, "onClick: ");
                toggleMode();
                break;
            case R.id.open_ats:
                Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().toString());
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(selectedUri, "*/*");
                startActivityForResult(intent, 1);//TODO request code сделать psf
                break;
            case R.id.gen_button:
                SpectrumFragment fragment = ((SpectrumFragment) manager.getFragments().get(0));
//                fragment.updateID("New ID!!!");
                SpecDTO dto = AtsReader.parseFile(pathForAtsFile, getApplicationContext());
                fragment.updateData(dto);
                manager.beginTransaction().detach(fragment).commitAllowingStateLoss();
                manager.beginTransaction().attach(fragment).commitAllowingStateLoss();
                Log.e(TAG, "onClick: " + manager.getFragments().size());
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

    public SpecDTO dtoFromAts() {
        return AtsReader.parseFile("content://com.android.externalstorage.documents/document/primary%3ACs-137-UNI.ats", getApplicationContext());

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (1 == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                Uri uri = data.getData();
                if (uri != null) {
                    String pathHolder = uri.toString();
                    pathForAtsFile = pathHolder;  //save ats path for specGenerator
                    Log.e(TAG, "PATH: = " + pathHolder);
                    if (pathHolder.endsWith(".ats")) {
                        openAtsFile(pathHolder);
//                        Toast.makeText(this, "WWWorks!!!", Toast.LENGTH_SHORT).show();
                        makeToast("Открытие...");
                    } else {
                        makeToast("Неверный формат");
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //todo вынести метод в отдельный класс (Controller)
    private void openAtsFile(String path) {
        SpecDTO dto = AtsReader.parseFile(path, getApplicationContext());
        if (dto != null) {
            try { processIdenResult(nuclidesIdent(dto.getSpectrum().length, dto.getSpectrum(), dto.getSigma(), dto.getEnergy(), mPrefIdenThreshold));
            } catch (ProcessException e) { e.printStackTrace(); Log.e(TAG, "openAtsFile: EXCEPTION"); }
        }
        else makeToast("Не удалось открыть файл");

//        ((SpectrumFragment) manager.getFragments().get(0)).updateData(dto, peaks, peakEnergies, lineOwners);//todo кроме dto всё пустое и не правильное, пока как затычка
        ((SpectrumFragment) manager.getFragments().get(0)).updateData(dto/*, mPeakChannels, mPeakEnergies, mLineOwners*/);//todo кроме dto всё пустое и не правильное, пока как затычка
        SpectrumFragment fragment = ((SpectrumFragment) manager.getFragments().get(0));
        manager.beginTransaction().detach(fragment).commitAllowingStateLoss();
        manager.beginTransaction().attach(fragment).commitAllowingStateLoss();
    }

    private void processIdenResult(NucIdent nuc) {

        Log.e(TAG, "processIdenResult: STARTED ");
        List<Nuclide> nuclides = NucIdent.getNuclides();
        int numLines = nuc.getnLine();

        Log.e(TAG, "processIdenResult: NUMLINES = " + numLines);

        if (numLines > 0) {
            float[] peaks = nuc.getNiChannels();
            Log.e(TAG, "processIdenResult: PEAKS" + peaks.length);
            int[] energies = nuc.getEnergies();
            float[] peakChannels = new float[numLines];
            float[] peakEnergies = new float[numLines];
            String[] lineOwners = new String[numLines];

            int count = 0;

            for (int i = 0; i < peaks.length; i++) {
                if (peaks[i] > 0) {
                    peakChannels[count] = peaks[i];
                    peakEnergies[count] = energies[i];
                    count++;
                }
            }


            for (Nuclide nuclide : nuclides) {
                if (nuclide.getState() == IDENTIFIED) {
                    EnergyLine[] lines = nuclide.getEnergyLines();
                    for (EnergyLine line : lines) {
                        int index = line.getIndex();
                        if (index != BAD_INDEX
                                && line.getFactorsNoShield() > 0
                                && line.getFactorsShield() > 0) {
                            lineOwners[index] = nuclide.getName() + " " + nuclide.getNumStr();
                        }
                    }
                }
            }
            mPeakChannels = peakChannels;
            Log.e(TAG, "processIdenResult: " + peakChannels.length);
            mPeakEnergies = peakEnergies;
            mLineOwners = lineOwners;
//            mGlasses.setPeaksData(peakChannels, peakEnergies);
        } else {
//            mGlasses.setPeaksData(null, null);
            mPeakChannels = null;
        }

//        return nuclides;
    }

    //todo вынести метод в отдельный класс (Controller)
    public NucIdent nuclidesIdent(int channelNumber, int[] spectrum,  //todo was private
                                  float[] sigma, float[] energy, int threshold) throws ProcessException {

        NucIdent nuc = new NucIdent(channelNumber,
                16, 20, 32, 24,
                20, 20, 0, true);

        nuc.setSpectrum(spectrum);
        nuc.setSpectrumSigma(sigma);
        nuc.setSpectrumEnergy(energy);

//        nuc.detectLines(threshold);

//        nuc.makeNuclideIdentification(threshold);

        return nuc;
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
