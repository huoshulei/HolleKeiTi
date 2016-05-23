package edu.hsl.hollekeiti.phone.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2016/05/09.
 */
public class BitmapUtil {
    public static Bitmap loadBitmap(String pathName, SizeMessage message) {
        int                   targetw  = message.getW();
        int                   targehth = message.getH();
        Context               context  = message.getContext();
        BitmapFactory.Options options  = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//打开"边界处理"
        BitmapFactory.decodeFile(pathName, options);
        int resw = options.outWidth;
        int resh = options.outHeight;
        if (resw <= targetw && resh < +targehth) {
            options.inSampleSize = 1;//设置加载使得资源比例
        } else {//计算比例
            int scalew = resw / targetw;
            int scaleh = resh / targehth;
            options.inSampleSize = scalew > scaleh ? scalew : scaleh;//设置加载比例
        }
        options.inJustDecodeBounds = false;//关闭 边界处理
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
        return bitmap;
    }

    public static Bitmap loadBitmap(int ID, SizeMessage message) {
//        加载图片目标大小
        int                   targetw = message.getW();
        int                   targeth = message.getH();
        Context               context = message.getContext();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), ID, options);
        int resw = options.outWidth;
        int resh = options.outHeight;
        if (resw <= targetw && resh == targeth) {
            options.inSampleSize=1;
        }else{
            int scalew=resw/targetw;
            int scaleh=resh/targeth;
            options.inSampleSize=scalew>scaleh?scalew:scaleh;
        }
        options.inJustDecodeBounds=false;
        Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(),ID,options);
        return bitmap;
    }

    public static class SizeMessage {
        private int     w;
        private int     h;
        private Context context;

        public SizeMessage(Context context, boolean isPX, int w, int h) {
            this.context = context;
            if (isPX) {
                w = DeviceUtil.dp2px(context, w);
                h = DeviceUtil.dp2px(context, h);
            }
            this.w = w;
            this.h = h;
        }

        public Context getContext() {
            return context;
        }

        public int getW() {
            return w;
        }

        public void setW(int w) {
            this.w = w;
        }

        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }
    }
}
