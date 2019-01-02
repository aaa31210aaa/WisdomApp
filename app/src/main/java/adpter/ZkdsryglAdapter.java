package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.ZkdsryglBean;
import demo.yqh.wisdomapp.R;

public class ZkdsryglAdapter extends BaseQuickAdapter<ZkdsryglBean.CellsBean, BaseViewHolder> {
    public ZkdsryglAdapter(@LayoutRes int layoutResId, @Nullable List<ZkdsryglBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ZkdsryglBean.CellsBean item) {
        helper.setText(R.id.yjjy_item3_tv1,"人员类型："+item.getParamname());
        helper.setText(R.id.yjjy_item3_tv2,"姓名："+item.getFusername());
        helper.setText(R.id.yjjy_item3_tv3,"乡镇："+item.getTownshipname());
    }
}
