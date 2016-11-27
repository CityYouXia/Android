package com.youxia.widget;

import com.youxia.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

@SuppressLint({ "DrawAllocation", "ClickableViewAccessibility" })
public class TouchFingerImageView extends ImageView {

    /**
     * 指纹的图片
     */
    private Bitmap fingerBitmap;
    /**
     * 图片按下的状态标识
     */
    private boolean state = false;

    /**
     * 图片数字小红点
     */
    private int number = 0;
    
    /**
     * 点击事件
     */
    private OnClickListener onClickListener;
    
    public TouchFingerImageView(Context context) {
        super(context);
        /**获取指纹图片*/
        fingerBitmap = zoom(BitmapFactory.decodeResource(getResources(), R.drawable.finger), 300, 300);

    }

    /**
     * 默认的构造函数
     *
     * @param context
     * @param attrs
     */
    public TouchFingerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**获取指纹图片*/
        fingerBitmap = zoom(BitmapFactory.decodeResource(getResources(), R.drawable.finger), 300, 300);

    }

    public void setNumber(int num)
    {
    	this.number = num;
    }
    
    /**
     * 图片的缩放方法
     *
     * @param bitmap    源图片资源
     * @param newWidth  缩放后的宽
     * @param newHeight 缩放后的高
     * @return Bitmap    缩放后的图片资源
     */
    public Bitmap zoom(Bitmap bitmap, int newWidth, int newHeight) {
        // 获取这个图片的宽和高
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        // 计算宽高缩放率
        float rateWidth = ((float) newWidth) / width;
        float rateHeight = ((float) newHeight) / height;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(rateWidth, rateHeight);
        //创建一个新的缩放后的bitmap
        Bitmap zoomBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) width,
                (int) height, matrix, true);
        return zoomBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**获取源资源图片文件**/
        Bitmap bitmap = ((BitmapDrawable) this.getDrawable()).getBitmap();
        Matrix matrix0 = new Matrix();
        /**
         * 平移指纹图片使指纹居中显示
         */
        matrix0.postTranslate(this.getWidth() / 2 - fingerBitmap.getWidth() / 2,
                this.getHeight() / 2 - fingerBitmap.getHeight() / 2);
        /**绘制源资源图片文件**/
        canvas.drawBitmap(zoom(bitmap, getWidth(), getHeight()), 0, 0, null);
        if (state) {
            Matrix matrix = new Matrix();
            /**
             * 平移指纹图片使指纹居中显示
             */
            matrix.postTranslate(this.getWidth() / 2 - fingerBitmap.getWidth() / 2,
                    this.getHeight() / 2 - fingerBitmap.getHeight() / 2);
            canvas.drawBitmap(fingerBitmap, matrix, null);
        }
        
        if (number > 0) drawNumber(canvas, number);
    }
    
    protected void drawNumber(Canvas canvas, int num)
    {
          //初始化半径
    	  int radius = 40;
    	  
	      //初始化字体大小
	      int textSize = num < 10 ? radius + 5 : radius;
	      
	      //初始化边距
	      int paddingRight = getPaddingRight();
	      int paddingTop = getPaddingTop();
	      
	      //初始化画笔
	      Paint paint = new Paint();
	      //设置抗锯齿
	      paint.setAntiAlias(true);
	      //设置颜色为红色
	      paint.setColor(0xffff4444);
	      //设置填充样式为充满
	      paint.setStyle(Paint.Style.FILL);
	      //画圆
	      canvas.drawCircle(getWidth() - radius - paddingRight/2, radius + paddingTop/2, radius, paint);
	      //设置颜色为白色
	      paint.setColor(0xffffffff);
	      //设置字体大小
	      paint.setTextSize(textSize);
	      //画数字
	      canvas.drawText("" + (num < 99 ? num : 99),
	              num < 10 ? getWidth() - radius - textSize / 4 - paddingRight/2
	                      : getWidth() - radius - textSize / 2 - paddingRight/2,
	              radius + textSize / 3 + paddingTop/2, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float begin = 1.0f;
        float end = 0.95f;
        /** 收缩动画**/
        Animation beginAnimation = new ScaleAnimation(begin, end, begin, end,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        /** 伸展动画**/
        Animation finishAnimation = new ScaleAnimation(end, begin, end, begin,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        /** 设置动画持续时间和保留动画结果 **/
        beginAnimation.setDuration(200);
        /**设置动画停留在最后一个的状态**/
        beginAnimation.setFillAfter(true);
        finishAnimation.setDuration(200);
        finishAnimation.setFillAfter(true);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://手指按下时
                startAnimation(beginAnimation);
                state = true;
                invalidate();
                if (onClickListener != null) {
                    onClickListener.onClick(this);
                }
                break;
            case MotionEvent.ACTION_UP:
                startAnimation(finishAnimation);
                state = false;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                startAnimation(finishAnimation);
                state = false;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
