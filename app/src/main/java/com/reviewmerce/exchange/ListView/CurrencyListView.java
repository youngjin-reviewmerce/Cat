package com.reviewmerce.exchange.ListView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.BankExchangeItem;

/**
 * Created by onebuy on 2015-08-18.
 */
public class CurrencyListView extends ListView {

    private ImageView mDragView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;
    private int mDragPos;
    private int mFirstDragPos;
    private int mDragPoint;
    private int mCoordOffset;
    private DragListener mDragListener;
    private DropListener mDropListener;
    private RemoveListener mRemoveListener;
    private int mUpperBound;
    private int mLowerBound;
    private int mHeight;
    private GestureDetector mGestureDetector;
    public static final int FLING = 0;
    public static final int SLIDE_RIGHT = 1;
    public static final int SLIDE_LEFT = 2;
    private int mRemoveMode = -1;
    private Rect mTempRect = new Rect();
    private Bitmap mDragBitmap;
    private final int mTouchSlop;
    private int mItemHeightNormal=-1;
    private int mItemHeightExpanded=-1;
    private int grabberId=-1;
    private int dragndropBackgroundColor=0x00000000;


    public CurrencyListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurrencyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        if (attrs!=null) {
            TypedArray a=getContext()
                    .obtainStyledAttributes(attrs,
                            R.styleable.CurrencyListView,
                            0, 0);

            mItemHeightNormal=a.getDimensionPixelSize(R.styleable.CurrencyListView_normal_height, 0);
            mItemHeightExpanded=a.getDimensionPixelSize(R.styleable.CurrencyListView_expanded_height, mItemHeightNormal);
            grabberId=a.getResourceId(R.styleable.CurrencyListView_grabber, -1);
            dragndropBackgroundColor=a.getColor(R.styleable.CurrencyListView_dragndrop_background, 0x00000000);
            mRemoveMode=a.getInt(R.styleable.CurrencyListView_remove_mode, -1);

            a.recycle();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mRemoveListener != null && mGestureDetector == null) {
            if (mRemoveMode == FLING) {
                mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        if (mDragView != null) {
                            if (velocityX > 1000) {
                                Rect r = mTempRect;
                                mDragView.getDrawingRect(r);
                                if ( e2.getX() > r.right * 2 / 3) {
                                    stopDragging();
                                    mRemoveListener.remove(mFirstDragPos);
                                    unExpandViews(true);
                                }
                            }

                            return true;
                        }
                        return false;
                    }
                });
            }
        }
        if (mDragListener != null || mDropListener != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    int x = (int) ev.getX();
                    int y = (int) ev.getY();
                    int itemnum = pointToPosition(x, y);
                    if (itemnum == AdapterView.INVALID_POSITION) {
                        break;
                    }
                    ViewGroup item = (ViewGroup) getChildAt(itemnum - getFirstVisiblePosition());
                    mDragPoint = y - item.getTop();
                    mCoordOffset = ((int)ev.getRawY()) - y;
                    View dragger = item.findViewById(grabberId);
                    Rect r = mTempRect;
//									dragger.getDrawingRect(r);

                    r.left=dragger.getLeft();
                    r.right=dragger.getRight();
                    r.top=dragger.getTop();
                    r.bottom=dragger.getBottom();

                    if ((r.left<x) && (x<r.right)) {
                        item.setDrawingCacheEnabled(true);
                        // Create a copy of the drawing cache so that it does not get recycled
                        // by the framework when the list tries to clean up memory
                        Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());
                        startDragging(bitmap, y);
                        mDragPos = itemnum;
                        mFirstDragPos = mDragPos;
                        mHeight = getHeight();
                        int touchSlop = mTouchSlop;
                        mUpperBound = Math.min(y - touchSlop, mHeight / 3);
                        mLowerBound = Math.max(y + touchSlop, mHeight * 2 /3);
                        return false;
                    }
                    mDragView = null;
                    break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private int myPointToPosition(int x, int y) {
        Rect frame = mTempRect;
        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            child.getHitRect(frame);
            if (frame.contains(x, y)) {
                return getFirstVisiblePosition() + i;
            }
        }
        return INVALID_POSITION;
    }

    private int getItemForPosition(int y) {
        int adjustedy = y - mDragPoint - 32;
        int pos = myPointToPosition(0, adjustedy);
        if (pos >= 0) {
            if (pos <= mFirstDragPos) {
                pos += 1;
            }
        } else if (adjustedy < 0) {
            pos = 0;
        }
        return pos;
    }

    private void adjustScrollBounds(int y) {
        if (y >= mHeight / 3) {
            mUpperBound = mHeight / 3;
        }
        if (y <= mHeight * 2 / 3) {
            mLowerBound = mHeight * 2 / 3;
        }
    }


    private void unExpandViews(boolean deletion) {
        for (int i = 0;; i++) {
            View v = getChildAt(i);
            if (v == null) {
                if (deletion) {

                    int position = getFirstVisiblePosition();
                    int y = getChildAt(0).getTop();
                    setAdapter(getAdapter());
                    setSelectionFromTop(position, y);

                }
                layoutChildren();
                v = getChildAt(i);
                if (v == null) {
                    break;
                }
            }
            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.height = mItemHeightNormal;
            v.setLayoutParams(params);
            v.setVisibility(View.VISIBLE);
        }
    }
    private void doExpansion() {
        int childnum = mDragPos - getFirstVisiblePosition();
        if (mDragPos > mFirstDragPos) {
            childnum++;
        }

        View first = getChildAt(mFirstDragPos - getFirstVisiblePosition());

        for (int i = 0;; i++) {
            View vv = getChildAt(i);
            if (vv == null) {
                break;
            }
            int height = mItemHeightNormal;
            int visibility = View.VISIBLE;
            if (vv.equals(first)) {

                if (mDragPos == mFirstDragPos) {

                    visibility = View.INVISIBLE;
                } else {

                    height = 1;
                }
            } else if (i == childnum) {
                if (mDragPos < getCount() - 1) {
                    height = mItemHeightExpanded;
                }
            }
            ViewGroup.LayoutParams params = vv.getLayoutParams();
            params.height = height;
            vv.setLayoutParams(params);
            vv.setVisibility(visibility);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(ev);
        }
        if ((mDragListener != null || mDropListener != null) && mDragView != null) {
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    Rect r = mTempRect;
                    mDragView.getDrawingRect(r);
                    stopDragging();

                    if (mRemoveMode == SLIDE_RIGHT && ev.getX() > r.left+(r.width()*3/4)) {
                        if (mRemoveListener != null) {
                            mRemoveListener.remove(mFirstDragPos);
                        }
                        unExpandViews(true);
                    } else if (mRemoveMode == SLIDE_LEFT && ev.getX() < r.left+(r.width()/4)) {
                        if (mRemoveListener != null) {
                            mRemoveListener.remove(mFirstDragPos);
                        }
                        unExpandViews(true);
                    } else {
                        if (mDropListener != null && mDragPos >= 0 && mDragPos < getCount()) {
                            mDropListener.drop(mFirstDragPos, mDragPos);
                        }
                        unExpandViews(false);
                    }
                    break;

                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    int x = (int) ev.getX();
                    int y = (int) ev.getY();
                    dragView(x, y);
                    int itemnum = getItemForPosition(y);
                    if (itemnum >= 0) {
                        if (action == MotionEvent.ACTION_DOWN || itemnum != mDragPos) {
                            if (mDragListener != null) {
                                mDragListener.drag(mDragPos, itemnum);
                            }
                            mDragPos = itemnum;
                            doExpansion();
                        }
                        int speed = 0;
                        adjustScrollBounds(y);
                        if (y > mLowerBound) {
                            // scroll the list up a bit
                            speed = y > (mHeight + mLowerBound) / 2 ? 16 : 4;
                        } else if (y < mUpperBound) {
                            // scroll the list down a bit
                            speed = y < mUpperBound / 2 ? -16 : -4;
                        }
                        if (speed != 0) {
                            int ref = pointToPosition(0, mHeight / 2);
                            if (ref == AdapterView.INVALID_POSITION) {
                                //we hit a divider or an invisible view, check somewhere else
                                ref = pointToPosition(0, mHeight / 2 + getDividerHeight() + 64);
                            }
                            View v = getChildAt(ref - getFirstVisiblePosition());
                            if (v!= null) {
                                int pos = v.getTop();
                                setSelectionFromTop(ref, pos - speed);
                            }
                        }
                    }
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    private void startDragging(Bitmap bm, int y) {
        stopDragging();

        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.gravity = Gravity.TOP;
        mWindowParams.x = 0;
        mWindowParams.y = y - mDragPoint + mCoordOffset;

        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.windowAnimations = 0;

        ImageView v = new ImageView(getContext());
//        int backGroundColor = getContext().getResources().getColor(R.color.dragndrop_background);
        v.setBackgroundColor(dragndropBackgroundColor);
        v.setImageBitmap(bm);
        mDragBitmap = bm;

        mWindowManager = (WindowManager)getContext().getSystemService("window");
        mWindowManager.addView(v, mWindowParams);
        mDragView = v;
    }

    private void dragView(int x, int y) {
        float alpha = 1.0f;
        int width = mDragView.getWidth();

        if (mRemoveMode == SLIDE_RIGHT) {
            if (x > width / 2) {
                alpha = ((float)(width - x)) / (width / 2);
            }
            mWindowParams.alpha = alpha;
        }
        else if (mRemoveMode == SLIDE_LEFT) {
            if (x < width / 2) {
                alpha = ((float)x) / (width / 2);
            }
            mWindowParams.alpha = alpha;
        }
        mWindowParams.y = y - mDragPoint + mCoordOffset;
        mWindowManager.updateViewLayout(mDragView, mWindowParams);
    }

    private void stopDragging() {
        if (mDragView != null) {
            WindowManager wm = (WindowManager)getContext().getSystemService("window");
            wm.removeView(mDragView);
            mDragView.setImageDrawable(null);
            mDragView = null;
        }
        if (mDragBitmap != null) {
            mDragBitmap.recycle();
            mDragBitmap = null;
        }
    }

    public void setDragListener(DragListener l) {
        mDragListener = l;
    }

    public void setDropListener(DropListener l) {
        mDropListener = l;
    }

    public void setRemoveListener(RemoveListener l) {
        mRemoveListener = l;
    }

    public interface DragListener {
        void drag(int from, int to);
    }
    public interface DropListener {
        void drop(int from, int to);
    }
    public interface RemoveListener {
        void remove(int which);
    }
}
