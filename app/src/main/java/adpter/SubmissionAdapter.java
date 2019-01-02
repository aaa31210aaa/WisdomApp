package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.SubmissionBean;
import cn.bingoogolapple.badgeview.BGABadgeLinearLayout;
import demo.yqh.wisdomapp.R;

public class SubmissionAdapter extends BaseQuickAdapter<SubmissionBean.CellsBean, BaseViewHolder> {

    public SubmissionAdapter(@LayoutRes int layoutResId, @Nullable List<SubmissionBean.CellsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SubmissionBean.CellsBean item) {
        helper.setText(R.id.submission_item1, "检查场所：" + item.getCheckadd());
        helper.setText(R.id.submission_item2, "检查人员：" + item.getCheckman());
        helper.setText(R.id.submission_item3, "检查时间：" + item.getScdate());
        helper.setText(R.id.submission_item4, "查处问题：" + item.getProblems());
        helper.setText(R.id.submission_item5, "处理方式：" + item.getHandlemethod());
        int yhnum = Integer.parseInt(item.getYhNum());
        BGABadgeLinearLayout badge_view = helper.getView(R.id.submission_item_badge);
        if (yhnum > 0) {
            badge_view.setVisibility(View.VISIBLE);
            badge_view.isShowBadge();
            badge_view.showTextBadge(yhnum + "");
        } else {
            badge_view.hiddenBadge();
        }
    }
}
