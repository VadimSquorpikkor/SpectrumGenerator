package com.atomtex.spectrumgenerator;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class SaveLoad {

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

}
