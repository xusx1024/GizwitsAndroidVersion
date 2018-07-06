package com.test.gizwitsandroidversion.appkit.picker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.skydoves.colorpickerview.ColorListener;
import com.skydoves.colorpickerview.ColorPickerView;
import com.test.gizwitsandroidversion.R;

/**
 * Created by archer. on 2018/7/2.
 */

public class ColorPicker extends DialogFragment implements ColorListener, View.OnClickListener {

  private OnColorPickerListener onColorPickerListener;
  private ColorPickerView colorPickerView;
  private View currentColor;
  private View confirm;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_dialog_picker_color, container, false);
    colorPickerView = (ColorPickerView) rootView.findViewById(R.id.colorPickerView);
    currentColor = rootView.findViewById(R.id.currentColor);
    confirm = rootView.findViewById(R.id.btn_confirm);
    return rootView;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    initEvnet();
  }

  private void initEvnet() {
    colorPickerView.setColorListener(this);
    confirm.setOnClickListener(this);
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_confirm:
        confirm();
        break;
    }
  }

  private void confirm() {
    if (onColorPickerListener != null) {
      int[] colorRGB = colorPickerView.getColorRGB();
      onColorPickerListener.onColorSelected(colorRGB[0], colorRGB[1], colorRGB[2]);
    }
    dismiss();
  }

  @Override public void onColorSelected(int color) {
    currentColor.setBackgroundColor(color);
  }

  public void setOnColorPickerListener(OnColorPickerListener onColorPickerListener) {
    this.onColorPickerListener = onColorPickerListener;
  }

  public interface OnColorPickerListener {
    void onColorSelected(int red, int green, int blue);
  }
}
