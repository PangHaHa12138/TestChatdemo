package com.panghaha.it.testchatdemo.common;

import android.content.Context;
import android.content.SharedPreferences;

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
public class SharedPreferencesUtil {

    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;


    /**
     * 读取获取Channelid
     *
     * @param context
     * @return
     */
    public static String readUsername(Context context) {
        preferences = context.getSharedPreferences("share", Context.MODE_PRIVATE);
        return preferences.getString("Username", "");
    }

    public static void writeUsername(String userid, Context context) {
        preferences = context.getSharedPreferences("share", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("Username", userid);
        editor.commit();
    }

    public static String readAvatar(Context context) {
        preferences = context.getSharedPreferences("share", Context.MODE_PRIVATE);
        return preferences.getString("Avatar", "");
    }

    public static void writeAvatar(String userid, Context context) {
        preferences = context.getSharedPreferences("share", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("Avatar", userid);
        editor.commit();
    }


}
