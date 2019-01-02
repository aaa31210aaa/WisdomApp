package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.YbzlistWbzlistBean;
import demo.yqh.wisdomapp.R;

public class YbzlistWbzlistAdapter extends BaseQuickAdapter<YbzlistWbzlistBean.CellsBean, BaseViewHolder> {
    private String type;

    public YbzlistWbzlistAdapter(@LayoutRes int layoutResId, @Nullable List<YbzlistWbzlistBean.CellsBean> data, String type) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, YbzlistWbzlistBean.CellsBean item) {
        if (type.equals("已制定")){
            helper.setText(R.id.yjjy_item3_tv1, "企业名称：" + item.getQyname());
            helper.setText(R.id.yjjy_item3_tv2, "行业领域：" + item.getHylyname());
            helper.setText(R.id.yjjy_item3_tv3, "行业标准：" + item.getHybzname());
        }else{
            helper.setText(R.id.yjjy_item3_tv1, "企业名称：" + item.getComname());
            helper.setText(R.id.yjjy_item3_tv2, "企业类型：" + item.getMainfield());
            helper.setText(R.id.yjjy_item3_tv3, "行业标准：" + item.getSetupdate());
        }
    }
}
