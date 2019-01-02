package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.DutyRecordBean;
import demo.yqh.wisdomapp.R;

public class DutyRecordAdapter extends BaseQuickAdapter<DutyRecordBean.CellsBean, BaseViewHolder> {
    public DutyRecordAdapter(@LayoutRes int layoutResId, @Nullable List<DutyRecordBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DutyRecordBean.CellsBean item) {
        helper.setText(R.id.yjjy_item4_tv1, "启动事件：" + item.getSummaryname());
        helper.setText(R.id.yjjy_item4_tv2, "计划名称：" + item.getDutyPlanName());
        helper.setText(R.id.yjjy_item4_tv3, "记录人：" + item.getRecordman());
        helper.setText(R.id.yjjy_item4_tv4, "记录时间：" + item.getRecorddate());
    }
}
