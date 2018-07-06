package com.test.gizwitsandroidversion.appkit.sharingdevice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizMessage;
import com.gizwits.gizwifisdk.enumration.GizMessageStatus;
import com.gizwits.gizwifisdk.enumration.GizMessageType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;
import com.test.gizwitsandroidversion.R;
import com.test.gizwitsandroidversion.appkit.CommonModule.GosBaseActivity;
import java.util.List;

public class messageCenterActivity extends GosBaseActivity {

  private View redpoint;
  private LinearLayout gizwitsmes;
  private LinearLayout deviceshared;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_gos_message);

    initView();
  }

  @Override public void onResume() {
    super.onResume();
    String token = spf.getString("Token", "");
    GizDeviceSharing.queryMessageList(token, GizMessageType.GizMessageSharing);
    //GizDeviceSharing.queryMessageList(token, GizMessageType.GizMessageSystem);

    GizDeviceSharing.setListener(new GizDeviceSharingListener() {

      @SuppressLint("WrongConstant") @Override
      public void didQueryMessageList(GizWifiErrorCode result, List<GizMessage> messageList) {
        super.didQueryMessageList(result, messageList);

        if (messageList.size() > 0) {
          isShowRedPoint(messageList);
        } else {
          redpoint.setVisibility(View.GONE);
        }

        if (result.ordinal() != 0) {
          Toast.makeText(messageCenterActivity.this, toastError(result), 2).show();
        }
      }
    });
  }

  private void isShowRedPoint(List<GizMessage> messageList) {

    boolean isshow = false;
    redpoint.setVisibility(View.GONE);
    for (int i = 0; i < messageList.size(); i++) {

      GizMessage gizMessage = messageList.get(i);
      GizMessageStatus status = gizMessage.getStatus();
      if (status.ordinal() == 0) {
        isshow = true;

        redpoint.setVisibility(View.VISIBLE);
      }
    }
  }

  private void initView() {
    // 判断当前的view 是否需要显示这个红点
    redpoint = findViewById(R.id.redpoint);

    gizwitsmes = (LinearLayout) findViewById(R.id.gizwitsmes);

    deviceshared = (LinearLayout) findViewById(R.id.deviceshared);
  }

  // 跳转到设备分享的界面
  public void deviceshared(View v) {

    Intent tent = new Intent(this, deviceSharedMessageActivity.class);
    startActivity(tent);

    deviceshared.setEnabled(false);
    deviceshared.postDelayed(new Runnable() {
      @Override public void run() {
        deviceshared.setEnabled(true);
      }
    }, 1000);
  }

  // 跳转到机智云公告页面
  public void gizwitsmes(View v) {

    Intent intent = new Intent(this, MsgNoticeActivity.class);
    startActivity(intent);

    gizwitsmes.setEnabled(false);
    gizwitsmes.postDelayed(new Runnable() {
      @Override public void run() {
        gizwitsmes.setEnabled(true);
      }
    }, 1000);
  }
}
