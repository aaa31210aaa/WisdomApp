package tab.homedetail.emergencydetail.duty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;

public class DutyRecordDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.duty_record_detail_summaryname)
    TextView duty_record_detail_summaryname;
    @BindView(R.id.duty_record_detail_dutyPlanName)
    TextView duty_record_detail_dutyPlanName;
    @BindView(R.id.duty_record_detail_recordbeonduty)
    TextView duty_record_detail_recordbeonduty;
    @BindView(R.id.duty_record_detail_recordcontent)
    TextView duty_record_detail_recordcontent;
    @BindView(R.id.duty_record_detail_dutycontent)
    TextView duty_record_detail_dutycontent;
    @BindView(R.id.duty_record_detail_handlecase)
    TextView duty_record_detail_handlecase;
//    @BindView(R.id.duty_record_detail_recordman)
//    TextView duty_record_detail_recordman;
//    @BindView(R.id.duty_record_detail_recorddate)
//    TextView duty_record_detail_recorddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_record_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.duty_record_detail_title);
        Intent intent = getIntent();
        duty_record_detail_summaryname.setText(intent.getStringExtra("summaryname"));
        duty_record_detail_dutyPlanName.setText(intent.getStringExtra("dutyPlanName"));
        duty_record_detail_recordbeonduty.setText(intent.getStringExtra("recordbeonduty"));
        duty_record_detail_recordcontent.setText(intent.getStringExtra("recordcontent"));
        duty_record_detail_dutycontent.setText(intent.getStringExtra("dutycontent"));
        duty_record_detail_handlecase.setText(intent.getStringExtra("handlecase"));
//        duty_record_detail_recordman.setText(intent.getStringExtra("recordman"));
//        duty_record_detail_recorddate.setText(intent.getStringExtra("recorddate"));
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

}
