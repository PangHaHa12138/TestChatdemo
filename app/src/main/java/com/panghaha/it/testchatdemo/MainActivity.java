package com.panghaha.it.testchatdemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationBar bottomNavigationBar;
    private ContentFragment contentFragment;
    private ContentFragment2 contentFragment2;
    private ContentFragment3 contentFragment3;
    private ContentFragment4 contentFragment4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        setDefaultFragment();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        bottomNavigationBar //值得一提，模式跟背景的设置都要在添加tab前面，不然不会有效果。
                .setActiveColor(R.color.green)//选中颜色 图标和文字
//                .setInActiveColor("#8e8e8e")//默认未选择颜色
                .setBarBackgroundColor("#ECECEC");//默认背景色


        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.weixin_pressed,"微信").setInactiveIconResource(R.drawable.weixin_normal))
                .addItem(new BottomNavigationItem(R.drawable.contact_list_pressed,"通讯录").setInactiveIconResource(R.drawable.contact_list_normal))
                .addItem(new BottomNavigationItem(R.drawable.find_pressed,"发现").setInactiveIconResource(R.drawable.find_normal))
                .addItem(new BottomNavigationItem(R.drawable.profile_pressed,"我").setInactiveIconResource(R.drawable.profile_normal))

//                .addItem(new BottomNavigationItem(R.drawable.ic_favorite_white_24dp,"Like").setActiveColor().setInActiveColor())
//                .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp,"Home").setActiveColor().setInActiveColor())
//                .addItem(new BottomNavigationItem(R.drawable.ic_music_note_white_24dp,"Music").setActiveColor().setInActiveColor())
//                .addItem(new BottomNavigationItem(R.drawable.ic_tv_white_24dp,"TV").setActiveColor().setInActiveColor())
//                .setFirstSelectedPosition(0)//设置默认选择的按钮
                .initialise();//所有的设置需在调用该方法前完成

//        bottomNavigationBar.setAutoHideEnabled(true);

        bottomNavigationBar //设置lab点击事件
                .setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(int position) {
                        FragmentManager fm = getFragmentManager();
                        // 开启Fragment事务
                        FragmentTransaction transaction = fm.beginTransaction();
                        switch (position){


                            case 0:
//                                Toast.makeText(MainActivity.this,"竟让有这种操作！",Toast.LENGTH_SHORT).show();
                                if (contentFragment == null)
                                {
                                    contentFragment = new ContentFragment();
                                }
                                transaction.replace(R.id.content, contentFragment);

                                break;
                            case 1:
//                                Toast.makeText(MainActivity.this,"真的有这种操作！",Toast.LENGTH_SHORT).show();
                                if (contentFragment2 == null)
                                {
                                    contentFragment2 = new ContentFragment2();
                                }
                                transaction.replace(R.id.content, contentFragment2);

                                break;
                            case 2:
//                                Toast.makeText(MainActivity.this,"这是假的操作！",Toast.LENGTH_SHORT).show();
                                if (contentFragment3 == null)
                                {
                                    contentFragment3 = new ContentFragment3();
                                }
                                transaction.replace(R.id.content, contentFragment3);

                                break;
                            case 3:
//                                Toast.makeText(MainActivity.this,"我有一句吗卖批不知当讲不当讲！",Toast.LENGTH_SHORT).show();
                                if (contentFragment4 == null)
                                {
                                    contentFragment4 = new ContentFragment4();
                                }
                                transaction.replace(R.id.content, contentFragment4);

                                break;

                        }

                        transaction.commit();
                    }

                    @Override
                    public void onTabUnselected(int position) {

                    }

                    @Override
                    public void onTabReselected(int position) {

                    }
                });
    }

    private void setDefaultFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        contentFragment = new ContentFragment();
        transaction.replace(R.id.content, contentFragment);
        transaction.commit();
    }
}
