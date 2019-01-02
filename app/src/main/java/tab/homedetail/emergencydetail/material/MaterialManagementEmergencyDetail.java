package tab.homedetail.emergencydetail.material;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;

public class MaterialManagementEmergencyDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.material_detail_vehiclecode)
    TextView material_detail_vehiclecode;
    @BindView(R.id.material_detail_vehiclename)
    TextView material_detail_vehiclename;
    @BindView(R.id.material_detail_ownsum)
    TextView material_detail_ownsum;
    @BindView(R.id.material_detail_deploymentsum)
    TextView material_detail_deploymentsum;
    @BindView(R.id.material_detail_ownman)
    TextView material_detail_ownman;
    @BindView(R.id.material_detail_linkman)
    TextView material_detail_linkman;
    @BindView(R.id.material_detail_linktel)
    TextView material_detail_linktel;
    private String vehiclecode;
    private String vehiclename;
    private String ownsum;
    private String deploymentsum;
    private String ownman;
    private String linkman;
    private String linktel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_management_emergency_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.material_management_emergency_detail);
        Intent intent = getIntent();
        vehiclecode = intent.getStringExtra("vehiclecode");
        vehiclename = intent.getStringExtra("vehiclename");
        ownsum = intent.getStringExtra("ownsum");
        deploymentsum = intent.getStringExtra("deploymentsum");
        ownman = intent.getStringExtra("ownman");
        linkman = intent.getStringExtra("linkman");
        linktel = intent.getStringExtra("linktel");

        material_detail_vehiclecode.setText(vehiclecode);
        material_detail_vehiclename.setText(vehiclename);
        material_detail_ownsum.setText(ownsum);
        material_detail_deploymentsum.setText(deploymentsum);
        material_detail_ownman.setText(ownman);
        material_detail_linkman.setText(linkman);
        material_detail_linktel.setText(linktel);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }
}
