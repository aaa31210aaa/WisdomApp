package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.YhInfoBean;
import demo.yqh.wisdomapp.R;

public class QyYhInformationAdapter extends BaseQuickAdapter<YhInfoBean, BaseViewHolder> {
    private String mycode;

    public QyYhInformationAdapter(@LayoutRes int layoutResId, @Nullable List<YhInfoBean> data, String mycode) {
        super(layoutResId, data);
        this.mycode = mycode;
    }

    @Override
    protected void convert(BaseViewHolder helper, YhInfoBean item) {
        helper.setText(R.id.qyyh_information_item_crname, "隐患名称：" + item.getCrname());
        helper.setText(R.id.qyyh_information_item_crtypename, "隐患类型：" + item.getCrtypename());
        helper.setText(R.id.qyyh_information_item_pcdate, "排查日期：" + item.getPcdate());
        helper.setText(R.id.qyyh_information_item_qyname, "企业名称：" + item.getQyname());

        if (mycode.equals("home")) {
            helper.getView(R.id.qyyh_information_item_qyname).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.qyyh_information_item_qyname).setVisibility(View.GONE);
        }

    }
}
