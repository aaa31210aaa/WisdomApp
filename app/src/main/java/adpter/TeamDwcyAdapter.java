package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.TeamDwcyBean;
import demo.yqh.wisdomapp.R;

public class TeamDwcyAdapter extends BaseQuickAdapter<TeamDwcyBean, BaseViewHolder> {

    public TeamDwcyAdapter(@LayoutRes int layoutResId, @Nullable List<TeamDwcyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TeamDwcyBean item) {
        helper.setText(R.id.yjjy_item5_tv1, item.getPersonname());
        helper.setText(R.id.yjjy_item5_tv2, item.getPersontel());
        helper.setText(R.id.yjjy_item5_tv3, item.getPersonzw());
        helper.setText(R.id.yjjy_item5_tv4, item.getPersonzc());
        helper.setText(R.id.yjjy_item5_tv5, item.getPersonmail());
    }
}
