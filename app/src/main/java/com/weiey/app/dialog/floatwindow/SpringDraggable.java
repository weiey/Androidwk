package com.weiey.app.dialog.floatwindow;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import com.weiey.app.dialog.floatwindow.AbsDraggable;

/**
 * 拖拽后回弹处理实现类
 */
public class SpringDraggable extends AbsDraggable {

        private boolean isTouchMove;

        private int mViewDownX;
        private int mViewDownY;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            // 获取当前触摸点在 屏幕 的位置
            int rawMoveX = (int) event.getRawX();
            int rawMoveY = (int) (event.getRawY() - getStatusBarHeight());

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isTouchMove = false;
                    // 获取当前触摸点在 View 的位置
                    mViewDownX = (int) event.getX();
                    mViewDownY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    isTouchMove = true;
                    // 更新移动的位置
                    updateViewLayout(rawMoveX - mViewDownX, rawMoveY - mViewDownY);
                    break;
                case MotionEvent.ACTION_UP:
                    // 获取屏幕的宽度
                    int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
                    // 自动回弹吸附
                    int rawFinalX;
                    if (rawMoveX < screenWidth / 2) {
                        rawFinalX = 0;
                    } else {
                        rawFinalX = screenWidth;
                    }
                    // updateViewLayout(endX - mViewDownX, mRawMoveY - mViewDownY);
                    // 从移动的点回弹到边界上
                    startAnimation(rawMoveX, rawFinalX - mViewDownX, rawMoveY - mViewDownY);
                    break;
            }
            return isTouchMove;
        }

        /**
         * 执行动画
         *
         * @param startX        X轴起点坐标
         * @param endX          X轴终点坐标
         *
         * @param y             Y轴坐标
         */
        private void startAnimation(int startX, int endX, final int y) {
            ValueAnimator animator = ValueAnimator.ofInt(startX, endX);
            animator.setDuration(500);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    updateViewLayout((Integer) animation.getAnimatedValue(), y);
                }
            });
            animator.start();
        }

}
