package com.atomtex.spectrumgenerator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atomtex.spectrumgenerator.domain.EnergyLine;
import com.atomtex.spectrumgenerator.domain.NucIdent;
import com.atomtex.spectrumgenerator.domain.Nuclide;
import com.atomtex.spectrumgenerator.exception.ProcessException;
import com.atomtex.spectrumgenerator.util.SpectrumGenerator;

import java.util.List;

import static com.atomtex.spectrumgenerator.domain.NucIdent.BAD_INDEX;
import static com.atomtex.spectrumgenerator.domain.Nuclide.State.IDENTIFIED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SpectrumFragment fragment1;
    SpectrumFragment fragment2;

    public static final String TAG = "TAGGG!!!";

    boolean iCanGenerate;

    TextView spectrumTimeTV;
    TextView requiredTimeTV;

    float[] mPeakChannels;
    float[] mPeakEnergies;
    String[] mLineOwners;

    int spectrumTime;
    int requiredTime;

    FragmentManager manager;
    SpecDTO dto;

    int mode;
    String pathForAtsFile = "content://com.android.externalstorage.documents/document/primary%3ACs-137-UNI.ats";
    private int mPrefIdenThreshold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NucIdent.setNuclides(AllNuclidesList.getAllNuclides());

        //todo затычки
        dto = new SpecDTO();
        dto.setSpectrum(new int[]{0});
        dto.setMeasTim(new int[]{0});
        dto.setEnergy(new float[]{0});
        mPrefIdenThreshold = 4;

        spectrumTime = 5;
        requiredTime = 1;

        spectrumTimeTV = findViewById(R.id.spectrum_time);
        requiredTimeTV = findViewById(R.id.requiredTime);

        setSpectrumTime();
        setRequiredTime();

        mode = 0;
        findViewById(R.id.mode_button).setOnClickListener(this);
        findViewById(R.id.open_ats).setOnClickListener(this);
        findViewById(R.id.gen_button).setOnClickListener(this);
        findViewById(R.id.time_layout).setOnClickListener(this);

        manager = getSupportFragmentManager();
        /*manager.beginTransaction().replace(R.id.fragment_container1, createFragment("Эталонный спектр")).commit();
        manager.beginTransaction().replace(R.id.fragment_container2, createFragment("Сгенерированный спектр")).commit();*/
        if(fragment1==null){fragment1 = SpectrumFragment.newInstance(dto, mPeakChannels, mPeakEnergies, mLineOwners, "Эталонный спектр");
        manager.beginTransaction().replace(R.id.fragment_container1, fragment1).commit();}

        if(fragment2==null){fragment2 = SpectrumFragment.newInstance(dto, mPeakChannels, mPeakEnergies, mLineOwners, "Сгенерированный спектр");

        manager.beginTransaction().replace(R.id.fragment_container2, fragment2).commit();}

    }

/*    protected Fragment createFragment(String ID, SpecDTO dto) {
        return SpectrumFragment.newInstance(dto, mPeakChannels, mPeakEnergies, mLineOwners, ID);
    }*/

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
                if(iCanGenerate)generateSpectrum(pathForAtsFile, spectrumTime, requiredTime);
                else makeToast("Сначала загрузите файл .ats");
                break;
            case R.id.time_layout:
                showDialog();
                break;
        }
    }

    private String result;

    private void showDialog() {
        final View view = this.getLayoutInflater().inflate(R.layout.dialog, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(view);
        final EditText editText1 = view.findViewById(R.id.dialog_spectrum_time_text);
        final EditText editText2 = view.findViewById(R.id.dialog_required_time_text);
        editText1.setText(String.valueOf(spectrumTime));
//        editText1.setSelection(editText1.getText().length());
        editText2.setText(String.valueOf(requiredTime));
//        editText2.setSelection(editText2.getText().length());
        alert.setPositiveButton("Понял", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                spectrumTime = (Integer.parseInt(editText1.getText().toString()));
                requiredTime = (Integer.parseInt(editText2.getText().toString()));
                setSpectrumTime();
                setRequiredTime();
                dialog.cancel();
            }
        });
        alert.show();
    }

    private void setSpectrumTime() {
        spectrumTimeTV.setText(String.valueOf(spectrumTime));
    }

    private void setRequiredTime() {
        requiredTimeTV.setText(String.valueOf(requiredTime));
    }

    private void toggleMode() {
        switch (mode) {
            case 0:
                findViewById(R.id.fragment_container1).setVisibility(View.VISIBLE);
                findViewById(R.id.fragment_container2).setVisibility(View.GONE);
                mode = 1;
                break;
            case 1:
                findViewById(R.id.fragment_container1).setVisibility(View.GONE);
                findViewById(R.id.fragment_container2).setVisibility(View.VISIBLE);
                mode = 2;
                break;
            case 2:
                findViewById(R.id.fragment_container1).setVisibility(View.VISIBLE);
                findViewById(R.id.fragment_container2).setVisibility(View.VISIBLE);
                mode = 0;
                break;

        }
    }

/*    @Override
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
                        makeToast("Открытие...");
                    } else {
                        makeToast("Неверный формат");
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri uri = data.getData();
                    if (uri != null) {
                        String pathHolder = uri.toString();
                        pathForAtsFile = pathHolder;  //save ats path for specGenerator
                        Log.e(TAG, "PATH: = " + pathHolder);
                        if (pathHolder.endsWith(".ats")) {
                            openAtsFile(pathHolder);
                            makeToast("Открытие...");
                            iCanGenerate = true;
                        } else {
                            makeToast("Неверный формат");
                        }
                    }
                    break;
                case 2:
                    //...
                    break;
            }
//            updateUI();
        }
    }

/*    public void openWeightPicker() {
        DialogFragment fragment = new WeightDialogFragment();
        fragment.setTargetFragment(this, REQUEST_WEIGHT);
        fragment.show(getFragmentManager(), fragment.getClass().getName());
    }*/

    //todo вынести метод в отдельный класс (Controller)
    private void openAtsFile(String path) {
        SpecDTO dto = AtsReader.parseFile(path);
        if (dto != null) {
            int[] spectrum = dto.getSpectrum();
            Log.e(TAG, "run: " + dto.getSpectrum()[5]);
            float[] energy = dto.getEnergy();
            float[] sigma = dto.getSigma();
            NucIdent nuc = null;
            try {
                nuc = nuclidesIdent(spectrum.length, spectrum, sigma, energy, mPrefIdenThreshold);
            } catch (ProcessException e) {
                e.printStackTrace();
            }
            processIdenResult(nuc);
        }
        //todo сделать не через новый инстанс, а через модификацию уже существующего объекта
        manager.beginTransaction().replace(R.id.fragment_container1, SpectrumFragment.newInstance(dto, mPeakChannels, mPeakEnergies, mLineOwners, "Эталонный спектр")).commitAllowingStateLoss();
    }

    //todo remove path
    private void generateSpectrum(String path, int spTime, int rqTime) {
        SpecDTO dto = AtsReader.parseFile(path);
        SpectrumGenerator mSpectrumGenerator = new SpectrumGenerator();
        dto.setSpectrum(mSpectrumGenerator.generatedSpectrum(dto.getSpectrum(), spTime, rqTime));
        if (dto != null) {
            int[] spectrum = dto.getSpectrum();
            float[] energy = dto.getEnergy();
            float[] sigma = dto.getSigma();
            NucIdent nuc = null;
            try {
                nuc = nuclidesIdent(spectrum.length, spectrum, sigma, energy, mPrefIdenThreshold);
            } catch (ProcessException e) {
                e.printStackTrace();
            }
            processIdenResult(nuc);
        }
        //todo сделать не через новый инстанс, а через модификацию уже существующего объекта
        manager.beginTransaction().replace(R.id.fragment_container2, SpectrumFragment.newInstance(dto, mPeakChannels, mPeakEnergies, mLineOwners, "Сгенерированный спектр")).commitAllowingStateLoss();
    }

    private void processIdenResult(NucIdent nuc) {

        List<Nuclide> nuclides = NucIdent.getNuclides();
        Log.e(TAG, "NUCLIDES = " + nuclides.size());
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
        } else {
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
