package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.RyGlBean;
import demo.yqh.wisdomapp.R;

public class RyGlAdapter extends BaseQuickAdapter<RyGlBean, BaseViewHolder> {
    public RyGlAdapter(@LayoutRes int layoutResId, @Nullable List<RyGlBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RyGlBean item) {
        helper.setText(R.id.rygl_item_sptypename, "人员类型：" + item.getSptypename());
        helper.setText(R.id.rygl_item_mainhead, "姓名：" + item.getMainhead());
        helper.setText(R.id.rygl_item_deptname, "所在部门：" + item.getDeptname());
        helper.setText(R.id.rygl_item_yxstardate, "有效时间：" + item.getYxstardate());
    }
}
