package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.QyYhInformationBean;
import demo.yqh.wisdomapp.R;

public class RiskListAdapter extends BaseQuickAdapter<QyYhInformationBean.CellsBean, BaseViewHolder> {
    public RiskListAdapter(@LayoutRes int layoutResId, @Nullable List<QyYhInformationBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QyYhInformationBean.CellsBean item) {
        helper.setText(R.id.risklist_item_crname, "隐患名称：" + item.getCrname());
        helper.setText(R.id.risklist_item_crtypename, "隐患类型：" + item.getCrtypename());
        helper.setText(R.id.risklist_item_pcdate, "排查日期：" + item.getPcdate());

    }
}
