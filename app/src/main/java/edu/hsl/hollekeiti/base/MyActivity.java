package edu.hsl.hollekeiti.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.phone.view.ActionBarView;

/**
 * Created by Administrator on 2016/05/06.
 */
public class MyActivity extends AppCompatActivity {
    private static List<MyActivity> myActivity = new ArrayList<>();

    public static void finishAll() {
        Iterator<MyActivity> iterator = myActivity.iterator();
        if (iterator.hasNext()) {
            iterator.next().finish();
        }
    }

    /**
     * 初始化ActionBar
     */
    protected void initActionBar(String title, int leftID, int rightID
            , View.OnClickListener listener) {
        try {
            ActionBarView barView = (ActionBarView) findViewById(R.id.action_bar);
            barView.initActionBar(title, leftID, rightID, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void startActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    protected void startActivity(Class<?> activity, Bundle bundle) {
        Intent intent = new Intent(this, activity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 切换Activity动画效果
     * overridePendingTransition(R.anim.fade, R.anim.hold);//淡入淡出
     * overridePendingTransition(R.anim.my_scale_action,R.anim.my_alpha_action);//放大淡出
     * overridePendingTransition(R.anim.scale_rotate,R.anim.my_alpha_action);//转动淡出1
     * overridePendingTransition(R.anim.scale_translate_rotate,R.anim.my_alpha_action);//转动淡出2
     * overridePendingTransition(R.anim.scale_translate,R.anim.my_alpha_action);//左上角展开淡出效果
     * overridePendingTransition(R.anim.hyperspace_in,R.anim.hyperspace_out);//压缩变小淡出效果
     * overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);//右往左推出效果
     * overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);//下往上推出效果
     * overridePendingTransition(R.anim.slide_left,R.anim.slide_right);//左右交叉效果
     * overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);//缩小效果
     * overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);//上下文交错
     */
    int[]  i       = {R.anim.fade, R.anim.my_scale_action, R.anim.scale_rotate, R.anim.scale_translate_rotate,
            R.anim.scale_translate, R.anim.hyperspace_in, R.anim.push_left_in, R.anim.push_up_in,
            R.anim.slide_left, R.anim.zoom_enter, R.anim.slide_up_in};
    int[]  j       = {R.anim.hold, R.anim.my_alpha_action, R.anim.my_alpha_action, R.anim.my_alpha_action
            , R.anim.my_alpha_action, R.anim.hyperspace_out, R.anim.push_left_out, R.anim.push_up_out
            , R.anim.slide_right, R.anim.zoom_exit, R.anim.slide_down_out};
    Random mRandom = new Random();


    protected void startActivity(Class<?> activity, int in) {
        int ran = mRandom.nextInt(i.length);
        startActivity(activity);
        overridePendingTransition(i[ran], j[ran]);
    }

    protected void startActivity(Class<?> activity, Bundle bundle, int in) {
        int ran = mRandom.nextInt(i.length);
        startActivity(activity, bundle);
        overridePendingTransition(i[ran], j[ran]);
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     * 不明所以
     */
    protected Handler handler = new Handler() {
        public void handleMessage(Message m) {
            myHandleMessage(m);
        }
    };

    protected void myHandleMessage(Message m) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myActivity.remove(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
