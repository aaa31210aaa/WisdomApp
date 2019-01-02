package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.YjWzBean;
import demo.yqh.wisdomapp.R;

public class YjWzAdapter extends BaseQuickAdapter<YjWzBean, BaseViewHolder> {
    public YjWzAdapter(@LayoutRes int layoutResId, @Nullable List<YjWzBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, YjWzBean item) {
        helper.setText(R.id.yjwz_item_materialsname, "应急物资名称：" + item.getMaterialsname());
        helper.setText(R.id.yjwz_item_materialssum, "应急物资数量：" + item.getMaterialssum());
        helper.setText(R.id.yjwz_item_ohaddres, "物资存放位置：" + item.getOhaddres());
        helper.setText(R.id.yjwz_item_modifydate, "更新时间：" + item.getModifydate());

    }
}
