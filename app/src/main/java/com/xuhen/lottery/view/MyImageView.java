package com.xuhen.lottery.view;

import com.xuhen.lottery.common.MyVar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.AbsoluteLayout.LayoutParams;

public class MyImageView extends ImageView {
	private int res = 0;

	public MyImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	//初始化控件大小,位置
	public void InitSize(int view_x,int view_y, int view_width, int view_height){
		if(this.res==0){
			int i = 1/0;//防止调试时，没有设置按钮图片
		}
		int screen_width = MyVar.GetScreenWidth();
		int screen_height = MyVar.GetScreenHeight();
		int view_new_x = view_x*screen_width/MyVar.GetDesignWidth();
		int view_new_y = view_y*screen_height/MyVar.GetDesignHeight();
		int view_new_width = view_width*screen_width/MyVar.GetDesignWidth();
		int view_new_height = view_height*screen_height/MyVar.GetDesignHeight();
		if(MyVar.IS_16_9_PROGRAM==true){
			//view_new_height = view_new_height*10/9;
		}
		//Log.i("MyImageView", "x="+view_new_x+",y="+view_new_y+",width="+view_new_width+",height="+view_new_height);
		//对1366x768使用自己图片大小
		/*if(screen_width ==1366&&screen_height==768){
			Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), this.res_up);
			view_new_height = bitmap.getHeight();
			view_new_width= bitmap.getWidth();
		}*/
		LayoutParams layout = new LayoutParams(view_new_width,view_new_height,view_new_x,view_new_y);
		this.setLayoutParams(layout);
	}
	//设置控件切换图片
	public void SetImages(int res_id){
		this.res = res_id;
		this.setBackgroundResource(this.res);
	}
}
