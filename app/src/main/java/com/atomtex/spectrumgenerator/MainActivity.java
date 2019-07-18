package com.atomtex.spectrumgenerator;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import static com.atomtex.spectrumgenerator.MainViewModel.GENERATED_SPECTRUM;
import static com.atomtex.spectrumgenerator.MainViewModel.REFERENCE_SPECTRUM;
import static com.atomtex.spectrumgenerator.domain.NucIdent.BAD_INDEX;
import static com.atomtex.spectrumgenerator.domain.Nuclide.State.IDENTIFIED;

/*  SpecDTO.class --> public void addSpectrumToCurrent(int[] spectrum) {*/

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    MainViewModel mViewModel;
    NavigationView navigationView;

    Fragment fragment4;//todo (переместить в ViewModel) вообще хранить фрагменты в mainView -- плохая идея, надо вернуть фрагменты 1 и 2 обратно

    public static final String TAG = "TAG!!!";
    public static final String SHARED_PREFFERENCES = "sPref";

    TextView requiredTimeTV;
    TextView delayTimeTV;
    Button genButton;
    EditText timeText;
    EditText delayText;

    float[] mPeakChannels;
    float[] mPeakEnergies;
    String[] mLineOwners;

    String nameForMixer = "";

    FragmentManager manager;
    Toggler toggler;

    SaveLoad saveLoad;

    private int mPrefIdenThreshold;//todo не понятно, для чего нужно

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveLoad = new SaveLoad(this);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        toggler = new Toggler(this, mViewModel);

        //если приложение только запустилось
        if (mViewModel.isFirstTime) loadVars();

        if (findViewById(R.id.nav_view) != null) {
            navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }

        NucIdent.setNuclides(AllNuclidesList.getAllNuclides());

        mPrefIdenThreshold = 3;

        toggler.setTimeLayoutMode(mViewModel.getTimeLayoutMode());
        toggler.setAllVisibilities();
        toggler.setMixerLayoutMode(mViewModel.getMixerMode());

        requiredTimeTV = findViewById(R.id.requiredTime);
        delayTimeTV = findViewById(R.id.delay_time_main);
        genButton = findViewById(R.id.gen_button);
        timeText = findViewById(R.id.dialog_required_time_text);
        delayText = findViewById(R.id.dialog_delay_text);

        setRequiredTime();
        setDelayTime();

//        ((TextView)findViewById(R.id.ref_spec_text)).setText(REFERENCE_SPECTRUM);

        findViewById(R.id.gen_button).setOnClickListener(this);
        findViewById(R.id.time_layout).setOnClickListener(this);
        findViewById(R.id.mixer_fragment_list_view).setOnClickListener(this);
        findViewById(R.id.hide_time_button).setOnClickListener(this);
        findViewById(R.id.add_new_spectrum_2).setOnClickListener(this);

        final SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(mViewModel.getDelay());

        final SeekBar seekBarTime = findViewById(R.id.seekBar2);
        seekBarTime.setOnSeekBarChangeListener(this);
        seekBarTime.setProgress(mViewModel.getRequiredTime());

        navigationView.getMenu().findItem(R.id.nav_switch_1).setActionView(new Switch(this));
        navigationView.getMenu().findItem(R.id.nav_switch_2).setActionView(new Switch(this));
        navigationView.getMenu().findItem(R.id.nav_switch_3).setActionView(new Switch(this));
        navigationView.getMenu().findItem(R.id.nav_switch_4).setActionView(new Switch(this));
//        navigationView.getMenu().findItem(R.id.nav_switch_5).setActionView(new Switch(this));

        ((Switch) findViewById(R.id.switch1)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) mViewModel.setSecMode(true);
                else mViewModel.setSecMode(false);
            }
        });
        ((Switch) findViewById(R.id.switch1)).setChecked(true);

        ((Switch) navigationView.getMenu().findItem(R.id.nav_switch_1).getActionView()).setOnCheckedChangeListener(this);
        ((Switch) navigationView.getMenu().findItem(R.id.nav_switch_2).getActionView()).setOnCheckedChangeListener(this);
        ((Switch) navigationView.getMenu().findItem(R.id.nav_switch_3).getActionView()).setOnCheckedChangeListener(this);
        ((Switch) navigationView.getMenu().findItem(R.id.nav_switch_4).getActionView()).setOnCheckedChangeListener(this);
//        ((Switch) navigationView.getMenu().findItem(R.id.nav_switch_5).getActionView()).setOnCheckedChangeListener(this);

        ((Switch) navigationView.getMenu().findItem(R.id.nav_switch_1).getActionView()).setChecked(mViewModel.isRefVisible());
        ((Switch) navigationView.getMenu().findItem(R.id.nav_switch_2).getActionView()).setChecked(mViewModel.isMixerVisible());
        ((Switch) navigationView.getMenu().findItem(R.id.nav_switch_3).getActionView()).setChecked(mViewModel.isTimeVisible());
        ((Switch) navigationView.getMenu().findItem(R.id.nav_switch_4).getActionView()).setChecked(mViewModel.isButtonVisible());
//        ((Switch) navigationView.getMenu().findItem(R.id.nav_switch_5).getActionView()).setChecked(mViewModel.isMatrixVisible());

        manager = getSupportFragmentManager();
        fragment4 = manager.findFragmentById(R.id.mixer_fragment_list_view_big);

        if (mViewModel.getReferenceFragment() == null) {
            mViewModel.setReferenceFragment(SpectrumFragment.newInstance(REFERENCE_SPECTRUM));
            manager.beginTransaction().replace(R.id.fragment_container1, mViewModel.getReferenceFragment()).commit();

        } else mViewModel.setReferenceFragment((SpectrumFragment) manager.findFragmentById(R.id.fragment_container1));

        if (mViewModel.getGeneratedFragment() == null) {
            mViewModel.setGeneratedFragment(SpectrumFragment.newInstance(GENERATED_SPECTRUM));
            manager.beginTransaction().replace(R.id.fragment_container2, mViewModel.getGeneratedFragment()).commit();
        } else  mViewModel.setGeneratedFragment((SpectrumFragment) manager.findFragmentById(R.id.fragment_container2));

        if (fragment4 == null) {
            fragment4 = MixerListFragment.newInstance();
            manager.beginTransaction().replace(R.id.mixer_fragment_list_view_big, fragment4).commit();
        }
    }

    private void loadVars() {
        ArrayList<Boolean> b = saveLoad.loadBooleanArray(SHARED_PREFFERENCES);
        if (b.size() > 0) {
            mViewModel.setRefVisibility(b.get(0));
            mViewModel.setMixerVisibility(b.get(1));
            mViewModel.setTimeVisibility(b.get(2));
            mViewModel.setButtonVisibility(b.get(3));
            mViewModel.setMatrixVisibility(b.get(4));
        }
    }

    private void saveVars() {
        ArrayList<Boolean> list = new ArrayList<>();
        list.add(mViewModel.isRefVisible());
        list.add(mViewModel.isMixerVisible());
        list.add(mViewModel.isTimeVisible());
        list.add(mViewModel.isButtonVisible());
        list.add(mViewModel.isMatrixVisible());
        saveLoad.saveBooleanArray(list, SHARED_PREFFERENCES);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gen_button: toggleGenButton(); break;
            case R.id.time_layout: toggler.setTimeLayoutMode(1); break;
            case R.id.hide_time_button: hideTime(); break;
            case R.id.mixer_fragment_list_view: toggler.setMixerLayoutMode(0); break;
            case R.id.add_new_spectrum_2: openFile(); break;
        }
    }

    public void hideMixer() {
        toggler.setMixerLayoutMode(1);
    }

    private void hideTime() {
        toggler.setTimeLayoutMode(0);
        mViewModel.setDelay(Integer.parseInt(delayText.getText().toString()));
        delayTimeTV.setText(delayText.getText());
        mViewModel.setRequiredTime(Integer.parseInt(timeText.getText().toString()));
        requiredTimeTV.setText(timeText.getText());
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.nav_switch_1) toggler.setReferenceFragmentMode(isChecked);
        if (buttonView.getId() == R.id.nav_switch_2) toggler.setMixerMode(isChecked);
        if (buttonView.getId() == R.id.nav_switch_3) toggler.setTimeMode(isChecked);
        if (buttonView.getId() == R.id.nav_switch_4) toggler.setButtonsMode(isChecked);
//        if(buttonView.getId()==R.id.nav_switch_5) toggler.setMatrixMode(isChecked);
        saveVars();
    }

    public void openFile() {
        Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().toString());
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(selectedUri, "*/*");
        startActivityForResult(intent, 1);//TODO request code сделать psf
    }

    private void gen() {
        if (iCanGenerate()) {
            mViewModel.getTempDTO().setSpectrum(new int[mViewModel.getTempDTO().getSpectrum().length]);//todo ??????
            startQuickMixer();
        } else makeToast("Сначала загрузите файл спектра");
    }

    private void genMixer() {
        if (iCanGenerate()) {
            mViewModel.getTempDTO().setSpectrum(new int[mViewModel.getTempDTO().getSpectrum().length]);//todo ??????
            startMixer();
        } else makeToast("Сначала загрузите файл спектра");
    }

    public boolean iCanGenerate() {
//        return !TextUtils.isEmpty(mViewModel.getPathForAts());// if not empty or not null
        return mViewModel.getSourceList().size() > 0;
    }

    private void setRequiredTime() {
        requiredTimeTV.setText(String.valueOf(mViewModel.getRequiredTime()));
        timeText.setText(String.valueOf(mViewModel.getRequiredTime()));
    }

    private void setDelayTime() {
        delayTimeTV.setText(String.valueOf(mViewModel.getDelay()));
        delayText.setText(String.valueOf(mViewModel.getDelay()));
    }

    private void toggleGenButton() {
        if (mViewModel.isSecMode() && iCanGenerate()) {
            if (mViewModel.isGenButtonIsPressed()) {
                stop();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Drawable drawable = getDrawable(R.drawable.button_shape_selector);
                    genButton.setBackground(drawable);
                }
                genButton.setText("Генератор");
                mViewModel.setGenButtonIsPressed(false);
            } else {
                genMixer();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Drawable drawable = getDrawable(R.drawable.my_button_shape_pressed);
                    genButton.setBackground(drawable);
                }
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
                    mViewModel.setTempDTO(AtsReader.parseFile(pathHolder));
                    openAtsFile(pathHolder);
                    makeToast("Открытие...");
                } else if (pathHolder.endsWith(".spe")) {
                    mViewModel.setPathForAts(pathHolder); //save ats path for specGenerator
                    mViewModel.setTempDTO(SpeReader.parseFile(pathHolder));
                    openSpeFile(pathHolder);
                    makeToast("Открытие...");
                } else {
                    makeToast("Неверный формат");
                }
            }
        }
    }

    private String makeName() {
        String name = "Unknown";
//        name = path.substring(path.length() - 10);
        if (!nameForMixer.equals("")) name = nameForMixer;
        return name;
    }


    //todo вынести метод в отдельный класс (Controller)
    private void openAtsFile(String path) {//todo убрать pos

        nameForMixer = ""; //сброс
        SpecDTO dto = AtsReader.parseFile(path);
        ((MixerListFragment) fragment4).updateAdapter();//чтобы во фрагменте появился item

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

        mViewModel.addNewSpectrum(dto, makeName());
        nameForMixer = ""; //сброс
        mViewModel.setSpectrumTime(dto.getMeasTim()[0]);//todo потом убрать, когда везде сделаю через getMeas[0]
        preferenceMixer();
        //////////////////////////////manager.beginTransaction().replace(R.id.fragment_container1, SpectrumFragment.newInstance(dto, mPeakChannels, mPeakEnergies, mLineOwners, REFERENCE_SPECTRUM, path)).commitAllowingStateLoss();
    }

    public void identificateNucl(SpecDTO dto) {
//        dto.setMeasTim(new int[]{count, 1});
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

    }

    //todo объединить с ats
    private void openSpeFile(String path) {//todo убрать pos
        nameForMixer = ""; //сброс
        SpecDTO dto = SpeReader.parseFile(path);
        ((MixerListFragment) fragment4).updateAdapter();//чтобы во фрагменте появился item

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
        mViewModel.addNewSpectrum(dto, makeName());
        nameForMixer = ""; //сброс
//        ((TextView)findViewById(R.id.ref_spec_text)).setText(REFERENCE_SPECTRUM);
        mViewModel.setSpectrumTime(dto.getMeasTim()[0]);//todo потом убрать, когда везде сделаю через getMeas[0]
        preferenceMixer();
        //////////////////////manager.beginTransaction().replace(R.id.fragment_container1, SpectrumFragment.newInstance(dto, mPeakChannels, mPeakEnergies, mLineOwners, REFERENCE_SPECTRUM, path)).commitAllowingStateLoss();

    }

    //todo remove path
    private void generateSpectrumTeak(SpecDTO refDto, SpecDTO tempDto, int spTime, int rqTime, int count, boolean isDrawable) {
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


        if (isDrawable) {
            mViewModel.getGeneratedFragment().setNewValues(tempDto, mPeakChannels, mPeakEnergies, mLineOwners);
            mViewModel.getGeneratedFragment().update();
        }


    }

    private void processIdenResult(NucIdent nuc) {

        List<Nuclide> nuclides = NucIdent.getNuclides();
        int numLines = nuc.getnLine();
//        int numLines = 5;

        Log.e(TAG, "processIdenResult: NUM_LINES = " + numLines);

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
                            if (nameForMixer.equals("")) nameForMixer = lineOwners[index];
                        }
                    }
                }
            }
            mPeakChannels = peakChannels;
            mPeakEnergies = peakEnergies;
            mLineOwners = lineOwners;
            Log.e(TAG, "identMixer: ch = " + mPeakChannels.length + ", ener = " + mPeakEnergies.length + ", lines = " + mLineOwners.length);
        } else {
            mPeakChannels = null;//todo было включено, но с включенной не определяется нуклид в методе identificateNucl, но определяется при генерации. нужно разобраться  и сделать так, чтобы работало при включенной
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
            openFile();
        } else if (id == R.id.nav_generate) {
            toggleGenButton();
        } /*else if (id == R.id.nav_switch_5) {

        }*/ else if (id == R.id.nav_iden) {
            preferenceMixer();
//            identificateNucl(mViewModel.getEmptyDto());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    final Handler handler = new Handler();
    Runnable runnable;

    void startMixer() {
//        final int cnt;
//        if(mViewModel.getSourceList().size()==0)cnt=mViewModel.getRequiredTime()-1;
//        else cnt = 0;
        handler.postDelayed(runnable = new Runnable() {
            int count = 0;//cnt;

            public void run() {
                if (count++ < mViewModel.getRequiredTime()) {
//                    mixerPreTeak(count, 1, true);
                    mixerPreTeak2(count, 1, true);
                    handler.postDelayed(runnable, mViewModel.getDelay());
                } else {
//                    mViewModel.setTempDTO(new SpecDTO(1024));
                    genButton.setText("Генератор");
                    mViewModel.setGenButtonIsPressed(false);
//                    mViewModel.setTempDTO(AtsReader.parseFile(mViewModel.getPathForAts()));
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        Drawable drawable = getDrawable(R.drawable.button_shape_selector);
                        genButton.setBackground(drawable);
                    }
                }
            }
        }, 1);

    }

    //старая версия для раздельного складывания спектров -- в новой версии складываются доли уже сложенного спектра
    /*private void mixerPreTeak(int count, int rqTimeOne, boolean isDrawable) {
///////        if(mViewModel.getSourceList().size()==0)manager.beginTransaction().replace(R.id.fragment_container2, SpectrumFragment.newInstance(REFERENCE_SPECTRUM)).commitAllowingStateLoss();
        for (SpecMixerParcel parcel:mViewModel.getSourceList()) {
            if(parcel.isChecked()) {
                SpecDTO dto = parcel.getReferenceDTO();
                generateSpectrumTeak(dto, mViewModel.getTempDTO(), dto.getMeasTim()[0] * 100 / parcel.getPercent(), rqTimeOne, count, isDrawable);
            }
        }
    }*/

    private void mixerPreTeak2(int count, int rqTimeOne, boolean isDrawable) {
        generateSpectrumTeak(mViewModel.getEmptyDto(), mViewModel.getTempDTO(), mViewModel.getEmptyDto().getMeasTim()[0], rqTimeOne, count, isDrawable);
    }


    private void updateGeneratedSpectrum() {
        mViewModel.getGeneratedFragment().setNewValues(mViewModel.getTempDTO(), mPeakChannels, mPeakEnergies, mLineOwners);
        mViewModel.getGeneratedFragment().update();
    }

    public void preferenceMixer() {
        mViewModel.setEmptyDto(new SpecDTO(1024));
        for (SpecMixerParcel parcel : mViewModel.getSourceList()) {
            if (parcel.isChecked()) {
                SpecDTO dto = parcel.getReferenceDTO();
                mViewModel.getEmptyDto().addSpectrumToCurrent(dto.getSpectrum(), parcel.getPercent());
                if (dto.getMeasTim()[0] > mViewModel.getEmptyDto().getMeasTim()[0])
                    mViewModel.getEmptyDto().setMeasTim(dto.getMeasTim());//в meas сохраняется максимальное время
                //emptyDto.setMeasTim(new int[]{emptyDto.getMeasTim()[0]+dto.getMeasTim()[0],1});//в meas сохраняется сумма времен всех спектров
            }
        }
        identificateNucl(mViewModel.getEmptyDto());
        mViewModel.getReferenceFragment().setNewValues(mViewModel.getEmptyDto(), mPeakChannels, mPeakEnergies, mLineOwners);
        mViewModel.getReferenceFragment().update();
//        Log.e(TAG, "preferenceMixer: " + mPeakChannels.length + mPeakEnergies.length + mLineOwners.length);
        ////////////////manager.beginTransaction().replace(R.id.fragment_container1, SpectrumFragment.newInstance(mViewModel.getEmptyDto(), mPeakChannels, mPeakEnergies, mLineOwners, REFERENCE_SPECTRUM)).commitAllowingStateLoss();
    }

    void startQuickMixer() {
        for (int count = 0; count < mViewModel.getRequiredTime(); count++) {
            mixerPreTeak2(count, 1, false);
        }
        genButton.setText("Генератор");
        mViewModel.setGenButtonIsPressed(false);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = getDrawable(R.drawable.button_shape_selector);
            genButton.setBackground(drawable);
        }
//        manager.beginTransaction().replace(R.id.fragment_container2, SpectrumFragment.newInstance(mViewModel.getTempDTO(), mPeakChannels, mPeakEnergies, mLineOwners, "Сгенерированный спектр")).commitAllowingStateLoss();
        mViewModel.getGeneratedFragment().setNewValues(mViewModel.getTempDTO(), mPeakChannels, mPeakEnergies, mLineOwners);
        mViewModel.getGeneratedFragment().update();
    }

    void stop() {
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == R.id.seekBar) {
            delayText.setText(String.valueOf(seekBar.getProgress()));
            delayTimeTV.setText(String.valueOf(seekBar.getProgress()));
        }
        if (seekBar.getId() == R.id.seekBar2) {
            timeText.setText(String.valueOf(seekBar.getProgress()));
            requiredTimeTV.setText(String.valueOf(seekBar.getProgress()));
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.getId() == R.id.seekBar)
            mViewModel.setDelay(Integer.parseInt(String.valueOf(seekBar.getProgress())));
        if (seekBar.getId() == R.id.seekBar2)
            mViewModel.setRequiredTime(Integer.parseInt(String.valueOf(seekBar.getProgress())));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.isFirstTime = false;
        preferenceMixer();//это чтобы при повороте устройства восстанавливался эталонный спектр
    }
}
