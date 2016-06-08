package com.xuhen.lottery.view;

import com.xuhen.lottery.common.MyClass;
import com.xuhen.lottery.common.MyVar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;

public class MyTextImageButton extends AbsoluteLayout {
	private int res_up = 0;
	private int res_down = 0;
	private int res_disable = 0;
	private MyImageView icon  = null;
	private MyBorderTextView text = null;
	private boolean normal_layout = true;
	private float text_size = 25;
	private int text_color = 0xffffffff;
	public boolean button_enable = true;
	private boolean press_lock = false;
	//闪动效果
	private AnimationDrawable gif_light = null;
	
	public MyTextImageButton(Context context) {
		super(context);
		Init(context);
	}

	public MyTextImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		Init(context);
	}

	public MyTextImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Init(context);
	}
	
	private void Init(Context context){
		this.icon = new MyImageView(context);
		this.icon.setBackgroundDrawable(this.getBackground());
		this.text = new MyBorderTextView(context);
		this.text.setWidth(4);
		this.text.SetBorderColor(0xff154f9a);
		this.text.setSingleLine(true);
		setClickable(true);        
        setFocusable(true);
		this.addView(icon);
		this.addView(text);
	}
	
	
	
	public void SetTextColor(int color){
		this.text_color = color;
		this.text.setTextColor(color);
	}
	
	public void SetTextBorderColor(int color){
		this.text.SetBorderColor(color);
	}
	public void SetText(String font_text){
		this.text.setText(font_text);
	}
	
	public void SetHorizontalLayout(boolean b){
		normal_layout = b;
	}
	
	//初始化控件大小,位置
	public void InitSize(int view_x,int view_y, int view_width, int view_height,float size){
		this.text_size = size;
		if(this.res_up==0){
			int i = 1/0;//防止调试时，没有设置按钮图片
		}
		int screen_width = MyVar.GetScreenWidth();
		int screen_height = MyVar.GetScreenHeight();
		int view_new_x = view_x*screen_width/MyVar.GetDesignWidth();
		int view_new_y = view_y*screen_height/MyVar.GetDesignHeight();
		int view_new_width = view_width*screen_width/MyVar.GetDesignWidth();
		int view_new_height = view_height*screen_height/MyVar.GetDesignHeight();
		if(MyVar.IS_16_9_PROGRAM==true){
			view_new_height = view_new_height*10/9;
		}
		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), this.res_up);
		int icon_height = bitmap.getHeight();
		int icon_width= bitmap.getWidth();
		if(normal_layout==true){
			int icon_new_width = icon_width*view_height/icon_height;
			if(MyVar.IS_16_9_PROGRAM==true){//imageview不支持10:9 10:10相互转换
				this.icon.InitSize(0, 0, icon_new_width, view_height*10/9);
				this.text.setGravity(Gravity.CENTER_VERTICAL);
				this.text.InitSize(icon_new_width, 0, view_width-icon_new_width, view_height*10/9, size);
			}else{
				this.icon.InitSize(0, 0, icon_new_width, view_height);
				this.text.setGravity(Gravity.CENTER_VERTICAL);
				this.text.InitSize(icon_new_width, 0, view_width-icon_new_width, view_height, size);
			}			
		}else{
			int text_hgiht = Math.round(size);
			int icon_dest_height =view_height - text_hgiht;
			int icon_dest_width = icon_width*icon_dest_height/icon_height;
			text_hgiht = view_height-icon_dest_height+text_hgiht/4;
			if(MyVar.IS_16_9_PROGRAM==true){//imageview不支持10:9 10:10相互转换
				this.icon.InitSize((view_width-icon_dest_width)/2, 0, icon_dest_width, icon_dest_height*10/9);
				this.text.setGravity(Gravity.CENTER_HORIZONTAL);
				this.text.InitSize(0, icon_dest_height-text_hgiht/4, view_width, text_hgiht*10/9, size);
			}else{
				this.icon.InitSize((view_width-icon_dest_width)/2, 0, icon_dest_width, icon_dest_height);
				this.text.setGravity(Gravity.CENTER_HORIZONTAL);
				this.text.InitSize(0, icon_dest_height-text_hgiht/4, view_width, text_hgiht, size);
			}
		}
		LayoutParams layout = new LayoutParams(view_new_width,view_new_height,view_new_x,view_new_y);
		this.setLayoutParams(layout);
	}
	
	//设置控件切换图片
	public void SetImages(int res_id_up,int res_id_down){
		this.res_up = res_id_up;
		this.res_down = res_id_down;
		SetTouchListener();
	}
	//设置控件切换图片
	public void SetImages(int res_id_up,int res_id_down,int res_id_disable){
		this.res_up = res_id_up;
		this.res_down = res_id_down;
		this.res_disable = res_id_disable;
		SetTouchListener();
	}
	
	//设置控件切换图片
	public void SetImages(int res_id_up){
		this.res_up = res_id_up;
		this.res_down = res_id_up;
		SetTouchListener();
	}
	
	public void SetTouchListener(){
		icon.SetImages(res_up);		
		icon.setBackgroundResource(res_up);
		this.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(button_enable==false){
					return false;
				}
				if(arg1.getAction()==MotionEvent.ACTION_DOWN){
					if(MyVar.IsFastClick(icon)==true){
						MyClass.PrintInfoLog("MyImageButton","click too fast!");
						return true;
					}
					if(gif_light!=null){
						gif_light.stop();
						gif_light = null;
					}
					//停止掉闪动
					icon.setBackgroundResource(res_down);
					if(press_lock==true){
						return false;
					}
					press_lock = true;
					text.setWidth(6);
					text.setTextSize(MyClass.GetTextSize(text_size*9/8));
					LayoutParams layout = (LayoutParams)text.getLayoutParams();
					layout.x -= Math.round(MyClass.GetTextSize(text_size/8)/2);
					if(normal_layout==false){
						layout.y -= Math.round(MyClass.GetTextSize(text_size/8)/2);
					}
					text.setLayoutParams(layout);
				}else if(arg1.getAction()==MotionEvent.ACTION_UP){					
					icon.setBackgroundResource(res_up);
					if(press_lock==false){
						return false;
					}
					//停止掉闪动
					if(gif_light!=null){
						gif_light.stop();
						gif_light = null;
					}
					press_lock = false;
					text.setWidth(4);
					text.setTextSize(MyClass.GetTextSize(text_size));
					LayoutParams layout = (LayoutParams)text.getLayoutParams();
					layout.x += Math.round(MyClass.GetTextSize(text_size/8)/2);
					if(normal_layout==false){
						layout.y += Math.round(MyClass.GetTextSize(text_size/8)/2);
					}
					text.setLayoutParams(layout);
				}
				return false;
			}			
		});
	}
	//设置控间不可操作
	public void SetDisable(){
		if(res_disable!=0){
			this.icon.setBackgroundResource(res_disable);
		}
		this.text.setTextColor(0xff6d6d6d);
		button_enable = false;
	}
	public void SetDisable(int color){
		if(res_disable!=0){
			this.icon.setBackgroundResource(res_disable);
		}
		this.text.setTextColor(color);
		button_enable = false;
	}
	public void SetEnable(){
		if(res_up!=0){
			this.icon.setBackgroundResource(res_up);
		}
		this.text.setTextColor(text_color);
		button_enable = true;
	}
	
	public void StartLight(int res_id_light){
		this.icon.setBackgroundResource(res_id_light);
		gif_light = (AnimationDrawable)this.icon.getBackground();
		if(gif_light!=null){
			gif_light.start();
		}
	}
}
