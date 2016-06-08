package com.xuhen.lottery.cls;

import com.xuhen.lottery.common.GlobalApplication;


public class MyThread extends Thread {
	private boolean thread_run_flag = false;
	
	public void SetRunFlag(boolean b){
		this.thread_run_flag = b;
		if(b==false){
			GlobalApplication.getInstance().RemoveThread(this);
		}
	}
	
	public boolean GetRunFlag(){
		return this.thread_run_flag;
	}

	@Override
	public void run() {
		super.run();
		this.thread_run_flag = false;
		GlobalApplication.getInstance().RemoveThread(this);
	}

	@Override
	public void destroy() {
		this.thread_run_flag = false;
		GlobalApplication.getInstance().RemoveThread(this);
	}

	public MyThread() {
		Init();
	}

	public MyThread(Runnable runnable) {
		super(runnable);
		Init();
	}

	public MyThread(String threadName) {
		super(threadName);
		Init();
	}

	public MyThread(Runnable runnable, String threadName) {
		super(runnable, threadName);
		Init();
	}

	public MyThread(ThreadGroup group, Runnable runnable) {
		super(group, runnable);
		Init();
	}

	public MyThread(ThreadGroup group, String threadName) {
		super(group, threadName);
		Init();
	}

	public MyThread(ThreadGroup group, Runnable runnable, String threadName) {
		super(group, runnable, threadName);
		Init();
	}
	
	public MyThread(ThreadGroup group, Runnable runnable, String threadName,
			long stackSize) {
		super(group, runnable, threadName, stackSize);
		Init();
	}

	private void Init(){
		GlobalApplication.getInstance().AddThread(this);
	}
	
	
}
