package com.javen.devicemange.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.javen.devicemange.R;
import com.javen.devicemange.sms.SMSActivity;


public class DeviceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button id_clickMe=(Button)findViewById(R.id.id_clickMe);
        //启动之后模拟点击显示Toast
        setSimulateClick(id_clickMe, 10, 5);
    }
    // 激活程序
    public void OnActivate(View v) {
        DeviceMethod.getInstance(this).onActivate();
    }

    // 移除程序 如果不移除程序 APP无法被卸载
    public void OnRemoveActivate(View v) {
        DeviceMethod.getInstance(this).onRemoveActivate();
    }

    // 设置解锁方式 不需要激活就可以运行
    public void btnszmm(View v) {
        DeviceMethod.getInstance(this).startLockMethod();
    }

    // 设置解锁方式
    public void btnmm(View v) {
       DeviceMethod.getInstance(this).setLockMethod();
    }

    // 立刻锁屏
    public void btnlock(View v) {
        DeviceMethod.getInstance(this).LockNow();
    }

    // 设置5秒后锁屏
    public void btnlocktime(View v) {
        DeviceMethod.getInstance(this).LockByTime(5000);
    }

    // 恢复出厂设置
    public void btnwipe(View v) {
        DeviceMethod.getInstance(this).WipeData();
    }

    // 设置密码锁
    public void setPassword(View v) {
       DeviceMethod.getInstance(this).setPassword("1234");

    }

    public void startSMSActivity(View view){
        startActivity(new Intent(this, SMSActivity.class));
    }

    public void simulateClick(View view){
        Toast.makeText(this, "simulateClick clicked", Toast.LENGTH_LONG).show();
    }

    //模拟点击事件
    private void setSimulateClick(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }
}
