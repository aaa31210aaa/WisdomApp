package tab.homedetail.emergencydetail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;

public class LeadershipEmergencyDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_name_right)
    TextView title_name_right;
    //队伍名称
    @BindView(R.id.emergency_detail_ranksname)
    TextView emergency_detail_ranksname;
    //队伍类型
    @BindView(R.id.emergency_detail_rankstype)
    TextView emergency_detail_rankstype;
    //所在区域
    @BindView(R.id.emergency_detail_areas)
    TextView emergency_detail_areas;
    //负责人
    @BindView(R.id.emergency_detail_ownman)
    TextView emergency_detail_ownman;
    //负责人联系方式
    @BindView(R.id.emergency_detail_owntel)
    TextView emergency_detail_owntel;
    //联系人
    @BindView(R.id.emergency_detail_linkman)
    TextView emergency_detail_linkman;
    //联系方式
    @BindView(R.id.emergency_detail_linktel)
    TextView emergency_detail_linktel;
    //人员数量
    @BindView(R.id.emergency_detail_pepsum)
    TextView emergency_detail_pepsum;
    //主管单位
    @BindView(R.id.emergency_detail_competentdetp)
    TextView emergency_detail_competentdetp;
    //专场描述
    @BindView(R.id.emergency_detail_specialtydesc)
    TextView emergency_detail_specialtydesc;
    //    //x坐标
//    @BindView(R.id.emergency_detail_x)
//    TextView emergency_detail_x;
//    //y坐标
//    @BindView(R.id.emergency_detail_y)
//    TextView emergency_detail_y;
//    //队伍状态
//    @BindView(R.id.emergency_detail_ranksstatus)
//    TextView emergency_detail_ranksstatus;
    //所处位置
    @BindView(R.id.emergency_detail_address)
    TextView emergency_detail_address;
    //所处位置
    @BindView(R.id.emergency_detail_memo)
    TextView emergency_detail_memo;
    private String ranksid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leadership_emergency_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.leadershipemergency_detail_title);
        title_name_right.setText(R.string.leadershipemergency_detail_man_title);
        Intent intent = getIntent();
        ranksid = intent.getStringExtra("ranksid");
        mConnect(ranksid);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    @OnClick(R.id.title_name_right)
    void Jgry() {
        Intent intent = new Intent(this, Institutional.class);
        intent.putExtra("ranksid", ranksid);
        startActivity(intent);
    }

    private void mConnect(String ranksid) {
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        OkGo.<String>get(PortIpAddress.Leadership())
                .tag(this)
                .params("ldjgbean.ranksid", ranksid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                emergency_detail_ranksname.setText(jsonArray.optJSONObject(0).getString("ranksname"));
                                emergency_detail_rankstype.setText(jsonArray.optJSONObject(0).getString("rankstype"));
                                emergency_detail_areas.setText(jsonArray.optJSONObject(0).getString("areas"));
                                emergency_detail_ownman.setText(jsonArray.optJSONObject(0).getString("ownman"));
                                emergency_detail_owntel.setText(jsonArray.optJSONObject(0).getString("owntel"));
                                emergency_detail_linkman.setText(jsonArray.optJSONObject(0).getString("linkman"));
                                emergency_detail_linktel.setText(jsonArray.optJSONObject(0).getString("linktel"));
                                emergency_detail_pepsum.setText(jsonArray.optJSONObject(0).getString("pepsum"));
                                emergency_detail_competentdetp.setText(jsonArray.optJSONObject(0).getString("competentdetp"));
                                emergency_detail_specialtydesc.setText(jsonArray.optJSONObject(0).getString("specialtydesc"));
//                                emergency_detail_ranksstatus.setText(jsonArray.optJSONObject(0).getString("ranksstatus"));
                                emergency_detail_address.setText(jsonArray.optJSONObject(0).getString("address"));
//                                emergency_detail_memo.setText(jsonArray.optJSONObject(0).getString("memo"));
                            } else {
                                ShowToast.showShort(LeadershipEmergencyDetail.this, R.string.getInfoErr);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dialog.dismiss();
                        ShowToast.showShort(LeadershipEmergencyDetail.this, R.string.connect_err);
                    }
                });
    }


}
