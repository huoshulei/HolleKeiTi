package edu.hsl.hollekeiti.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.activity.SoftmgrAppshowActivity;
import edu.hsl.hollekeiti.base.MyAdapter;
import edu.hsl.hollekeiti.bean.AppInfo;
import edu.hsl.hollekeiti.phone.util.BitmapCache;
import edu.hsl.hollekeiti.phone.util.BitmapUtil;

/**
 * Created by Administrator on 2016/05/09.
 */
public class AppAdapter extends MyAdapter<AppInfo> {
    public static final String TAG = "ccccccccccccccccccc";
    private LayoutInflater inflater;
    private BitmapCache    bitmapCache;//软引用
    private Bitmap         defIconBitmap;//默认图像,在快速滑动时会显示默认图像
    private boolean        isFling;//是否在快速滑动(此时就该显示默认图像,而不去做加载图像等操作)

    public boolean isFling() {
        return isFling;
    }

    public void setFling(boolean fling) {
        isFling = fling;
    }

    public Bitmap mBitmap() {
        if (isFling) {
            return defIconBitmap;
        }
        return defIconBitmap;
    }

    public AppAdapter(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
//        设置图片大小
        BitmapUtil.SizeMessage sizeMessage = new BitmapUtil.SizeMessage(context, false, 60, 60);
        defIconBitmap = BitmapUtil.loadBitmap(R.mipmap.ic_launcher, sizeMessage);
        bitmapCache = BitmapCache.getInstance();
        mBitmap();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHaber haber;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_showapp_listitem, null);
            haber = new ViewHaber();
            haber.iv_icon = (ImageView) convertView.findViewById(R.id.iv_showapp_icon);
            haber.tv_title = (TextView) convertView.findViewById(R.id.tv_showapp_lable);
            haber.tv_text = (TextView) convertView.findViewById(R.id.tv_showapp_packagename);
            haber.tv_version = (TextView) convertView.findViewById(R.id.tv_showapp_version);
            haber.cb_del = (CheckBox) convertView.findViewById(R.id.cb_del);
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
        AppInfo info = getItem(position);
        String title = info.getPackageInfo().applicationInfo
                .loadLabel(context.getPackageManager()).toString();
        String text    = info.getPackageInfo().packageName;//包名
        String version = info.getPackageInfo().versionName;//版本号
        Log.d(TAG, "getView:吾问无为谓吾问无为谓S "+getCount());
        boolean isDel  = info.isDel();
        Bitmap  bitmap = null;
        try {
            bitmap = ((BitmapDrawable) info.getPackageInfo()
                    .applicationInfo.loadIcon(context.getPackageManager())).getBitmap();
        } catch (Exception e) {
            Log.d(SoftmgrAppshowActivity.TAG, "run: 为什么" + e);
            e.printStackTrace();
        }
        bitmapCache.addCacheBitmap(bitmap, position);
        haber.iv_icon.setImageBitmap(bitmapCache.getBitmap(position, context));
        haber.tv_title.setText(title);
        haber.tv_text.setText(text);
        haber.tv_version.setText(version);
        haber.cb_del.setChecked(isDel);


        return convertView;
    }
}