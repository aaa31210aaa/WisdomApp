package tab.homedetail.emergencydetail.teammanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;

public class ExpertManageDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.expert_manage_detail_expertname)
    TextView expert_manage_detail_expertname;
    @BindView(R.id.expert_manage_detail_expertsex)
    TextView expert_manage_detail_expertsex;
    @BindView(R.id.expert_manage_detail_telphone)
    TextView expert_manage_detail_telphone;
    @BindView(R.id.expert_manage_detail_mobiletel)
    TextView expert_manage_detail_mobiletel;
    @BindView(R.id.expert_manage_detail_position)
    TextView expert_manage_detail_position;
    @BindView(R.id.expert_manage_detail_oudept)
    TextView expert_manage_detail_oudept;
    @BindView(R.id.expert_manage_detail_specialtydesc)
    TextView expert_manage_detail_specialtydesc;
    private String expertname;
    private String expertsex;
    private String telphone;
    private String mobiletel;
    private String position;
    private String oudept;
    private String specialtydesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_manage_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.expert_manage_detail);
        Intent intent = getIntent();
        expertname = intent.getStringExtra("expertname");
        expertsex = intent.getStringExtra("expertsex");
        telphone = intent.getStringExtra("telphone");
        mobiletel = intent.getStringExtra("mobiletel");
        position = intent.getStringExtra("position");
        oudept = intent.getStringExtra("oudept");
        specialtydesc = intent.getStringExtra("specialtydesc");
        expert_manage_detail_expertname.setText(expertname);
        expert_manage_detail_expertsex.setText(expertsex);
        expert_manage_detail_telphone.setText(telphone);
        expert_manage_detail_mobiletel.setText(mobiletel);
        expert_manage_detail_position.setText(position);
        expert_manage_detail_oudept.setText(oudept);
        expert_manage_detail_specialtydesc.setText(specialtydesc);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }


}
