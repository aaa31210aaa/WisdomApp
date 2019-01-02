package tab.homedetail.enterprisedetail;

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
import cn.bingoogolapple.badgeview.BGABadgeTextView;
import demo.yqh.wisdomapp.R;
import tab.homedetail.Enforcement;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;

public class EnterpriseDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_name_right)
    TextView title_name_right;
    @BindView(R.id.enterprisedetail_companyname)
    TextView enterprisedetail_companyname;
    @BindView(R.id.enterprisedetail_setupdate)
    TextView enterprisedetail_setupdate;
    @BindView(R.id.enterprisedetail_legalperson)
    TextView enterprisedetail_legalperson;
    @BindView(R.id.enterprisedetail_certificates)
    TextView enterprisedetail_certificates;
    @BindView(R.id.enterprisedetail_jytypename)
    TextView enterprisedetail_jytypename;
    @BindView(R.id.enterprisedetail_mainfield)
    TextView enterprisedetail_mainfield;
    @BindView(R.id.enterprisedetail_chargedeptname)
    TextView enterprisedetail_chargedeptname;
    @BindView(R.id.enterprisedetail_staffnum)
    TextView enterprisedetail_staffnum;
    @BindView(R.id.enterprisedetail_regcapital)
    TextView enterprisedetail_regcapital;
    @BindView(R.id.enterprisedetail_zcaddress)
    TextView enterprisedetail_zcaddress;
    @BindView(R.id.enterprisedetail_risktypename)
    TextView enterprisedetail_risktypename;
    @BindView(R.id.enterprisedetail_riskscore)
    TextView enterprisedetail_riskscore;
    @BindView(R.id.enterprise_detail_bmgl)
    BGABadgeTextView enterprise_detail_bmgl;
    @BindView(R.id.enterprise_detail_qyzz)
    BGABadgeTextView enterprise_detail_qyzz;
    @BindView(R.id.enterprise_detail_rygl)
    BGABadgeTextView enterprise_detail_rygl;
    @BindView(R.id.enterprise_detail_aqsctr)
    BGABadgeTextView enterprise_detail_aqsctr;
    @BindView(R.id.enterprise_detail_gzzd)
    BGABadgeTextView enterprise_detail_gzzd;
    @BindView(R.id.enterprise_detail_wxy)
    BGABadgeTextView enterprise_detail_wxy;
    @BindView(R.id.enterprise_detail_zywsxx)
    BGABadgeTextView enterprise_detail_zywsxx;
    @BindView(R.id.enterprise_detail_aqjc)
    BGABadgeTextView enterprise_detail_aqjc;
    @BindView(R.id.enterprise_detail_yhxx)
    BGABadgeTextView enterprise_detail_yhxx;
    @BindView(R.id.enterprise_detail_xzzf)
    BGABadgeTextView enterprise_detail_xzzf;
    @BindView(R.id.enterprise_detail_tzsb)
    BGABadgeTextView enterprise_detail_tzsb;
    @BindView(R.id.enterprise_detail_yjjyxx)
    BGABadgeTextView enterprise_detail_yjjyxx;
    @BindView(R.id.enterprise_detail_yjwz)
    BGABadgeTextView enterprise_detail_yjwz;
    @BindView(R.id.enterprise_detail_yjyl)
    BGABadgeTextView enterprise_detail_yjyl;
    @BindView(R.id.enterprise_detail_aqjypx)
    BGABadgeTextView enterprise_detail_aqjypx;

    //企业经纬度
    private double qyLat;
    private double qyLng;

    //点击企业的id
    private String clickId;
    private int index = 0;
    private Intent intent;
    private String mycode = "emap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.enterprise_detail_title);
        title_name_right.setText(R.string.enterprise_detail_see_map);
        //获得传过来的企业id
        Intent intent = getIntent();
        clickId = intent.getStringExtra("clickId");

        mConnect(clickId);
    }

    @OnClick(R.id.back)
    void Back(){
        finish();
    }


    @OnClick(R.id.title_name_right)
    void SeeMap() {
        Intent intent = new Intent(this, QyMap.class);
        intent.putExtra("tag","ed");
        intent.putExtra("myqyLat", qyLat);
        intent.putExtra("myqyLng", qyLng);
        intent.putExtra("qyname", enterprisedetail_companyname.getText().toString());
        startActivity(intent);
    }


    private void mConnect(String clickid) {
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        OkGo.<String>get(PortIpAddress.CompanyDetailInfo())
                .tag(this)
                .params("qyid", clickid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);

                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                enterprisedetail_companyname.setText(jsonArray.optJSONObject(index).getString("comname"));
                                enterprisedetail_setupdate.setText(jsonArray.optJSONObject(index).getString("setupdate"));
                                enterprisedetail_legalperson.setText(jsonArray.optJSONObject(index).getString("legalperson"));
                                enterprisedetail_certificates.setText(jsonArray.optJSONObject(index).getString("certificates"));
                                enterprisedetail_jytypename.setText(jsonArray.optJSONObject(index).getString("jytypename"));
                                enterprisedetail_mainfield.setText(jsonArray.optJSONObject(index).getString("mainfield"));
                                enterprisedetail_chargedeptname.setText(jsonArray.optJSONObject(index).getString("chargedeptname"));
                                enterprisedetail_staffnum.setText(jsonArray.optJSONObject(index).getString("staffnum"));
                                enterprisedetail_regcapital.setText(jsonArray.optJSONObject(index).getString("regcapital"));
                                enterprisedetail_zcaddress.setText(jsonArray.optJSONObject(index).getString("zcaddress"));
                                enterprisedetail_risktypename.setText(jsonArray.optJSONObject(index).getString("risktypename"));
                                enterprisedetail_riskscore.setText(jsonArray.optJSONObject(index).getString("riskscore"));

                                if (!jsonArray.getJSONObject(index).getString("longitudecoord").equals("") && !jsonArray.getJSONObject(index).getString("latitudecoord").equals("")) {
                                    qyLng = Double.parseDouble(jsonArray.getJSONObject(index).getString("longitudecoord"));
                                    qyLat = Double.parseDouble(jsonArray.getJSONObject(index).getString("latitudecoord"));
                                } else {
                                    qyLng = 0;
                                    qyLat = 0;
                                }

                                if (jsonArray.getJSONObject(index).getString("aqjg").equals("1")) {
                                    enterprise_detail_bmgl.showCirclePointBadge();
                                } else {
                                    enterprise_detail_bmgl.hiddenBadge();
                                }

                                if (jsonArray.getJSONObject(index).getString("zyzz").equals("1")) {
                                    enterprise_detail_qyzz.showCirclePointBadge();
                                } else {
                                    enterprise_detail_qyzz.hiddenBadge();
                                }
                                if (jsonArray.getJSONObject(index).getString("rygl").equals("1")) {
                                    enterprise_detail_rygl.showCirclePointBadge();
                                } else {
                                    enterprise_detail_rygl.hiddenBadge();
                                }
                                if (jsonArray.getJSONObject(index).getString("aqtr").equals("1")) {
                                    enterprise_detail_aqsctr.showCirclePointBadge();
                                } else {
                                    enterprise_detail_aqsctr.hiddenBadge();
                                }
                                if (jsonArray.getJSONObject(index).getString("gzzd").equals("1")) {
                                    enterprise_detail_gzzd.showCirclePointBadge();
                                } else {
                                    enterprise_detail_gzzd.hiddenBadge();
                                }
                                if (jsonArray.getJSONObject(index).getString("wxyl").equals("1")) {
                                    enterprise_detail_wxy.showCirclePointBadge();
                                } else {
                                    enterprise_detail_wxy.hiddenBadge();
                                }
                                if (jsonArray.getJSONObject(index).getString("zyws").equals("1")) {
                                    enterprise_detail_zywsxx.showCirclePointBadge();
                                } else {
                                    enterprise_detail_zywsxx.hiddenBadge();
                                }
                                if (jsonArray.getJSONObject(index).getString("aqjc").equals("1")) {
                                    enterprise_detail_aqjc.showCirclePointBadge();
                                } else {
                                    enterprise_detail_aqjc.hiddenBadge();
                                }
                                if (jsonArray.getJSONObject(index).getString("yhzg").equals("1")) {
                                    enterprise_detail_yhxx.showCirclePointBadge();
                                } else {
                                    enterprise_detail_yhxx.hiddenBadge();
                                }
                                if (jsonArray.getJSONObject(index).getString("xzzf").equals("1")) {
                                    enterprise_detail_xzzf.showCirclePointBadge();
                                } else {
                                    enterprise_detail_xzzf.hiddenBadge();
                                }
                                if (jsonArray.getJSONObject(index).getString("sbss").equals("1")) {
                                    enterprise_detail_tzsb.showCirclePointBadge();
                                } else {
                                    enterprise_detail_tzsb.hiddenBadge();
                                }
                                if (jsonArray.getJSONObject(index).getString("yjya").equals("1")) {
                                    enterprise_detail_yjjyxx.showCirclePointBadge();
                                } else {
                                    enterprise_detail_yjjyxx.hiddenBadge();
                                }
                                if (jsonArray.getJSONObject(index).getString("yjwz").equals("1")) {
                                    enterprise_detail_yjwz.showCirclePointBadge();
                                } else {
                                    enterprise_detail_yjwz.hiddenBadge();
                                }
                                if (jsonArray.getJSONObject(index).getString("yjyl").equals("1")) {
                                    enterprise_detail_yjyl.showCirclePointBadge();
                                } else {
                                    enterprise_detail_yjyl.hiddenBadge();
                                }
                                if (jsonArray.getJSONObject(index).getString("aqpx").equals("1")) {
                                    enterprise_detail_aqjypx.showCirclePointBadge();
                                } else {
                                    enterprise_detail_aqjypx.hiddenBadge();
                                }

                            } else {
                                ShowToast.showShort(EnterpriseDetail.this, R.string.getInfoErr);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    /**
     * 部门管理
     */
    @OnClick(R.id.enterprise_detail_bmgl)
    void Bmgl() {
        intent = new Intent(this, BmGl.class);
        intent.putExtra("mClickId", clickId);
        startActivity(intent);
    }

    /**
     * 企业证照
     */
    @OnClick(R.id.enterprise_detail_qyzz)
    void Qyzz() {
        intent = new Intent(this, QyZz.class);
        intent.putExtra("mycode", mycode);
        intent.putExtra("mClickId", clickId);
        startActivity(intent);
    }

    /**
     * 人员管理
     */
    @OnClick(R.id.enterprise_detail_rygl)
    void Rygl() {
        intent = new Intent(this, RyGl.class);
        intent.putExtra("mycode", mycode);
        intent.putExtra("mClickId", clickId);
        startActivity(intent);
    }

    /**
     * 安全投入
     */
    @OnClick(R.id.enterprise_detail_aqsctr)
    void Aqsctr() {
        intent = new Intent(this, AqScTr.class);
        intent.putExtra("mycode", mycode);
        intent.putExtra("mClickId", clickId);
        startActivity(intent);
    }

    /**
     * 安全制度
     */
    @OnClick(R.id.enterprise_detail_gzzd)
    void Gzzd() {
        intent = new Intent(this, Rules.class);
        intent.putExtra("mycode", mycode);
        intent.putExtra("mClickId", clickId);
        startActivity(intent);
    }

    /**
     * 危险源
     */
    @OnClick(R.id.enterprise_detail_wxy)
    void Wxy() {
        intent = new Intent(this, WeiXianYuan.class);
        intent.putExtra("mycode", mycode);
        intent.putExtra("mClickId", clickId);
        startActivity(intent);
    }

    /**
     * 职业卫生
     */
    @OnClick(R.id.enterprise_detail_zywsxx)
    void Zywsxx() {
        intent = new Intent(this, ZyWsXx.class);
        intent.putExtra("mycode", mycode);
        intent.putExtra("mClickId", clickId);
        startActivity(intent);
    }

    /**
     * 安全检查
     */
    @OnClick(R.id.enterprise_detail_aqjc)
    void Aqjc() {
        intent = new Intent(this, ScInfomation.class);
        intent.putExtra("mycode", mycode);
        intent.putExtra("mClickId", clickId);
        startActivity(intent);
    }

    /**
     * 隐患信息
     */
    @OnClick(R.id.enterprise_detail_yhxx)
    void Yhxx() {
        intent = new Intent(this, QyYhInformation.class);
        intent.putExtra("mycode", mycode);
        intent.putExtra("mClickId", clickId);
        startActivity(intent);
    }

    /**
     * 行政执法
     */
    @OnClick(R.id.enterprise_detail_xzzf)
    void Xzzf() {
        intent = new Intent(this,Enforcement.class);
        intent.putExtra("mycode", mycode);
        intent.putExtra("mClickId", clickId);
        startActivity(intent);
    }

    /**
     * 特种设备
     */
    @OnClick(R.id.enterprise_detail_tzsb)
    void Tzsb() {
        intent = new Intent(this, TzSb.class);
        intent.putExtra("mycode", mycode);
        intent.putExtra("mClickId", clickId);
        startActivity(intent);
    }

    /**
     * 应急预案
     */
    @OnClick(R.id.enterprise_detail_yjjyxx)
    void Yjya() {
        intent = new Intent(this, YjYaXx.class);
        intent.putExtra("mycode", mycode);
        intent.putExtra("mClickId", clickId);
        startActivity(intent);
    }

    /**
     * 应急物资
     */
    @OnClick(R.id.enterprise_detail_yjwz)
    void Yjwz() {
        intent = new Intent(this, YjWz.class);
        intent.putExtra("mClickId", clickId);
        startActivity(intent);
    }

    /**
     * 应急演练
     */
    @OnClick(R.id.enterprise_detail_yjyl)
    void Yjyl() {
        intent = new Intent(this, YjYl.class);
        intent.putExtra("mClickId", clickId);
        startActivity(intent);
    }

    /**
     * 安全培训
     */
    @OnClick(R.id.enterprise_detail_aqjypx)
    void Aqjypx() {
        intent = new Intent(this, AqJyPx.class);
        intent.putExtra("mycode", mycode);
        intent.putExtra("mClickId", clickId);
        startActivity(intent);
    }
}
