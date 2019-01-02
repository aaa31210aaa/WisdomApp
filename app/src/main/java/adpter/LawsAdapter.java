package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.LawsBean;
import demo.yqh.wisdomapp.R;

public class LawsAdapter extends BaseQuickAdapter<LawsBean.CellsBean, BaseViewHolder> {

    public LawsAdapter(@LayoutRes int layoutResId, @Nullable List<LawsBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LawsBean.CellsBean item) {
        helper.setText(R.id.laws_item_lrtitle, item.getLrtitle());
        helper.setText(R.id.laws_item_industryname, "所属行业：" + item.getIndustryname());
        helper.setText(R.id.laws_item_lrtypename, "所属具体类别：" + item.getLrtypename());
    }
}
