package com.xuhen.lottery.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.xuhen.lottery.cls.MyThread;
import com.xuhen.lottery.cls.MyUnCeHandler;

import android.app.Activity;
import android.app.Application;

public class GlobalApplication extends Application {
	private List<Activity> list_activity = new ArrayList<Activity>();
	private List<MyThread> list_thread = new ArrayList<MyThread>();
	private MyThread prev_thread = null;
	
	public GlobalApplication() {
		Init();
	}
	//UI管理
	public void AddActivity(Activity m_activity) {
		list_activity.add(m_activity);
    }
	//线程管理
	public void AddThread(MyThread m_thread) {
		list_thread.add(m_thread);
    }
	//UI管理
	public void RemoveActivity(Activity m_activity){
		for(int i=0;i<list_activity.size();i++){
			Activity activity = list_activity.get(i);
			if(activity.equals(m_activity)==true){
				list_activity.remove(activity);
				break;//移除队列中数据后，队列长度变化，必须退出
			}
        }
	}
	public void DestroyOtherActivity(Activity m_activity){
		MyClass.PrintErrorLog("DestroyOtherActivity");
		for(int i=0;i<list_activity.size();i++){
			Activity activity = list_activity.get(i);
			if(activity!=m_activity){
				activity.finish();
				list_activity.remove(activity);
				break;//移除队列中数据后，队列长度变化，必须退出
			}
        }
	}
	//线程管理
	public void RemoveThread(MyThread m_thread){
		if(prev_thread==null&&m_thread==null){
			return;
		}
		if(m_thread.equals(prev_thread)==true){
			return;
		}
		prev_thread = m_thread;
		for(int i=0;i<list_thread.size();i++){
			MyThread th_thread = list_thread.get(i);
			if(th_thread.equals(m_thread)==true){
				list_thread.remove(th_thread);
				break;//移除队列中数据后，队列长度变化，必须退出
			}
        }
	}
	//移除所有检查线程
	public void RemoveAllCheckThread(){
		for(int i=0;i<list_thread.size();i++){
			MyThread th_thread = list_thread.get(i);
			String thread_name = th_thread.getName().toLowerCase();
			if(thread_name==null||thread_name.length()==0){
				continue;
			}
			if(thread_name.indexOf("th_check")!=-1){//检测线程名字都带有th_check
				th_thread.SetRunFlag(false);
				list_thread.remove(th_thread);
				i=0;
				continue;
			}
        }
	}
	
	public void Init(){
		MyUnCeHandler catchExcep = new MyUnCeHandler(this);  
		Thread.setDefaultUncaughtExceptionHandler(catchExcep);
		Thread th = new Thread(){
			@Override
			public void run() {
				int count = -1;
				while(true){
					if(list_thread.size()!=count){						
						count = list_thread.size();
						MyClass.PrintInfoLog("Thread:count="+count);
						for(int i=0;i<list_thread.size();i++){
							MyThread th_thread = list_thread.get(i);
							if(th_thread.isAlive()==true){
								MyClass.PrintInfoLog("Thread"+i+":name="+th_thread.getName());
							}
						}
					}					
					MyClass.MySleep(1000);
				}
			}			
		};
		th.setName("GlobalApplication->th0");
		th.start();
	}
	
	private static GlobalApplication instance;

    public static GlobalApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    
    public void ExitApp(){  
    	this.onTerminate();
		//杀死该应用进程
		System.exit(0);
	}

	@Override
	public void onTerminate() {
		MyClass.PrintErrorLog("onTerminate");
		for(MyThread th_thread:list_thread){
			th_thread.SetRunFlag(false);
		}
		for (Activity activity:list_activity) {
			activity.finish();
        }
		super.onTerminate();
	}	
}
