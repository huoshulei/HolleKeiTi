package edu.hsl.hollekeiti.base;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.hsl.hollekeiti.activity.SoftmgrAppshowActivity;

/**
 * Created by Administrator on 2016/05/05.
 *
 * @param <DataInfo>
 */
public abstract class MyAdapter<DataInfo> extends BaseAdapter {
    protected Context context;
    private List<DataInfo> dataList = new ArrayList<>();
//    LayoutInflater cInflater;


    public MyAdapter(Context context, List<DataInfo> list) {
        this.context = context;
        this.dataList = list;
    }

    public MyAdapter(Context context) {
        this.context = context;
    }

    public List<DataInfo> getDataList() {
        return dataList;
    }

    public void setClearDataList(DataInfo data) {
        dataList.clear();
        dataList.add(data);
    }

    public void addDataToAdapter(DataInfo data) {
        dataList.add(data);
    }

    public void addDataToAdapter(List<DataInfo> data) {
        dataList.addAll(data);
    }

    public void setClearDataList(List<DataInfo> data) {
        Log.d(SoftmgrAppshowActivity.TAG, "run: " + dataList.size());
        dataList.clear();
        Log.d(SoftmgrAppshowActivity.TAG, "run: " + dataList.size());
        dataList.addAll(data);
    }

    @Override
    public int getCount() {
        if (dataList.size() > 0) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public DataInfo getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    public class ViewHaber {
        public TextView  tv_table;
        public TextView  tv_name;
        public TextView  tv_tel;
        public ImageView im_logo;
        public ImageView iv_icon;
        public TextView  tv_title;
        public TextView  tv_text;
        public TextView  tv_version;
        public CheckBox  cb_del;
        public TextView  tv_size;
        public TextView  tv_time;
    }
}
