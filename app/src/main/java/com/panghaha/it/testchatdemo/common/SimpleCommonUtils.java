package com.panghaha.it.testchatdemo.common;

import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.panghaha.it.testchatdemo.R;
import com.sj.emoji.DefEmoticons;
import com.sj.emoji.EmojiBean;
import com.testemticon.DefXhsEmoticons;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;

import sj.keyboard.adpater.EmoticonsAdapter;
import sj.keyboard.adpater.PageSetAdapter;
import sj.keyboard.data.EmoticonEntity;
import sj.keyboard.data.EmoticonPageEntity;
import sj.keyboard.data.EmoticonPageSetEntity;
import sj.keyboard.data.PageEntity;
import sj.keyboard.data.PageSetEntity;
import sj.keyboard.interfaces.EmoticonClickListener;
import sj.keyboard.interfaces.EmoticonDisplayListener;
import sj.keyboard.interfaces.PageViewInstantiateListener;
import sj.keyboard.utils.EmoticonsKeyboardUtils;
import sj.keyboard.utils.imageloader.ImageBase;
import sj.keyboard.utils.imageloader.ImageLoader;
import sj.keyboard.widget.EmoticonPageView;
import sj.keyboard.widget.EmoticonsEditText;
import sj.qqkeyboard.DefQqEmoticons;

/**
 *
 * 表情工厂类 加载表情种类
 * */

public class SimpleCommonUtils {

    public static void initEmoticonsEditText(EmoticonsEditText etContent) {
        etContent.addEmoticonFilter(new EmojiFilter());
        etContent.addEmoticonFilter(new XhsFilter());
    }

    public static EmoticonClickListener getCommonEmoticonClickListener(final EditText editText) {
        return new EmoticonClickListener() {
            @Override
            public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {
                if (isDelBtn) {
                    SimpleCommonUtils.delClick(editText);
                } else {
                    if (o == null) {
                        return;
                    }
                    if (actionType == Constants.EMOTICON_CLICK_TEXT) {
                        String content = null;
                        if (o instanceof EmojiBean) {
                            content = ((EmojiBean) o).emoji;
                        } else if (o instanceof EmoticonEntity) {
                            content = ((EmoticonEntity) o).getContent();
                        }

                        if (TextUtils.isEmpty(content)) {
                            return;
                        }
                        int index = editText.getSelectionStart();
                        Editable editable = editText.getText();
                        editable.insert(index, content);
                    }
                }
            }
        };
    }

    public static PageSetAdapter sCommonPageSetAdapter;

    public static PageSetAdapter getCommonAdapter(Context context, EmoticonClickListener emoticonClickListener) {

        if(sCommonPageSetAdapter != null){
            return sCommonPageSetAdapter;
        }

        PageSetAdapter pageSetAdapter = new PageSetAdapter();
        //原生的笑脸表情
//        addEmojiPageSetEntity(pageSetAdapter, context, emoticonClickListener);
        //QQ笑脸表情
        addQqPageSetEntity(pageSetAdapter, context, emoticonClickListener);

        //龟头表情
//        addXhsPageSetEntity(pageSetAdapter, context, emoticonClickListener);
        //兔斯基
//        addWechatPageSetEntity(pageSetAdapter, context, emoticonClickListener);
        //好好学习表情包
//        addGoodGoodStudyPageSetEntity(pageSetAdapter, context, emoticonClickListener);
        //颜文字
        addKaomojiPageSetEntity(pageSetAdapter, context, emoticonClickListener);

//        addTestPageSetEntity(pageSetAdapter, context);

        return pageSetAdapter;
    }

    /**
     * 插入emoji表情集
     *
     * @param pageSetAdapter
     * @param context
     * @param emoticonClickListener
     */
    public static void addEmojiPageSetEntity(PageSetAdapter pageSetAdapter, Context context, final EmoticonClickListener emoticonClickListener) {
        ArrayList<EmojiBean> emojiArray = new ArrayList<>();
        Collections.addAll(emojiArray, DefEmoticons.sEmojiArray);
        EmoticonPageSetEntity emojiPageSetEntity
                = new EmoticonPageSetEntity.Builder()
                .setLine(3)
                .setRow(7)
                .setEmoticonList(emojiArray)
                .setIPageViewInstantiateItem(getDefaultEmoticonPageViewInstantiateItem(new EmoticonDisplayListener<Object>() {
                    @Override
                    public void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, Object object, final boolean isDelBtn) {
                        final EmojiBean emojiBean = (EmojiBean) object;
                        if (emojiBean == null && !isDelBtn) {
                            return;
                        }

                        viewHolder.ly_root.setBackgroundResource(com.keyboard.view.R.drawable.bg_emoticon);

                        if (isDelBtn) {
                            viewHolder.iv_emoticon.setImageResource(R.drawable.icon_del);
                        } else {
                            viewHolder.iv_emoticon.setImageResource(emojiBean.icon);
                        }

                        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (emoticonClickListener != null) {
                                    emoticonClickListener.onEmoticonClick(emojiBean, Constants.EMOTICON_CLICK_TEXT, isDelBtn);
                                }
                            }
                        });
                    }
                }))
                .setShowDelBtn(EmoticonPageEntity.DelBtnStatus.LAST)
                .setIconUri(ImageBase.Scheme.DRAWABLE.toUri("icon_emoji"))
                .build();
        pageSetAdapter.add(emojiPageSetEntity);
    }


    public static void addQqPageSetEntity(PageSetAdapter pageSetAdapter, Context context, final EmoticonClickListener emoticonClickListener) {
        EmoticonPageSetEntity kaomojiPageSetEntity
                = new EmoticonPageSetEntity.Builder()
                .setLine(3)
                .setRow(7)
                .setEmoticonList(ParseDataUtils.ParseQqData(DefQqEmoticons.sQqEmoticonHashMap))
                .setIPageViewInstantiateItem(new PageViewInstantiateListener<EmoticonPageEntity>() {
                    @Override
                    public View instantiateItem(ViewGroup container, int position, EmoticonPageEntity pageEntity) {
                        if (pageEntity.getRootView() == null) {
                            EmoticonPageView pageView = new EmoticonPageView(container.getContext());
                            pageView.setNumColumns(pageEntity.getRow());
                            pageEntity.setRootView(pageView);
                            try {
                                EmoticonsAdapter adapter = new EmoticonsAdapter(container.getContext(), pageEntity, emoticonClickListener);
                                adapter.setItemHeightMaxRatio(1.8);
                                adapter.setOnDisPlayListener(getEmoticonDisplayListener(emoticonClickListener));
                                pageView.getEmoticonsGridView().setAdapter(adapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return pageEntity.getRootView();
                    }
                })
                .setShowDelBtn(EmoticonPageEntity.DelBtnStatus.LAST)
                .setIconUri(ImageBase.Scheme.DRAWABLE.toUri("kys"))
                .build();
        pageSetAdapter.add(kaomojiPageSetEntity);
    }



    public static EmoticonDisplayListener<Object> getEmoticonDisplayListener(final EmoticonClickListener emoticonClickListener){
        return new EmoticonDisplayListener<Object>() {
            @Override
            public void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, Object object, final boolean isDelBtn) {
                final EmoticonEntity emoticonEntity = (EmoticonEntity) object;
                if (emoticonEntity == null && !isDelBtn) {
                    return;
                }
                viewHolder.ly_root.setBackgroundResource(com.keyboard.view.R.drawable.bg_emoticon);

                if (isDelBtn) {
                    viewHolder.iv_emoticon.setImageResource(R.drawable.icon_del);
                } else {
                    try {
                        ImageLoader.getInstance(viewHolder.iv_emoticon.getContext()).displayImage(emoticonEntity.getIconUri(), viewHolder.iv_emoticon);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (emoticonClickListener != null) {
                            emoticonClickListener.onEmoticonClick(emoticonEntity, Constants.EMOTICON_CLICK_TEXT, isDelBtn);
                        }
                    }
                });
            }
        };
    }


    /**
     * 插入xhs表情集
     *
     * @param pageSetAdapter
     * @param context
     * @param emoticonClickListener
     */
    public static void addXhsPageSetEntity(PageSetAdapter pageSetAdapter, Context context, EmoticonClickListener emoticonClickListener) {
        EmoticonPageSetEntity xhsPageSetEntity
                = new EmoticonPageSetEntity.Builder()
                .setLine(3)
                .setRow(7)
                .setEmoticonList(ParseDataUtils.ParseXhsData(DefXhsEmoticons.xhsEmoticonArray, ImageBase.Scheme.ASSETS))
                .setIPageViewInstantiateItem(getDefaultEmoticonPageViewInstantiateItem(getCommonEmoticonDisplayListener(emoticonClickListener, Constants.EMOTICON_CLICK_TEXT)))
                .setShowDelBtn(EmoticonPageEntity.DelBtnStatus.LAST)
                .setIconUri(ImageBase.Scheme.ASSETS.toUri("xhsemoji_19.png"))
                .build();
        pageSetAdapter.add(xhsPageSetEntity);
    }

    /**
     * 插入微信表情集
     *
     * @param pageSetAdapter
     * @param context
     * @param emoticonClickListener
     */
    public static void addWechatPageSetEntity(PageSetAdapter pageSetAdapter, Context context, EmoticonClickListener emoticonClickListener) {
        String filePath = FileUtils.getFolderPath("wxemoticons");
        EmoticonPageSetEntity<EmoticonEntity> emoticonPageSetEntity = ParseDataUtils.parseDataFromFile(context, filePath, "wxemoticons.zip", "wxemoticons.xml");
        if (emoticonPageSetEntity == null) {
            return;
        }
        EmoticonPageSetEntity pageSetEntity
                = new EmoticonPageSetEntity.Builder()
                .setLine(emoticonPageSetEntity.getLine())
                .setRow(emoticonPageSetEntity.getRow())
                .setEmoticonList(emoticonPageSetEntity.getEmoticonList())
                .setIPageViewInstantiateItem(getEmoticonPageViewInstantiateItem(BigEmoticonsAdapter.class, emoticonClickListener))
                .setIconUri(ImageBase.Scheme.FILE.toUri(filePath + "/" + emoticonPageSetEntity.getIconUri()))
                .build();
        pageSetAdapter.add(pageSetEntity);
    }

    /**
     * 插入我们爱学习表情集
     *
     * @param pageSetAdapter
     * @param context
     * @param emoticonClickListener
     */
    public static void addGoodGoodStudyPageSetEntity(PageSetAdapter pageSetAdapter, Context context, EmoticonClickListener emoticonClickListener) {
        String filePath = FileUtils.getFolderPath("goodgoodstudy");
        EmoticonPageSetEntity<EmoticonEntity> emoticonPageSetEntity = ParseDataUtils.parseDataFromFile(context, filePath, "goodgoodstudy.zip", "goodgoodstudy.xml");
        if (emoticonPageSetEntity == null) {
            return;
        }
        EmoticonPageSetEntity pageSetEntity
                = new EmoticonPageSetEntity.Builder()
                .setLine(emoticonPageSetEntity.getLine())
                .setRow(emoticonPageSetEntity.getRow())
                .setEmoticonList(emoticonPageSetEntity.getEmoticonList())
                .setIPageViewInstantiateItem(getEmoticonPageViewInstantiateItem(BigEmoticonsAndTitleAdapter.class, emoticonClickListener))
                .setIconUri(ImageBase.Scheme.FILE.toUri(filePath + "/" + emoticonPageSetEntity.getIconUri()))
                .build();
        pageSetAdapter.add(pageSetEntity);
    }


    /**
     * 插入颜文字表情集
     *
     * @param pageSetAdapter
     * @param context
     * @param emoticonClickListener
     */
    public static void addKaomojiPageSetEntity(PageSetAdapter pageSetAdapter, Context context, EmoticonClickListener emoticonClickListener) {
        EmoticonPageSetEntity kaomojiPageSetEntity
                = new EmoticonPageSetEntity.Builder()
                .setLine(3)
                .setRow(3)
                .setEmoticonList(ParseDataUtils.parseKaomojiData(context))
                .setIPageViewInstantiateItem(getEmoticonPageViewInstantiateItem(TextEmoticonsAdapter.class, emoticonClickListener))
                .setIconUri(ImageBase.Scheme.DRAWABLE.toUri("icon_kaomoji"))
                .build();
        pageSetAdapter.add(kaomojiPageSetEntity);
    }

    /**
     * 测试页集
     *
     * @param pageSetAdapter
     * @param context
     */
    public static void addTestPageSetEntity(PageSetAdapter pageSetAdapter, Context context) {
        PageSetEntity pageSetEntity = new PageSetEntity.Builder()
                .addPageEntity(new PageEntity(new SimpleAppsGridView(context)))
                .setIconUri(ImageBase.Scheme.DRAWABLE.toUri("icon_kaomoji"))
                .setShowIndicator(false)
                .build();
        pageSetAdapter.add(pageSetEntity);
    }

    @SuppressWarnings("unchecked")
    public static Object newInstance(Class _Class, Object... args) throws Exception {
        return newInstance(_Class, 0, args);
    }

    @SuppressWarnings("unchecked")
    public static Object newInstance(Class _Class, int constructorIndex, Object... args) throws Exception {
        Constructor cons = _Class.getConstructors()[constructorIndex];
        return cons.newInstance(args);
    }

    public static PageViewInstantiateListener<EmoticonPageEntity> getDefaultEmoticonPageViewInstantiateItem(final EmoticonDisplayListener<Object> emoticonDisplayListener) {
        return getEmoticonPageViewInstantiateItem(EmoticonsAdapter.class, null, emoticonDisplayListener);
    }

    public static PageViewInstantiateListener<EmoticonPageEntity> getEmoticonPageViewInstantiateItem(final Class _class, EmoticonClickListener onEmoticonClickListener) {
        return getEmoticonPageViewInstantiateItem(_class, onEmoticonClickListener, null);
    }

    public static PageViewInstantiateListener<EmoticonPageEntity> getEmoticonPageViewInstantiateItem(final Class _class, final EmoticonClickListener onEmoticonClickListener, final EmoticonDisplayListener<Object> emoticonDisplayListener) {
        return new PageViewInstantiateListener<EmoticonPageEntity>() {
            @Override
            public View instantiateItem(ViewGroup container, int position, EmoticonPageEntity pageEntity) {
                if (pageEntity.getRootView() == null) {
                    EmoticonPageView pageView = new EmoticonPageView(container.getContext());
                    pageView.setNumColumns(pageEntity.getRow());
                    pageEntity.setRootView(pageView);
                    try {
                        EmoticonsAdapter adapter = (EmoticonsAdapter) newInstance(_class, container.getContext(), pageEntity, onEmoticonClickListener);
                        if (emoticonDisplayListener != null) {
                            adapter.setOnDisPlayListener(emoticonDisplayListener);
                        }
                        pageView.getEmoticonsGridView().setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return pageEntity.getRootView();
            }
        };
    }

    public static EmoticonDisplayListener<Object> getCommonEmoticonDisplayListener(final EmoticonClickListener onEmoticonClickListener, final int type) {
        return new EmoticonDisplayListener<Object>() {
            @Override
            public void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, Object object, final boolean isDelBtn) {

                final EmoticonEntity emoticonEntity = (EmoticonEntity) object;
                if (emoticonEntity == null && !isDelBtn) {
                    return;
                }
                viewHolder.ly_root.setBackgroundResource(com.keyboard.view.R.drawable.bg_emoticon);

                if (isDelBtn) {
                    viewHolder.iv_emoticon.setImageResource(R.drawable.icon_del);
                } else {
                    try {
                        ImageLoader.getInstance(viewHolder.iv_emoticon.getContext()).displayImage(emoticonEntity.getIconUri(), viewHolder.iv_emoticon);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onEmoticonClickListener != null) {
                            onEmoticonClickListener.onEmoticonClick(emoticonEntity, type, isDelBtn);
                        }
                    }
                });
            }
        };
    }

    public static void delClick(EditText editText) {
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
    }

    public static void spannableEmoticonFilter(TextView tv_content, String content) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);

        Spannable spannable = QqFilter.spannableFilter(tv_content.getContext(),
                spannableStringBuilder,
                content,
                EmoticonsKeyboardUtils.getFontHeight(tv_content),
                null);
        tv_content.setText(spannable);

        /* Spannable spannable = EmojiDisplay.spannableFilter(tv_content.getContext(),
                spannableStringBuilder,
                content,
                EmoticonsKeyboardUtils.getFontHeight(tv_content));*/

//        spannable = XhsFilter.spannableFilter(tv_content.getContext(),
//                spannable,
//                content,
//                EmoticonsKeyboardUtils.getFontHeight(tv_content),
//                null);
//        tv_content.setText(spannable);
    }
}
