package tab.homedetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import adpter.EnterpriseAdapter;
import bean.EnterpriseBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import tab.homedetail.enterprisedetail.EnterpriseDetail;
import utils.BaseActivity;
import utils.DividerItemDecoration;
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;

/**
 * 企业信息
 */
public class Enterprise extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_name_right)
    TextView title_name_right;
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    //    @BindView(R.id.enterprise_type)
//    TextView enterprise_type;
//    @BindView(R.id.enterprise_line)
//    ImageView enterprise_line;
    private List<EnterpriseBean.CellsBean> mDatas;
    private List<EnterpriseBean.CellsBean> searchDatas;
    private EnterpriseAdapter adapter;
    private EnterpriseBean.CellsBean bean;
    //行业
    private String[] industry_arr;
    //行业集合
    private Map<String, String> industryMap;
    //页面标识
    private String intentIndex = "";
    private String tag = "";
    private String crtype = "";
    private String addTag = "";
    private String url = "";
    private String industryKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_list_layout);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.enterprise_title);
        Intent intent = getIntent();
        intentIndex = intent.getStringExtra("intentIndex");
        tag = intent.getStringExtra("tag");
        addTag = intent.getStringExtra("addTag");

//        if (tag.equals("home")) {
        url = PortIpAddress.Companyinfo();
        crtype = "";
        industryKey = "bean.chargedept";
//        } else if (tag.equals("riskmanagement")) {
//            url = PortIpAddress.YhCompanyinfo();
//            crtype = intent.getStringExtra("crtype");
//            industryKey = "ribean.chargeDept";
//        }

//        else if (tag.equals("safety")) {
//            crtype = "";
//            enterprise_type.setVisibility(View.VISIBLE);
//            enterprise_line.setVisibility(View.VISIBLE);
//            //初始化数据集
//            String[] type_arr = getResources().getStringArray(R.array.zd_type);
//            enterprise_type.setText(type_arr[0]);
//            setPopWindowFull(enterprise_type, type_arr, refreshLayout);
//        }
        //将数据添加至map集合

        //初始化数据集
        industry_arr = getResources().getStringArray(R.array.industry);
        industryMap = new HashMap<>();
        title_name_right.setText(industry_arr[0]);

        industryMap.put(industry_arr[0], "");
        industryMap.put(industry_arr[1], "9c6eed6f7ecd482793293d39e60118e8");
        industryMap.put(industry_arr[2], "2fff1d3f587f40a3821edd0375edc018");
        industryMap.put(industry_arr[3], "3963ae5ed1164b32a421d28409050c9b");
        industryMap.put(industry_arr[4], "dc4d58a5e6ec49498167e15ffda2e09b");
        initRv();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
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
            searchDatas = new ArrayList<EnterpriseBean.CellsBean>();
            for (EnterpriseBean.CellsBean entity : mDatas) {
                try {
                    if (entity.getComname().contains(str) || entity.getIndustryname().contains(str) ||
                            entity.getZcaddress().contains(str) || entity.getAddStatus().contains(str) || entity.getTablename().contains(str)) {
                        searchDatas.add(entity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.setNewData(searchDatas);
            }
        }
    }

    /**
     * 设置企业列表
     */
    private void initRv() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        if (adapter == null) {
            adapter = new EnterpriseAdapter(R.layout.enterprise_item, mDatas);
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
                    ShowToast.showShort(Enterprise.this, R.string.loadSuccess);
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

    /**
     * 获取接口数据
     */
    private void mConnect(String industry) {
        OkGo.<String>get(url)
                .tag(this)
                .params(industryKey, industry)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new EnterpriseBean.CellsBean();
                                    bean.setQyid(jsonArray.optJSONObject(i).getString("qyid"));
                                    bean.setComname(jsonArray.optJSONObject(i).getString("comname"));
                                    bean.setIndustryname(jsonArray.optJSONObject(i).getString("industryname"));
                                    bean.setZcaddress(jsonArray.optJSONObject(i).getString("zcaddress"));
                                    bean.setAddStatus(jsonArray.optJSONObject(i).getString("addStatus"));
                                    bean.setTablename(jsonArray.optJSONObject(i).getString("tablename"));
                                    mDatas.add(bean);
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
                                        bean = (EnterpriseBean.CellsBean) adapter.getData().get(position);
                                        Intent intent = new Intent(Enterprise.this, EnterpriseDetail.class);
                                        intent.putExtra("clickId", bean.getQyid());
                                        startActivity(intent);
                                    }
                                });

                            } else {
                                ShowToast.showShort(Enterprise.this, R.string.getInfoErr);
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
                        ShowToast.showShort(Enterprise.this, R.string.connect_err);
                    }
                });
    }


}
