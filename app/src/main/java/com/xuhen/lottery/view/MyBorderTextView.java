package com.xuhen.lottery.view;

import com.xuhen.lottery.common.MyVar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.AbsoluteLayout.LayoutParams;

public class MyBorderTextView extends TextView {
	private TextView borderText = null;//用于描边的TextView
	
	public MyBorderTextView(Context context) {
		super(context);
		borderText = new TextView(context);
        init();
	}

	public MyBorderTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		borderText = new TextView(context,attrs);
        init();
	}

	public MyBorderTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		borderText = new TextView(context,attrs,defStyle);
        init();
	}
	
	public void init(){
        TextPaint tp1 = borderText.getPaint(); 
        tp1.setStrokeWidth(4);                                  //设置描边宽度
        tp1.setStyle(Style.STROKE);                             //对文字只描边
        borderText.setTextColor(0xffffffff);  //设置描边颜色
        borderText.setGravity(getGravity());
    }
	
	public void SetBorderColor(int color){
		borderText.setTextColor(color);
		borderText.postInvalidate();
	}
	
	public void SetBorderWidth(int width){
		TextPaint tp1 = borderText.getPaint(); 
        tp1.setStrokeWidth(width);                                  //设置描边宽度
        tp1.setStyle(Style.STROKE);
        borderText.postInvalidate();
	}
	
	@Override
    public void setLayoutParams (ViewGroup.LayoutParams params){
        super.setLayoutParams(params);
        borderText.setLayoutParams(params);
    }
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CharSequence tt = borderText.getText();

        if(tt== null || tt.equals(this.getText())==false){
            borderText.setText(getText());
            this.postInvalidate();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderText.measure(widthMeasureSpec, heightMeasureSpec);
    }
 
    protected void onLayout (boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
        borderText.layout(left, top, right, bottom);
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        borderText.draw(canvas);
        super.onDraw(canvas);
    }
    
	@Override
	public void setTextSize(float size) {
		borderText.setTextSize(size);
		super.setTextSize(size);
	}

	@Override
	public void setTextSize(int unit, float size) {
		borderText.setTextSize(unit,size);
		super.setTextSize(unit, size);
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
	}

	@Override
	public void setGravity(int gravity) {
		if(borderText!=null){
			borderText.setGravity(gravity);
		}
		super.setGravity(gravity);
	}

	@Override
	public void setSingleLine(boolean singleLine) {
		if(borderText!=null){
			borderText.setSingleLine(singleLine);
		}
		super.setSingleLine(singleLine);
	}

	@Override
	public void setLineSpacing(float add, float mult) {
		if(borderText!=null){
			borderText.setLineSpacing(add,mult);
		}
		super.setLineSpacing(add, mult);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		if(borderText!=null){
			borderText.setText(text,type);
		}
		super.setText(text, type);
	}	
	
	
}
