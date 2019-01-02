package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.AqJyPxBean;
import demo.yqh.wisdomapp.R;

public class AqJyPxAdapter extends BaseQuickAdapter<AqJyPxBean, BaseViewHolder> {
    private String mycode;

    public AqJyPxAdapter(@LayoutRes int layoutResId, @Nullable List<AqJyPxBean> data, String mycode) {
        super(layoutResId, data);
        this.mycode = mycode;
    }

    @Override
    protected void convert(BaseViewHolder helper, AqJyPxBean item) {
        helper.setText(R.id.aqjypx_item_trainingname, "培训名称：" + item.getTrainingname());
        helper.setText(R.id.aqjypx_item_trainingdate, "培训时间：" + item.getTrainingdate());
        helper.setText(R.id.aqjypx_item_trainingadd, "培训地点：" + item.getTrainingadd());
        helper.setText(R.id.aqjypx_item_qyname, "企业名称：" + item.getQyname());

        if (mycode.equals("home")) {
            helper.getView(R.id.aqjypx_item_qyname).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.aqjypx_item_qyname).setVisibility(View.GONE);
        }

    }
}
