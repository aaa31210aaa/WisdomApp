package adpter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.SearchQyBean;
import demo.yqh.wisdomapp.R;

public class SearchQyAdapter extends BaseQuickAdapter<SearchQyBean, BaseViewHolder> {
    public SearchQyAdapter(@LayoutRes int layoutResId, @Nullable List<SearchQyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchQyBean item) {
        helper.setText(R.id.searchqy_name, item.getQyname());
    }
}
