package com.test.gizwitsandroidversion.appkit;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizEventType;
import com.gizwits.gizwifisdk.enumration.GizLogPrintLevel;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.test.gizwitsandroidversion.R;
import com.test.gizwitsandroidversion.appkit.CommonModule.GosDeploy;
import java.util.concurrent.ConcurrentHashMap;

public class MessageCenter {
  private static MessageCenter mCenter;
  int flag = 0;

  GosDeploy gosDeploy;
  Handler hand = new Handler() {
    public void handleMessage(android.os.Message msg) {
      GizWifiSDK.sharedInstance().setLogLevel(GizLogPrintLevel.GizLogPrintAll);
    }

    ;
  };

  private int SETCLOUD = 1111;

  private MessageCenter(Context c) {
    if (mCenter == null) {
      init(c);
    }
  }

  public static MessageCenter getInstance(Context c) {
    if (mCenter == null) {
      mCenter = new MessageCenter(c);
    }
    return mCenter;
  }

  private void init(Context c) {
    gosDeploy = new GosDeploy(c);
    String AppID = GosDeploy.setAppID();
    String AppSecret = GosDeploy.setAppSecret();
    if (TextUtils.isEmpty(AppID)
        || AppID.contains("your_app_id")
        || TextUtils.isEmpty(AppSecret)
        || AppSecret.contains("your_app_secret")) {
      String AppID_Toast = c.getString(R.string.AppID_Toast);
      if (flag == 0) {
        Toast.makeText(c, AppID_Toast, Toast.LENGTH_LONG).show();
      }
      flag++;
    } else {
      // 启动SDK
      ConcurrentHashMap<String, String> serverMap = new ConcurrentHashMap<String, String>();

      serverMap.put("openAPIInfo",
          TextUtils.isEmpty((String) GosDeploy.infoMap.get("openAPI_URL")) ? "api.gizwits.com"
              : (String) GosDeploy.infoMap.get("openAPI_URL"));
      serverMap.put("siteInfo",
          TextUtils.isEmpty((String) GosDeploy.infoMap.get("site_URL")) ? "site.gizwits.com"
              : (String) GosDeploy.infoMap.get("site_URL"));
      serverMap.put("pushInfo", (String) GosDeploy.infoMap.get("push_URL"));
      //GizWifiSDK.sharedInstance().startWithAppID(c, AppID, GosDeploy.setProductKeyList(), serverMap);
      GizWifiSDK.sharedInstance().setListener(mListener);
      GizWifiSDK.sharedInstance()
          .startWithAppID(c, AppID, AppSecret, GosDeploy.setProductKeyList(), serverMap, false);
      Log.d("SDK version",GizWifiSDK.sharedInstance().getVersion());
    }
    hand.sendEmptyMessageDelayed(SETCLOUD, 3000);
  }

  GizWifiSDKListener mListener = new GizWifiSDKListener() {
    @Override
    public void didNotifyEvent(GizEventType eventType, Object eventSource, GizWifiErrorCode eventID, String eventMessage) {
      if (eventType == GizEventType.GizEventSDK) {
        // SDK的事件通知
        Log.i("GizWifiSDK", "SDK event happened: " + eventID + ", " + eventMessage);
      } else if (eventType == GizEventType.GizEventDevice) {
        // 设备连接断开时可能产生的通知
        GizWifiDevice mDevice = (GizWifiDevice)eventSource;
        Log.i("GizWifiSDK", "device mac: " + mDevice.getMacAddress() + " disconnect caused by eventID: " + eventID + ", eventMessage: " + eventMessage);
      } else if (eventType == GizEventType.GizEventM2MService) {
        // M2M服务返回的异常通知
        Log.i("GizWifiSDK", "M2M domain " + (String)eventSource + " exception happened, eventID: " + eventID + ", eventMessage: " + eventMessage);
      } else if (eventType == GizEventType.GizEventToken) {
        // token失效通知
        Log.i("GizWifiSDK", "token " + (String)eventSource + " expired: " + eventMessage);
      }
    }
  };
}
