package edu.hsl.hollekeiti.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.adapter.RubbishFileAdapter;
import edu.hsl.hollekeiti.base.MyActivity;
import edu.hsl.hollekeiti.bean.RubbishFileInfo;
import edu.hsl.hollekeiti.phone.db.DBClearPathManager;
import edu.hsl.hollekeiti.phone.db.FileManager;
import edu.hsl.hollekeiti.phone.util.CommonUtil;

public class ClearActivity extends MyActivity {
    public static final String TAG = "蔡伟";
    ProgressBar        pb_loading;
    ListView           listView;
    RubbishFileAdapter adapter;
    TextView           tv_size;
    CheckBox           cb_delall;
    long titalsize = 0;
    Button btn_del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onClick: 100000000000000000000000000000");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear);
        initActionBar(getResources().getString(R.string.sdclean),
                R.mipmap.btn_homeasup_default, -1, getListener());
        pb_loading = (ProgressBar) findViewById(R.id.pb_clear);
        listView = (ListView) findViewById(R.id.lv_rubbish);
        adapter = new RubbishFileAdapter(getApplicationContext());
        tv_size = (TextView) findViewById(R.id.tv_file_size);
        cb_delall = (CheckBox) findViewById(R.id.cb_all);
        btn_del = (Button) findViewById(R.id.btn_clear_rubbish);
        btn_del.setOnClickListener(getListener());
        cb_delall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                List<RubbishFileInfo> infos = adapter.getDataList();
                for (RubbishFileInfo info : infos) {
                    info.setDel(isChecked);
                }
                adapter.notifyDataSetChanged();
            }
        });
        listView.setAdapter(adapter);
        try {
            asyncLoadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void myHandleMessage(Message m) {
        if (m.what == 1) {
            long size = (long) m.obj;
            titalsize += size;
            Log.d(TAG, "run: 11" + titalsize);
            tv_size.setText(CommonUtil.getFileSize(titalsize));
            Log.d(TAG, "run: 12" + CommonUtil.getFileSize(titalsize));
        }
        if (m.what == 2) {
            Log.d(TAG, "run: 13" + titalsize);
            tv_size.setText(CommonUtil.getFileSize(titalsize));
            List<RubbishFileInfo> infos = (List<RubbishFileInfo>) m.obj;
            pb_loading.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            adapter = new RubbishFileAdapter(getApplicationContext());
            listView.setAdapter(adapter);
            adapter.setClearDataList(infos);
            adapter.notifyDataSetChanged();
        }
    }

    private void asyncLoadData() throws IOException {
        InputStream path = getResources().getAssets().open("db/clearpath.db");
        DBClearPathManager.readUpdataDB(path);
        final List<RubbishFileInfo> infos = DBClearPathManager.getPhoneRubbishFile(getApplicationContext());
        titalsize = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: 00000000001" + infos.size());
                for (RubbishFileInfo info : infos) {
                    File file = new File(info.getFilePath());
                    long size = FileManager.getFileSize(file);
                    info.setSize(size);
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    message.obj = size;
                    handler.sendMessage(message);
                    Log.d(TAG, "run: 1" + size);
                }
                Message message = handler.obtainMessage();
                message.what = 2;
                message.obj = infos;
                handler.sendMessage(message);
                Log.d(TAG, "run: 2" + infos.size());
            }
        }).start();
    }

    @NonNull
    private View.OnClickListener getListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.image_left:
                        finish();
                        break;
                    case R.id.btn_clear_rubbish:
                        Log.d(TAG, "onClick: 1");
                        deleteFile();
                        Log.d(TAG, "onClick: 2");
                        break;
                }
            }
        };
    }

    private void deleteFile() {
        Log.d(TAG, "onClick: 12");
        List<RubbishFileInfo> infos   = adapter.getDataList();
        List<RubbishFileInfo> delinfo = new ArrayList<>();
        for (RubbishFileInfo info : infos) {
            if (info.isDel()) {
                delinfo.add(info);
                Log.d(TAG, "onClick: 14");
            }
        }
        Log.d(TAG, "onClick: 13");
        for (RubbishFileInfo info : delinfo) {
            Log.d(TAG, "onClick: 15");
            FileManager.getFileManager()
                    .deleteFile(new File(info.getFilePath()));
            adapter.getDataList().remove(info);
            Log.d(TAG, "onClick: 16");
            titalsize -= info.getSize();
            Log.d(TAG, "onClick: 17");
            tv_size.setText(CommonUtil.getFileSize(titalsize));
            Log.d(TAG, "onClick: 18");
            adapter.notifyDataSetChanged();
        }
        Log.d(TAG, "onClick: 20");
        System.gc();
    }
}
