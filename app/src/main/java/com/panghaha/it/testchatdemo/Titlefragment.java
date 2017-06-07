package com.panghaha.it.testchatdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

/***
 * ━━━━ Code is far away from ━━━━━━
 * 　　  () 　　　  ()
 * 　　  ( ) 　　　( )
 * 　　  ( ) 　　　( )
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　┻　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━ bug with the more protecting ━━━
 * <p/>
 * Created by PangHaHa12138 on 2017/6/6.
 */
public class Titlefragment extends Fragment {


    private ImageView leftbut,leftbut2,rightbut,rightbut2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.title_fragment,container,false);
        leftbut = (ImageView) view.findViewById(R.id.leftbut);
        leftbut2 = (ImageView) view.findViewById(R.id.leftbut2);
        rightbut = (ImageView) view.findViewById(R.id.rightbut);
        rightbut2 = (ImageView) view.findViewById(R.id.rightbut2);

        leftbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"大娘，让我们离婚吧，感情破裂了！",Toast.LENGTH_SHORT).show();
            }
        });

        leftbut2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"再好好想想，感情还没破裂！",Toast.LENGTH_SHORT).show();
            }
        });
        rightbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"你一个傻老娘们你知道什么！",Toast.LENGTH_SHORT).show();
            }
        });
        rightbut2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"大娘，让我们离婚吧，感情真他妈的破裂了！",Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }
}
