package com.atomtex.spectrumgenerator;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.atomtex.spectrumgenerator.domain.NucIdent;
import com.atomtex.spectrumgenerator.util.NuclideLibraryReader;

import java.util.ArrayList;

import static com.atomtex.spectrumgenerator.MainViewModel.GENERATED_SPECTRUM;
import static com.atomtex.spectrumgenerator.MainViewModel.REFERENCE_SPECTRUM;

/*  SpecDTO.class --> public void addSpectrumToCurrent(int[] spectrum) {*/

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    MainViewModel mViewModel;
    NavigationView navigationView;
    FragmentManager manager;
    Toggler toggler;
    SaveLoad saveLoad;
    IdentificateAndGenerate generator;

    Fragment fragment4;//todo (переместить в ViewModel) вообще хранить фрагменты в mainView -- плохая идея, надо вернуть фрагменты 1 и 2 обратно

    public static final String TAG = "TAG!!!";
    public static final String SHARED_PREFFERENCES = "sPref";

    TextView requiredTimeTV;
    TextView delayTimeTV;
    Button genButton;
    EditText timeText;
    EditText delayText;

    TextView versionTV;

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


        toggler.setTimeLayoutMode(mViewModel.getTimeLayoutMode());
        toggler.setAllVisibilities();
        toggler.setMixerLayoutMode(mViewModel.getMixerMode());

        requiredTimeTV = findViewById(R.id.requiredTime);
        delayTimeTV = findViewById(R.id.delay_time_main);
        genButton = findViewById(R.id.gen_button);
        timeText = findViewById(R.id.dialog_required_time_text);
        delayText = findViewById(R.id.dialog_delay_text);

        generator = new IdentificateAndGenerate(mViewModel, toggler, this, genButton);
//        generator.setPrefIdenThreshold(3);

        setRequiredTime();
        setDelayTime();

        findViewById(R.id.gen_button).setOnClickListener(this);
        findViewById(R.id.time_layout).setOnClickListener(this);
        findViewById(R.id.mixer_fragment_list_view).setOnClickListener(this);//show mixer
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
        versionTV = navigationView.getHeaderView(0).findViewById(R.id.version);
        versionTV.setText(versionName());

//        navigationView.getMenu().findItem(R.id.nav_switch_5).setActionView(new Switch(this));

        timeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (timeText.getText().toString().equals("")) timeText.setText("1");
                if (Integer.parseInt(timeText.getText().toString()) < 501)
                    ((SeekBar) findViewById(R.id.seekBar2)).setProgress(Integer.parseInt(timeText.getText().toString()));
                timeText.setSelection(timeText.getText().length());
            }
        });
        delayText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {//todo при наборе БОЛЬШЕ максимального для seekBar, становится максимальным
                if (delayText.getText().toString().matches("")) delayText.setText("1");
                if (Integer.parseInt(delayText.getText().toString()) < 1001)
                    ((SeekBar) findViewById(R.id.seekBar)).setProgress(Integer.parseInt(delayText.getText().toString()));
                delayText.setSelection(delayText.getText().length());
            }
        });

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

        } else
            mViewModel.setReferenceFragment((SpectrumFragment) manager.findFragmentById(R.id.fragment_container1));

        if (mViewModel.getGeneratedFragment() == null) {
            mViewModel.setGeneratedFragment(SpectrumFragment.newInstance(GENERATED_SPECTRUM));
            manager.beginTransaction().replace(R.id.fragment_container2, mViewModel.getGeneratedFragment()).commit();
        } else
            mViewModel.setGeneratedFragment((SpectrumFragment) manager.findFragmentById(R.id.fragment_container2));

        if (fragment4 == null) {
            fragment4 = MixerListFragment.newInstance();
            manager.beginTransaction().replace(R.id.mixer_fragment_list_view_big, fragment4).commit();
        }


//        versionTV.setText("1");
    }

    public void updateNuclideStroke() {
        StringBuilder stroke = new StringBuilder();
        String prefix = "";
        if (mViewModel.getSourceList().size() == 0)
            stroke = new StringBuilder("Спектры не загружены");
        else prefix = "Нет включенных спектров";
        for (SpecMixerParcel parcel : mViewModel.getSourceList()) {
            if (parcel.isChecked()) {
                prefix = "";
                stroke.append(parcel.getName()).append(" ");
            }
        }
        ((TextView) findViewById(R.id.nuclide_stroke)).setText((prefix + stroke));
    }

    private String versionName() {
        String version = "Unknown";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
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
            case R.id.gen_button:
                toggleGenButton();
                break;
            case R.id.time_layout:
                toggler.setTimeLayoutMode(1);
                break;
            case R.id.hide_time_button:
                hideTime();
                break;
            case R.id.mixer_fragment_list_view:
                toggler.setMixerLayoutMode(0);
                break;
            case R.id.add_new_spectrum_2:
                openFile();
                break;
        }
    }

    public void hideMixer() {
        toggler.setMixerLayoutMode(1);
        updateNuclideStroke();
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

    public void openLibrary() {
        Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().toString());
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(selectedUri, "*/*");
        startActivityForResult(intent, 2);//TODO request code сделать psf
    }

    private void gen() {
        if (iCanGenerate()) {
            mViewModel.getTempDTO().setSpectrum(new int[mViewModel.getTempDTO().getSpectrum().length]);//todo ??????
            generator.startQuickMixer();
        } else makeToast("Сначала загрузите файл спектра");
    }

    private void genMixer() {
        if (iCanGenerate()) {
            mViewModel.getTempDTO().setSpectrum(new int[mViewModel.getTempDTO().getSpectrum().length]);//todo ??????
            if (mViewModel.getEmptyDto().getMeasTim()[0] != 0) {
                toggler.setGenButtonMode(1, genButton);
                generator.startMixer();
            } else {
                makeToast("Все спектры выключены! Нечего генерировать!");
            }
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
                generator.stop();
                toggler.setGenButtonMode(0, genButton);
            } else genMixer();
        } else gen();
    }

    void preferenceMixer() {//todo когда в IdentificateAndGenerate будет нормальный конструктор, удалить метод, а в mainView вызывать метод напрямую из generator
        generator.preferenceMixer();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                String pathHolder = uri.toString();
                if (requestCode == 1) getDtoFromFile(pathHolder);
                if (requestCode == 2) getNuclFromFile(pathHolder);
            }
        }
    }

    private void getNuclFromFile(String path) {
        Log.e(TAG, "getNuclFromFile: " + path);
        try {
            mViewModel.setOuterLibrary(NuclideLibraryReader.getLibrary(path));
        } catch (NuclideLibraryReader.NuclideLibraryException e) {
            e.printStackTrace();
        }
    }

    private String makeName() {
        String name = "Unknown";
        if (!mViewModel.getNameForMixer().equals("")) name = mViewModel.getNameForMixer();
        return name;
    }

    //переделанный openAtsFile
    private void getDtoFromFile(String path) {//todo убрать pos
        Log.e(TAG, "--------------PATH = " + path);
        mViewModel.setPathForAts(path);
        mViewModel.setNameForMixer(""); //сброс
        SpecDTO dto;
        if (path.endsWith(".ats")) {
            mViewModel.setTempDTO(AtsReader.parseFile(path));
            dto = AtsReader.parseFile(path);
        } else if (path.endsWith(".spe")) {
            mViewModel.setTempDTO(SpeReader.parseFile(path));
            dto = SpeReader.parseFile(path);
        } else {
            makeToast("Неверный формат");
            dto = null;
        }
        if (dto != null) {

            ((MixerListFragment) fragment4).updateAdapter();//чтобы во фрагменте появился item

            generator.identificateNucl(dto);

            mViewModel.addNewSpectrum(dto, makeName());
            updateNuclideStroke();
            mViewModel.setNameForMixer(""); //сброс
            mViewModel.setSpectrumTime(dto.getMeasTim()[0]);//todo потом убрать, когда везде сделаю через getMeas[0]
            generator.preferenceMixer();
        }
    }

    private void makeToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
//        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//        toast.getView().setBackgroundColor(Color.WHITE);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        /*View view = toast.getView();
        view.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        TextView textView = view.findViewById(android.R.id.message);
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);*/
        toast.show();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_open_ats) openFile();
        if (id == R.id.nav_generate) toggleGenButton();
        if (id == R.id.nav_library) openLibrary();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        generator.preferenceMixer();//это чтобы при повороте устройства восстанавливался эталонный спектр
    }

}
