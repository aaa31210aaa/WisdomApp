package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.AccidentBean;
import demo.yqh.wisdomapp.R;

public class AccidentAdapter extends BaseQuickAdapter<AccidentBean, BaseViewHolder> {
    public AccidentAdapter(@LayoutRes int layoutResId, @Nullable List<AccidentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AccidentBean item) {
        helper.setText(R.id.yjjy_item3_tv1, item.getAccidentName());
        helper.setText(R.id.yjjy_item3_tv2, item.getPublisher());
        helper.setText(R.id.yjjy_item3_tv3, item.getReleaseTime());
    }
}
