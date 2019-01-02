package tab.homedetail.hiddendetail;

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
import java.util.List;

import adpter.LxxzldAdapter;
import bean.ZkdsryglBean;
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

public class Lxxzld extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<ZkdsryglBean.CellsBean> searchDatas;
    private List<ZkdsryglBean.CellsBean> mDatas;
    private LxxzldAdapter adapter;
    private ZkdsryglBean.CellsBean bean;
    private String usertype = "";
    private String userid = "";

    private String ym = "Lxxzld";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_list_layout);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        usertype = intent.getStringExtra("usertype");
        userid = SharedPrefsUtil.getValue(this, "userInfo", "userid", "");
        title_name.setText(intent.getStringExtra("title_name"));
        initRv();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }


    private void initRv() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        if (adapter == null) {
            adapter = new LxxzldAdapter(R.layout.yjjy_item5, mDatas);
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
                    ShowToast.showShort(Lxxzld.this, R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
                    mConnect(usertype);
                    break;
                default:
                    break;
            }
        }
    };

    private void mConnect(String usertype) {
        OkGo.<String>get(PortIpAddress.Zkdsrygl())
                .tag(this)
                .params("inminebean.usertype", usertype)
                .params("inminebean.username", userid)
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
                                    bean = new ZkdsryglBean.CellsBean();
                                    bean.setUserid(jsonArray.optJSONObject(i).getString("username"));
                                    bean.setUsertype(jsonArray.optJSONObject(i).getString("usertype"));
                                    bean.setParamname(jsonArray.optJSONObject(i).getString("paramname"));
                                    bean.setFusername(jsonArray.optJSONObject(i).getString("fusername"));
                                    bean.setTownshipname(jsonArray.optJSONObject(i).getString("townshipname"));
                                    bean.setLinktel(jsonArray.optJSONObject(i).getString("linktel"));
                                    bean.setComname(jsonArray.optJSONObject(i).getString("comname"));
                                    bean.setInminetime(jsonArray.optJSONObject(i).getString("inminetime"));
                                    bean.setMemo(jsonArray.optJSONObject(i).getString("memo"));
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
                                        bean = (ZkdsryglBean.CellsBean) adapter.getData().get(position);
//                                        Intent intent = new Intent(Lxxzld.this, ZkdsryglDetail.class);
//                                        intent.putExtra("paramname", bean.getParamname());
//                                        intent.putExtra("fusername", bean.getFusername());
//                                        intent.putExtra("townshipname", bean.getTownshipname());
//                                        intent.putExtra("linktel", bean.getLinktel());
//                                        intent.putExtra("comname", bean.getComname());
//                                        intent.putExtra("inminetime", bean.getInminetime());
//                                        intent.putExtra("memo", bean.getMemo());
//                                        if (usertype.equals("ZKGB001")) {
//                                            intent.putExtra("title_name", "联系乡镇领导详情");
//                                        } else if (usertype.equals("ZKGB002")) {
//                                            intent.putExtra("title_name", "驻矿安监员详情");
//                                        } else if (usertype.equals("ZKGB003")) {
//                                            intent.putExtra("title_name", "联系矿责任人详情");
//                                        } else if (usertype.equals("ZKGB004")) {
//                                            intent.putExtra("title_name", "驻矿责任人详情");
//                                        }
                                        Intent intent = new Intent(Lxxzld.this, WorkInfo.class);
                                        intent.putExtra("userid", bean.getUserid());
                                        intent.putExtra("usertype", bean.getUsertype());
                                        intent.putExtra("title_name", title_name.getText().toString());
                                        intent.putExtra("ym", ym);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                ShowToast.showShort(Lxxzld.this, R.string.getInfoErr);
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
                        ShowToast.showShort(Lxxzld.this, R.string.connect_err);
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
            searchDatas = new ArrayList<ZkdsryglBean.CellsBean>();
            for (ZkdsryglBean.CellsBean entity : mDatas) {
                try {
                    if (entity.getFusername().contains(str) || entity.getTownshipname().contains(str) || entity.getLinktel().contains(str)
                            || entity.getComname().contains(str) || entity.getInminetime().contains(str)) {
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
