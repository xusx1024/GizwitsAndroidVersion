package com.test.gizwitsandroidversion.appkit.picker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.test.gizwitsandroidversion.R;
import com.test.gizwitsandroidversion.appkit.picker.adapter.MonthdaysAdapter;
import com.test.gizwitsandroidversion.appkit.picker.model.DayCheckedModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by archer. on 2018/7/2.
 */

public class MonthdaysPicker extends DialogFragment {

  private OnMonthdaysPickerListener onMonthdaysPickerListener;

  private List<Integer> monthdays;

  private RecyclerView rvMonths;
  private Button btnConfirm;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_dialog_monthdays, container, false);
    rvMonths = (RecyclerView) rootView.findViewById(R.id.rv_months);
    btnConfirm = (Button) rootView.findViewById(R.id.btn_confirm);
    return rootView;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    if (monthdays == null) {
      monthdays = new ArrayList<>();
    }

    rvMonths.setLayoutManager(new GridLayoutManager(getActivity(), 7));
    final MonthdaysAdapter monthdaysAdapter = new MonthdaysAdapter();
    monthdaysAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        DayCheckedModel data = (DayCheckedModel) adapter.getData().get(position);
        data.setChecked(!data.isChecked());
        adapter.notifyItemChanged(position);

        if (data.isChecked()) {
          monthdays.add(data.getDay());
        } else {
          monthdays.remove(data.getDay());
        }
      }
    });

    for (Integer day : monthdays) {
      monthdaysAdapter.getData().get(day).setChecked(true);
    }

    rvMonths.setAdapter(monthdaysAdapter);

    btnConfirm.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (onMonthdaysPickerListener != null) {
          onMonthdaysPickerListener.onMonthdays(monthdays,
              monthdays.toString().replaceAll("[\\[\\]]", ""));
        }
        dismiss();
      }
    });
  }

  public void setOnMonthdaysPickerListener(OnMonthdaysPickerListener onMonthdaysPickerListener) {
    this.onMonthdaysPickerListener = onMonthdaysPickerListener;
  }

  public void setMonthdays(List<Integer> monthdays) {
    this.monthdays = monthdays;
  }

  public interface OnMonthdaysPickerListener {
    void onMonthdays(List<Integer> monthdays, String monthdaysText);
  }
}
