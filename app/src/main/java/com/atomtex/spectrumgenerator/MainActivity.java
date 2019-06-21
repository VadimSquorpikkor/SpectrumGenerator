package com.atomtex.spectrumgenerator;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    String pathForAtsFile;
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
        if (1 == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                Uri uri = data.getData();
                if (uri != null) {
                    String pathHolder = uri.toString();
                    pathForAtsFile = pathHolder;  //save ats path for specGenerator
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
        NucIdent nuc;
//        Intent intentDTO = new Intent("ACTION_FILE_RECEIVED");
        SpecDTO dto = AtsReader.parseFile(path, getApplicationContext());
        if (dto != null) {
            int[] spectrum = dto.getSpectrum();
            float[] energy = dto.getEnergy();
            float[] sigma = dto.getSigma();
            Log.e(TAG, "openAtsFile: " + spectrum.length + ", " + energy.length + ", " + sigma.length);


/*           "EXTRA_PEAK_POSITIONS" == peakChannels;
            "EXTRA_PEAK_ENERGIES" == peakEnergies;
            "EXTRA_LINE_OWNERS" == lineOwners;*/


            float[] peaks = mPeakChannels;
            float[] peakEnergies = mPeakEnergies;
            String[] lineOwners = mLineOwners;
//            Intent spectrumIntent = SpectrumActivity.newIntent(mContext, dto, peaks, peakEnergies, lineOwners); //todo как было
//          startActivity(spectrumIntent);//todo как было



            /*FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag(tag);*/

            //            Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.container);

/*            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container1,createFragment("qqq")).addToBackStack(null).commit();*/

//            manager = getSupportFragmentManager();
//            manager.beginTransaction().replace(R.id.fragment_container1, createFragment(dto, peaks, peakEnergies, lineOwners, "Эталонный спектр")).commit();

/*            try {
                nuc = nuclidesIdent(spectrum.length,
                        spectrum, sigma
                        , energy, mPrefIdenThreshold);
            } catch (ProcessException e) {
                e.printStackTrace();
            }*/

/*            NucIdent nuc = null;
            try {
                nuc = nuclidesIdent(spectrum.length,
                        spectrum, sigma
                        , energy
                        , mPrefIdenThreshold
                );
            } catch (ProcessException e) {
                e.printStackTrace();
            }*/




//            processIdenResult(nuc, intentDTO);
        }
        else makeToast("Не удалось открыть файл");
    }

    private List<Nuclide> processIdenResult(NucIdent nuc, Intent intent) {

        List<Nuclide> nuclides = NucIdent.getNuclides();
        int numLines = nuc.getnLine();

        if (numLines > 0) {
            float[] peaks = nuc.getNiChannels();
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
            mPeakEnergies = peakEnergies;
            mLineOwners = lineOwners;
//            mGlasses.setPeaksData(peakChannels, peakEnergies);
        } else {
//            mGlasses.setPeaksData(null, null);
            mPeakChannels = null;
        }

        return nuclides;
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

        nuc.detectLines(threshold);

        nuc.makeNuclideIdentification(threshold);

        return nuc;
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
