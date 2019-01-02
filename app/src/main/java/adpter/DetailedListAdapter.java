package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.DetailedListBean;
import demo.yqh.wisdomapp.R;

public class DetailedListAdapter extends BaseQuickAdapter<DetailedListBean.CellsBean, BaseViewHolder> {
    public DetailedListAdapter(@LayoutRes int layoutResId, @Nullable List<DetailedListBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DetailedListBean.CellsBean item) {
        helper.setText(R.id.yjjy_item3_tv1,"标准描述："+item.getRscdesc());
        helper.setText(R.id.yjjy_item3_tv2,"参考依据："+item.getRsckyj());
        helper.setText(R.id.yjjy_item3_tv3,"排查周期："+item.getPczq());

    }
}
