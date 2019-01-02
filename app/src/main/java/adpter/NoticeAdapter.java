package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.NoticeBean;
import demo.yqh.wisdomapp.R;

public class NoticeAdapter extends BaseQuickAdapter<NoticeBean.CellsBean, BaseViewHolder> {
    public NoticeAdapter(@LayoutRes int layoutResId, @Nullable List<NoticeBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeBean.CellsBean item) {
        helper.setText(R.id.notice_item_title, item.getMessagetitle());
        helper.setText(R.id.notice_item_mestime, item.getMestime());
    }
}
