package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.XcJcBean;
import demo.yqh.wisdomapp.R;

public class JobListAdapter extends BaseQuickAdapter<XcJcBean, BaseViewHolder> {
    public JobListAdapter(@LayoutRes int layoutResId, @Nullable List<XcJcBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, XcJcBean item) {
//        helper.setBackgroundRes(R.id.job_list_item_image, item.getImage());
        helper.setText(R.id.job_list_item_qyname, item.getBjcdw());
        helper.setText(R.id.job_list_item_content, item.getRemark());
        helper.setText(R.id.job_list_item_handlingstatusname,item.getHandlingstatusname());
        helper.setText(R.id.job_list_item_zgdate, item.getStarttime() + "---" + item.getEndtime());
    }
}
