package com.weiey.app;

import android.os.Environment;

/**
 * Author:yezw on 2016/10/13
 * Description:
 */
public class AppConstants {



    public static final class Paths {
        public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        public static final String IMAGE_LOADER_CACHE_PATH = "/SimplifyReader/Images/";

    }
    public static final class Events {
        public static final int EVENT_BEGIN = 0X100;
        public static final int EVENT_REFRESH_DATA = EVENT_BEGIN + 10;
        public static final int EVENT_LOAD_MORE_DATA = EVENT_BEGIN + 20;
        public static final int EVENT_START_PLAY_MUSIC = EVENT_BEGIN + 30;
        public static final int EVENT_STOP_PLAY_MUSIC = EVENT_BEGIN + 40;
    }

    public static boolean isSwipeBackEnable = true;

}
