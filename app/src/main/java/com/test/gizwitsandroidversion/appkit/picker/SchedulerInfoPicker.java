package com.test.gizwitsandroidversion.appkit.picker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.DateTimePicker;
import cn.addapp.pickers.picker.SinglePicker;
import com.gizwits.gizwifisdk.enumration.GizScheduleWeekday;
import com.test.gizwitsandroidversion.R;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by archer. on 2018/7/2.
 */

public class SchedulerInfoPicker extends DialogFragment implements View.OnClickListener {

  private OnSchedulerInfoPickerListener onSchedulerInfoPickerListener;

  private SchedulerInfo currentSchedulerInfo = new SchedulerInfo();

  private FrameLayout pickerType;
  private TextView pickerTypeText;
  private FrameLayout pickerTime;
  private TextView pickerTimeText;
  private FrameLayout pickerRepetition;
  private TextView pickerRepetitionText;
  private TextView pickerRepetitionDetail;
  private Button pickerConfirm;
  private FrameLayout pickerToggle;
  private Switch pickerToggleSwitch;
  private FrameLayout pickerColor;
  private View currentColor;

  private boolean isSetCurrentColor;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_dialog_scheduler_picker, container, false);
    pickerType = (FrameLayout) rootView.findViewById(R.id.picker_scheduler_type);
    pickerTypeText = (TextView) rootView.findViewById(R.id.picker_scheduler_type_text);
    pickerToggle = (FrameLayout) rootView.findViewById(R.id.picker_scheduler_toggle);
    pickerToggleSwitch = (Switch) rootView.findViewById(R.id.picker_scheduler_toggle_switch);
    pickerColor = (FrameLayout) rootView.findViewById(R.id.picker_scheduler_color);
    currentColor = rootView.findViewById(R.id.current_scheduler_color);
    pickerTime = (FrameLayout) rootView.findViewById(R.id.picker_scheduler_time);
    pickerTimeText = (TextView) rootView.findViewById(R.id.picker_scheduler_time_text);
    pickerRepetition = (FrameLayout) rootView.findViewById(R.id.picker_scheduler_repetition);
    pickerRepetitionText = (TextView) rootView.findViewById(R.id.picker_scheduler_repetition_text);
    pickerRepetitionDetail =
        (TextView) rootView.findViewById(R.id.picker_scheduler_repetition_detail);
    pickerConfirm = (Button) rootView.findViewById(R.id.btn_confirm);
    return rootView;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    pickerType.setOnClickListener(this);
    pickerColor.setOnClickListener(this);
    pickerTime.setOnClickListener(this);
    pickerRepetition.setOnClickListener(this);
    pickerConfirm.setOnClickListener(this);
  }

  @Override public void onClick(View view) {

    switch (view.getId()) {
      case R.id.picker_scheduler_type:
        onPickerScheduleType(pickerTypeText);
        break;
      case R.id.picker_scheduler_color:
        onColorPicker();
        break;
      case R.id.picker_scheduler_time:
        onPickerSchedulerTime(pickerTimeText);
        break;
      case R.id.picker_scheduler_repetition:
        onPickerRepetitionType(pickerRepetitionText);
        break;
      case R.id.btn_confirm:
        confirm();
        break;
    }
  }

  private void confirm() {

    if (TextUtils.isEmpty(pickerTypeText.getText())) {
      Toast.makeText(getActivity(), "请选择任务类型", Toast.LENGTH_SHORT).show();
      return;
    }

    if (pickerColor.getVisibility() == View.VISIBLE && !isSetCurrentColor) {
      Toast.makeText(getActivity(), "请选择颜色", Toast.LENGTH_SHORT).show();
      return;
    }

    if (TextUtils.isEmpty(pickerTimeText.getText())) {
      Toast.makeText(getActivity(), "请选择任务时间", Toast.LENGTH_SHORT).show();
      return;
    }

    switch (currentSchedulerInfo.getRepetitionType()) {
      case 1/*每周*/:
        List<GizScheduleWeekday> weekdays = currentSchedulerInfo.getWeekdays();
        if (weekdays == null || weekdays.size() == 0) {
          Toast.makeText(getActivity(), "请选择每周重复时间", Toast.LENGTH_SHORT).show();
          return;
        }
        break;
      case 2/*每月*/:
        List<Integer> monthdays = currentSchedulerInfo.getMonthdays();
        if (monthdays == null || monthdays.size() == 0) {
          Toast.makeText(getActivity(), "请选择每月重复时间", Toast.LENGTH_SHORT).show();
          return;
        }
        break;
    }

    currentSchedulerInfo.setToggle(pickerToggleSwitch.isChecked());

    if (onSchedulerInfoPickerListener != null) {
      onSchedulerInfoPickerListener.onSchedulerInfo(currentSchedulerInfo);
    }

    dismiss();
  }

  private void onColorPicker() {
    ColorPicker colorPicker = new ColorPicker();
    colorPicker.setOnColorPickerListener(new ColorPicker.OnColorPickerListener() {
      @Override public void onColorSelected(int red, int green, int blue) {
        currentColor.setBackgroundColor(Color.rgb(red, green, blue));
        currentSchedulerInfo.setRgb(new int[] { red, green, blue });
        isSetCurrentColor = true;
      }
    });
    colorPicker.show(getActivity().getSupportFragmentManager(), "colorPicker");
  }

  /**
   * 选择重复执行次数
   */
  public void onPickerRepetitionType(final View view) {
    boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
    final SinglePicker<String> picker =
        new SinglePicker<>(this.getActivity(), new String[] { "一次", "每周", "每月" });

    picker.setCanLoop(false);//不禁用循环
    picker.setTopBackgroundColor(0xFFEEEEEE);
    picker.setTopHeight(50);
    picker.setTopLineColor(0xFF33B5E5);
    picker.setTopLineHeight(1);
    picker.setTitleText("请选择");
    picker.setTitleTextColor(0xFF999999);
    picker.setTitleTextSize(12);
    picker.setCancelTextColor(0xFF33B5E5);
    picker.setCancelTextSize(13);
    picker.setSubmitTextColor(0xFF33B5E5);
    picker.setSubmitTextSize(13);
    picker.setSelectedTextColor(0xFFEE0000);
    picker.setUnSelectedTextColor(0xFF999999);
    picker.setWheelModeEnable(false);
    LineConfig config = new LineConfig();
    config.setColor(Color.BLUE);//线颜色
    config.setAlpha(120);//线透明度
    //        config.setRatio(1);//线比率
    picker.setLineConfig(config);
    picker.setItemWidth(200);
    picker.setBackgroundColor(0xFFE1E1E1);

    picker.setSelectedIndex(7);
    picker.setOnItemPickListener(new OnItemPickListener<String>() {
      @Override public void onItemPicked(int index, String item) {

        pickerRepetitionText.setText(item);
        pickerRepetitionDetail.setText("");
        pickerRepetitionDetail.setOnClickListener(null);
        switch (index) {
          case 0:
            currentSchedulerInfo.setRepetitionType(0);
            break;
          case 1:
            currentSchedulerInfo.setRepetitionType(1);
            pickerRepetitionDetail.setOnClickListener(new View.OnClickListener() {
              @Override public void onClick(View view) {
                onPickerWeekdays();
              }
            });
            onPickerWeekdays();
            break;
          case 2:
            currentSchedulerInfo.setRepetitionType(2);
            pickerRepetitionDetail.setOnClickListener(new View.OnClickListener() {
              @Override public void onClick(View view) {
                onPickerMonthdays();
              }
            });
            onPickerMonthdays();
            break;
        }
        Toast.makeText(getActivity(), "index=" + index + ", item=" + item, Toast.LENGTH_SHORT)
            .show();
      }
    });
    picker.show();
  }

  private void onPickerMonthdays() {
    MonthdaysPicker monthdaysPicker = new MonthdaysPicker();
    monthdaysPicker.setMonthdays(currentSchedulerInfo.getMonthdays());
    monthdaysPicker.setOnMonthdaysPickerListener(new MonthdaysPicker.OnMonthdaysPickerListener() {
      @Override public void onMonthdays(List<Integer> monthdays, String monthdaysText) {
        currentSchedulerInfo.setMonthdays(monthdays);
        pickerRepetitionDetail.setText(monthdaysText);
      }
    });
    monthdaysPicker.show(getActivity().getSupportFragmentManager(), "monthdaysPicker");
  }

  private void onPickerWeekdays() {
    WeekdaysPicker weekdaysPicker = new WeekdaysPicker();
    weekdaysPicker.setWeekdays(currentSchedulerInfo.weekdays);
    weekdaysPicker.setOnWeekdaysPickerListener(new WeekdaysPicker.OnWeekdaysPickerListener() {
      @Override public void onWeekdays(List<GizScheduleWeekday> weekdays, String weekdaysText) {
        currentSchedulerInfo.setWeekdays(weekdays);
        pickerRepetitionDetail.setText(weekdaysText);
        Toast.makeText(getActivity(), "weekdaysText: " + weekdaysText, Toast.LENGTH_SHORT).show();
      }
    });
    weekdaysPicker.show(getActivity().getSupportFragmentManager(), "weekdaysPicker");
  }

  /**
   * 选择定时任务执行的时间
   */
  public void onPickerSchedulerTime(final View view) {
    final DateTimePicker picker = new DateTimePicker(this.getActivity(), DateTimePicker.HOUR_24);
    picker.setActionButtonTop(false);
    picker.setDateRangeStart(2018, 1, 1);
    picker.setDateRangeEnd(2025, 11, 11);
    picker.setSelectedItem(2018, 7, 1, 19, 30);
    picker.setTimeRangeStart(9, 0);
    picker.setTimeRangeEnd(20, 30);
    picker.setCanLinkage(false);
    picker.setTitleText("请选择");
    //        picker.setStepMinute(5);
    picker.setWeightEnable(true);
    picker.setWheelModeEnable(true);
    LineConfig config = new LineConfig();
    config.setColor(Color.BLUE);//线颜色
    config.setAlpha(120);//线透明度
    config.setVisible(true);//线不显示 默认显示
    picker.setLineConfig(config);
    picker.setLabel(null, null, null, null, null);
    picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
      @Override public void onDateTimePicked(String year, String month, String day, String hour,
          String minute) {
        currentSchedulerInfo.setDate(year + "-" + month + "-" + day);
        currentSchedulerInfo.setTime(hour + ":" + minute);

        if (view instanceof TextView) {
          ((TextView) view).setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
        }

        Toast.makeText(getActivity(), year + "-" + month + "-" + day + " " + hour + ":" + minute,
            Toast.LENGTH_SHORT).show();
      }
    });
    picker.show();
  }

  /**
   * 选择定时任务类型
   */
  public void onPickerScheduleType(final View view) {
    boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
    SinglePicker<String> picker =
        new SinglePicker<>(this.getActivity(), new String[] { "开关灯", "改变灯的颜色" });

    picker.setCanLoop(false);//不禁用循环
    picker.setTopBackgroundColor(0xFFEEEEEE);
    picker.setTopHeight(50);
    picker.setTopLineColor(0xFF33B5E5);
    picker.setTopLineHeight(1);
    picker.setTitleText("请选择");
    picker.setTitleTextColor(0xFF999999);
    picker.setTitleTextSize(12);
    picker.setCancelTextColor(0xFF33B5E5);
    picker.setCancelTextSize(13);
    picker.setSubmitTextColor(0xFF33B5E5);
    picker.setSubmitTextSize(13);
    picker.setSelectedTextColor(0xFFEE0000);
    picker.setUnSelectedTextColor(0xFF999999);
    picker.setWheelModeEnable(false);
    LineConfig config = new LineConfig();
    config.setColor(Color.BLUE);//线颜色
    config.setAlpha(120);//线透明度
    //        config.setRatio(1);//线比率
    picker.setLineConfig(config);
    picker.setItemWidth(200);
    picker.setBackgroundColor(0xFFE1E1E1);

    picker.setSelectedIndex(7);
    picker.setOnItemPickListener(new OnItemPickListener<String>() {
      @Override public void onItemPicked(int index, String item) {
        if (view instanceof TextView) {
          ((TextView) view).setText(item);
        }
        switch (index) {
          case 0:
            pickerToggle.setVisibility(View.VISIBLE);
            pickerColor.setVisibility(View.GONE);
            currentSchedulerInfo.setRemark(item);
            currentSchedulerInfo.setType(0);
            break;
          case 1:
            pickerToggle.setVisibility(View.GONE);
            pickerColor.setVisibility(View.VISIBLE);
            currentSchedulerInfo.setRemark(item);
            currentSchedulerInfo.setType(1);
            break;
        }
        Toast.makeText(getActivity(), "index=" + index + ", item=" + item, Toast.LENGTH_SHORT)
            .show();
      }
    });
    picker.show();
  }

  public void setOnSchedulerInfoPickerListener(
      OnSchedulerInfoPickerListener onSchedulerInfoPickerListener) {
    this.onSchedulerInfoPickerListener = onSchedulerInfoPickerListener;
  }

  public interface OnSchedulerInfoPickerListener {
    void onSchedulerInfo(SchedulerInfo info);
  }

  public static class SchedulerInfo {
    private String remark;
    private int type; // 0. 开关， 1. 改变颜色
    private String date; //日期
    private String time; // 时间
    private boolean toggle; //开关状态
    private int[] rgb;// 颜色
    private int repetitionType; // 重复类型， 0. 一次， 1. 每周，2.每月
    private List<GizScheduleWeekday> weekdays; // 每周重复
    private List<Integer> monthdays;//每月重复

    public String getRemark() {
      return remark;
    }

    public void setRemark(String remark) {
      this.remark = remark;
    }

    public int getType() {
      return type;
    }

    public void setType(int type) {
      this.type = type;
    }

    public String getDate() {
      return date;
    }

    public void setDate(String date) {
      this.date = date;
    }

    public String getTime() {
      return time;
    }

    public void setTime(String time) {
      this.time = time;
    }

    public boolean isToggle() {
      return toggle;
    }

    public void setToggle(boolean toggle) {
      this.toggle = toggle;
    }

    public int[] getRgb() {
      return rgb;
    }

    public void setRgb(int[] rgb) {
      this.rgb = rgb;
    }

    public int getRepetitionType() {
      return repetitionType;
    }

    public void setRepetitionType(int repetitionType) {
      this.repetitionType = repetitionType;
    }

    public List<GizScheduleWeekday> getWeekdays() {
      return weekdays;
    }

    public void setWeekdays(List<GizScheduleWeekday> weekdays) {
      this.weekdays = weekdays;
    }

    public List<Integer> getMonthdays() {
      return monthdays;
    }

    public void setMonthdays(List<Integer> monthdays) {
      this.monthdays = monthdays;
    }

    @Override public String toString() {
      return "SchedulerInfo{"
          + "remark='"
          + remark
          + '\''
          + ", type="
          + type
          + ", date='"
          + date
          + '\''
          + ", time='"
          + time
          + '\''
          + ", toggle="
          + toggle
          + ", rgb="
          + Arrays.toString(rgb)
          + ", repetitionType="
          + repetitionType
          + ", weekdays="
          + weekdays
          + ", monthdays="
          + monthdays
          + '}';
    }
  }
}
