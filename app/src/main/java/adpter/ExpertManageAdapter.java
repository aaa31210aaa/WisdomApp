package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.ExpertManageBean;
import demo.yqh.wisdomapp.R;

public class ExpertManageAdapter extends BaseQuickAdapter<ExpertManageBean, BaseViewHolder> {
    public ExpertManageAdapter(@LayoutRes int layoutResId, @Nullable List<ExpertManageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ExpertManageBean item) {
        helper.setText(R.id.yjjy_item3_tv1,"姓名："+item.getExpertname());
        helper.setText(R.id.yjjy_item3_tv2,"固定电话："+item.getExpertname());
        helper.setText(R.id.yjjy_item3_tv3,"职位："+item.getExpertname());
    }
}
