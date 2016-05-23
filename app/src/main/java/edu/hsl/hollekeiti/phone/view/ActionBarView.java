package edu.hsl.hollekeiti.phone.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.hsl.hollekeiti.R;


/**
 * Created by Administrator on 2016/05/06.
 */
public class ActionBarView extends LinearLayout {
    public static final String TAG="才才踩踩踩为";
    private ImageView image_left;
    private ImageView image_right;
    private TextView  tv_title;

    public ActionBarView(Context context, AttributeSet attrs) {

        super(context, attrs);
        Log.d(TAG, "ActionBarView: ");
        inflate(context, R.layout.activity_actionbar, this);
        image_left = (ImageView) findViewById(R.id.image_left);
        image_right = (ImageView) findViewById(R.id.image_right);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    public void initActionBar(String title, int leftID, int rightID
            , OnClickListener listener) {
        tv_title.setText(title);
        if (leftID==-1){
            image_left.setVisibility(View.INVISIBLE);
        }else{
            image_left.setImageResource(leftID);
            image_left.setOnClickListener(listener);
        }
        if (rightID==-1){
            image_right.setVisibility(INVISIBLE);
        }else {
            image_right.setImageResource(rightID);
            image_right.setOnClickListener(listener);
        }
    }


}
