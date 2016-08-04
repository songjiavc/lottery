package com.xuhen.lottery.activity;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.baiyilin.lottery.R;
import com.xuhen.lottery.activity.AppActivity.AppsAdapter;
import com.xuhen.lottery.common.GlobalApplication;
import com.xuhen.lottery.common.MyClass;
import com.xuhen.lottery.common.MyVar;
import com.xuhen.lottery.common.SystemVar;
import com.xuhen.lottery.view.CustomDialog;
import com.xuhen.lottery.view.MyEditText;
import com.xuhen.lottery.view.MyImageButton;
import com.xuhen.lottery.view.MyImageView;
import com.xuhen.lottery.view.MyTextView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingActivity extends Activity {

	private MyImageView iv_setting_message_bg = null;
	private MyImageView iv_setting_ico_bg = null;
	private MyImageView iv_setting_system_ico = null;
	private MyImageView iv_setting_network_ico = null;
	private MyImageView iv_setting_other_ico = null;
	private MyImageView iv_setting_ap_ico = null;
	private MyImageView iv_setting_app_ico = null;
	//private MyImageView iv_setting_layout_type_ico = null;
	private MyImageView iv_setting_about_ico = null;
	/*add by songjia 增加系统设置按钮*/
	private MyImageView iv_setting_systemconfig_ico = null;

	
	private MyTextView tv_setting_system = null;
	private MyTextView tv_setting_network = null;
//	private MyTextView tv_setting_other = null;
//	private MyTextView tv_setting_ap = null;
	private MyTextView tv_setting_app = null;
//	private MyTextView tv_setting_layout_type = null;
	private MyTextView tv_setting_about = null;

	/*添加系统设置按钮，借此调用系统设置菜单*/
	private MyTextView tv_setting_systemconfig = null;

	private MyImageView iv_setting_other_msg = null;
	
	//wifi变量
	private ListView lv_wifi_list = null;
	private WifiManager wifi_manager = null;
	private List<ScanResult> wifi_list = null;
	private WifiReceiver wifi_receiver = null;
	private WifiResultAdapter wifi_result_adapter= null;
	private Switch switch_wifi_status_switch = null;
	private ImageView iv_wifi_refresh = null;
	private TextView tv_wifi_status_text = null;
	private String str_wifi_connected_ssid = null;
	private int width = 1024;
	private int height = 600;
	//是否已初始化wifi
	private boolean is_init_wifi = false;
	
	private Switch switch_network_dhcp = null;	
	private MyEditText et_ip = null;
	private MyEditText et_netmask = null;
	private MyEditText et_gateway = null;
	private MyEditText et_dns = null;
	private MyImageButton btn_ip_modi = null;
	
	private Switch switch_wifi_network_dhcp = null;
	private MyEditText et_wifi_ip = null;
	private MyEditText et_wifi_netmask = null;
	private MyEditText et_wifi_gateway = null;
	private MyEditText et_wifi_dns = null;
	private MyImageButton btn_wifi_ip_modi = null;
	
	private MyEditText et_ap_ssid = null;
	private MyEditText et_ap_password = null;
	private MyImageButton btn_ap_set = null;
	//app列表控件
	private ListView lv_app_list = null;
	private AppsAdapter list_adapter = null;
	//所有可启动的APK
	private List<ResolveInfo> mApps = null;
	//主页显示PKG
	private ArrayList<String> main_pkg_list = null;
	//主要显示屏幕旋转
	//private Switch switch_layout_type = null;
	//右下部分布局
	private LayoutParams layout_right_bottom = null;
	private long start_time = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		start_time = System.currentTimeMillis();
		GlobalApplication.getInstance().AddActivity(this);
        //屏幕常亮
      	getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_setting);
        
        //开启切换屏幕为竖屏
        if(MyVar.SYSTEM_LAYOUT_TYPE==1){
	        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
        }else{
        	if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
  				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
  			}
        	AbsoluteLayout layout = (AbsoluteLayout)this.findViewById(R.id.layout_setting);
        	layout.setBackgroundResource(R.drawable.main_v);
        }
		
        MyClass.CheckError();
		MyVar.set_handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				/*
				if(msg.what==0x1000){
					ShowNetworkInfo();
				}*/
				super.handleMessage(msg);
			}			
		};
		
		Thread.currentThread().setName("AppActivity");	
		MyVar.CURRENT_PAGE_ID = MyVar.SETTING_PAGE_ID;
		//生成屏幕右下部分布局
		int screen_width = MyVar.GetScreenWidth();
		int screen_height = MyVar.GetScreenHeight();
		if(MyVar.SYSTEM_LAYOUT_TYPE==1){
			int view_new_x = 337*screen_width/MyVar.GetDesignWidth();
			int view_new_y = (123+30)*screen_height/MyVar.GetDesignHeight();
			int view_new_width = 693*screen_width/MyVar.GetDesignWidth();
			int view_new_height = 1698*screen_height/MyVar.GetDesignHeight();
			layout_right_bottom = new LayoutParams(view_new_width,view_new_height,view_new_x,view_new_y);
			//int view_new_x = 287*screen_width/MyVar.GetDesignWidth();
			//int view_new_y = 123*screen_height/MyVar.GetDesignHeight();
			//int view_new_width = 793*screen_width/MyVar.GetDesignWidth();
			//int view_new_height = 1798*screen_height/MyVar.GetDesignHeight();
			//layout_right_bottom = new LayoutParams(view_new_width,view_new_height,view_new_x,view_new_y);
		}else{
			int view_new_x = 470*screen_width/MyVar.GetDesignWidth();
			int view_new_y = 90*screen_height/MyVar.GetDesignHeight();
			int view_new_width = 1400*screen_width/MyVar.GetDesignWidth();
			int view_new_height = 960*screen_height/MyVar.GetDesignHeight();
			layout_right_bottom = new LayoutParams(view_new_width,view_new_height,view_new_x,view_new_y);
		}
		InitView();
		
		RelativeLayout layout_wifi = (RelativeLayout)this.findViewById(R.id.layout_wifi);
		layout_wifi.setVisibility(View.GONE);
		//RelativeLayout layout_ipset = (RelativeLayout)this.findViewById(R.id.layout_ipset);
		//layout_ipset.setVisibility(View.GONE);
		//RelativeLayout layout_apset = (RelativeLayout)this.findViewById(R.id.layout_apset);
		//layout_apset.setVisibility(View.GONE);
		lv_app_list = (ListView)this.findViewById(R.id.lv_app_list);
		lv_app_list.setVisibility(View.GONE);
		/*
		RelativeLayout layout_layout_modi = (RelativeLayout)this.findViewById(R.id.layout_layout_modi);
		layout_layout_modi.setVisibility(View.GONE);
		*/
		InitSystemInfo();
		//InitIpSet();
		//InitApSet();
		//读取软件列表
		InitAppList();
		//屏幕旋转初始化
		//InitLayoutType();
	}
	
	
	private void InitSystemInfo(){
		LinearLayout layout_system = (LinearLayout)findViewById(R.id.setting_layout_system_info);
		layout_system.setLayoutParams(this.layout_right_bottom);
		
		int font_size = 32*MyVar.GetScreenHeight()/MyVar.GetDesignHeight();
		int font_height = 100*MyVar.GetScreenHeight()/MyVar.GetDesignHeight();
		
		MyTextView name = (MyTextView)this.findViewById(R.id.setting_tv_system_info_name);
		//model.setText("产品型号:"+android.os.Build.MODEL);
		name.setTextSize(font_size);
		ViewGroup.LayoutParams layout_tmp =  name.getLayoutParams();
		layout_tmp.height=font_height;
		name.setLayoutParams(layout_tmp);
		
		MyTextView model = (MyTextView)this.findViewById(R.id.setting_tv_system_info_model);
		model.setText("产品型号:"+android.os.Build.MODEL);
		model.setTextSize(font_size);
		layout_tmp =  name.getLayoutParams();
		layout_tmp.height=font_height;
		model.setLayoutParams(layout_tmp);
		
		MyTextView os = (MyTextView)this.findViewById(R.id.setting_tv_system_info_os);
		os.setText("系统版本号:"+android.os.Build.VERSION.RELEASE);
		os.setTextSize(font_size);
		layout_tmp =  name.getLayoutParams();
		layout_tmp.height=font_height;
		os.setLayoutParams(layout_tmp);
		
		MyTextView cpu = (MyTextView)this.findViewById(R.id.setting_tv_system_info_cpu);
		cpu.setText("CPU型号:RK3188");
		cpu.setTextSize(font_size);
		layout_tmp =  name.getLayoutParams();
		layout_tmp.height=font_height;
		cpu.setLayoutParams(layout_tmp);
		
		MyTextView ram = (MyTextView)this.findViewById(R.id.setting_tv_system_info_ram);
		ram.setText("RAM大小:"+String.format("%.1f",MyClass.GetRamSize()/(1024*1024*1024.0))+"G");
		ram.setTextSize(font_size);
		layout_tmp =  name.getLayoutParams();
		layout_tmp.height=font_height;
		ram.setLayoutParams(layout_tmp);
		
		MyTextView screen = (MyTextView)this.findViewById(R.id.setting_tv_system_info_screen);
		screen.setText("屏幕分辨率:"+MyVar.GetScreenWidth()+" X "+MyVar.GetScreenHeight());
		screen.setTextSize(font_size);
		layout_tmp =  name.getLayoutParams();
		layout_tmp.height=font_height;
		screen.setLayoutParams(layout_tmp);
	}
	//列表listview
	private void InitAppList(){
		ReloadAppList();
		list_adapter = new AppsAdapter();
		lv_app_list.setLayoutParams(this.layout_right_bottom);
		this.lv_app_list.setAdapter(list_adapter);
		this.lv_app_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,long id) {
				String pkg_name = main_pkg_list.get(position);       
		        if(pkg_name==null||"null".equals(pkg_name)==true){
		        	return;
		        }
		        //通过程序的包名创建URL  
				Uri packageURI=Uri.parse("package:"+pkg_name);  
				//创建Intent意图  
				Intent intent=new Intent(Intent.ACTION_DELETE);
				//设置Uri  
				intent.setData(packageURI);  
				//卸载程序  
				startActivityForResult(intent,0x100);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==0x100){
			ApkUnInstallFinish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	public void ApkUnInstallFinish(){
    	//读取软件列表
		ReloadAppList();
		list_adapter.notifyDataSetChanged();
    }
	
	public class AppsAdapter extends BaseAdapter {
	    public AppsAdapter() {
	    }
	
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View item_view = null;
	        item_view = View.inflate(getBaseContext(), R.layout.setting_app_list_item, null);
	    	//应用名
	        String pkg_name = main_pkg_list.get(position);
	        ResolveInfo info = FindInfo(pkg_name);
	        String app_name = info.activityInfo.loadLabel(getPackageManager()).toString();
			MyTextView tv_username = (MyTextView)item_view.findViewById(R.id.tv_username);
			tv_username.setText(app_name);
			tv_username.setTextColor(0xffffffff);
			//应用删除按钮
			MyTextView btn_status = (MyTextView)item_view.findViewById(R.id.main_tv_status);
			btn_status.setTextColor(0xffffea76);
			btn_status.setText("删除");
			btn_status.setFocusable(false);
			if(MyVar.SYSTEM_LAYOUT_TYPE==1){
				tv_username.InitSize(11, 0, 540, 78, 36);
				btn_status.InitSize(603, 0, 90, 78,36);
			}else{
				tv_username.InitSize(10, 0, 1000, 78, 36);
				btn_status.InitSize(1290, 0, 90, 78,36);
			}
			return item_view;  
	    }
	    
	    public final int getCount() {
            return main_pkg_list.size();
        }

        public final Object getItem(int position) {
            return main_pkg_list.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
    }
	
	//初始化屏幕旋转
	/*
	private void InitLayoutType(){
		RelativeLayout layout_modi = (RelativeLayout)findViewById(R.id.layout_layout_modi);
		layout_modi.setLayoutParams(layout_right_bottom);
		
		
		int font_size = 32*MyVar.GetScreenHeight()/MyVar.GetDesignHeight();
		int font_height = 70*MyVar.GetScreenHeight()/MyVar.GetDesignHeight();
		
		int button_height = 84*MyVar.GetScreenHeight()/MyVar.GetDesignHeight();
		int button_width = 305*MyVar.GetScreenWidth()/MyVar.GetDesignWidth();
		
		int et_height = 70*MyVar.GetScreenHeight()/MyVar.GetDesignHeight();
		int et_width = 400*MyVar.GetScreenWidth()/MyVar.GetDesignWidth();
		
		//ssid
		MyTextView tv_layout_type = (MyTextView)this.findViewById(R.id.setting_tv_layout_type);
		tv_layout_type.setTextSize(font_size);
		ViewGroup.LayoutParams layout_tmp = tv_layout_type.getLayoutParams();
		layout_tmp.height=font_height;
		tv_layout_type.setLayoutParams(layout_tmp);
		switch_layout_type = (Switch)this.findViewById(R.id.setting_switch_layout_type);
		layout_tmp = switch_layout_type.getLayoutParams();
		layout_tmp.height = font_height;
		switch_layout_type.setLayoutParams(layout_tmp);
		if(MyVar.SYSTEM_LAYOUT_TYPE==1){
			switch_layout_type.setChecked(false);
		}else{
			switch_layout_type.setChecked(true);
		}
		//按钮事件
		switch_layout_type.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1==false){
					SetLayoutType(1);
				}else{
					SetLayoutType(0);
				}				
			}
		});
		long run_time = System.currentTimeMillis()-start_time;
		MyClass.PrintLog("SettingActivity init finish"+run_time);
	}
	*/
	//初始化AP设置
	/*
	private void InitApSet(){
		RelativeLayout layout_apset = (RelativeLayout)findViewById(R.id.layout_apset);
		layout_apset.setLayoutParams(this.layout_right_bottom);
		
		et_ap_ssid = (MyEditText)this.findViewById(R.id.setting_et_ap_ssid);
		et_ap_password = (MyEditText)this.findViewById(R.id.setting_et_ap_password);
		btn_ap_set = (MyImageButton)this.findViewById(R.id.setting_btn_ap_set);
		btn_ap_set.SetImages(R.drawable.main_btn_modi_up, R.drawable.main_btn_modi_down);
		
		int font_size = 32*MyVar.GetScreenHeight()/MyVar.GetDesignHeight();
		int font_height = 70*MyVar.GetScreenHeight()/MyVar.GetDesignHeight();
		
		int button_height = 84*MyVar.GetScreenHeight()/MyVar.GetDesignHeight();
		int button_width = 305*MyVar.GetScreenWidth()/MyVar.GetDesignWidth();
		
		int et_height = 70*MyVar.GetScreenHeight()/MyVar.GetDesignHeight();
		int et_width = 400*MyVar.GetScreenWidth()/MyVar.GetDesignWidth();
		
		//ssid
		MyTextView tv_ap_ssid = (MyTextView)this.findViewById(R.id.setting_tv_ap_ssid);
		tv_ap_ssid.setTextSize(font_size);
		ViewGroup.LayoutParams layout_tmp = tv_ap_ssid.getLayoutParams();
		layout_tmp.height=font_height;
		tv_ap_ssid.setLayoutParams(layout_tmp);
		//password
		MyTextView tv_ap_password = (MyTextView)this.findViewById(R.id.setting_tv_ap_password);
		tv_ap_password.setTextSize(font_size);
		layout_tmp = tv_ap_password.getLayoutParams();
		layout_tmp.height=font_height;
		tv_ap_password.setLayoutParams(layout_tmp);
		//输入框
		layout_tmp = et_ap_ssid.getLayoutParams();
		layout_tmp.height = et_height;
		layout_tmp.width = et_width;
		et_ap_ssid.setLayoutParams(layout_tmp);
		et_ap_ssid.setTextSize(font_size);
		
		layout_tmp = et_ap_password.getLayoutParams();
		layout_tmp.height = et_height;
		layout_tmp.width = et_width;
		et_ap_password.setLayoutParams(layout_tmp);
		et_ap_password.setTextSize(font_size);
		et_ap_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
		
		layout_tmp = btn_ap_set.getLayoutParams();
		layout_tmp.height = button_height;
		layout_tmp.width = button_width;
		btn_ap_set.setLayoutParams(layout_tmp);
		
		btn_ap_set.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				//保存有线配置
				String str_ap_ssid = et_ap_ssid.getText().toString().trim();	
				String str_ap_pwd = et_ap_password.getText().toString().trim();										
				SystemVar.ap_enable = true;
				SystemVar.ap_ssid = str_ap_ssid;
				SystemVar.ap_pwd = str_ap_pwd;
				SystemVar.SaveCfg();
				wifi_manager = (WifiManager)getSystemService(WIFI_SERVICE);
				if(wifi_manager.isWifiEnabled()==true){
					MyClass.ShowOkDlgWindow(SettingActivity.this, "热点设置", "请先关闭WIFI后,再开启热点!");
					return;
				}
				StopAp();
				boolean b = false;
				try {
					//热点的配置类
					WifiConfiguration apConfig = new WifiConfiguration();
					//配置热点的名称(可以在名字后面加点随机数什么的)
					apConfig.SSID = SystemVar.ap_ssid;
					//配置热点的密码
					apConfig.preSharedKey=SystemVar.ap_pwd;
		     			//通过反射调用设置热点
					Method method = wifi_manager.getClass().getMethod(
							"setWifiApEnabled", WifiConfiguration.class, boolean.class);
					//返回热点打开状态
					b = (Boolean)method.invoke(wifi_manager, apConfig, true);
				} catch (Exception e) {
					b = false;
				}
				if(b==true){
					MyClass.ShowWindowText(getBaseContext(), "热点设置成功");
				}else{
					MyClass.ShowOkDlgWindow(SettingActivity.this, "热点设置", "热点设置失败,请检查配置信息");
				}
			}							
		});
	}
	*/
	
	public boolean IsWifiApEnabled() {  
		boolean result = false;
		wifi_manager = (WifiManager)getSystemService(WIFI_SERVICE);
		try {  
			Method method = wifi_manager.getClass().getMethod("isWifiApEnabled");  
			result = (Boolean)method.invoke(wifi_manager);  
		} catch (Exception e) {  
			e.printStackTrace();
		}  
		return result;  
	}  
/*
	private boolean StopAp(){
		boolean b = false;
		if(IsWifiApEnabled()==false){
			return b;
		}
		try{
			wifi_manager = (WifiManager)getSystemService(WIFI_SERVICE);
			Method method = wifi_manager.getClass().getMethod("getWifiApConfiguration");  
			method.setAccessible(true);   
			WifiConfiguration config = (WifiConfiguration)method.invoke(wifi_manager);  
			Method method2 = wifi_manager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);  
			method2.invoke(wifi_manager, config, false);
			b = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return b;
	}*/

	//初始化IP设置
	/*
	private void InitIpSet(){
		RelativeLayout layout_system = (RelativeLayout)findViewById(R.id.layout_ipset);
		layout_system.setLayoutParams(this.layout_right_bottom);
		
		switch_network_dhcp = (Switch)this.findViewById(R.id.setting_switch_network_dhcp);
		et_ip = (MyEditText)this.findViewById(R.id.setting_et_ip);
		et_netmask = (MyEditText)this.findViewById(R.id.setting_et_netmask);
		et_gateway = (MyEditText)this.findViewById(R.id.setting_et_gateway);
		et_dns = (MyEditText)this.findViewById(R.id.setting_et_dns);
		btn_ip_modi = (MyImageButton)this.findViewById(R.id.setting_btn_ip_modi);
		btn_ip_modi.SetImages(R.drawable.main_btn_modi_up, R.drawable.main_btn_modi_down);
		
		switch_wifi_network_dhcp = (Switch)this.findViewById(R.id.setting_switch_wifi_network_dhcp);
		et_wifi_ip = (MyEditText)this.findViewById(R.id.setting_et_wifi_ip);
		et_wifi_netmask = (MyEditText)this.findViewById(R.id.setting_et_wifi_netmask);
		et_wifi_gateway = (MyEditText)this.findViewById(R.id.setting_et_wifi_gateway);
		et_wifi_dns = (MyEditText)this.findViewById(R.id.setting_et_wifi_dns);
		btn_wifi_ip_modi = (MyImageButton)this.findViewById(R.id.setting_btn_wifi_ip_modi);
		
		btn_wifi_ip_modi.SetImages(R.drawable.main_btn_modi_up, R.drawable.main_btn_modi_down);
		
		if(SystemVar.eth_dhcp==true){
			switch_network_dhcp.setChecked(false);
		}else{
			switch_network_dhcp.setChecked(true);
		}
		if(SystemVar.wifi_dhcp==true){
			switch_wifi_network_dhcp.setChecked(false);
		}else{
			switch_wifi_network_dhcp.setChecked(true);
		}
		int font_size = 32*MyVar.GetScreenHeight()/MyVar.GetDesignHeight();
		int font_height = 70*MyVar.GetScreenHeight()/MyVar.GetDesignHeight();
		
		int button_height = 84*MyVar.GetScreenHeight()/MyVar.GetDesignHeight();
		int button_width = 305*MyVar.GetScreenWidth()/MyVar.GetDesignWidth();
		
		int et_height = 70*MyVar.GetScreenHeight()/MyVar.GetDesignHeight();
		int et_width = 400*MyVar.GetScreenWidth()/MyVar.GetDesignWidth();
		
		//网络类型
		MyTextView network_type = (MyTextView)this.findViewById(R.id.setting_tv_network_type);
		network_type.setTextSize(font_size);
		ViewGroup.LayoutParams layout_tmp = network_type.getLayoutParams();
		layout_tmp.height=font_height;
		network_type.setLayoutParams(layout_tmp);
		//有线网络
		MyTextView network_type_value = (MyTextView)this.findViewById(R.id.setting_tv_network_type_value);
		network_type_value.setTextSize(font_size);
		layout_tmp = network_type_value.getLayoutParams();
		layout_tmp.height=font_height;
		network_type_value.setLayoutParams(layout_tmp);
		//IP设定模式		
		MyTextView network_dhcp = (MyTextView)this.findViewById(R.id.setting_tv_network_dhcp);
		network_dhcp.setTextSize(font_size);
		layout_tmp = network_dhcp.getLayoutParams();
		layout_tmp.height=font_height;
		network_dhcp.setLayoutParams(layout_tmp);
		//IP
		MyTextView ip = (MyTextView)this.findViewById(R.id.setting_tv_ip);
		ip.setTextSize(font_size);
		layout_tmp = ip.getLayoutParams();
		layout_tmp.height=font_height;
		ip.setLayoutParams(layout_tmp);
		//netmask
		MyTextView netmask = (MyTextView)this.findViewById(R.id.setting_tv_netmask);
		netmask.setTextSize(font_size);
		layout_tmp = netmask.getLayoutParams();
		layout_tmp.height=font_height;
		netmask.setLayoutParams(layout_tmp);
		//gateway
		MyTextView gateway = (MyTextView)this.findViewById(R.id.setting_tv_gateway);
		gateway.setTextSize(font_size);
		layout_tmp = gateway.getLayoutParams();
		layout_tmp.height=font_height;
		gateway.setLayoutParams(layout_tmp);
		//dns
		MyTextView dns = (MyTextView)this.findViewById(R.id.setting_tv_dns);
		dns.setTextSize(font_size);
		layout_tmp = dns.getLayoutParams();
		layout_tmp.height=font_height;
		dns.setLayoutParams(layout_tmp);
		//输入框
		layout_tmp = switch_network_dhcp.getLayoutParams();
		layout_tmp.height = font_height;
		switch_network_dhcp.setLayoutParams(layout_tmp);
		
		layout_tmp = et_ip.getLayoutParams();
		layout_tmp.height = et_height;
		layout_tmp.width = et_width;
		et_ip.setLayoutParams(layout_tmp);
		et_ip.setTextSize(font_size);
		
		layout_tmp = et_netmask.getLayoutParams();
		layout_tmp.height = et_height;
		layout_tmp.width = et_width;
		et_netmask.setLayoutParams(layout_tmp);
		et_netmask.setTextSize(font_size);
		
		layout_tmp = et_gateway.getLayoutParams();
		layout_tmp.height = et_height;
		layout_tmp.width = et_width;
		et_gateway.setLayoutParams(layout_tmp);
		et_gateway.setTextSize(font_size);
		
		layout_tmp = et_dns.getLayoutParams();
		layout_tmp.height = et_height;
		layout_tmp.width = et_width;
		et_dns.setLayoutParams(layout_tmp);
		et_dns.setTextSize(font_size);
		
		layout_tmp = btn_ip_modi.getLayoutParams();
		layout_tmp.height = button_height;
		layout_tmp.width = button_width;
		btn_ip_modi.setLayoutParams(layout_tmp);
		
		//wifi组件UI
		//网络类型
		MyTextView wifi_network_type = (MyTextView)this.findViewById(R.id.setting_tv_wifi_network_type);
		wifi_network_type.setTextSize(font_size);		
		if(MyVar.SYSTEM_LAYOUT_TYPE==1){
			RelativeLayout.LayoutParams layout_tmp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layout_tmp2.height=font_height;		
			layout_tmp2.addRule(RelativeLayout.BELOW, btn_ip_modi.getId());
			layout_tmp2.addRule(RelativeLayout.ALIGN_LEFT,network_type.getId());
			layout_tmp2.topMargin = 20;
			wifi_network_type.setLayoutParams(layout_tmp2);
		}else{
			layout_tmp = wifi_network_type.getLayoutParams();
			layout_tmp.height=font_height;	
			wifi_network_type.setLayoutParams(layout_tmp);
		}
		
		//无线网络
		MyTextView wifi_network_type_value = (MyTextView)this.findViewById(R.id.setting_tv_wifi_network_type_value);
		wifi_network_type_value.setTextSize(font_size);
		layout_tmp = wifi_network_type_value.getLayoutParams();
		layout_tmp.height=font_height;
		wifi_network_type_value.setLayoutParams(layout_tmp);
		//IP设定模式		
		MyTextView wifi_network_dhcp = (MyTextView)this.findViewById(R.id.setting_tv_wifi_network_dhcp);
		wifi_network_dhcp.setTextSize(font_size);
		layout_tmp = wifi_network_dhcp.getLayoutParams();
		layout_tmp.height=font_height;
		wifi_network_dhcp.setLayoutParams(layout_tmp);
		//IP
		MyTextView wifi_ip = (MyTextView)this.findViewById(R.id.setting_tv_wifi_ip);
		wifi_ip.setTextSize(font_size);
		layout_tmp = wifi_ip.getLayoutParams();
		layout_tmp.height=font_height;
		wifi_ip.setLayoutParams(layout_tmp);
		//netmask
		MyTextView wifi_netmask = (MyTextView)this.findViewById(R.id.setting_tv_wifi_netmask);
		wifi_netmask.setTextSize(font_size);
		layout_tmp = wifi_netmask.getLayoutParams();
		layout_tmp.height=font_height;
		wifi_netmask.setLayoutParams(layout_tmp);
		//gateway
		MyTextView wifi_gateway = (MyTextView)this.findViewById(R.id.setting_tv_wifi_gateway);
		wifi_gateway.setTextSize(font_size);
		layout_tmp = wifi_gateway.getLayoutParams();
		layout_tmp.height=font_height;
		wifi_gateway.setLayoutParams(layout_tmp);
		//dns
		MyTextView wifi_dns = (MyTextView)this.findViewById(R.id.setting_tv_wifi_dns);
		wifi_dns.setTextSize(font_size);
		layout_tmp = wifi_dns.getLayoutParams();
		layout_tmp.height=font_height;
		wifi_dns.setLayoutParams(layout_tmp);
		//输入框
		layout_tmp = switch_wifi_network_dhcp.getLayoutParams();
		layout_tmp.height = font_height;
		switch_wifi_network_dhcp.setLayoutParams(layout_tmp);
		
		layout_tmp = et_wifi_ip.getLayoutParams();
		layout_tmp.height = et_height;
		layout_tmp.width = et_width;
		et_wifi_ip.setLayoutParams(layout_tmp);
		et_wifi_ip.setTextSize(font_size);
		
		layout_tmp = et_wifi_netmask.getLayoutParams();
		layout_tmp.height = et_height;
		layout_tmp.width = et_width;
		et_wifi_netmask.setLayoutParams(layout_tmp);
		et_wifi_netmask.setTextSize(font_size);
		
		layout_tmp = et_wifi_gateway.getLayoutParams();
		layout_tmp.height = et_height;
		layout_tmp.width = et_width;
		et_wifi_gateway.setLayoutParams(layout_tmp);
		et_wifi_gateway.setTextSize(font_size);
		
		layout_tmp = et_wifi_dns.getLayoutParams();
		layout_tmp.height = et_height;
		layout_tmp.width = et_width;
		et_wifi_dns.setLayoutParams(layout_tmp);
		et_wifi_dns.setTextSize(font_size);
		
		layout_tmp = btn_wifi_ip_modi.getLayoutParams();
		layout_tmp.height = button_height;
		layout_tmp.width = button_width;
		btn_wifi_ip_modi.setLayoutParams(layout_tmp);
		
		//按钮事件
		switch_network_dhcp.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1==false){
					et_ip.setEnabled(false);
					et_netmask.setEnabled(false);
					et_gateway.setEnabled(false);
					et_dns.setEnabled(false);
					btn_ip_modi.setFocusable(false);
					et_ip.setFocusable(false);
					et_netmask.setFocusable(false);
					et_gateway.setFocusable(false);
					et_dns.setFocusable(false);
					SystemVar.eth_dhcp = true;
					SystemVar.SaveCfg();
					//重新连接网络
					IpAddressSet.SetAutoDhcp(IpAddressSet.ETH0);
					MyClass.ShowWindowText(getBaseContext(), "修改成功");
					RefreNetworkInfo();
				}else{
					et_ip.setEnabled(true);
					et_netmask.setEnabled(true);
					et_gateway.setEnabled(true);
					et_dns.setEnabled(true);
					btn_ip_modi.setFocusable(true);
					et_ip.setFocusable(true);
					et_netmask.setFocusable(true);
					et_gateway.setFocusable(true);
					et_dns.setFocusable(true);
				}				
			}
		});
		switch_wifi_network_dhcp.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1==false){
		  			et_wifi_ip.setEnabled(false);
		  			et_wifi_netmask.setEnabled(false);
		  			et_wifi_gateway.setEnabled(false);
		  			et_wifi_dns.setEnabled(false);
		  			btn_wifi_ip_modi.setFocusable(false);
		  			et_wifi_ip.setFocusable(false);
		  			et_wifi_netmask.setFocusable(false);
		  			et_wifi_gateway.setFocusable(false);
		  			et_wifi_dns.setFocusable(false);
		  			SystemVar.wifi_dhcp = true;
					SystemVar.SaveCfg();
					//重新连接WIFI					
					//WifiManager wifi_manager_tmp = (WifiManager)getSystemService(WIFI_SERVICE);					
					//wifi_manager_tmp.disconnect();
					//wifi_manager_tmp.reconnect();
					IpAddressSet.SetAutoDhcp(IpAddressSet.WLAN0);
					MyClass.ShowWindowText(getBaseContext(), "修改成功");
					RefreNetworkInfo();
		  		}else{
		  			et_wifi_ip.setEnabled(true);
		  			et_wifi_netmask.setEnabled(true);
		  			et_wifi_gateway.setEnabled(true);
		  			et_wifi_dns.setEnabled(true);
		  			btn_wifi_ip_modi.setFocusable(true);
		  			et_wifi_ip.setFocusable(true);
		  			et_wifi_netmask.setFocusable(true);
		  			et_wifi_gateway.setFocusable(true);
		  			et_wifi_dns.setFocusable(true);
		  		}			
			}
		});
		btn_wifi_ip_modi.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				//保存有线配置
				if(switch_wifi_network_dhcp.isChecked()==false){
					//SystemVar.wifi_dhcp = true;
					//SystemVar.SaveCfg();
					//重新连接WIFI					
					//WifiManager wifi_manager_tmp = (WifiManager)getSystemService(WIFI_SERVICE);
					//wifi_manager_tmp.reconnect();
				}else{					
					String ip = et_wifi_ip.getText().toString().trim();
					if(MyClass.CheckIpVaild(ip)==false){
						MyClass.ShowOkDlgWindow(SettingActivity.this, "IP设定", "IP地址错误"+ip);
						return;
					}
					String netmask = et_wifi_netmask.getText().toString().trim();
					if(MyClass.CheckIpVaild(netmask)==false){
						MyClass.ShowOkDlgWindow(SettingActivity.this, "IP设定", "子网掩码错误");
						return;
					}					
					String gateway = et_wifi_gateway.getText().toString().trim();
					if(MyClass.CheckIpVaild(gateway)==false){
						MyClass.ShowOkDlgWindow(SettingActivity.this, "IP设定", "网关地址错误");
						return;
					}									
					String dns = et_wifi_dns.getText().toString().trim();
					if(MyClass.CheckIpVaild(dns)==false){
						MyClass.ShowOkDlgWindow(SettingActivity.this, "IP设定", "DNS地址错误");
						return;
					}
					SystemVar.wifi_ip = ip;
					SystemVar.wifi_gateway = gateway;
					SystemVar.wifi_netmask = netmask;
					SystemVar.wifi_dns = dns;
					SystemVar.wifi_dhcp = false;
					SystemVar.SaveCfg();
					//设置无线IP
					IpAddressSet.SetIpAddress(IpAddressSet.WLAN0, SystemVar.wifi_ip, SystemVar.wifi_netmask);
					IpAddressSet.SetGateWay(IpAddressSet.WLAN0, SystemVar.wifi_gateway);
					IpAddressSet.SetDns(SystemVar.wifi_dns);
					//
					MyClass.ShowWindowText(getBaseContext(), "修改成功");
					RefreNetworkInfo();
				}											
			}
		});
		
		btn_ip_modi.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				//保存有线配置
				if(switch_network_dhcp.isChecked()==false){
					//SystemVar.eth_dhcp = true;
				}else{
					String ip = et_ip.getText().toString().trim();
					if(MyClass.CheckIpVaild(ip)==false){
						MyClass.ShowOkDlgWindow(SettingActivity.this, "IP设定", "IP地址错误");
						return;
					}
					String netmask = et_netmask.getText().toString().trim();
					if(MyClass.CheckIpVaild(netmask)==false){
						MyClass.ShowOkDlgWindow(SettingActivity.this, "IP设定", "子网掩码错误");
						return;
					}					
					String gateway = et_gateway.getText().toString().trim();
					if(MyClass.CheckIpVaild(gateway)==false){
						MyClass.ShowOkDlgWindow(SettingActivity.this, "IP设定", "网关地址错误");
						return;
					}									
					String dns = et_dns.getText().toString().trim();
					if(MyClass.CheckIpVaild(dns)==false){
						MyClass.ShowOkDlgWindow(SettingActivity.this, "IP设定", "DNS地址错误");
						return;
					}					
					SystemVar.eth_dhcp = false;
					SystemVar.eth_ip = ip;
					SystemVar.eth_gateway = gateway;
					SystemVar.eth_netmask = netmask;
					SystemVar.eth_dns = dns;
					SystemVar.SaveCfg();
					//设置有线IP
					IpAddressSet.SetIpAddress(IpAddressSet.ETH0, SystemVar.eth_ip, SystemVar.eth_netmask);
					IpAddressSet.SetGateWay(IpAddressSet.ETH0, SystemVar.eth_gateway);
					IpAddressSet.SetDns(SystemVar.eth_dns);
					MyClass.ShowWindowText(getBaseContext(), "修改成功");
					RefreNetworkInfo();
				}							
			}
		});
	}
	*/
	//初始化自定义TAB按钮
	private void InitCommonView(int cur_activity_id){
		//标题背景
		MyImageView iv_title_bg = (MyImageView)this.findViewById(R.id.iv_title_bg);
		iv_title_bg.setVisibility(View.VISIBLE);
		//按钮背景
		MyImageView iv_title_user_bg = (MyImageView)this.findViewById(R.id.iv_title_user_bg);
		iv_title_user_bg.setVisibility(View.VISIBLE);
		MyImageView iv_title_app_bg = (MyImageView)this.findViewById(R.id.iv_title_app_bg);
		iv_title_app_bg.setVisibility(View.VISIBLE);
		MyImageView iv_title_setting_bg = (MyImageView)this.findViewById(R.id.iv_title_setting_bg);
		iv_title_setting_bg.setVisibility(View.VISIBLE);
		//按钮文字信息
		MyTextView tv_title_user = (MyTextView)this.findViewById(R.id.tv_title_user);
		tv_title_user.setTextColor(0xffdafffc);
		tv_title_user.setGravity(Gravity.CENTER);
		tv_title_user.setVisibility(View.VISIBLE);
		tv_title_user.setFocusable(true);
		MyTextView tv_title_app = (MyTextView)this.findViewById(R.id.tv_title_app);
		tv_title_app.setTextColor(0xffdafffc);
		tv_title_app.setGravity(Gravity.CENTER);
		tv_title_app.setVisibility(View.VISIBLE);
		tv_title_app.setFocusable(true);
		MyTextView tv_title_setting = (MyTextView)this.findViewById(R.id.tv_title_setting);
		tv_title_setting.setTextColor(0xffdafffc);
		tv_title_setting.setGravity(Gravity.CENTER);
		tv_title_setting.setVisibility(View.VISIBLE);
		tv_title_setting.setFocusable(true);
		
		if(MyVar.SYSTEM_LAYOUT_TYPE==1){
			iv_title_bg.SetImages(R.drawable.iv_title_bg);
			iv_title_user_bg.SetImages(R.drawable.iv_title_item_bg);
			iv_title_app_bg.SetImages(R.drawable.iv_title_item_bg);
			iv_title_setting_bg.SetImages(R.drawable.iv_title_item_bg);			
			iv_title_bg.InitSize(0, 0, MyVar.GetDesignWidth(), 122);
			iv_title_user_bg.InitSize(0, 0, 361, 120);
			iv_title_app_bg.InitSize(360, 0, 361, 120);
			iv_title_setting_bg.InitSize(720, 0, 361, 120);
			tv_title_user.InitSize(0, 0, 360, 120,36);
			tv_title_app.InitSize(360, 0, 360, 120,36);
			tv_title_setting.InitSize(720, 0, 360, 120,36);
		}else{
			iv_title_bg.SetImages(R.drawable.iv_title_bg_v);
			iv_title_user_bg.SetImages(R.drawable.iv_title_item_bg_v);
			iv_title_app_bg.SetImages(R.drawable.iv_title_item_bg_v);
			iv_title_setting_bg.SetImages(R.drawable.iv_title_item_bg_v);
			iv_title_bg.InitSize(0, 0, MyVar.GetDesignWidth(), 60);
			iv_title_user_bg.InitSize(27, 0, 587, 60);
			iv_title_app_bg.InitSize(640+27, 0, 587, 60);
			iv_title_setting_bg.InitSize(1280+27, 0, 587, 60);
			tv_title_user.InitSize(0, 0, 640, 60,36);
			tv_title_app.InitSize(640, 0, 640, 60,36);
			tv_title_setting.InitSize(1280, 0, 640, 60,36);
		}
		//控制按钮状态
		if(cur_activity_id==MyVar.USER_PAGE_ID){
			iv_title_setting_bg.setVisibility(View.GONE);
			iv_title_app_bg.setVisibility(View.GONE);
			tv_title_user.setTextColor(0xffffd150);
			tv_title_app.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {					
					if(MyVar.login_flag==true){
						MyClass.ShowAppActivity(MyVar.CURRENT_PAGE_ID);
					}else{
						MyClass.ShowOkDlgWindow(SettingActivity.this,"操作提示", "请登录后再访问应用库");
					}
				}
			});
			tv_title_setting.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {					
					MyClass.ShowSettingActivity(MyVar.CURRENT_PAGE_ID);
				}
			});
		}else if(cur_activity_id==MyVar.APP_PAGE_ID){
			iv_title_setting_bg.setVisibility(View.GONE);
			iv_title_user_bg.setVisibility(View.GONE);
			tv_title_app.setTextColor(0xffffd150);
			tv_title_app.requestFocus();
			tv_title_user.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {					
					MyClass.ShowUserActivity(MyVar.CURRENT_PAGE_ID);
				}
			});
			tv_title_setting.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {					
					MyClass.ShowSettingActivity(MyVar.CURRENT_PAGE_ID);
				}
			});
		}else if(cur_activity_id==MyVar.SETTING_PAGE_ID){
			iv_title_user_bg.setVisibility(View.GONE);
			iv_title_app_bg.setVisibility(View.GONE);
			tv_title_setting.setTextColor(0xffffd150);
			tv_title_setting.requestFocus();
			tv_title_app.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {					
					if(MyVar.login_flag==true){
						MyClass.ShowAppActivity(MyVar.CURRENT_PAGE_ID);
					}else{
						MyClass.ShowOkDlgWindow(SettingActivity.this,"操作提示", "请登录后再访问应用库");
					}
				}
			});
			tv_title_user.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {					
					MyClass.ShowUserActivity(MyVar.CURRENT_PAGE_ID);
				}
			});
		}
	}
	
	private void InitView(){
		InitCommonView(MyVar.CURRENT_PAGE_ID);
		//用户信息背景
		iv_setting_message_bg = (MyImageView)this.findViewById(R.id.setting_iv_setting_message_bg);
		iv_setting_message_bg.SetImages(R.drawable.setting_iv_setting_message_bg);
		iv_setting_message_bg.setVisibility(View.VISIBLE);
		
		//左边标题
		int y = 213;
		iv_setting_ico_bg = (MyImageView)this.findViewById(R.id.setting_iv_setting_ico_bg);
		iv_setting_ico_bg.SetImages(R.drawable.setting_iv_setting_ico_bg);
		iv_setting_ico_bg.setVisibility(View.VISIBLE);
		
		iv_setting_system_ico = (MyImageView)this.findViewById(R.id.setting_iv_setting_system_ico);
		iv_setting_system_ico.SetImages(R.drawable.setting_iv_setting_system_ico);
		iv_setting_system_ico.setVisibility(View.VISIBLE);
		
		iv_setting_network_ico = (MyImageView)this.findViewById(R.id.setting_iv_setting_network_ico);
		iv_setting_network_ico.SetImages(R.drawable.setting_iv_setting_network_ico);
		iv_setting_network_ico.setVisibility(View.VISIBLE);
		/*
		iv_setting_other_ico = (MyImageView)this.findViewById(R.id.setting_iv_setting_other_ico);
		iv_setting_other_ico.SetImages(R.drawable.setting_iv_setting_other_ico);
		iv_setting_other_ico.setVisibility(View.VISIBLE);
		*/
		/*  去掉热点设置问题
		iv_setting_ap_ico = (MyImageView)this.findViewById(R.id.setting_iv_setting_ap_ico);
		iv_setting_ap_ico.SetImages(R.drawable.setting_iv_setting_other_ico);
		iv_setting_ap_ico.setVisibility(View.GONE);
		*/
		
		iv_setting_app_ico = (MyImageView)this.findViewById(R.id.setting_iv_setting_app_ico);
		iv_setting_app_ico.SetImages(R.drawable.setting_iv_setting_other_ico);
		iv_setting_app_ico.setVisibility(View.VISIBLE);
		/*
		iv_setting_layout_type_ico = (MyImageView)this.findViewById(R.id.setting_iv_setting_layout_type_ico);
		iv_setting_layout_type_ico.SetImages(R.drawable.setting_iv_setting_other_ico);
		iv_setting_layout_type_ico.setVisibility(View.VISIBLE);
		*/
		iv_setting_about_ico = (MyImageView)this.findViewById(R.id.setting_iv_setting_about_ico);
		iv_setting_about_ico.SetImages(R.drawable.setting_iv_setting_about_ico);
		iv_setting_about_ico.setVisibility(View.VISIBLE);
		/* 配置图片系统设置显示内容 */
		iv_setting_systemconfig_ico = (MyImageView)this.findViewById(R.id.setting_iv_setting_systemconfig_ico);
		iv_setting_systemconfig_ico.SetImages(R.drawable.setting_iv_setting_system_ico);
		iv_setting_systemconfig_ico.setVisibility(View.VISIBLE);

		//显示设置
		tv_setting_system = (MyTextView)this.findViewById(R.id.setting_tv_setting_system);
		tv_setting_system.setTextColor(0xfffeee87);
		tv_setting_system.setGravity(Gravity.CENTER);
		tv_setting_system.setVisibility(View.VISIBLE);
		tv_setting_system.setFocusable(true);
		tv_setting_system.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				RelativeLayout layout_wifi = (RelativeLayout)findViewById(R.id.layout_wifi);
				layout_wifi.setVisibility(View.GONE);
				LinearLayout layout_system = (LinearLayout)findViewById(R.id.setting_layout_system_info);
				layout_system.setVisibility(View.GONE);
				//RelativeLayout layout_ipset = (RelativeLayout)findViewById(R.id.layout_ipset);
				//layout_ipset.setVisibility(View.GONE);
				//RelativeLayout layout_apset = (RelativeLayout)findViewById(R.id.layout_apset);
				//layout_apset.setVisibility(View.GONE);
				lv_app_list.setVisibility(View.GONE);
				/*
				RelativeLayout layout_layout_type = (RelativeLayout)findViewById(R.id.layout_layout_modi);
				layout_layout_type.setVisibility(View.GONE);
				*/
				iv_setting_other_msg.setVisibility(View.GONE);
				tv_setting_about.setTextColor(0xffdafffc);
//				tv_setting_other.setTextColor(0xffdafffc);
				tv_setting_network.setTextColor(0xffdafffc);
				tv_setting_system.setTextColor(0xffdafffc);
//				tv_setting_ap.setTextColor(0xffdafffc);
				tv_setting_app.setTextColor(0xffdafffc);
				//tv_setting_layout_type.setTextColor(0xffdafffc);
				tv_setting_systemconfig.setTextColor(0xffdafffc);
				if(MyVar.SYSTEM_LAYOUT_TYPE==0){
					iv_setting_ico_bg.InitSize(0, 213+114*0, 420, 114);
				}else{
					iv_setting_ico_bg.InitSize(0, 213+114*0, 287, 114);
				}				
				tv_setting_system.setTextColor(0xfffeee87);
				layout_system.setVisibility(View.VISIBLE);
			}
		});
		
		tv_setting_network = (MyTextView)this.findViewById(R.id.setting_tv_setting_network);
		tv_setting_network.setTextColor(0xffdafffc);
		tv_setting_network.setGravity(Gravity.CENTER);
		tv_setting_network.setVisibility(View.VISIBLE);
		tv_setting_network.setFocusable(true);
		tv_setting_network.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				RelativeLayout layout_wifi = (RelativeLayout)findViewById(R.id.layout_wifi);
				layout_wifi.setVisibility(View.GONE);
				LinearLayout layout_system = (LinearLayout)findViewById(R.id.setting_layout_system_info);
				layout_system.setVisibility(View.GONE);
				//RelativeLayout layout_ipset = (RelativeLayout)findViewById(R.id.layout_ipset);
				//layout_ipset.setVisibility(View.GONE);
		//		RelativeLayout layout_apset = (RelativeLayout)findViewById(R.id.layout_apset);
		//		layout_apset.setVisibility(View.GONE);
				lv_app_list.setVisibility(View.GONE);
				/*
				RelativeLayout layout_layout_type = (RelativeLayout)findViewById(R.id.layout_layout_modi);
				layout_layout_type.setVisibility(View.GONE);
				*/
				iv_setting_other_msg.setVisibility(View.GONE);
				tv_setting_about.setTextColor(0xffdafffc);
		//		tv_setting_other.setTextColor(0xffdafffc);
				tv_setting_network.setTextColor(0xffdafffc);
				tv_setting_system.setTextColor(0xffdafffc);
		//		tv_setting_ap.setTextColor(0xffdafffc);
				tv_setting_app.setTextColor(0xffdafffc);
				tv_setting_systemconfig.setTextColor(0xffdafffc);
				//tv_setting_layout_type.setTextColor(0xffdafffc);
				
				if(MyVar.SYSTEM_LAYOUT_TYPE==0){
					iv_setting_ico_bg.InitSize(0, 213+114*1, 420, 114);
				}else{
					iv_setting_ico_bg.InitSize(0, 213+114*1, 287, 114);
				}	
				tv_setting_network.setTextColor(0xfffeee87);
				layout_wifi.setVisibility(View.VISIBLE);
				
				WifiInit();
			}
		});
		/*
		tv_setting_other = (MyTextView)this.findViewById(R.id.setting_tv_setting_other);
		tv_setting_other.setTextColor(0xffdafffc);
		tv_setting_other.setGravity(Gravity.CENTER);
		tv_setting_other.setVisibility(View.VISIBLE);
		tv_setting_other.setFocusable(true);
		tv_setting_other.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				RelativeLayout layout_wifi = (RelativeLayout)findViewById(R.id.layout_wifi);
				layout_wifi.setVisibility(View.GONE);
				LinearLayout layout_system = (LinearLayout)findViewById(R.id.setting_layout_system_info);
				layout_system.setVisibility(View.GONE);
				RelativeLayout layout_ipset = (RelativeLayout)findViewById(R.id.layout_ipset);
				layout_ipset.setVisibility(View.GONE);
				RelativeLayout layout_apset = (RelativeLayout)findViewById(R.id.layout_apset);
				layout_apset.setVisibility(View.GONE);
				lv_app_list.setVisibility(View.GONE);
				RelativeLayout layout_layout_type = (RelativeLayout)findViewById(R.id.layout_layout_modi);
				layout_layout_type.setVisibility(View.GONE);
				iv_setting_other_msg.setVisibility(View.GONE);
				tv_setting_about.setTextColor(0xffdafffc);
				tv_setting_other.setTextColor(0xffdafffc);
				tv_setting_network.setTextColor(0xffdafffc);
				tv_setting_system.setTextColor(0xffdafffc);
				tv_setting_ap.setTextColor(0xffdafffc);
				tv_setting_app.setTextColor(0xffdafffc);
				//tv_setting_layout_type.setTextColor(0xffdafffc);
				
				if(MyVar.SYSTEM_LAYOUT_TYPE==0){
					iv_setting_ico_bg.InitSize(0, 213+114*2, 420, 114);
				}else{
					iv_setting_ico_bg.InitSize(0, 213+114*2, 287, 114);
				}	
				tv_setting_other.setTextColor(0xfffeee87);
				layout_ipset.setVisibility(View.VISIBLE);
				
				//读取当前网络信息，并显示
				ShowNetworkInfo();
			}
		});
		*/
		/*
		tv_setting_ap = (MyTextView)this.findViewById(R.id.setting_tv_setting_ap);
		tv_setting_ap.setTextColor(0xffdafffc);
		tv_setting_ap.setGravity(Gravity.CENTER);
		tv_setting_ap.setVisibility(View.GONE);
		tv_setting_ap.setFocusable(true);
		tv_setting_ap.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				RelativeLayout layout_wifi = (RelativeLayout)findViewById(R.id.layout_wifi);
				layout_wifi.setVisibility(View.GONE);
				LinearLayout layout_system = (LinearLayout)findViewById(R.id.setting_layout_system_info);
				layout_system.setVisibility(View.GONE);
				RelativeLayout layout_ipset = (RelativeLayout)findViewById(R.id.layout_ipset);
				layout_ipset.setVisibility(View.GONE);
				RelativeLayout layout_apset = (RelativeLayout)findViewById(R.id.layout_apset);
				layout_apset.setVisibility(View.GONE);
				lv_app_list.setVisibility(View.GONE);
				RelativeLayout layout_layout_type = (RelativeLayout)findViewById(R.id.layout_layout_modi);
				layout_layout_type.setVisibility(View.GONE);
				iv_setting_other_msg.setVisibility(View.GONE);
				tv_setting_about.setTextColor(0xffdafffc);
				tv_setting_other.setTextColor(0xffdafffc);
				tv_setting_network.setTextColor(0xffdafffc);
				tv_setting_system.setTextColor(0xffdafffc);
				tv_setting_ap.setTextColor(0xffdafffc);
				tv_setting_app.setTextColor(0xffdafffc);
				//tv_setting_layout_type.setTextColor(0xffdafffc);
				
				if(MyVar.SYSTEM_LAYOUT_TYPE==0){
					iv_setting_ico_bg.InitSize(0, 213+114*3, 420, 114);
				}else{
					iv_setting_ico_bg.InitSize(0, 213+114*3, 287, 114);
				}
				tv_setting_ap.setTextColor(0xfffeee87);
				layout_apset.setVisibility(View.VISIBLE);
				
				//读取当前网络信息，并显示
				ShowApInfo();
			}
		});
		*/
		tv_setting_app = (MyTextView)this.findViewById(R.id.setting_tv_setting_app);
		tv_setting_app.setTextColor(0xffdafffc);
		tv_setting_app.setGravity(Gravity.CENTER);
		tv_setting_app.setVisibility(View.VISIBLE);
		tv_setting_app.setFocusable(true);
		tv_setting_app.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				RelativeLayout layout_wifi = (RelativeLayout)findViewById(R.id.layout_wifi);
				layout_wifi.setVisibility(View.GONE);
				LinearLayout layout_system = (LinearLayout)findViewById(R.id.setting_layout_system_info);
				layout_system.setVisibility(View.GONE);
				//RelativeLayout layout_ipset = (RelativeLayout)findViewById(R.id.layout_ipset);
				//layout_ipset.setVisibility(View.GONE);
				//RelativeLayout layout_apset = (RelativeLayout)findViewById(R.id.layout_apset);
				//layout_apset.setVisibility(View.GONE);
				lv_app_list.setVisibility(View.GONE);
				/*
				RelativeLayout layout_layout_type = (RelativeLayout)findViewById(R.id.layout_layout_modi);
				layout_layout_type.setVisibility(View.GONE);
				*/
				iv_setting_other_msg.setVisibility(View.GONE);
				tv_setting_about.setTextColor(0xffdafffc);
				//tv_setting_other.setTextColor(0xffdafffc);
				tv_setting_network.setTextColor(0xffdafffc);
				tv_setting_system.setTextColor(0xffdafffc);
//				tv_setting_ap.setTextColor(0xffdafffc);
				tv_setting_app.setTextColor(0xffdafffc);
				//tv_setting_layout_type.setTextColor(0xffdafffc);
				tv_setting_systemconfig.setTextColor(0xffdafffc);
				if(MyVar.SYSTEM_LAYOUT_TYPE==0){
					iv_setting_ico_bg.InitSize(0, 213+114*2, 420, 114);
				}else{
					iv_setting_ico_bg.InitSize(0, 213+114*2, 287, 114);
				}	
				tv_setting_app.setTextColor(0xfffeee87);
				
				//读取当前网络信息，并显示
				lv_app_list.setVisibility(View.VISIBLE);
				//ShowNetworkInfo();
			}
		});
		/*
		tv_setting_layout_type = (MyTextView)this.findViewById(R.id.setting_tv_setting_layout_type);
		tv_setting_layout_type.setTextColor(0xffdafffc);
		tv_setting_layout_type.setGravity(Gravity.CENTER);
		tv_setting_layout_type.setVisibility(View.VISIBLE);
		tv_setting_layout_type.setFocusable(true);
		tv_setting_layout_type.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				RelativeLayout layout_wifi = (RelativeLayout)findViewById(R.id.layout_wifi);
				layout_wifi.setVisibility(View.GONE);
				LinearLayout layout_system = (LinearLayout)findViewById(R.id.setting_layout_system_info);
				layout_system.setVisibility(View.GONE);
				RelativeLayout layout_ipset = (RelativeLayout)findViewById(R.id.layout_ipset);
				layout_ipset.setVisibility(View.GONE);
				RelativeLayout layout_apset = (RelativeLayout)findViewById(R.id.layout_apset);
				layout_apset.setVisibility(View.GONE);
				lv_app_list.setVisibility(View.GONE);
				RelativeLayout layout_layout_type = (RelativeLayout)findViewById(R.id.layout_layout_modi);
				layout_layout_type.setVisibility(View.GONE);
				iv_setting_other_msg.setVisibility(View.GONE);
				tv_setting_about.setTextColor(0xffdafffc);
				tv_setting_other.setTextColor(0xffdafffc);
				tv_setting_systemconfig.setTextColor(0xffdafffc);
				tv_setting_network.setTextColor(0xffdafffc);
				tv_setting_system.setTextColor(0xffdafffc);
				tv_setting_ap.setTextColor(0xffdafffc);
				tv_setting_app.setTextColor(0xffdafffc);
				tv_setting_layout_type.setTextColor(0xffdafffc);
				
				if(MyVar.SYSTEM_LAYOUT_TYPE==0){
					iv_setting_ico_bg.InitSize(0, 213+114*4, 420, 114);
				}else{
					iv_setting_ico_bg.InitSize(0, 213+114*4, 287, 114);
				}	
				tv_setting_layout_type.setTextColor(0xfffeee87);
				layout_layout_type.setVisibility(View.VISIBLE);
			}
		});
		*/
		tv_setting_about = (MyTextView)this.findViewById(R.id.setting_tv_setting_about);
		tv_setting_about.setTextColor(0xffdafffc);
		tv_setting_about.setGravity(Gravity.CENTER);
		tv_setting_about.setVisibility(View.VISIBLE);
		tv_setting_about.setFocusable(true);
		tv_setting_about.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				RelativeLayout layout_wifi = (RelativeLayout)findViewById(R.id.layout_wifi);
				layout_wifi.setVisibility(View.GONE);
				LinearLayout layout_system = (LinearLayout)findViewById(R.id.setting_layout_system_info);
				layout_system.setVisibility(View.GONE);
				//RelativeLayout layout_ipset = (RelativeLayout)findViewById(R.id.layout_ipset);
				//layout_ipset.setVisibility(View.GONE);
				//RelativeLayout layout_apset = (RelativeLayout)findViewById(R.id.layout_apset);
				//layout_apset.setVisibility(View.GONE);
				lv_app_list.setVisibility(View.GONE);
				/*
				RelativeLayout layout_layout_type = (RelativeLayout)findViewById(R.id.layout_layout_modi);
				layout_layout_type.setVisibility(View.GONE);
				*/
				iv_setting_other_msg.setVisibility(View.GONE);
				tv_setting_about.setTextColor(0xffdafffc);
//				tv_setting_other.setTextColor(0xffdafffc);
				tv_setting_network.setTextColor(0xffdafffc);
				tv_setting_system.setTextColor(0xffdafffc);
//				tv_setting_ap.setTextColor(0xffdafffc);
				tv_setting_app.setTextColor(0xffdafffc);
				//tv_setting_layout_type.setTextColor(0xffdafffc);
				tv_setting_systemconfig.setTextColor(0xffdafffc);
				if(MyVar.SYSTEM_LAYOUT_TYPE==0){
					iv_setting_ico_bg.InitSize(0, 213+114*4, 420, 114);
				}else{
					iv_setting_ico_bg.InitSize(0, 213+114*4, 287, 114);
				}	
				tv_setting_about.setTextColor(0xfffeee87);
				iv_setting_other_msg.setVisibility(View.VISIBLE);
				
			}
		});

		tv_setting_systemconfig = (MyTextView)this.findViewById(R.id.setting_tv_setting_systemconfig);
		tv_setting_systemconfig.setTextColor(0xffdafffc);
		tv_setting_systemconfig.setGravity(Gravity.CENTER);
		tv_setting_systemconfig.setVisibility(View.VISIBLE);
		tv_setting_systemconfig.setFocusable(true);
		tv_setting_systemconfig.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				RelativeLayout layout_wifi = (RelativeLayout)findViewById(R.id.layout_wifi);
				layout_wifi.setVisibility(View.GONE);
				LinearLayout layout_system = (LinearLayout)findViewById(R.id.setting_layout_system_info);
				layout_system.setVisibility(View.GONE);
				//RelativeLayout layout_ipset = (RelativeLayout)findViewById(R.id.layout_ipset);
				//layout_ipset.setVisibility(View.GONE);
				//RelativeLayout layout_apset = (RelativeLayout)findViewById(R.id.layout_apset);
				//layout_apset.setVisibility(View.GONE);
				lv_app_list.setVisibility(View.GONE);
				/*
				RelativeLayout layout_layout_type = (RelativeLayout)findViewById(R.id.layout_layout_modi);
				layout_layout_type.setVisibility(View.GONE);
				*/
				iv_setting_other_msg.setVisibility(View.GONE);
				tv_setting_about.setTextColor(0xffdafffc);
//				tv_setting_other.setTextColor(0xffdafffc);
				tv_setting_network.setTextColor(0xffdafffc);
				tv_setting_system.setTextColor(0xffdafffc);
//				tv_setting_ap.setTextColor(0xffdafffc);
				tv_setting_app.setTextColor(0xffdafffc);
				//tv_setting_layout_type.setTextColor(0xffdafffc);

				if(MyVar.SYSTEM_LAYOUT_TYPE==0){
					iv_setting_ico_bg.InitSize(0, 213+114*3, 420, 114);
				}else{
					iv_setting_ico_bg.InitSize(0, 213+114*3, 287, 114);
				}
				tv_setting_systemconfig.setTextColor(0xfffeee87);
				Intent mIntent = new Intent(Settings.ACTION_SETTINGS);
				startActivityForResult(mIntent,0x200);
			}
		});
		
		iv_setting_other_msg = (MyImageView)this.findViewById(R.id.setting_iv_setting_other_msg);
		iv_setting_other_msg.SetImages(R.drawable.setting_iv_setting_other_msg);
		iv_setting_other_msg.setVisibility(View.GONE);
		if(MyVar.SYSTEM_LAYOUT_TYPE==1){
			iv_setting_message_bg.InitSize(287, 122, 795, MyVar.GetDesignHeight()-120);			
			iv_setting_ico_bg.InitSize(0, y, 287, 114);
			iv_setting_system_ico.InitSize(36, y+32,58, 49);
			iv_setting_network_ico.InitSize(36, y+114+32, 58, 49);
			//iv_setting_other_ico.InitSize(36, y+114*2+32, 58, 49);
			//iv_setting_ap_ico.InitSize(36, y+114*3+32, 58, 49);
			iv_setting_app_ico.InitSize(36, y+114*2+32, 58, 49);
			//iv_setting_layout_type_ico.InitSize(36, y+114*4+32, 58, 49);
			iv_setting_about_ico.InitSize(36, y+114*4+32, 58, 49);
			iv_setting_systemconfig_ico.InitSize(36,y+114*3+32,58,49);
			tv_setting_system.InitSize(0, y, 287, 114,34);
			tv_setting_network.InitSize(0, y+114, 287, 114,34);
			//tv_setting_other.InitSize(0, y+114*2, 287, 114,34);
			//tv_setting_ap.InitSize(0, y+114*3, 287, 114,34);
			tv_setting_app.InitSize(0, y+114*2, 287, 114,34);
			//tv_setting_layout_type.InitSize(0, y+114*4, 287, 114,34);
			tv_setting_about.InitSize(0, y+114*4, 287, 114,34);
			tv_setting_systemconfig.InitSize(0,y+114*3,287,114,34);
			iv_setting_other_msg.InitSize(340, 200, 702,1004);
		}else{
			iv_setting_message_bg.InitSize(420, 60, 1500, 1020);			
			iv_setting_ico_bg.InitSize(0, y, 420, 114);
			iv_setting_system_ico.InitSize(96, y+32,58, 49);
			iv_setting_network_ico.InitSize(96, y+114+32, 58, 49);
//			iv_setting_other_ico.InitSize(96, y+114*2+32, 58, 49);
//			iv_setting_ap_ico.InitSize(96, y+114*3+32, 58, 49);
			iv_setting_app_ico.InitSize(96, y+114*2+32, 58, 49);
			//iv_setting_layout_type_ico.InitSize(96, y+114*4+32, 58, 49);
			iv_setting_about_ico.InitSize(96, y+114*4+32, 58, 49);
			iv_setting_systemconfig_ico.InitSize(96, y+114*3+32, 58, 49);
			tv_setting_system.InitSize(0, y, 420, 114,34);
			tv_setting_network.InitSize(0, y+114, 420, 114,34);
//			tv_setting_other.InitSize(0, y+114*2, 420, 114,34);
//			tv_setting_ap.InitSize(0, y+114*3, 420, 114,34);
			tv_setting_app.InitSize(0, y+114*2, 420, 114,34);
			//tv_setting_layout_type.InitSize(0, y+114*4, 420, 114,34);
			tv_setting_about.InitSize(0, y+114*4, 420, 114,34);
			tv_setting_systemconfig.InitSize(0, y+114*3, 420, 114,34);
			iv_setting_other_msg.InitSize(470, 90, 540, 800);
		}
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
	
	//wifi初始化
	private void WifiInit(){
		if(is_init_wifi==true){
			return;
		}
		is_init_wifi = true;
		//初始化变量	
		this.str_wifi_connected_ssid = null;
		wifi_manager = (WifiManager)getSystemService(WIFI_SERVICE);
		
		RelativeLayout layout_wifi = (RelativeLayout)this.findViewById(R.id.layout_wifi);
		layout_wifi.setLayoutParams(this.layout_right_bottom);
		this.width = layout_right_bottom.width;
		this.height = layout_right_bottom.height;
		this.lv_wifi_list = (ListView)this.findViewById(R.id.lv_wifi_list);
		RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(width,height-60);
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lv_wifi_list.setLayoutParams(lp);
		this.iv_wifi_refresh = (ImageView)this.findViewById(R.id.iv_wifi_refresh);
		this.switch_wifi_status_switch = (Switch)this.findViewById(R.id.switch_wifi_status_switch);
		this.switch_wifi_status_switch.setEnabled(true);
		this.tv_wifi_status_text = (TextView)this.findViewById(R.id.tv_wifi_status_text);
		//显示WIFI打开或关闭
		if(wifi_manager.isWifiEnabled()==false){
			this.switch_wifi_status_switch.setChecked(false);
			tv_wifi_status_text.setText("要查看可用网络，请打开 WI-FI。");
			tv_wifi_status_text.setVisibility(View.VISIBLE);
			iv_wifi_refresh.setVisibility(View.INVISIBLE);
		}else{
			this.switch_wifi_status_switch.setChecked(true);
			tv_wifi_status_text.setText("正在扫描 WI-FI...");
			tv_wifi_status_text.setVisibility(View.VISIBLE);
			iv_wifi_refresh.setVisibility(View.VISIBLE);
		}
		//如果wifi打开,扫描WIFI
		wifi_result_adapter = new WifiResultAdapter();
		//WifiScanStart();
		//WIFI开关事件
		this.switch_wifi_status_switch.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton switch_wifi, boolean isChecked) {
				if(isChecked==true){
					if(SystemVar.ap_enable==true){
						int width = 720;
						int height = width*5/8;
						final CustomDialog dlg = new CustomDialog(SettingActivity.this);
						dlg.SetSize(width, height);
						dlg.show();
						dlg.SetMessage("开启WIFI,将关闭热点,是否继续?");
						dlg.SetTitle("开启WIFI");
						dlg.btn_ok.setOnClickListener(new OnClickListener() {					
							@Override
							public void onClick(View arg0) {
								SystemVar.ap_enable = false;
								//StopAp();
								wifi_manager.setWifiEnabled(true);
								tv_wifi_status_text.setText("正在打开 WI-FI...");
								tv_wifi_status_text.setVisibility(View.VISIBLE);
								iv_wifi_refresh.setVisibility(View.VISIBLE);
								dlg.dismiss();
								return;
							}
						});
						dlg.btn_cancel.setOnClickListener(new OnClickListener() {					
							@Override
							public void onClick(View arg0) {
								switch_wifi_status_switch.setChecked(false);
								dlg.dismiss();
								return;
							}
						});
						return;
					}
					wifi_manager.setWifiEnabled(true);
					tv_wifi_status_text.setText("正在打开 WI-FI...");
					tv_wifi_status_text.setVisibility(View.VISIBLE);
					iv_wifi_refresh.setVisibility(View.VISIBLE);
				}else{					
					WifiScanStop();
					if(wifi_list!=null){
						wifi_list.clear();
						wifi_result_adapter.notifyDataSetChanged();
						tv_wifi_status_text.setText("正在关闭 WI-FI...");
						tv_wifi_status_text.setVisibility(View.VISIBLE);
						wifi_manager.setWifiEnabled(false);
						iv_wifi_refresh.setVisibility(View.INVISIBLE);
					}
				}			
			}			
		});
		//刷新事件
		this.iv_wifi_refresh.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(wifi_manager.isWifiEnabled()==false){
					return;
				}
				WifiScanStop();					
				wifi_list.clear();
				wifi_result_adapter.notifyDataSetChanged();
				tv_wifi_status_text.setText("正在重新扫描 WI-FI...");
				tv_wifi_status_text.setVisibility(View.VISIBLE);
				WifiScanStart();
			}			
		});
		//注册wifi开关广播
		RegWifiStatusEvent();
		//注册列表单击
		this.lv_wifi_list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				ScanResult result = wifi_list.get(arg2);
				String str_wifi_encrypt_type = "";
				if(result.capabilities.contains("WPA-PSK")==true&&result.capabilities.contains("WPA2-PSK")==true){
                	str_wifi_encrypt_type = "WPA-PSK/WPA2-PSK";
                }else if(result.capabilities.contains("WPA-PSK")==true){
                	str_wifi_encrypt_type = "WPA-PSK";
                }else if(result.capabilities.contains("WPA2-PSK")==true){
                	str_wifi_encrypt_type = "WPA2-PSK";
                }else if(result.capabilities.contains("WEP")==true){
                	str_wifi_encrypt_type = "WEP";
                }else{
                	str_wifi_encrypt_type = "NONE";
                }
                if(result.capabilities.contains("WPS")==true){
                	str_wifi_encrypt_type += "/WPS";
                }
				if(str_wifi_connected_ssid!=null&&result.SSID.equals(str_wifi_connected_ssid)==true){
					AlertDialog alert_window = new AlertDialog.Builder(SettingActivity.this).create();				
					WifiInfo info = wifi_manager.getConnectionInfo();
					final int net_id = info.getNetworkId();
					alert_window.setTitle(result.SSID);					
					int ip = info.getIpAddress();
					String str_ip = (ip & 0xFF ) + "." + ((ip >> 8 ) & 0xFF) + "." +  ((ip >> 16 ) & 0xFF) + "." + ( ip >> 24 & 0xFF);
		    		alert_window.setMessage("状态信息\n已连接\n信号强度\n"+Integer.toString(info.getRssi())+"\n连接速度\n"+Integer.toString(info.getLinkSpeed())+"Mbps\n安全性\n"+result.capabilities+"\nIP地址\n"+str_ip);
		    		alert_window.setButton("取消", new DialogInterface.OnClickListener() {				
		    			@Override
		    			public void onClick(DialogInterface arg0, int arg1) {
		    				
		    			}
		    		});
		    		alert_window.setButton2("取消保存", new DialogInterface.OnClickListener(){
		    			@Override
		    			public void onClick(DialogInterface arg0, int arg1) {
		    				wifi_manager.disableNetwork(net_id);
		    				wifi_manager.removeNetwork(net_id);
		    				wifi_manager.saveConfiguration();
		    				wifi_manager.startScan();
		    				//wifi_result_adapter.notifyDataSetChanged();
		    				//lv_wifi_list.postInvalidate();
		    			}
		    		});
		    		alert_window.show();
				}else{
					//如果保存过配置，直接连接
					List<WifiConfiguration> arr_wifi_config = wifi_manager.getConfiguredNetworks();
					for(WifiConfiguration tmp_wifi_config:arr_wifi_config){
						if(tmp_wifi_config.SSID.replaceAll("\"", "").equals(result.SSID.replaceAll("\"", ""))==true){
							final int net_id = tmp_wifi_config.networkId;
							//显示对话框
							AlertDialog alert_window = new AlertDialog.Builder(SettingActivity.this).create();				
							WifiInfo info = wifi_manager.getConnectionInfo();
							alert_window.setTitle(result.SSID);					
							int ip = info.getIpAddress();
							String str_ip = (ip & 0xFF ) + "." + ((ip >> 8 ) & 0xFF) + "." +  ((ip >> 16 ) & 0xFF) + "." + ( ip >> 24 & 0xFF);
				    		alert_window.setMessage("信号强度\n"+Integer.toString(result.level)+"\n安全性\n"+str_wifi_encrypt_type);
				    		alert_window.setButton("取消", new DialogInterface.OnClickListener() {				
				    			@Override
				    			public void onClick(DialogInterface arg0, int arg1) {
				    				
				    			}
				    		});
				    		alert_window.setButton3("取消保存", new DialogInterface.OnClickListener(){
				    			@Override
				    			public void onClick(DialogInterface arg0, int arg1) {
				    				wifi_manager.disableNetwork(net_id);
				    				wifi_manager.removeNetwork(net_id);
				    				wifi_manager.saveConfiguration();
				    				wifi_manager.startScan();
				    				//wifi_result_adapter.notifyDataSetChanged();
				    			}
				    		});
				    		alert_window.setButton2("连接", new DialogInterface.OnClickListener(){
				    			@Override
				    			public void onClick(DialogInterface arg0, int arg1) {
				    				wifi_manager.enableNetwork(net_id, true);
				    				lv_wifi_list.setSelection(0);
				    			}
				    		});
				    		alert_window.show();
							
							return;
						}
					}
					//没有密码直接连接
					if(str_wifi_encrypt_type.equals("NONE")==true||str_wifi_encrypt_type.equals("NONE/WPS")==true){
						WifiConfiguration wifi_config = createWifiInfo(result.SSID,"",1);
	    				int net_id = wifi_manager.addNetwork(wifi_config);
	    				if(net_id!=-1){
		    				wifi_manager.saveConfiguration();
		    				wifi_manager.enableNetwork(net_id, true);
		    				lv_wifi_list.setSelection(0);
	    				}
						return;
					}
					LayoutInflater factory = LayoutInflater.from(SettingActivity.this);//提示框  
					final View my_view = factory.inflate(R.layout.activity_wifi_connect, null);//这里必须是final的
					final AlertDialog alert_window = new AlertDialog.Builder(SettingActivity.this).create();				
					final String str_wifi_encrypt_type1 = str_wifi_encrypt_type;
					final String str_ssid = result.SSID;
					alert_window.setView(my_view);
					alert_window.setTitle(result.SSID);
					alert_window.setButton("取消", new DialogInterface.OnClickListener() {				
		    			@Override
		    			public void onClick(DialogInterface arg0, int arg1) {
		    				
		    			}
		    		});
		    		alert_window.setButton2("连接", new DialogInterface.OnClickListener(){
		    			@Override
		    			public void onClick(DialogInterface arg0, int arg1) {
		    				EditText et_tmp_var = (EditText)alert_window.findViewById(R.id.et_wifi_encrypt_pwd);
							String str_tmp_var = et_tmp_var.getText().toString().trim();
							int encrypt_type = 1;
							if(str_wifi_encrypt_type1.equals("WEP")==true){
								encrypt_type =2;
							}else{
								encrypt_type = 3;
							}
		    				WifiConfiguration wifi_config = createWifiInfo(str_ssid,str_tmp_var,encrypt_type);
		    				int net_id = wifi_manager.addNetwork(wifi_config);
		    				if(net_id!=-1){		    					
			    				wifi_manager.saveConfiguration();
			    				wifi_manager.enableNetwork(net_id, true);
			    				lv_wifi_list.setSelection(0);
		    				}
		    				
		    			}
		    		});
		    		alert_window.show();
		    		TextView tv_wifi_signal_level = (TextView)alert_window.findViewById(R.id.tv_wifi_signal_level);
					TextView tv_wifi_encrypt_type = (TextView)alert_window.findViewById(R.id.tv_wifi_encrypt_type);
					EditText et_wifi_encrypt_pwd = (EditText)alert_window.findViewById(R.id.et_wifi_encrypt_pwd);
					tv_wifi_signal_level.setText(Integer.toString(result.level));
					tv_wifi_encrypt_type.setText(str_wifi_encrypt_type);
					alert_window.getButton(DialogInterface.BUTTON2).setEnabled(false);
									
					et_wifi_encrypt_pwd.setOnKeyListener(new OnKeyListener(){
						@Override
						public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
							if(arg2.getAction()==KeyEvent.ACTION_UP){
								EditText et_tmp_var = (EditText)alert_window.findViewById(R.id.et_wifi_encrypt_pwd);
								String str_tmp_var = et_tmp_var.getText().toString();
								if(str_wifi_encrypt_type1.equals("WEP")!=true){
									if(str_tmp_var.length()>=8){
										alert_window.getButton(DialogInterface.BUTTON2).setEnabled(true);
									}else{
										alert_window.getButton(DialogInterface.BUTTON2).setEnabled(false);
									}
								}else{
									if(str_tmp_var.length()==5||str_tmp_var.length()==13){
										alert_window.getButton(DialogInterface.BUTTON2).setEnabled(true);
									}else{
										alert_window.getButton(DialogInterface.BUTTON2).setEnabled(false);
									}
								}
							}
							return false;
						}
						
					});
				}
			}			
		});
	}
	
	private WifiConfiguration createWifiInfo(String SSID, String password, int type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";		
		// 分为三种情况：1没有密码2用wep加密3用wpa加密
		if (type == 1) {// WIFICIPHER_NOPASS
			//config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			//config.wepTxKeyIndex = 0;			
		} else if (type == 2) {  //  WIFICIPHER_WEP 
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + password + "\"";
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		} else if (type == 3) {   // WIFICIPHER_WPA
			config.preSharedKey = "\"" + password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		} 
		
		return config;
	}
	
	private void WifiScanStart(){
		if(wifi_manager.isWifiEnabled()==false){
			return;
		}
		if(wifi_receiver==null){
			wifi_receiver = new WifiReceiver();
		}
		registerReceiver(wifi_receiver, new IntentFilter(wifi_manager.SCAN_RESULTS_AVAILABLE_ACTION));
		wifi_manager.startScan();
	}
	
	private void WifiScanStop(){
		if(wifi_receiver!=null){
			this.unregisterReceiver(wifi_receiver);
			wifi_receiver.abortBroadcast();
			wifi_receiver = null;
		}
	}
	
	private void ReloadWifi(){
		WifiInfo wifiInfo = wifi_manager.getConnectionInfo();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.wifi, menu);
		return true;
	}
	//设置全屏与常亮
	private void setFullScreen(){
		//全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//屏幕常亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}	
	class WifiReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			wifi_list = wifi_manager.getScanResults();
			//移除SSID为空的网络
			RemoveEmptySsid();
			//把连上的网络排到开始位置
			if(str_wifi_connected_ssid!=null){
				ReSortWifiList();
			}
			//显示网络列表
			tv_wifi_status_text.setVisibility(View.INVISIBLE);
			lv_wifi_list.setAdapter(wifi_result_adapter);
		}
	}
	private void RemoveEmptySsid(){ 
		//查找空ssid列表
		for(int i=0;i<wifi_list.size();i++){
			ScanResult result = wifi_list.get(i);
			if(result.SSID==""){
				wifi_list.remove(i);
				i--;
			}
		}
	}
	private void ReSortWifiList(){
		for(int i=0;i<wifi_list.size();i++){
			ScanResult result = wifi_list.get(i);
			if(str_wifi_connected_ssid!=null&&result.SSID.equals(str_wifi_connected_ssid)==true){
				if(i==0){
					return;
				}
				wifi_list.set(i, wifi_list.get(0));
				wifi_list.set(0,result);
				return;
			}
		}
	}
	//注册WIFI状态检查
	private void RegWifiStatusEvent(){
		BroadcastReceiver wifiReceiver = new BroadcastReceiver(){
			public void onReceive(Context arg0, Intent arg1) {
				if(arg1.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)==true){
					int wifi_state = arg1.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
		            switch (wifi_state) {
		            case WifiManager.WIFI_STATE_DISABLED:
		            	tv_wifi_status_text.setText("要查看可用网络，请打开 WI-FI。");
						tv_wifi_status_text.setVisibility(View.VISIBLE);
		                break;
		            case WifiManager.WIFI_STATE_ENABLED:
		            	tv_wifi_status_text.setText("正在扫描 WI-FI...");
						tv_wifi_status_text.setVisibility(View.VISIBLE);
						WifiScanStart();
		                break;
		            }										
				}
				if(arg1.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)==true){
					NetworkInfo info = arg1.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
					if(info.getState().equals(NetworkInfo.State.CONNECTING)==true){
						
					}
					if(info.getState().equals(NetworkInfo.State.CONNECTED)==true){
						 WifiInfo wifi_info = arg1.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
						 str_wifi_connected_ssid = wifi_info.getSSID();
						 str_wifi_connected_ssid = str_wifi_connected_ssid.replaceAll("\"", "");
						 if(wifi_list!=null&&wifi_list.size()!=0){
							 ReSortWifiList();
							 wifi_result_adapter.notifyDataSetChanged();
						 }
					}
					if(info.getState().equals(NetworkInfo.State.DISCONNECTED)==true){
						 str_wifi_connected_ssid = null;
					}
				}
			}			
		};
		IntentFilter wifiReceiverFilter = new IntentFilter(); 
		wifiReceiverFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		wifiReceiverFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		registerReceiver(wifiReceiver, wifiReceiverFilter);		
	}
	public class WifiResultAdapter extends BaseAdapter {
        public WifiResultAdapter() {
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView img;
            ImageView iv_wifi_encrypt;
            TextView tv_ssid_name;
            TextView tv_ssid_stauts;
            View my_view = null;
            String str_ssid_status = null;
            //if (convertView == null) {
            	my_view = View.inflate(getBaseContext(), R.layout.activity_wifi_item, null);
                img = (ImageView)my_view.findViewById(R.id.iv_wifi_signal);
                iv_wifi_encrypt = (ImageView)my_view.findViewById(R.id.iv_wifi_encrypt);
                tv_ssid_name = (TextView)my_view.findViewById(R.id.tv_ssid_name);                
                tv_ssid_stauts = (TextView)my_view.findViewById(R.id.tv_wifi_status);
                ScanResult result= wifi_list.get(position);
                if(result.capabilities.contains("WPA-PSK")==true&&result.capabilities.contains("WPA2-PSK")==true){
                	str_ssid_status = "通过 WPA/WPA2 进行保护";
                }else if(result.capabilities.contains("WPA-PSK")==true){
                	str_ssid_status = "通过 WPA 进行保护";
                }else if(result.capabilities.contains("WPA2-PSK")==true){
                	str_ssid_status = "通过 WPA2 进行保护";
                }else if(result.capabilities.contains("WEP")==true){
                	str_ssid_status = "通过 WEP 进行保护";
                }else{
                	iv_wifi_encrypt.setVisibility(View.INVISIBLE);
                	str_ssid_status = "无密码进行保护";
                }
                if(result.capabilities.contains("WPS")==true){
                	str_ssid_status += "(可使用WPS)";
                }
                //显示已保存的网络
                List<WifiConfiguration> arr_wifi_config = wifi_manager.getConfiguredNetworks();
				for(WifiConfiguration tmp_wifi_config:arr_wifi_config){
					if(tmp_wifi_config.SSID.replaceAll("\"", "").equals(result.SSID.replaceAll("\"", ""))==true){
						str_ssid_status = "已保存，"+str_ssid_status;
						break;
					}
				}
                int level = WifiManager.calculateSignalLevel(result.level,6);
                if(level==5){
                	img.setImageResource(R.drawable.wifi_signal_5);
                }else if(level==4){
                	img.setImageResource(R.drawable.wifi_signal_4);
                }else if(level==3){
                	img.setImageResource(R.drawable.wifi_signal_3);
                }else if(level==2){
                	img.setImageResource(R.drawable.wifi_signal_2);
                }else{
                	img.setImageResource(R.drawable.wifi_signal_1);
                }
                tv_ssid_name.setText(result.SSID);
                if(str_wifi_connected_ssid!=null&&str_wifi_connected_ssid.equals(result.SSID)==true){
                	tv_ssid_stauts.setText("已连接");
                }else{
                	tv_ssid_stauts.setText(str_ssid_status+" 信号强度("+Integer.toString(result.level)+"db)");
                }
            //}else{
            //	my_view = convertView;
            //}     
            return my_view;
        }
        public final int getCount() {
            return wifi_list.size();
        }

        public final Object getItem(int position) {
            return wifi_list.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
	}
	/*
	private void ShowNetworkInfo(){
		//有线信息显示
		if(SystemVar.eth_dhcp==true){
			switch_network_dhcp.setChecked(false);
			et_ip.setEnabled(false);
			et_netmask.setEnabled(false);
			et_gateway.setEnabled(false);
			et_dns.setEnabled(false);
			et_ip.setFocusable(false);
			et_netmask.setFocusable(false);
			et_gateway.setFocusable(false);
			et_dns.setFocusable(false);
		}else{
			switch_network_dhcp.setChecked(true);
			et_ip.setEnabled(true);
			et_netmask.setEnabled(true);
			et_gateway.setEnabled(true);
			et_dns.setEnabled(true);
			et_ip.setFocusable(true);
			et_netmask.setFocusable(true);
			et_gateway.setFocusable(true);
			et_dns.setFocusable(true);
		}
        String ip = IpAddressSet.GetIpAddress(IpAddressSet.ETH0);
        if(ip!=null&&"".equals(ip)==false){
        	et_ip.setText(ip);
        	String netmask = IpAddressSet.GetNetMask(IpAddressSet.ETH0);
        	if(netmask!=null&&"".equals(netmask)==false){
        		et_netmask.setText(netmask);
        	}
            String gateway = IpAddressSet.GetGateWay(IpAddressSet.ETH0);
            if(gateway!=null&&"".equals(gateway)==false){
            	et_gateway.setText(gateway);
        	}            
            String dns = IpAddressSet.GetDns();
            if(dns!=null&&"".equals(dns)==false){
            	et_dns.setText(dns);
        	}            
        }  
        
        //无线信息显示
  		if(SystemVar.wifi_dhcp==true){
  			switch_wifi_network_dhcp.setChecked(false);
  			et_wifi_ip.setEnabled(false);
  			et_wifi_netmask.setEnabled(false);
  			et_wifi_gateway.setEnabled(false);
  			et_wifi_dns.setEnabled(false);
  			et_wifi_ip.setFocusable(false);
  			et_wifi_netmask.setFocusable(false);
  			et_wifi_gateway.setFocusable(false);
  			et_wifi_dns.setFocusable(false);
  		}else{
  			switch_wifi_network_dhcp.setChecked(true);
  			et_wifi_ip.setEnabled(true);
  			et_wifi_netmask.setEnabled(true);
  			et_wifi_gateway.setEnabled(true);
  			et_wifi_dns.setEnabled(true);
  			et_wifi_ip.setFocusable(true);
  			et_wifi_netmask.setFocusable(true);
  			et_wifi_gateway.setFocusable(true);
  			et_wifi_dns.setFocusable(true);
  		}
		ip = IpAddressSet.GetIpAddress(IpAddressSet.WLAN0);
		if(ip!=null&&"".equals(ip)==false){
			et_wifi_ip.setText(ip);
        	String netmask = IpAddressSet.GetNetMask(IpAddressSet.WLAN0);
        	if(netmask!=null&&"".equals(netmask)==false){
        		et_wifi_netmask.setText(netmask);
        	}
            String gateway = IpAddressSet.GetGateWay(IpAddressSet.WLAN0);
            if(gateway!=null&&"".equals(gateway)==false){
            	et_wifi_gateway.setText(gateway);
        	}            
            String dns = IpAddressSet.GetDns();
            if(dns!=null&&"".equals(dns)==false){
            	et_wifi_dns.setText(dns);
        	}            
        }  
	}
	*/
	//显示AP信息
	/*
	private void ShowApInfo(){
		et_ap_ssid.setText(SystemVar.ap_ssid);
		et_ap_password.setText(SystemVar.ap_pwd);
	}
	
	private void RefreNetworkInfo(){
		MyClass.SendMessageDelay(MyVar.set_handler, 0x1000, 2000);
	}
	*/
	private void LoadApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = getPackageManager().queryIntentActivities(mainIntent, 0);
    }
	
	//通过包名查找pkg info
	private PackageInfo FindPackageInfo(String pkg_name){
		PackageInfo pkg_info = null;
		PackageManager pm = getPackageManager(); 
		List<PackageInfo> pkg_info_list  = pm.getInstalledPackages(0);
		for(int i=0;i<pkg_info_list.size();i++){
			if(pkg_info_list.get(i).packageName.equals(pkg_name)==true){
				pkg_info = pkg_info_list.get(i);
				break;
			}
		}
		return pkg_info;
	}
	
	private void ReloadAppList(){
		LoadApps();
		//首页显示包名
		main_pkg_list = new ArrayList<String>();
        for(int i=0;i<mApps.size();i++){
        	String pkg_name = mApps.get(i).activityInfo.packageName;
        	PackageInfo pinfo = this.FindPackageInfo(pkg_name);
            ApplicationInfo appInfo = pinfo.applicationInfo;
            if((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0){
            	
            }else{
	        	String app_name = mApps.get(i).activityInfo.loadLabel(getPackageManager()).toString();
	        	if("com.xuhen.lottery".equals(pkg_name)==true){
	        		continue;
	        	}
	        	main_pkg_list.add(pkg_name);
            }
        }
		//结束
        return;
	}
	
	//通过包名查找结构
	private ResolveInfo FindInfo(String pkg_name){
		String name = null;
		ResolveInfo info = null;
		for(int i=0;i<mApps.size();i++){
			name = mApps.get(i).activityInfo.packageName;
			if(name.equalsIgnoreCase(pkg_name)==true){
				info = mApps.get(i);
				break;
			}
		}
		return info;
	}
	/*
	private void SetLayoutType(int type){
		if(type==1){		
			IpAddressSet.SetLayoutType(1);
		}else{
			IpAddressSet.SetLayoutType(0);
		}
		IpAddressSet.Reboot();
	}
	*/
	
}


