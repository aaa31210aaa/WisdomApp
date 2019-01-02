package tab.homedetail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import tab.homedetail.safetydetail.SafetyPlan;
import tab.homedetail.safetydetail.SafetyRecord;
import tab.homedetail.safetydetail.YbzlistWbzlist;
import utils.BaseActivity;

/**
 * 安全自查
 */
public class Safety extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    private String tag = "safety";
    private String intentIndex = "safety";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.safety_title);
    }

    @OnClick(R.id.back)
    void Back(){
        finish();
    }

    /**
     * 安全自查计划
     */
    @OnClick(R.id.safety_plan)
    void SafetyPlan() {
        startActivity(new Intent(this, SafetyPlan.class));
    }

    /**
     * 安全自查内容
     */
    @OnClick(R.id.safety_content)
    void SafetyContent() {
        Intent intent = new Intent(this, YbzlistWbzlist.class);
        startActivity(intent);
    }

    /**
     * 安全自查记录
     */
    @OnClick(R.id.safety_record)
    void SafetyRecord() {
        startActivity(new Intent(this, SafetyRecord.class));
    }
}
