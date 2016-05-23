package edu.hsl.hollekeiti.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.adapter.AppAdapter;
import edu.hsl.hollekeiti.base.MyActivity;
import edu.hsl.hollekeiti.bean.AppInfo;
import edu.hsl.hollekeiti.phone.db.AppInfoManager;

public class SoftmgrAppshowActivity extends MyActivity {
    public static final String TAG = "才才踩踩踩为";
    private int            id;
    private ListView       appListView;
    private AppAdapter     adapter;
    private ProgressBar    progressBar;
    private CheckBox       cb_delall;
    private Button         btn_delall;
    private AppDelRecevice appDelRecevice;
    private List<AppInfo> mAppInfoList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_softmgr_appshow);
        final int id    = getIntent().getIntExtra("id", R.id.rl_soft_all);
        String    title = getResources().getString(R.string.softmgr);
        switch (id) {
            case R.id.rl_soft_all:
                title = getResources().getString(R.string.allsoft);
                break;
            case R.id.rl_soft_sys:
                title = getResources().getString(R.string.syssoft);
                break;
            case R.id.rl_soft_use:
                title = getResources().getString(R.string.usesoft);
                break;
        }
        this.id = id;
        initActionBar(title, R.mipmap.btn_homeasup_default, -1, onClickListener());
        adapter = new AppAdapter(this);
        appListView = (ListView) findViewById(R.id.listviewLoad);
        appListView.setAdapter(adapter);
        appListView.setOnScrollListener(getL());
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn_delall = (Button) findViewById(R.id.bt_delall);
        cb_delall = (CheckBox) findViewById(R.id.cb_all);
        cb_delall.setOnCheckedChangeListener(getListener());
        btn_delall.setOnClickListener(onClickListener());
        asynLoadApp();
        appDelRecevice = new AppDelRecevice();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        filter.addAction(AppDelRecevice.ACTION_APPDEL);
        registerReceiver(appDelRecevice, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(appDelRecevice);
    }

    @NonNull
    private CompoundButton.OnCheckedChangeListener getListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppAdapter    adapter      = (AppAdapter) appListView.getAdapter();
                List<AppInfo> mAppInfoList = adapter.getDataList();
                for (AppInfo info : mAppInfoList) {
                    info.setDel(isChecked);
                }
                adapter.notifyDataSetChanged();
            }
        };
    }

    @NonNull
    private AbsListView.OnScrollListener getL() {
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://快速滑动
                        adapter.setFling(true);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://滑动时
                        adapter.setFling(false);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://停止时
                        adapter.setFling(false);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        };
    }

    @NonNull
    private View.OnClickListener onClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.image_left:
                        finish();
                        break;
                    case R.id.bt_delall:
                        AppAdapter appAdapter = (AppAdapter) appListView.getAdapter();
                        List<AppInfo> appInfos = appAdapter.getDataList();
                        for (AppInfo info : appInfos) {
                            if (info.isDel()) {
                                String packageName = info.getPackageInfo().packageName;
                                Intent intent      = new Intent(Intent.ACTION_DELETE);
                                intent.setData(Uri.parse("package:" + packageName));
                                startActivity(intent);
                            }
                        }
                        break;
                }
            }
        };
    }

    public void asynLoadApp() {
        progressBar.setVisibility(View.VISIBLE);
        appListView.setVisibility(View.INVISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (id) {
                    case R.id.rl_soft_all:
                        mAppInfoList = AppInfoManager.getAppInfoManager(getApplicationContext())
                                .getAllPackageInfo(true);

                        break;
                    case R.id.rl_soft_sys:
                        mAppInfoList = AppInfoManager.getAppInfoManager(getApplicationContext())
                                .getSystemPackageInfo(true);
                        break;
                    case R.id.rl_soft_use:
                        mAppInfoList = AppInfoManager.getAppInfoManager(getApplicationContext())
                                .getUserPackageInfo(true);
                        break;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        appListView.setVisibility(View.VISIBLE);
                        Log.d(TAG, "run: " + mAppInfoList.size());
                        Log.d(TAG, "run: " + mAppInfoList.get(0).getPackageInfo().packageName);
                        adapter.setClearDataList(mAppInfoList);
//                        Log.d(TAG, "run: "+adapter.getItem(0).getPackageInfo().packageName);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    public class AppDelRecevice extends BroadcastReceiver {
        public final static String ACTION_APPDEL = "com.androidy.app.phone.del";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_PACKAGE_REMOVED) || action.equals(ACTION_APPDEL)) {
                asynLoadApp();
            }
        }
    }
}
