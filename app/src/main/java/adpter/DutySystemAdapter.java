package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.DutySystemBean;
import demo.yqh.wisdomapp.R;

public class DutySystemAdapter extends BaseQuickAdapter<DutySystemBean.CellsBean, BaseViewHolder> {
    public DutySystemAdapter(@LayoutRes int layoutResId, @Nullable List<DutySystemBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DutySystemBean.CellsBean item) {
        helper.setText(R.id.yjjy_item4_tv1,item.getCbname());
        helper.setText(R.id.yjjy_item4_tv2,item.getCbtypename());
        helper.setText(R.id.yjjy_item4_tv3,item.getEfftime());
        helper.setText(R.id.yjjy_item4_tv4,item.getQyname());
    }
}
