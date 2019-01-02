package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.ZyWsXxBean;
import demo.yqh.wisdomapp.R;

import static demo.yqh.wisdomapp.R.id.zywsxx_item_qyname;

public class ZyWsXxAdapter extends BaseQuickAdapter<ZyWsXxBean, BaseViewHolder> {
    private String mycode;

    public ZyWsXxAdapter(@LayoutRes int layoutResId, @Nullable List<ZyWsXxBean> data, String mycode) {
        super(layoutResId, data);
        this.mycode = mycode;
    }

    @Override
    protected void convert(BaseViewHolder helper, ZyWsXxBean item) {
        helper.setText(R.id.zywsxx_item_ohaddres, "作业场所：" + item.getOhaddres());
        helper.setText(R.id.zywsxx_item_sites, "岗位：" + item.getSites());
        helper.setText(R.id.zywsxx_item_ohname, "职业危害因素名称：" + item.getOhname());
        helper.setText(R.id.zywsxx_item_createdate, "创建日期：" + item.getCreatedate());
        helper.setText(zywsxx_item_qyname, "企业名称：" + item.getQyname());
        helper.setText(R.id.zywsxx_item_dqdate, "到期日期：" + item.getDqdate());

        if (mycode.equals("home")) {
            helper.getView(zywsxx_item_qyname).setVisibility(View.VISIBLE);
        } else {
            helper.getView(zywsxx_item_qyname).setVisibility(View.GONE);
        }
    }


}
