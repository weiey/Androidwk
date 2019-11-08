package com.weiey.app.dialog.floatwindow;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import com.weiey.app.dialog.floatwindow.AbsDraggable;

/**
 *移动拖拽处理实现类
 */
public class MovingDraggable  extends AbsDraggable {

    private boolean isTouchMove;

    private int mViewDownX;
    private int mViewDownY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouchMove = false;
                mViewDownX = (int) event.getX();
                mViewDownY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                isTouchMove = true;
                int moveX = (int) event.getRawX();
                int moveY = (int) (event.getRawY() - getStatusBarHeight());
                updateViewLayout(moveX - mViewDownX, moveY - mViewDownY);
                break;
        }
        return isTouchMove;
    }
}
