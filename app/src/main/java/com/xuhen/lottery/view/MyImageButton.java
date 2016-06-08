package com.xuhen.lottery.view;

import com.xuhen.lottery.common.MyClass;
import com.xuhen.lottery.common.MyVar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ImageButton;

@SuppressWarnings("deprecation")
public class MyImageButton extends ImageButton {
	private int res_up = 0;
	private int res_down = 0;
	private int res_disable = 0;
	private boolean button_enable = true;
	public MyImageButton(Context context) {
		super(context);
		Init();
	}

	public MyImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		Init();
	}
	
	public MyImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Init();
	}
	
	public void Init(){
		this.setFocusable(true);
	}
	//初始化控件大小,位置
	public void InitSize(int view_x,int view_y, int view_width, int view_height){
		if(this.res_up==0){
			int i = 1/0;//防止调试时，没有设置按钮图片
		}
		int screen_width = MyVar.GetScreenWidth();
		int screen_height = MyVar.GetScreenHeight();
		int view_new_x = view_x*screen_width/MyVar.GetDesignWidth();
		int view_new_y = view_y*screen_height/MyVar.GetDesignHeight();
		int view_new_width = view_width*screen_width/MyVar.GetDesignWidth();
		int view_new_height = view_height*screen_height/MyVar.GetDesignHeight();
		//if(MyVar.IS_16_9_PROGRAM==false){
		//	view_new_width = view_new_width*10/9;
		//}
		//Log.i("MyImageButton", "x="+view_new_x+",y="+view_new_y+",width="+view_new_width+",height="+view_new_height);
		//对1366x768使用自己图片大小 最后再开启
		/*if(screen_width ==1366&&screen_height==768){
			Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), this.res_up);
			view_new_height = bitmap.getHeight();
			view_new_width= bitmap.getWidth();
		}*/
		LayoutParams layout = new LayoutParams(view_new_width,view_new_height,view_new_x,view_new_y);
		this.setLayoutParams(layout);
	}
	//设置控件切换图片
	public void SetImages(int res_id_up,int res_id_down,int res_id_disable){
		this.res_up = res_id_up;
		this.res_down = res_id_down;
		this.res_disable = res_id_disable;
		this.setBackgroundResource(res_up);
		this.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(button_enable == false){
					return false;
				}
				if(arg1.getAction()==MotionEvent.ACTION_DOWN){
					if(MyVar.IsFastClick(arg0)==true){
						MyClass.PrintInfoLog("MyImageButton","click too fast!");
						return true;
					}
					arg0.setBackgroundResource(res_down);
				}else if(arg1.getAction()==MotionEvent.ACTION_UP){
					arg0.setBackgroundResource(res_up);
				}
				return false;
			}			
		});
	}
	//设置控件切换图片
	public void SetImages(int res_id_up){
		this.res_up = res_id_up;
		this.res_down = 0;
		this.res_disable = 0;
		this.setImageResource(this.res_up);
		this.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(arg1.getAction()==MotionEvent.ACTION_DOWN){
					if(MyVar.IsFastClick(arg0)==true){//防止双击
						MyClass.PrintInfoLog("MyImageButton","click too fast!");
						return true;
					}
				}
				return false;
			}			
		});
	}
	//设置控件切换图片
	public void SetImages(int res_id_up,int res_id_down){
		this.res_up = res_id_up;
		this.res_down = res_id_down;
		this.res_disable = 0;
		this.setBackgroundResource(res_up);
		this.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(arg1.getAction()==MotionEvent.ACTION_DOWN){
					if(MyVar.IsFastClick(arg0)==true){
						MyClass.PrintInfoLog("MyImageButton","click too fast!");
						return true;
					}
					if(res_down==0){
						arg0.setBackgroundResource(res_up);
					}else{
						arg0.setBackgroundResource(res_down);
					}					
				}else if(arg1.getAction()==MotionEvent.ACTION_UP){
					arg0.setBackgroundResource(res_up);
				}
				return false;
			}			
		});
		this.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1==true){
					if(res_down==0){
						arg0.setBackgroundResource(res_up);
					}else{
						arg0.setBackgroundResource(res_down);
					}					
				}else if(arg1==false){
					arg0.setBackgroundResource(res_up);
				}			
			}
		});
	}
	//设置控间不可操作
	public void SetDisable(){
		SetDisable(true);
	}
	public void SetEnable(){
		if(res_up!=0){
			this.setBackgroundResource(res_up);
		}
		this.setEnabled(true);
		this.button_enable = true;
	}
	//设置控间不可用,但可以单击
	public void SetDisable(boolean b){
		if(res_disable!=0){
			this.setBackgroundResource(res_disable);
		}
		if(b==true){
			this.setEnabled(false);
		}else{
			this.button_enable = false;
		}		
	}
	//获取控件是否隐藏状态
	public boolean GetDisableStatus(){
		if(this.button_enable == false){
			return true;
		}else{
			return false;
		}
	}
}
