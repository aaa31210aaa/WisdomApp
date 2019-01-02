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

import adpter.SearchQyAdapter;
import bean.SearchQyBean;
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

public class SearchQy extends BaseActivity {
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
    private List<SearchQyBean> mDatas = new ArrayList<>();
    private List<SearchQyBean> searchDatas = new ArrayList<>();


    private SearchQyAdapter adapter;
    private SearchQyBean bean;

    public static int RESULT_SEARCHQY = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_list_layout);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.search_qy);

        initRv();
        MonitorEditext();
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
            adapter = new SearchQyAdapter(R.layout.searchqy_item, mDatas);
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
                    ShowToast.showShort(SearchQy.this, R.string.loadSuccess);
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


//    @OnClick(R.id.search_qy_submit)
//    void Submit() {
//        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
//        mOkhttp(search_edittext.getText().toString());
//    }

//    private void mOkhttp(String qyname) {
//        OkHttpUtils
//                .get()
//                .url(PortIpAddress.GetSelectQyname())
//                .addParams("access_token", PortIpAddress.GetToken(this))
//                .addParams("qyname", qyname)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        e.printStackTrace();
//                        dialog.dismiss();
//                        ShowToast.showToastNoWait(SearchQy.this, R.string.network_error);
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        Log.e(TAG, response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
//                            String success = jsonObject.getString("success");
//                            String error = jsonObject.getString("errormessage");
//                            mDatas.clear();
//                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    bean = new SearchQyBean();
//                                    bean.setQyname(jsonArray.optJSONObject(i).getString("qyname"));
//                                    bean.setQyid(jsonArray.optJSONObject(i).getString("qyid"));
//                                    mDatas.add(bean);
//                                }
//                                if (adapter == null) {
//                                    adapter = new SearchQyAdapter(R.layout.searchqy_item, mDatas);
//                                    recyclerView.setAdapter(adapter);
//                                } else {
//                                    adapter.setNewData(mDatas);
//                                }
//
//                                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                                        SearchQyBean bean = (SearchQyBean) adapter.getData().get(position);
//                                        Intent intent = new Intent();
//                                        intent.putExtra("qyname", bean.getQyname());
//                                        intent.putExtra("qyid", bean.getQyid());
//                                        setResult(RESULT_SEARCHQY, intent);
//                                        finish();
//                                    }
//                                });
//
//                            } else {
//                                ShowToast.showShort(SearchQy.this, error);
//                            }
//                            dialog.dismiss();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }

    private void mConnect() {
        OkGo.<String>get(PortIpAddress.XzzfQy())
                .tag(this)
                .params("username", SharedPrefsUtil.getValue(SearchQy.this, "userInfo", "userid", ""))
                .execute(new com.lzy.okgo.callback.StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new SearchQyBean();
                                    bean.setQyid(jsonArray.optJSONObject(i).getString("code"));
                                    bean.setQyname(jsonArray.optJSONObject(i).getString("name"));
                                    mDatas.add(bean);
                                }

                                adapter.setNewData(mDatas);
                                //如果无数据设置空布局
                                if (mDatas.size() == 0) {
                                    adapter.setEmptyView(R.layout.nodata_layout, (ViewGroup) recyclerView.getParent());
                                }

                                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        bean = (SearchQyBean) adapter.getData().get(position);
                                        Intent intent = new Intent();
                                        intent.putExtra("qyname", bean.getQyname());
                                        intent.putExtra("qyid", bean.getQyid());
                                        setResult(RESULT_SEARCHQY, intent);
                                        finish();
                                    }
                                });

                            } else {
                                ShowToast.showShort(SearchQy.this, R.string.getInfoErr);
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
                        ShowToast.showShort(SearchQy.this, R.string.connect_err);
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
            searchDatas = new ArrayList<SearchQyBean>();
            for (SearchQyBean entity : mDatas) {
                try {
                    if (entity.getQyname().contains(str)) {
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
