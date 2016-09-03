package com.javen.devicemange;

import android.app.admin.DeviceAdminInfo;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.javen.devicemange.demo.DeviceActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private LinearLayout sectionB;
    private int aHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //提示激活设备管理器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        ComponentName deviceComponentName = new ComponentName(getPackageName(),MyDeviceReceiver.class.getName());
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceComponentName);
        this.startActivity(intent);


        sectionB = (LinearLayout) findViewById(R.id.main_section_b_outside);
        aHeight = getResources().getDimensionPixelSize(R.dimen.main_a_height);
        initListView();
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.main_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        for (int i = 0; i < 100; i++) {
            if (i==0){
                adapter.add("点击启动设备管理器Demo " + String.valueOf(i));
            }else {
                adapter.add("item " + String.valueOf(i));
            }
        }
        View headerView = LayoutInflater.from(this).inflate(R.layout.main_header, null);
        listView.addHeaderView(headerView);
        listView.setAdapter(adapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (getScrollY() >= aHeight) {
                    if (sectionB.getVisibility() == View.INVISIBLE) {
                        sectionB.setVisibility(View.VISIBLE);
                    }
                } else if (getScrollY() < aHeight) {
                    if (sectionB.getVisibility() == View.VISIBLE) {
                        sectionB.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(MainActivity.this, DeviceActivity.class));
            }
        });
    }

    //获取滚动距离
    public int getScrollY() {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();
        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = listView.getHeight();
        }
        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    public boolean isDeviceMange() {
        List<ResolveInfo> avail = getPackageManager().queryBroadcastReceivers(
                new Intent(DeviceAdminReceiver.ACTION_DEVICE_ADMIN_ENABLED),
                PackageManager.GET_META_DATA);
        //获取所有注册了DeviceAdminReceiver.ACTION_DEVICE_ADMIN_ENABLED即android.app.action.DEVICE_ADMIN_ENABLED action的广播接收者列表avail
        int count = avail == null ? 0 : avail.size();

        for (int i = 0; i < count; i++) {
            ResolveInfo ri = avail.get(i);
            System.out.println("isDefault>>>>" + ri.isDefault);
            System.out.println("ssss>>>" + ri.activityInfo.name);
            try {
                DeviceAdminInfo dpi = new DeviceAdminInfo(this, ri);
                System.out.println("isVisible>>>>" + dpi.isVisible());
            } catch (XmlPullParserException e) {
                Log.w("tag", "Skipping " + ri.activityInfo, e);
            } catch (IOException e) {
                Log.w("tag", "Skipping " + ri.activityInfo, e);
            }
        }
        return false;
    }
}
