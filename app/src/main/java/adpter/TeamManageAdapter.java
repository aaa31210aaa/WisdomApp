package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.TeamManageBean;
import demo.yqh.wisdomapp.R;

public class TeamManageAdapter extends BaseQuickAdapter<TeamManageBean.CellsBean, BaseViewHolder> {
    public TeamManageAdapter(@LayoutRes int layoutResId, @Nullable List<TeamManageBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TeamManageBean.CellsBean item) {
        helper.setText(R.id.yjjy_item4_tv1, "队伍名称：" + item.getRanksname());
        helper.setText(R.id.yjjy_item4_tv2, "队伍类型：" + item.getRankstype());
        helper.setText(R.id.yjjy_item4_tv3, "所在区域：" + item.getAreas());
        helper.setText(R.id.yjjy_item4_tv4, "负责人：" + item.getOwnman());
    }
}
