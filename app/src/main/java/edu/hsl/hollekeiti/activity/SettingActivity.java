package edu.hsl.hollekeiti.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.base.MyActivity;
import edu.hsl.hollekeiti.phone.util.NotificationUtil;

public class SettingActivity extends MyActivity {
    public static final String TAG = "才才踩踩踩为";
    private ToggleButton tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initActionBar(getResources().getString(R.string.app_name),
                R.mipmap.btn_homeasup_default, -1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.image_left:
//                                startActivity(HomeActivity.class, 0);
                                finish();
                                break;
                        }
                    }
                });
        initMainButton();
    }

    private void initMainButton() {
        tb = (ToggleButton) findViewById(R.id.tb_toggle_notification);
        Log.d(TAG, "initMainButton:1 "+(getApplicationContext()));
        Log.d(TAG, "initMainButton: 2"+(NotificationUtil.isOpenNotification(getApplicationContext())));
        tb.setChecked(NotificationUtil.isOpenNotification(getApplicationContext()));
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d(TAG, "onCheckedChanged: 00000000000000000000000000000000000000");
                    NotificationUtil.showAppIconNotification(getApplicationContext());
                } else {
                    NotificationUtil.cancelAppIconNotification(getApplicationContext());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationUtil.setOpenNotification(getApplicationContext(),tb.isChecked());
    }
    public void hitSettingitem(View v){
        switch (v.getId()){
            case R.id.rl_setting_about:
                Bundle bundle=new Bundle();
                bundle.putString("className",SettingActivity.this.getClass().getSimpleName());
                startActivity(AboutActivity.class,bundle,0);
                break;
            case R.id.rl_setting_help:
                SharedPreferences preferences=getSharedPreferences("lead_config", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("isFirstRun",true);
                editor.commit();
                Bundle bundle1=new Bundle();
                bundle1.putString("className",SettingActivity.this.getClass().getSimpleName());
                startActivity(Kaijiyindao.class,bundle1,0);
                break;
            default:
                break;
        }
    }
}
