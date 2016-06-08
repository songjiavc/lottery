package com.xuhen.lottery.view;

import com.xuhen.lottery.common.MyClass;
import com.xuhen.lottery.common.MyVar;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.AbsoluteLayout.LayoutParams;

@SuppressWarnings("deprecation")
public class MyTextView extends TextView {
	private boolean is_font_size_change = false;
	private float old_font_size = 20;
	
	public MyTextView(Context context) {
		super(context);
		Init();
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Init();
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Init();
	}
	
	private void Init(){
		//this.setFocusable(true);
		//this.setFocusableInTouchMode(true);
		this.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				MyTextView tv_arg0 = (MyTextView)arg0;
				if(arg1==true){
					if(is_font_size_change==false){
						tv_arg0.setTextSize(old_font_size*1.1f);
						tv_arg0.setBackgroundColor(0x666ad7d7);
						is_font_size_change = true;
					}					
				}else{
					if(is_font_size_change==true){
						tv_arg0.setTextSize(old_font_size);
						tv_arg0.setBackgroundColor(0x00ffffff);
						is_font_size_change = false;
					}
				}				
			}
		});
	}

	public void InitSize(int view_x,int view_y,int view_width,int view_height,float size){
		int screen_width = MyVar.GetScreenWidth();
		int screen_height = MyVar.GetScreenHeight();
		int view_new_x = view_x*screen_width/MyVar.GetDesignWidth();
		int view_new_y = view_y*screen_height/MyVar.GetDesignHeight();
		float new_size = size*screen_width/MyVar.GetDesignWidth()/MyVar.GetScaledDensity();//依据缩放等级显示字体大小
		int view_new_width = view_width*screen_width/MyVar.GetDesignWidth();
		int view_new_height = view_height*screen_height/MyVar.GetDesignHeight();
		//MyClass.PrintInfoLog("MyTextView","x="+view_new_x+",y="+view_new_y+",width="+view_new_width+",height="+view_new_height);
		LayoutParams layout = new LayoutParams(view_new_width,view_new_height,view_new_x,view_new_y);
		this.setLayoutParams(layout);
		this.setTextSize(new_size);
		this.old_font_size = new_size;
		//
		String str_text = this.getText().toString();
		int pos = -1;
		if(str_text!=null){
			pos = str_text.indexOf("(");
		}
		if(pos!=-1){
			int pos_end = str_text.indexOf(")");
			if(pos_end!=-1){
				String lb_name = str_text.substring(0,pos);
				String lb_unit = str_text.substring(pos,pos_end);
				SpannableString style_text = new SpannableString(str_text);  
				style_text.setSpan(new AbsoluteSizeSpan((int)MyClass.GetTextSize(30)), 0, pos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
				style_text.setSpan(new AbsoluteSizeSpan((int)MyClass.GetTextSize(20)), pos, pos_end+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);   
				this.setText(style_text, TextView.BufferType.SPANNABLE); 
			}
		}
	}
	
}
