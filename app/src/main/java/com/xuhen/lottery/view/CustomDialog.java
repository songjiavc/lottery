package com.xuhen.lottery.view;

import java.util.ArrayList;

import com.baiyilin.lottery.R;
import com.xuhen.lottery.common.MyClass;
import com.xuhen.lottery.common.MyVar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

 
public class CustomDialog extends Dialog {
	//变量
	private Context context;
	private String title;//标题
	private String message;//信息
	private String positiveButtonText;//确定按钮内容
	private String negativeButtonText;//取消按钮内容
	private int view_width = 686;
	private int view_height = 461;
	private int title_height = 75;//84
	private int title_size = 35;
	private float message_size = 25;
	
	public MyImageButton btn_ok = null;
	public MyImageButton btn_cancel = null;
	public MyBorderTextView tv_title = null;
	public MyTextView tv_message = null;
	//按钮宽度高度
	public int button_width = 217;
	public int button_height = 107;
	public int button_buttom_padding = 15;//按钮离底部距离
	//自定义布局
	private AbsoluteLayout view = null;
	public ArrayList<View> list_view = new ArrayList<View>();
	
	public CustomDialog(Context context) {
		super(context,R.style.Dialog);
		this.context = context;
		Init();
	}
	
	public CustomDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;	
		Init();
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		int screen_width = MyVar.GetScreenWidth();
		int screen_height = MyVar.GetScreenHeight();
		int new_view_width = view_width*screen_width/MyVar.GetDesignWidth();
		int new_view_height = view_height*screen_height/MyVar.GetDesignHeight();
		//对话框窗体布局
		@SuppressWarnings("deprecation")
		LayoutParams layout = new LayoutParams(new_view_width,new_view_height,(screen_width-new_view_width)/2,(screen_height-new_view_height)/2);
		//对话框加载自定义布局
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Activity activity_base = (Activity)context;
        View view1=inflater.inflate(R.layout.dialog_normal_layout, null);
        view = (AbsoluteLayout)view1;
        //对话框父对像,主要控制对话框居中,父对像全屏
		@SuppressWarnings("deprecation")
		AbsoluteLayout bg_view = new AbsoluteLayout(activity_base);
		//对话框背景布局
		@SuppressWarnings("deprecation")
		LayoutParams bg_layout = new LayoutParams(screen_width,screen_height,0,0);
		bg_view.addView(view, layout);
		this.setContentView(bg_view,bg_layout);
		//对话框中对像
		this.btn_ok = (MyImageButton)view.findViewById(R.id.custom_dialog_btn_ok);
        this.btn_cancel = (MyImageButton)view.findViewById(R.id.custom_dialog_btn_cancel);
        this.tv_title = (MyBorderTextView)view.findViewById(R.id.custom_dialog_tv_title);
        this.tv_message = (MyTextView)view.findViewById(R.id.custom_dialog_tv_message);
        this.btn_ok.SetImages(R.drawable.custom_dlalog_btn_ok_up, R.drawable.custom_dlalog_btn_ok_down);
        this.btn_ok.InitSize(view_width/2-button_width-30, view_height-button_buttom_padding-button_height, button_width, button_height);
        this.btn_cancel.SetImages(R.drawable.custom_dlalog_btn_cancel_up, R.drawable.custom_dlalog_btn_cancel_down);
        this.btn_cancel.InitSize(view_width/2+30, view_height-button_buttom_padding-button_height, button_width, button_height);
        this.tv_title.InitSize(0, 0, view_width, title_height, title_size);        
        this.tv_title.SetBorderColor(0xff154f9a);
        this.tv_title.SetBorderWidth(4);
        this.tv_message.InitSize(0, title_height, view_width, view_height-title_height-button_buttom_padding-button_height, message_size);
        this.tv_message.setBackgroundColor(0xffffff);
        this.tv_message.setTextColor(0xff0059cf);
        this.btn_ok.requestFocus();
        this.tv_message.setFocusable(false);
        //整个窗体布局,对话框对像是全屏的,并不是只有屏幕中间部分
        WindowManager.LayoutParams window_layout = this.getWindow().getAttributes();
        window_layout.dimAmount = 0.0f; // 去背景遮盖
        window_layout.height=screen_height;
        window_layout.width=screen_width;
        this.getWindow().setAttributes(window_layout); 
	}
	
	public void AddView(View my_view){
		this.SetMessage("");
		this.view.addView(my_view);
		list_view.add(my_view);
	}
	
	public void RemoveAddView(){
		for(int i=0;i<list_view.size();i++){
			View view = list_view.get(i);
			this.view.removeView(view);
		}
		list_view.clear();
	}
	
	public void SetTitle(String str_title){
		this.tv_title.setText(str_title);
	}
	
	public void SetTitleVisibility(Boolean b){
		if(this.tv_title==null){
			return;
		}
		if(b==true){
			this.tv_title.setVisibility(View.VISIBLE);
		}else{
			this.tv_title.setVisibility(View.INVISIBLE);
		}
		ReSize();
	}
	
	public void SetMessagePos(int x,int y){
		if(x<0||y<0){
			return;
		}
		int screen_width = MyVar.GetScreenWidth();
		int screen_height = MyVar.GetScreenHeight();
		int new_x = x*screen_width/MyVar.GetDesignWidth();
		int new_y = y*screen_height/MyVar.GetDesignHeight();
		this.tv_message.setGravity(Gravity.TOP);
		this.tv_message.setPadding(new_x, new_y, 0, 0);
	}
	
	public void SetMessage(String str_message){
		this.tv_message.setText(str_message);
		this.tv_message.setGravity(Gravity.CENTER);
	}
	public void SetMessageSize(float font_size){
		message_size = font_size;		
		this.tv_message.setTextSize(font_size*MyVar.GetScreenWidth()/MyVar.GetDesignWidth()/MyVar.GetScaledDensity());
	}
	public void SetMessageGravity(int layout){
		this.tv_message.setGravity(layout);
	}
	public void SetMessageBackground(int res_id){
		this.tv_message.setBackgroundResource(res_id);
	}
	public void SetMessageBackgroundColor(int color){//主要用于背景被设置了图片,重置用
		//this.tv_message.setBackgroundColor(color);
		this.tv_message.setBackgroundResource(R.drawable.dialog_message_round_shape_bg);
	}
	public void SetSize(int width,int height){
		this.view_width = width;
		this.view_height = height;
	}
	public void SetButtonPos(MyImageButton btn,int x,int y){
		if(this.btn_ok.equals(btn)==false&&this.btn_cancel.equals(btn)==false){
			return;
		}
		if(x<0||y<0){
			return;
		}
		btn.InitSize(x, y, button_width, button_height);
	}
	public void SetButtonStatus(MyImageButton btn,int visible){
		if(this.btn_ok.equals(btn)==false&&this.btn_cancel.equals(btn)==false){
			return;
		}
		if(visible!=View.INVISIBLE&&visible!=View.VISIBLE){
			return;
		}
		if(this.btn_ok.equals(btn)==true){
			btn_ok.setVisibility(visible);
			btn_ok.postInvalidate();
		}
		if(this.btn_cancel.equals(btn)==true){
			this.btn_cancel.setVisibility(visible);
			btn_cancel.postInvalidate();
		}
		ReSize();
	}
	
	public void SetTitle(String str_title,int height,int size){
		this.tv_message.setText(str_title);
		this.title_height = height;
		this.title_size = size;
	}
	
	public void SetTitleColor(int bgcolor, int color){
		this.tv_title.setBackgroundColor(bgcolor);
		this.tv_title.setTextColor(color);
	}
	
	public void ReSize(){
		int new_title_height = 0;//获取标题占用高度
		if(this.tv_title.getVisibility()==View.VISIBLE){
			new_title_height = title_height;
			this.tv_title.InitSize(0, 0, view_width, new_title_height, title_size);
		}
		//遍历现在有几个按钮显示
		if(this.btn_ok.getVisibility()==View.VISIBLE&&this.btn_cancel.getVisibility()==View.VISIBLE){
			this.btn_ok.InitSize(view_width/2-button_width-30, view_height-button_buttom_padding-button_height, button_width, button_height);
	        this.btn_cancel.InitSize(view_width/2+30, view_height-button_buttom_padding-button_height, button_width, button_height);
	        this.tv_message.InitSize(0, new_title_height, view_width, view_height-new_title_height-90, message_size);
			return;
		}
		if(this.btn_ok.getVisibility()==View.INVISIBLE&&this.btn_cancel.getVisibility()==View.INVISIBLE){
	        this.tv_message.InitSize(0, new_title_height, view_width, view_height-new_title_height, message_size);
			return;
		}
		if(this.btn_ok.getVisibility()==View.VISIBLE){
			this.btn_ok.InitSize(view_width/2-button_width/2, view_height-button_buttom_padding-button_height, button_width, button_height);
		}
		if(this.btn_cancel.getVisibility()==View.VISIBLE){
			this.btn_cancel.InitSize(view_width/2-button_width/2, view_height-button_buttom_padding-button_height, button_width, button_height);
		}
        this.tv_message.InitSize(0, new_title_height, view_width, view_height-new_title_height-button_height-button_buttom_padding, message_size);		
        this.btn_ok.requestFocus();
        this.tv_message.setFocusable(false);
	}
	private void Init(){
		setCanceledOnTouchOutside(false);
		list_view.clear();
	}
	
	@Override
	public void show() {//显示时，如果当前前页面已关闭
		try{
			super.show();
		}catch(Exception e){
			e.printStackTrace();
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
	
}
