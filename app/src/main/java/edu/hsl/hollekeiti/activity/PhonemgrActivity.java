package edu.hsl.hollekeiti.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.adapter.PhoneAdapter;
import edu.hsl.hollekeiti.base.MyActivity;
import edu.hsl.hollekeiti.bean.PhoneInfo;
import edu.hsl.hollekeiti.phone.db.MemoryManager;
import edu.hsl.hollekeiti.phone.db.SystemManager;
import edu.hsl.hollekeiti.phone.util.CommonUtil;

public class PhonemgrActivity extends MyActivity {
    private View                     ll_battery;//电池布局
    private ListView                 mListView; //手机检测
    private PhoneAdapter             mAdapter;
    private TextView                 tv_battery;//电池百分比
    private ProgressBar              pb_battery;//电池进度百分比
    private BatteryBroadcastReceiver mBroadcastReceiver;//电池广播接收
    private ProgressBar              pb_loading;
    private int                      currentBattery;//当前电量
    private int                      temperatureBattery;//电池温度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonemgr);
        initActionBar(getResources().getString(R.string.phonemgr),
                R.mipmap.btn_homeasup_default, -1, getListener());
        initMainButton();
        mListView = (ListView) findViewById(R.id.listviewLoad);
        mAdapter = new PhoneAdapter(this);
        mListView.setAdapter(mAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                initAdapterData();

            }
        }).start();
    }

    private void initAdapterData() {
        pb_loading.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.INVISIBLE);
        SystemManager   manager = SystemManager.getSystemManager(getApplicationContext());
        String          title   = "设备名称:" + SystemManager.getPhoneName();
        String          text    = "系统版本:" + SystemManager.getPhoneSystemVersion();
        Drawable        icon    = getResources().getDrawable(R.mipmap.setting_info_icon_version);
        final PhoneInfo info1   = new PhoneInfo(title, text, icon);
        title = "全部运行内存:" + CommonUtil.getFileSize(MemoryManager.getPhoneRamMenry());
        text = "剩余运行内存:" + CommonUtil.getFileSize(MemoryManager
                .getPhoneFreeRamMenry(getApplicationContext()));
        icon = getResources().getDrawable(R.mipmap.setting_info_icon_space);
        final PhoneInfo info2 = new PhoneInfo(title, text, icon);
        title = "CPU名称:" + manager.getPhoneCPUName();
        text = "CPU数量:" + manager.getPhoneCPUNumber();
        icon = getResources().getDrawable(R.mipmap.setting_info_icon_cpu);
        final PhoneInfo info3 = new PhoneInfo(title, text, icon);
        title = "手机分辨率:" + manager.getResolution();
        text = "相机分辨率:" + manager.getMaxPhoneSize();
        icon = getResources().getDrawable(R.mipmap.setting_info_icon_camera);
        final PhoneInfo info4 = new PhoneInfo(title, text, icon);
        title = "基带版本:" + manager.getPhoneSystemBasebandVersion();
        text = "是否Root:" + (manager.isRoot() ? "是" : "否");
        icon = getResources().getDrawable(R.mipmap.setting_info_icon_cpu);
        final PhoneInfo info5 = new PhoneInfo(title, text, icon);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.addDataToAdapter(info1);
                mAdapter.addDataToAdapter(info2);
                mAdapter.addDataToAdapter(info3);
                mAdapter.addDataToAdapter(info4);
                mAdapter.addDataToAdapter(info5);
                mAdapter.notifyDataSetChanged();
                pb_loading.setVisibility(View.INVISIBLE);
                mListView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initMainButton() {
        ll_battery = findViewById(R.id.ll_layout_battery);
        ll_battery.setOnClickListener(getListener());
        tv_battery = (TextView) findViewById(R.id.tv_battery);
        pb_battery = (ProgressBar) findViewById(R.id.pb_battery);
        pb_loading = (ProgressBar) findViewById(R.id.progressBar);
        mBroadcastReceiver = new BatteryBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBroadcastReceiver, filter);
    }

    @NonNull
    private View.OnClickListener getListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.image_left:
//                        startActivity(HomeActivity.class, 0);
                        finish();
                        break;
                    case R.id.ll_layout_battery:
                        showBatteryMessage();
                        break;
                }
            }
        };
    }

    private void showBatteryMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("电池电量信息");
        builder.setItems(new String[]{"当前电量:" + currentBattery,
                "电池温度" + temperatureBattery}, null);
        builder.show();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    class BatteryBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                Bundle bundle = intent.getExtras();
                int    maxB   = (int) bundle.get(BatteryManager.EXTRA_SCALE);
//                currentBattery = (int) bundle.get(BatteryManager.EXTRA_LEVEL);
//                temperatureBattery = (int) bundle.get(BatteryManager.EXTRA_TEMPERATURE);
                temperatureBattery = intent.getIntExtra("temperature", 0) / 10;
                currentBattery = intent.getIntExtra("level", 0);
                pb_battery.setMax(maxB);
                pb_battery.setProgress(currentBattery);
                int current100 = currentBattery * 100 / maxB;
                tv_battery.setText(current100 + "%");
            }
        }
    }
}
