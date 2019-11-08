package com.weiey.app.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import com.weiey.app.AppContext;

import java.util.Map;

/**
 * 作者：weiey
 * 版本：
 * 创建日期：2018/6/21
 * 描述：在SharedPreferences中存取数据
 * 修订历史：
 */
public class SPUtil {
//    public final static String SHARED_PREFERENCED_NAME = "shared_preferenced_name";

    private SharedPreferences preferences;
    private static SPUtil sp;

    public static void destroy() {
        sp = null;
    }

    private SPUtil(Context context) {
//        this.preferences = context.getSharedPreferences(SHARED_PREFERENCED_NAME, Context.MODE_PRIVATE);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SPUtil getInstance() {
        if (sp == null) {
            sp = new SPUtil(AppContext.getContext());
        }
        return sp;
    }


    public boolean putStringEncrypt(String key, String value) {
        try {
            String encryptValue = DES3Util.encode(value);
            Editor editor = preferences.edit();
            editor.putString(key, encryptValue);
            return editor.commit();
        } catch (Exception e) {

        }
        return false;
    }

    public String getStringDecrypt(String key, String defaultValue) {
        String value = null;
        try {

            try {
                value = preferences.getString(key, defaultValue);
                String encryptValue = DES3Util.decode(value);
                return encryptValue;
            } catch (Exception e) {

            }

        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 此方法无法存入空字串。
     */
    public boolean putString(Editor editor,
                             String key, String value) {
        try {
            editor.putString(key, value);
            return editor.commit();
        } catch (Exception e) {

        }
        return false;
    }

    public boolean putString(String key, String value) {
        try {
            Editor editor = preferences.edit();
            editor.putString(key, value);
            return editor.commit();
        } catch (Exception e) {

        }
        return false;
    }

    public boolean putString(Map<String, String> map) {
        try {
            Editor editor = preferences.edit();
            for (String key : map.keySet()) {
                editor.putString(key, map.get(key));
            }
            return editor.commit();
        } catch (Exception e) {

        }
        return false;
    }

    public String getString(SharedPreferences sharedPreferences,
                            String key, String defaultValue) {
        String value = null;
        try {
            value = sharedPreferences.getString(key, defaultValue);
        } catch (Exception e) {
        }
        return value;
    }

    public String getString(String key, String defaultValue) {
        String value = null;
        try {
            value = preferences.getString(key, defaultValue);
        } catch (Exception e) {

        }
        return value;
    }

    public boolean putBoolean(Editor editor,
                              String key, boolean value) {
        try {
            editor.putBoolean(key, value);
            return editor.commit();
        } catch (Exception e) {

        }

        return false;
    }



    public boolean putBoolean(String key, boolean value) {

        Editor editor = preferences.edit();
        try {
            editor.putBoolean(key, value);
            return editor.commit();
        } catch (Exception e) {
        }
        return false;
    }

    //PrismApplication.getInstance().getAppSharedPreferences().edit()

    public boolean getBoolean(SharedPreferences sp, String key,
                              boolean defaultValue) {
        boolean value = defaultValue;
        try {
            value = sp.getBoolean(key, defaultValue);
        } catch (Exception e) {

        }
        return value;
    }

    public boolean getBoolean(String key,
                              boolean defaultValue) {
        boolean value = defaultValue;
        try {
            value = preferences.getBoolean(key, defaultValue);
        } catch (Exception e) {

        }
        return value;
    }

    public void remove(Editor editor, String key) {
        editor.remove(key);
        editor.commit();
    }

    public void remove(String key) {
        Editor editor = preferences.edit();
        editor.remove(key).commit();
    }


    public boolean putLong(Editor editor, String key,
                           long value) {
        try {
            editor.putLong(key, value);
            return editor.commit();
        } catch (Exception e) {

        }
        return false;
    }

    public boolean putLong(String key,
                           long value) {

        try {
            Editor editor = preferences.edit();
            editor.putLong(key, value);
            return editor.commit();
        } catch (Exception e) {

        }finally {

        }
        return false;
    }

    public long getLong(SharedPreferences sp, String key,
                        long defaultValue) {
        long value = 0L;
        try {
            value = sp.getLong(key, defaultValue);
        } catch (Exception e) {

        }
        return value;
    }

    public long getLong(String key,
                        long defaultValue) {
        long value = 0L;
        try {
            value = preferences.getLong(key, defaultValue);
        } catch (Exception e) {

        }
        return value;
    }

    // add by cbf
    public boolean putInt(Editor editor, String key,
                          int value) {
        try {
            editor.putInt(key, value);
            return editor.commit();
        } catch (Exception e) {

        }

        return false;
    }

    // add by cbf
    public boolean putInt(String key, int value) {
        Editor editor = preferences.edit();
        try {
            editor.putInt(key, value);
            return editor.commit();
        } catch (Exception e) {
        }
        return false;
    }

    public int getInt(SharedPreferences sp, String key, int defaultValue) {
        int value = 0;
        try {
            value = sp.getInt(key, defaultValue);
        } catch (Exception e) {

        }
        return value;
    }

    public int getInt(String key, int defaultValue) {
        int value = 0;
        try {
            value = preferences.getInt(key, defaultValue);
        } catch (Exception e) {

        }
        return value;
    }

    public Map<String, ?> getAll() {
        Map<String, ?> map = (Map<String, ?>) preferences.getAll();
        return map;
    }
    public void clear(){
        try {
            Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
        } catch (Exception e) {

        }

    }

}

