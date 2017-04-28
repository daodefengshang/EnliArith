package com.szh.enliarith.activity;

import android.support.v4.view.WindowCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.szh.enliarith.R;
import com.szh.enliarith.utils.VersionUtil;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private View backView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_MODE_OVERLAY);
        setContentView(R.layout.activity_about);
        initView();
        initEvents();
    }

    private void initView() {
        backView = findViewById(R.id.back);
        TextView versionView = (TextView) findViewById(R.id.version);
        versionView.setText("v" + VersionUtil.getVersion(this));
    }

    private void initEvents() {
        backView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.anim_access, R.anim.anim_return);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            overridePendingTransition(R.anim.anim_access, R.anim.anim_return);
        }
        return true;
    }
}
