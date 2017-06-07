package com.panghaha.it.testchatdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.panghaha.it.testchatdemo.common.ChattingListAdapter;
import com.panghaha.it.testchatdemo.common.Constants;
import com.panghaha.it.testchatdemo.common.Data_ReceiverNews;
import com.panghaha.it.testchatdemo.common.NyChattingListAdapter;
import com.panghaha.it.testchatdemo.common.SimpleCommonUtils;
import com.panghaha.it.testchatdemo.common.SimpleUserDefAppsGridView;
import com.panghaha.it.testchatdemo.common.SimpleUserdefEmoticonsKeyBoard;
import com.sj.emoji.EmojiBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import sj.keyboard.data.EmoticonEntity;
import sj.keyboard.interfaces.EmoticonClickListener;
import sj.keyboard.widget.EmoticonsEditText;
import sj.keyboard.widget.FuncLayout;

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
 * Created by PangHaHa12138 on 2017/6/7.
 */
public class ActivityChat extends AppCompatActivity implements FuncLayout.OnFuncKeyBoardListener {
    private ListView lvChat;
    private SimpleUserdefEmoticonsKeyBoard ekBar;
    private ChattingListAdapter chattingListAdapter;
    //    private MyChattingAdapter myChattingAdapter;
    private NyChattingListAdapter myChattingAdapter;
    private Toolbar toolbar;
    private TextView title;
    private ImageView addview;
    private String userid,taskid;
    private Data_ReceiverNews.NewsBean newsBean;
    private Data_ReceiverNews data_receiverNews;
    private List<Data_ReceiverNews.NewsBean> newsBeanList = new ArrayList<Data_ReceiverNews.NewsBean>();
    private List<Data_ReceiverNews.NewsBean> newsBeanListshort= new ArrayList<Data_ReceiverNews.NewsBean>();
    private int allcount;
    private SwipeRefreshLayout uploadmore;
    private View footview;
    private int footerHeight;
    private boolean isshort;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitychat);
//        userid = SharedPreferencesUtil.readUserid(Activity_Chat.this);
//        Intent intent = getIntent();
//        taskid = intent.getStringExtra("taskidchat");
        //这里参数我是为了测试写死的，正常可以通过intent或者读取缓存传值
        userid = "02774bc536964386a68bd2b64145c910";
        taskid = "eb05f06c46dd4acd87e0bef85575f981";
        initview();
    }

    private void initview() {
        lvChat = (ListView) findViewById(R.id.lv_chat);
        ekBar = (SimpleUserdefEmoticonsKeyBoard) findViewById(R.id.keyboard);
        uploadmore = (SwipeRefreshLayout) findViewById(R.id.uploadmore);
        toolbar = (Toolbar) findViewById(R.id.toobaraaa);
        setSupportActionBar(toolbar);
        footview = View.inflate(ActivityChat.this,R.layout.chat_footer,null);
        footview.measure(0,0);
        footerHeight = footview.getMeasuredHeight();
        footview.setPadding(0,-footerHeight,0,0);
        lvChat.addFooterView(footview);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        showBack();
        initEmoticonsKeyBoardBar();
        initListView();
        uploadmore.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        //上拉加载历史记录
        uploadmore.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpUtils.get(Http_Api.URL_NewReceiver)
                                    .params("userid",userid)
                                    .params("taskid",taskid)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
//                                            LogUtil.d("返回值",s);
                                            data_receiverNews = JsonUtil.parseJsonToBean(s,Data_ReceiverNews.class);
                                            allcount = Integer.parseInt(data_receiverNews.getResult());
                                            if (allcount != 0){

                                                if (newsBeanList!=null){
                                                    newsBeanList.clear();
                                                }
                                                newsBeanList = data_receiverNews.getNews();
                                                myChattingAdapter = new NyChattingListAdapter(ActivityChat.this,userid,newsBeanList);
                                                lvChat.setAdapter(myChattingAdapter);
                                                myChattingAdapter.notifyDataSetChanged();
                                                uploadmore.setRefreshing(false);
                                            }else {
//                                                ToastUtil.showToast("对不起，没有更多消息了");
                                                Toast.makeText(ActivityChat.this,"对不起没有更多消息了",Toast.LENGTH_SHORT).show();
                                                uploadmore.setRefreshing(false);
                                            }

                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },2000);


            }
        });
    }

    private void initEmoticonsKeyBoardBar() {
        SimpleCommonUtils.initEmoticonsEditText(ekBar.getEtChat());
        ekBar.setAdapter(SimpleCommonUtils.getCommonAdapter(this, emoticonClickListener));
        ekBar.addOnFuncKeyBoardListener(this);
        ekBar.addFuncView(new SimpleUserDefAppsGridView(this));
//        ekBar.addFuncView(new SimpleAppsGridView(this));

        ekBar.getEtChat().setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int w, int h, int oldw, int oldh) {
                scrollToBottom();
            }
        });
        ekBar.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSendBtnClick(ekBar.getEtChat().getText().toString());
                ekBar.getEtChat().setText("");
            }
        });
        ekBar.getBtnVoice().setLongClickable(true);
        ekBar.getBtnVoice().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                ToastUtil.showToast("功能未完善");
                return false;
            }
        });
        ekBar.getBtnVoice().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtil.showToast("功能未完善");
            }
        });

//        ekBar.getEmoticonsToolBarView().addFixedToolItemView(false, R.drawable.icon_add, null, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Activity_Chat.this, "ADD", Toast.LENGTH_SHORT).show();
//            }
//        });

//        ekBar.getEmoticonsToolBarView().addToolItemView(R.drawable.icon_setting, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Activity_Chat.this, "SETTING", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    //表情点击事件
    EmoticonClickListener emoticonClickListener = new EmoticonClickListener() {
        @Override
        public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {

            if (isDelBtn) {
                SimpleCommonUtils.delClick(ekBar.getEtChat());
            } else {
                if(o == null){
                    return;
                }
                if(actionType == Constants.EMOTICON_CLICK_BIGIMAGE){
                    if(o instanceof EmoticonEntity){
                        OnSendImage(((EmoticonEntity)o).getIconUri());
                    }
                } else {
                    String content = null;
                    if(o instanceof EmojiBean){
                        content = ((EmojiBean)o).emoji;
                    } else if(o instanceof EmoticonEntity){
                        content = ((EmoticonEntity)o).getContent();
                    }

                    if(TextUtils.isEmpty(content)){
                        return;
                    }
                    int index = ekBar.getEtChat().getSelectionStart();
                    Editable editable = ekBar.getEtChat().getText();
                    editable.insert(index, content);
                }
            }
        }
    };

    private void initListView() {

        ShowNow();

        uploadmoreadd();


    }
    //显示界面的联网操作
    private void ShowNow() {
        try {
            OkHttpUtils.get(Http_Api.URL_NewReceiver)
                    .params("userid",userid)
                    .params("taskid",taskid)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
//                            LogUtil.d("返回值",s);
                            data_receiverNews = JsonUtil.parseJsonToBean(s,Data_ReceiverNews.class);
                            allcount = Integer.parseInt(data_receiverNews.getResult());
                            if (data_receiverNews.getNews()!=null){
                                newsBeanList = data_receiverNews.getNews();
                            }

//                            LogUtil.d("集合:newsBeanList--",newsBeanList+"");
                            if (allcount>28){
                                //设置最多显示28条数据也就是4页，然后下拉加载历史数据，如果不够28条数据有多少展示多少
                                newsBeanListshort = newsBeanList.subList(newsBeanList.size()-28,newsBeanList.size());

                                myChattingAdapter = new NyChattingListAdapter(ActivityChat.this,userid,newsBeanListshort);
                                isshort = true;
                            }else {
                                myChattingAdapter = new NyChattingListAdapter(ActivityChat.this,userid,newsBeanList);
                                isshort = false;
                            }

                            lvChat.setAdapter(myChattingAdapter);


                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //上拉加载最新数据
    private void uploadmoreadd() {
        lvChat.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE){
                    ekBar.reset();
                    int lastposition = lvChat.getLastVisiblePosition();//最后一个item的位置
                    if (lastposition == lvChat.getCount() - 1){
                        footview.setPadding(0,0,0,footerHeight);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    OkHttpUtils.get(Http_Api.URL_NewReceiver)
                                            .params("userid",userid)
                                            .params("taskid",taskid)
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
//                                                    LogUtil.d("返回值",s);
                                                    data_receiverNews = JsonUtil.parseJsonToBean(s,Data_ReceiverNews.class);
                                                    allcount = Integer.parseInt(data_receiverNews.getResult());
                                                    if (newsBeanList!=null){
                                                        newsBeanList.clear();
                                                    }
                                                    newsBeanList = data_receiverNews.getNews();

                                                    if (allcount>28){
                                                        newsBeanListshort = newsBeanList.subList(newsBeanList.size()-28,newsBeanList.size());

                                                        myChattingAdapter = new NyChattingListAdapter(ActivityChat.this,userid,newsBeanListshort);
                                                        isshort = true;
                                                    }else {
                                                        myChattingAdapter = new NyChattingListAdapter(ActivityChat.this,userid,newsBeanList);
                                                        isshort = false;
                                                    }

                                                    lvChat.setAdapter(myChattingAdapter);
                                                    myChattingAdapter.notifyDataSetChanged();
                                                    Toast.makeText(ActivityChat.this,"加载完成",Toast.LENGTH_SHORT).show();
//                                                    ToastUtil.showToast("加载完成");

                                                }
                                            });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                footview.setPadding(0,-footerHeight,0,0);

                            }
                        },2000);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                boolean enable = false;
//                if(lvChat != null && lvChat.getChildCount() > 0){
//                    // 检查列表的第一个项目是否可见
//                    boolean firstItemVisible = lvChat.getFirstVisiblePosition() == 0;
//                    // 检查第一个项目的顶部是否可见
//                    boolean topOfFirstItemVisible = lvChat.getChildAt(0).getTop() == 0;
//                    // 启用或禁用刷新布局
//                    enable = firstItemVisible && topOfFirstItemVisible;
//                }
//                uploadmore.setRefreshing(enable);
            }
        });
    }

    //用当前时间
    private String getTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm ss");
        return dateFormat.format(date);
    }
    //发送信息联网
    private void OnSendBtnClick(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            final Data_ReceiverNews.NewsBean bean = new Data_ReceiverNews.NewsBean();
            bean.setNewscontent(msg);
            bean.setCreatime(getTime());
//            LogUtil.d("------Emoji:",msg);
            try {
                OkHttpUtils.post(Http_Api.URL_NewSend)
                        .params("taskid",taskid)
                        .params("userid",userid)
                        .params("newscontent",msg)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
//                                LogUtil.d("返回值",s);
                                Data_uploadBack_tag back_tag = JsonUtil.parseJsonToBean(s,Data_uploadBack_tag.class);
                                if(back_tag.getResult().equals("0")){
//                                    ToastUtil.showToast("非法的表情符号！");
                                    Toast.makeText(ActivityChat.this,"非法的表情符号!",Toast.LENGTH_SHORT).show();
                                }else if (back_tag.getResult().equals("1")){
//                                    if (isshort){
//                                        if (bean!= null){
//                                            newsBeanListshort.add(bean);
//                                        }
//
//                                    }else {
//                                        if (bean!=null){
//                                            newsBeanList.add(bean);
//                                        }
//                                    }
//                                    myChattingAdapter.setsendtype(1);
                                    ShowNow();//这里选择重新联网刷新列表，而不是在集合里手动添加数据
                                    myChattingAdapter.notifyDataSetChanged();
                                    scrollToBottom();
                                }
                            }

                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 版本号小于21的后退按钮图片
     */
    private void showBack(){
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void OnSendImage(String image) {
        if (!TextUtils.isEmpty(image)) {
            OnSendBtnClick("[img]" + image);//给大图片加标记
        }
    }

    private void scrollToBottom() {//设置滚动到底部
        lvChat.requestLayout();
        lvChat.post(new Runnable() {
            @Override
            public void run() {
                lvChat.setSelection(lvChat.getBottom()+2);
            }
        });
    }

    @Override
    public void OnFuncPop(int i) {
        scrollToBottom();
    }

    @Override
    public void OnFuncClose() {

    }
    @Override
    protected void onPause() {
        super.onPause();
        ekBar.reset();
    }
}
