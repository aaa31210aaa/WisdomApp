package tab.homedetail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import tab.homedetail.emergencydetail.DutyEmergency;
import tab.homedetail.emergencydetail.EventManagementEmergency;
import tab.homedetail.emergencydetail.LeadershipEmergency;
import tab.homedetail.emergencydetail.MaterialManagementEmergency;
import tab.homedetail.emergencydetail.MaterialSharingEmergency;
import tab.homedetail.emergencydetail.TeamManagementEmergency;
import utils.BaseActivity;

/**
 * 应急救援
 */
public class EmergencyRescue extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    private String ym = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_rescue);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.emergency_rescue_title);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    /**
     * 应急救援领导机构
     */
    @OnClick(R.id.emergency_rescue_ldjg)
    void Qyyjjy() {
        startActivity(new Intent(this, LeadershipEmergency.class));
    }

    /**
     * 应急救援值班值守
     */
    @OnClick(R.id.emergency_rescue_zbzs)
    void Zfyjjy() {
        startActivity(new Intent(this, DutyEmergency.class));
    }

    /**
     * 应急救援队伍管理
     */
    @OnClick(R.id.emergency_rescue_dwgl)
    void Yjsjgl() {
        startActivity(new Intent(this, TeamManagementEmergency.class));
    }

    /**
     * 应急救援物资管理
     */
    @OnClick(R.id.emergency_rescue_wzgl)
    void Wzgl() {
        startActivity(new Intent(this, MaterialManagementEmergency.class));
    }

    /**
     * 资源共享信息
     */
    @OnClick(R.id.emergency_rescue_gxxx)
    void Gxxx() {
        startActivity(new Intent(this, MaterialSharingEmergency.class));
    }

    /**
     * 应急事件管理
     */
    @OnClick(R.id.emergency_rescue_sjgl)
    void Sjgl() {
        ym = "sjgl";
        Intent intent = new Intent(this, EventManagementEmergency.class);
        intent.putExtra("ym", ym);
        startActivity(intent);
    }

    /**
     * 应急评估总结
     */
    @OnClick(R.id.emergency_rescue_pgzj)
    void Pgzj() {
        ym = "pgzj";
        Intent intent = new Intent(this, EventManagementEmergency.class);
        intent.putExtra("ym", ym);
        startActivity(intent);
    }

}
