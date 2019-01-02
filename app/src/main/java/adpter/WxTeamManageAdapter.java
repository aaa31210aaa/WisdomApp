package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.WxTeamManageBean;
import demo.yqh.wisdomapp.R;

public class WxTeamManageAdapter extends BaseQuickAdapter<WxTeamManageBean, BaseViewHolder> {
    public WxTeamManageAdapter(@LayoutRes int layoutResId, @Nullable List<WxTeamManageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WxTeamManageBean item) {
        helper.setText(R.id.yjjy_item3_tv1,"外协单位名称："+item.getExternaldeptname());
        helper.setText(R.id.yjjy_item3_tv2,"负责人："+item.getExternaldeptname());
        helper.setText(R.id.yjjy_item3_tv3,"机构类型："+item.getExternaldeptname());
    }
}
