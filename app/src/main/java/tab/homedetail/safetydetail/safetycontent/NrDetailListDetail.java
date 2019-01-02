package tab.homedetail.safetydetail.safetycontent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;

public class NrDetailListDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    //    @BindView(R.id.nrdetail_list_hybz1)
//    TextView nrdetail_list_hybz1;
    @BindView(R.id.nrdetail_list_hybz2)
    TextView nrdetail_list_hybz2;
    @BindView(R.id.nrdetail_list_hybz3)
    TextView nrdetail_list_hybz3;
    @BindView(R.id.nrdetail_list_hybz4)
    TextView nrdetail_list_hybz4;
    @BindView(R.id.nrdetail_list_hybz5)
    TextView nrdetail_list_hybz5;
    @BindView(R.id.nrdetail_list_bzflname)
    TextView nrdetail_list_bzflname;
    @BindView(R.id.nrdetail_list_rscdesc)
    TextView nrdetail_list_rscdesc;
    @BindView(R.id.nrdetail_list_rsckyj)
    TextView nrdetail_list_rsckyj;
    @BindView(R.id.nrdetail_list_pczq)
    TextView nrdetail_list_pczq;
    @BindView(R.id.nrdetail_list_sfbc)
    TextView nrdetail_list_sfbc;
    @BindView(R.id.nrdetail_list_pcdeptname)
    TextView nrdetail_list_pcdeptname;
    @BindView(R.id.nrdetail_list_wgpcd)
    TextView nrdetail_list_wgpcd;
    private String zcbzdid;
    private String bzflname;
    //    private String hybz1;
    private String hybz2;
    private String hybz3;
    private String hybz4;
    private String hybz5;
    private String bzorder;
    private String rscdesc;
    private String rsckyj;
    private String pczq;
    private String pczqval;
    private String sfbc;
    private String pcdeptname;
    private String wgpcd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nr_detail_list_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.nrdetail_list_detail_title);
        Intent intent = getIntent();
        zcbzdid = intent.getStringExtra("zcbzdid");
        bzflname = intent.getStringExtra("bzflname");
//        hybz1 = intent.getStringExtra("hybz1");
        hybz2 = intent.getStringExtra("hybz2");
        hybz3 = intent.getStringExtra("hybz3");
        hybz4 = intent.getStringExtra("hybz4");
        hybz5 = intent.getStringExtra("hybz5");
        bzorder = intent.getStringExtra("bzorder");
        rscdesc = intent.getStringExtra("rscdesc");
        rsckyj = intent.getStringExtra("rsckyj");
        pczq = intent.getStringExtra("pczq");
        pczqval = intent.getStringExtra("pczqval");
        sfbc = intent.getStringExtra("sfbc");
        pcdeptname = intent.getStringExtra("pcdeptname");
        wgpcd = intent.getStringExtra("wgpcd");

//        nrdetail_list_hybz1.setText(hybz1);
        nrdetail_list_hybz2.setText(hybz2);
        nrdetail_list_hybz3.setText(hybz3);
        nrdetail_list_hybz4.setText(hybz4);
        nrdetail_list_hybz5.setText(hybz5);
        nrdetail_list_bzflname.setText(bzflname);
        nrdetail_list_rscdesc.setText(rscdesc);
        nrdetail_list_rsckyj.setText(rsckyj);
        nrdetail_list_pczq.setText(pczq);
        nrdetail_list_sfbc.setText(sfbc);
        nrdetail_list_pcdeptname.setText(pcdeptname);
        nrdetail_list_wgpcd.setText(wgpcd);

    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

}
