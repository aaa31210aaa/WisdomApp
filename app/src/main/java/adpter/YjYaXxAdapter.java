package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.YjYaXxBean;
import demo.yqh.wisdomapp.R;

public class YjYaXxAdapter extends BaseQuickAdapter<YjYaXxBean, BaseViewHolder> {
    private String mycode;

    public YjYaXxAdapter(@LayoutRes int layoutResId, @Nullable List<YjYaXxBean> data, String mycode) {
        super(layoutResId, data);
        this.mycode = mycode;
    }

    @Override
    protected void convert(BaseViewHolder helper, YjYaXxBean item) {
        helper.setText(R.id.yjyaxx_item_ohaddres, "预案类型：" + item.getOhaddres());
        helper.setText(R.id.yjyaxx_item_reservname, "应急预案名称：" + item.getReservname());
        helper.setText(R.id.yjyaxx_item_reloaddate, "修编时间：" + item.getReloaddate());
//        helper.setText(R.id.zywsxx_item_qyname, "企业名称：" + item.getQyname());

//        if (mycode.equals("home")) {
//            helper.getView(zywsxx_item_qyname).setVisibility(View.VISIBLE);
//        } else {
//            helper.getView(zywsxx_item_qyname).setVisibility(View.GONE);
//        }
    }
}
