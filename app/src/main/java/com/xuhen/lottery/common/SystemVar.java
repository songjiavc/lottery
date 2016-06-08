package com.xuhen.lottery.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;



public class SystemVar {
	private static SharedPreferences setting = null;
	//数据库版本号
	public static int sqllite_version = 13120100;
	//资源版本号
	public static int assets_version = 13120100;
	//播放声音大小
	public static float video_volume = 1.00f;
	//用户数据最后同步时间
	public static long user_syn_timestamp = 0;
	//最后能够正常的时间
	public static int last_one_run_data = MyVar.build_date;
	
	public static String uuid = "";
	//记住的用户名
	public static String username = "";
	public static String password = "";
	
	//网络设置
	public static boolean eth_dhcp = true;
	public static String eth_ip = "";
	public static String eth_netmask = "";
	public static String eth_gateway = "";
	public static String eth_dns = "";
	
	public static boolean wifi_dhcp = true;
	public static String wifi_ip = "";
	public static String wifi_netmask = "";
	public static String wifi_gateway = "";
	public static String wifi_dns = "";
	//AP设置
	public static boolean ap_enable = false;
	public static String ap_ssid = "Lottery";
	public static String ap_pwd = "12345678";
	
	private static void Init(){
		//读取配置文件
		Context context = GlobalApplication.getInstance();
		setting = context.getSharedPreferences("lottery.cfg", Context.MODE_PRIVATE);
	}
	
	public static void LoadCfg(){
		Init();
		SystemVar.sqllite_version = setting.getInt("sqllite_version", sqllite_version);
		SystemVar.assets_version = setting.getInt("assets_version", assets_version);
		SystemVar.video_volume = setting.getFloat("video_volume", video_volume);
		SystemVar.user_syn_timestamp = setting.getLong("user_syn_timestamp", user_syn_timestamp);
		SystemVar.last_one_run_data = setting.getInt("last_one_run_data", last_one_run_data);
		SystemVar.uuid = setting.getString("uuid", uuid);
		SystemVar.username = setting.getString("username", username);
		SystemVar.password = setting.getString("password", password);
		
		SystemVar.eth_dhcp = setting.getBoolean("eth_dhcp", true);
		SystemVar.eth_ip = setting.getString("eth_ip", "");
		SystemVar.eth_netmask = setting.getString("eth_netmask", "");
		SystemVar.eth_gateway = setting.getString("eth_gateway", "");
		SystemVar.eth_dns = setting.getString("eth_dns", "");
		
		SystemVar.wifi_dhcp = setting.getBoolean("wifi_dhcp", true);
		SystemVar.wifi_ip = setting.getString("wifi_ip", "");
		SystemVar.wifi_netmask = setting.getString("wifi_netmask", "");
		SystemVar.wifi_gateway = setting.getString("wifi_gateway", "");
		SystemVar.wifi_dns = setting.getString("wifi_dns", "");
		//AP模式
		SystemVar.ap_enable = setting.getBoolean("ap_enable", ap_enable);
		SystemVar.ap_ssid = setting.getString("ap_ssid", ap_ssid);
		SystemVar.ap_pwd = setting.getString("ap_pwd", ap_pwd);
		//IMEI如果为0,获取真正的串号
		if("".equals(SystemVar.uuid)==true){			
			SystemVar.uuid = MyClass.UUID(GlobalApplication.getInstance());
			SaveCfg();
		}
	}
	
	public static void SaveCfg(){
		Init();
		Editor editor = setting.edit();
		editor.putInt("sqllite_version", sqllite_version);
		editor.putInt("assets_version", assets_version);
		editor.putFloat("video_volume", video_volume);
		editor.putLong("user_syn_timestamp", user_syn_timestamp);
		editor.putInt("last_one_run_data", last_one_run_data);

		editor.putBoolean("eth_dhcp", eth_dhcp);
		editor.putString("eth_ip", eth_ip);
		editor.putString("eth_netmask", eth_netmask);
		editor.putString("eth_gateway", eth_gateway);
		editor.putString("eth_dns", eth_dns);
		
		editor.putBoolean("wifi_dhcp", wifi_dhcp);
		editor.putString("wifi_ip", wifi_ip);
		editor.putString("wifi_netmask", wifi_netmask);
		editor.putString("wifi_gateway", wifi_gateway);
		editor.putString("wifi_dns", wifi_dns);
		
		editor.putString("uuid", uuid);
		editor.putString("username", username);
		editor.putString("password", password);
		
		//AP模式
		editor.putBoolean("ap_enable", ap_enable);
		editor.putString("ap_ssid", ap_ssid);
		editor.putString("ap_pwd", ap_pwd);
		editor.commit();
	}
}
