package tab.homedetail.hiddendetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;

public class ZkdsryglDetail extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.zkdsrygl_detail_title)
    TextView zkdsrygl_detail_title;
    @BindView(R.id.zkdsrygl_detail_paramname)
    TextView zkdsrygl_detail_paramname;
    @BindView(R.id.zkdsrygl_detail_fusername)
    TextView zkdsrygl_detail_fusername;
    @BindView(R.id.zkdsrygl_detail_townshipname)
    TextView zkdsrygl_detail_townshipname;
    @BindView(R.id.zkdsrygl_detail_linktel)
    TextView zkdsrygl_detail_linktel;
    @BindView(R.id.zkdsrygl_detail_comname)
    TextView zkdsrygl_detail_comname;
    @BindView(R.id.zkdsrygl_detail_inminetime)
    TextView zkdsrygl_detail_inminetime;
    @BindView(R.id.zkdsrygl_detail_memo)
    TextView zkdsrygl_detail_memo;
    private String paramname;
    private String fusername;
    private String townshipname;
    private String linktel;
    private String comname;
    private String inminetime;
    private String memo;
    private String title_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zkdsrygl_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        initToolbar();
        Intent intent = getIntent();
        title_name = intent.getStringExtra("title_name");
        zkdsrygl_detail_title.setText(title_name);
        paramname = intent.getStringExtra("paramname");
        fusername = intent.getStringExtra("fusername");
        townshipname = intent.getStringExtra("townshipname");
        linktel = intent.getStringExtra("linktel");
        comname = intent.getStringExtra("comname");
        inminetime = intent.getStringExtra("inminetime");
        memo = intent.getStringExtra("memo");
        zkdsrygl_detail_paramname.setText(paramname);
        zkdsrygl_detail_fusername.setText(fusername);
        zkdsrygl_detail_townshipname.setText(townshipname);
        zkdsrygl_detail_linktel.setText(linktel);
        zkdsrygl_detail_comname.setText(comname);
        zkdsrygl_detail_inminetime.setText(inminetime);
        zkdsrygl_detail_memo.setText(memo);
    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
