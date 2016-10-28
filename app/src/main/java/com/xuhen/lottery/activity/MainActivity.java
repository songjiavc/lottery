package com.xuhen.lottery.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;

import com.baiyilin.lottery.R;
import com.xuhen.lottery.common.GlobalApplication;
import com.xuhen.lottery.common.MyClass;
import com.xuhen.lottery.common.MyVar;
import com.xuhen.lottery.common.SystemVar;
import com.xuhen.lottery.view.AppListAdapter;
import com.xuhen.lottery.view.CustomDialog;
import com.xuhen.lottery.view.MyListView;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Locale;


public class MainActivity extends Activity {
	//重要数据,一直保存
	private static boolean init_flag = false;
	private static JSONObject json_user_list = null;
	private static Handler my_handler = null;

	//重进首页时恢复
	private CustomDialog dlg = null;
	private AppListAdapter listItemAdapter = null;
	private MyListView lv_guest_list = null;
	private BroadcastReceiver networkReceiver = null;//网络连接管理

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Init();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) private void Init(){
		//获取屏幕的高度和宽读
		String dpi=null;
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
			method.invoke(display, dm);
			MyVar.SetScreenSize(dm.widthPixels, dm.heightPixels,dm.scaledDensity);
		}catch(Exception e){
			e.printStackTrace();
			MyVar.SetScreenSize(dm.widthPixels, dm.heightPixels,dm.scaledDensity);
		}
		//管理activity
		GlobalApplication.getInstance().AddActivity(this);
		ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		if(GetLayoutType()==1){
			MyVar.SYSTEM_LAYOUT_TYPE = 1;
			if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT||MyVar.GetScreenWidth()>MyVar.GetScreenHeight()){
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}else{
			MyVar.SYSTEM_LAYOUT_TYPE = 0;
			if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE||MyVar.GetScreenWidth()<MyVar.GetScreenHeight()){
				//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				if(MyVar.GetScreenWidth()<MyVar.GetScreenHeight()){
					MyVar.SetScreenSize(MyVar.GetScreenHeight(), MyVar.GetScreenWidth(), MyVar.GetScaledDensity());
				}
			}
		}
		//屏幕常亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		if(MyVar.SYSTEM_LAYOUT_TYPE==0){
			AbsoluteLayout layout = (AbsoluteLayout)this.findViewById(R.id.layout_main);
			layout.setBackgroundResource(R.drawable.load_v);
		}
		//应对错误的竖屏标识
		if(MyVar.SYSTEM_LAYOUT_TYPE==1&&MyVar.GetScreenWidth()>=MyVar.GetScreenHeight()){
			AbsoluteLayout layout_main = (AbsoluteLayout)this.findViewById(R.id.layout_main);
			layout_main.setBackgroundColor(0xff000000);
			layout_main.invalidate();
			return;
		}
		//应对错误的横屏标识
		if(MyVar.SYSTEM_LAYOUT_TYPE==0&&MyVar.GetScreenWidth()<=MyVar.GetScreenHeight()){
			AbsoluteLayout layout_main = (AbsoluteLayout)this.findViewById(R.id.layout_main);
			layout_main.setBackgroundColor(0xff000000);
			layout_main.invalidate();
			return;
		}
		//输出APK版本号
		MyClass.PrintInfoLog("version:"+MyClass.GetCurrentApkVersion(this.getBaseContext()));

		//创建程序根目录
		File file = new File(MyVar.APK_ROOT_PATH);
		if(file.exists() == false){
			if(file.mkdirs()==false){
				MyClass.ShowWindowText(this.getBaseContext(), "目录'"+MyVar.APK_ROOT_PATH+"'创建失败!");
				this.finish();
				return;
			}
		}
		MyClass.CreateDir(MyVar.SHARE_DATA_DIR);

		MyVar.main_handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what==0x1000){
					ShowNetworkConnect();
				}
				super.handleMessage(msg);
			}
		};

		Thread.currentThread().setName("MainActivity");
		//获取设备网络状
		//检查APP参数设置是否正确
		CheckGlobalStaticVars();
		//设置程序运行标志
		MyVar.SetProRunFlag(true);
		MyVar.CURRENT_PAGE_ID = 1;
		ProgramInit();
	}

	private int GetLayoutType(){
		int rtn = 1;
		try {
			Class cls;
			cls = Class.forName("android.os.SystemProperties");
			Method method = cls.getMethod("getInt",new Class[] {String.class,int.class});
			int angleInt = (Integer)method.invoke(null,new Object[] {"persist.sys.hwrotation",-1});

			if(angleInt == -1){
				MyClass.PrintInfoLog("persist.sys.hwrotation 该属性无法获取！");
				angleInt = (Integer)method.invoke(null,new Object[] {"persist.sys.orientation.value",-1});
				if(angleInt == -1){
					if(MyVar.GetScreenWidth() > MyVar.GetScreenHeight()){
						rtn = 0;
					}
				}else {
					//如果这个值也是-1 则说明从未设置过该属性
					if (angleInt == 0 || angleInt == 180) {
						rtn = 0;
					}
				}
			}else{
				MyClass.PrintInfoLog("persist.sys.hwrotation 该属性获取！");
				if (angleInt == 90 || angleInt == 270) {
					rtn = 0;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return rtn;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if(newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
			//开始重新启动
			Init();
			this.onResume();
		}
		super.onConfigurationChanged(newConfig);
	}

	private String ipstring(int ip){
		String str_ip = "0.0.0.0";
		int a=0,b=0,c=0,d=0;
		a = (ip>>24)&0xff;
		b = (ip>>16)&0xff;
		c = (ip>>8)&0xff;
		d = ip&0xff;
		str_ip = String.format(Locale.ENGLISH,"%d.%d.%d.%d", d,c,b,a);
		return str_ip;
	}

	private void CheckGlobalStaticVars(){
		if(MyVar.assets_version<=15090901||MyVar.assets_version>=29123199){
			MyClass.ShowDlgWindow(MainActivity.this,"全局参数检查", "资源版本号设置出错");
			return;
		}
		if(MyVar.build_date<150909||MyVar.build_date>=301231){
			MyClass.ShowDlgWindow(MainActivity.this,"全局参数检查", "系统运行最低时间错误");
			return;
		}
	}

	private void ProgramInit(){
		//防止这里代码多次运行
		if(init_flag==false){
			//提示用户连接网络,在没有任何网络结果时显示网络问题
//			if(MyVar.main_handler.hasMessages(0x1000)==false){
//				MyClass.SendMessageDelay(MyVar.main_handler, 0x1000, 60000);
//			}
			RegNetworkStatusEvent();
			//读入配置文件
			SystemVar.LoadCfg();
			//获取设备UUID
			MyClass.PrintInfoLog("uuid:"+SystemVar.uuid);
			init_flag = true;
		}
	}

	//显示用户没有连网
	private void ShowNetworkConnect(){		//开启切换屏幕为竖屏
		//AbsoluteLayout layout_main = (AbsoluteLayout)this.findViewById(R.id.layout_main);
		//layout_main.setBackgroundResource(R.drawable.load);
		//layout_main.invalidate();
		int width = 720;
		int height = width*5/8;
		if(dlg!=null){
			dlg.dismiss();
			dlg = null;
		}
		dlg = new CustomDialog(MainActivity.this);
		dlg.SetSize(width, height);
		dlg.show();
		dlg.SetTitle("网络连接");
		dlg.SetMessage("检测到网络未连接,是否连接网络?");
		dlg.SetMessageSize(30);
		dlg.SetMessageGravity(Gravity.CENTER);
		dlg.btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MyClass.ShowSettingActivity(MyVar.CURRENT_PAGE_ID);
			}
		});
		dlg.btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dlg.dismiss();
				dlg = null;
			}
		});
	}

	//注册网络状态监听
	private void RegNetworkStatusEvent(){
		UnRegNetworkStatusEvent();
		networkReceiver = new BroadcastReceiver(){
			public void onReceive(Context arg0, Intent arg1) {
				ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				NetworkInfo  ethNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
				if(!mobNetInfo.isConnected()&&!wifiNetInfo.isConnected()&&!ethNetInfo.isConnected()) {
					MyVar.SetNetworkStatusFlag(false);
					//提示用户连接网络
					if(MyVar.main_handler.hasMessages(0x1000)==false){
						MyClass.SendMessageDelay(MyVar.main_handler, 0x1000, 6000);
					}
				}else{
					if(MyVar.GetNetWorkStatusFlag()==true){
						return;
					}
					//设置网络状态
					MyVar.SetNetworkStatusFlag(true);
					//删除提示信息
					if(MyVar.main_handler.hasMessages(0x1000)==true){
						MyClass.RemoveMessage(MyVar.main_handler, 0x1000);
					}

					/*
					//在这里设置WIFI的静态IP
					if(SystemVar.wifi_dhcp==false){
						IpAddressSet.SetIpAddress(IpAddressSet.WLAN0, SystemVar.wifi_ip, SystemVar.wifi_netmask);
						IpAddressSet.SetGateWay(IpAddressSet.WLAN0, SystemVar.wifi_gateway);
						IpAddressSet.SetDns(SystemVar.wifi_dns);
					}else{
						IpAddressSet.SetAutoDhcp(IpAddressSet.WLAN0);
					}
					//设置有线IP
					if(SystemVar.eth_dhcp==false){
						IpAddressSet.SetIpAddress(IpAddressSet.ETH0, SystemVar.eth_ip, SystemVar.eth_netmask);
						IpAddressSet.SetGateWay(IpAddressSet.ETH0, SystemVar.eth_gateway);
						IpAddressSet.SetDns(SystemVar.eth_dns);
					}else{
						IpAddressSet.SetAutoDhcp(IpAddressSet.ETH0);
					}
					*/
					//无账号保存的用户登录
					if(MyVar.login_flag==false){
						UserLogin();
					}else{
						MyClass.ShowAppActivity(MyVar.CURRENT_PAGE_ID);
					}
					GlobalApplication.getInstance().RemoveActivity(MainActivity.this);
					finish();
				}
			}
		};
		IntentFilter networkReceiverFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		GlobalApplication.getInstance().registerReceiver(networkReceiver, networkReceiverFilter);
	}

	//用户登录
	private void UserLogin(){
		//未保存登录信息
		if("".equals(SystemVar.username)==true||"".equals(SystemVar.password)==true){
			MyClass.ShowUserActivity(MyVar.CURRENT_PAGE_ID);
			return;
		}else{//已保存的用户，需要保存用户信息
			MyClass.ShowUserActivity(MyVar.CURRENT_PAGE_ID);
		}
	}

	//取消注册网络监控
	private void UnRegNetworkStatusEvent(){
		if(networkReceiver!=null){
			GlobalApplication.getInstance().unregisterReceiver(networkReceiver);
			networkReceiver = null;
		}
	}

	@Override
	protected void onResume() {
		//应对错误的竖屏标识
		if(MyVar.SYSTEM_LAYOUT_TYPE==1&&MyVar.GetScreenWidth()>=MyVar.GetScreenHeight()){
			MyClass.PrintErrorLog("MainActivity->onResume->exit");
			super.onResume();
			return;
		}
		if(MyVar.SYSTEM_LAYOUT_TYPE==0&&MyVar.GetScreenWidth()<=MyVar.GetScreenHeight()){
			super.onResume();
			return;
		}
		MyClass.PrintInfoLog("onResume 中执行的方法弹出的！");
		if(MyVar.main_handler!=null&&MyVar.main_handler.hasMessages(0x1000)==false){
			MyClass.SendMessageDelay(MyVar.main_handler, 0x1000, 6000);
		}
		ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo  ethNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
		if(!mobNetInfo.isConnected()&&!wifiNetInfo.isConnected()&&!ethNetInfo.isConnected()) {
			MyClass.PrintInfoLog("onResume 检查网络状态不正常！");
			MyVar.SetNetworkStatusFlag(false);
			//提示用户连接网络
			if(MyVar.main_handler.hasMessages(0x1000)==false){
				MyClass.SendMessageDelay(MyVar.main_handler, 0x1000, 6000);
			}
		}else{

			MyClass.PrintInfoLog("onResume 检查网络状态正常！");
			if(MyVar.GetNetWorkStatusFlag()==true){
				return;
			}
			//设置网络状态
			MyVar.SetNetworkStatusFlag(true);
			//删除提示信息
			if(MyVar.main_handler.hasMessages(0x1000)==true){
				MyClass.RemoveMessage(MyVar.main_handler, 0x1000);
			}

			/*
			//在这里设置WIFI的静态IP
			if(SystemVar.wifi_dhcp==false){
				IpAddressSet.SetIpAddress(IpAddressSet.WLAN0, SystemVar.wifi_ip, SystemVar.wifi_netmask);
				IpAddressSet.SetGateWay(IpAddressSet.WLAN0, SystemVar.wifi_gateway);
				IpAddressSet.SetDns(SystemVar.wifi_dns);
			}else{
				IpAddressSet.SetAutoDhcp(IpAddressSet.WLAN0);
			}
			//设置有线IP
			if(SystemVar.eth_dhcp==false){
				IpAddressSet.SetIpAddress(IpAddressSet.ETH0, SystemVar.eth_ip, SystemVar.eth_netmask);
				IpAddressSet.SetGateWay(IpAddressSet.ETH0, SystemVar.eth_gateway);
				IpAddressSet.SetDns(SystemVar.eth_dns);
			}else{
				IpAddressSet.SetAutoDhcp(IpAddressSet.ETH0);
			}
			*/
			//无账号保存的用户登录
			if(MyVar.login_flag==false){
				UserLogin();
			}else{
				MyClass.ShowAppActivity(MyVar.CURRENT_PAGE_ID);
			}
			GlobalApplication.getInstance().RemoveActivity(this);
			this.finish();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if(dlg!=null){
			dlg.dismiss();
			dlg = null;
		}
		UnRegNetworkStatusEvent();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(MyClass.SetKeyDown(keyCode, event)==true){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(MyClass.SetKeyUp(keyCode,event)==true){
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
}