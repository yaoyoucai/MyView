package shbd.flowlayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yh on 2016/7/19.
 * 自定义流式布局
 */
public class FlowLayout extends ViewGroup {
    //所有子view的集合
    private List<List<View>> mAllViews = new ArrayList<>();

    //行高的集合
    private List<Integer> mLineHeights = new ArrayList<>();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        int childCount = getChildCount();

        //遍历所有的子控件
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //测量子控件的宽和高
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            //换行
            if (lineWidth + childWidth > sizeWidth) {
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            //最后一个控件
            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }

        }

        Log.e("TAG", "sizeWidth: " + sizeWidth);
        Log.e("TAG", "sizeHeight: " + sizeHeight);
        /**
         * 设置控件的宽和高
         */
        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height
        );

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeights.clear();

        //整个控件的宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        //每一行的View的集合
        List<View> listViews = new ArrayList<>();
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            //换行
            if (lineWidth + childWidth > width) {
                //记录当前行的views
                mAllViews.add(listViews);
                //记录lineHeights
                mLineHeights.add(lineHeight);

                //重置行宽和行高
                lineHeight = childHeight;
                lineWidth = 0;

                //重置view的集合
                listViews = new ArrayList<>();


            }
            lineHeight = Math.max(childHeight, lineHeight);
            lineWidth += childWidth;
            listViews.add(childView);
        }
        //最后一个子控件
        mLineHeights.add(lineHeight);
        mAllViews.add(listViews);

        int left = 0;
        int top = 0;

        for (int i = 0; i < mAllViews.size(); i++) {
            lineHeight = mLineHeights.get(i);
            listViews = mAllViews.get(i);
            for (int j = 0; j < listViews.size(); j++) {
                View childView = listViews.get(j);

                if (childView.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
                int childLeft = left + params.leftMargin;
                int childTop = top + params.topMargin;
                int childRight = childLeft + childView.getMeasuredWidth();
                int childBottom = childTop + childView.getMeasuredHeight();

                childView.layout(childLeft, childTop, childRight, childBottom);

                left += childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            }
            left = 0;
            top += lineHeight;
        }

    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
