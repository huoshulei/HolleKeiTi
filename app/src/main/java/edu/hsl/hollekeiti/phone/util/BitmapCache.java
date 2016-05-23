package edu.hsl.hollekeiti.phone.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

import edu.hsl.hollekeiti.activity.SoftmgrAppshowActivity;

/**
 * Created by Administrator on 2016/05/09.
 */
public class BitmapCache {
    static private BitmapCache                   cache;
    /*用于Cache的储存*/
    private        Hashtable<Integer, MySoftRef> hashRefs;
    /*垃圾Reference的队列(所引用的对象已经被回收,则将该引用存入队列中)*/
    private        ReferenceQueue<Bitmap>        q;

    /**
     * 继承SoftReference,使得,每一个实例都是具有可识别的标识
     */
    private class MySoftRef extends SoftReference<Bitmap> {
        private Integer _key = 0;

        public MySoftRef(Bitmap r, ReferenceQueue<Bitmap> q, int key) {
            super(r, q);
            _key = key;
        }
    }

    public BitmapCache() {
        hashRefs = new Hashtable<>();
        q = new ReferenceQueue<>();
    }

    /**
     * 取得缓存器实例
     */
    public static BitmapCache getInstance() {
        if (cache == null) {
            cache = new BitmapCache();
        }
        return cache;
    }

    /**
     * 一软引用的方式对一个Bitmap对象的实例进行引用并保存该引用
     */
    public void addCacheBitmap(Bitmap b, Integer key) {
        cleanCache();
        MySoftRef ref = new MySoftRef(b, q, key);
        hashRefs.put(key, ref);
    }

    /**
     * 依据所指的drawable下的图片资源ID号(可以根据自己需要从网络或本地path下获取),重新获取Bitmap对象的实例
     */
    public Bitmap getBitmap(int id, Context context) {
        Bitmap bitmap = null;
        /*缓存中是否有该Bitmap实例的软引用,如果有,从软引用中取得*/
        if (hashRefs.containsKey(id)) {
            MySoftRef ref = hashRefs.get(id);
            bitmap = ref.get();
        }
        /*如果没有软引用,或者从软引用中得到的实例是null,重新构建一个实例
        * 并保存这个新建实例的软引用*/
        if (bitmap == null) {
            /*传说decodeStream直接调用JNI>>nativeDecodeAsset()来完成decode,
            * 无须再使用java层的createBitmap,从而节省了java层的空间*/
            try {
                bitmap = BitmapFactory.decodeStream(context.getResources().openRawResource(id));
            } catch (Resources.NotFoundException e) {
                Log.d(SoftmgrAppshowActivity.TAG, "run: 为什么0" + e);
                e.printStackTrace();
            }
            this.addCacheBitmap(bitmap, id);
        }
        return bitmap;
    }

    private void cleanCache() {
        MySoftRef ref = null;
        while ((ref = (MySoftRef) q.poll()) != null) {
            hashRefs.remove(ref._key);
        }
    }

    /**
     * 清除Cache内的全部内容
     */
    private void clearCache() {
        cleanCache();
        hashRefs.clear();
        System.gc();
        System.runFinalization();
    }
}
