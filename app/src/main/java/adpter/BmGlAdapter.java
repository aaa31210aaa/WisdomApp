package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.BmGlBean;
import demo.yqh.wisdomapp.R;

public class BmGlAdapter extends BaseQuickAdapter<BmGlBean.CellsBean, BaseViewHolder> {
    public BmGlAdapter(@LayoutRes int layoutResId, @Nullable List<BmGlBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BmGlBean.CellsBean item) {
        helper.setText(R.id.bmgl_item_parentdeptname, "单位名称：" + item.getParentdeptname());
        helper.setText(R.id.bmgl_item_deptname, "部门名称：" + item.getDeptname());
        helper.setText(R.id.bmgl_item_deptcode, "部门编号：" + item.getDeptcode());
    }
}
