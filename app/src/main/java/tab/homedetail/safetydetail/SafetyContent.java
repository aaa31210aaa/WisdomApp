package tab.homedetail.safetydetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adpter.HiddenDangerDetailAdapter;
import bean.HiddenDangerDetailBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;
import utils.DividerItemDecoration;
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;
import static utils.PortIpAddress.TOKEN_KEY;


public class SafetyContent extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //    @BindView(R.id.search_edittext)
//    EditText search_edittext;
//    @BindView(R.id.search_clear)
//    ImageView search_clear;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<HiddenDangerDetailBean> searchDatas;
    private List<HiddenDangerDetailBean> mDatas;
    private HiddenDangerDetailAdapter adapter;
    private String clickId;
    private String qyid;
    @BindView(R.id.safety_content_cycle)
    TextView safety_content_cycle;
    //周期
    private String[] cycle_arr;
    //周期
    private Map<String, String> cycleMap = new HashMap<>();
    private boolean[] flag;
    private int[] flag_btn;
    private String mNamestr;
    private String zcbzid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_content);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        clickId = intent.getStringExtra("clickId");
//        qyid = intent.getStringExtra("clickId");
        zcbzid = intent.getStringExtra("zcbzid");
        initToolbar();
        initIndustry();
        initRv();
    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化检查周期
     */
    private void initIndustry() {
        cycle_arr = getResources().getStringArray(R.array.safety_content_cycle);
        safety_content_cycle.setText(cycle_arr[0]);
        cycleMap.put(cycle_arr[0], "");
        cycleMap.put(cycle_arr[1], "PCZQ007");
        cycleMap.put(cycle_arr[2], "PCZQ008");
        cycleMap.put(cycle_arr[3], "PCZQ001");
        cycleMap.put(cycle_arr[4], "PCZQ002");
        cycleMap.put(cycle_arr[5], "PCZQ003");
        cycleMap.put(cycle_arr[6], "PCZQ004");
        cycleMap.put(cycle_arr[7], "PCZQ005");
        cycleMap.put(cycle_arr[8], "PCZQ006");
        cycleMap.put(cycle_arr[9], "PCZQ009");
    }

    private void initRv() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        mDatas = new ArrayList<>();
        isFirstEnter = true;
        initRefresh();
//      MonitorEditext();
    }

    /**
     * 设置下拉刷新
     */
    private void initRefresh() {
        if (isFirstEnter) {
            isFirstEnter = false;
            refreshLayout.autoRefresh();//第一次进入触发自动刷新
        }

        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                handler.sendEmptyMessageDelayed(1, ShowToast.refreshTime);
            }
        });

        //加载
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                handler.sendEmptyMessageDelayed(0, ShowToast.refreshTime);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    ShowToast.showShort(SafetyContent.this, R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
                    mConnect();
                    break;
                default:
                    break;
            }
        }
    };

    @OnClick(R.id.safety_content_cycle)
    void CycleSelect() {
        CycleDialog();
    }

    /**
     * 周期分类dialog
     */
    private void CycleDialog() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cycle_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择周期").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        safety_content_cycle.setText(cycle_arr[which]);
                        refreshLayout.autoRefresh();
                    }
                }).create();
        dialog.show();
    }


    private void mConnect() {
        OkGo.<String>get(PortIpAddress.Riskzcbzdetail())
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.GetToken(this))
                .params("cycle", cycleMap.get(safety_content_cycle.getText().toString()))
                .params("bean.zcbzid", zcbzid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e(TAG, response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);

                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                mDatas.clear();
                                flag = new boolean[jsonArray.length()];
                                flag_btn = new int[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    HiddenDangerDetailBean bean = new HiddenDangerDetailBean();
                                    bean.setIndex(String.valueOf(i + 1));
                                    if (jsonArray.optJSONObject(i).getString("amount").equals("0")) {
                                        bean.setmVisible(false);
                                        flag[i] = true;
                                    } else {
                                        bean.setmVisible(true);
                                        flag[i] = false;
                                    }

                                    bean.setZcbzdid(jsonArray.optJSONObject(i).getString("zcbzdid"));
                                    bean.setQyid(jsonArray.optJSONObject(i).getString("qyid"));
                                    bean.setPczq(jsonArray.optJSONObject(i).getString("pczq"));
                                    bean.setPcdeptname(jsonArray.optJSONObject(i).getString("pcdeptname"));
                                    bean.setAmount(jsonArray.optJSONObject(i).getString("amount"));
                                    mNamestr = jsonArray.optJSONObject(i).getString("namestr").replace("\\", "");
                                    bean.setNamestr(mNamestr);
                                    bean.setRscdesc(jsonArray.optJSONObject(i).getString("rscdesc"));
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new HiddenDangerDetailAdapter(SafetyContent.this, mDatas, flag);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas, flag);
                                }
                            } else {
                                ShowToast.showShort(SafetyContent.this, R.string.getInfoErr);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh();
                        ShowToast.showShort(SafetyContent.this, R.string.connect_err);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mConnect();
        }
    }

//    /**
//     * 监听搜索框
//     */
//    private void MonitorEditext() {
//        search_edittext.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.e(TAG, count + "----");
//                if (mDatas != null) {
//                    if (search_edittext.length() > 0) {
//                        refreshLayout.setEnableRefresh(false);
//                        search_clear.setVisibility(View.VISIBLE);
//                        search(search_edittext.getText().toString().trim());
//                    } else {
//                        refreshLayout.setEnableRefresh(true);
//                        search_clear.setVisibility(View.GONE);
//                        if (adapter != null) {
//                            adapter.setNewData(mDatas);
//                        }
//                    }
//                } else {
//                    if (search_edittext.length() > 0) {
//                        search_clear.setVisibility(View.VISIBLE);
//                    } else {
//                        search_clear.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }
//
//    /**
//     * 清除搜索框内容
//     */
//    @OnClick(R.id.search_clear)
//    public void ClearSearch() {
//        search_edittext.setText("");
//        search_clear.setVisibility(View.GONE);
//    }
//
//    //搜索框
//    private void search(String str) {
//        if (mDatas != null) {
//            searchDatas = new ArrayList<HiddenDangerDetailBean>();
//            for (HiddenDangerDetailBean entity : mDatas) {
//                try {
//                    if () {
//                        searchDatas.add(entity);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                adapter.setNewData(searchDatas);
//            }
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        refreshLayout.finishRefresh();
    }
}
