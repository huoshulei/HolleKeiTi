package edu.hsl.hollekeiti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.base.MyAdapter;
import edu.hsl.hollekeiti.bean.TelClassInfo;

/**
 * Created by Administrator on 2016/04/27.
 */
public class MyAdapterTel extends MyAdapter<TelClassInfo> {
    List<TelClassInfo> listItem;
    LayoutInflater     inflater;

    public MyAdapterTel(Context context, List<TelClassInfo> listItem) {
        super(context,listItem);
        this.listItem = listItem;
        inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHaber haber;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dialog_myadapter, null);
            haber = new ViewHaber();
            haber.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            haber.im_logo = (ImageView) convertView.findViewById(R.id.im_logo);
            haber.tv_tel = (TextView) convertView.findViewById(R.id.tv_tel);
            convertView.setTag(haber);
        } else {
            haber = (ViewHaber) convertView.getTag();
        }
        haber.tv_name.setText(listItem.get(position).tv_name);
        haber.im_logo.setImageResource(listItem.get(position).im_logo);
        haber.tv_tel.setText(listItem.get(position).tv_tel);
        return convertView;

    }
}
