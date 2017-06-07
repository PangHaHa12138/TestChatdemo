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

public class ChattingListAdapter extends BaseAdapter {

    private final int VIEW_TYPE_COUNT = 8;
    private final int VIEW_TYPE_LEFT_TEXT = 0;
    private final int VIEW_TYPE_LEFT_IMAGE = 1;
    private final int VIEW_TYPE_RIGTH_TEXT = 2;
    private final int VIEW_TYPE_RIGTH_IMAGE = 3;

    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<ImMsgBean> mData;
    private List<Data_ReceiverNews.NewsBean> receiverNewseslist;
    private Data_ReceiverNews.NewsBean receiverNewses;

    public ChattingListAdapter(Activity activity) {
        this.mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    public void addData(List<ImMsgBean> data,int isMesend) {
        if (data == null || data.size() == 0) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
        }
        for (ImMsgBean bean : data) {
            addData(bean, false, false,0);
        }
        this.notifyDataSetChanged();
    }

    public void addData(ImMsgBean bean, boolean isNotifyDataSetChanged, boolean isFromHead,int isMesend) {
        if (bean == null) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
        }

        if (bean.getMsgType() <= 0) {
            String content = bean.getContent();
            if (content != null) {
                if (isMesend == 1){ //发送在右边
                    bean.setSenderType(ImMsgBean.CHAT_SENDER_ME);
                    if (content.indexOf("[img]") >= 0) {
                        bean.setImage(content.replace("[img]", ""));
                        bean.setMsgType(ImMsgBean.CHAT_MSGTYPE_IMG);
                    } else {
                        bean.setMsgType(ImMsgBean.CHAT_MSGTYPE_TEXT);
                    }
                }else if (isMesend == 0){//左边接收
                    bean.setSenderType(ImMsgBean.CHAT_SENDER_OTHER);
                    if (content.indexOf("[img]") >= 0) {
                        bean.setImage(content.replace("[img]", ""));
                        bean.setMsgType(ImMsgBean.CHAT_MSGTYPE_IMG);
                    } else {
                        bean.setMsgType(ImMsgBean.CHAT_MSGTYPE_TEXT);
                    }
                }

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
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) == null) {
            return -1;
        }
        if (mData.get(position).getSenderType() == ImMsgBean.CHAT_SENDER_ME){
            if (mData.get(position).getMsgType() == ImMsgBean.CHAT_MSGTYPE_TEXT){
                return VIEW_TYPE_RIGTH_TEXT;
            }else if (mData.get(position).getMsgType() == ImMsgBean.CHAT_MSGTYPE_IMG){
                return VIEW_TYPE_RIGTH_IMAGE;
            }
        }else if (mData.get(position).getSenderType() == ImMsgBean.CHAT_SENDER_OTHER){
            if (mData.get(position).getMsgType() == ImMsgBean.CHAT_MSGTYPE_TEXT){
                return VIEW_TYPE_LEFT_TEXT;
            }else if (mData.get(position).getMsgType() == ImMsgBean.CHAT_MSGTYPE_IMG){
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
        final ImMsgBean bean = mData.get(position);
        int type = getItemViewType(position);
        View holderView = null;
        switch (type) {
            case VIEW_TYPE_RIGTH_TEXT:
                ViewHolderRightText rightholder;
                if (convertView == null) {
                    rightholder = new ViewHolderRightText();
                    holderView = mInflater.inflate(R.layout.listitem_cha_right_text, null);
                    holderView.setFocusable(true);
                    rightholder.iv_avatar = (ImageView) holderView.findViewById(R.id.iv_avatar);
                    rightholder.tv_content = (TextView) holderView.findViewById(R.id.tv_content);
                    rightholder.sendtime = (TextView) holderView.findViewById(R.id.sendtime);
                    rightholder.sendtime.setText(bean.getTime());
                    holderView.setTag(rightholder);
                    convertView = holderView;
                } else {
                    rightholder = (ViewHolderRightText) convertView.getTag();
                }
                disPlayRightTextView(position, convertView, rightholder, bean);
                break;
            case VIEW_TYPE_LEFT_TEXT:
                ViewHolderLeftText leftholder;
                if (convertView == null) {
                    leftholder = new ViewHolderLeftText();
                    holderView = mInflater.inflate(R.layout.listitem_cha_left_text, null);
                    holderView.setFocusable(true);
                    leftholder.iv_avatar = (ImageView) holderView.findViewById(R.id.iv_avatar);
                    leftholder.tv_content = (TextView) holderView.findViewById(R.id.tv_content);
                    leftholder.sendtime = (TextView) holderView.findViewById(R.id.sendtime);
                    leftholder.sendtime.setText(bean.getTime());
                    holderView.setTag(leftholder);
                    convertView = holderView;
                } else {
                    leftholder = (ViewHolderLeftText) convertView.getTag();
                }
                disPlayLeftTextView(position, convertView, leftholder, bean);
                break;
            case VIEW_TYPE_RIGTH_IMAGE:
                ViewHolderRightImage imageHolder;
                if (convertView == null) {
                    imageHolder = new ViewHolderRightImage();
                    holderView = mInflater.inflate(R.layout.listitem_chat_right_image, null);
                    holderView.setFocusable(true);
                    imageHolder.iv_avatar = (ImageView) holderView.findViewById(R.id.iv_avatar);
                    imageHolder.iv_image = (ImageView) holderView.findViewById(R.id.iv_image);
                    imageHolder.sendtime = (TextView) holderView.findViewById(R.id.sendtime);
                    imageHolder.sendtime.setText(bean.getTime());
                    holderView.setTag(imageHolder);
                    convertView = holderView;
                } else {
                    imageHolder = (ViewHolderRightImage) convertView.getTag();
                }
                disPlayRightImageView(position, convertView, imageHolder, bean);
                break;
            case VIEW_TYPE_LEFT_IMAGE:
                ViewHolderLeftImage imageHolder2;
                if (convertView == null) {
                    imageHolder2 = new ViewHolderLeftImage();
                    holderView = mInflater.inflate(R.layout.listitem_chat_left_image, null);
                    holderView.setFocusable(true);
                    imageHolder2.iv_avatar = (ImageView) holderView.findViewById(R.id.iv_avatar);
                    imageHolder2.iv_image = (ImageView) holderView.findViewById(R.id.iv_image);
                    imageHolder2.sendtime = (TextView) holderView.findViewById(R.id.sendtime);
                    imageHolder2.sendtime.setText(bean.getTime());
                    holderView.setTag(imageHolder2);
                    convertView = holderView;
                } else {
                    imageHolder2 = (ViewHolderLeftImage) convertView.getTag();
                }
                disPlayLeftImageView(position, convertView, imageHolder2, bean);
                break;
            default:
                convertView = new View(mActivity);
                break;
        }
        return convertView;
    }

    public void disPlayLeftTextView(int position, View view, ViewHolderLeftText holder, ImMsgBean bean) {
        setContent(holder.tv_content, bean.getContent());
    }

    public void setContent(TextView tv_content, String content) {
        SimpleCommonUtils.spannableEmoticonFilter(tv_content, content);
    }

    public void disPlayLeftImageView(int position, View view, ViewHolderLeftImage holder, ImMsgBean bean) {
        try {
            if (ImageBase.Scheme.FILE == ImageBase.Scheme.ofUri(bean.getImage())) {
                String filePath = ImageBase.Scheme.FILE.crop(bean.getImage());
                Glide.with(holder.iv_image.getContext())
                        .load(filePath)
                        .into(holder.iv_image);
            } else {
                ImageLoadUtils.getInstance(mActivity).displayImage(bean.getImage(), holder.iv_image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disPlayRightTextView(int position, View view, ViewHolderRightText holder, ImMsgBean bean) {
        setContent2(holder.tv_content, bean.getContent());
    }

    public void setContent2(TextView tv_content, String content) {
        SimpleCommonUtils.spannableEmoticonFilter(tv_content, content);
    }

    public void disPlayRightImageView(int position, View view, ViewHolderRightImage holder, ImMsgBean bean) {
        try {
            if (ImageBase.Scheme.FILE == ImageBase.Scheme.ofUri(bean.getImage())) {
                String filePath = ImageBase.Scheme.FILE.crop(bean.getImage());
                Glide.with(holder.iv_image.getContext())
                        .load(filePath)
                        .into(holder.iv_image);
            } else {
                ImageLoadUtils.getInstance(mActivity).displayImage(bean.getImage(), holder.iv_image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public final class ViewHolderLeftText {
        public ImageView iv_avatar;
        public TextView tv_content;
        public TextView sendtime;
    }

    public final class ViewHolderLeftImage {
        public ImageView iv_avatar;
        public ImageView iv_image;
        public TextView sendtime;
    }
    public final class ViewHolderRightText {
        public ImageView iv_avatar;
        public TextView tv_content;
        public TextView sendtime;
    }

    public final class ViewHolderRightImage {
        public ImageView iv_avatar;
        public ImageView iv_image;
        public TextView sendtime;
    }
}