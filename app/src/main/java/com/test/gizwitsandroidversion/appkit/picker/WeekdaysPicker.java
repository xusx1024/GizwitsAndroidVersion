package com.test.gizwitsandroidversion.appkit.picker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.gizwits.gizwifisdk.enumration.GizScheduleWeekday;
import com.test.gizwitsandroidversion.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by archer. on 2018/7/2.
 */

public class WeekdaysPicker extends DialogFragment implements View.OnClickListener {

  private OnWeekdaysPickerListener onWeekdaysPickerListener;

  private FrameLayout sunday;
  private FrameLayout monday;
  private FrameLayout tuesday;
  private FrameLayout wednesday;
  private FrameLayout thursday;
  private FrameLayout friday;
  private FrameLayout saturday;

  private ImageView sundayChecked;
  private ImageView mondayChecked;
  private ImageView tuesdayChecked;
  private ImageView wednesdayChecked;
  private ImageView thursdayChecked;
  private ImageView fridayChecked;
  private ImageView saturdayChecked;

  private Button btnConfirm;

  private List<GizScheduleWeekday> weekdays;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_dialog_weekdays, container, false);

    sunday = (FrameLayout) rootView.findViewById(R.id.sunday);
    monday = (FrameLayout) rootView.findViewById(R.id.monday);
    tuesday = (FrameLayout) rootView.findViewById(R.id.tuesday);
    wednesday = (FrameLayout) rootView.findViewById(R.id.wednesday);
    thursday = (FrameLayout) rootView.findViewById(R.id.thursday);
    friday = (FrameLayout) rootView.findViewById(R.id.friday);
    saturday = (FrameLayout) rootView.findViewById(R.id.saturday);

    sundayChecked = (ImageView) rootView.findViewById(R.id.sunday_checked);
    mondayChecked = (ImageView) rootView.findViewById(R.id.monday_checked);
    tuesdayChecked = (ImageView) rootView.findViewById(R.id.tuesday_checked);
    wednesdayChecked = (ImageView) rootView.findViewById(R.id.wednesday_checked);
    thursdayChecked = (ImageView) rootView.findViewById(R.id.thursday_checked);
    fridayChecked = (ImageView) rootView.findViewById(R.id.friday_checked);
    saturdayChecked = (ImageView) rootView.findViewById(R.id.saturday_checked);

    btnConfirm = (Button) rootView.findViewById(R.id.btn_confirm);

    return rootView;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    if (weekdays == null) {
      weekdays = new ArrayList<>();
    }

    sunday.setOnClickListener(this);
    monday.setOnClickListener(this);
    tuesday.setOnClickListener(this);
    wednesday.setOnClickListener(this);
    thursday.setOnClickListener(this);
    friday.setOnClickListener(this);
    saturday.setOnClickListener(this);

    btnConfirm.setOnClickListener(this);

    updateUI();
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.sunday:
        put(GizScheduleWeekday.GizScheduleSunday);
        break;
      case R.id.monday:
        put(GizScheduleWeekday.GizScheduleMonday);
        break;
      case R.id.tuesday:
        put(GizScheduleWeekday.GizScheduleTuesday);
        break;
      case R.id.wednesday:
        put(GizScheduleWeekday.GizScheduleWednesday);
        break;
      case R.id.thursday:
        put(GizScheduleWeekday.GizScheduleThursday);
        break;
      case R.id.friday:
        put(GizScheduleWeekday.GizScheduleFriday);
        break;
      case R.id.saturday:
        put(GizScheduleWeekday.GizScheduleSaturday);
        break;
      case R.id.btn_confirm:
        confirm();
        break;
    }
    updateUI();
  }

  private void updateUI() {
    sundayChecked.setVisibility(View.GONE);
    mondayChecked.setVisibility(View.GONE);
    tuesdayChecked.setVisibility(View.GONE);
    wednesdayChecked.setVisibility(View.GONE);
    thursdayChecked.setVisibility(View.GONE);
    fridayChecked.setVisibility(View.GONE);
    saturdayChecked.setVisibility(View.GONE);

    for (GizScheduleWeekday weekday : weekdays) {
      switch (weekday) {
        case GizScheduleSunday:
          sundayChecked.setVisibility(View.VISIBLE);
          break;
        case GizScheduleMonday:
          mondayChecked.setVisibility(View.VISIBLE);
          break;
        case GizScheduleTuesday:
          tuesdayChecked.setVisibility(View.VISIBLE);
          break;
        case GizScheduleWednesday:
          wednesdayChecked.setVisibility(View.VISIBLE);
          break;
        case GizScheduleThursday:
          thursdayChecked.setVisibility(View.VISIBLE);
          break;
        case GizScheduleFriday:
          fridayChecked.setVisibility(View.VISIBLE);
          break;
        case GizScheduleSaturday:
          saturdayChecked.setVisibility(View.VISIBLE);
          break;
      }
    }
  }

  private String weekdaysToString() {
    int index = 0;

    StringBuilder builder = new StringBuilder();
    if (weekdays.contains(GizScheduleWeekday.GizScheduleSunday)) {
      builder.append("日");
      appendSeparator(builder, index++);
    }

    if (weekdays.contains(GizScheduleWeekday.GizScheduleMonday)) {
      builder.append("一");
      appendSeparator(builder, index++);
    }

    if (weekdays.contains(GizScheduleWeekday.GizScheduleTuesday)) {
      builder.append("二");
      appendSeparator(builder, index++);
    }

    if (weekdays.contains(GizScheduleWeekday.GizScheduleWednesday)) {
      builder.append("三");
      appendSeparator(builder, index++);
    }

    if (weekdays.contains(GizScheduleWeekday.GizScheduleThursday)) {
      builder.append("四");
      appendSeparator(builder, index++);
    }

    if (weekdays.contains(GizScheduleWeekday.GizScheduleFriday)) {
      builder.append("五");
      appendSeparator(builder, index++);
    }

    if (weekdays.contains(GizScheduleWeekday.GizScheduleSaturday)) {
      builder.append("六");
      appendSeparator(builder, index++);
    }

    return builder.toString();
  }

  private void appendSeparator(StringBuilder builder, int index) {
    if (index != weekdays.size() - 1) {
      builder.append("、");
    }
  }

  private void confirm() {
    if (onWeekdaysPickerListener != null) {
      onWeekdaysPickerListener.onWeekdays(weekdays, weekdaysToString());
    }
    dismiss();
  }

  private void put(GizScheduleWeekday day) {
    if (weekdays.contains(day)) {
      weekdays.remove(day);
    } else {
      weekdays.add(day);
    }
  }

  public void setOnWeekdaysPickerListener(OnWeekdaysPickerListener onWeekdaysPickerListener) {
    this.onWeekdaysPickerListener = onWeekdaysPickerListener;
  }

  public void setWeekdays(List<GizScheduleWeekday> weekdays) {
    this.weekdays = weekdays;
  }

  public interface OnWeekdaysPickerListener {
    void onWeekdays(List<GizScheduleWeekday> weekdays, String weekdaysText);
  }
}
