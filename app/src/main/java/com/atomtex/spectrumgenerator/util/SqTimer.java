package com.atomtex.spectrumgenerator.util;

import android.app.Activity;
import android.util.Log;

import static com.atomtex.spectrumgenerator.MainActivity.TAG;

public class SqTimer {
    private int seconds = 3;
    private Activity activity;
    public boolean canRun = false;

    //todo УДАЛИТЬ КЛАСС!
    public SqTimer(Activity activity, int seconds) {
        this.seconds = seconds;
        this.activity = activity;
    }

    public void start() {
        canRun = true;
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void stop() {
        canRun = false;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try {
                Thread.sleep(seconds * 1000);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        while (canRun) {
                            Log.e(TAG, "run: ");
                        }
                    }
                });

//                    endTime = System.currentTimeMillis() - startTime;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
}
