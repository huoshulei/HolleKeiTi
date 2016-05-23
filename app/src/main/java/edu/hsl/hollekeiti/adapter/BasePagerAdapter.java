package edu.hsl.hollekeiti.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/05/05.
 */
public class BasePagerAdapter extends PagerAdapter {
    private Context cContext;
    private List<View> cViewList=new ArrayList<>();
    private List<String> tableList=new ArrayList<>();

    public BasePagerAdapter(Context context) {
        cContext = context;
    }

    public List<View> getViewList() {
        return cViewList;
    }

    public void addViewAdapter(View view) {
        cViewList .add(view);
    }
    public void addTableAdapter(String title) {
        tableList .add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tableList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view=cViewList.get(position);
        container.removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       View view=cViewList.get(position);
        container.addView(view);
        return view;
    }


    @Override
    public int getCount() {
        return cViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
