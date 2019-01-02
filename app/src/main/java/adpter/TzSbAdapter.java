package adpter;


import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.TzSbBean;
import demo.yqh.wisdomapp.R;

public class TzSbAdapter extends BaseQuickAdapter<TzSbBean, BaseViewHolder> {
    public TzSbAdapter(@LayoutRes int layoutResId, @Nullable List<TzSbBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TzSbBean item) {
        helper.setText(R.id.tzsb_item_facilitiesname, "设备名称：" + item.getFacilitiesname());
        helper.setText(R.id.tzsb_item_facilitiesno, "规格型号：" + item.getFacilitiesno());
        helper.setText(R.id.tzsb_item_manufacturer, "制造厂商：" + item.getManufacturer());
        helper.setText(R.id.tzsb_item_installsite, "安装位置：" + item.getInstallsite());
        helper.setText(R.id.tzsb_item_checkdate, "检查时间：" + item.getCheckdate());

        if (item.getIsexpire().equals("1")){
            helper.setTextColor(R.id.tzsb_item_checkdate,Color.RED);
        }else{
            helper.setTextColor(R.id.tzsb_item_checkdate,Color.BLACK);
        }
    }
}
