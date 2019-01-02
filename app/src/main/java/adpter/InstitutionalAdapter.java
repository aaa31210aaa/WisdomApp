package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.InstitutionalBean;
import demo.yqh.wisdomapp.R;

public class InstitutionalAdapter extends BaseQuickAdapter<InstitutionalBean.CellsBean, BaseViewHolder> {
    public InstitutionalAdapter(@LayoutRes int layoutResId, @Nullable List<InstitutionalBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InstitutionalBean.CellsBean item) {
        helper.setText(R.id.yjjy_item3_tv1, item.getPersonname());
        helper.setText(R.id.yjjy_item3_tv2, item.getPersontel());
        helper.setText(R.id.yjjy_item3_tv3, item.getPersonzw());
    }
}
