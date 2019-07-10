package com.atomtex.spectrumgenerator;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.atomtex.spectrumgenerator.domain.EnergyLine;
import com.atomtex.spectrumgenerator.domain.NucIdent;
import com.atomtex.spectrumgenerator.domain.Nuclide;
import com.atomtex.spectrumgenerator.exception.ProcessException;
import com.atomtex.spectrumgenerator.util.SpectrumGenerator;

import java.util.List;

import static com.atomtex.spectrumgenerator.MainViewModel.GENERATED_SPECTRUM;
import static com.atomtex.spectrumgenerator.MainViewModel.REFERENCE_SPECTRUM;
import static com.atomtex.spectrumgenerator.domain.NucIdent.BAD_INDEX;
import static com.atomtex.spectrumgenerator.domain.Nuclide.State.IDENTIFIED;

/*  SpecDTO.class --> public void addSpectrumToCurrent(int[] spectrum) {*/

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    MainViewModel mViewModel;
    NavigationView navigationView;

    SpectrumFragment fragment1;//todo переместить в ViewModel
    SpectrumFragment fragment2;//todo переместить в ViewModel
//    Fragment fragment3;//todo переместить в ViewModel
    Fragment fragment4;//todo переместить в ViewModel

    public static final String TAG = "TAG!!!";

    TextView requiredTimeTV;
    TextView delayTimeTV;
    Button genButton;
    EditText timeText;
    EditText delayText;
    Switch switchTV;
    Switch drawerSwitch;

    float[] mPeakChannels;
    float[] mPeakEnergies;
    String[] mLineOwners;

    FragmentManager manager;
    Toggler toggler;

    //todo не понятно, для чего нужно
    private int mPrefIdenThreshold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        toggler = new Toggler(this, mViewModel);

        if (findViewById(R.id.nav_view) != null) {
            navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }

        NucIdent.setNuclides(AllNuclidesList.getAllNuclides());

        mPrefIdenThreshold = 3;

        toggler.setDisplayMode(mViewModel.getDisplayMode());
        toggler.setButtonMode(mViewModel.getButtonMode());
        toggler.setTimeLayoutMode(mViewModel.getTimeLayoutMode());

        requiredTimeTV = findViewById(R.id.requiredTime);
        delayTimeTV = findViewById(R.id.delay_time_main);
        genButton = findViewById(R.id.gen_button);
        timeText = findViewById(R.id.dialog_required_time_text);
        delayText = findViewById(R.id.dialog_delay_text);

        setRequiredTime();
        setDelayTime();

        ((TextView)findViewById(R.id.ref_spec_text)).setText(REFERENCE_SPECTRUM);

        findViewById(R.id.gen_button).setOnClickListener(this);
        findViewById(R.id.time_layout).setOnClickListener(this);
        findViewById(R.id.ref_spec_exp_more_button).setOnClickListener(this);
        findViewById(R.id.ref_spec_exp_less_button).setOnClickListener(this);

        final SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(mViewModel.getDelay());

        final SeekBar seekBarTime = findViewById(R.id.seekBar2);
        seekBarTime.setOnSeekBarChangeListener(this);
        seekBarTime.setProgress(mViewModel.getRequiredTime());

        toggler.setRefLayoutMode(0);

        switchTV = findViewById(R.id.switch1);
        if (switchTV != null) switchTV.setOnCheckedChangeListener(this);
        switchTV.setChecked(true);

        navigationView.getMenu().findItem(R.id.nav_switch).setActionView(new Switch(this));

        // To set whether switch is on/off use:
        ((Switch) navigationView.getMenu().findItem(R.id.nav_switch).getActionView()).setChecked(true);

        drawerSwitch = ((Switch) navigationView.getMenu().findItem(R.id.nav_switch).getActionView());

        manager = getSupportFragmentManager();
        //Это чтобы при повороте устройства не создавать новый фрагмент:
        fragment1 = (SpectrumFragment) manager.findFragmentById(R.id.fragment_container1);
        fragment2 = (SpectrumFragment) manager.findFragmentById(R.id.fragment_container2);
//        fragment3 = manager.findFragmentById(R.id.mixer_fragment);
        fragment4 = manager.findFragmentById(R.id.mixer_fragment_list_view);

        if (fragment1 == null) {
            fragment1 = SpectrumFragment.newInstance(REFERENCE_SPECTRUM);
            manager.beginTransaction().replace(R.id.fragment_container1, fragment1).commit();
        }

        if (fragment2 == null) {
            fragment2 = SpectrumFragment.newInstance(GENERATED_SPECTRUM);
            manager.beginTransaction().replace(R.id.fragment_container2, fragment2).commit();
        }

/*        if (fragment3 == null) {
            fragment3 = MixerFragment.newInstance();
            manager.beginTransaction().replace(R.id.mixer_fragment, fragment3).commit();
        }*/

        if (fragment4 == null) {
            fragment4 = MixerListFragment.newInstance();
            manager.beginTransaction().replace(R.id.mixer_fragment_list_view, fragment4).commit();
        }

//        ((MixerFragment) fragment3).updateText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gen_button: toggleGenButton(); break;
            case R.id.time_layout: toggleTimeLayoutMode(); break;
            case R.id.ref_spec_exp_less_button: toggler.setRefLayoutMode(1); break;
            case R.id.ref_spec_exp_more_button: toggler.setRefLayoutMode(0); break;
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId()==R.id.switch1) {
            if (isChecked) mViewModel.setSecMode(true);
            else mViewModel.setSecMode(false);
        }
        if(buttonView.getId()==R.id.drawer_switch){
            if (isChecked) toggler.setButtonMode(1);
            else toggler.setButtonMode(0);
        }
    }

    public void openAts(int num) {
        Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().toString());
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(selectedUri, "*/*");
        startActivityForResult(intent, num);//TODO request code сделать psf
    }

    private void gen() {
        if (iCanGenerate()) generateSpectrum(mViewModel.getPathForAts(), mViewModel.getSpectrumTime(), mViewModel.getRequiredTime());
        else makeToast("Сначала загрузите файл .ats");
    }

    private void genMixer() {
        if (iCanGenerate()){
            getParamFromTimeField();
            getDtoFromPath();
            mViewModel.getTempDTO().setSpectrum(new int[mViewModel.getTempDTO().getSpectrum().length]);
            startMixer();
        }
        else makeToast("Сначала загрузите файл .ats");
    }

    public boolean iCanGenerate() {
        return !TextUtils.isEmpty(mViewModel.getPathForAts());// if not empty or not null
    }

    public void getParamFromTimeField() {
        mViewModel.setRequiredTime(Integer.parseInt(timeText.getText().toString()));
        mViewModel.setDelay(Integer.parseInt(delayText.getText().toString()));
    }

    private void setRequiredTime() {
        int time = mViewModel.getRequiredTime();
        requiredTimeTV.setText(String.valueOf(time));//todo может есть смысл хранить в стринге?
        timeText.setText(String.valueOf(time));
    }

    private void setDelayTime() {
        int delay = mViewModel.getDelay();
        delayTimeTV.setText(String.valueOf(delay));
        delayText.setText(String.valueOf(delay));
    }

    public void toggleTimeLayoutMode() {
        getParamFromTimeField();
        setDelayTime();
        setRequiredTime();

        int mode = 0;
        if (mViewModel.getTimeLayoutMode() == 0) mode = 1;
        mViewModel.setTimeLayoutMode(mode);
        toggler.setTimeLayoutMode(mode);
    }


    private void toggleButtonMode() {
        int mode = 0;
        if (mViewModel.getButtonMode() == 0) mode = 1;
        mViewModel.setButtonMode(mode);
        toggler.setButtonMode(mode);
    }

    private void toggleGenButton() {
        if (mViewModel.isSecMode() && iCanGenerate()){
            if (mViewModel.isGenButtonIsPressed()) {
                stop();
                genButton.setText("Генератор");
                mViewModel.setGenButtonIsPressed(false);
            } else {
                genMixer();
//                genButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.my_button_shape_pressed,0, 0, 0);
//                genButton.setBackgroundColor(getResources().getColor(R.color.colorOrange));
//                genButton.setBackgroundDrawable(R.drawable.my_button_shape_pressed);
                genButton.setText("Остановить");
                mViewModel.setGenButtonIsPressed(true);
            }
        } else gen();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        String pathHolder = uri.toString();
                        Log.e(TAG, "PATH: = " + pathHolder);
                        if (pathHolder.endsWith(".ats")) {
                            mViewModel.setPathForAts(pathHolder); //save ats path for specGenerator
                            openAtsFile(requestCode, pathHolder);
                            makeToast("Открытие...");
                        } else {
                            makeToast("Неверный формат");
                        }
                    }
        }
    }

    private String makeName(String path) {
        String name;
        name = path.substring(path.length() - 10);
        return name;
    }


    //todo вынести метод в отдельный класс (Controller)
    private void openAtsFile(int pos, String path) {
//        mViewModel.addName(pos, makeName(path));
        SpecDTO dto = AtsReader.parseFile(path);
//        mViewModel.setReferenceDTO(dto);//todo
//        mViewModel.addDtoArr(pos, dto);
        mViewModel.addNewSpectrum(dto, makeName(path));
//        ((MixerFragment) fragment3).updateText();
        ((MixerListFragment) fragment4).updateAdapter();//чтобы во фрагменте появился item
        Log.e(TAG, "-------------openAtsFile: " + mViewModel.getDtoArr()[0] + ", " + mViewModel.getDtoArr()[1]);

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
        ((TextView)findViewById(R.id.ref_spec_text)).setText(REFERENCE_SPECTRUM);
        mViewModel.setSpectrumTime(dto.getMeasTim()[0]);//todo потом убрать, когда везде сделаю через getMeas[0]
        manager.beginTransaction().replace(R.id.fragment_container1, SpectrumFragment.newInstance(dto, mPeakChannels, mPeakEnergies, mLineOwners, REFERENCE_SPECTRUM, path)).commitAllowingStateLoss();

    }

    //todo remove path
    private void generateSpectrum(String path, int spTime, int rqTime) {
        SpecDTO dto = AtsReader.parseFile(path);
//        SpecDTO dto = mViewModel.getReferenceDTO();
        dto.setMeasTim(new int[]{rqTime, 1});//todo здесь вторая переменная перезаписывается, можно сделать, чтобы сохранялась, но пока не надо -- она все равно всегда 0
        SpectrumGenerator mSpectrumGenerator = new SpectrumGenerator();
        dto.setSpectrum(mSpectrumGenerator.generatedSpectrum(dto.getSpectrum(), spTime, rqTime));
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
        manager.beginTransaction().replace(R.id.fragment_container2, SpectrumFragment.newInstance(dto, mPeakChannels, mPeakEnergies, mLineOwners, "Сгенерированный спектр")).commitAllowingStateLoss();
    }


    void getDtoFromPath() {//todo масло маслянное...
        mViewModel.setTempDTO(AtsReader.parseFile(mViewModel.getPathForAts()));
    }
    //todo remove path
    private void generateSpectrumTeak(SpecDTO refDto, SpecDTO tempDto, int spTime, int rqTime, int count) {
//        SpecDTO refDto = AtsReader.parseFile(mViewModel.getPathForAts());
        //        SpecDTO refDto = mViewModel.getReferenceDTO();//  :(((
//        SpecDTO tempDto = mViewModel.getTempDTO();
        tempDto.setMeasTim(new int[]{count, 1});//todo здесь вторая переменная перезаписывается, можно сделать, чтобы сохранялась, но пока не надо -- она все равно всегда 0
        SpectrumGenerator mSpectrumGenerator = new SpectrumGenerator();
//        dto.addSpectrumToCurrent(mSpectrumGenerator.generatedSpectrum(refDto.getSpectrum(), spTime, rqTime));
        tempDto.addSpectrumToCurrent(mSpectrumGenerator.generatedSpectrum(refDto.getSpectrum(), spTime, rqTime));
        int[] spectrum = tempDto.getSpectrum();
        float[] energy = tempDto.getEnergy();
        float[] sigma = tempDto.getSigma();
        NucIdent nuc = null;
        try {
            nuc = nuclidesIdent(spectrum.length, spectrum, sigma, energy, mPrefIdenThreshold);
        } catch (ProcessException e) {
            e.printStackTrace();
        }
        processIdenResult(nuc);
        //todo сделать не через новый инстанс, а через модификацию уже существующего объекта
        manager.beginTransaction().replace(R.id.fragment_container2, SpectrumFragment.newInstance(tempDto, mPeakChannels, mPeakEnergies, mLineOwners, "Сгенерированный спектр")).commitAllowingStateLoss();
//        fragment2.setNewValues(dto, mPeakChannels, mPeakEnergies, mLineOwners);
//        manager.beginTransaction().detach(fragment2).attach(fragment2).commitAllowingStateLoss();
//        manager.beginTransaction().replace(R.id.fragment_container1, fragment1).commitAllowingStateLoss();

    }

    private void processIdenResult(NucIdent nuc) {

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

        nuc.detectLines(threshold);

        nuc.makeNuclideIdentification(threshold);

        return nuc;
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_open_ats) {
//            openAts();
        } else if (id == R.id.nav_generate) {
            gen();
        } else if (id == R.id.nav_mixer) {
            manager.beginTransaction().replace(R.id.fragment_container1, MixerFragment.newInstance()).commit();

/*            FrameLayout fl = findViewById(R.id.fragment_container1);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) fl.getLayoutParams();
            params.weight = 2;*/
        } else if (id == R.id.nav_toggle_mode) {
            toggler.toggleMode();
        } else if (id == R.id.nav_ref_spec) {

            fragment1 = SpectrumFragment.newInstance(REFERENCE_SPECTRUM);
            manager.beginTransaction().replace(R.id.fragment_container1, fragment1).commit();

/*            FrameLayout fl = findViewById(R.id.fragment_container1);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) fl.getLayoutParams();
            params.weight = 1;*/

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    final Handler handler = new Handler();
    Runnable runnable;

    void start(){
            handler.postDelayed(runnable = new Runnable() {
                int count = 0;
                public void run() {
                    if (count++ < mViewModel.getRequiredTime()) {
                        generateSpectrumTeak(AtsReader.parseFile(mViewModel.getPathForAts()), mViewModel.getTempDTO(), mViewModel.getSpectrumTime(), 1, count);
                        handler.postDelayed(runnable, mViewModel.getDelay());
                    } else {
                        genButton.setText("Генератор");
                        mViewModel.setGenButtonIsPressed(false);
                    }
                }
            }, 1);

    }

    void startMixer(final SpecDTO[] dtoList){
        handler.postDelayed(runnable = new Runnable() {
            int count = 0;
            public void run() {
                if (count++ < mViewModel.getRequiredTime()) {
                    for (SpecDTO dto:dtoList) {
                        if(dto!=null) generateSpectrumTeak(dto, mViewModel.getTempDTO(), dto.getMeasTim()[0], 1, count);
                    }
                    handler.postDelayed(runnable, mViewModel.getDelay());
                } else {
                    genButton.setText("Генератор");
                    mViewModel.setGenButtonIsPressed(false);
                }
            }
        }, 1);

    }

    void startMixer(){
        handler.postDelayed(runnable = new Runnable() {
            int count = 0;
            SpecDTO dto;
            public void run() {
                if (count++ < mViewModel.getRequiredTime()) {
                    /*for (int i = 0; i<mViewModel.getSourcesItemsCount(); i++) {
                      if(mViewModel.getDtoArr()[i]!=null&&mViewModel.getIsChecked()[i]) {dto = mViewModel.getDtoArr()[i];//&&parcelList.get(i).isChecked()
                      generateSpectrumTeak(dto, mViewModel.getTempDTO(), dto.getMeasTim()[0] * 100 / mViewModel.getPercentArr()[i] , 1, count);} //* (101-mViewModel.getPercentArr()[i])
                    }*/

                    for (SpecMixerParcel parcel:mViewModel.getSourceList()) {
                        if(parcel.isChecked()) {
                            dto = parcel.getReferenceDTO();
                            generateSpectrumTeak(dto, mViewModel.getTempDTO(), dto.getMeasTim()[0] * 100 / parcel.getPercent(), 1, count);
                        }
                        }



                    handler.postDelayed(runnable, mViewModel.getDelay());
                } else {
                    genButton.setText("Генератор");
                    mViewModel.setGenButtonIsPressed(false);
                }
            }
        }, 1);

    }


    void stop() {
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(seekBar.getId()==R.id.seekBar) delayText.setText(String.valueOf(seekBar.getProgress()));
        if(seekBar.getId()==R.id.seekBar2) timeText.setText(String.valueOf(seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar.getId()==R.id.seekBar) mViewModel.setDelay(Integer.parseInt(String.valueOf(seekBar.getProgress())));
        if(seekBar.getId()==R.id.seekBar2) mViewModel.setRequiredTime(Integer.parseInt(String.valueOf(seekBar.getProgress())));
    }



}
