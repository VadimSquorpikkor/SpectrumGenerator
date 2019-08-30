package com.atomtex.spectrumgenerator;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

class SaveLoad {

    private final String SAVE_FIELD = "setting";
    private Context context;
    private SharedPreferences preferences;

    SaveLoad(Context context) {
        this.context = context;
    }

    void saveBooleanArray(ArrayList<Boolean> list, String prefName) {
        preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        saveBooleanArray(list, preferences);
    }

    ArrayList<Boolean> loadBooleanArray(String prefName) {
        preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return loadBooleanArray(preferences);
    }

    void saveDouble(double d, String prefName) {
        preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        saveDouble(d, preferences);
    }

    double loadDouble(String prefName) {
        preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return loadDouble(preferences);
    }

    void saveBoolean(boolean b, String prefName) {
        preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        saveBoolean(b, preferences);
    }

    boolean loadBoolean(String prefName) {
        preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return loadBoolean(preferences);
    }

    void saveString(String s, String prefName) {
        preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        saveString(s, preferences);
    }

    String loadString(String prefName) {
        preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return loadString(preferences);
    }

//----------------------PRIVATE METHODS-------------------------------------------------------------

    private void saveBooleanArray(ArrayList<Boolean> list, SharedPreferences sPref) {
        int count = 0;
        SharedPreferences.Editor editor = sPref.edit();
        editor.clear();//For save less variables than before, if do not clear, it will load old variables, from old session
        for (Boolean b : list) {
            editor.putBoolean(SAVE_FIELD + count, b);
            count++;
        }
        editor.apply();
    }

    private ArrayList<Boolean> loadBooleanArray(SharedPreferences sPref) {
        ArrayList<Boolean> list = new ArrayList<>();
        int count = 0;
        while (sPref.contains(SAVE_FIELD + count)) {
            boolean b = sPref.getBoolean(SAVE_FIELD + count, true);
            list.add(b);
            count++;
        }
        return list;
    }

    private void saveDouble(double d, SharedPreferences sPref) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putFloat(SAVE_FIELD, (float) d);
        editor.apply();
    }

    private double loadDouble(SharedPreferences sPref) {
        double d = 0;
        if (sPref.contains(SAVE_FIELD)) d = sPref.getFloat(SAVE_FIELD, 0);
        return d;
    }

    private void saveBoolean(boolean b, SharedPreferences sPref) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(SAVE_FIELD, b);
        editor.apply();
    }

    private boolean loadBoolean(SharedPreferences sPref) {
        boolean b = false;
        if (sPref.contains(SAVE_FIELD)) b = sPref.getBoolean(SAVE_FIELD, false);
        return b;
    }

    private void saveString(String s, SharedPreferences sPref) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(SAVE_FIELD, s);
        editor.apply();
    }

    private String loadString(SharedPreferences sPref) {
        String s = "";
        if (sPref.contains(SAVE_FIELD)) {
            s = sPref.getString(SAVE_FIELD, "");
        }
        return s;
    }
}
