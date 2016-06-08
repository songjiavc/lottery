package com.xuhen.lottery.cls;

import java.lang.Thread.UncaughtExceptionHandler;

import org.json.JSONObject;

import com.xuhen.lottery.activity.MainActivity;
import com.xuhen.lottery.common.GlobalApplication;
import com.xuhen.lottery.common.MyClass;
import com.xuhen.lottery.common.MyVar;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class MyUnCeHandler implements UncaughtExceptionHandler{	
	private UncaughtExceptionHandler mDefaultHandler;
	private GlobalApplication myapp = null;
	public MyUnCeHandler(GlobalApplication myapp) {
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		this.myapp = myapp;
	}

	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		arg1.printStackTrace();
		StackTraceElement[] stack = arg1.getStackTrace();
		String str = arg1.getLocalizedMessage()+"\r\n"+arg1.getMessage()+"\r\n";
		for(StackTraceElement ste : stack){
			str += ste.toString()+"\r\n";
		}
		MyClass.PrintErrorLog(str);
		JSONObject json = MyClass.JsonInit();
		MyClass.JsonPutString(json, "info", str);
		//这个是可能是主线程上访问网络，可能会出问题
		//String str_ret = Http.HttpPost(MyVar.SERVER_REPORT_SOFT_BUG, json);
		//异常后，不重启APP
		Intent intent = new Intent(myapp.getApplicationContext(), MainActivity.class);  
		PendingIntent restartIntent = PendingIntent.getActivity(myapp.getApplicationContext(), 0, intent,Intent.FLAG_ACTIVITY_CLEAR_TOP);                                                 
		AlarmManager mgr = (AlarmManager)myapp.getSystemService(Context.ALARM_SERVICE);    
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, restartIntent); // 10秒钟后重启应用   
		//退出程序
		myapp.ExitApp();		
	}
}

