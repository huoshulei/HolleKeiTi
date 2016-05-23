package edu.hsl.hollekeiti.bean;

/**
 * Created by Administrator on 2016/04/26.
 */
public class TelClassInfo {
    public String tv_table;
    public String tv_name;
    public String tv_tel;
    public int    im_logo;
    public int    idx;


    public TelClassInfo(String tv_name, String tv_tel, int im_logo) {
        this.tv_name = tv_name;
        this.tv_tel = tv_tel;
        this.im_logo = im_logo;
    }


    public TelClassInfo(String tv_table, int idx) {
        this.tv_table = tv_table;
        this.idx = idx;
    }
}
