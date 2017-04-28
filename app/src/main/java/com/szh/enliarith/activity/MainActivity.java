package com.szh.enliarith.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.szh.enliarith.R;
import com.szh.enliarith.adapter.MainRecyclerViewAdapter;
import com.szh.enliarith.listener.OnRecyclerItemClickListener;
import com.szh.enliarith.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnRecyclerItemClickListener.OnItemClickListener {

    private long exitTime = 0;
    private View versionView;
    private RecyclerView recyclerView;

    private List<int[]> list = new ArrayList<>();
    private OnRecyclerItemClickListener itemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvents();
    }

    private void initView() {
        versionView = findViewById(R.id.version);
        recyclerView = (RecyclerView) findViewById(R.id.content_recycler);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position == 0 || position == 5 ? 2 : 1;
                }
            });
        }
        recyclerView.setHasFixedSize(true);
        int[] counts = {R.drawable.count, R.string.count_name};
        int[] plusSmall = {R.drawable.plus_small,  R.string.plus_small_name};
        int[] minusSmall = {R.drawable.minus_small, R.string.minus_small_name};
        int[] plusLarge = {R.drawable.plus_large, R.string.plus_large_name};
        int[] minusLarge = {R.drawable.minus_large, R.string.minus_large_name};
        int[] abacus = {R.drawable.abacus_thumb, R.string.abacus_name};
        list.add(counts);
        list.add(plusSmall);
        list.add(minusSmall);
        list.add(plusLarge);
        list.add(minusLarge);
        list.add(abacus);
        MainRecyclerViewAdapter mainRecyclerViewAdapter = new MainRecyclerViewAdapter(this, list);
        recyclerView.setAdapter(mainRecyclerViewAdapter);
    }

    private void initEvents() {
        versionView.setOnClickListener(this);
        itemClickListener = new OnRecyclerItemClickListener(recyclerView, this);
        recyclerView.addOnItemTouchListener(itemClickListener);
    }

    @Override
    public void onLongClick(RecyclerView.ViewHolder vh, int position) {

    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder vh, int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(MainActivity.this, CountActivity.class));
                break;
            case 1:
                startActivity(new Intent(MainActivity.this, PlusSmallActivity.class));
                break;
            case 2:
                startActivity(new Intent(MainActivity.this, MinusSmallActivity.class));
                break;
            case 3:
                startActivity(new Intent(MainActivity.this, PlusLargeActivity.class));
                break;
            case 4:
                startActivity(new Intent(MainActivity.this, MinusLargeActivity.class));
                break;
            case 5:
                startActivity(new Intent(MainActivity.this, AbacusActivity.class));
                break;
        }
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.version:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.removeOnItemTouchListener(itemClickListener);
        ToastUtil.setToastNull();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.toast(MainActivity.this.getApplicationContext(), R.string.exit_string);
                exitTime = System.currentTimeMillis();
            } else {
                this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
