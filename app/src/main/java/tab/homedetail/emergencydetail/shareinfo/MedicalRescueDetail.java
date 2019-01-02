package tab.homedetail.emergencydetail.shareinfo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;

public class MedicalRescueDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.medical_rescue_hospitalname)
    TextView medical_rescue_hospitalname;
    @BindView(R.id.medical_rescue_beddata)
    TextView medical_rescue_beddata;
    @BindView(R.id.medical_rescue_doctordata)
    TextView medical_rescue_doctordata;
    @BindView(R.id.medical_rescue_nursedate)
    TextView medical_rescue_nursedate;
    @BindView(R.id.medical_rescue_ambulancedata)
    TextView medical_rescue_ambulancedata;
    @BindView(R.id.medical_rescue_storedplasmadata)
    TextView medical_rescue_storedplasmadata;
    @BindView(R.id.medical_rescue_linkman)
    TextView medical_rescue_linkman;
    @BindView(R.id.medical_rescue_linktel)
    TextView medical_rescue_linktel;
    @BindView(R.id.medical_rescue_address)
    TextView medical_rescue_address;
    private String hospitalname;
    private String beddata;
    private String doctordata;
    private String nursedate;
    private String ambulancedata;
    private String storedplasmadata;
    private String linkman;
    private String linktel;
    private String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_rescue_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.medical_rescue_detail);
        Intent intent = getIntent();
        hospitalname = intent.getStringExtra("hospitalname");
        beddata = intent.getStringExtra("beddata");
        doctordata = intent.getStringExtra("doctordata");
        nursedate = intent.getStringExtra("nursedate");
        ambulancedata = intent.getStringExtra("ambulancedata");
        storedplasmadata = intent.getStringExtra("storedplasmadata");
        linkman = intent.getStringExtra("linkman");
        linktel = intent.getStringExtra("linktel");
        address = intent.getStringExtra("address");

        medical_rescue_hospitalname.setText(hospitalname);
        medical_rescue_beddata.setText(beddata);
        medical_rescue_doctordata.setText(doctordata);
        medical_rescue_nursedate.setText(nursedate);
        medical_rescue_ambulancedata.setText(ambulancedata);
        medical_rescue_storedplasmadata.setText(storedplasmadata);
        medical_rescue_linkman.setText(linkman);
        medical_rescue_linktel.setText(linktel);
        medical_rescue_address.setText(address);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }
}
