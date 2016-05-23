package edu.hsl.hollekeiti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.base.MyAdapter;
import edu.hsl.hollekeiti.bean.PhoneInfo;

/**
 * Created by Administrator on 2016/05/11.
 */
public class PhoneAdapter extends MyAdapter<PhoneInfo> {
    LayoutInflater inflater;

    public PhoneAdapter(Context context) {
        super(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHaber haber;
        if (convertView == null) {
            haber = new ViewHaber();
            convertView = inflater.inflate(R.layout.layout_phonemgr_listitem, null);
            haber.iv_icon = (ImageView) convertView.findViewById(R.id.iv_phonemgr_icon);
            haber.tv_title = (TextView) convertView.findViewById(R.id.tv_phonemgr_title);
            haber.tv_text = (TextView) convertView.findViewById(R.id.tv_phonemgr_text);
            convertView.setTag(haber);
        } else {
            haber = (ViewHaber) convertView.getTag();
        }
        haber.iv_icon.setImageDrawable(getItem(position).getIcon());
        haber.tv_title.setText(getItem(position).getTitle());
        haber.tv_text.setText(getItem(position).getText());
//        给每个图片加载不同背景
        switch (position % 3) {
            case 0:
                haber.iv_icon.setBackgroundResource
                        (R.mipmap.notification_information_progress_green);
                break;
            case 1:
                haber.iv_icon.setBackgroundResource
                        (R.mipmap.notification_information_progress_bg);
                break;
            default:
                haber.iv_icon.setBackgroundResource
                        (R.mipmap.notification_information_progress_yellow);
                break;
        }
        return convertView;
    }
}
