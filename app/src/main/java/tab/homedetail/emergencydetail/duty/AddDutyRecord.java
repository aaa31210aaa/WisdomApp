package tab.homedetail.emergencydetail.duty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.DeptBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;

public class AddDutyRecord extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_name_right)
    ImageView title_name_right;
    //应急启动事件
    @BindView(R.id.add_duty_record_summaryname)
    TextView add_duty_record_summaryname;
    //值班计划名称
    @BindView(R.id.add_duty_record_dutyPlanName)
    TextView add_duty_record_dutyPlanName;
    //当班人
    @BindView(R.id.add_duty_record_recordbeonduty)
    EditText add_duty_record_recordbeonduty;
    //记录内容
    @BindView(R.id.add_duty_record_recordcontent)
    EditText add_duty_record_recordcontent;
    //值班内容
    @BindView(R.id.add_duty_record_dutycontent)
    EditText add_duty_record_dutycontent;
    //处理情况
    @BindView(R.id.add_duty_record_handlecase)
    EditText add_duty_record_handlecase;
    //    //记录人
//    @BindView(R.id.add_duty_record_recordman)
//    EditText add_duty_record_recordman;
//    //记录时间
//    @BindView(R.id.add_duty_record_recorddate)
//    EditText add_duty_record_recorddate;
    //应急启动事件集合
    private List<String> summaryname_arr = new ArrayList<>();
    private Map<String, String> summaryname_map = new HashMap<>();
    //值班计划名称集合
    private List<String> dutyPlanName_arr = new ArrayList<>();
    private Map<String, String> dutyPlanName_map = new HashMap<>();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_duty_record);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.add_duty_record_title);
        title_name_right.setBackgroundResource(R.drawable.msubmit);
        getSummaryname();
    }


    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    @OnClick(R.id.title_name_right)
    void AddSubmit() {
        if (add_duty_record_recordbeonduty.getText().toString().trim().equals("")) {
            ShowToast.showShort(this, "请填写当班人");
        } else if (add_duty_record_dutycontent.getText().toString().trim().equals("")) {
            ShowToast.showShort(this, "请填写值班内容");
        } else if (add_duty_record_recordcontent.getText().toString().trim().equals("")) {
            ShowToast.showShort(this, "请填写记录内容");
        } else {
            dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
            try {
                OkGo.<String>get(PortIpAddress.AddRecordInfo())
                        .tag(this)
                        .params("emeRecordbean.Sumstartinfoid", summaryname_map.get(add_duty_record_summaryname.getText().toString()))
                        .params("emeRecordbean.dutyplanid", dutyPlanName_map.get(add_duty_record_dutyPlanName.getText().toString()))
                        .params("emeRecordbean.recordbeonduty", URLEncoder.encode(add_duty_record_recordbeonduty.getText().toString(), "UTF-8"))
                        .params("emeRecordbean.recordcontent", URLEncoder.encode(add_duty_record_recordcontent.getText().toString(), "UTF-8"))
                        .params("emeRecordbean.dutycontent", URLEncoder.encode(add_duty_record_dutycontent.getText().toString(), "UTF-8"))
                        .params("emeRecordbean.handlecase", URLEncoder.encode(add_duty_record_handlecase.getText().toString(), "UTF-8"))
                        .params("emeRecordbean.recordman", URLEncoder.encode(SharedPrefsUtil.getValue(this, "userInfo", "username", "")))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                Log.e(TAG, response.body().toString());
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().toString());
                                    if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                        ShowToast.showToastNoWait(AddDutyRecord.this, R.string.write_success);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    } else {
                                        ShowToast.showToastNoWait(AddDutyRecord.this, R.string.write_fail);
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
                                ShowToast.showShort(AddDutyRecord.this, R.string.connect_err);
                            }
                        });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 选择应急启动事件
     */
    @OnClick(R.id.add_duty_record_summaryname)
    void InitSummaryname() {
        ShowSummarynameDialog();
    }

    private void ShowSummarynameDialog() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, summaryname_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择应急启动事件").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        add_duty_record_summaryname.setText(summaryname_arr.get(which));
                        getDutyPlanName(summaryname_map.get(add_duty_record_summaryname.getText().toString()));
                    }
                }).create();
        dialog.show();
    }

    /**
     * 获取应急启动事件信息
     */
    private void getSummaryname() {
        OkGo.<String>get(PortIpAddress.EventList())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                summaryname_arr.clear();
                                summaryname_map.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    DeptBean.CellsBean bean = new DeptBean.CellsBean();
                                    bean.setCode(jsonArray.optJSONObject(i).getString("code"));
                                    bean.setName(jsonArray.optJSONObject(i).getString("name"));
                                    summaryname_arr.add(bean.getName());
                                    summaryname_map.put(bean.getName(), bean.getCode());
                                }
                                add_duty_record_summaryname.setText(summaryname_arr.get(0));
                                getDutyPlanName(summaryname_map.get(summaryname_arr.get(0)));
                            } else {
                                ShowToast.showShort(AddDutyRecord.this, R.string.getInfoErr);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(AddDutyRecord.this, R.string.connect_err);
                    }
                });
    }


    /**
     * 选择值班计划
     */
    @OnClick(R.id.add_duty_record_dutyPlanName)
    void InitDutyPlanName() {
        ShowDutyPlanNameDialog();
    }

    private void ShowDutyPlanNameDialog() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dutyPlanName_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择值班计划").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        add_duty_record_dutyPlanName.setText(dutyPlanName_arr.get(which));
                    }
                }).create();
        dialog.show();
    }

    /**
     * 获取值班计划信息
     */
    private void getDutyPlanName(String sumstartinfoid) {
        OkGo.<String>get(PortIpAddress.PlanByEvent())
                .tag(this)
                .params("emePlanbean.sumstartinfoid", sumstartinfoid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                dutyPlanName_arr.clear();
                                dutyPlanName_map.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    DeptBean.CellsBean bean = new DeptBean.CellsBean();
                                    bean.setCode(jsonArray.optJSONObject(i).getString("code"));
                                    bean.setName(jsonArray.optJSONObject(i).getString("name"));
                                    dutyPlanName_arr.add(bean.getName());
                                    dutyPlanName_map.put(bean.getName(), bean.getCode());
                                }
                                add_duty_record_dutyPlanName.setText(dutyPlanName_arr.get(0));
                            } else {
                                ShowToast.showShort(AddDutyRecord.this, R.string.getInfoErr);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(AddDutyRecord.this, R.string.connect_err);
                    }
                });
    }


}
