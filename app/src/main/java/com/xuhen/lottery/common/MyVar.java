package com.xuhen.lottery.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xuhen.lottery.cls.CheckStatusTip;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.ArrayMap;

public class MyVar {
	//是否10：9程序
	public final static boolean IS_16_9_PROGRAM = true;
	//服务器地址
	public final static String BASE_SERVER_URL = "http://www.dzzst.cn:1819/webappmgr/";
	//登录接口地址
	public final static String LOGIN_URL = BASE_SERVER_URL+"/outerInterface/login.action";
	//修改密码接口地址
	public final static String MODI_PWD_URL = BASE_SERVER_URL+"/outerInterface/updatePassword.action";
	//最新应用接口地址
	public final static String LAST_APPS_URL = BASE_SERVER_URL+"/outerInterface/getAppversionsOfnew.action";
	//应用权限接口
	public final static String APP_POWER_URL = BASE_SERVER_URL+"/outerInterface/getAuthOfStationAndApp.action";
	//应用上级代理信息
	public final static String PARENT_AGENT_URL = BASE_SERVER_URL+"/outerInterface/getProxyOfStation.action";
	//向服务器报告确认查看
	public final static String REPORT_READ_FINISH_URL = BASE_SERVER_URL+"/outerInterface/addReceiptOfAnnouncement.action";
	//通告数组保存
	public static JSONArray announcement = null;
	//投注站信息
	public static JSONObject station_dto = null;
	//应用APP列表
	public static JSONArray use_app_dtos = null;
	//公司通知
	public static JSONArray company_notice = null;
	//APP最新版本列表,用于升级
	public static JSONArray server_apps_list = null;
	//登录成功标识
	public static boolean login_flag = false;
	//当前登录已显示过的APPID列表
	public static String have_show_app_ids = "";
	//测试升级的APP地址
	public static String TEST_APK_URL = "http://gdown.baidu.com/data/wisegame/20d7a67f4324280e/weixin_760.apk";
	//全局下载速度
	public final static int DOWN_SPEED = 3000*1000;
	//全局下载分片数
	public final static int DOWN_THREAD = 5;
	//当前登录已显示过的通告ID列表
	public static String read_annuonce_ids = "";
	
	//assets版本号,升级需要修改该值
	public final static int assets_version = 15121502;//升级资源文件，需要修改该编号
	//当前标准时间戳
	public final static int build_date = 151211;
	//服务器资源地址
	public final static String APK_UPDATE_URL = "http://www.eoncn.com/app/update.php";
	//app数据路径
	public final static String APK_ROOT_PATH = Environment.getExternalStorageDirectory().getPath()+"/lottery/";
	//共享文件夹
	public final static String SHARE_DATA_DIR = APK_ROOT_PATH+"share/";
	//共享数据文件
	public final static String SHARE_DATA_PATH = SHARE_DATA_DIR+"data.txt";
	
	public final static String VIDEO_PATH = APK_ROOT_PATH+"video/";
	public static String AUDIO_PATH = APK_ROOT_PATH+"audio/";
	public final static String APK_UPDATE_FILE = APK_ROOT_PATH+"apk/updata.apk";
	
	//屏幕大小
	private static int screen_width = 1080;
	private static int screen_heidht = 1920;
	private static int design_screen_width = 1080;
	private static int design_screen_height = 1920;
	private static float scaled_density = 1;
	private static boolean pro_run_flag = false;
	private static boolean network_status_flag = false;
	private static boolean login_status_flag = false;
	//全局对像
	//是否同一按钮双击判定
	private static long last_click_time = 0;
	
	private static Object last_click_obj = null;
	//UPNP支持
	public static boolean upnp_support = false;
	
	//检测状态文字提示
	public static CheckStatusTip check_status_tip = new CheckStatusTip();
	//短信验证码长度
	public static int sms_check_code_length = 4;
	//全局消息编号
	private static int msg_id = 0;
	//消息重试时间间隔
	public static long message_retry_time = 5000;
	//是否正在通话中
	public static boolean is_chating = false;
	
	//主线程消息
	public static Handler main_handler = null;
	//用户界面消息
	public static Handler user_handler = null;
	//应用库页面消息
	public static Handler app_handler = null;
	//设置页页消息
	public static Handler set_handler = null;
	//各个界面ID定义
	public final static int MAIN_PAGE_ID = 1;
	public final static int USER_PAGE_ID = 2;
	public final static int APP_PAGE_ID = 3;
	public final static int SETTING_PAGE_ID = 4;
	public static int CURRENT_PAGE_ID = 1;
	//当前布局 竖屏 1 横屏 0
	public static int SYSTEM_LAYOUT_TYPE = 1;
	
	//获取一个消息编号
	public static int GetMessageId(){
		msg_id += 1;
		return msg_id;
	}
	public static void SetLoginFlag(boolean b){
		MyVar.login_status_flag = b;
	}
	public static boolean GetLoginFlag(){
		return MyVar.login_status_flag;
	}
	//判定是否快速点击
    public static boolean IsFastClick(Object obj) {
    	if(obj!=null&&obj.equals(last_click_obj)==true){
	        long time = System.currentTimeMillis();  
	        long timeD = time - last_click_time;  
	        if ( 0 < timeD && timeD < 1000) {
	            return true;    
	        }     
	        last_click_time = time;
	        last_click_obj = obj;
	        return false;
    	}else{
    		last_click_time = System.currentTimeMillis();
	        last_click_obj = obj;
    		return false;
    	}
    } 
	public static void SetScreenSize(int width,int height,float scaled_density){
		MyVar.screen_width = width;
		MyVar.screen_heidht = height;
		MyVar.scaled_density = scaled_density;
		MyClass.PrintInfoLog("width="+width+",height="+height+",scaled_density="+scaled_density);
	}
	public static int GetScreenWidth(){
		return MyVar.screen_width;
	}
	public static int GetScreenHeight(){
		return MyVar.screen_heidht;
	}
	public static float GetScaledDensity(){
		return MyVar.scaled_density;
	}
	public static int GetDesignWidth(){
		if(MyVar.SYSTEM_LAYOUT_TYPE==0){
			return MyVar.design_screen_height;
		}else{
			return MyVar.design_screen_width;
		}
	}
	public static int GetDesignHeight(){
		if(MyVar.SYSTEM_LAYOUT_TYPE==0){
			return MyVar.design_screen_width;
		}else{
			return MyVar.design_screen_height;
		}
	}
	public static void SetProRunFlag(boolean pro_run_flag){
		MyVar.pro_run_flag = pro_run_flag;
	}
	public static boolean GetProRunFlag(){
		return MyVar.pro_run_flag;
	}
	public static void SetNetworkStatusFlag(boolean network_status_flag){
		MyVar.network_status_flag = network_status_flag;
	}
	public static boolean GetNetWorkStatusFlag(){
		return MyVar.network_status_flag;
	}
}
