package edu.hsl.hollekeiti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.base.MyAdapter;
import edu.hsl.hollekeiti.bean.TelClassInfo;

/**
 * Created by Administrator on 2016/05/06.
 */
public class MyAdapterTongxun extends MyAdapter<TelClassInfo> {
    public List<TelClassInfo> listItem;
    LayoutInflater inflater;
    int            selectItem;
//    public static int tv_info;

    /*关联上下文,以及数据集合*/
    public MyAdapterTongxun(Context context, List<TelClassInfo> listItem) {
        super(context,listItem);
        this.listItem = listItem;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHaber haber;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dialog_adapter, null);
            haber = new ViewHaber();
            haber.tv_table = (TextView) convertView.findViewById(R.id.tv_table);
            convertView.setTag(haber);
        } else {
            haber = (ViewHaber) convertView.getTag();
        }
        haber.tv_table.setText(listItem.get(position).tv_table);
//        if (position == selectItem) {
//            tv_info = position;
//        }
        return convertView;


    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }


}
