package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.EnterpriseBean;
import demo.yqh.wisdomapp.R;

public class EnterpriseAdapter extends BaseQuickAdapter<EnterpriseBean.CellsBean, BaseViewHolder> {
    public EnterpriseAdapter(@LayoutRes int layoutResId, @Nullable List<EnterpriseBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EnterpriseBean.CellsBean item) {
        helper.setText(R.id.enterprise_item_comname, item.getComname());
        helper.setText(R.id.enterprise_item_industryname, item.getIndustryname());
        helper.setText(R.id.enterprise_item_zcaddress, item.getZcaddress());
        helper.setText(R.id.enterprise_item_tablename, item.getTablename());
    }
}
