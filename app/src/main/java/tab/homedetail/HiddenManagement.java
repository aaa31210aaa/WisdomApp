package tab.homedetail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import tab.homedetail.hiddendetail.CommonlyRiskList;
import tab.homedetail.hiddendetail.HiddenDangerRegistrationList;
import utils.BaseActivity;
import utils.PortIpAddress;

/**
 * 隐患管理
 */
public class HiddenManagement extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.yb_hidden_tv)
    TextView yb_hidden_tv;
    @BindView(R.id.zd_hidden_tv)
    TextView zd_hidden_tv;

    private String intentIndex_yb;  //一般隐患标识
    private String intnetIndex_zd; //重大隐患标识
    //区分从首页进入还是隐患进入
    private String tag = "riskmanagement";
    //一般重大入参
    private String crtype = "";
//    //区分添加按钮显示隐藏
//    private String addTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_management);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        intentIndex_yb = "risk_yb";
        intnetIndex_zd = "risk_zd";
        if (PortIpAddress.getUserType(this)) {
            title_name.setText("隐患监管");
            yb_hidden_tv.setText(R.string.yb_hidden);
            zd_hidden_tv.setText(R.string.zd_hidden);
        } else {
            title_name.setText("隐患排查治理");
            yb_hidden_tv.setText(R.string.yb_hidden_zl);
            zd_hidden_tv.setText(R.string.zd_hidden_zl);
        }
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    /**
     * 一般隐患监管
     */
    @OnClick(R.id.yb_hidden_rl)
    void YbHiddenManage() {
        Intent intent = new Intent(this, CommonlyRiskList.class);
//      intent.putExtra("intentIndex", intentIndex_yb);
        crtype = "YHLB001";
        intent.putExtra("crtype", crtype);
        startActivity(intent);
    }

    /**
     * 重大隐患监管
     */
    @OnClick(R.id.zd_hidden_rl)
    void ZdHiddenManage() {
        Intent intent = new Intent(this, HiddenDangerRegistrationList.class);
//        intent.putExtra("intentIndex", intnetIndex_zd);
        crtype = "YHLB002";
        intent.putExtra("crtype", crtype);
        startActivity(intent);
    }

//    @OnClick(R.id.yb_hidden_check_rl)
//    void YbHiddenCheck(){
//        //一般隐患查看
//        Intent intent = new Intent(this, Enterprise.class);
//        crtype = "YHLB001";
//        intent.putExtra("intentIndex", intentIndex_yb);
//        //从隐患点进去
//        intent.putExtra("tag", tag);
//        intent.putExtra("crtype", crtype);
//        addTag = "ck";
//        intent.putExtra("addTag",addTag);
//        startActivity(intent);
//    }
//
//    @OnClick(R.id.zd_hidden_check_rl)
//    void ZdHiddenCheck(){
//        //重大隐患查看
//       Intent intent = new Intent(this, Enterprise.class);
//        crtype = "YHLB002";
//        intent.putExtra("intentIndex", intnetIndex_zd);
//        //从隐患点进去
//        intent.putExtra("tag", tag);
//        intent.putExtra("crtype", crtype);
//        addTag = "ck";
//        intent.putExtra("addTag",addTag);
//        startActivity(intent);
//    }

//    /**
//     * 日常监管
//     */
//    @OnClick(R.id.daily_regulation_rl)
//    void Regulation() {
//        startActivity(new Intent(this, DailyRegulation.class));
//    }
//
//    /**
//     * 驻矿盯守
//     */
//    @OnClick(R.id.in_mine_rl)
//    void InMine() {
//        startActivity(new Intent(this, InMine.class));
//    }


}
