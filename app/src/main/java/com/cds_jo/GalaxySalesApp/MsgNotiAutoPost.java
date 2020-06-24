package com.cds_jo.GalaxySalesApp;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class MsgNotiAutoPost {
     private static final String NOTIFICATION_TAG = "التحديث التلقائي";


    public static void notify(final Context context,final String Title, final String MsgContent,final String OrderNo  ) {
        final Resources res = context.getResources();
        final Bitmap picture = BitmapFactory.decodeResource(res, R.mipmap.gi);
        int M=Integer.parseInt(DB.GetValue(context,"OrdersSitting","PostDely","1=1"));
        if (M <= 0){
            M = 2 ;
        }
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final String text = Title ;
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSound(soundUri)
                .setSmallIcon(R.drawable.share)
                .setContentTitle(" التحديث التلقائي" )
                .setContentText("   ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(picture)
                .setTicker("التحديث التلقائي" + "  " + OrderNo)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text)
                        .setBigContentTitle(MsgContent  + " : " + OrderNo)
                        .setSummaryText("الفترة الزمنية للتحديث" + "  :  " +  M +" (دقيقة) "))
                .setAutoCancel(true);
        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}
