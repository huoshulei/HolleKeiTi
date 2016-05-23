package edu.hsl.hollekeiti.phone.db;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/04/27.
 */
public class AssetsDBManager {
    public static final String TAG="蔡蔡蔡蔡伟";
    public static void copyAssetsFileToFile(Context context, String path, File toFile) {
        InputStream          inputStream          = null;
        BufferedInputStream  bufferedInputStream  = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            Log.d(TAG, "onStartCommand:03 "+DBRead.telFile1);
            inputStream = context.getAssets().open(path);
            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream(toFile, false));
            int    len  = 0;
            byte[] buff = new byte[2048];
            while ((len = bufferedInputStream.read(buff)) != -1) {
                bufferedOutputStream.write(buff, 0, len);
                bufferedOutputStream.flush();
            }
        } catch (IOException e) {
            Log.d(TAG, "onStartCommand:034 "+DBRead.telFile1);
            e.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
