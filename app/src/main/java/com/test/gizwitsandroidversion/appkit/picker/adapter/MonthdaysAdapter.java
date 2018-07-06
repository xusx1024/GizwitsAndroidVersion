package com.test.gizwitsandroidversion.appkit.picker.adapter;

import android.graphics.Color;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.gizwitsandroidversion.R;
import com.test.gizwitsandroidversion.appkit.picker.model.DayCheckedModel;
import java.util.ArrayList;

/**
 * Created by archer. on 2018/7/2.
 */

public class MonthdaysAdapter extends BaseQuickAdapter<DayCheckedModel, BaseViewHolder> {

  public MonthdaysAdapter() {
    super(R.layout.adapter_monthdays_item, new ArrayList<DayCheckedModel>() {{
      for (int i = 1; i <= 31; i++) {
        add(new DayCheckedModel(i, false));
      }
    }});
  }

  @Override protected void convert(BaseViewHolder helper, final DayCheckedModel item) {
    helper.setText(R.id.day, item.getDayStr());
    helper.setBackgroundColor(R.id.day,
        Color.parseColor(item.isChecked() ? "#999999" : "#00000000"));
  }
}
