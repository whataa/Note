package io.github.whataa.alarm;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/12/13.
 */
public class CardLayoutManager extends RecyclerView.LayoutManager {



    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     *  注意！这个方法不是在每次你对布局作出改变时调用的。 它是 初始化布局 或者 在数据改变时重置子视图布局
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return dy;
    }

    /**
     * onLayoutChildren()会在每次数据集改变后被调用两次， 一次是"预布局"(pre-layout)阶段，一次是真实布局(real layout)
     * @return
     */
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

}
