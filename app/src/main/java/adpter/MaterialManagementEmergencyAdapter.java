package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.MaterialManagementEmergencyBean;
import demo.yqh.wisdomapp.R;

public class MaterialManagementEmergencyAdapter extends BaseQuickAdapter<MaterialManagementEmergencyBean, BaseViewHolder> {
    public MaterialManagementEmergencyAdapter(@LayoutRes int layoutResId, @Nullable List<MaterialManagementEmergencyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaterialManagementEmergencyBean item) {
        helper.setText(R.id.yjjy_item3_tv1,"物资名称："+item.getVehiclename());
        helper.setText(R.id.yjjy_item3_tv2,"数量："+item.getOwnsum());
        helper.setText(R.id.yjjy_item3_tv3,"联系人："+item.getLinkman());

    }
}
