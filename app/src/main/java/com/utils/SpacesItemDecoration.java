package com.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by bharat on 24/7/16.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space, count = 0;
    private boolean value = false;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    public SpacesItemDecoration(int space, boolean value) {
        this.space = space;
        this.value = value;
    }

    public SpacesItemDecoration(int space, int count) {
        this.space = space;
        this.count = count;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        if (!value) {
            outRect.bottom = space + space;
            outRect.top = 0;

            // Add top margin only for the first item to avoid double space between items
            if (count == 0) {
                if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
                    outRect.top = space;
                } else {
                    outRect.top = 0;
                }
            } else {
                if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1 || parent.getChildLayoutPosition(view) == 2) {
                    outRect.top = space;
                } else {
                    outRect.top = 0;
                }
            }
        }
    }
}