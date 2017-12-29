package com.wty.app.uexpress.task;

import android.os.AsyncTask;

public abstract class SimpleTask extends AsyncTask<String, Integer, Object> {

    public void startTask() {
		executeOnExecutor(TaskManager.getInstance().getParallelTaskPool());
    }
}
