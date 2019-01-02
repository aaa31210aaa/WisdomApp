package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.WorkInfoBean;
import demo.yqh.wisdomapp.R;

public class WorkInfoAdapter extends BaseQuickAdapter<WorkInfoBean.CellsBean, BaseViewHolder> {
    public WorkInfoAdapter(@LayoutRes int layoutResId, @Nullable List<WorkInfoBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkInfoBean.CellsBean item) {
        helper.setText(R.id.yjjy_item4_tv1, "姓名：" + item.getJcr());
        helper.setText(R.id.yjjy_item4_tv2, "检查时间：" + item.getJcsj());
        helper.setText(R.id.yjjy_item4_tv3, "检查情况：" + item.getJcfxwt());
        helper.setText(R.id.yjjy_item4_tv4, "处理措施：" + item.getWtcljy());
    }
}
