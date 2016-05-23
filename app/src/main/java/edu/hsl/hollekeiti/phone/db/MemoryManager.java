package edu.hsl.hollekeiti.phone.db;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2016/05/08.
 */
public class MemoryManager {

    public static final String CARD_PATH = "SECONDARY_STORAGE";
//    public static final String CARD_PATH = "KNOX_STORAGE";
    public static final String TAG="土豪";

    /**
     * 内部储存路径
     */
    public static String getPhoneInSDCardPath() {
        String sd = Environment.getExternalStorageState();
        if (!sd.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 外部储存路径
     */
    @Nullable
    public static String getPhoneOutSDCardPath() {
        Map<String, String> map = System.getenv();
        Log.d(TAG, "getPhoneOutSDCardPath: "+map.containsKey(CARD_PATH));
        for (Map.Entry entry:map.entrySet()){
            Log.d(TAG, "getPhoneOutSDCardPath00000000000000: "+entry.getKey());
        }

        if (map.containsKey(CARD_PATH)) {
            String paths  = map.get(CARD_PATH);
            String path[] = paths.split(":");
            if (path == null || path.length <= 0) {
                return null;
            }
            return path[0];
        }
        return null;
    }

    public static long getPhoneOutSDCardSize(Context context) {
        try {
            File path = new File(getPhoneOutSDCardPath());
            if (path == null) {
                return 0;
            }
            StatFs statFs     = new StatFs(path.getPath());
            long   blockSize  = statFs.getBlockSizeLong();//单个数据块的大小
            long   blockCount = statFs.getBlockCountLong();//数据块总个数
            return blockCount * blockSize;//单位byte
        } catch (Exception e) {
            Toast.makeText(context, "外部存储卡异常", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public static long getPhoneOutSDCardFreeSize(Context context) {
        try {
            File path = new File(getPhoneOutSDCardPath());
            if (path == null) {
                return 0;
            }
            StatFs statFs     = new StatFs(path.getPath());
            long   blockSize  = statFs.getBlockSizeLong();
            long   blockCount = statFs.getAvailableBlocksLong();
            return blockCount * blockSize;
        } catch (Exception e) {
            Toast.makeText(context, "外部存储卡异常", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    /**
     * 机身内存
     */
    public static long getPhoneSelSize() {
        File   path       = Environment.getRootDirectory();//根目录
        StatFs statFs     = new StatFs(path.getPath());
        long   blockSize  = statFs.getBlockSizeLong();
        long   blockCount = statFs.getBlockCountLong();
        long   rfileSize  = blockSize * blockCount;
        path = Environment.getDownloadCacheDirectory();//下载目录
        statFs = new StatFs(path.getPath());
        blockSize = statFs.getBlockSizeLong();
        blockCount = statFs.getBlockCountLong();
        long dfileSize = blockSize * blockCount;
        return rfileSize + dfileSize;
    }
/**
 * 机身剩余空间
 * */
    public static long getPhoneSelFreeSize() {
        File   path       = Environment.getRootDirectory();
        StatFs statFs     = new StatFs(path.getPath());
        long   blockSize  = statFs.getBlockSizeLong();
        long   blockCount = statFs.getAvailableBlocksLong();
        long   rfileSize  = blockSize * blockCount;
        path = Environment.getDownloadCacheDirectory();
        statFs = new StatFs(path.getPath());
        blockCount = statFs.getAvailableBlocksLong();
        blockSize = statFs.getBlockSizeLong();
        long dfileSize = blockSize * blockCount;
        return rfileSize + dfileSize;
    }
    /**
     * 机身内置存储卡大小
     * */
    public static long getPhoneSelSDCardSize(){
        String sd=Environment.getExternalStorageState();
        if (!sd.equals(Environment.MEDIA_MOUNTED)){
            return 0;
        }
        File path=Environment.getExternalStorageDirectory();
        StatFs statFs=new StatFs(path.getPath());
        long blockSize=statFs.getBlockSizeLong();
        long blockCount=statFs.getBlockCountLong();
        return blockSize*blockCount;
    }
    /**
     * 机身内置存储卡剩余空间
     * */
    public static long getPhoneSelSDCardFreeSize(){
        String sd=Environment.getExternalStorageState();
        if (!sd.equals(Environment.MEDIA_MOUNTED)){
            return 0;
        }
        File path=Environment.getExternalStorageDirectory();
        StatFs statFs=new StatFs(path.getPath());
        long blockSize=statFs.getBlockSizeLong();
        long blockCount=statFs.getAvailableBlocksLong();
        return blockSize*blockCount;
    }
    /**
     * 获取手机总内存大小
     * */
    public static long getPhoneAllSize(){
        File path=Environment.getDataDirectory();
        StatFs statFs=new StatFs(path.getPath());
        long blockSize=statFs.getBlockSizeLong();
        long blockCount=statFs.getBlockCountLong();
        long fileSize=blockSize*blockCount;
        return fileSize+getPhoneSelSize();
    }
    /**
     * 获取机身剩余内存
     * */
    public static long getPhoneAllFreeSize(){
        File path=Environment.getDataDirectory();
        StatFs statFs=new StatFs(path.getPath());
        long blockSize=statFs.getBlockSizeLong();
        long blockCount=statFs.getAvailableBlocksLong();
        long fileSize=blockSize*blockCount;
        return fileSize+getPhoneSelFreeSize();
    }
    /**
     * 设备空闲运行内存
     * */
    public static long getPhoneFreeRamMenry(Context context){
        ActivityManager.MemoryInfo info =new ActivityManager.MemoryInfo();
        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        am.getMemoryInfo(info);
        return info.availMem;
    }
    /**
     * 设备总运行内存
     * */
    public static long getPhoneRamMenry(){
        try {
            FileReader fr=new FileReader("/proc/meminfo");
            BufferedReader br=new BufferedReader(fr);
            String text=br.readLine();
            String[] array=text.split("\\s+");
            return Long.valueOf(array[1])*1024;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
