package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.LeadershipBean;
import demo.yqh.wisdomapp.R;

public class LeadershipAdapter extends BaseQuickAdapter<LeadershipBean.CellsBean, BaseViewHolder> {
    public LeadershipAdapter(@LayoutRes int layoutResId, @Nullable List<LeadershipBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LeadershipBean.CellsBean item) {
        helper.setText(R.id.yjjy_item3_tv1, item.getRanksname());
        helper.setText(R.id.yjjy_item3_tv2, item.getLinkman());
        helper.setText(R.id.yjjy_item3_tv3, item.getLinktel());
    }
}
