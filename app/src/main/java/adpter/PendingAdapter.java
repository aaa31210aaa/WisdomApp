package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.PendingBean;
import demo.yqh.wisdomapp.R;

public class PendingAdapter extends BaseQuickAdapter<PendingBean.CellsBean, BaseViewHolder> {

    public PendingAdapter(@LayoutRes int layoutResId, @Nullable List<PendingBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PendingBean.CellsBean item) {
        helper.setText(R.id.yjjy_item3_tv1, "计划名称：" + item.getCpname());
        helper.setText(R.id.yjjy_item3_tv2, "计划类型：" + item.getChecktypename());
        helper.setText(R.id.yjjy_item3_tv3, "计划日期：" + item.getCheckstatar() + "---" + item.getCheckend());

    }
}
