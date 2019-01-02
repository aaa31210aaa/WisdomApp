package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.YjYlBean;
import demo.yqh.wisdomapp.R;

public class YjYlAdapter extends BaseQuickAdapter<YjYlBean, BaseViewHolder> {
    public YjYlAdapter(@LayoutRes int layoutResId, @Nullable List<YjYlBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, YjYlBean item) {
        helper.setText(R.id.yjyl_item_drilldate, "演练时间：" + item.getDrilldate());
        helper.setText(R.id.yjyl_item_drillcontent, "演练内容：" + item.getDrillcontent());
        helper.setText(R.id.yjyl_item_drillman, "参与人员：" + item.getDrillman());
        helper.setText(R.id.yjyl_item_reservname, "演练预案：" + item.getReservname());

    }
}
