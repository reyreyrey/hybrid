package com.ivi.hybrid.gesture.views;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hybrid.R;

/**
 * Created by Rea.X on 2017/1/25.
 * 显示选择的结果的view
 */

public class ShowGestureView extends View{
    private Bitmap choiceDrawable, unChoiceDrawable;
    private int viewWidth;
    private int space;
    private Paint paint;
    private int drawableWidth;
    private int [] passwordInts;
    public ShowGestureView(Context context) {
        super(context);
    }

    public ShowGestureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowGestureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ShowGestureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(){
        drawableWidth = (int)getResources().getDimension(R.dimen.draw_size);
        space = (int)getResources().getDimension(R.dimen.space);
        viewWidth = drawableWidth * 3 + space * 4;
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void setImage(@DrawableRes int normal, @DrawableRes int select){
        choiceDrawable = BitmapFactory.decodeResource(getResources(), select);
        unChoiceDrawable = BitmapFactory.decodeResource(getResources(), normal);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(viewWidth != 0){
            setMeasuredDimension(viewWidth, viewWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int pos = 0;
        for(int i=0; i< 3; i++){
            for(int j=0; j<3;j++){
                pos ++;
                Rect rect = new Rect();
                rect.left = space * (j + 1) + drawableWidth * j;
                rect.top = space * (i + 1) + drawableWidth * i;
                rect.right = rect.left + (int)getResources().getDimension(R.dimen.draw_size);
                rect.bottom = rect.top + (int)getResources().getDimension(R.dimen.draw_size);
                boolean isSelect = checkIsSelect(pos);
                canvas.drawBitmap(isSelect ? choiceDrawable : unChoiceDrawable, null, rect, paint);
            }
        }
    }

    private boolean checkIsSelect(int num) {
        if(passwordInts != null && passwordInts.length != 0){
            for(int a : passwordInts){
                if(num == a)return true;
            }
        }
        return false;
    }

    /**
     * 设置解锁密码
     * @param s
     */
    public void setPassword(String s){
        if(!TextUtils.isEmpty(s)){
            char [] cs = s.toCharArray();
            passwordInts = new int [cs.length];
            for(int i = 0;i<cs.length;i++){
                char c = cs [i];
                String str =  Character.toString(c);
                int a = Integer.parseInt(str);
                passwordInts [i] = a;
            }
        }
        invalidate();
    }
}
