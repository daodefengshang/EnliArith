package com.szh.enliarith.listener;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.szh.enliarith.adapter.MainRecyclerViewAdapter;

/**
 * Created by szh on 2017/3/5.
 */
public class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private RecyclerView recyclerView;
    private GestureDetectorCompat gestureDetectorCompat;
    private OnItemClickListener onItemClickListener;

    public OnRecyclerItemClickListener(RecyclerView recyclerView, OnItemClickListener onItemClickListener) {
        this.recyclerView = recyclerView;
        this.onItemClickListener = onItemClickListener;
        gestureDetectorCompat = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(child);
                if (childViewHolder instanceof MainRecyclerViewAdapter.ViewHolder) {
                    int position = recyclerView.getChildAdapterPosition(child);
                    onItemClickListener.onItemClick(childViewHolder, position);
                }
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(child);
                if (childViewHolder instanceof MainRecyclerViewAdapter.ViewHolder) {
                    int position = recyclerView.getChildAdapterPosition(child);
                    onItemClickListener.onLongClick(childViewHolder, position);
                }
            }
            super.onLongPress(e);
        }
    }
    public interface OnItemClickListener {
        void onLongClick(RecyclerView.ViewHolder vh, int position);
        void onItemClick(RecyclerView.ViewHolder vh, int position);
    }
}
