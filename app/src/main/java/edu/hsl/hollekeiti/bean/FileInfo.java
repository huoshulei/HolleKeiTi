package edu.hsl.hollekeiti.bean;

import java.io.File;

/**
 * Created by Administrator on 2016/05/11.
 */
public class FileInfo {
    private File    mFile;
    private boolean isSelect;
    private String  iconName;
    private String  fileType;

    public FileInfo(File file, String iconName, String fileType) {
        mFile = file;
        this.iconName = iconName;
        this.fileType = fileType;
        isSelect = false;
    }

    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        mFile = file;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
