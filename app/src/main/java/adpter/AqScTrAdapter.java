package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.AqScTrBean;
import demo.yqh.wisdomapp.R;

public class AqScTrAdapter extends BaseQuickAdapter<AqScTrBean, BaseViewHolder> {
    private String mycode;

    public AqScTrAdapter(@LayoutRes int layoutResId, @Nullable List<AqScTrBean> data, String mycode) {
        super(layoutResId, data);
        this.mycode = mycode;
    }

    @Override
    protected void convert(BaseViewHolder helper, AqScTrBean item) {
        helper.setText(R.id.aqsctr_item_investname, "项目名称：" + item.getInvestname());
        helper.setText(R.id.aqsctr_item_monthvalue, "生产月份：" + item.getMonthvalue());
        helper.setText(R.id.aqsctr_item_investypename, "费用类型：" + item.getInvestypename());
        helper.setText(R.id.aqsctr_item_usemoney, "费用金额：" + item.getUsemoney());

        if (mycode.equals("home")) {
            helper.setText(R.id.aqsctr_item_qyname, item.getQyname());
            helper.getView(R.id.aqsctr_item_qyname).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.aqsctr_item_qyname).setVisibility(View.GONE);
        }

    }
}
