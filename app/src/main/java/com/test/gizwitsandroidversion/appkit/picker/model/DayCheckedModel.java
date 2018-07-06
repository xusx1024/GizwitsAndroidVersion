package com.test.gizwitsandroidversion.appkit.picker.model;

/**
 * Created by archer. on 2018/7/2.
 */

public class DayCheckedModel {
  private int day;
  private boolean checked;

  public DayCheckedModel(int day, boolean checked) {
    this.day = day;
    this.checked = checked;
  }

  public int getDay() {
    return day;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public String getDayStr() {
    return String.valueOf(day);
  }

  public boolean isChecked() {
    return checked;
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
  }

  @Override public String toString() {
    return "DayCheckedModel{" + "day=" + day + ", checked=" + checked + '}';
  }
}
