package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.XcJcBean;

public class TypeInfoListAdapter extends BaseQuickAdapter<XcJcBean, BaseViewHolder> {

    public TypeInfoListAdapter(@LayoutRes int layoutResId, @Nullable List<XcJcBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, XcJcBean item) {

    }
}
