package custom.circle.progressbar;

import java.text.AttributedCharacterIterator.Attribute;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CustomCircleProgressBar extends View{

	private Boolean isShowText ;//是否显示文字进度
	
	private int textSize ;//文字大小
	
	private int textColor ;//文字颜色
	
	private int maxValue ;//最大进度值
	
	private int initProgress ;//初始进度值
	
	private int progress;//进度值
	
	private int roundWidth = 5;//外圈圆的边宽
	
	private int roundColor ;//圆的变框颜色
	
	
	public CustomCircleProgressBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public CustomCircleProgressBar(Context context,AttributeSet set	){
		super(context,set);
		
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(set, R.styleable.CustomCircleProgressBar,
				0, 0);
		try {
			
			isShowText = typedArray.getBoolean(R.styleable.CustomCircleProgressBar_isShowText, false);
			textColor = typedArray.getColor(R.styleable.CustomCircleProgressBar_textColor, Color.BLACK);
			textSize = typedArray.getInt(R.styleable.CustomCircleProgressBar_textSize, 30);
			maxValue = typedArray.getInt(R.styleable.CustomCircleProgressBar_maxProgress, 100);
			roundColor = typedArray.getColor(R.styleable.CustomCircleProgressBar_roundColor, Color.YELLOW);
		} catch (Exception e) {
			
		}
		typedArray.recycle();
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		
		//圆心
		int centre = getWidth()/2;
		//半径
		int radiums = centre-roundWidth/2;
		
		//用于绘制最外层圆的画笔
		Paint paint = new Paint();
		paint.setColor(roundColor);
		paint.setAntiAlias(true);//去除锯齿
		paint.setStrokeWidth(roundWidth);//设置圆环的宽度
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(centre, centre, radiums, paint);
		
		//进度百分比
		double percent = (double)progress/(double)maxValue;
		Region ovalProgressF;//用来和圆做交集的区域
		double rectHeight = 2*radiums*percent;//直径
		/*
		 * 算法的核心思想:百分比=进度条的高/圆的直径
		*/
		if (rectHeight>radiums) {
			//位于下半圆的时候,(这里计算高度和宽度没有按照面积之比计算,只是采用高和直径的比来表示所占的百分比)
			double height = rectHeight - radiums;
			ovalProgressF = new Region(centre-radiums,centre-(int)height,centre+radiums,centre+radiums);
			
		}else {
			double height = centre -rectHeight;
			ovalProgressF = new Region(centre-radiums,centre+(int)height,centre+radiums,centre+radiums);
		}
		
		//进度填充颜色
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.FILL);
		
		Path path = new Path();
		path.addCircle(centre, centre, radiums, Direction.CCW);
		
		//交集区域
		Region region = new Region();
		//canvas.drawArc(ovalProgressF, 0, 180, true, paint);
		region.setPath(path, ovalProgressF);
		drawRegion(canvas, region, paint);
		
		if (isShowText) {
			Log.d("text_custom_attribute", "maxValue:   "+maxValue);
			int per = progress*100/maxValue;
			//绘制进度百分比
			Paint textPaint = new Paint();
			textPaint.setStrokeWidth(0);
			textPaint.setColor(Color.RED);
			textPaint.setTextSize(textSize*3);//放大文字三倍显示
			textPaint.setTypeface(Typeface.DEFAULT_BOLD);
			float textWidth = textPaint.measureText(per+"%");//计算文字的宽度
			
			canvas.drawText(per+"%", centre-textWidth/2, centre+textSize/2, textPaint);
		}
		
	}
	
	//绘制交集区域  
    private void drawRegion(Canvas canvas,Region rgn,Paint paint)  
    {  
        RegionIterator iter = new RegionIterator(rgn);  
        Rect r = new Rect();  
          
        while (iter.next(r)) {  
          canvas.drawRect(r, paint);  
        }   
    }  
	
	
	//设置最大值
	public synchronized void setMaxValue(int value){
		maxValue = value;
	}

	//获取最大值
	public int getMaxVaule(){
		return maxValue;
	}
	
	//设置进度
	public synchronized void setProgress(int progress){
		
		
		if (progress<0) {
			Log.d("text_custom_attribute", "progress:   "+progress);
			throw new IllegalAccessError("progress could not less than 0");
		}
		
		if (progress>maxValue) {
			this.progress = maxValue;
		}
		
		if (progress<=maxValue) {
			//Log.d("text_custom_attribute", "progress: +++++++++++++"+progress);
			this.progress =progress;
			//postInvalidate();
			postInvalidate();
		}
	}
	
	//设置文字颜色
	public void setTextColor(int textColor){
		this.textColor = textColor;
	}
	
	//设置字体颜色
	public synchronized int getTextColor(){
		return textColor;
	}
	
	//设置圆环宽度
	public synchronized void setRoundWidth(int roundWidth){
		this.roundWidth = roundWidth;
	}
	
	
	
	
}
