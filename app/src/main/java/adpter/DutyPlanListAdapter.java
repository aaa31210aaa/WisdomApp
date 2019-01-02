package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.DutyPlanListBean;
import demo.yqh.wisdomapp.R;

public class DutyPlanListAdapter extends BaseQuickAdapter<DutyPlanListBean.CellsBean, BaseViewHolder> {
    public DutyPlanListAdapter(@LayoutRes int layoutResId, @Nullable List<DutyPlanListBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DutyPlanListBean.CellsBean item) {
        helper.setText(R.id.yjjy_item4_tv1, "值班名称：" + item.getDutyPlanName());
        helper.setText(R.id.yjjy_item4_tv2, "值班人：" + item.getDutyman());
        helper.setText(R.id.yjjy_item4_tv3, "班次：" + item.getShiftmunname());
        helper.setText(R.id.yjjy_item4_tv4, "值班时间：" + item.getStarttime() + "---" + item.getEndtime());
    }
}
