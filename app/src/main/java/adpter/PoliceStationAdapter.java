package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.PoliceStationBean;
import demo.yqh.wisdomapp.R;

public class PoliceStationAdapter extends BaseQuickAdapter<PoliceStationBean, BaseViewHolder> {
    public PoliceStationAdapter(@LayoutRes int layoutResId, @Nullable List<PoliceStationBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PoliceStationBean item) {
        helper.setText(R.id.yjjy_item7_tv1, "乡镇名称：" + item.getTownname());
        helper.setText(R.id.yjjy_item7_tv2, "派出所名称：" + item.getPolicename());
        helper.setText(R.id.yjjy_item7_tv3, "主管领导：" + item.getPostname());
        helper.setText(R.id.yjjy_item7_tv4, "职务：" + item.getStationname());
        helper.setText(R.id.yjjy_item7_tv5, "联系电话：" + item.getLinktel());
        helper.setText(R.id.yjjy_item7_tv6, "情况说明：" + item.getPolicecontent());
        helper.setText(R.id.yjjy_item7_tv7, "备注：" + item.getMemo());

    }
}
