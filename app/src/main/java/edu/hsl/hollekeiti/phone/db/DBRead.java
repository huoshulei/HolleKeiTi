package edu.hsl.hollekeiti.phone.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.bean.TelClassInfo;


/**
 * Created by Administrator on 2016/04/27.读取数据库数据
 */
public class DBRead {
    public static File                    telFile;
    public static File                    telFile1;
    public static ArrayList<TelClassInfo> classListInfos;

    public static final String TAG = "蔡蔡蔡蔡伟";

    static {
        String dbFile  = "data/data/edu.hsl.hollekeiti/";
        File   fileDir = new File(dbFile);
//        fileDir.mkdirs();
        Log.d(TAG,"telFile dir path:"+ fileDir.mkdirs());
        Log.d(TAG,"telFile dir path:"+ fileDir.exists());
        Log.d(TAG,"telFile dir path:"+ fileDir.canRead());
        Log.d(TAG,"telFile dir path:"+ fileDir.canWrite());
        telFile = new File(dbFile, "commonnum.db");
        telFile1=new File(dbFile, "music/om.mp3");
        Log.d(TAG, "cgfhafhjdasb你的骄傲的客服编号看" + telFile.canRead());
    }

    //判断文件是否存在
    public static boolean isExistsTeladFile(File telFile) {
        File toFile = telFile;
        Log.d(TAG, "isExistsTeladFile: "+toFile.isFile());
        Log.d(TAG, "isExistsTeladFile: "+toFile.length());
        Log.d(TAG, "isExistsTeladFile: "+toFile.delete());
        telFile.delete();
        if (!toFile.exists() || toFile.length() <= 0) {
            return false;
        }
        return true;
    }

    //清空数据
    public static void clear() {
        if (classListInfos != null) {
            classListInfos.clear();
        }
    }
    //读取数据库目录
    public static ArrayList<TelClassInfo> readTelClassList(Context context) throws Exception {
        Log.d(TAG, "readTelClassList: 11");
        classListInfos = new ArrayList<>();
        add(new TelClassInfo("本地电话", 0));

        SQLiteDatabase db     = null;
        Cursor         cursor = null;
        db = SQLiteDatabase.openOrCreateDatabase(telFile, null);
        cursor = db.rawQuery("select*from classlist", null);

        if (cursor.moveToFirst()) {

            do {
                String       table        = cursor.getString(cursor.getColumnIndex("name"));
                int          idx          = cursor.getInt(cursor.getColumnIndex("idx"));

//                TelClassInfo telClassInfo = new TelClassInfo(table, idx);
                TelClassInfo telClassInfo =new TelClassInfo(table,idx);
                Log.d(TAG, "readTelClassList: 11"+telClassInfo.tv_table);
                Log.d(TAG, "readTelClassList: 11"+table);
                //添加数据到集合
                add(telClassInfo);
            } while (cursor.moveToNext());
        }
        //关闭数据库
        cursor.close();
        db.close();
        //返回集合数据
        return classListInfos;
    }

    public static void add(TelClassInfo t) {
        classListInfos.add(t);
    }
/**
 * 根据数据库目录读取子数据库数据添加到子集合;
 * */
    public static ArrayList<TelClassInfo> readTelClassList(int idx) throws Exception {

        ArrayList<TelClassInfo> telNumber = new ArrayList<>();

        SQLiteDatabase db     = SQLiteDatabase.openOrCreateDatabase(telFile, null);//打开一个数据库
        Cursor         cursor = db.rawQuery("select*from table" + idx, null);//游标
        if (cursor.moveToFirst()) {
            do {
                String       name         = cursor.getString(cursor.getColumnIndex("name"));
                String       number       = cursor.getString(cursor.getColumnIndex("number"));
                TelClassInfo telClassInfo = new TelClassInfo(name, number, R.mipmap.ic_launcher);
                telNumber.add(telClassInfo);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return telNumber;
    }
/**
 * 读取本地通讯录数据并添加到子集合
 * */
//    public static ArrayList<TelClassInfo> readTelbendi(Context context) throws Exception {
//        ArrayList<TelClassInfo> telNumber = new ArrayList<>();
//        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
//                null, null, null, null);
//        int telid  = 0;
//        int nameid = 0;
//        if (cursor.getCount() > 0) {
//            telid = cursor.getColumnIndex(ContactsContract.Contacts._ID);
//            nameid = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//        }
//        while (cursor.moveToNext()) {
//            String name   = cursor.getString(nameid);
//            String telNum = cursor.getString(telid);
//            Cursor phones = context.getContentResolver().
//                    query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + telNum,
//                            null, null);
//            int phonesid = 0;
//            if (phones.getCount() > 0) {
//                phonesid = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//            }
//            String telp = "";
//            while (phones.moveToNext()) {
//                telp = phones.getString(phonesid);
//            }
//            TelClassInfo telClassInfo = new TelClassInfo(name, telp, R.mipmap.ic_launcher);
//            telNumber.add(telClassInfo);
//        }
//        cursor.close();
//        return telNumber;
//
//    }
}

