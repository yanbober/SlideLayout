/**
 * MIT License
 *
 * Copyright (c) 2016 yanbo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cn.yan.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * like SlidingPaneLayout, all direction support.
 */
public class SlideLayout extends ViewGroup {
    public static final int STATE_CLOSE = 0;
    public static final int STATE_SLIDING = 1;
    public static final int STATE_OPEN = 2;

    private static final int SLIDE_RIGHT = 0;
    private static final int SLIDE_LEFT = 1;
    private static final int SLIDE_TOP = 2;
    private static final int SLIDE_BOTTOM = 3;

    private View mContentView;
    private View mSlideView;

    private Scroller mScroller;

    private int mLastX = 0;
    private int mLastY = 0;

    private int mSlideSensitiveWidth = 0;
    private boolean mIsScrolling = false;
    private int mSlideDirection = SLIDE_RIGHT;

    public SlideLayout(Context context) {
        this(context, null);
    }

    public SlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mScroller = new Scroller(context);
    }

    public int getSlideState() {
        int retValue = STATE_CLOSE;
        if (mIsScrolling) {
            retValue = STATE_SLIDING;
        } else {
            retValue = (getScrollX() == 0) ? STATE_CLOSE : STATE_OPEN;
        }
        return retValue;
    }

    public void smoothCloseSlide() {
        smoothScrollTo(0, 0);
    }

    public void smoothOpenSlide() {
        smoothScrollTo(mSlideView.getMeasuredWidth(), 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new IllegalArgumentException("SlideLayout only need contains two child (content and slide).");
        }

        mContentView = getChildAt(0);
        mSlideView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mContentView.getMeasuredWidth(), mContentView.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mContentView.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        switch (mSlideDirection) {
            case SLIDE_LEFT:
                mSlideView.layout(-mSlideView.getMeasuredWidth(), 0, 0, getMeasuredHeight());
                break;
            case SLIDE_RIGHT:
                mSlideView.layout(getMeasuredWidth(), 0, mSlideView.getMeasuredWidth() + getMeasuredWidth(), getMeasuredHeight());
                break;
            case SLIDE_TOP:
                mSlideView.layout(0, -mSlideView.getMeasuredHeight(), getMeasuredWidth(), 0);
                break;
            case SLIDE_BOTTOM:
                mSlideView.layout(0, getMeasuredHeight(), getMeasuredWidth(), mSlideView.getMeasuredHeight() + getMeasuredHeight());
                break;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mIsScrolling || super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int eventX = (int) event.getX();
        int eventY = (int) event.getY();
        int scrollX = getScrollX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getX();
                mLastY = (int) event.getY();
                mIsScrolling = false;
                //Maybe child not set OnClickListener, so ACTION_DOWN need to return true and use super.
                super.dispatchTouchEvent(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                int offsetX = eventX - mLastX;
                int offsetY = eventY - mLastY;
                if (Math.abs(offsetX) - Math.abs(offsetY) < 1) {
                    break;
                }
                getParent().requestDisallowInterceptTouchEvent(true);
                mIsScrolling = true;
                int newScrollX = scrollX - offsetX;
                if (newScrollX < 0) {
                    newScrollX = 0;
                } else if (newScrollX > mSlideView.getMeasuredWidth()) {
                    newScrollX = mSlideView.getMeasuredWidth();
                }
                scrollTo(newScrollX, 0);
                break;
            case MotionEvent.ACTION_UP:
                mIsScrolling = false;
                getParent().requestDisallowInterceptTouchEvent(false);
                int finalScrollX = 0;
                mSlideSensitiveWidth = mSlideView.getMeasuredWidth() / 2;
                if (scrollX > mSlideSensitiveWidth) {
                    finalScrollX = mSlideView.getMeasuredWidth();
                }
                smoothScrollTo(finalScrollX, 0);
                break;
        }

        mLastX = eventX;
        mLastY = eventY;
        return super.dispatchTouchEvent(event);
    }

    private void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int deltaX = destX - scrollX;
        int scrollY = getScrollY();
        int deltaY = destY - scrollY;
        mScroller.startScroll(scrollX, scrollY, deltaX, deltaY,
                (int) (Math.abs(Math.sqrt(deltaX*deltaX + deltaY*deltaY)) * 3));
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
