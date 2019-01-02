package tab.homedetail.hiddendetail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;

/**
 * 驻矿盯守
 */
public class InMine extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_mine);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.in_mine);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

//    /**
//     * 驻矿盯守人员管理
//     */
//    @OnClick(R.id.inmine_zkdsrygl)
//    void Zkdsrygl() {
//        startActivity(new Intent(this, Zkdsrygl.class));
//    }

    /**
     * 联系乡镇领导
     */
    @OnClick(R.id.inmine_lxxzld)
    void Lxxzld() {
        Intent intent = new Intent(this, Lxxzld.class);
        intent.putExtra("usertype", "ZKGB001");
        intent.putExtra("title_name", "联系乡镇领导");
        intent.putExtra("ym", "Lxxzld");
        startActivity(intent);
    }

    /**
     * 联系乡镇领导工作信息
     */
    @OnClick(R.id.inmine_lxxzldgzxx)
    void Lxxzldgzxx() {
        Intent intent = new Intent(this, WorkInfo.class);
        intent.putExtra("usertype", "ZKGB001");
        intent.putExtra("title_name", "联系乡镇领导");
        intent.putExtra("ym", "Lxxzldgzxx");
        startActivity(intent);
    }

    /**
     * 联系矿责任人
     */
    @OnClick(R.id.inmine_lxkzrr)
    void Lxkzrr() {
        Intent intent = new Intent(this, Lxxzld.class);
        intent.putExtra("usertype", "ZKGB003");
        intent.putExtra("title_name", "联系矿责任人");
        intent.putExtra("ym", "Lxkzrr");
        startActivity(intent);
    }

    /**
     * 联系矿责任人工作信息
     */
    @OnClick(R.id.inmine_lxkzrrgzxx)
    void Lxkzrrgzxx() {
        Intent intent = new Intent(this, WorkInfo.class);
        intent.putExtra("usertype", "ZKGB003");
        intent.putExtra("title_name", "联系矿责任人");
        intent.putExtra("ym", "Lxkzrrgzxx");
        startActivity(intent);
    }

    /**
     * 驻矿责任人
     */
    @OnClick(R.id.inmin_zkzrr)
    void Zkzrr() {
        Intent intent = new Intent(this, Lxxzld.class);
        intent.putExtra("usertype", "ZKGB004");
        intent.putExtra("title_name", "驻矿责任人");
        intent.putExtra("ym", "Zkzrr");
        startActivity(intent);
    }

    /**
     * 驻矿责任人工作信息
     */
    @OnClick(R.id.inmine_zkzrrgzxx)
    void Zkzrrgzxx() {
        Intent intent = new Intent(this, WorkInfo.class);
        intent.putExtra("usertype", "ZKGB004");
        intent.putExtra("title_name", "联系矿责任人");
        intent.putExtra("ym", "Zkzrrgzxx");
        startActivity(intent);
    }

    /**
     * 安监员信息
     */
    @OnClick(R.id.inmine_ajyxx)
    void Ajyxx() {
        Intent intent = new Intent(this, Lxxzld.class);
        intent.putExtra("usertype", "ZKGB002");
        intent.putExtra("title_name", "安监员");
        intent.putExtra("ym", "Ajyxx");
        startActivity(intent);
    }

    /**
     * 安监员工作信息
     */
    @OnClick(R.id.inmine_ajygzxx)
    void Ajygzxx() {
        Intent intent = new Intent(this, WorkInfo.class);
        intent.putExtra("usertype", "ZKGB004");
        intent.putExtra("title_name", "安监员");
        intent.putExtra("ym", "Ajygzxx");
        startActivity(intent);
    }


}
