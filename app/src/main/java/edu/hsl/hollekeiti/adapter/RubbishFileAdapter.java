package edu.hsl.hollekeiti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.base.MyAdapter;
import edu.hsl.hollekeiti.bean.RubbishFileInfo;
import edu.hsl.hollekeiti.phone.util.CommonUtil;

/**
 * Created by Administrator on 2016/05/13.
 */
public class RubbishFileAdapter extends MyAdapter<RubbishFileInfo> {
    LayoutInflater inflater;

    public RubbishFileAdapter(Context context) {
        super(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHaber haber;
        if (convertView == null) {
            haber = new ViewHaber();
            convertView = inflater.inflate(R.layout.layout_rubbishfile_listitem, null);
            haber.tv_name = (TextView) convertView.findViewById(R.id.tv_app_name);
            haber.iv_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
            haber.tv_size = (TextView) convertView.findViewById(R.id.tv_app_size);
            haber.cb_del = (CheckBox) convertView.findViewById(R.id.cb_app_del);
            convertView.setTag(haber);
        } else {
            haber = (ViewHaber) convertView.getTag();
        }
        haber.cb_del.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (int) buttonView.getTag();
                getDataList().get(position).setDel(isChecked);
            }
        });
        haber.cb_del.setTag(position);
        haber.iv_icon.setImageDrawable(getItem(position).getIcon());
        haber.tv_name.setText(getItem(position).getSoftCName());
        haber.tv_size.setText(CommonUtil.getFileSize(getItem(position).getSize()));
        haber.cb_del.setChecked(getItem(position).isDel());
        return convertView;
    }
}
