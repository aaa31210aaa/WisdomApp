package tab.homedetail.emergencydetail.teammanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;

public class WxTeamManageDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.wx_team_manage_detail_externaldeptname)
    TextView wx_team_manage_detail_externaldeptname;
    @BindView(R.id.wx_team_manage_detail_ownman)
    TextView wx_team_manage_detail_ownman;
    @BindView(R.id.wx_team_manage_detail_owntel)
    TextView wx_team_manage_detail_owntel;
    @BindView(R.id.wx_team_manage_detail_linkman)
    TextView wx_team_manage_detail_linkman;
    @BindView(R.id.wx_team_manage_detail_linktel)
    TextView wx_team_manage_detail_linktel;
    @BindView(R.id.wx_team_manage_detail_externaltype)
    TextView wx_team_manage_detail_externaltype;
    @BindView(R.id.wx_team_manage_detail_areas)
    TextView wx_team_manage_detail_areas;
    @BindView(R.id.wx_team_manage_detail_equipmentstatusname)
    TextView wx_team_manage_detail_equipmentstatusname;
    @BindView(R.id.wx_team_manage_detail_address)
    TextView wx_team_manage_detail_address;
    private String externaldeptname;
    private String ownman;
    private String owntel;
    private String linkman;
    private String linktel;
    private String externaltype;
    private String areas;
    private String equipmentstatusname;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_team_manage_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.wxteam_manage_detail_title);
        Intent intent = getIntent();
        externaldeptname = intent.getStringExtra("externaldeptname");
        ownman = intent.getStringExtra("ownman");
        owntel = intent.getStringExtra("owntel");
        linkman = intent.getStringExtra("linkman");
        linktel = intent.getStringExtra("linktel");
        externaltype = intent.getStringExtra("externaltype");
        areas = intent.getStringExtra("areas");
        equipmentstatusname = intent.getStringExtra("equipmentstatusname");
        address = intent.getStringExtra("address");

        wx_team_manage_detail_externaldeptname.setText(externaldeptname);
        wx_team_manage_detail_ownman.setText(ownman);
        wx_team_manage_detail_owntel.setText(owntel);
        wx_team_manage_detail_linkman.setText(linkman);
        wx_team_manage_detail_linktel.setText(linktel);
        wx_team_manage_detail_externaltype.setText(externaltype);
        wx_team_manage_detail_areas.setText(areas);
        wx_team_manage_detail_equipmentstatusname.setText(equipmentstatusname);
        wx_team_manage_detail_address.setText(address);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }
}
