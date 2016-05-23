package edu.hsl.hollekeiti.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.base.MyAdapter;
import edu.hsl.hollekeiti.bean.RuningAppInfo;
import edu.hsl.hollekeiti.phone.util.CommonUtil;

/**
 * Created by Administrator on 2016/05/10.
 */
public class RuningAdapter extends MyAdapter<RuningAppInfo> {
    public static final int STATE_SHOW_USER = 0;
    public static final int STATE_SHOW_ALL  = 1;
    public static final int STATE_SHOW_SYS  = 2;
    private LayoutInflater inflater;
    private int            state;

    public RuningAdapter(Context context) {
        super(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        state = STATE_SHOW_USER;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHaber haber;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_speedup_listitem, null);
            haber = new ViewHaber();
            haber.tv_table = (TextView) convertView.findViewById(R.id.tv_app_lable);
            haber.tv_size = (TextView) convertView.findViewById(R.id.tv_app_packageSize);
            haber.iv_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
            haber.cb_del = (CheckBox) convertView.findViewById(R.id.cb_clear);
            haber.tv_version = (TextView) convertView.findViewById(R.id.tv_app_version);
            convertView.setTag(haber);
        }else{
            haber= (ViewHaber) convertView.getTag();
        }
        haber.cb_del.setTag(position);
        haber.cb_del.setOnCheckedChangeListener(getListener());
        haber.tv_table.setText(getItem(position).getLableName());
        haber.tv_size.setText("内存:"+ CommonUtil.getFileSize(getItem(position).getSize()));
        haber.iv_icon.setImageDrawable(getItem(position).getIcon());
        haber.cb_del.setChecked(getItem(position).isClear());
        haber.tv_version.setText(getItem(position).isSystem()?"系统进程":"");
        return convertView;
    }

    @NonNull
    private CompoundButton.OnCheckedChangeListener getListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getItem((Integer) buttonView.getTag()).setClear(isChecked);
            }
        };
    }
}
