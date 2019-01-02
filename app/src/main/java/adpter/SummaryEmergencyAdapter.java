package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.SummaryEmergencyBean;
import demo.yqh.wisdomapp.R;

public class SummaryEmergencyAdapter extends BaseQuickAdapter<SummaryEmergencyBean, BaseViewHolder> {
    public SummaryEmergencyAdapter(@LayoutRes int layoutResId, @Nullable List<SummaryEmergencyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SummaryEmergencyBean item) {
        helper.setText(R.id.yjjy_item4_tv1, "总结评估单位：" + item.getEvaluaunitName());
        helper.setText(R.id.yjjy_item4_tv2, "评估人：" + item.getEvaluaman());
        helper.setText(R.id.yjjy_item4_tv3, "评估时间：" + item.getEvaluatime());
        helper.setText(R.id.yjjy_item4_tv4, "评估内容：" + item.getValuacontent());
    }
}
