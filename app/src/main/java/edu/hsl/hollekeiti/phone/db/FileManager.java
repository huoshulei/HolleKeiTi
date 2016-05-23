package edu.hsl.hollekeiti.phone.db;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.hsl.hollekeiti.bean.FileInfo;
import edu.hsl.hollekeiti.phone.util.FileTypeUtil;

/**
 * Created by Administrator on 2016/05/11.
 */
public class FileManager {
    public static File inSDCardDir  = null;
    public static File outSDCardDir = null;

    static {
        Log.d("我走了吗", "setListener:1 0000000000000");
        if (MemoryManager.getPhoneInSDCardPath() != null) {

            inSDCardDir = new File(MemoryManager.getPhoneInSDCardPath());
            Log.d("我走了吗", "setListener:1 00000000000001" + inSDCardDir);
        }
        if (MemoryManager.getPhoneOutSDCardPath() != null) {

            outSDCardDir = new File(MemoryManager.getPhoneOutSDCardPath());
            Log.d("我走了吗", "setListener:1 00000000000002" + outSDCardDir);
        }
    }

    public FileManager() {
    }

    public static FileManager sFileManager = new FileManager();

    public static FileManager getFileManager() {
        return sFileManager;
    }

    private boolean isStopSearch = false;//是否停止搜索

    public boolean isStopSearch() {
        return isStopSearch;
    }

    public void setStopSearch(boolean stopSearch) {
        isStopSearch = stopSearch;
    }

    /**
     * 文件搜索
     * 1任意文件集合
     * 2文档文件集合
     * 3视频文件集合
     * 4音频文件集合
     * 5图像文件集合
     * 6ZIP文件集合
     * 7Apk文件集合
     */
    private List<FileInfo> anyFileList = new ArrayList<>();
    private long anyFileSize;
    private List<FileInfo> txtFileList = new ArrayList<>();
    private long txtFileSize;
    private List<FileInfo> videoFileList = new ArrayList<>();
    private long videoFileSize;
    private List<FileInfo> audioFileList = new ArrayList<>();
    private long audioFileSize;
    private List<FileInfo> imageFileList = new ArrayList<>();
    private long imageFileSize;
    private List<FileInfo> zipFileList = new ArrayList<>();
    private long zipFileSize;
    private List<FileInfo> apkFileList = new ArrayList<>();
    private long apkFileSize;

    public List<FileInfo> getAnyFileList() {
        return anyFileList;
    }

    public List<FileInfo> getTxtFileList() {
        return txtFileList;
    }

    public List<FileInfo> getVideoFileList() {
        return videoFileList;
    }

    public List<FileInfo> getAudioFileList() {
        return audioFileList;
    }

    public List<FileInfo> getImageFileList() {
        return imageFileList;
    }

    public List<FileInfo> getZipFileList() {
        return zipFileList;
    }

    public List<FileInfo> getApkFileList() {
        return apkFileList;
    }

    public long getAnyFileSize() {
        Log.d("我走了吗", "setListener:1 " + anyFileSize);
        return anyFileSize;
    }

    public void setAnyFileSize(long anyFileSize) {
        this.anyFileSize = anyFileSize;
        if (this.anyFileSize < 0) {
            this.anyFileSize = 0;
        }
    }

    public long getTxtFileSize() {
        return txtFileSize;
    }

    public void setTxtFileSize(long txtFileSize) {
        this.txtFileSize = txtFileSize;
        if (this.txtFileSize < 0) {
            this.txtFileSize = 0;
        }
    }

    public long getVideoFileSize() {
        return videoFileSize;
    }

    public void setVideoFileSize(long videoFileSize) {
        this.videoFileSize = videoFileSize;
        if (this.videoFileSize < 0) {
            this.videoFileSize = 0;
        }
    }

    public long getAudioFileSize() {
        return audioFileSize;
    }

    public void setAudioFileSize(long audioFileSize) {
        this.audioFileSize = audioFileSize;
        if (this.audioFileSize < 0) {
            this.audioFileSize = 0;
        }
    }

    public long getImageFileSize() {
        return imageFileSize;
    }

    public void setImageFileSize(long imageFileSize) {
        this.imageFileSize = imageFileSize;
        if (this.imageFileSize < 0) {
            this.imageFileSize = 0;
        }
    }

    public long getZipFileSize() {
        return zipFileSize;
    }

    public void setZipFileSize(long zipFileSize) {
        this.zipFileSize = zipFileSize;
        if (this.zipFileSize < 0) {
            this.zipFileSize = 0;
        }
    }

    public long getApkFileSize() {
        return apkFileSize;
    }

    public void setApkFileSize(long apkFileSize) {
        this.apkFileSize = apkFileSize;
        if (this.apkFileSize < 0) {
            this.apkFileSize = 0;
        }
    }

    /**
     * 刷新数据
     */
    private void initData() {
        isStopSearch = false;
        anyFileSize = 0;
        txtFileSize = 0;
        videoFileSize = 0;
        audioFileSize = 0;
        imageFileSize = 0;
        zipFileSize = 0;
        apkFileSize = 0;
        anyFileList.clear();
        txtFileList.clear();
        videoFileList.clear();
        audioFileList.clear();
        imageFileList.clear();
        zipFileList.clear();
        apkFileList.clear();
    }

    /**
     * 搜索存储卡目录下的所有文件,结果实时保存在 {@link #anyFileList}内
     */
    public void searchSDCardFile() {
        Log.d("我走了吗", "setListener:10 " + anyFileSize);
        if (anyFileList == null || anyFileList.size() <= 0) {
            Log.d("我走了吗", "setListener:11 " + anyFileSize);
            initData();
            searchFile(inSDCardDir, false);
            searchFile(outSDCardDir, true);
        } else {
            Log.d("我走了吗", "setListener:12 " + anyFileSize);
            callbackSearchFileListenerEnd(true);
        }
    }

    public void searchFile(File file) {
        initData();
        searchFile(file, true);
    }

    /**
     * 递归搜索所有文件
     *
     * @param file
     * @param isFirstRunFlag 为递归结束判断准备,每次调用方法栈内的flag值都不一样,第一次调用的结束才是真正结束(才会调用反馈结束方法)
     */
    public void searchFile(File file, boolean isFirstRunFlag) {
        Log.d("我走了吗", "setListener:21 " + anyFileSize);
        if (isStopSearch) {
            if (isFirstRunFlag) {
                callbackSearchFileListenerEnd(true);
            }
            Log.d("我走了吗", "setListener:211 " + anyFileSize);
            return;
        }
        if (file == null || !file.canRead() || !file.exists()) {
            if (isFirstRunFlag) {
                callbackSearchFileListenerEnd(true);
            }
            Log.d("我走了吗", "setListener:212 " + anyFileSize);
            return;
        }
        if (!file.isDirectory()) {
            if (file.length() <= 0) {
                Log.d("我走了吗", "setListener:23 " + anyFileSize);
                return;
            }
            if (file.getName().lastIndexOf(".") == -1) {
                Log.d("我走了吗", "setListener:24 " + anyFileSize);
                return;
            }
            Log.d("我走了吗", "setListener:21 5" + anyFileSize);
            String[] iconAndTypeName = FileTypeUtil.getFileIconAndTypeName(file);
            String   iconName        = iconAndTypeName[0];
            String   typeName        = iconAndTypeName[1];
            FileInfo info            = new FileInfo(file, iconName, typeName);
            anyFileList.add(info);
            anyFileSize += file.length();
            if (typeName.equals(FileTypeUtil.TYPE_APK)) {
                Log.d("我走了吗", "setListener:216 " + anyFileSize);
                apkFileList.add(info);
                apkFileSize += file.length();
            } else if (typeName.equals(FileTypeUtil.TYPE_AUDIO)) {
                Log.d("我走了吗", "setListener:217 " + anyFileSize);
                audioFileList.add(info);
                audioFileSize += file.length();
            } else if (typeName.equals(FileTypeUtil.TYPE_IMAGE)) {
                Log.d("我走了吗", "setListener:218 " + anyFileSize);
                imageFileList.add(info);
                imageFileSize += file.length();
            } else if (typeName.equals(FileTypeUtil.TYPE_TXT)) {
                Log.d("我走了吗", "setListener:21 9" + anyFileSize);
                txtFileList.add(info);
                txtFileSize += file.length();
            } else if (typeName.equals(FileTypeUtil.TYPE_VIDEO)) {
                Log.d("我走了吗", "setListener:2110 " + anyFileSize);
                videoFileList.add(info);
                videoFileSize += file.length();
            } else if (typeName.equals(FileTypeUtil.TYPE_ZIP)) {
                Log.d("我走了吗", "setListener:2111 " + anyFileSize);
                zipFileList.add(info);
                zipFileSize += file.length();
            }
            callbackSearchFileListenerSearching(typeName);
            Log.d("我走了吗", "setListener:2112 " + anyFileSize);
            return;
        }
        File[] files = file.listFiles();
        if (files == null || files.length <= 0) {
            Log.d("我走了吗", "setLi000stener:2113 " + files.length);
            return;
        }
        for (File f : files) {
            Log.d("我走了吗", "setListener:214 " + anyFileSize);
            searchFile(f, false);

        }
        if (isFirstRunFlag) {
            Log.d("我走了吗", "setListener:2115 " + anyFileSize);
            callbackSearchFileListenerEnd(false);
        }

    }

    private SearchFileListener mListener;

    public interface SearchFileListener {
        void searching(String typename);

        void end(boolean isExceptionEnd);
    }

    public void setListener(SearchFileListener listener) {
        Log.d("我走了吗", "setListener: " + System.currentTimeMillis());
        mListener = listener;
    }

    private void callbackSearchFileListenerSearching(String typename) {
        if (mListener != null) {
            mListener.searching(typename);
        }
    }

    private void callbackSearchFileListenerEnd(boolean isExceptionEnd) {
        if (mListener != null) {
            mListener.end(isExceptionEnd);
        }
    }

    public static long getFileSize(File file) {
        long size = 0;
        if (!file.isDirectory()) {
            return file.length();
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                size += getFileSize(f);
            } else {
                size += f.length();
            }
        }
        return size;
    }

    public void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null || files.length > 0) {
                for (File f : files) {
                    deleteFile(f);
                }
            }
        }
        file.delete();
    }
}
