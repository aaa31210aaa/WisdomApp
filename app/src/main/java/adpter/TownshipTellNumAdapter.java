package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.TownshipTellNumBean;
import demo.yqh.wisdomapp.R;

public class TownshipTellNumAdapter extends BaseQuickAdapter<TownshipTellNumBean, BaseViewHolder> {
    public TownshipTellNumAdapter(@LayoutRes int layoutResId, @Nullable List<TownshipTellNumBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TownshipTellNumBean item) {
        helper.setText(R.id.yjjy_item7_tv1, "乡镇名称：" + item.getTownname());
        helper.setText(R.id.yjjy_item7_tv2, "联系人姓名：" + item.getPolicename());
        helper.setText(R.id.yjjy_item7_tv3, "主管工作：" + item.getPostname());
        helper.setText(R.id.yjjy_item7_tv4, "职务：" + item.getStationname());
        helper.setText(R.id.yjjy_item7_tv5, "联系电话：" + item.getLinktel());
        helper.setText(R.id.yjjy_item7_tv6, "情况说明：" + item.getPolicecontent());
        helper.setText(R.id.yjjy_item7_tv7, "备注：" + item.getMemo());
    }
}
