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

public class InstitutionalDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    private String dwid;
    private String personid;
    @BindView(R.id.institutional_detail_personname)
    TextView institutional_detail_personname;
    @BindView(R.id.institutional_detail_personsex)
    TextView institutional_detail_personsex;
    @BindView(R.id.institutional_detail_persontel)
    TextView institutional_detail_persontel;
    @BindView(R.id.institutional_detail_personzw)
    TextView institutional_detail_personzw;
    @BindView(R.id.institutional_detail_personbytel)
    TextView institutional_detail_personbytel;
    @BindView(R.id.institutional_detail_personzc)
    TextView institutional_detail_personzc;
    @BindView(R.id.institutional_detail_personzhiwei)
    TextView institutional_detail_personzhiwei;
    @BindView(R.id.institutional_detail_personcardnum)
    TextView institutional_detail_personcardnum;
    @BindView(R.id.institutional_detail_personmail)
    TextView institutional_detail_personmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institutional_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.institutional_detail_title);
        Intent intent = getIntent();
        dwid = intent.getStringExtra("dwid");
        personid = intent.getStringExtra("personid");
        mConnect(dwid, personid);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    private void mConnect(String dwid, String personid) {
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        OkGo.<String>get(PortIpAddress.Ldjgrylist())
                .tag(this)
                .params("ldjgrybean.dwid", dwid)
                .params("ldjgrybean.personid", personid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                institutional_detail_personname.setText(jsonArray.optJSONObject(0).getString("personname"));
                                institutional_detail_personsex.setText(jsonArray.optJSONObject(0).getString("personsex"));
                                institutional_detail_persontel.setText(jsonArray.optJSONObject(0).getString("persontel"));
                                institutional_detail_personzw.setText(jsonArray.optJSONObject(0).getString("personzw"));
                                institutional_detail_personbytel.setText(jsonArray.optJSONObject(0).getString("personbytel"));
                                institutional_detail_personzc.setText(jsonArray.optJSONObject(0).getString("personzc"));
                                institutional_detail_personzhiwei.setText(jsonArray.optJSONObject(0).getString("personzhiwei"));
                                institutional_detail_personcardnum.setText(jsonArray.optJSONObject(0).getString("personcardnum"));
                                institutional_detail_personmail.setText(jsonArray.optJSONObject(0).getString("personmail"));
                            } else {
                                ShowToast.showShort(InstitutionalDetail.this, R.string.getInfoErr);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dialog.dismiss();
                        ShowToast.showShort(InstitutionalDetail.this, R.string.connect_err);
                    }
                });
    }
}
