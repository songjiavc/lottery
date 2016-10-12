package com.xuhen.lottery.activity;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baiyilin.lottery.R;
import com.xuhen.lottery.cls.AudioPlayer;
import com.xuhen.lottery.cls.MyThread;
import com.xuhen.lottery.common.GlobalApplication;
import com.xuhen.lottery.common.MyClass;
import com.xuhen.lottery.common.MyVar;
import com.xuhen.lottery.common.SystemVar;
import com.xuhen.lottery.view.CustomDialog;
import com.xuhen.lottery.view.AppListAdapter;
import com.xuhen.lottery.view.MyEditText;
import com.xuhen.lottery.view.MyImageButton;
import com.xuhen.lottery.view.MyImageView;
import com.xuhen.lottery.view.MyListView;
import com.xuhen.lottery.view.MyTextView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.net.ConnectivityManager;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.InputFilter;
import android.text.InputType;
import android.text.format.Formatter;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.AbsoluteLayout.LayoutParams;


public class UserActivity extends Activity {
	//所有可启动的APK
	private List<ResolveInfo> mApps = null;

	//重进首页时恢复	
	private CustomDialog dlg = null;	
	private AppListAdapter listItemAdapter = null;
	private MyListView lv_app_list = null;
	private BroadcastReceiver networkReceiver = null;//网络连接管理	
	
	//登录账号密码
	private MyEditText main_et_login_username = null;
	private MyEditText main_et_login_password = null;
	private MyTextView main_tv_login_password = null;
	private MyTextView main_tv_login_username = null;
	private MyImageButton main_btn_login = null;
	
	//修改密码
	private MyTextView main_tv_modi_old_pwd = null;
	private MyTextView main_tv_modi_new_pwd = null;
	private MyTextView main_tv_modi_confirm_pwd = null;
	private MyEditText main_et_modi_old_pwd = null;
	private MyEditText main_et_modi_new_pwd = null;
	private MyEditText main_et_modi_confirm_pwd = null;
	private MyImageButton main_btn_modi = null;
	
	//用户信息标题
	private MyTextView main_tv_info_tip = null;
	private MyImageButton main_btn_change_user = null;
	//APP权限
	private MyTextView main_tv_open_new_app = null;
	private MyTextView main_tv_app_power_tip = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalApplication.getInstance().AddActivity(this);
        //屏幕常亮
      	getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
      	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      	setContentView(R.layout.activity_user);
      	//开启切换屏幕为竖屏
      	if(MyVar.SYSTEM_LAYOUT_TYPE==1){
	        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}	        
        }else{
        	if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
  				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
  			}
        	AbsoluteLayout layout = (AbsoluteLayout)this.findViewById(R.id.layout_user);
        	layout.setBackgroundResource(R.drawable.main_v);
        }
      	
      	MyClass.CheckError();
		MyVar.user_handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what==0x1000){
					String str_ret = (String)msg.obj;
					if(str_ret==null||"ERROR".equals(str_ret)==true){
						MyClass.ShowOkDlgWindow(UserActivity.this,"账号登录", "网络错误");
						return;
					}
					JSONObject json = MyClass.JsonInit(str_ret);
					if(json==null){
						MyClass.ShowOkDlgWindow(UserActivity.this,"账号登录", "服务器应答出错");
						return;
					}
					if(json.has("loginFlag")==false){
						MyClass.ShowOkDlgWindow(UserActivity.this,"账号登录", "服务器应答参数缺失");
						return;
					}
					boolean loginFlag = MyClass.GetJsonbool(json, "loginFlag");
					if(loginFlag==false){
						String message = MyClass.GetJsonString(json, "message");
						if(message==null||"".equals(message)==true){
							MyClass.ShowOkDlgWindow(UserActivity.this,"账号登录", "账号或密码错误");
						}else{
							MyClass.ShowOkDlgWindow(UserActivity.this,"账号登录", message);
						}
						return;
					}
					if(json.has("announcement")==false||json.has("companyNotice")==false||json.has("stationDto")==false||json.has("useAppDTOs")==false){
						MyClass.ShowOkDlgWindow(UserActivity.this,"账号登录", "登录成功,缺少重要参数");
						return;
					}
					MyVar.announcement = MyClass.GetJsonArray(json, "announcement");
					MyVar.company_notice = MyClass.GetJsonArray(json, "companyNotice");
					MyVar.station_dto = MyClass.GetJsonObject(json, "stationDto");
					MyVar.use_app_dtos = MyClass.GetJsonArray(json, "useAppDTOs");
					MyVar.server_apps_list = MyClass.GetJsonArray(json, "couldUseAppversions");
					//登录成功后保存用户数据
					MyVar.login_flag = true;
					SystemVar.username = main_et_login_username.getText().toString().trim();
					SystemVar.password = main_et_login_password.getText().toString().trim();
					SystemVar.SaveCfg();
					//保存共享数据
					JSONObject share_json = MyVar.station_dto;
					MyClass.JsonPutString(share_json, "stationCode", SystemVar.username);
					MyClass.WriteFile(share_json.toString(), MyVar.SHARE_DATA_PATH);
					//显示登录后页面
					ShowModiPwdWindow();
					ShowAppList();
					MyClass.ShowAppActivity(MyVar.CURRENT_PAGE_ID);
				}
				if(msg.what==0x1001){
					String str_ret = (String)msg.obj;
					if(str_ret==null||"ERROR".equals(str_ret)==true){
						MyClass.ShowOkDlgWindow(UserActivity.this,"账号登录", "网络错误");
						return;
					}
					JSONObject json = MyClass.JsonInit(str_ret);
					if(json==null){
						MyClass.ShowOkDlgWindow(UserActivity.this,"账号登录", "服务器应答出错");
						return;
					}
					if(json.has("loginFlag")==false){
						MyClass.ShowOkDlgWindow(UserActivity.this,"账号登录", "服务器应答参数缺失");
						return;
					}
					boolean loginFlag = MyClass.GetJsonbool(json, "loginFlag");
					if(loginFlag==false){
						String message = MyClass.GetJsonString(json, "message");
						if(message==null||"".equals(message)==true){
							MyClass.ShowOkDlgWindow(UserActivity.this,"账号登录", "账号或密码错误");
						}else{
							MyClass.ShowOkDlgWindow(UserActivity.this,"账号登录", message);
						}
					}
					if(json.has("announcement")==false||json.has("companyNotice")==false||json.has("stationDto")==false||json.has("useAppDTOs")==false){
						MyClass.ShowOkDlgWindow(UserActivity.this,"账号登录", "登录成功,缺少重要参数");
						return;
					}
					MyVar.announcement = MyClass.GetJsonArray(json, "announcement");
					MyVar.company_notice = MyClass.GetJsonArray(json, "companyNotice");
					MyVar.station_dto = MyClass.GetJsonObject(json, "stationDto");
					MyVar.use_app_dtos = MyClass.GetJsonArray(json, "useAppDTOs");
					MyVar.server_apps_list = MyClass.GetJsonArray(json, "couldUseAppversions");
					//登录成功后保存用户数据
					MyVar.login_flag = true;
					//保存共享数据
					JSONObject share_json = MyVar.station_dto;
					MyClass.JsonPutString(share_json, "stationCode", SystemVar.username);
					MyClass.WriteFile(share_json.toString(), MyVar.SHARE_DATA_PATH);
					//显示登录后页面
					ShowModiPwdWindow();
					ShowAppList();
					MyClass.ShowAppActivity(MyVar.CURRENT_PAGE_ID);
				}
				if(msg.what==0x1002){
					String str_ret = (String)msg.obj;
					if(str_ret==null||"ERROR".equals(str_ret)==true){
						MyClass.ShowOkDlgWindow(UserActivity.this,"修改密码", "网络错误");
						return;
					}
					JSONObject json = MyClass.JsonInit(str_ret);
					if(json==null){
						MyClass.ShowOkDlgWindow(UserActivity.this,"修改密码", "服务器应答出错");
						return;
					}
					if(json.has("updateFlag")==false){
						MyClass.ShowOkDlgWindow(UserActivity.this,"修改密码", "服务器应答参数缺失");
						return;
					}
					boolean loginFlag = MyClass.GetJsonbool(json, "updateFlag");
					if(loginFlag==false){						
						MyClass.ShowOkDlgWindow(UserActivity.this,"修改密码", "修改密码失败");
						return;
					}
					SystemVar.username = "";
					SystemVar.password = "";
					SystemVar.SaveCfg();
					MyVar.login_flag = false;
					MyVar.announcement = null;
					MyVar.company_notice = null;
					MyVar.station_dto = null;
					MyVar.use_app_dtos = null;
					MyVar.server_apps_list = null;
					ShowLoginWindow();
					MyClass.ShowOkDlgWindow(UserActivity.this,"修改密码", "修改密码成功,请重新登录");
				}
				if(msg.what==0x1003){
					String str_ret = (String)msg.obj;
					if(str_ret==null||"ERROR".equals(str_ret)==true){
						MyClass.ShowOkDlgWindow(UserActivity.this,"应用列表", "网络错误");
						return;
					}
					try{
						MyVar.server_apps_list = new JSONArray(str_ret);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if(MyVar.server_apps_list==null){
						MyClass.ShowOkDlgWindow(UserActivity.this,"应用列表", "服务器应答出错");
						return;
					}
					MyClass.ShowAppActivity(MyVar.CURRENT_PAGE_ID);
				}
				super.handleMessage(msg);
			}			
		};
		
		Thread.currentThread().setName("UserActivity");	
		MyVar.CURRENT_PAGE_ID = MyVar.USER_PAGE_ID;
		InitView();
	}
	
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
			tv_title_user.requestFocus();
			tv_title_setting.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {					
					MyClass.ShowSettingActivity(MyVar.CURRENT_PAGE_ID);
				}
			});
			tv_title_app.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					if(MyVar.login_flag==true){
						MyClass.ShowAppActivity(MyVar.CURRENT_PAGE_ID);
					}else{
						MyClass.ShowOkDlgWindow(UserActivity.this,"操作提示", "请登录后再访问应用库");
					}
				}
			});
		}else if(cur_activity_id==MyVar.APP_PAGE_ID){
			iv_title_setting_bg.setVisibility(View.GONE);
			iv_title_user_bg.setVisibility(View.GONE);
			tv_title_app.setTextColor(0xffffd150);
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
			tv_title_app.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {					
					if(MyVar.login_flag==true){
						MyClass.ShowAppActivity(MyVar.CURRENT_PAGE_ID);
					}else{
						MyClass.ShowOkDlgWindow(UserActivity.this,"操作提示", "请登录后再访问应用库");
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
		MyImageView iv_user_info = (MyImageView)this.findViewById(R.id.iv_user_info);
		iv_user_info.setVisibility(View.VISIBLE);
		//用户信息背景
		MyImageView iv_user_info2 = (MyImageView)this.findViewById(R.id.iv_user_info2_v);
		iv_user_info2.setVisibility(View.GONE);
		//通行证文字
		main_tv_login_username = (MyTextView)this.findViewById(R.id.main_tv_login_username);
		main_tv_login_username.setTextColor(0xfffefffc);
		main_tv_login_username.setGravity(Gravity.LEFT);
		main_tv_login_username.setVisibility(View.VISIBLE);
		//密码文字
		main_tv_login_password = (MyTextView)this.findViewById(R.id.main_tv_login_password);
		main_tv_login_password.setTextColor(0xfffefffc);
		main_tv_login_password.setGravity(Gravity.LEFT);
		main_tv_login_password.setVisibility(View.VISIBLE);
		//通行证输入框
		main_et_login_username = (MyEditText)this.findViewById(R.id.main_et_login_username);
		main_et_login_username.setHint("请输入通行证");
		main_et_login_username.setText("");
		main_et_login_username.requestFocus();
		main_et_login_username.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});//最大输入9个数字
//		main_et_login_username.setKeyListener(MyClass.GetTextKeyListener());
		main_et_login_username.setSingleLine(true);
		main_et_login_username.setVisibility(View.VISIBLE);
		//通行证密码输入框
		main_et_login_password = (MyEditText)this.findViewById(R.id.main_et_login_password);
		main_et_login_password.setHint("请输入密码");
		main_et_login_password.setText("");
		main_et_login_password.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});//最大输入9个数字
//		main_et_login_password.setKeyListener(MyClass.GetTextKeyListener());
		main_et_login_password.setSingleLine(true);
		main_et_login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
		main_et_login_password.setVisibility(View.VISIBLE);
		main_et_login_username.setOnKeyListener(new OnKeyListener() {			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if(arg1==KeyEvent.KEYCODE_DPAD_UP&&arg2.getAction()==MotionEvent.ACTION_DOWN){
					MyTextView tv_title_user = (MyTextView)findViewById(R.id.tv_title_user);
					tv_title_user.requestFocus();
					return true;
				}
				if(arg1==KeyEvent.KEYCODE_DPAD_DOWN&&arg2.getAction()==MotionEvent.ACTION_DOWN){
					main_et_login_password.requestFocus();
					return true;
				}
                //当键盘事件为enter 并且 获取事件也为down 则唤醒软件盘
                if(arg1==KeyEvent.KEYCODE_ENTER && arg2.getAction() == MotionEvent.ACTION_DOWN){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()){
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        if(getWindow().getAttributes().softInputMode==WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
                            main_et_login_password.requestFocus();
                        }
                    }
                    return true;
				}
                //当按到返回的时候
                if(arg1 == KeyEvent.KEYCODE_ESCAPE && arg2.getAction() == MotionEvent.ACTION_DOWN){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()&&getCurrentFocus()!=null){
                        if (getCurrentFocus().getWindowToken()!=null) {
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                }

				return false;
			}
		});
		main_et_login_password.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if(arg1==KeyEvent.KEYCODE_DPAD_UP&&arg2.getAction()==MotionEvent.ACTION_DOWN){
					main_et_login_username.requestFocus();
					return true;
				}
				if(arg1==KeyEvent.KEYCODE_DPAD_DOWN&&arg2.getAction()==MotionEvent.ACTION_DOWN){
					main_btn_login.requestFocus();
					return true;
				}
				if(arg1==KeyEvent.KEYCODE_ENTER && arg2.getAction() == MotionEvent.ACTION_DOWN){
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()){
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return true;
				}
                //当按到返回的时候
                if(arg1 == KeyEvent.KEYCODE_ESCAPE && arg2.getAction() == MotionEvent.ACTION_DOWN){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()&&getCurrentFocus()!=null){
                        if (getCurrentFocus().getWindowToken()!=null) {
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                    return true;
                }
				return false;
			}
		});
		//登录按钮
		main_btn_login = (MyImageButton)this.findViewById(R.id.main_btn_login);
		main_btn_login.SetImages(R.drawable.main_btn_login_up, R.drawable.main_btn_login_down);
		main_btn_login.setVisibility(View.VISIBLE);
		main_btn_login.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				String username = main_et_login_username.getText().toString().trim();
				String password = main_et_login_password.getText().toString().trim();
				MyClass.SendHttpRequest(MyVar.LOGIN_URL+"?stationCode="+username+"&password="+password,MyVar.user_handler,0x1000);
			}
		});
		//通信证信息
		main_tv_info_tip = (MyTextView)this.findViewById(R.id.main_tv_info_tip);
		main_tv_info_tip.setTextColor(0xfffdde86);
		main_tv_info_tip.setText("通信证："+SystemVar.username);
		main_tv_info_tip.setGravity(Gravity.CENTER);
		main_tv_info_tip.setVisibility(View.VISIBLE);
		//切换用户按钮
		main_btn_change_user = (MyImageButton)this.findViewById(R.id.main_btn_change_user);
		main_btn_change_user.SetImages(R.drawable.main_btn_change_user_up, R.drawable.main_btn_change_user_down);
		main_btn_change_user.setVisibility(View.VISIBLE);
		main_btn_change_user.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0){
				MyClass.UserLogout();
				ShowLoginWindow();
			}
		});
		//旧密码
		main_tv_modi_old_pwd = (MyTextView)this.findViewById(R.id.main_tv_modi_old_pwd);
		main_tv_modi_old_pwd.setTextColor(0xfffefffc);
		main_tv_modi_old_pwd.setGravity(Gravity.LEFT);
		main_tv_modi_old_pwd.setVisibility(View.VISIBLE);
		//新密码
		main_tv_modi_new_pwd = (MyTextView)this.findViewById(R.id.main_tv_modi_new_pwd);
		main_tv_modi_new_pwd.setTextColor(0xfffefffc);
		main_tv_modi_new_pwd.setGravity(Gravity.LEFT);
		main_tv_modi_new_pwd.setVisibility(View.VISIBLE);
		//确认密码
		main_tv_modi_confirm_pwd = (MyTextView)this.findViewById(R.id.main_tv_modi_confirm_pwd);
		main_tv_modi_confirm_pwd.setTextColor(0xfffefffc);
		main_tv_modi_confirm_pwd.setGravity(Gravity.LEFT);
		main_tv_modi_confirm_pwd.setVisibility(View.VISIBLE);
		//原密码输入框
		main_et_modi_old_pwd = (MyEditText)this.findViewById(R.id.main_et_modi_old_pwd);
		main_et_modi_old_pwd.setHint("请输入原密码");
		main_et_modi_old_pwd.setText("");
		main_et_modi_old_pwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});//最大输入24个数字
//		main_et_modi_old_pwd.setKeyListener(MyClass.GetTextKeyListener());
		main_et_modi_old_pwd.setSingleLine(true);
		main_et_modi_old_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
		main_et_modi_old_pwd.setVisibility(View.VISIBLE);
		
		//新密码输入框
		main_et_modi_new_pwd = (MyEditText)this.findViewById(R.id.main_et_modi_new_pwd);
		main_et_modi_new_pwd.setHint("请输入新密码");
		main_et_modi_new_pwd.setText("");
		main_et_modi_new_pwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});//最大输入24个数字
//		main_et_modi_new_pwd.setKeyListener(MyClass.GetTextKeyListener());
		main_et_modi_new_pwd.setSingleLine(true);
		main_et_modi_new_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
		main_et_modi_new_pwd.setVisibility(View.VISIBLE);
		//确认密码输入框
		main_et_modi_confirm_pwd = (MyEditText)this.findViewById(R.id.main_et_modi_confirm_pwd);
		main_et_modi_confirm_pwd.setHint("请再次输入密码");
		main_et_modi_confirm_pwd.setText("");
		main_et_modi_confirm_pwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});//最大输入24个数字
//		main_et_modi_confirm_pwd.setKeyListener(MyClass.GetTextKeyListener());
		main_et_modi_confirm_pwd.setSingleLine(true);
		main_et_modi_confirm_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
		main_et_modi_confirm_pwd.setVisibility(View.VISIBLE);
		main_et_modi_old_pwd.setOnKeyListener(new OnKeyListener() {			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				MyClass.PrintInfoLog(arg1+"");
                MyClass.PrintInfoLog(arg2.getAction()+"");
                if(arg1==KeyEvent.KEYCODE_DPAD_UP&&arg2.getAction()==MotionEvent.ACTION_DOWN){
					main_btn_change_user.requestFocus();
					return true;
				}
				if(arg1==KeyEvent.KEYCODE_DPAD_DOWN&&arg2.getAction()==MotionEvent.ACTION_DOWN){
					main_et_modi_new_pwd.requestFocus();
					return true;
				}
                if(arg1 == KeyEvent.KEYCODE_ENTER && arg2.getAction()==MotionEvent.ACTION_DOWN){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()){
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return true;
                }
				return false;
			}
		});
		main_et_modi_new_pwd.setOnKeyListener(new OnKeyListener() {			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if(arg1==KeyEvent.KEYCODE_DPAD_UP&&arg2.getAction()==MotionEvent.ACTION_DOWN){
					main_et_modi_old_pwd.requestFocus();
					return true;
				}
				if(arg1==KeyEvent.KEYCODE_DPAD_DOWN&&arg2.getAction()==MotionEvent.ACTION_DOWN){
					main_et_modi_confirm_pwd.requestFocus();
					return true;
				}
				if(arg1 == KeyEvent.KEYCODE_ENTER && arg2.getAction()==MotionEvent.ACTION_DOWN){
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					if(imm.isActive()){
						imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					}
					return true;
				}
				return false;
			}
		});
		main_et_modi_confirm_pwd.setOnKeyListener(new OnKeyListener() {			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if(arg1==KeyEvent.KEYCODE_DPAD_UP&&arg2.getAction()==MotionEvent.ACTION_DOWN){
					main_et_modi_new_pwd.requestFocus();
					return true;
				}
				if(arg1==KeyEvent.KEYCODE_DPAD_DOWN&&arg2.getAction()==MotionEvent.ACTION_DOWN){
					main_btn_modi.requestFocus();
					return true;
				}
				if(arg1 == KeyEvent.KEYCODE_ENTER && arg2.getAction()==MotionEvent.ACTION_DOWN){
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					if(imm.isActive()){
						imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					}
					return true;
				}
				return false;
			}
		});
		//修改密码按钮
		main_btn_modi = (MyImageButton)this.findViewById(R.id.main_btn_modi);
		main_btn_modi.SetImages(R.drawable.main_btn_modi_up, R.drawable.main_btn_modi_down);
		main_btn_modi.setVisibility(View.VISIBLE);
		main_btn_modi.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				String old_pwd = main_et_modi_old_pwd.getText().toString().trim();
				String new_pwd = main_et_modi_new_pwd.getText().toString().trim();
				String confirm_pwd = main_et_modi_confirm_pwd.getText().toString().trim();
				if(old_pwd==null||new_pwd==null||confirm_pwd==null){
					MyClass.ShowOkDlgWindow(UserActivity.this, "修改密码", "参数出错");
					return;
				}
				if("".contains(old_pwd)==true||"".contains(new_pwd)==true||"".contains(confirm_pwd)==true){
					MyClass.ShowOkDlgWindow(UserActivity.this, "修改密码", "密码不能为空");
					return;
				}
				if(old_pwd.equals(SystemVar.password)==false){
					MyClass.ShowOkDlgWindow(UserActivity.this, "修改密码", "原始密码错误");
					return;
				}
				if(new_pwd.equals(confirm_pwd)==false){
					MyClass.ShowOkDlgWindow(UserActivity.this, "修改密码", "新密码与确认密码不一致");
					return;
				}
				MyClass.SendHttpRequest(MyVar.MODI_PWD_URL+"?stationCode="+SystemVar.username+"&password="+new_pwd,MyVar.user_handler,0x1002);
			}
		});
		//权限
		main_tv_app_power_tip = (MyTextView)this.findViewById(R.id.main_tv_app_power_tip);
		main_tv_app_power_tip.setTextColor(0xffe8e8e8);
		main_tv_app_power_tip.setGravity(Gravity.CENTER);
		main_tv_app_power_tip.setVisibility(View.VISIBLE);
		main_tv_open_new_app = (MyTextView)this.findViewById(R.id.main_tv_open_new_app);
		main_tv_open_new_app.setTextColor(0xfffdffff);
		main_tv_open_new_app.setGravity(Gravity.CENTER);
		main_tv_open_new_app.setVisibility(View.VISIBLE);
		
		//app授权列表
		lv_app_list = (MyListView)this.findViewById(R.id.lv_app_list);
		
		
		if(MyVar.SYSTEM_LAYOUT_TYPE==1){
			iv_user_info.SetImages(R.drawable.main_user_info);
			iv_user_info2.setVisibility(View.GONE);
			iv_user_info.InitSize(90, 205, 897, 1340);
			main_tv_info_tip.InitSize(244, 345, 596, 70,45);
			main_btn_change_user.InitSize(856, 366, 102, 37);
			
			main_tv_login_username.InitSize(350, 494, 160, 50,36);			
			main_tv_login_password.InitSize(350, 590, 160, 50,36);			
			main_et_login_username.InitSize(510, 489, 240, 70, 30);		
			main_et_login_password.InitSize(510, 585, 240, 70, 30);
			main_btn_login.InitSize(391, 693, 305, 84);
			
			main_tv_modi_old_pwd.InitSize(352, 490, 210, 50,36);
			main_tv_modi_new_pwd.InitSize(352, 570, 210, 50,36);
			main_tv_modi_confirm_pwd.InitSize(352, 650, 210, 50,36);
			main_et_modi_old_pwd.InitSize(540, 484, 240, 70, 30);
			main_et_modi_new_pwd.InitSize(540, 564, 240, 70, 30);
			main_et_modi_confirm_pwd.InitSize(540, 644, 240, 70, 30);
			main_btn_modi.InitSize(391, 724, 305, 84);
			
			main_tv_app_power_tip.InitSize(234, 976, 610, 50,36);			
			main_tv_open_new_app.InitSize(206, 1474, 679, 40,22);			
			lv_app_list.InitSize(226, 1039, 624, 404);
		}else{
			iv_user_info.SetImages(R.drawable.main_user_info_v);
			iv_user_info.InitSize(130, 110, 827, 831);
			iv_user_info2.SetImages(R.drawable.main_user_info2_v);
			iv_user_info2.setVisibility(View.VISIBLE);			
			iv_user_info2.InitSize(950, 110, 827, 831);
			main_tv_info_tip.InitSize(244, 263, 596, 70,45);
			main_btn_change_user.InitSize(773, 284, 102, 37);
			
			main_tv_login_username.InitSize(350-20, 494-60, 160, 50,36);			
			main_tv_login_password.InitSize(350-20, 590-60, 160, 50,36);			
			main_et_login_username.InitSize(510-20, 489-60, 240, 70, 30);		
			main_et_login_password.InitSize(510-20, 585-60, 240, 70, 30);
			main_btn_login.InitSize(391, 693-30, 305, 84);
			
			main_tv_modi_old_pwd.InitSize(352-20, 490-60, 210, 50,36);
			main_tv_modi_new_pwd.InitSize(352-20, 570-60, 210, 50,36);
			main_tv_modi_confirm_pwd.InitSize(352-20, 650-60, 210, 50,36);
			main_et_modi_old_pwd.InitSize(540-20, 484-60, 240, 70, 30);
			main_et_modi_new_pwd.InitSize(540-20, 564-60, 240, 70, 30);
			main_et_modi_confirm_pwd.InitSize(540-20, 644-60, 240, 70, 30);
			main_btn_modi.InitSize(391, 724-30, 305, 84);
			
			main_tv_app_power_tip.InitSize(1059, 298, 610, 50,36);			
			main_tv_open_new_app.InitSize(1031, 853, 679, 40,22);	
			lv_app_list.InitSize(1051, 368, 624, 404);
		}
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
	
	//显示授权app列表
	public void ShowAppList(){
		listItemAdapter = new AppListAdapter();			    
		lv_app_list.setAdapter(listItemAdapter);		
	}
	
	//显示登录后用户信息
	private void ShowUserInfoWindow(){
		HideAll();
	}
	//显示修改密码
	private void ShowModiPwdWindow(){
		HideAll();
		main_tv_modi_old_pwd.setVisibility(View.VISIBLE);
		main_tv_modi_new_pwd.setVisibility(View.VISIBLE);
		main_tv_modi_confirm_pwd.setVisibility(View.VISIBLE);
		main_et_modi_old_pwd.setVisibility(View.VISIBLE);
		main_et_modi_new_pwd.setVisibility(View.VISIBLE);
		main_et_modi_confirm_pwd.setVisibility(View.VISIBLE);
		main_btn_modi.setVisibility(View.VISIBLE);
		main_tv_info_tip.setVisibility(View.VISIBLE);
		main_btn_change_user.setVisibility(View.VISIBLE);
		main_tv_open_new_app.setVisibility(View.VISIBLE);
		main_tv_app_power_tip.setVisibility(View.VISIBLE);
		lv_app_list.setVisibility(View.VISIBLE);
		
		main_tv_info_tip.setText("通信证："+SystemVar.username);
	}
	//隐藏所有替换控件
	public void HideAll(){
		main_et_login_username.setVisibility(View.GONE);
		main_et_login_password.setVisibility(View.GONE);
		main_tv_login_password.setVisibility(View.GONE);
		main_tv_login_username.setVisibility(View.GONE);
		main_btn_login.setVisibility(View.GONE);
		
		main_tv_modi_old_pwd.setVisibility(View.GONE);
		main_tv_modi_new_pwd.setVisibility(View.GONE);
		main_tv_modi_confirm_pwd.setVisibility(View.GONE);
		main_et_modi_old_pwd.setVisibility(View.GONE);
		main_et_modi_new_pwd.setVisibility(View.GONE);
		main_et_modi_confirm_pwd.setVisibility(View.GONE);
		main_btn_modi.setVisibility(View.GONE);
		
		main_btn_change_user.setVisibility(View.GONE);
		main_tv_info_tip.setVisibility(View.GONE);
		
		main_tv_open_new_app.setVisibility(View.GONE);
		main_tv_app_power_tip.setVisibility(View.GONE);
		
		lv_app_list.setVisibility(View.GONE);
	}
	//显示登录框
	public void ShowLoginWindow(){
		HideAll();
		main_et_login_username.setVisibility(View.VISIBLE);
		main_et_login_password.setVisibility(View.VISIBLE);
		main_tv_login_password.setVisibility(View.VISIBLE);
		main_tv_login_username.setVisibility(View.VISIBLE);
		main_btn_login.setVisibility(View.VISIBLE);
	}
	
	//取消注册网络监控
	private void UnRegNetworkStatusEvent(){
		if(networkReceiver!=null){
			unregisterReceiver(networkReceiver);
			networkReceiver = null;
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

	@Override
	protected void onResume() {
		if(MyVar.login_flag==false){
			if("".equals(SystemVar.username)==true||"".equals(SystemVar.password)==true){
				this.ShowLoginWindow();
			}else{
				this.ShowLoginWindow();
				MyClass.SendHttpRequest(MyVar.LOGIN_URL+"?stationCode="+SystemVar.username+"&password="+SystemVar.password,MyVar.user_handler,0x1001);
			}			
		}else{			
			this.ShowModiPwdWindow();
			this.ShowAppList();
		}
		super.onResume();
	}
}
