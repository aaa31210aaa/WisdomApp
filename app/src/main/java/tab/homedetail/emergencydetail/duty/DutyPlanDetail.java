package tab.homedetail.emergencydetail.duty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;

public class DutyPlanDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.duty_detai_dutyman)
    TextView duty_detai_dutyman;
    @BindView(R.id.duty_detai_dutychargeman)
    TextView duty_detai_dutychargeman;
    @BindView(R.id.duty_detai_shiftmun)
    TextView duty_detai_shiftmun;
    @BindView(R.id.duty_detai_recordman)
    TextView duty_detai_recordman;
    @BindView(R.id.duty_detai_starttime)
    TextView duty_detai_starttime;
    @BindView(R.id.duty_detai_endtime)
    TextView duty_detai_endtime;
    @BindView(R.id.duty_detai_memo)
    TextView duty_detai_memo;
    private String dutyplanid;
    private String sumstartinfoid;
    private String dutyman;
    private String dutychargeman;
    private String shiftmun;
    private String recordman;
    private String starttime;
    private String endtime;
    private String memo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_plan_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.duty_plan_detail_title);
        Intent intent = getIntent();
        dutyplanid = intent.getStringExtra("dutyplanid");
        sumstartinfoid = intent.getStringExtra("sumstartinfoid");
        dutyman = intent.getStringExtra("dutyman");
        dutychargeman = intent.getStringExtra("dutychargeman");
        shiftmun = intent.getStringExtra("shiftmun");
        recordman = intent.getStringExtra("recordman");
        starttime = intent.getStringExtra("starttime");
        endtime = intent.getStringExtra("endtime");
        memo = intent.getStringExtra("memo");
        duty_detai_dutyman.setText(dutyman);
        duty_detai_dutychargeman.setText(dutychargeman);
        duty_detai_shiftmun.setText(shiftmun);
        duty_detai_recordman.setText(recordman);
        duty_detai_starttime.setText(starttime);
        duty_detai_endtime.setText(endtime);
        duty_detai_memo.setText(memo);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }


}
