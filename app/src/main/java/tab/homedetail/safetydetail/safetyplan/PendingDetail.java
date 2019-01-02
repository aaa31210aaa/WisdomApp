package tab.homedetail.safetydetail.safetyplan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;

public class PendingDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_name_right)
    TextView title_name_right;
    @BindView(R.id.pending_detail_cpname)
    TextView pending_detail_cpname;
    @BindView(R.id.pending_detail_checktypename)
    TextView pending_detail_checktypename;
    @BindView(R.id.pending_detail_checkman)
    TextView pending_detail_checkman;
    @BindView(R.id.pending_detail_checkdeptname)
    TextView pending_detail_checkdeptname;
    @BindView(R.id.pending_detail_checkstatar)
    TextView pending_detail_checkstatar;
    @BindView(R.id.pending_detail_checkend)
    TextView pending_detail_checkend;
    @BindView(R.id.pending_detail_planstatusname)
    TextView pending_detail_planstatusname;
    @BindView(R.id.pending_detail_planfromname)
    TextView pending_detail_planfromname;
    private String cpid;
    private String cpname;
    private String checktypename;
    private String checkman;
    private String checkdeptname;
    private String checkstatar;
    private String checkend;
    private String planstatusname;
    private String planfromname;
    private String titlename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        titlename = intent.getStringExtra("titlename");
        cpid = intent.getStringExtra("cpid");
        cpname = intent.getStringExtra("cpname");
        checktypename = intent.getStringExtra("checktypename");
        checkman = intent.getStringExtra("checkman");
        checkdeptname = intent.getStringExtra("checkdeptname");
        checkstatar = intent.getStringExtra("checkstatar");
        checkend = intent.getStringExtra("checkend");
        planstatusname = intent.getStringExtra("planstatusname");
        planfromname = intent.getStringExtra("planfromname");
        title_name.setText(titlename);
        title_name_right.setText(R.string.pending_detail_list);

        pending_detail_cpname.setText(cpname);
        pending_detail_checktypename.setText(checktypename);
        pending_detail_checkman.setText(checkman);
        pending_detail_checkdeptname.setText(checkdeptname);
        pending_detail_checkstatar.setText(checkstatar);
        pending_detail_checkend.setText(checkend);
        pending_detail_planstatusname.setText(planstatusname);
        pending_detail_planfromname.setText(planfromname);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    @OnClick(R.id.title_name_right)
    void DetailedList() {
        Intent intent = new Intent(this, DetailedList.class);
        intent.putExtra("cpid", cpid);
        startActivity(intent);
    }


}
