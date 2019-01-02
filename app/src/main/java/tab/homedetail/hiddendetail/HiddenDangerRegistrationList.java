package tab.homedetail.hiddendetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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

import adpter.RiskListAdapter;
import bean.QyYhInformationBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;
import utils.DividerItemDecoration;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;
import static utils.PortIpAddress.TOKEN_KEY;

public class HiddenDangerRegistrationList extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_name_right)
    TextView title_name_right;
    @BindView(R.id.hidden_risk_list_add)
    FloatingActionButton hidden_risk_list_add;
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private List<QyYhInformationBean.CellsBean> mDatas;
    private List<QyYhInformationBean.CellsBean> searchDatas;
    private QyYhInformationBean.CellsBean bean;
    private RiskListAdapter adapter;

    private String clickId = "";
    private String qyidKey = "";
    private String crtype = "";
    //行业
    private String[] industry_arr;
    //行业集合
    private Map<String, String> industryMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_danger_registration_list);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        if (PortIpAddress.getUserType(this)) {
            title_name.setText(R.string.hidden_danger_title);
        }else{
            title_name.setText(R.string.hidden_danger_title_qy);
        }

        industry_arr = getResources().getStringArray(R.array.industry);
        industryMap = new HashMap<>();
        title_name_right.setText(industry_arr[0]);
        industryMap.put(industry_arr[0], "");
        industryMap.put(industry_arr[1], "9c6eed6f7ecd482793293d39e60118e8");
        industryMap.put(industry_arr[2], "2fff1d3f587f40a3821edd0375edc018");
        industryMap.put(industry_arr[3], "3963ae5ed1164b32a421d28409050c9b");
        industryMap.put(industry_arr[4], "dc4d58a5e6ec49498167e15ffda2e09b");

        Intent intent = getIntent();
        if (SharedPrefsUtil.getValue(this, "userInfo", "usertype", "").equals("2")) {
            qyidKey = "qyid";
            clickId = SharedPrefsUtil.getValue(this, "userInfo", "userid", "");
        }
        crtype = intent.getStringExtra("crtype");
        initRv();
    }


    @OnClick(R.id.title_name_right)
    void Industry() {
        ShowIndustry();
    }

    private void ShowIndustry() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industry_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择行业分类").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        title_name_right.setText(industry_arr[which]);
                        refreshLayout.autoRefresh();
                    }
                }).create();
        dialog.show();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }


    @OnClick(R.id.hidden_risk_list_add)
    void HiddenAdd() {
        Intent intent = new Intent(this, AddHiddenDangerRegistration.class);
        intent.putExtra("clickId", clickId);
        startActivityForResult(intent, 10);
    }

    private void initRv() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        if (adapter == null) {
            adapter = new RiskListAdapter(R.layout.risklist_item, mDatas);
            adapter.bindToRecyclerView(recyclerView);
            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
            adapter.isFirstOnly(true);
            recyclerView.setAdapter(adapter);
        }
        mDatas = new ArrayList<>();
        isFirstEnter = true;
        initRefresh();
        MonitorEditext();
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
                    ShowToast.showShort(HiddenDangerRegistrationList.this, R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
                    mConnect(industryMap.get(title_name_right.getText().toString()));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 10) {
                mConnect(industryMap.get(title_name_right.getText().toString()));
            }
        }
    }

    private void mConnect(String industry) {
        OkGo.<String>get(PortIpAddress.GetRiskinfo())
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.GetToken(this))
                .params(qyidKey, clickId)
                .params("crtype", crtype)
                .params("chargedept", industry)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e(TAG, response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);

                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new QyYhInformationBean.CellsBean();
                                    bean.setCrname(jsonArray.optJSONObject(i).getString("crname"));
                                    bean.setYhid(jsonArray.optJSONObject(i).getString("yhid"));
                                    bean.setCrtypename(jsonArray.optJSONObject(i).getString("crtypename"));
                                    bean.setPcdate(jsonArray.optJSONObject(i).getString("pcdate"));
//                                    if (jsonArray.optJSONObject(i).getString("crtypename").equals("重大隐患")) {
                                    mDatas.add(bean);
//                                    }
                                }

                                adapter.setNewData(mDatas);

                                //如果无数据设置空布局
                                if (mDatas.size() == 0) {
                                    adapter.setEmptyView(R.layout.nodata_layout, (ViewGroup) recyclerView.getParent());
                                }

                                //子项点击事件
                                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        if (SharedPrefsUtil.getValue(HiddenDangerRegistrationList.this, "userInfo", "usertype", "").equals("1")) {
                                            bean = (QyYhInformationBean.CellsBean) adapter.getData().get(position);
                                            Intent intent = new Intent(HiddenDangerRegistrationList.this, QyYhInfomationDetail.class);
                                            intent.putExtra("yhid", bean.getYhid());
                                            startActivity(intent);
                                        } else {
                                            bean = (QyYhInformationBean.CellsBean) adapter.getData().get(position);
                                            Intent intent = new Intent(HiddenDangerRegistrationList.this, HiddenDangerDetail.class);
                                            intent.putExtra("yhid", bean.getYhid());
                                            startActivity(intent);
                                        }
                                    }
                                });
                            } else {
                                ShowToast.showShort(HiddenDangerRegistrationList.this, R.string.getInfoErr);
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
                        ShowToast.showShort(HiddenDangerRegistrationList.this, R.string.connect_err);
                    }
                });
    }


    /**
     * 监听搜索框
     */
    private void MonitorEditext() {
        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, count + "----");
                if (mDatas != null) {
                    if (search_edittext.length() > 0) {
                        refreshLayout.setEnableRefresh(false);
                        search_clear.setVisibility(View.VISIBLE);
                        search(search_edittext.getText().toString().trim());
                    } else {
                        refreshLayout.setEnableRefresh(true);
                        search_clear.setVisibility(View.GONE);
                        if (adapter != null) {
                            adapter.setNewData(mDatas);
                        }
                    }
                } else {
                    if (search_edittext.length() > 0) {
                        search_clear.setVisibility(View.VISIBLE);
                    } else {
                        search_clear.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 清除搜索框内容
     */
    @OnClick(R.id.search_clear)
    public void ClearSearch() {
        search_edittext.setText("");
        search_clear.setVisibility(View.GONE);
    }

    //搜索框
    private void search(String str) {
        if (mDatas != null) {
            searchDatas = new ArrayList<QyYhInformationBean.CellsBean>();
            for (QyYhInformationBean.CellsBean entity : mDatas) {
                try {
                    if (entity.getCrname().contains(str) || entity.getCrtypename().contains(str) || entity.getPcdate().contains(str)) {
                        searchDatas.add(entity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.setNewData(searchDatas);
            }
        }
    }

}
