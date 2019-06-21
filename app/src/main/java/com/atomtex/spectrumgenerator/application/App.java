package com.atomtex.spectrumgenerator.application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.balsikandar.crashreporter.CrashReporter;

import java.io.File;


public class App extends Application {
    //todo USELESS CLASS!!!!!!

    public static final String DIRECTORY = "Android/data/com.atomtex.scannermobile";

    private static Application sApplication;

    public static Context getContext() {
        return sApplication.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "onCreate: ");
        sApplication = this;

    }
}
