package edu.hsl.hollekeiti.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.base.MyAdapter;
import edu.hsl.hollekeiti.bean.FileInfo;
import edu.hsl.hollekeiti.phone.util.BitmapUtil;
import edu.hsl.hollekeiti.phone.util.CommonUtil;
import edu.hsl.hollekeiti.phone.util.FileTypeUtil;

/**
 * Created by Administrator on 2016/05/11.
 */
public class FileAdapter extends MyAdapter<FileInfo> {
    LayoutInflater           mInflater;
    LruCache<String, Bitmap> mLruCache;

    public FileAdapter(Context context) {
        super(context);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (Build.VERSION.SDK_INT >= 12) {
            mLruCache = new LruCache<String, Bitmap>(1 * 1024 * 1024) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
        } else {
            mLruCache = new LruCache<>(100);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHaber haber;
        if (convertView == null) {
            haber = new ViewHaber();
            convertView = mInflater.inflate(R.layout.layout_filemgl_listitem, null);
            haber.cb_del = (CheckBox) convertView.findViewById(R.id.cb_file_del);
            haber.tv_name = (TextView) convertView.findViewById(R.id.tv_file_name);
            haber.tv_size = (TextView) convertView.findViewById(R.id.tv_file_size);
            haber.tv_time = (TextView) convertView.findViewById(R.id.tv_file_time);
            haber.iv_icon = (ImageView) convertView.findViewById(R.id.iv_file_icon);
            convertView.setTag(haber);
        } else {
            haber = (ViewHaber) convertView.getTag();
        }
        haber.cb_del.setTag(position);
        haber.cb_del.setOnCheckedChangeListener(getListener());
        FileInfo info = getItem(position);
        haber.tv_name.setText(info.getFile().getName());
        haber.tv_time.setText(CommonUtil.getStrTime(info.getFile().lastModified()));
        haber.tv_size.setText(CommonUtil.getFileSize(info.getFile().length()));
        haber.cb_del.setChecked(info.isSelect());
        if (getFileIcon(info, haber.iv_icon) != null) {
            haber.iv_icon.setImageBitmap(getFileIcon(info, haber.iv_icon));
        }
        return convertView;
    }

    @NonNull
    private CompoundButton.OnCheckedChangeListener getListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (int) buttonView.getTag();
                getDataList().get(position).setSelect(isChecked);
            }
        };
    }

    private Bitmap getFileIcon(FileInfo fileInfo, ImageView iv_fileicon) {
        Bitmap bitmap   = null;
        String filePath = fileInfo.getFile().getPath();
        bitmap = mLruCache.get(filePath);
        if (bitmap != null) {
            return bitmap;
        }
        if (fileInfo.getFileType().equals(FileTypeUtil.TYPE_IMAGE)) {
            bitmap = BitmapUtil.loadBitmap(filePath,
                    new BitmapUtil.SizeMessage(context, true, 40, 40));
            if (bitmap != null) {
                mLruCache.put(filePath, bitmap);
                return bitmap;
            }
        }
        Resources res = context.getResources();
        int icon = res.getIdentifier(fileInfo.getIconName(),
                "drawable", context.getPackageName());//根据给定的文件类型查找对应的资源ID
        if (icon <= 0) {
            icon = R.drawable.icon_file;
        }
        bitmap = BitmapFactory.decodeResource(context.getResources(), icon);
        return bitmap;
    }
}
