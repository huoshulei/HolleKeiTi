package edu.hsl.hollekeiti.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.adapter.BasePagerAdapter;
import edu.hsl.hollekeiti.base.MyActivity;
import edu.hsl.hollekeiti.phone.service.MusicService;

public class Kaijiyindao extends MyActivity implements View.OnClickListener {
    public static final String TAG = "蔡蔡蔡蔡伟";
    private ImageView icon1;
    private ImageView icon2;
    private ImageView icon3;
    private TextView  tv_tiaoguo;
    private ViewPager vp_shouye;
    BasePagerAdapter adapter;
    ImageView[] icons = {icon1, icon2, icon3};
    private boolean isFromSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //获得跳转前的Activity的类名
        String fromClassName = intent.getStringExtra("className");
        //如果fromClassName为空并且fromClassName不等于SettingActivity时返回true
        if (fromClassName != null && fromClassName.equals("SettingActivity")) {
            isFromSetting = true;
            Log.d(TAG, "onCreate: 1");
        }
        Log.d(TAG, "onCreate: 2");
        //读取储存的数据
        SharedPreferences preferences = getSharedPreferences("lead_config",
                Context.MODE_PRIVATE);
        //判断是否是第一次运行
        boolean isFirstRun = preferences.getBoolean("isFirstRun", true);
        //如果不是第一次直接跳过进入logo页面
        if (!isFirstRun) {
            Log.d(TAG, "onCreate: 3");
            startActivity(LogoActivity.class);
            finish();
        } else {
            Log.d(TAG, "onCreate: 4");
            //加载当前布局
            setContentView(R.layout.activity_kaijiyindao);
            initLeadIcon();
            initViewPager();
            initPagerData();
//            initAppDBFile();
            Intent intent1=new Intent(this, MusicService.class);
            Log.d(TAG, "onCreate: 44");
            startService(intent1);
        }
        Log.d(TAG, "onCreate: 5");
//        finish();

    }

    /**
     * 关联当前布局图标id
     */
    private void initLeadIcon() {
        Log.d(TAG, "onCreate: 6");
        icons[0] = (ImageView) findViewById(R.id.icon1);
        icons[1] = (ImageView) findViewById(R.id.icon2);
        icons[2] = (ImageView) findViewById(R.id.icon3);
        icons[0].setImageResource(R.mipmap.adware_style_selected);
        tv_tiaoguo = (TextView) findViewById(R.id.tv_tiaoguo);
        //设置当前控件不可见
        tv_tiaoguo.setVisibility(View.INVISIBLE);
        tv_tiaoguo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onCreate: 7");
        savePreferences();
        if (isFromSetting) {
            Log.d(TAG, "onCreate: 8");
            startActivity(SettingActivity.class);
        } else {
            Log.d(TAG, "onCreate: 9");
            startActivity(LogoActivity.class);
        }
        Log.d(TAG, "onCreate: 10");
        Intent intent=new Intent(this,MusicService.class);
        stopService(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            Intent intent=new Intent(this,MusicService.class);
            stopService(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 加载主图片
     */
    private void initViewPager() {
        Log.d(TAG, "onCreate: 11");
        vp_shouye = (ViewPager) findViewById(R.id.vp_shouye);
        adapter = new BasePagerAdapter(this);
        vp_shouye.setAdapter(adapter);
        //左右滑动的方法
        vp_shouye.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //当界面改变时系统调用此方法
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onCreate: 12");
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onCreate: 13");

                //设置文本不可见
                tv_tiaoguo.setVisibility(View.INVISIBLE);
                if (position >= 2) {
                    Log.d(TAG, "onCreate: 14");
                    // 到达最后一个 page 时，显示出 skip 文本
                    tv_tiaoguo.setVisibility(View.VISIBLE);
                }
                //更新下标图标
                for (ImageView i : icons) {
                    Log.d(TAG, "onCreate: 15");
                    i.setImageResource(R.mipmap.adware_style_default);
                }
                //把当前显示的图片对应的下标颜色改变
                icons[position].setImageResource(R.mipmap.adware_style_selected);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onCreate: 16");

            }
        });
    }

    /**
     * 为当前适配器添加图片
     */
    private void initPagerData() {
        Log.d(TAG, "onCreate: 17");
        ImageView image = null;
        image = (ImageView) getLayoutInflater()
                .inflate(R.layout.activity_lead_item, null);
        image.setImageResource(R.mipmap.adware_style_applist);
        adapter.addViewAdapter(image);

        image = (ImageView) getLayoutInflater()
                .inflate(R.layout.activity_lead_item, null);
        image.setImageResource(R.mipmap.adware_style_banner);
        adapter.addViewAdapter(image);
        image = (ImageView) getLayoutInflater()
                .inflate(R.layout.activity_lead_item, null);
        image.setImageResource(R.mipmap.adware_style_creditswall);
        adapter.addViewAdapter(image);
        Log.d(TAG, "onCreate: 18");
        adapter.notifyDataSetChanged();

    }

    /**
     * 此方法记录并保存当前设置
     */
    private void savePreferences() {
        Log.d(TAG, "onCreate: 19");
        SharedPreferences preferences = getSharedPreferences("lead_config",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editer = preferences.edit();
        editer.putBoolean("isFirstRun", false);
        Log.d(TAG, "onCreate: 20");
        editer.commit();
    }
//    private void initAppDBFile() {
//        if (!DBRead.isExistsTeladFile(DBRead.telFile1)) {
//            Log.d(TAG, "onStartCommand:4 "+DBRead.telFile.canRead());
//            Log.d(TAG, "onStartCommand:4 "+DBRead.telFile.exists());
//            Log.d(TAG, "onStartCommand:4 "+DBRead.telFile.isFile());
//            Log.d(TAG, "onStartCommand:4 "+DBRead.telFile.canWrite());
//            AssetsDBManager.copyAssetsFileToFile(this,
//                    "music/om.mp3", DBRead.telFile1);
//        } }
}
