package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.ZkdsryglBean;
import demo.yqh.wisdomapp.R;

public class LxxzldAdapter extends BaseQuickAdapter<ZkdsryglBean.CellsBean, BaseViewHolder> {
    public LxxzldAdapter(@LayoutRes int layoutResId, @Nullable List<ZkdsryglBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ZkdsryglBean.CellsBean item) {
        helper.setText(R.id.yjjy_item5_tv1, "姓名：" + item.getFusername());
        helper.setText(R.id.yjjy_item5_tv2, "乡镇" + item.getTownshipname());
        helper.setText(R.id.yjjy_item5_tv3, "联系电话：" + item.getLinktel());
        helper.setText(R.id.yjjy_item5_tv4, "驻矿企业：" + item.getComname());
        helper.setText(R.id.yjjy_item5_tv5, "驻矿时间：" + item.getInminetime());

    }
}
