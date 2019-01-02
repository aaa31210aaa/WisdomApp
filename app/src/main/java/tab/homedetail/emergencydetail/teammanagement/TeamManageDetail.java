package tab.homedetail.emergencydetail.teammanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;

public class TeamManageDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_name_right)
    TextView title_name_right;
    @BindView(R.id.team_manage_detail_ranksname)
    TextView team_manage_detail_ranksname;
    @BindView(R.id.team_manage_detail_rankstype)
    TextView team_manage_detail_rankstype;
    @BindView(R.id.team_manage_detail_areas)
    TextView team_manage_detail_areas;
    @BindView(R.id.team_manage_detail_ownman)
    TextView team_manage_detail_ownman;
    @BindView(R.id.team_manage_detail_owntel)
    TextView team_manage_detail_owntel;
    @BindView(R.id.team_manage_detail_linkman)
    TextView team_manage_detail_linkman;
    @BindView(R.id.team_manage_detail_linktel)
    TextView team_manage_detail_linktel;
    @BindView(R.id.team_manage_detail_pepsum)
    TextView team_manage_detail_pepsum;
    @BindView(R.id.team_manage_detail_competentdetp)
    TextView team_manage_detail_competentdetp;
    @BindView(R.id.team_manage_detail_specialtydesc)
    TextView team_manage_detail_specialtydesc;
    @BindView(R.id.team_manage_detail_ranksstatus)
    TextView team_manage_detail_ranksstatus;
    @BindView(R.id.team_manage_detail_address)
    TextView team_manage_detail_address;
    @BindView(R.id.team_manage_detail_memo)
    TextView team_manage_detail_memo;

    private String ranksid;
    private String ranksname;
    private String rankstype;
    private String areas;
    private String ownman;
    private String owntel;
    private String linkman;
    private String linktel;
    private String pepsum;
    private String competentdetp;
    private String specialtydesc;
    private String equipmentstatusname;
    private String address;
    private String memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_manage_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.team_management_emergency_detail_title);
        title_name_right.setText(R.string.team_man);
        Intent intent = getIntent();
        ranksid = intent.getStringExtra("ranksid");
        ranksname = intent.getStringExtra("ranksname");
        rankstype = intent.getStringExtra("rankstype");
        areas = intent.getStringExtra("areas");
        ownman = intent.getStringExtra("ownman");
        owntel = intent.getStringExtra("owntel");
        linkman = intent.getStringExtra("linkman");
        linktel = intent.getStringExtra("linktel");
        pepsum = intent.getStringExtra("pepsum");
        competentdetp = intent.getStringExtra("competentdetp");
        specialtydesc = intent.getStringExtra("specialtydesc");
        equipmentstatusname = intent.getStringExtra("equipmentstatusname");
        address = intent.getStringExtra("address");
        memo = intent.getStringExtra("memo");

        team_manage_detail_ranksname.setText(ranksname);
        team_manage_detail_rankstype.setText(rankstype);
        team_manage_detail_areas.setText(areas);
        team_manage_detail_ownman.setText(ownman);
        team_manage_detail_owntel.setText(owntel);
        team_manage_detail_linkman.setText(linkman);
        team_manage_detail_linktel.setText(linktel);
        team_manage_detail_pepsum.setText(pepsum);
        team_manage_detail_competentdetp.setText(competentdetp);
        team_manage_detail_specialtydesc.setText(specialtydesc);
        team_manage_detail_ranksstatus.setText(equipmentstatusname);
        team_manage_detail_address.setText(address);
        team_manage_detail_memo.setText(memo);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    /**
     * 队伍成员
     */
    @OnClick(R.id.title_name_right)
    void Dwcy() {
        Intent intent = new Intent(this, TeamDwcy.class);
        intent.putExtra("ranksid", ranksid);
        startActivity(intent);
    }

}
