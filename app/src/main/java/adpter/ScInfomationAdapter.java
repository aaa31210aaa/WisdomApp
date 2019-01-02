package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.ScInfomationBean;
import demo.yqh.wisdomapp.R;

public class ScInfomationAdapter extends BaseQuickAdapter<ScInfomationBean, BaseViewHolder> {
    public ScInfomationAdapter(@LayoutRes int layoutResId, @Nullable List<ScInfomationBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScInfomationBean item) {
        helper.setText(R.id.bmgl_item_parentdeptname, "被检查单位：" + item.getCheckname());
        helper.setText(R.id.bmgl_item_deptname, "查处问题：" + item.getProblems());
        helper.setText(R.id.bmgl_item_deptcode, "是否提交：" + item.getIscommit());
    }
}
