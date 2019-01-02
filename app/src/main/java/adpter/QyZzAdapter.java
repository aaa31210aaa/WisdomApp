package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.QyZzBean;
import demo.yqh.wisdomapp.R;

public class QyZzAdapter extends BaseQuickAdapter<QyZzBean, BaseViewHolder> {
    public QyZzAdapter(@LayoutRes int layoutResId, @Nullable List<QyZzBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QyZzBean item) {
        helper.setText(R.id.qyzz_item_cftypename, "证照类型：" + item.getCftypename());
        helper.setText(R.id.qyzz_item_certificate, "证件号：" + item.getCertificate());
        helper.setText(R.id.qyzz_item_cardname, "证件名称：" + item.getCardname());
        helper.setText(R.id.qyzz_item_administrator, "负责人：" + item.getAdministrator());
        helper.setText(R.id.qyzz_item_carddate, "颁布时间：" + item.getCarddate());
    }
}
