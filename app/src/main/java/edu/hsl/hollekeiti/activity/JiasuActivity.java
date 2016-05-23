package edu.hsl.hollekeiti.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.adapter.RuningAdapter;
import edu.hsl.hollekeiti.base.MyActivity;
import edu.hsl.hollekeiti.bean.RuningAppInfo;
import edu.hsl.hollekeiti.phone.db.AppInfoManager;
import edu.hsl.hollekeiti.phone.db.MemoryManager;
import edu.hsl.hollekeiti.phone.db.SystemManager;
import edu.hsl.hollekeiti.phone.util.CommonUtil;

public class JiasuActivity extends MyActivity {
    public static final String TAG = "才才踩踩踩为";
    private ListView      runListView;
    private RuningAdapter adapter;
    private Button        btn_clear;
    private CheckBox      cb_checkClearAll;
    private Button        btn_showall;
    private ProgressBar   progressBar;
    private TextView      tv_ram;
    private ProgressBar   pb_ram;
    private TextView      tv_phoneName;
    private TextView      tv_phoneModle;
    private Map<Integer, List<RuningAppInfo>> runMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiasu);
        initActionBar(getResources().getString(R.string.rocket),
                R.mipmap.btn_homeasup_default, -1, getListener());
        adapter = new RuningAdapter(this);
        runListView = (ListView) findViewById(R.id.listviewLoad);
        Log.d(TAG, "run: 00000000000000000" + adapter.getCount() + 1);
        runListView.setAdapter(adapter);
        initMainButton();
        loadData();

    }

    private void initMainButton() {
        tv_phoneName = (TextView) findViewById(R.id.tv_phoneName);
        tv_phoneModle = (TextView) findViewById(R.id.tv_phoneModel);
        pb_ram = (ProgressBar) findViewById(R.id.pb_ram);
        tv_ram = (TextView) findViewById(R.id.tv_ramMessage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        cb_checkClearAll = (CheckBox) findViewById(R.id.cb_all);
        btn_clear = (Button) findViewById(R.id.btn_oneKeyClear);
        btn_showall = (Button) findViewById(R.id.btn_showapp);
        btn_clear.setOnClickListener(getListener());
        btn_showall.setOnClickListener(getListener());
        cb_checkClearAll.setOnCheckedChangeListener(getCheckListener());
        initPhoneData();
        initRamData();
    }

    @NonNull
    private CompoundButton.OnCheckedChangeListener getCheckListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                List<RuningAppInfo> infos = adapter.getDataList();
                for (RuningAppInfo info : infos) {
                    info.setClear(isChecked);
                }
                adapter.notifyDataSetChanged();
            }
        };
    }

    private void initRamData() {
        float totalRam = MemoryManager.getPhoneRamMenry();
        float freeRam  = MemoryManager.getPhoneFreeRamMenry(getApplicationContext());
        float usedRam  = totalRam - freeRam;
        float usedP    = usedRam / totalRam;
        int   used100  = (int) (usedP * 100);
        pb_ram.setMax(100);
        pb_ram.setProgress(used100);
        tv_ram.setText("可用内存:" + CommonUtil.getFileSize((long) usedRam)
                + "/" + CommonUtil.getFileSize((long) totalRam));
    }

    private void initPhoneData() {
        tv_phoneName.setText(SystemManager.getPhoneName().toUpperCase());
        tv_phoneModle.setText(SystemManager.getPhoneModel());
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
                    case R.id.btn_oneKeyClear:
                        List<RuningAppInfo> listapp = adapter.getDataList();
                        for (RuningAppInfo info : listapp) {
                            if (info.isClear()) {
                                AppInfoManager.getAppInfoManager(getApplicationContext())
                                        .killProcesses(info.getPackageName());
                            }
                        }
                        loadData();
                        cb_checkClearAll.setChecked(false);
                        break;
                    case R.id.btn_showapp:
                        if (runMap != null) {
                            switch (adapter.getState()) {
                                case RuningAdapter.STATE_SHOW_USER:
                                    adapter.setClearDataList(runMap
                                            .get(AppInfoManager.RUNING_APP_TYPE_SYS));
                                    adapter.setState(RuningAdapter.STATE_SHOW_ALL);
                                    btn_showall.setText(getResources()
                                            .getString(R.string.speedup_show_userapp));
                                    break;
                                case RuningAdapter.STATE_SHOW_ALL:
                                    adapter.setClearDataList(runMap
                                            .get(AppInfoManager.RUNING_APP_TYPE_USER));
                                    adapter.setState(RuningAdapter.STATE_SHOW_USER);
                                    btn_showall.setText(getResources()
                                            .getString(R.string.speedup_show_sysapp));
                                    break;
                            }
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    default:
                        break;
                }

            }
        };
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        runListView.setVisibility(View.INVISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: 000000000000000001111111111111");
                runMap = AppInfoManager
                        .getAppInfoManager(getApplicationContext())
                        .getRuningAppInfos();
                Log.d(TAG, "run: 00000000000000000" + runMap.size());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRamData();
                        progressBar.setVisibility(View.INVISIBLE);
                        runListView.setVisibility(View.VISIBLE);
                        adapter.setClearDataList(runMap
                                .get(AppInfoManager.RUNING_APP_TYPE_USER));
                        adapter.setState(RuningAdapter.STATE_SHOW_USER);
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "run: 00000000000000000" + runMap.size());
                    }
                });
            }
        }).start();
    }
}
