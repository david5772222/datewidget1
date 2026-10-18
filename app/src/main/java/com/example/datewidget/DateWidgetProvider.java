package com.example.datewidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Calendar;

/**
 * 1x1 日期小工具:上方顯示星期(一~日),下方顯示日期數字。
 * 日期數字由 TextClock 自動更新;星期則在每天午夜(以及改時間/時區時)重新設定。
 */
public class DateWidgetProvider extends AppWidgetProvider {

    private static final String[] WEEKDAYS = {"日", "一", "二", "三", "四", "五", "六"};
    private static final String ACTION_MIDNIGHT = "com.example.datewidget.MIDNIGHT";

    @Override
    public void onEnabled(Context context) {
        scheduleMidnight(context);
    }

    @Override
    public void onDisabled(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(midnightIntent(context));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            manager.updateAppWidget(id, buildViews(context));
        }
        scheduleMidnight(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (ACTION_MIDNIGHT.equals(action)
                || Intent.ACTION_DATE_CHANGED.equals(action)
                || Intent.ACTION_TIME_CHANGED.equals(action)
                || Intent.ACTION_TIMEZONE_CHANGED.equals(action)) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] ids = manager.getAppWidgetIds(new ComponentName(context, DateWidgetProvider.class));
            onUpdate(context, manager, ids);
        }
    }

    private static RemoteViews buildViews(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_date);
        int dow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK); // 週日 = 1
        views.setTextViewText(R.id.weekday, WEEKDAYS[dow - 1]);
        return views;
    }

    private static PendingIntent midnightIntent(Context context) {
        Intent intent = new Intent(context, DateWidgetProvider.class).setAction(ACTION_MIDNIGHT);
        return PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    /** 設定下一次午夜 00:00:05 更新(不需特殊權限,系統可能延遲數分鐘)。 */
    private static void scheduleMidnight(Context context) {
        Calendar next = Calendar.getInstance();
        next.add(Calendar.DAY_OF_MONTH, 1);
        next.set(Calendar.HOUR_OF_DAY, 0);
        next.set(Calendar.MINUTE, 0);
        next.set(Calendar.SECOND, 5);
        next.set(Calendar.MILLISECOND, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, next.getTimeInMillis(), midnightIntent(context));
    }
}
