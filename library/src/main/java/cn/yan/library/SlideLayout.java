package cn.yan.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by yan on 17-1-8.
 *
 * like SlidingPaneLayout, but this used to mini lib, such as no support v4.
 *
 */

public class SlideLayout extends LinearLayout {
    public static final int STATE_CLOSE = 0;
    public static final int STATE_SLIDING = 1;
    public static final int STATE_OPEN = 2;

    private View mContentView;
    private View mSlideView;

    private Scroller mScroller;

    private int mLastX = 0;
    private int mLastY = 0;

    private int mSlideSensitiveWidth = 0;

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
        setOrientation(HORIZONTAL);
        mScroller = new Scroller(context);
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

    public int getSlideState() {
        int retValue = STATE_CLOSE;
        if (isScrolling) {
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
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return isScrolling || super.onInterceptTouchEvent(event);
    }

    boolean isScrolling = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = getScrollX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getX();
                mLastY = (int) event.getY();
                isScrolling = false;
                //Maybe child not set OnClickListener, so ACTION_DOWN need to return true and use super.
                super.dispatchTouchEvent(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - mLastX;
                int offsetY = y - mLastY;
                if (/*Math.abs(offsetX) < 10 &&*/ Math.abs(offsetX) - Math.abs(offsetY) < 1) {
                    break;
                }
                getParent().requestDisallowInterceptTouchEvent(true);
                isScrolling = true;
                int newScrollX = scrollX - offsetX;
                if (offsetX != 0) {
                    if (newScrollX < 0) {
                        newScrollX = 0;
                    } else if (newScrollX > mSlideView.getMeasuredWidth()) {
                        newScrollX = mSlideView.getMeasuredWidth();
                    }
                    scrollTo(newScrollX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                isScrolling = false;
                getParent().requestDisallowInterceptTouchEvent(false);
                int finalScrollX = 0;
                mSlideSensitiveWidth = mSlideView.getMeasuredWidth() / 2;
                if (scrollX > mSlideSensitiveWidth) {
                    finalScrollX = mSlideView.getMeasuredWidth();
                }
                smoothScrollTo(finalScrollX, 0);
                break;
        }

        mLastX = x;
        mLastY = y;
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
