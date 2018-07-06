package com.test.gizwitsandroidversion.appkit.PushModule;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushSettings;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizPushType;
import com.test.gizwitsandroidversion.R;
import com.test.gizwitsandroidversion.appkit.CommonModule.GosBaseActivity;
import com.test.gizwitsandroidversion.appkit.CommonModule.GosDeploy;
import java.util.Set;

public class GosPushManager {

  /** Channel_ID */
  public static String Channel_ID;
  static GizPushType gizPushType;
  static Context context;

  public GosPushManager(int PushType, Context context) {
    super();
    GosPushManager.context = context;
    if (PushType == 1) {
      GosPushManager.gizPushType = GizPushType.GizPushJiGuang;
      jPush();
    } else if (PushType == 2) {
      GosPushManager.gizPushType = GizPushType.GizPushBaiDu;
      bDPush();
    } else {

    }
  }

  /**
   * 向云端绑定推送
   */
  public static void pushBindService(String token) {

    if (GizPushType.GizPushJiGuang == gizPushType) {
      // 获取JPush的RegistrationID,即Channel_ID
      Channel_ID = JPushInterface.getRegistrationID(context);

      // 设定JPush类型
      JPushInterface.setAlias(context, Channel_ID, new TagAliasCallback() {
        @Override public void gotResult(int arg0, String arg1, Set<String> arg2) {
          if (arg0 == 0) {
            Log.i("Apptest", "Alias: " + arg1);
          } else {
            Log.e("Apptest", "Result: " + arg0);
          }
        }
      });
    } else if (GizPushType.GizPushBaiDu == gizPushType) {
      // 获取BDPush的Channel_ID
      Channel_ID = BaiDuPushReceiver.BaiDuPush_Channel_ID;
    } else {
      return;
    }

    // TODO 绑定推送
    Log.i("Apptest", Channel_ID + "\n" + gizPushType.toString() + "\n" + token);
    GizWifiSDK.sharedInstance().channelIDBind(token, Channel_ID, gizPushType);
  }

  public static void pushUnBindService(String token) {

    if (token.isEmpty()) {
      return;
    }

    if (GizPushType.GizPushJiGuang == gizPushType) {
      // 获取JPush的RegistrationID,即Channel_ID
      Channel_ID = JPushInterface.getRegistrationID(context);
    } else if (GizPushType.GizPushBaiDu == gizPushType) {
      // 获取BDPush的Channel_ID
      Channel_ID = BaiDuPushReceiver.BaiDuPush_Channel_ID;
    } else {
      return;
    }
    // TODO 绑定推送
    Log.i("Apptest", Channel_ID + "\n" + gizPushType.toString() + "\n" + token);
    GizWifiSDK.sharedInstance().channelIDUnBind(token, Channel_ID);
  }

  /**
   * 此方法完成了初始化JPush SDK等功能 但仍需在MainActivity的onResume和onPause添加相应方法
   * JPushInterface.onResume(context); JPushInterface.onPause(context);
   */
  public void jPush() {
    // 设置JPush调试模式
    JPushInterface.setDebugMode(true);

    // 初始化JPushSDK
    JPushInterface.init(context);
  }

  public void bDPush() {
    final String BDPushAppKey = GosDeploy.setBaiDuPushAppKey();
    if (TextUtils.isEmpty(BDPushAppKey) || BDPushAppKey.contains("your_bpush_api_key")) {
      GosBaseActivity.noIDAlert(context, R.string.BDPushAppID_Toast);
    } else {

      PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, BDPushAppKey);
      PushSettings.enableDebugMode(context, true);
    }
  }
}
