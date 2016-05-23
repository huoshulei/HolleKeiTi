package edu.hsl.hollekeiti.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.base.MyActivity;
import edu.hsl.hollekeiti.phone.db.AppInfoManager;
import edu.hsl.hollekeiti.phone.db.MemoryManager;
import edu.hsl.hollekeiti.phone.view.ClearArcView;

public class HomeActivity extends MyActivity {
    private View         v_yijianqingli;
    private View         v_yijianqinglitext;
    private ClearArcView cav_yijianqingli;
    private TextView     tv_yijianqingli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        setContentView(getLayoutInflater().inflate(R.layout.activity_home,null));
        String title = getResources().getString(R.string.app_name);
        initActionBar(title, R.mipmap.ic_launcher,
                R.mipmap.ic_child_configs, mClickListener);
        initHomeClear();
        initHomeClearData();
    }

    private void initHomeClear() {
        v_yijianqingli = findViewById(R.id.iv_homeclear);
        v_yijianqinglitext = findViewById(R.id.tv_homelear_text);
        tv_yijianqingli = (TextView) findViewById(R.id.tv_score);
        cav_yijianqingli = (ClearArcView) findViewById(R.id.homeclear_arc);
        v_yijianqingli.setOnClickListener(mClickListener);
        v_yijianqinglitext.setOnClickListener(mClickListener);
    }

    /**
     * @see android:onClick=hitHomeitem
     */
    public void hitHomeitem(View v) {
        switch (v.getId()) {
            case R.id.tv_jiasu:
                startActivity(JiasuActivity.class, 0);
                break;
            case R.id.tv_ruanjian:
                startActivity(SoftmgrActivity.class, 0);
                break;
            case R.id.tv_jiance:
                startActivity(PhonemgrActivity.class, 0);
                break;
            case R.id.tv_tongxun:
                startActivity(TongxunActivity.class, 0);
                break;
            case R.id.tv_wenjian:
                startActivity(FilemgrActivity.class, 0);
                break;
            case R.id.tv_laji:
                startActivity(ClearActivity.class, 0);
                break;

        }
    }

    private void initHomeClearData() {
        float totalRam = MemoryManager.getPhoneRamMenry();
        float freeRam  = MemoryManager.getPhoneFreeRamMenry(getApplicationContext());
        float useRam   = totalRam - freeRam;
        float usedP    = useRam / totalRam;
        int   used100  = (int) (usedP * 100);
        tv_yijianqingli.setText(used100 + "");
        int angle = (int) (usedP * 360);
        cav_yijianqingli.setAngleWithAnim(angle);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_homeclear:
                case R.id.tv_homelear_text:
                    AppInfoManager.getAppInfoManager(HomeActivity.this).killAllProcesses();
                    initHomeClearData();
                    break;
                case R.id.image_left:
                    startActivity(AboutActivity.class, 1);
                    break;
                case R.id.image_right:
                    startActivity(SettingActivity.class, 0);
                    break;
            }
        }
    };

}
