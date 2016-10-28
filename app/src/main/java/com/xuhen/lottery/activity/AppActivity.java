package com.xuhen.lottery.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baiyilin.lottery.R;
import com.eoncn.download.Download;
import com.xuhen.lottery.cls.MyThread;
import com.xuhen.lottery.common.GlobalApplication;
import com.xuhen.lottery.common.MyClass;
import com.xuhen.lottery.common.MyVar;
import com.xuhen.lottery.common.SystemVar;
import com.xuhen.lottery.view.CustomDialog;
import com.xuhen.lottery.view.MyBorderTextView;
import com.xuhen.lottery.view.MyImageButton;
import com.xuhen.lottery.view.MyImageView;
import com.xuhen.lottery.view.MyListView;
import com.xuhen.lottery.view.MyTextView;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;

public class AppActivity extends Activity {
	//APP列表
	private GridView gv_list = null;
	private AppsAdapter list_adapter = null;
	//所有可启动的APK
	private List<ResolveInfo> mApps = null;
	//主页显示PKG
	private ArrayList<String> main_pkg_list = null;
	//APP升级
	private MyImageView iv_app_update_bg = null;
	//APP升级内容
	private MyBorderTextView tv_app_update_message = null;
	//APP升级下一条按钮
	private MyImageButton btn_next_app_news = null;
	//APP升级按钮
	private MyImageButton btn_app_update = null;
	//公司公告背景
	private MyImageView iv_company_notice_bg = null;
	//公司公告正文内容
	private MyBorderTextView tv_company_notice = null;
	//应用ID对应表
	private Hashtable<String,String> arr_app_ids = new Hashtable<String,String>();
	//应用权限检查异步对像
	private ResolveInfo cur_select_apk = null;
	private String error_message = null;
	//公司公告显示序号
	private int company_notice_number = 0;
	//公司公告下一条按钮
	private MyImageButton btn_next_company_notice = null;
	//公司公告剩余数
	private MyTextView tv_company_notice_number = null;
	//通告对话框
	private CustomDialog dlg = null;
	//当前需要升级的APP地址
	private String current_app_update_url = null;
	//app升级显示序号
	private int app_update_number = 0;
	//app升级剩余数
	private MyTextView tv_app_update_number = null;
	//下载线程
	private MyThread th_down_apk = null;
	
	private JSONArray app_update_info_list = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalApplication.getInstance().AddActivity(this);
        //屏幕常亮
      	getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_app);
        //开启切换屏幕为竖屏
        if(MyVar.SYSTEM_LAYOUT_TYPE==1){
	        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
        }else{
        	if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
  				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
  			}
        	AbsoluteLayout layout = (AbsoluteLayout)this.findViewById(R.id.layout_app);
        	layout.setBackgroundResource(R.drawable.main_v);
        }
        
        MyClass.CheckError();
        
        MyVar.app_handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what==0x1000){
					String str_ret = (String)msg.obj;
					if(str_ret==null||"ERROR".equals(str_ret)==true){
						MyClass.ShowOkDlgWindow(AppActivity.this,"权限检查", "网络错误");
						return;
					}
					JSONObject json = MyClass.JsonInit(str_ret);
					if(json==null){
						MyClass.ShowOkDlgWindow(AppActivity.this,"权限检查", "服务器应答出错");
						return;
					}
					if(json.has("useFlag")==false){
						MyClass.ShowOkDlgWindow(AppActivity.this,"权限检查", "服务器应答参数缺失");
						return;
					}
					boolean useFlag = MyClass.GetJsonbool(json, "useFlag");
					if(useFlag==false){
						error_message = MyClass.GetJsonString(json, "message");
						//获取上级代理信息
						String station_id = MyClass.GetStationId();
						MyClass.SendHttpRequest(MyVar.PARENT_AGENT_URL+"?stationId="+station_id,MyVar.app_handler,0x1001);
						return;
					}
					//该应用的包名
			        String pkg = cur_select_apk.activityInfo.packageName;
			        //应用的主activity类
			        String cls = cur_select_apk.activityInfo.name;            
			        ComponentName componet = new ComponentName(pkg, cls);            
			        Intent i = new Intent();
			        i.setComponent(componet);
			        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        startActivity(i);
				}
				if(msg.what==0x1001){
					String str_ret = (String)msg.obj;
					if(str_ret==null||"ERROR".equals(str_ret)==true){
						MyClass.ShowOkDlgWindow(AppActivity.this,"查询代理", "网络错误");
						return;
					}
					JSONObject json = MyClass.JsonInit(str_ret);
					if(json==null){
						MyClass.ShowOkDlgWindow(AppActivity.this,"查询代理", "服务器应答出错");
						return;
					}
					if(json.has("name")==false||json.has("telephone")==false){
						MyClass.ShowOkDlgWindow(AppActivity.this,"查询代理", "无法查询到代理信息");
						return;
					}
					String agent_name = MyClass.GetJsonString(json, "name");
					String agent_tel = MyClass.GetJsonString(json, "telephone");
					if(error_message==null||"".equals(error_message)==true){
						error_message = "你当前还没有权限使用本应用";
					}

					int width = 833;
					int height = 930;
					dlg = new CustomDialog(AppActivity.this);
					dlg.SetSize(width, height);
					dlg.show();
					dlg.SetTitleVisibility(false);
					dlg.SetTitle("应用提示");
					dlg.SetButtonStatus(dlg.btn_cancel, View.INVISIBLE);
					dlg.SetButtonStatus(dlg.btn_ok, View.INVISIBLE);
					dlg.RemoveAddView();
					MyImageView iv_bg = new MyImageView(AppActivity.this);
					iv_bg.SetImages(R.drawable.app_iv_app_error_bg);
					iv_bg.InitSize(0, 0, 833, 930);
					dlg.AddView(iv_bg);
					
					MyTextView tv_line1 = new MyTextView(AppActivity.this);
					tv_line1.setText(error_message);
					tv_line1.InitSize(75, 234, 700, 80, 48);
					tv_line1.setTextColor(0xff07253c);
					tv_line1.setGravity(Gravity.LEFT);
					dlg.AddView(tv_line1);
					MyTextView tv_line2 = new MyTextView(AppActivity.this);
					tv_line2.setText("开通权限请联系代理人员");
					tv_line2.InitSize(75, 313, 642, 80, 48);
					tv_line2.setTextColor(0xff07253c);
					tv_line2.setGravity(Gravity.LEFT);
					dlg.AddView(tv_line2);
					MyTextView tv_line3 = new MyTextView(AppActivity.this);
					tv_line3.setText("代理:"+agent_name);
					tv_line3.InitSize(75, 431, 642, 85, 60);
					tv_line3.setTextColor(0xff052537);
					tv_line3.setGravity(Gravity.LEFT);
					dlg.AddView(tv_line3);
					MyTextView tv_line4 = new MyTextView(AppActivity.this);
					tv_line4.setText("电话:"+agent_tel);
					tv_line4.InitSize(75, 505, 642, 85, 60);
					tv_line4.setTextColor(0xff052537);
					tv_line4.setGravity(Gravity.LEFT);
					dlg.AddView(tv_line4);
					MyTextView tv_line5 = new MyTextView(AppActivity.this);
					tv_line5.setText("公司客服:400-1111-354");
					tv_line5.InitSize(75, 629, 642, 85, 46);
					tv_line5.setTextColor(0xff06263a);
					tv_line5.setGravity(Gravity.LEFT);
					dlg.AddView(tv_line5);
					MyImageButton btn_confirm_read = new MyImageButton(AppActivity.this);					
					btn_confirm_read.SetImages(R.drawable.app_btn_app_ok_up, R.drawable.app_btn_app_ok_down);
					btn_confirm_read.InitSize(273, 751, 286, 110);
					btn_confirm_read.setOnClickListener(new OnClickListener() {			
						@Override
						public void onClick(View arg0){
							if(dlg==null){
								return;
							}
							dlg.dismiss();
							dlg=null;
						}
					});
					dlg.AddView(btn_confirm_read);
				}
				if(msg.what==0x1002){
					if(dlg==null){
						return;
					}
					dlg.dismiss();
					ShowAannounceDlg();
				}
				if(msg.what==0x1003){
					if(dlg==null){
						return;
					}
					dlg.SetMessage("正在下载中,已完成下载 "+String.format("%.1f", (Double)msg.obj)+" %");
				}
				if(msg.what==0x1004){
					if(dlg==null){
						return;
					}
					dlg.dismiss();
					GuiInstallApk(MyVar.APK_UPDATE_FILE);
				}
				if(msg.what==0x1005){
					if(dlg==null){
						return;
					}
					dlg.SetMessage("下载应用失败,原因:"+(String)msg.obj);
				}
				if(msg.what==0x1006){
					//下边不用处理
				}
				if(msg.what==0x1007){
					/*int ret = IpAddressSet.GetResult();
					//if(ret==999){
					if(ret==998){
						int width = 720;
						int height = width*5/8;
						CustomDialog dlg = new CustomDialog(AppActivity.this);
						dlg.SetSize(width, height);
						dlg.show();
						dlg.SetTitle(getResources().getString(R.string.app_update));
						dlg.SetButtonStatus(dlg.btn_cancel, View.INVISIBLE);
						dlg.SetButtonStatus(dlg.btn_ok, View.INVISIBLE);
						dlg.SetMessage(getResources().getString(R.string.launcher_date_expire));
						dlg.SetMessageGravity(Gravity.CENTER);
					}*/
					MyTextView tv_title_app = (MyTextView)findViewById(R.id.tv_title_app);
					tv_title_app.setFocusable(true);
					tv_title_app.requestFocus();
				}
				if(msg.what==0x1008){
					int i =  9/0;
				}
				super.handleMessage(msg);
			}			
		};
		
		if(MyVar.server_apps_list!=null){
			for(int i=0;i<MyVar.server_apps_list.length();i++){
				JSONObject json = null;
				try {
					json = MyVar.server_apps_list.getJSONObject(i);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(json==null){
					continue;
				}
				if(json.has("appName")==false||json.has("appId")==false){
					continue;
				}
				String app_name = MyClass.GetJsonString(json, "appName");
				String app_id = MyClass.GetJsonString(json, "appId");
				arr_app_ids.put(app_id, app_name);
			}
		}
		
		//读取软件列表
		ReloadAppList();
		list_adapter = new AppsAdapter();
		Thread.currentThread().setName("AppActivity");	
		MyVar.CURRENT_PAGE_ID = MyVar.APP_PAGE_ID;
		InitView();
		//读取通告
		if(MyVar.announcement!=null){
			ShowAannounceDlg();
		}
		//公司公告
		ShowCompanyNotice();
		//升级APP
		app_update_info_list = GetUpdateAppList();
		ShowNextAppUpdate();
		StartCheckIpAddress();
	}
	
	private void StartCheckIpAddress(){
		MyClass.SendMessageDelay(MyVar.app_handler, 0x1007,100);
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
			tv_title_app.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {					
					if(MyVar.login_flag==true){
						MyClass.ShowAppActivity(MyVar.CURRENT_PAGE_ID);
					}else{
						MyClass.ShowOkDlgWindow(AppActivity.this,"操作提示", "请登录后再访问应用库");
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
			tv_title_app.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {					
					if(MyVar.login_flag==true){
						MyClass.ShowAppActivity(MyVar.CURRENT_PAGE_ID);
					}else{
						MyClass.ShowOkDlgWindow(AppActivity.this,"操作提示", "请登录后再访问应用库");
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
		//APP列表
		this.gv_list = (GridView)this.findViewById(R.id.gv_list);
		int screen_width = MyVar.GetScreenWidth();
		int screen_height = MyVar.GetScreenHeight();
		if(MyVar.SYSTEM_LAYOUT_TYPE==1){
			int view_new_x = 0*screen_width/MyVar.GetDesignWidth();
			int view_new_y = (120+22)*screen_height/MyVar.GetDesignHeight();
			int view_new_width = MyVar.GetDesignWidth()*screen_width/MyVar.GetDesignWidth();
			int view_new_height = 1493*screen_height/MyVar.GetDesignHeight();
			LayoutParams layout = new LayoutParams(view_new_width,view_new_height,view_new_x,view_new_y);
			this.gv_list.setLayoutParams(layout);
		}else{
			int view_new_x = 0*screen_width/MyVar.GetDesignWidth();
			int view_new_y = 120*screen_height/MyVar.GetDesignHeight();
			int view_new_width = MyVar.GetDesignWidth()*screen_width/MyVar.GetDesignWidth();
			int view_new_height = (1080-82-82)*screen_height/MyVar.GetDesignHeight();
			LayoutParams layout = new LayoutParams(view_new_width,view_new_height,view_new_x,view_new_y);
			this.gv_list.setLayoutParams(layout);
			this.gv_list.setNumColumns(5);
		}
		//重新布局大小
		this.gv_list.setAdapter(list_adapter);
		this.gv_list.setOnItemClickListener(listener);
		//APP升级
		iv_app_update_bg = (MyImageView)this.findViewById(R.id.app_iv_app_update_bg);
		iv_app_update_bg.SetImages(R.drawable.app_iv_app_update_bg);
		iv_app_update_bg.setVisibility(View.VISIBLE);
		//APP升级内容信息
		tv_app_update_message = (MyBorderTextView)this.findViewById(R.id.app_tv_app_update_message);
		tv_app_update_message.setTextColor(0xff04263a);
		tv_app_update_message.SetBorderWidth(2);
		tv_app_update_message.SetBorderColor(0xffffffff);
		tv_app_update_message.setGravity(Gravity.LEFT);
		tv_app_update_message.setVisibility(View.VISIBLE);
		//APP升级下一条按钮
		btn_next_app_news = (MyImageButton)this.findViewById(R.id.app_btn_next_app_news);
		btn_next_app_news.SetImages(R.drawable.app_btn_next_company_notice_up, R.drawable.app_btn_next_company_notice_down);
		btn_next_app_news.setVisibility(View.VISIBLE);
		btn_next_app_news.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0){
				ShowNextAppUpdate();
			}
		});
		//APP升级按钮
		btn_app_update = (MyImageButton)this.findViewById(R.id.app_btn_app_update);
		btn_app_update.SetImages(R.drawable.app_btn_app_update_up, R.drawable.app_btn_app_update_down);
		btn_app_update.setVisibility(View.VISIBLE);
		btn_app_update.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0){
				StartAppUpdate();
			}
		});
		//公司公告剩余数
		tv_app_update_number = (MyTextView)this.findViewById(R.id.app_tv_app_update_number);
		//公司公告
		iv_company_notice_bg = (MyImageView)this.findViewById(R.id.app_iv_company_notice_bg);
		iv_company_notice_bg.SetImages(R.drawable.app_iv_company_notice_bg);
		iv_company_notice_bg.setVisibility(View.VISIBLE);
		//公司公告正文内容
		tv_company_notice = (MyBorderTextView)this.findViewById(R.id.app_tv_company_notice);
		tv_company_notice.setTextColor(0xff052635);
		tv_app_update_message.SetBorderWidth(2);
		tv_app_update_message.SetBorderColor(0xffffffff);
		tv_company_notice.setGravity(Gravity.LEFT);
		tv_company_notice.setVisibility(View.VISIBLE);
		//公司公告下一条按钮
		btn_next_company_notice = (MyImageButton)this.findViewById(R.id.app_btn_next_company_notice);
		btn_next_company_notice.SetImages(R.drawable.app_btn_next_company_notice_up, R.drawable.app_btn_next_company_notice_down);
		btn_next_company_notice.setVisibility(View.VISIBLE);
		btn_next_company_notice.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0){
				ShowCompanyNotice();
			}
		});
		//公司公告剩余数
		tv_company_notice_number = (MyTextView)this.findViewById(R.id.app_tv_app_company_notice_number);
		if(MyVar.SYSTEM_LAYOUT_TYPE==1){
			//app升级布局
			iv_app_update_bg.InitSize(960-420, 900+840, 540, 180);			
			tv_app_update_message.InitSize(1045-420, 930+840, 750, 110,24);			
			btn_next_app_news.InitSize(1810-840, 930+840, 92, 62);		
			btn_app_update.InitSize(1811-840, 1005+840, 92, 62);
			tv_app_update_number.InitSize(1890-840, 918+840, 24, 24,14);
			tv_app_update_message.setLineSpacing(0, 1.3f);
			//公司公告布局
			iv_company_notice_bg.InitSize(0, 900+840, 540, 180);			
			tv_company_notice.InitSize(77, 930+840, 455, 110,24);
			btn_next_company_notice.InitSize(850-420, 1005+840, 92, 62);
			tv_company_notice_number.InitSize(930-420, 993+840, 24, 24, 14);
			tv_company_notice.setLineSpacing(0, 1.3f);
		}else{
			//app升级布局
			iv_app_update_bg.InitSize(960, 900, 960, 180);			
			tv_app_update_message.InitSize(1045, 930, 750, 110,24);			
			btn_next_app_news.InitSize(1810, 930, 92, 62);		
			btn_app_update.InitSize(1811, 1005, 92, 62);
			tv_app_update_number.InitSize(1890, 918, 24, 24,14);
			tv_app_update_message.setLineSpacing(0, 1.3f);
			//公司公告布局
			iv_company_notice_bg.InitSize(0, 900, 960, 180);			
			tv_company_notice.InitSize(77, 930, 563, 110,24);
			btn_next_company_notice.InitSize(850, 1005, 92, 62);
			tv_company_notice_number.InitSize(930, 993, 24, 24, 14);
			tv_company_notice.setLineSpacing(0, 1.3f);
		}
	}
	
	public class AppsAdapter extends BaseAdapter {
        public AppsAdapter() {
        	
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView img;
            TextView tv_title;
            
            View my_view = null;
        	my_view = View.inflate(getBaseContext(), R.layout.layout_app, null);
            img = (ImageView)my_view.findViewById(R.id.image);
            ViewGroup.LayoutParams layout = img.getLayoutParams();
            layout.width = 200*MyVar.GetScreenWidth()/MyVar.GetDesignWidth();
            layout.height = 200*MyVar.GetScreenWidth()/MyVar.GetDesignWidth();
            img.setLayoutParams(layout);
            tv_title = (TextView)my_view.findViewById(R.id.text);
            layout = tv_title.getLayoutParams();
            layout.width = 200*MyVar.GetScreenWidth()/MyVar.GetDesignWidth();
            layout.height = 60*MyVar.GetScreenWidth()/MyVar.GetDesignWidth();
            tv_title.setLayoutParams(layout);
            tv_title.setTextSize(30*MyVar.GetScreenWidth()/MyVar.GetDesignWidth()/MyVar.GetScaledDensity());
            String pkg_name = main_pkg_list.get(position);
            if("null".equals(pkg_name)==true){
            	img.setImageResource(R.drawable.app_iv_no_app);
            	tv_title.setText("应用虚拟位");
            	tv_title.setTextColor(0xff83a5b9);
            }else{
            	tv_title.setTextColor(0xffd9d9d9);
                ResolveInfo info = FindInfo(pkg_name);
                img.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));
                tv_title.setText(info.activityInfo.loadLabel(getPackageManager()).toString());
            }   
            tv_title.setFocusable(false);
            img.setFocusable(false);
            my_view.setFocusable(false);
            return my_view;
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

	private void LoadApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = getPackageManager().queryIntentActivities(mainIntent, 0);
    }
	private void ReloadAppList(){
		LoadApps();
		//首页显示包名
		main_pkg_list = new ArrayList<String>();
        for(int i=0;i<mApps.size();i++){
	        	String app_name = mApps.get(i).activityInfo.loadLabel(getPackageManager()).toString();
	        	if(arr_app_ids.containsValue(app_name)==false){
	        		continue;
	        	}
	        	String pkg_name = mApps.get(i).activityInfo.packageName;
	        	if("com.baiyilin.lottery".equals(pkg_name)==true || "com.xuhen.lottery".equals(pkg_name) == true){
	        		continue;
	        	}
	        	main_pkg_list.add(pkg_name);
        }
        int max_show_pkg_num = 9;
        if(MyVar.SYSTEM_LAYOUT_TYPE==0){
        	max_show_pkg_num = 10;
        }
        while(main_pkg_list.size()<max_show_pkg_num){
        	main_pkg_list.add("null");
        }
		//结束
        return;
	}
	
	private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
	        	String pkg_name = null;
		    pkg_name = main_pkg_list.get(position);       
	        if("null".equals(pkg_name)==true){
	        	return;
	        }
	        ResolveInfo info = FindInfo(pkg_name);
	        String app_name = info.activityInfo.loadLabel(getPackageManager()).toString();
	        //通过app_name查找appId
	        String app_id = FindAppId(app_name);
	        //获取station_id
	        String station_id = MyClass.GetStationId();
	        if(app_id==null||station_id==null){
	        	MyClass.ShowOkDlgWindow(AppActivity.this, "应用提示", "APP权限混乱,请联系管理员");
	        	return;
	        }
	        //如果前一次请求没完成，不能继续点击新的
	        if(MyVar.app_handler.hasMessages(0x1000)==true){				
				MyClass.ShowWindowText(AppActivity.this, "上一次应用请求授权未结束，请稍候再试...");
				return;
			}
	        //服务器请求校验APP
	        cur_select_apk = info;
	        MyClass.SendHttpRequest(MyVar.APP_POWER_URL+"?appId="+app_id+"&stationId="+station_id,MyVar.app_handler,0x1000);
        }
    };
    private String FindAppId(String app_name){
    	if(app_name==null){
    		return null;
    	}
    	if(arr_app_ids.containsValue(app_name)==false){
    		return null;
    	}
    	Enumeration<String> arr_ids = arr_app_ids.keys();
    	while(arr_ids.hasMoreElements()==true){
    		String key = arr_ids.nextElement();
    		if(arr_app_ids.get(key).equals(app_name)==true){
    			return key;
    		}
    	}
    	return null;
    }
    
    private void ShowAannounceDlg(){
    	//查找需要显示的id
    	JSONObject json = null;
    	String id = null;
    	boolean is_last_announce = false;
    	for(int i=0;i<MyVar.announcement.length();i++){
    		try {
				json = MyVar.announcement.getJSONObject(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		if(json==null){
    			json = null;
        		id = null;
    			continue;
    		}
    		id = MyClass.GetJsonString(json, "id");
    		if(id==null){
    			json = null;
        		id = null;
    			continue;
    		}
    		if(MyVar.read_annuonce_ids.contains(id)==false){
    			if(i==MyVar.announcement.length()-1){
    				is_last_announce = true;
    			}
    			break;
    		}
			
			json = null;
    		id = null;
    	}
    	if(json==null||id==null){
    		return;
    	}
    	String announcement_name = MyClass.GetJsonString(json, "announcementName");
    	String announcement_content = MyClass.GetJsonString(json, "announcementContent");
    	String create_time = MyClass.GetJsonString(json, "createTime").substring(0,10);
    	int width = 836;
		int height = 931;
		dlg = new CustomDialog(AppActivity.this);
		dlg.SetSize(width, height);
		dlg.show();
		dlg.SetTitleVisibility(false);
		dlg.SetTitle("应用提示");
		dlg.SetButtonStatus(dlg.btn_cancel, View.INVISIBLE);
		dlg.SetButtonStatus(dlg.btn_ok, View.INVISIBLE);
		dlg.RemoveAddView();
		MyImageView iv_bg = new MyImageView(AppActivity.this);
		iv_bg.SetImages(R.drawable.app_iv_announce_bg);
		iv_bg.InitSize(0, 0, 836, 931);
		dlg.AddView(iv_bg);
		MyBorderTextView tv_title = new MyBorderTextView(AppActivity.this);
		tv_title.setText(announcement_name);
		tv_title.getPaint().setFakeBoldText(true); 
		tv_title.InitSize(100, 28, 635, 90, 70);
		tv_title.setGravity(Gravity.CENTER);
		tv_title.setTextColor(0xff0c254c);
		tv_title.SetBorderWidth(3);
		tv_title.SetBorderColor(0xffbeddec);
		dlg.AddView(tv_title);
		MyTextView tv_message = new MyTextView(AppActivity.this);
		tv_message.setText(announcement_content);
		tv_message.InitSize(97, 204, 652, 373, 36);
		tv_message.setTextColor(0xff062638);
		tv_message.setGravity(Gravity.LEFT);
		dlg.AddView(tv_message);
		MyTextView tv_create_date = new MyTextView(AppActivity.this);
		tv_create_date.setText(create_time);
		tv_create_date.InitSize(567, 630, 184, 36, 24);
		tv_create_date.setTextColor(0xff072538);
		tv_create_date.setGravity(Gravity.LEFT);
		dlg.AddView(tv_create_date);
		MyImageButton btn_confirm_read = new MyImageButton(AppActivity.this);
		if(is_last_announce==true){
			btn_confirm_read.SetImages(R.drawable.app_btn_announce_confirm_read_up, R.drawable.app_btn_announce_confirm_read_down);
		}else{
			btn_confirm_read.SetImages(R.drawable.app_btn_announce_next_up, R.drawable.app_btn_announce_next_down);
		}
		btn_confirm_read.InitSize(230, 764, 381, 119);
		btn_confirm_read.setTag(dlg);
		final String str_id = id;
		if(is_last_announce==true){
			btn_confirm_read.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View arg0){
					/*if(SystemVar.read_annuonce_ids.equals("")==true){
						SystemVar.read_annuonce_ids = str_id;
					}else{
						SystemVar.read_annuonce_ids += ","+str_id;
					}
					SystemVar.SaveCfg();*/
					CustomDialog dlg = (CustomDialog)arg0.getTag();	
					dlg.dismiss();
					//向服务器报告，通信证下通告已查看
					String station_id = MyClass.GetStationId();
					MyClass.SendHttpRequest(MyVar.REPORT_READ_FINISH_URL+"?stationId="+station_id,MyVar.app_handler,0x1006);
					//ShowAannounceDlg();
				}
			});
		}else{
			btn_confirm_read.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View arg0){
					/*if(SystemVar.read_annuonce_ids.equals("")==true){
						SystemVar.read_annuonce_ids = str_id;
					}else{
						SystemVar.read_annuonce_ids += ","+str_id;
					}
					SystemVar.SaveCfg();*/
					CustomDialog dlg = (CustomDialog)arg0.getTag();	
					dlg.dismiss();
					ShowAannounceDlg();
				}
			});
		}
		dlg.AddView(btn_confirm_read);
		//原来设计，显示１０秒钟自动关闭
		/*if(MyVar.app_handler.hasMessages(0x1002)==true){
			MyClass.RemoveMessage(MyVar.app_handler, 0x1002);
		}
		MyClass.SendMessageDelay(MyVar.app_handler, 0x1002, 10000);*/
		//添加已经显示过的ID,本次登录不会再出现显示
		if("".equals(MyVar.read_annuonce_ids)==true){
			MyVar.read_annuonce_ids = id;
		}else{
			MyVar.read_annuonce_ids += ","+id;
		}
    }
    
    public void ShowCompanyNotice(){
    	//调试信息
    	//MyVar.company_notice = MyClass.JsonArrayInit("[{\"startTime\":1462809600000,\"id\":\"963a3c95-0686-47c2-9477-adbc989f2328\",\"createTime\":\"2016-05-10 18:32:44\",\"comnoticeContent\":\"辽宁福彩公告发布内容1\",\"endTimestr\":\"2016-05-31 00:00:00\",\"startTimestr\":\"2016-05-10 00:00:00\",\"comnoticeStatusName\":\"发布\",\"lotteryType\":\"2\",\"endTime\":1464624000000,\"comnoticeName\":\"辽宁福彩公告\",\"comnoticeStatus\":\"1\"},{\"startTime\":1462809600000,\"id\":\"963a3c95-0686-47c2-9477-adbc989f2328\",\"createTime\":\"2016-05-10 18:32:44\",\"comnoticeContent\":\"辽宁福彩公告发布内容2\",\"endTimestr\":\"2016-05-31 00:00:00\",\"startTimestr\":\"2016-05-10 00:00:00\",\"comnoticeStatusName\":\"发布\",\"lotteryType\":\"2\",\"endTime\":1464624000000,\"comnoticeName\":\"辽宁福彩公告\",\"comnoticeStatus\":\"1\"},{\"startTime\":1462809600000,\"id\":\"963a3c95-0686-47c2-9477-adbc989f2328\",\"createTime\":\"2016-05-10 18:32:44\",\"comnoticeContent\":\"辽宁福彩公告发布内容3\",\"endTimestr\":\"2016-05-31 00:00:00\",\"startTimestr\":\"2016-05-10 00:00:00\",\"comnoticeStatusName\":\"发布\",\"lotteryType\":\"2\",\"endTime\":1464624000000,\"comnoticeName\":\"辽宁福彩公告\",\"comnoticeStatus\":\"1\"}]");
    	if(MyVar.company_notice==null){
    		iv_company_notice_bg.setVisibility(View.GONE);
    		tv_company_notice.setVisibility(View.GONE);
    		btn_next_company_notice.setVisibility(View.GONE);
    		return;
    	}
    	iv_company_notice_bg.setVisibility(View.VISIBLE);
		tv_company_notice.setVisibility(View.VISIBLE);
		btn_next_company_notice.setVisibility(View.VISIBLE);
    	//查找需要显示的id
    	JSONObject json = null;
    	String id = null;
    	if(MyVar.company_notice.length()<=0){
    		//iv_company_notice_bg.setVisibility(View.GONE);
    		//tv_company_notice.setVisibility(View.GONE);
    		tv_company_notice_number.setVisibility(View.GONE);
    		btn_next_company_notice.setVisibility(View.GONE);
    		//return;
    	}
    	String company_notice_name = "";
    	String company_notice_content = "暂无公告信息";
    	String create_time = "2000-01-01";
    	if(MyVar.company_notice.length()>0){
    		company_notice_number = company_notice_number%MyVar.company_notice.length();
    	}
		try {
			if(company_notice_number == 0){
				json = null;
			}else {
				json = MyVar.company_notice.getJSONObject(company_notice_number);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(json!=null){
			id = MyClass.GetJsonString(json, "id");
			if(id!=null&&"".equals(id)==true){
				id = null;
			}
		}
    	if(json==null||id==null){
    		company_notice_name = "公告标题";
        	company_notice_content = "暂无公告信息";
        	create_time = "2000-01-01";
    	}else{
    		company_notice_name = MyClass.GetJsonString(json, "comnoticeName");
        	company_notice_content = MyClass.GetJsonString(json, "comnoticeContent");
        	create_time = MyClass.GetJsonString(json, "createTime").substring(0,10);
    	}
    	tv_company_notice.setText(company_notice_content);
    	if(MyVar.company_notice.length()>=2){
    		if(MyVar.company_notice.length()-company_notice_number>=2){
    			tv_company_notice_number.setVisibility(View.VISIBLE);
    			btn_next_company_notice.setVisibility(View.VISIBLE);
    			tv_company_notice_number.setText(Integer.toString(MyVar.company_notice.length()-company_notice_number-1));
    		}else{
    			tv_company_notice_number.setVisibility(View.GONE);
    			btn_next_company_notice.setVisibility(View.VISIBLE);
    		}
    	}else{
    		tv_company_notice_number.setVisibility(View.GONE);
    		btn_next_company_notice.setVisibility(View.GONE);
    	}
    	company_notice_number+=1;
    }
    
    private JSONArray GetUpdateAppList(){
    	JSONArray app_update_list = MyClass.JsonArrayInit();
    	if(MyVar.server_apps_list==null||MyVar.server_apps_list.length()<=0||app_update_number>=MyVar.server_apps_list.length()){
    		return app_update_list;
    	}
    	//查找需要显示的id
    	
    	for(int i=0;i<MyVar.server_apps_list.length();i++){
    		JSONObject json = null;
        	String app_id = "";
        	String app_name = "";
        	String app_url = "";
        	String app_pkg_name = "";
        	String app_message = "";
        	String app_version_code = "";
        	
    		json = MyClass.GetJsonbyJsonArray(MyVar.server_apps_list, i);
    		if(json==null){
    			continue;
    		}
    		app_id = MyClass.GetJsonString(json, "appId");
    		app_name = MyClass.GetJsonString(json, "appName");
    		app_url = MyClass.GetJsonString(json, "appVersionUrl");
    		app_version_code = MyClass.GetJsonString(json, "versionCode");
    		if(app_id==null||app_name==null||app_url==null||app_version_code==null){
    			continue;
    		}
    		if(app_url.contains("http://")==false){
    			app_url = MyVar.BASE_SERVER_URL+app_url;
    		}
    		app_pkg_name = this.FindPkgNameByAppName(app_name);
    		if(app_pkg_name==null){
    			app_message = "新应用［"+app_name+"］"+app_version_code+"\n可以安装，请升级安装";
    		}else{
    			PackageInfo pkg_info = this.FindPackageInfo(app_pkg_name);
    			if(pkg_info==null){
    				continue;
    			}
				String version_name = pkg_info.versionName;
				//如果版本一样，就不提示升级了
				if(app_version_code.equals(version_name)==true){
					continue;
				}
				app_message = "应用["+app_name+"]有新版本"+app_version_code+",\n更新内容如下:\n无";
    		}
    		JSONObject tmp_json = MyClass.JsonInit();
    		MyClass.JsonPutString(tmp_json, "app_id", app_id);
    		MyClass.JsonPutString(tmp_json, "app_name", app_name);
    		MyClass.JsonPutString(tmp_json, "app_url", app_url);
    		MyClass.JsonPutString(tmp_json, "app_pkg_name", app_pkg_name);
    		MyClass.JsonPutString(tmp_json, "app_message", app_message);
    		MyClass.JsonArrayPutJson(app_update_list, tmp_json);
    	}
		return app_update_list;
    }
    
    private void ShowNextAppUpdate(){
    	if(app_update_number<0){
    		return;
    	}
    	if(app_update_number>=app_update_info_list.length()){
    		app_update_number = 0;
    	}
    	ShowAppUpdate(app_update_number);
    	app_update_number+=1;
    }
    
    private void ShowAppUpdate(int id){
    	if(app_update_info_list==null||app_update_info_list.length()<=0){
    		//iv_app_update_bg.setVisibility(View.GONE);
    		btn_next_app_news.setVisibility(View.GONE);
    		//tv_app_update_message.setVisibility(View.GONE);
    		tv_app_update_number.setVisibility(View.GONE);
    		tv_app_update_message.setText("您当前系统已经是最新版本");
    		btn_app_update.setVisibility(View.GONE);
    		return;
    	}
    	iv_app_update_bg.setVisibility(View.VISIBLE);
		btn_next_app_news.setVisibility(View.VISIBLE);
		tv_app_update_message.setVisibility(View.VISIBLE);
		btn_app_update.setVisibility(View.VISIBLE);
		tv_app_update_number.setVisibility(View.VISIBLE);
		//获取第几条升级数据
    	JSONObject json = MyClass.GetJsonbyJsonArray(app_update_info_list, id);
    	if(json==null){
    		iv_app_update_bg.setVisibility(View.GONE);
    		btn_next_app_news.setVisibility(View.GONE);
    		tv_app_update_message.setVisibility(View.GONE);
    		btn_app_update.setVisibility(View.GONE);
    		tv_app_update_number.setVisibility(View.GONE);
    		return;
    	}
    	String message = MyClass.GetJsonString(json, "app_message");
    	current_app_update_url = MyClass.GetJsonString(json, "app_url");
    	if(message==null||current_app_update_url==null){
    		iv_app_update_bg.setVisibility(View.GONE);
    		btn_next_app_news.setVisibility(View.GONE);
    		tv_app_update_message.setVisibility(View.GONE);
    		btn_app_update.setVisibility(View.GONE);
    		tv_app_update_number.setVisibility(View.GONE);
    		return;
    	}
    	tv_app_update_message.setText(message);
    	if(app_update_info_list.length()>=2){
    		if(app_update_info_list.length()-id>=2){
    			btn_next_app_news.setVisibility(View.VISIBLE);
    			tv_app_update_number.setVisibility(View.VISIBLE);
    			tv_app_update_number.setText(Integer.toString(app_update_info_list.length()-id-1));
    		}else{
    			btn_next_app_news.setVisibility(View.VISIBLE);
    			tv_app_update_number.setVisibility(View.GONE);
    		}
    	}else{
    		btn_next_app_news.setVisibility(View.GONE);
    		tv_app_update_number.setVisibility(View.GONE);
    		if(app_update_info_list.length()==0){
    			this.btn_app_update.setVisibility(View.GONE);
    		}
    	}
    }
    
    private void StartAppUpdate(){
    	if(current_app_update_url==null){
    		MyClass.ShowOkDlgWindow(AppActivity.this, "应用升级", "应用升级失败,请确认升级通知");
    	}else{
    		if(current_app_update_url.equals("error")==true){
    			MyClass.ShowOkDlgWindow(AppActivity.this, "应用升级", "应用升级失败,请查看升级通知内容");
    			return;
    		}
    		//显示下载地址登录框
    		if(current_app_update_url.equals("url")==true||current_app_update_url.equals("null")==true){
    			//current_app_update_url = MyVar.TEST_APK_URL;
    			MyClass.ShowOkDlgWindow(AppActivity.this, "应用升级", "应用升级失败,app的url为null或为\"url\"");
    		}
			// 如果升级的应用已经安装到一体机中并且版本和名称一一致则无需再一次下载

			MyClass.PrintInfoLog("该安装的版本已经在一体中安装过，无需安装,直接在后台注册！");

			// 开始升级了
    		int width = 720;
    		int height = width*5/8;
    		dlg = new CustomDialog(AppActivity.this);
    		dlg.SetSize(width, height);
    		dlg.show();
    		dlg.SetTitle("应用升级");
    		dlg.SetButtonStatus(dlg.btn_ok, View.INVISIBLE);
    		dlg.SetMessage("正在下载中,已完成下载 0.0 %");
    		if(th_down_apk!=null){
    			th_down_apk.SetRunFlag(false);
    			th_down_apk = null;
    		}
    		th_down_apk = new MyThread(){
				@Override
				public void run() {
					this.SetRunFlag(true);
					Download down = new Download(current_app_update_url,MyVar.APK_UPDATE_FILE,MyVar.DOWN_SPEED,MyVar.DOWN_THREAD);
					down.start();
					while(this.GetRunFlag()==true){
						if(down.down_finish==true){
							MyClass.SendMessage(MyVar.app_handler, 0x1004);
							break;
						}
						if(down.down_error!=null&&"".equals(down.down_error)==false){
							MyClass.SendMessage(MyVar.app_handler, 0x1005, down.down_error);
							break;
						}
						if(down.down_status.file_size==0){
							MyClass.MySleep(1000);
							continue;
						}
						double finish_percent = (down.down_status.finish_size*1.0)/down.down_status.file_size;
						MyClass.SendMessage(MyVar.app_handler, 0x1003,finish_percent*100);
						MyClass.MySleep(1000);
					}
					this.SetRunFlag(false);
					down.CancleDwonload();
					super.run();
				}    			
    		};
    		th_down_apk.start();
    		dlg.btn_cancel.setOnClickListener(new OnClickListener() {			
    			@Override
    			public void onClick(View arg0){
    				th_down_apk.SetRunFlag(false);
    				dlg.dismiss();
    				ShowAannounceDlg();
    			}
    		});
    	}
    }
    public void GuiInstallApk(final String apk_file){
		Uri uri = Uri.fromFile(new File(apk_file));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri,"application/vnd.android.package-archive");
		//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivityForResult(intent, 0x01);
	}
    
    public void ApkInstallFinish(){
    	//读取软件列表
		ReloadAppList();
		list_adapter.notifyDataSetChanged();
		//升级APP
		app_update_info_list = GetUpdateAppList();
		ShowNextAppUpdate();
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==0x01){
			ApkInstallFinish();
		}
		super.onActivityResult(requestCode, resultCode, data);
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
	
	private String FindPkgNameByAppName(String find_app_name){
		if(find_app_name==null){
			return null;
		}
		String ret_pkg_name = null;
        for(int i=0;i<mApps.size();i++){
	        	String app_name = mApps.get(i).activityInfo.loadLabel(getPackageManager()).toString();
	        	if(find_app_name.equals(app_name)==true){
					String pkg_name = mApps.get(i).activityInfo.packageName;
					ret_pkg_name = pkg_name;
	        		break;
	        	}
	        	
        }
        return ret_pkg_name;
	}
}
