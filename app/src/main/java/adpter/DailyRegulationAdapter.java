package adpter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.DailyRegulationBean;
import demo.yqh.wisdomapp.R;


public class DailyRegulationAdapter extends BaseQuickAdapter<DailyRegulationBean, BaseViewHolder> {
    public DailyRegulationAdapter(@LayoutRes int layoutResId, @Nullable List<DailyRegulationBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DailyRegulationBean item) {
        helper.setText(R.id.yjjy_item3_tv1, "企业名称：" + item.getComname());
        helper.setText(R.id.yjjy_item3_tv2, "检查人：" + item.getJcr());
        helper.setText(R.id.yjjy_item3_tv3, "检查时间：" + item.getJcsj());
    }
}
