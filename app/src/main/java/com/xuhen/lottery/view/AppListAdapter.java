package com.xuhen.lottery.view;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baiyilin.lottery.R;
import com.xuhen.lottery.common.GlobalApplication;
import com.xuhen.lottery.common.MyClass;
import com.xuhen.lottery.common.MyVar;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class AppListAdapter extends BaseAdapter {
	private JSONArray array = null;
	private LayoutInflater layoutInflater = null;
	

	public AppListAdapter() {
		if(MyVar.use_app_dtos==null){
			return;
		}
		array = MyVar.use_app_dtos;
		layoutInflater = (LayoutInflater)GlobalApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return this.array.length();
	}

	@Override
	public Object getItem(int arg0) {
		JSONObject json = null;
		try {
			json = this.array.getJSONObject(arg0);
		} catch (JSONException e) {
			json = new JSONObject();
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		JSONObject json = (JSONObject)this.getItem(arg0);
		View item_view = layoutInflater.inflate(R.layout.app_list_item, null);
		//去掉系统默认的item颜色
		//item_view.setBackgroundColor(0xfff4fafb);
		//手机号显示
		String app_name = "应用";
		String app_vaild_date = "2000-01-01";
		int color_vaild_date = 0xffffea76;
		if(json.has("appName")==true){
			String str_ret = MyClass.GetJsonString(json, "appName");
			if(str_ret!=null){
				 app_name = str_ret;
			}
		}
		
		if(json.has("endTime")==true){ //surplusDays
			String str_ret = MyClass.GetJsonString(json, "endTime");
			if(str_ret!=null){
				if(str_ret.length()>=10){
					Date viald_date = MyClass.StrToDate(str_ret);
					if(viald_date!=null){
						Date dt_now = new Date();
						long day_30 = System.currentTimeMillis()+30*24*3600*1000;
						dt_now.setTime(day_30);
						if(dt_now.after(viald_date)==true){
							color_vaild_date = 0xffff1b1b;
						}
					}
					str_ret = str_ret.substring(0,10);
				}
				app_vaild_date = str_ret;
			}
		}
		MyTextView tv_username = (MyTextView)item_view.findViewById(R.id.tv_username);		
		tv_username.setText(app_name);
		tv_username.setTextColor(0xffffffff);
		tv_username.InitSize(11, 0, 375, 78, 36);
		//InitSize(item_name, 30, 742, 530);
		//APP状态信息
		MyTextView btn_status = (MyTextView)item_view.findViewById(R.id.main_tv_status);
		btn_status.InitSize(375, 0, 240, 78,36);
		btn_status.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
		btn_status.setTextColor(color_vaild_date);
		btn_status.setText(app_vaild_date);
		return item_view;
	}

}

