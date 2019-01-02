package tab.homedetail.safetydetail.safetyplan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;

public class DetailedListDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;

    @BindView(R.id.detailed_list_rscdesc)
    TextView detailed_list_rscdesc;
    @BindView(R.id.detailed_list_rsckyj)
    TextView detailed_list_rsckyj;
    @BindView(R.id.detailed_list_pczq)
    TextView detailed_list_pczq;
    @BindView(R.id.detailed_list_pcdeptid)
    TextView detailed_list_pcdeptid;
    @BindView(R.id.detailed_list_wgpcd)
    TextView detailed_list_wgpcd;
    @BindView(R.id.detailed_list_sfbc)
    TextView detailed_list_sfbc;
    @BindView(R.id.detailed_list_isok)
    TextView detailed_list_isok;
    @BindView(R.id.detailed_list_jlly)
    TextView detailed_list_jlly;
    @BindView(R.id.detailed_list_bzorder)
    TextView detailed_list_bzorder;
    private String scdid;
    private String cpid;
    private String rscdesc;
    private String rsckyj;
    private String pczq;
    private String pcdeptid;
    private String wgpcd;
    private String sfbc;
    private String isok;
    private String jlly;
    private String bzorder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_list_detail);
        ButterKnife.bind(this);
        initData();


    }

    @Override
    protected void initData() {
        title_name.setText(R.string.pending_detail_listxq);
        Intent intent = getIntent();
        scdid = intent.getStringExtra("scdid");
        cpid = intent.getStringExtra("cpid");
        rscdesc = intent.getStringExtra("rscdesc");
        rsckyj = intent.getStringExtra("rsckyj");
        pczq = intent.getStringExtra("pczq");
        pcdeptid = intent.getStringExtra("pcdeptid");
        wgpcd = intent.getStringExtra("wgpcd");
        sfbc = intent.getStringExtra("sfbc");
        isok = intent.getStringExtra("isok");
        jlly = intent.getStringExtra("jlly");
        bzorder = intent.getStringExtra("bzorder");

        detailed_list_rscdesc.setText(rscdesc);
        detailed_list_rsckyj.setText(rsckyj);
        detailed_list_pczq.setText(pczq);
        detailed_list_pcdeptid.setText(pcdeptid);
        detailed_list_wgpcd.setText(wgpcd);
        detailed_list_sfbc.setText(sfbc);
        detailed_list_isok.setText(isok);
        detailed_list_jlly.setText(jlly);
        detailed_list_bzorder.setText(bzorder);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }
}
