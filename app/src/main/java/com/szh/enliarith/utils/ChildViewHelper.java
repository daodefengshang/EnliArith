package com.szh.enliarith.utils;

import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by szh on 2017/4/27.
 */
public class ChildViewHelper {
    /**
     * Find the topmost view under the given point.
     *
     * @param x Horizontal position in pixels to search
     * @param y Vertical position in pixels to search
     * @return The index of The child view under (x, y) or -1 if no matching child is found
     */
    public static int findChildViewUnder(ViewGroup viewGroup, float x, float y) {
        final int count = viewGroup.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = viewGroup.getChildAt(i);
            final float translationX = ViewCompat.getTranslationX(child);
            final float translationY = ViewCompat.getTranslationY(child);
            if (x >= child.getLeft() + translationX &&
                    x <= child.getRight() + translationX &&
                    y >= child.getTop() + translationY &&
                    y <= child.getBottom() + translationY) {
                return i;
            }
        }
        return -1;
    }
}
