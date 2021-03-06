package com.viewpagerindicator.as.library.pageview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class LoopRecyclerViewPager extends RecyclerViewPager {

    public LoopRecyclerViewPager(Context context) {
        this(context, null);
    }

    public LoopRecyclerViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopRecyclerViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        super.scrollToPosition(getMiddlePosition());
    }

    @Override
    public void swapAdapter(RecyclerView.Adapter adapter, boolean removeAndRecycleExistingViews) {
        super.swapAdapter(adapter, removeAndRecycleExistingViews);
        super.scrollToPosition(getMiddlePosition());
    }

    @Override
    @NonNull
    protected RecyclerViewPagerAdapter ensureRecyclerViewPagerAdapter(RecyclerView.Adapter adapter) {
        return (adapter instanceof LoopRecyclerViewPagerAdapter)
                ? (LoopRecyclerViewPagerAdapter) adapter
                : new LoopRecyclerViewPagerAdapter(this, adapter);
    }

    /**
     * Starts a smooth scroll to an adapter position.
     * if position less than adapter.getActualCount,
     * position will be transform to right position.
     *
     * @param position target position
     */
    @Override
    public void smoothScrollToPosition(int position) {
        super.smoothScrollToPosition(transformInnerPositionIfNeed(position));
    }

    /**
     * Starts a scroll to an adapter position.
     * if position less than adapter.getActualCount,
     * position will be transform to right position.
     *
     * @param position target position
     */
    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(transformInnerPositionIfNeed(position));
    }

    /**
     * get actual current position in actual adapter.
     */
    public int getActualCurrentPosition() {
        int position = getCurrentPosition();
        return position % getActualItemCountFromAdpater();
    }

    private int getActualItemCountFromAdpater() {
        return ((LoopRecyclerViewPagerAdapter) getWrapperAdapter()).getActualItemCount();
    }

    private int transformInnerPositionIfNeed(int position) {
        final int actualItemCount = getActualItemCountFromAdpater();
        final int actualCurrentPosition = getCurrentPosition() % actualItemCount;
        int bakPosition1 = getCurrentPosition()
                - actualCurrentPosition
                + position % actualItemCount;
        int bakPosition2 = getCurrentPosition()
                - actualCurrentPosition
                - actualItemCount
                + position % actualItemCount;
        int bakPosition3 = getCurrentPosition()
                - actualCurrentPosition
                + actualItemCount
                + position % actualItemCount;
        // get position which is closer to current position
        if (Math.abs(bakPosition1 - getCurrentPosition()) > Math.abs(bakPosition2 -
                getCurrentPosition())){
            if (Math.abs(bakPosition2 -
                    getCurrentPosition())> Math.abs(bakPosition3 -
                    getCurrentPosition())) {
                return bakPosition3;
            }
            return bakPosition2;
        } else {
            if (Math.abs(bakPosition1 -
                    getCurrentPosition())> Math.abs(bakPosition3 -
                    getCurrentPosition())) {
                return bakPosition3;
            }
            return bakPosition1;
        }
    }

    private int getMiddlePosition() {
        int middlePosition = Integer.MAX_VALUE / 2;
        final int actualItemCount = getActualItemCountFromAdpater();
        if (actualItemCount > 0 && middlePosition % actualItemCount != 0) {
            middlePosition = middlePosition - middlePosition % actualItemCount;
        }
        return middlePosition;
    }
}
