package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.RulesBean;
import demo.yqh.wisdomapp.R;

public class RulesAdapter extends BaseQuickAdapter<RulesBean, BaseViewHolder> {
    private String mycode;

    public RulesAdapter(@LayoutRes int layoutResId, @Nullable List<RulesBean> data, String mycode) {
        super(layoutResId, data);
        this.mycode = mycode;
    }

    @Override
    protected void convert(BaseViewHolder helper, RulesBean item) {
        helper.setText(R.id.rules_detail_gzzdname, "规章制度名称：" + item.getRulesName());
        helper.setText(R.id.rules_detail_efftime, "生效时间：" + item.getRulesEffTime());
        helper.setText(R.id.rules_detail_cbtypename, "规章制度类型：" + item.getRulesType());
        helper.setText(R.id.rules_detail_qyname, "企业名称：" + item.getQyname());
        if (mycode.equals("home")) {
            helper.getView(R.id.rules_detail_qyname).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.rules_detail_qyname).setVisibility(View.GONE);
        }

    }
}
