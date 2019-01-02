package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.MedicalRescueBean;
import demo.yqh.wisdomapp.R;

public class MedicalRescueAdapter extends BaseQuickAdapter<MedicalRescueBean, BaseViewHolder> {

    public MedicalRescueAdapter(@LayoutRes int layoutResId, @Nullable List<MedicalRescueBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MedicalRescueBean item) {
        helper.setText(R.id.yjjy_item3_tv1, "单位名称：" + item.getHospitalname());
        helper.setText(R.id.yjjy_item3_tv2, "病床数：" + item.getBeddata());
        helper.setText(R.id.yjjy_item3_tv3, "医生数：" + item.getDoctordata());

    }
}
