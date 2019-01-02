package tab.homedetail.enforcementdetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import adpter.JobListAdapter;
import bean.XcJcBean;
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

public class TypeInfoList extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.typeinfo_sp)
    TextView typeinfo_sp;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<XcJcBean> mDatas;
    private List<XcJcBean> searchDatas;
    private String typename;
    private String typeid;
    private JobListAdapter adapter;
    private Map<String, String> type_map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_info_list);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        typename = intent.getStringExtra("typename");
        typeid = intent.getStringExtra("typeid");
        title_name.setText(typename);
        initSpinner();
        initRv();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    private void initSpinner() {
        //初始化数据集
        String[] type_arr = getResources().getStringArray(R.array.enforcement_sp);
        typeinfo_sp.setText(type_arr[0]);
        type_map.put(type_arr[0], "");
        type_map.put(type_arr[1], "XZZFZT001");
        type_map.put(type_arr[2], "XZZFZT002");
        type_map.put(type_arr[3], "XZZFZT003");

        setPopWindowFull(typeinfo_sp, type_arr, refreshLayout);
    }

    private void initRv() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        if (adapter == null) {
            adapter = new JobListAdapter(R.layout.job_list_item, mDatas);
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
                    ShowToast.showShort(TypeInfoList.this, R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
                    mConnect(type_map.get(typeinfo_sp.getText().toString()));
                    break;
                default:
                    break;
            }
        }
    };

    private void mConnect(String handlingstatus) {
        OkGo.<String>get(PortIpAddress.Xzzf())
                .tag(this)
                .params("bean.ChargeDept", typeid)
                .params("bean.handlingstatus", handlingstatus)
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
                                    XcJcBean bean = new XcJcBean();
//                                    bean.setImage(R.drawable.noimg);
                                    bean.setCrid(jsonArray.optJSONObject(i).getString("crid"));
                                    bean.setBjcdw(jsonArray.optJSONObject(i).getString("bjcdwname"));
                                    bean.setHandlingstatusname(jsonArray.optJSONObject(i).getString("handlingstatusname"));
                                    bean.setStarttime(jsonArray.optJSONObject(i).getString("starttime"));
                                    bean.setEndtime(jsonArray.optJSONObject(i).getString("endtime"));
                                    bean.setRemark(jsonArray.optJSONObject(i).getString("remark"));
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
                                        XcJcBean bean = (XcJcBean) adapter.getData().get(position);
                                        Intent intent = new Intent(TypeInfoList.this, TypeInfoDetail.class);
                                        intent.putExtra("crid", bean.getCrid());
                                        intent.putExtra("typename", typename);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                ShowToast.showShort(TypeInfoList.this, R.string.getInfoErr);
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
                        ShowToast.showShort(TypeInfoList.this, R.string.connect_err);
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
            searchDatas = new ArrayList<XcJcBean>();
            for (XcJcBean entity : mDatas) {
                try {
                    if (entity.getBjcdw().contains(str) || entity.getRemark().contains(str) || entity.getStarttime().contains(str) || entity.getHandlingstatusname().contains(str)) {
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
