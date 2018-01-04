package com.wty.app.uexpress.task;

import android.annotation.SuppressLint;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint("NewApi")
public class TaskManager {
	
	private static TaskManager taskManager = null;
	//线程并列完成
	private ExecutorService parallelTaskPool = null;
	
	public static synchronized TaskManager getInstance(){
		if(taskManager==null){
			taskManager= new TaskManager();
		}
		return taskManager;
	}

	//并行线程池，支持同时执行5条线程任务
	public ExecutorService getParallelTaskPool(){
		if(parallelTaskPool == null){
			parallelTaskPool = Executors.newFixedThreadPool(5);
		}
		return parallelTaskPool;
	}

	public void clear(){
		if(parallelTaskPool!=null){
			parallelTaskPool.shutdownNow();
			parallelTaskPool = null;
		}
	}
}
