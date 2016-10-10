package com.xuhen.lottery.view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.baiyilin.lottery.R;
import com.xuhen.lottery.common.GlobalApplication;
import com.xuhen.lottery.common.MyClass;
import com.xuhen.lottery.common.MyVar;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.AbsoluteLayout.LayoutParams;
import android.view.inputmethod.InputMethodManager;

public class MyEditText extends EditText {
	private Context context  = null;
	private boolean is_remove_first_focus = false;
	private Drawable btn_del_all = null;
	private boolean btn_del_all_visable = false;
	public MyEditText(Context context) {
		super(context);
		this.context = context;
		Init();
	}
	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		Init();
	}

	public MyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		Init();
	}
	
	public void ShowErrorStyle(){
		this.setBackgroundResource(R.drawable.edit_text_red_select_bg);
		//HideSoftInputMethod(this);
		this.setTextColor(0xffc2d7f4);
	}
	
	private void Init(){
		//创建一个全删按钮
		this.btn_del_all = getCompoundDrawables()[2];
		if(this.btn_del_all==null){
			this.btn_del_all = getResources().getDrawable(R.drawable.et_del_all);
		}
		this.btn_del_all.setBounds(0, 0, btn_del_all.getIntrinsicWidth(), btn_del_all.getIntrinsicHeight());
		SetDelAllVisible(false);
		this.setLongClickable(false);
		//只显示一行
		this.setSingleLine(true);
		this.setBackgroundResource(R.drawable.edit_text_bg);
		this.setTextColor(0xffc2d7f4);
		this.setHintTextColor(0xa0b0c6e8);
		//HideSoftInputMethod(this);
		/*this.setOnKeyListener(new OnKeyListener() {			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				MyEditText et =(MyEditText)arg0;
				HideSoftInputMethod(et);
				et.setBackgroundResource(com.xuhen.lottery.R.drawable.edit_text_select_bg);
				et.setTextColor(0xffffffff);//选中后的颜色
				return false;
			}
		});*/
	}	
	
	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
		if(getText().toString().trim().length()>=1){
			SetDelAllVisible(true);
		}else{
			SetDelAllVisible(false);
		}
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}
	
	@Override
	protected void onFocusChanged(boolean focused, int direction,	Rect previouslyFocusedRect) {
		if(focused==true){				
			this.setBackgroundResource(R.drawable.edit_text_select_bg);
			//HideSoftInputMethod(this);
			this.setTextColor(0xffffffff);//选中后的颜色
			//判断是否有文字，决定是否全删按钮隐藏
			if(getText().toString().trim().length()>=1){
				SetDelAllVisible(true);
			}else{
				SetDelAllVisible(false);
			}
		}else{
			this.setBackgroundResource(R.drawable.edit_text_bg);
			//HideSoftInputMethod(this);
			this.setTextColor(0xffc2d7f4);//未选中的颜色
			//全删按钮隐藏
			SetDelAllVisible(false);
		}
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(this.btn_del_all!=null&&btn_del_all_visable==true&&event.getAction()==MotionEvent.ACTION_UP){			
			if(event.getX()>this.getWidth()-this.btn_del_all.getBounds().width()-20){
				this.setText("");
				event.setAction(MotionEvent.ACTION_CANCEL);
			}
		}
		return super.onTouchEvent(event);
	}
	
	//初始化控件大小,位置
	public void InitSize(final int view_x,final int view_y, int view_width, final int view_height,float size){
		int screen_width = MyVar.GetScreenWidth();
		int screen_height = MyVar.GetScreenHeight();
		int view_new_x = view_x*screen_width/MyVar.GetDesignWidth();
		int view_new_y = view_y*screen_height/MyVar.GetDesignHeight();
		int view_new_width = view_width*screen_width/MyVar.GetDesignWidth();
		int view_new_height = view_height*screen_height/MyVar.GetDesignHeight();
//		float new_size = size*screen_width/MyVar.GetDesignHeight();
		float new_size = size*screen_width/MyVar.GetDesignWidth()/MyVar.GetScaledDensity();//依据缩放等级显示字体大小
		//Log.i("MyEditText", "x="+view_new_x+",y="+view_new_y+",width="+view_new_width+",height="+view_new_height);
		LayoutParams layout = new LayoutParams(view_new_width,view_new_height,view_new_x,view_new_y);
		this.setLayoutParams(layout);
		this.setTextSize(new_size);
	}
	
    // 隐藏系统键盘
	public void HideSoftInputMethod(EditText ed){
		//(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		String methodName = null;
		if(currentVersion >= 16){
			// 4.2
			methodName = "setShowSoftInputOnFocus";
		}
		else if(currentVersion >= 14){
			// 4.0
			methodName = "setSoftInputShownOnFocus";
		}
		
		if(methodName == null){
			ed.setInputType(InputType.TYPE_NULL);  
		}
		else{
	        Class<EditText> cls = EditText.class;  
	        Method setShowSoftInputOnFocus;  
	        try {
				setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);  
	            setShowSoftInputOnFocus.invoke(ed, false); 
			} catch (NoSuchMethodException e) {
				ed.setInputType(InputType.TYPE_NULL);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}  
	}
	public void RemoveFirstFocus() {
		this.is_remove_first_focus = true;		
	}
	
	private void SetDelAllVisible(boolean b){
		btn_del_all_visable = b;
		//
		//Drawable right = b ? this.btn_del_all:null;
		//this.setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}
}
