package com.xuhen.lottery.view;

import com.xuhen.lottery.common.MyVar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsoluteLayout;
import android.widget.ListView;


public class MyListView extends ListView {

	public MyListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void InitSize(int view_x,int view_y,int view_width,int view_height){
		int screen_width = MyVar.GetScreenWidth();
		int screen_height = MyVar.GetScreenHeight();
		int view_new_x = view_x*screen_width/MyVar.GetDesignWidth();
		int view_new_y = view_y*screen_height/MyVar.GetDesignHeight();
		int view_new_width = view_width*screen_width/MyVar.GetDesignWidth();
		int view_new_height = view_height*screen_height/MyVar.GetDesignHeight();
		if(MyVar.IS_16_9_PROGRAM==true){
		//	view_new_height = view_new_height*10/9;
		} 
		AbsoluteLayout.LayoutParams layout = new AbsoluteLayout.LayoutParams(view_new_width,view_new_height,view_new_x,view_new_y);
		this.setLayoutParams(layout);
	}

}

