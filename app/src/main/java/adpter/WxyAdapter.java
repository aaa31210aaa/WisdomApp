package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.WxyBean;
import demo.yqh.wisdomapp.R;

import static demo.yqh.wisdomapp.R.id.wxy_qyname;

public class WxyAdapter extends BaseQuickAdapter<WxyBean, BaseViewHolder> {
    private String mycode;

    public WxyAdapter(@LayoutRes int layoutResId, @Nullable List<WxyBean> data, String mycode) {
        super(layoutResId, data);
        this.mycode = mycode;
    }

    @Override
    protected void convert(BaseViewHolder helper, WxyBean item) {
        helper.setText(R.id.wxy_mainrisk, "主要风险点：" + item.getMainrisk());
        helper.setText(R.id.wxy_maindanger, "主要危险：" + item.getMaindanger());
        helper.setText(R.id.wxy_risktypename, "风险类型：" + item.getRisktypename());
        helper.setText(wxy_qyname, "企业名称：" + item.getQyname());

        if (mycode.equals("home")) {
            helper.getView(wxy_qyname).setVisibility(View.VISIBLE);
        } else {
            helper.getView(wxy_qyname).setVisibility(View.GONE);
        }

    }
}
