package tab.homedetail.hiddendetail;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.DeptBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;
import utils.DateUtils;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;

public class AddZkdsrygl extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.add_zkdsrygl_paramname)
    TextView add_zkdsrygl_paramname;
    @BindView(R.id.add_zkdsrygl_townshipname)
    TextView add_zkdsrygl_townshipname;
    @BindView(R.id.add_zkdsrygl_fusername)
    TextView add_zkdsrygl_fusername;
    @BindView(R.id.add_zkdsrygl_linktel)
    EditText add_zkdsrygl_linktel;
    @BindView(R.id.add_zkdsrygl_comname)
    TextView add_zkdsrygl_comname;
    @BindView(R.id.add_zkdsrygl_date)
    TextView add_zkdsrygl_date;
    @BindView(R.id.add_zkdsrygl_memo)
    EditText add_zkdsrygl_memo;

    //人员类型集合
    private List<String> paramname_arr;
    private Map<String, String> paramname_map;
    //乡镇集合
    private List<String> townshipname_arr;
    private Map<String, String> townshipname_map;
    //姓名集合
    private List<String> fusername_arr;
    private Map<String, String> fusername_map;
    //驻矿企业集合
    private List<String> comname_arr;
    private Map<String, String> comname_map;
    private Intent intent;
    private boolean first_click = true;
    //日期
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatter;
    private int myYear;
    private int myMonth;
    private int myDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zkdsrygl);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        initToolbar();
        paramname_arr = new ArrayList<>();
        paramname_map = new HashMap<>();
        townshipname_arr = new ArrayList<>();
        townshipname_map = new HashMap<>();
        fusername_arr = new ArrayList<>();
        fusername_map = new HashMap<>();
        comname_arr = new ArrayList<>();
        comname_map = new HashMap<>();
        //设置初始日期
        String nowdate = DateUtils.getStringDateShort();
        add_zkdsrygl_date.setText(nowdate);
        getParamname();
        getTownshipname();
        getFusername();
    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.add_zkdsrygl)
    void Submit() {
        if (add_zkdsrygl_linktel.getText().toString().trim().equals("")) {
            ShowToast.showShort(this, "请填写联系电话");
        } else {
            dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
            try {
                OkGo.<String>get(PortIpAddress.SavePeopleInfo())
                        .tag(this)
                        .params("inminebean.usertype", paramname_map.get(add_zkdsrygl_paramname.getText().toString()))
                        .params("inminebean.townshipid", townshipname_map.get(add_zkdsrygl_townshipname.getText().toString()))
                        .params("inminebean.username", fusername_map.get(add_zkdsrygl_fusername.getText().toString()))
                        .params("inminebean.linktel", URLEncoder.encode(add_zkdsrygl_linktel.getText().toString(), "UTF-8"))
                        .params("inminebean.inminedeptid", comname_map.get(add_zkdsrygl_comname.getText().toString()))
                        .params("inminebean.inminetime", add_zkdsrygl_date.getText().toString())
                        .params("inminebean.memo", URLEncoder.encode(add_zkdsrygl_memo.getText().toString(), "UTF-8"))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().toString());
                                    if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                        ShowToast.showToastNoWait(AddZkdsrygl.this, R.string.write_success);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    } else {
                                        ShowToast.showToastNoWait(AddZkdsrygl.this, R.string.write_fail);
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
                                ShowToast.showShort(AddZkdsrygl.this, R.string.connect_err);
                            }
                        });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 选择人员类型
     */
    @OnClick(R.id.add_zkdsrygl_paramname)
    void Paramname() {
        ShowParamnameDialog();
    }

    private void ShowParamnameDialog() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, paramname_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择人员类型").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        add_zkdsrygl_paramname.setText(paramname_arr.get(which));
                    }
                }).create();
        dialog.show();
    }

    /**
     * 获取人员类型数据
     */
    private void getParamname() {
        OkGo.<String>get(PortIpAddress.UserTypeList())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                if (jsonArray.length() > 0) {
                                    paramname_arr.clear();
                                    paramname_map.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        DeptBean.CellsBean bean = new DeptBean.CellsBean();
                                        bean.setCode(jsonArray.optJSONObject(i).getString("code"));
                                        bean.setName(jsonArray.optJSONObject(i).getString("name"));
                                        paramname_arr.add(bean.getName());
                                        paramname_map.put(bean.getName(), bean.getCode());
                                    }
                                    add_zkdsrygl_paramname.setText(paramname_arr.get(0));
                                }
                            } else {
                                ShowToast.showShort(AddZkdsrygl.this, R.string.getInfoErr);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(AddZkdsrygl.this, R.string.connect_err);
                    }
                });
    }

    /**
     * 选择乡镇集合
     */
    @OnClick(R.id.add_zkdsrygl_townshipname)
    void Townshipname() {
        ShowTownshipnameDialog();
    }

    private void ShowTownshipnameDialog() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, townshipname_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择乡镇").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        add_zkdsrygl_townshipname.setText(townshipname_arr.get(which));
                        getComname(townshipname_map.get(add_zkdsrygl_townshipname.getText().toString()));
                    }
                }).create();
        dialog.show();
    }

    /**
     * 获取乡镇数据
     */
    private void getTownshipname() {
        OkGo.<String>get(PortIpAddress.TownshipList())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                if (jsonArray.length() > 0) {
                                    townshipname_arr.clear();
                                    townshipname_map.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        DeptBean.CellsBean bean = new DeptBean.CellsBean();
                                        bean.setCode(jsonArray.optJSONObject(i).getString("code"));
                                        bean.setName(jsonArray.optJSONObject(i).getString("name"));
                                        townshipname_arr.add(bean.getName());
                                        townshipname_map.put(bean.getName(), bean.getCode());
                                    }
                                    add_zkdsrygl_townshipname.setText(townshipname_arr.get(0));
                                    getComname(townshipname_map.get(add_zkdsrygl_townshipname.getText().toString()));
                                }
                            } else {
                                ShowToast.showShort(AddZkdsrygl.this, R.string.getInfoErr);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(AddZkdsrygl.this, R.string.connect_err);
                    }
                });
    }


    /**
     * 选择人员姓名
     */
    @OnClick(R.id.add_zkdsrygl_fusername)
    void Fusername() {
        ShowFusernameDialog();
    }

    private void ShowFusernameDialog() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fusername_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择人员姓名").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        add_zkdsrygl_fusername.setText(fusername_arr.get(which));
                    }
                }).create();
        dialog.show();
    }

    /**
     * 获取人员姓名信息
     */
    private void getFusername() {
        OkGo.<String>get(PortIpAddress.PeopleList())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                if (jsonArray.length() > 0) {
                                    fusername_arr.clear();
                                    fusername_map.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        DeptBean.CellsBean bean = new DeptBean.CellsBean();
                                        bean.setCode(jsonArray.optJSONObject(i).getString("code"));
                                        bean.setName(jsonArray.optJSONObject(i).getString("name"));
                                        fusername_arr.add(bean.getName());
                                        fusername_map.put(bean.getName(), bean.getCode());
                                    }
                                    add_zkdsrygl_fusername.setText(fusername_arr.get(0));
                                }
                            } else {
                                ShowToast.showShort(AddZkdsrygl.this, R.string.getInfoErr);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(AddZkdsrygl.this, R.string.connect_err);
                    }
                });
    }

    /**
     * 选择驻矿企业
     */
    @OnClick(R.id.add_zkdsrygl_comname)
    void Comname() {
        ShowComnameDialog();
    }

    private void ShowComnameDialog() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, comname_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择驻矿企业").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        add_zkdsrygl_comname.setText(comname_arr.get(which));
                    }
                }).create();
        dialog.show();
    }

    /**
     * 获取驻矿企业信息
     */
    private void getComname(String townshipid) {
        OkGo.<String>get(PortIpAddress.ZkqyList())
                .tag(this)
                .params("townshipid", townshipid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e(TAG, response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                if (jsonArray.length() > 0) {
                                    comname_arr.clear();
                                    comname_map.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        DeptBean.CellsBean bean = new DeptBean.CellsBean();
                                        bean.setCode(jsonArray.optJSONObject(i).getString("code"));
                                        bean.setName(jsonArray.optJSONObject(i).getString("name"));
                                        comname_arr.add(bean.getName());
                                        comname_map.put(bean.getName(), bean.getCode());
                                    }
                                    add_zkdsrygl_comname.setText(comname_arr.get(0));
                                }
                            } else {
                                ShowToast.showShort(AddZkdsrygl.this, R.string.getInfoErr);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(AddZkdsrygl.this, R.string.connect_err);
                    }
                });
    }

    /**
     * 选择日期
     */
    @OnClick(R.id.add_zkdsrygl_date)
    void Date() {
        ShowDateDialog();
    }

    private void ShowDateDialog() {
        if (first_click) {
            DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    try {
                        String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = formatter.parse(select_time);
                        String select_date = formatter.format(date);
                        // TODO Auto-generated method stub
                        add_zkdsrygl_date.setText(select_date);
                        myYear = year;
                        myMonth = monthOfYear;
                        myDay = dayOfMonth;
                        first_click = false;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog_date.show();
        } else {
            DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    try {
                        String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = formatter.parse(select_time);
                        String select_date = formatter.format(date);
                        // TODO Auto-generated method stub
                        add_zkdsrygl_date.setText(select_date);
                        myYear = year;
                        myMonth = monthOfYear;
                        myDay = dayOfMonth;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, myYear, myMonth, myDay);
            dialog_date.show();
        }
    }
}
