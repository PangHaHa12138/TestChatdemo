package com.panghaha.it.testchatdemo.common;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.panghaha.it.testchatdemo.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sj.keyboard.utils.imageloader.ImageBase;

public class MyChattingAdapter extends BaseAdapter {

    private final int VIEW_TYPE_COUNT = 8;
    private final int VIEW_TYPE_LEFT_TEXT = 0;
    private final int VIEW_TYPE_LEFT_IMAGE = 1;
    private final int VIEW_TYPE_RIGTH_TEXT = 2;
    private final int VIEW_TYPE_RIGTH_IMAGE = 3;
    private int istext,ismesend = 1;//记录是否是文字或大表情 默认是发送
    private boolean isimage;
    private Activity mActivity;
    private String myuseridd;
    private LayoutInflater mInflater;
    private List<Data_ReceiverNews.NewsBean> mData;

    private Data_ReceiverNews.NewsBean receiverNewses;

    public MyChattingAdapter(Activity activity,String myuserid) {

        this.mActivity = activity;
        this.myuseridd = myuserid;
        mInflater = LayoutInflater.from(activity);
    }

    public void addData(List<Data_ReceiverNews.NewsBean> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
        }
        for (Data_ReceiverNews.NewsBean bean : data) {
            addData(bean, false, false);
        }
        this.notifyDataSetChanged();
    }

    public void addData(Data_ReceiverNews.NewsBean bean, boolean isNotifyDataSetChanged, boolean isFromHead) {
        if (bean == null) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
        }
            String content = bean.getNewscontent();
            if (content != null) {
                    if (content.indexOf("[img]") >= 0) {
                        bean.setNewscontent(content.replace("[img]", ""));
                        istext = 1;//大表情
                    } else {
                        istext = 0;//文字
                    }

            }

        if (isFromHead) {
            mData.add(0, bean);
        } else {
            mData.add(bean);
        }

        if (isNotifyDataSetChanged) {
            this.notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) == null) {
            return -1;
        }
        if (mData.get(position).getUserid()!= null&& mData.get(position).getUserid().equals(myuseridd)||ismesend == 1){//发送
            if (istext == 0){//文字
                return VIEW_TYPE_RIGTH_TEXT;
            }else if (istext == 1){//表情
                return VIEW_TYPE_RIGTH_IMAGE;
            }
        }else if (mData.get(position).getUserid()!= null&& !mData.get(position).getUserid().equals(myuseridd)){//接收
            if (istext == 0){ //文字
                return VIEW_TYPE_LEFT_TEXT;
            }else if (istext == 1){ //大表情
                return VIEW_TYPE_LEFT_IMAGE;
            }
        }
        return -1;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Data_ReceiverNews.NewsBean bean = mData.get(position);
        int type = getItemViewType(position);
        View holderView = null;

            if (type == VIEW_TYPE_RIGTH_TEXT){
                ViewHolderRightText rightholder;
                if (convertView == null) {
                    rightholder = new ViewHolderRightText();
                    holderView = mInflater.inflate(R.layout.listitem_cha_right_text, null);
                    holderView.setFocusable(true);
                    rightholder.iv_avatar = (CircleImageView) holderView.findViewById(R.id.iv_avatar);
                    rightholder.tv_content = (TextView) holderView.findViewById(R.id.tv_content);
                    rightholder.sendtime = (TextView) holderView.findViewById(R.id.sendtime);
                    rightholder.name = (TextView) holderView.findViewById(R.id.chatname);
//                    LogUtil.d("从这走了---","初始化找id");
                    holderView.setTag(rightholder);
                    convertView = holderView;
                } else {
                    rightholder = (ViewHolderRightText) convertView.getTag();
                }
                rightholder.sendtime.setText(bean.getCreatime());
                rightholder.name.setText(bean.getUserName());
                Glide.with(mActivity).load(bean.getPath()).into(rightholder.iv_avatar);
                disPlayRightTextView(position, convertView, rightholder, bean);
            }
        if (type == VIEW_TYPE_LEFT_TEXT){
            ViewHolderLeftText leftholder;
            if (convertView == null) {
                leftholder = new ViewHolderLeftText();
                holderView = mInflater.inflate(R.layout.listitem_cha_left_text, null);
                holderView.setFocusable(true);
                leftholder.iv_avatar = (CircleImageView) holderView.findViewById(R.id.iv_avatar);
                leftholder.tv_content = (TextView) holderView.findViewById(R.id.tv_content);
                leftholder.sendtime = (TextView) holderView.findViewById(R.id.sendtime);
                leftholder.name = (TextView) holderView.findViewById(R.id.chatname);
                holderView.setTag(leftholder);
                convertView = holderView;
            } else {
                leftholder = (ViewHolderLeftText) convertView.getTag();
            }
            Glide.with(mActivity).load(bean.getPath()).into(leftholder.iv_avatar);
            leftholder.name.setText(bean.getUserName());
            leftholder.sendtime.setText(bean.getCreatime());
            disPlayLeftTextView(position, convertView, leftholder, bean);
        }
        if (type == VIEW_TYPE_RIGTH_IMAGE){
            ViewHolderRightImage imageHolder;
            if (convertView == null) {
                imageHolder = new ViewHolderRightImage();
                holderView = mInflater.inflate(R.layout.listitem_chat_right_image, null);
                holderView.setFocusable(true);
                imageHolder.iv_avatar = (CircleImageView) holderView.findViewById(R.id.iv_avatar);
                imageHolder.iv_image = (ImageView) holderView.findViewById(R.id.iv_image);
                imageHolder.sendtime = (TextView) holderView.findViewById(R.id.sendtime);
                imageHolder.sendtime = (TextView) holderView.findViewById(R.id.chatname);
//                LogUtil.d("从这走了---","初始化找id");

                holderView.setTag(imageHolder);
                convertView = holderView;
            } else {
                imageHolder = (ViewHolderRightImage) convertView.getTag();
            }
            Glide.with(mActivity).load(bean.getPath()).into(imageHolder.iv_avatar);
            imageHolder.name.setText(bean.getUserName());
            imageHolder.sendtime.setText(bean.getCreatime());
            disPlayRightImageView(position, convertView, imageHolder, bean);
        }
        if (type == VIEW_TYPE_LEFT_IMAGE){
            ViewHolderLeftImage imageHolder2;
            if (convertView == null) {
                imageHolder2 = new ViewHolderLeftImage();
                holderView = mInflater.inflate(R.layout.listitem_chat_left_image, null);
                holderView.setFocusable(true);
                imageHolder2.iv_avatar = (CircleImageView) holderView.findViewById(R.id.iv_avatar);
                imageHolder2.iv_image = (ImageView) holderView.findViewById(R.id.iv_image);
                imageHolder2.sendtime = (TextView) holderView.findViewById(R.id.sendtime);
                imageHolder2.name = (TextView) holderView.findViewById(R.id.chatname);
                holderView.setTag(imageHolder2);
                convertView = holderView;
            } else {
                imageHolder2 = (ViewHolderLeftImage) convertView.getTag();
            }
            Glide.with(mActivity).load(bean.getPath()).into(imageHolder2.iv_avatar);
            imageHolder2.name.setText(bean.getUserName());
            imageHolder2.sendtime.setText(bean.getCreatime());
            disPlayLeftImageView(position, convertView, imageHolder2, bean);
        }


        convertView = new View(mActivity);


        return convertView;
    }

    public void disPlayLeftTextView(int position, View view, ViewHolderLeftText holder, Data_ReceiverNews.NewsBean bean) {
        setContent(holder.tv_content, bean.getNewscontent());
    }

    public void setContent(TextView tv_content, String content) {
        SimpleCommonUtils.spannableEmoticonFilter(tv_content, content);
    }

    public void disPlayLeftImageView(int position, View view, ViewHolderLeftImage holder, Data_ReceiverNews.NewsBean bean) {
        try {
            if (ImageBase.Scheme.FILE == ImageBase.Scheme.ofUri(bean.getNewscontent())) {
                String filePath = ImageBase.Scheme.FILE.crop(bean.getNewscontent());
                Glide.with(holder.iv_image.getContext())
                        .load(filePath)
                        .into(holder.iv_image);
            } else {
                ImageLoadUtils.getInstance(mActivity).displayImage(bean.getNewscontent(), holder.iv_image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disPlayRightTextView(int position, View view, ViewHolderRightText holder, Data_ReceiverNews.NewsBean bean) {
        setContent2(holder.tv_content, bean.getNewscontent());
    }

    public void setContent2(TextView tv_content, String content) {
        SimpleCommonUtils.spannableEmoticonFilter(tv_content, content);
    }

    public void disPlayRightImageView(int position, View view, ViewHolderRightImage holder, Data_ReceiverNews.NewsBean bean) {
        try {
            if (ImageBase.Scheme.FILE == ImageBase.Scheme.ofUri(bean.getNewscontent())) {
                String filePath = ImageBase.Scheme.FILE.crop(bean.getNewscontent());
                Glide.with(holder.iv_image.getContext())
                        .load(filePath)
                        .into(holder.iv_image);
            } else {
                ImageLoadUtils.getInstance(mActivity).displayImage(bean.getNewscontent(), holder.iv_image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public final class ViewHolderLeftText {
        public CircleImageView iv_avatar;
        public TextView tv_content;
        public TextView sendtime;
        public TextView name;
    }

    public final class ViewHolderLeftImage {
        public CircleImageView iv_avatar;
        public ImageView iv_image;
        public TextView sendtime;
        public TextView name;
    }
    public final class ViewHolderRightText {
        public CircleImageView iv_avatar;
        public TextView tv_content;
        public TextView sendtime;
        public TextView name;
    }

    public final class ViewHolderRightImage {
        public CircleImageView iv_avatar;
        public ImageView iv_image;
        public TextView sendtime;
        public TextView name;
    }
}