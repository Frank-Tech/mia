package com.franktech.mia.model;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tzlil on 12/06/17.
 */

public abstract class MiaAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>{

    public static List<MiaAsyncTask> asyncTasks = new ArrayList<>();

    public static void cancel() {

        for (MiaAsyncTask asyncOperation : asyncTasks) {
            if(asyncOperation.cancelOnBackground()) {
                asyncOperation.cancel(true);
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        asyncTasks.add(this);
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        asyncTasks.remove(this);
    }

    @Override
    protected void onCancelled(Result result) {
        asyncTasks.remove(this);
    }

    private boolean cancelOnBackground(){
        return true;
    }
}