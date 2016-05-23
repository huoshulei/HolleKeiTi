package edu.hsl.hollekeiti.activity;

import android.os.Bundle;
import android.view.View;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.base.MyActivity;

public class AboutActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initActionBar(getResources().getString(R.string.about)
                , R.mipmap.btn_homeasup_default, -1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.image_left:
//                                String fromClassName = getIntent().getStringExtra("className");
//                                if (fromClassName == null || fromClassName == "") {
////                                    startActivity(HomeActivity.class);
//                                    finish();
//                                    return;
//                                }
//                                if (fromClassName.equals(SettingActivity.class.getSimpleName())) {
////                                    startActivity(SettingActivity.class);
//                                } else {
////                                    startActivity(HomeActivity.class);
//                                }
                                finish();
                                break;
                            default:
                                break;
                        }
                    }
                });
    }
}
