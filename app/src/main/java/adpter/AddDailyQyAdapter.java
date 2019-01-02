package adpter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.DeptBean;
import demo.yqh.wisdomapp.R;


public class AddDailyQyAdapter extends BaseQuickAdapter<DeptBean.CellsBean, BaseViewHolder> {
    public AddDailyQyAdapter(@LayoutRes int layoutResId, @Nullable List<DeptBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeptBean.CellsBean item) {
        helper.setText(R.id.dialog_item_list_item_tv, item.getName());
    }
}
