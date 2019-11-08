package com.weiey.app.utils;

import android.annotation.TargetApi;
import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.weiey.app.R;

import static android.app.Notification.VISIBILITY_SECRET;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 通知工具类，适配Android O
 */
public class NotificationUtils {

    private Context context;
    private NotificationManager notificationManager;
    private static int id = 0;

    private static class NotificationUtilsHolder {
        public static final NotificationUtils notificationUtils = new NotificationUtils();
    }

    private NotificationUtils() {
    }

    public static NotificationUtils getInstance() {
        return NotificationUtilsHolder.notificationUtils;
    }

    /**
     * 初始化
     *
     * @param context 引用全局上下文
     */
    public void init(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     * 创建通知通道
     *
     * @param channelId   通道id
     * @param channelName 通道名称
     * @param importance  通道级别
     */
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {

        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.canBypassDnd();//绕过请勿打扰模式
        channel.enableLights(true);
        channel.setLockscreenVisibility(VISIBILITY_SECRET);//锁屏显示
        //channel.setLightColor(Color.RED);//闪光的灯管颜色
        channel.getAudioAttributes();
        channel.canShowBadge();//桌面角标
        channel.enableVibration(true);//允许震动
        channel.getGroup();//获取通知渠道组
        channel.setBypassDnd(true);//设置绕过请勿打扰模式
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * 发送通知
     *
     * @param channelId 通道id
     * @param title     标题
     * @param content   内容
     * @param intent    意图
     */
    private void sendNotification(String channelId, String title, String content, Intent intent) {

        PendingIntent pendingIntent = null;
        if (intent != null) {
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        Notification notification = new NotificationCompat.Builder(context, channelId).setContentTitle(title).setContentText(content).setSmallIcon(R.mipmap.ic_launcher).setWhen(System.currentTimeMillis()).setOngoing(false).setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND).setContentIntent(pendingIntent).build();
        notificationManager.notify(id++, notification);
    }

    /**
     * 判断App通知是否开启
     * 注意这个方法判断的是通知总开关，如果APP通知被关闭，则其下面的所有通知渠道也被关闭
     */
    private boolean isNotificationEnabled(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }

    /**
     * 判断APP某个通知渠道的通知是否开启
     */
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    private boolean isNotificationChannelEnabled(Context context, String channelId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
        return channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
    }

    /**
     * 检查是否开了通知，没有提示跳转
     * 默认渠道
     *
     * @param mContext
     */
    public void checkNotificationEnabled(Context mContext) {
        checkNotificationEnabled(mContext, channelId);
    }

    /**
     * 检查是否开了通知，没有提示跳转
     *
     * @param mContext
     * @param channelId
     */
    public void checkNotificationEnabled(final Context mContext, String channelId) {
        //判断通知是否开启
        if (!isNotificationEnabled(mContext)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("提示").setMessage("是否开启通知？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    openNotification(mContext);
                }
            }).setNegativeButton("取消", null).show();
            return;
        }
        //判断某个渠道的通知是否开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!isNotificationChannelEnabled(mContext, channelId)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("提示").setMessage("是否开启通知？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openNotification(mContext);
                    }
                }).setNegativeButton("取消", null).show();
                return;
            }
        }
    }

    /**
     * 打开通知设置页面
     */
    public void openNotification(Context context) {
        String packageName = context.getPackageName();
        int uid = context.getApplicationInfo().uid;
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName);
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", packageName);
            intent.putExtra("app_uid", uid);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + packageName));
        } else {
            intent.setAction(Settings.ACTION_SETTINGS);
        }
        context.startActivity(intent);
    }

    String channelId = "appNotification";
    String channelName = "通知";

    /**
     * 发送通知
     *
     * @param mContext
     * @param title
     * @param content
     * @param cls
     */
    public void sendNotification(Context mContext, String title, String content, Class<?> cls) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 8.0以上创建通道
            // 当然这里是具体按照项目需要设定的通道类型创建
            createNotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        }
        if (cls == null) {
            sendNotification(channelId, title, content, null);
            return;
        }
        // 设置Intent相关数据
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        sendNotification(channelId, title, content, intent);
    }

    /**
     * 发送通知
     *
     * @param mContext
     * @param title
     * @param content
     */
    public void sendNotification(Context mContext, String title, String content) {
        sendNotification(mContext, title, content, null);
    }


}