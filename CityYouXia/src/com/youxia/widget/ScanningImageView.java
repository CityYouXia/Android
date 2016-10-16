package com.youxia.widget;

import com.youxia.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class ScanningImageView extends ImageView
{
  public ScanningImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public ScanningImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    if (getDrawable() == null)
      setImageDrawable((AnimationDrawable)paramContext.getResources().getDrawable(R.anim.img_scanning));
  }

  public void startAnimation()
  {
    if ((getWidth() > 0) && (getVisibility() == 0))
    {
      Drawable localDrawable = getDrawable();
      if ((localDrawable != null) && ((localDrawable instanceof AnimationDrawable)) && (!((AnimationDrawable)localDrawable).isRunning()))
        ((AnimationDrawable)localDrawable).start();
    }
  }

  public void stopAnimation()
  {
    Drawable localDrawable = getDrawable();
    if ((localDrawable != null) && ((localDrawable instanceof AnimationDrawable)) && (((AnimationDrawable)localDrawable).isRunning()))
      ((AnimationDrawable)localDrawable).stop();
  }

  @Override
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    startAnimation();
  }

  @Override
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    stopAnimation();
  }

  @Override
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 > 0)
    	startAnimation();
  }

  @Override
  protected void onVisibilityChanged(View paramView, int paramInt)
  {
    super.onVisibilityChanged(paramView, paramInt);
    if ((paramInt == 4) || (paramInt == 8))
    	stopAnimation();
    startAnimation();
  }
}