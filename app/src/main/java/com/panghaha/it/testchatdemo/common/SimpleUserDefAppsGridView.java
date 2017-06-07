package com.panghaha.it.testchatdemo.common;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.GridView;


import com.panghaha.it.testchatdemo.R;

import java.util.ArrayList;

public class SimpleUserDefAppsGridView extends SimpleAppsGridView {

    public SimpleUserDefAppsGridView(Context context) {
        super(context);
    }

    public SimpleUserDefAppsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void init(){
        GridView gv_apps = (GridView) view.findViewById(R.id.gv_apps);
        gv_apps.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv_apps.setNumColumns(2);
        ArrayList<AppBean> mAppBeanList = new ArrayList<>();
        mAppBeanList.add(new AppBean(R.drawable.chatting_photo, "图片"));
        mAppBeanList.add(new AppBean(R.drawable.chatting_camera, "拍照"));
        ChattingAppsAdapter adapter = new ChattingAppsAdapter(getContext(), mAppBeanList);
        gv_apps.setAdapter(adapter);
    }
}
