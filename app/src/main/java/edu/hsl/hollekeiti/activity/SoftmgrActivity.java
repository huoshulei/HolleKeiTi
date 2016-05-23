package edu.hsl.hollekeiti.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.base.MyActivity;
import edu.hsl.hollekeiti.phone.db.MemoryManager;
import edu.hsl.hollekeiti.phone.util.CommonUtil;
import edu.hsl.hollekeiti.phone.view.PiechartView;

public class SoftmgrActivity extends MyActivity {
    private PiechartView piechartView;
    private ProgressBar  phoneBar;
    private ProgressBar  sdCardBar;
    private TextView     phoneSpaceMsg;
    private TextView     sdCardSpaceMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_softmgr);
        initActionBar(getResources().getString(R.string.app_name),
                R.mipmap.btn_homeasup_default, -1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.image_left:
                                finish();
                                break;
                        }
                    }
                });
        initMainButton();
        initSpace();
        initSpaceData();

    }

    public void hitListitem(View v) {
        switch (v.getId()) {
            case R.id.rl_soft_all:
            case R.id.rl_soft_sys:
            case R.id.rl_soft_use:
                Bundle bundle = new Bundle();
                bundle.putInt("id", v.getId());
                startActivity(SoftmgrAppshowActivity.class, bundle, 0);
                break;
            default:
                break;
        }
    }

    private void initSpaceData() {
        //        手机自身空间
        long phoneSelfTolat  = MemoryManager.getPhoneSelSize();//总空间
        long phoneSelfUnused = MemoryManager.getPhoneSelFreeSize();//空闲空间
        long phoneSelfUsed   = phoneSelfTolat - phoneSelfUnused;//已用空间
//        手机自带SD卡空间
        long phoneSelfSDCardTolat  = MemoryManager.getPhoneSelSDCardSize();//总空间
        long phoneSelfSDCardUnused = MemoryManager.getPhoneSelSDCardFreeSize();//空闲空间
        long phoneSelfSDCardUsed   = phoneSelfSDCardTolat - phoneSelfSDCardUnused;//已用空间
//        手机外置存储卡空间
        long phoneOutSDCardTolat  = MemoryManager.getPhoneOutSDCardSize(getApplicationContext());//总空间
        long phoneOutSDCardUnused = MemoryManager.getPhoneOutSDCardFreeSize(getApplicationContext());//空闲空间
        long phoneOutSDCardUsed   = phoneOutSDCardTolat - phoneOutSDCardUnused;//已用空间
//       手机总空间
        float phoneAllSpace = phoneSelfUsed + phoneSelfSDCardUsed + phoneOutSDCardUsed;//手机全部空间
//        计算比例
        float phoneSpaceF  = (phoneSelfSDCardUsed + phoneSelfUsed) / phoneAllSpace;//手机已用比例
        float phoneSDCardF = phoneOutSDCardUsed / phoneAllSpace;//SD卡已用比例
//        保留小数点后两位
        DecimalFormat df = new DecimalFormat("#.00");
        phoneSpaceF = Float.parseFloat(df.format(phoneSpaceF));
        phoneSDCardF = Float.parseFloat(df.format(phoneSDCardF));
//        设置饼形图比例
        piechartView.setPiecharProportionWithAnim(phoneSpaceF, phoneSDCardF);
//        设置手机和存储卡内存和已使用内存
        long phoneTolatSpace = phoneSelfTolat + phoneSelfSDCardTolat;//手机自带总空间
//        phoneTolatSpace=MemoryManager.getPhoneAllSize();
        long phoneUnusedSpace = phoneSelfUnused + phoneSelfSDCardUnused;//手机自带空闲空间
//        phoneUnusedSpace=MemoryManager.getPhoneAllFreeSize();
        long phoneUsedSpace = phoneTolatSpace - phoneUnusedSpace;//手机自带已用空间
//        设置空间使用情况文本:可用/全部
        phoneSpaceMsg.setText("可用:" + CommonUtil.getFileSize(phoneUnusedSpace)
                + "/" + CommonUtil.getFileSize(phoneTolatSpace));
        sdCardSpaceMsg.setText("可用" + CommonUtil.getFileSize(phoneOutSDCardUnused)
                + "/" + CommonUtil.getFileSize(phoneOutSDCardTolat));
//        设置空间使用进度:已用/全部
        phoneBar.setMax((int) (phoneTolatSpace / 1024));
        phoneBar.setProgress((int) (phoneUsedSpace / 1024));
        sdCardBar.setMax((int) (phoneOutSDCardTolat / 1024));
        sdCardBar.setProgress((int) (phoneOutSDCardUsed / 1024));
    }

    private void initSpace() {
        piechartView = (PiechartView) findViewById(R.id.piechart);
        phoneBar = (ProgressBar) findViewById(R.id.pb_phone_space);
        sdCardBar = (ProgressBar) findViewById(R.id.pb_sdcard_space);
        phoneSpaceMsg = (TextView) findViewById(R.id.tv_phone_space_msg);
        sdCardSpaceMsg = (TextView) findViewById(R.id.tv_sdcard_space_msg);
    }

    private void initMainButton() {

    }
}
