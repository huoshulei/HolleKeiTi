package edu.hsl.hollekeiti.phone.db;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.bean.RubbishFileInfo;

/**
 * Created by Administrator on 2016/05/13.
 */
public class DBClearPathManager {
    public static final String FILE_NAME    = "clearpath.db";
    public static final String PACKAGE_NAME = "edu.hsl.hollekeiti";
    public static final String FILE_PATH    = "/data" + Environment
            .getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME;
    public static final String TAG          = "我";
    private static List<RubbishFileInfo> sInfos;

    public static List<RubbishFileInfo> getPhoneRubbishFile(Context context) {
        if (sInfos == null) {
            sInfos = readSoftdetailTable();
        }
        List<RubbishFileInfo> infos = new ArrayList<>();
        for (RubbishFileInfo info : sInfos) {
            File file = new File(info.getFilePath());
            Log.d(TAG, "readSoftdetailTable:00001 "+file);
            if (file.exists()) {
                Drawable icon = null;
                try {
                    icon = context.getPackageManager().getApplicationIcon(info.getApkName());
                } catch (PackageManager.NameNotFoundException e) {
                    icon = context.getResources().getDrawable(R.mipmap.ic_launcher1);
                }
                info.setIcon(icon);
                infos.add(info);
            }
        }
        return infos;
    }

    @NonNull
    private static List<RubbishFileInfo> readSoftdetailTable() {

        List<RubbishFileInfo> infos = new ArrayList<>();
        String                path  = FILE_PATH + "/" + FILE_NAME;
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(path, null);
        String         sql      = "select*from softdetail";
        Cursor         cursor   = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int    _id       = cursor.getInt(cursor.getColumnIndex("_id"));
                String softCName = cursor.getString(cursor.getColumnIndex("softChinesename"));
                String softEName = cursor.getString(cursor.getColumnIndex("softEnglishname"));
                String apkName   = cursor.getString(cursor.getColumnIndex("apkname"));
                String filepath  = cursor.getString(cursor.getColumnIndex("filepath"));
                Log.d(TAG, "readSoftdetailTable:00001 "+filepath);
                filepath = Environment.getExternalStorageDirectory().getPath() + filepath;
                Log.d(TAG, "readSoftdetailTable:00002 "+filepath);
                RubbishFileInfo info = new RubbishFileInfo(_id, softCName, softEName, apkName, filepath);
                infos.add(info);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return infos;
    }

    public static void readUpdataDB(InputStream path) {
        File fileto = new File(FILE_PATH + "/" + FILE_NAME);
        if (fileto.exists()) {
            return;
        }
        try {
            BufferedInputStream  bis = new BufferedInputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileto));
            int                  len = 0;
            byte[]               b   = new byte[5 * 1024];
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
            bos.close();
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
