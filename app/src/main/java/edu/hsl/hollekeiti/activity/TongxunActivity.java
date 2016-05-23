package edu.hsl.hollekeiti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.adapter.MyAdapterTongxun;
import edu.hsl.hollekeiti.base.MyActivity;
import edu.hsl.hollekeiti.phone.db.AssetsDBManager;
import edu.hsl.hollekeiti.phone.db.DBRead;
import edu.hsl.hollekeiti.bean.TelClassInfo;

public class TongxunActivity extends MyActivity {
    static List<TelClassInfo> listItemBean;
    MyAdapterTongxun adapter;
    ListView         listView;
    public static final String TAG = "蔡蔡蔡蔡伟";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongxun);
        listView = (ListView) findViewById(R.id.lv_show);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectItem(position);
                adapter.notifyDataSetInvalidated();
                Intent intent = null;
                if (position==0) {
                    intent=new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                } else {
//                    TelClassInfo telClassInfo=adapter.getItem(position);
                    intent = new Intent(TongxunActivity.this, TelActivity.class);
//                    intent.putExtra("idx",telClassInfo.idx);
                    intent.putExtra("idx",listItemBean.get(position).idx);
                }
                startActivity(intent);
            }
        });
    }

    private void initAppDBFile() {
//        DBRead.telFile.delete();
        if (!DBRead.isExistsTeladFile(DBRead.telFile)) {
            AssetsDBManager.copyAssetsFileToFile(this,
                    "db/commonnum.db", DBRead.telFile);
        }
    }

    /*onResume 方法 系统自动调用*/
    protected void onResume() {
        super.onResume();
        initAppDBFile();
        DBRead.clear();
        try {
            listItemBean = DBRead.readTelClassList(this);
            Log.d(TAG, "onResume:0 "+listItemBean.get(5).tv_table);
            if (listItemBean != null) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter = new MyAdapterTongxun(this, listItemBean);
//        Log.d(TAG, "onResume:00 "+adapter.getItem(0).tv_name);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

