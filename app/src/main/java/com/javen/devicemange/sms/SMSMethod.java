package com.javen.devicemange.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javen on 2016-03-15.
 */
public class SMSMethod {
    private static  SMSMethod mSMSmsMethod;
    /* 自定义ACTION常数，作为广播的Intent Filter识别常数 */
    public static String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
    public static String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";

    /* 建立两个mServiceReceiver对象，作为类成员变量 */
    private SMSReceiver mSendSMSReceiver, mDeliveredSMSReceiver;
    
    private Context mContext;

    private SMSMethod(Context context){
        mContext=context;
        registerReceiver();
    }

    public static  SMSMethod getInstance(Context context){
        if (mSMSmsMethod==null){
            synchronized (SMSMethod.class){
                if (mSMSmsMethod==null){
                    mSMSmsMethod=new SMSMethod(context);
                }
            }
        }
        return  mSMSmsMethod;
    }

    /**
     * 注册
     */
    public void registerReceiver(){
         /* 自定义IntentFilter为SENT_SMS_ACTIOIN Receiver */
        IntentFilter mFilter01;
        mFilter01 = new IntentFilter(SMS_SEND_ACTIOIN);
        mSendSMSReceiver = new SMSReceiver();
        mContext.registerReceiver(mSendSMSReceiver, mFilter01);

        /* 自定义IntentFilter为DELIVERED_SMS_ACTION Receiver */
        mFilter01 = new IntentFilter(SMS_DELIVERED_ACTION);
        mDeliveredSMSReceiver = new SMSReceiver();
        mContext.registerReceiver(mDeliveredSMSReceiver, mFilter01);
    }

    public void unregisterReceiver(){
        /* 取消注册自定义Receiver */
        if (mSendSMSReceiver!=null){
            mContext.unregisterReceiver(mSendSMSReceiver);
        }
        if (mDeliveredSMSReceiver!=null) {
            mContext.unregisterReceiver(mDeliveredSMSReceiver);
        }
    }

    public void SendMessage(String strDestAddress,String strMessage){
        /* 建立SmsManager对象 */
        SmsManager smsManager = SmsManager.getDefault();
        try {
          /* 建立自定义Action常数的Intent(给PendingIntent参数之用) */
            Intent itSend = new Intent(SMS_SEND_ACTIOIN);
            Intent itDeliver = new Intent(SMS_DELIVERED_ACTION);

          /* sentIntent参数为传送后接受的广播信息PendingIntent */
            PendingIntent mSendPI = PendingIntent.getBroadcast(mContext, 0, itSend, 0);

          /* deliveryIntent参数为送达后接受的广播信息PendingIntent */
            PendingIntent mDeliverPI = PendingIntent.getBroadcast(mContext, 0, itDeliver, 0);
            List<String> divideContents = smsManager.divideMessage(strMessage);
            for (String text:divideContents) {
                 /* 发送SMS短信，注意倒数的两个PendingIntent参数 */
                smsManager.sendTextMessage(strDestAddress, null, text, mSendPI, mDeliverPI);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void SendMessage2(String strDestAddress,String strMessage){
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();


        /* 建立SmsManager对象 */
        SmsManager smsManager = SmsManager.getDefault();
        try {
          /* 建立自定义Action常数的Intent(给PendingIntent参数之用) */
            Intent itSend = new Intent(SMS_SEND_ACTIOIN);
            Intent itDeliver = new Intent(SMS_DELIVERED_ACTION);

          /* sentIntent参数为传送后接受的广播信息PendingIntent */
            PendingIntent mSendPI = PendingIntent.getBroadcast(mContext, 0, itSend, 0);

          /* deliveryIntent参数为送达后接受的广播信息PendingIntent */
            PendingIntent mDeliverPI = PendingIntent.getBroadcast(mContext, 0, itDeliver, 0);
            ArrayList<String> mSMSMessage = smsManager.divideMessage(strMessage);

            for (int i = 0; i < mSMSMessage.size(); i++) {
                sentPendingIntents.add(i, mSendPI);
                deliveredPendingIntents.add(i, mDeliverPI);
            }
             /* 发送SMS短信，注意倒数的两个PendingIntent参数 */
            smsManager.sendMultipartTextMessage(strDestAddress,null,mSMSMessage ,sentPendingIntents,deliveredPendingIntents);


        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
