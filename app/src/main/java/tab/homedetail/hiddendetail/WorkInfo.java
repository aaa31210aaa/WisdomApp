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

import adpter.WorkInfoAdapter;
import bean.WorkInfoBean;
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

public class WorkInfo extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_name_right)
    TextView title_name_right;
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<WorkInfoBean.CellsBean> searchDatas;
    private List<WorkInfoBean.CellsBean> mDatas;
    private WorkInfoAdapter adapter;
    private WorkInfoBean.CellsBean bean;
    private String userid = "";
    private String usertype = "";
    private String sfxz = "";
    private String ym = "";


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
        userid = intent.getStringExtra("userid");
        usertype = intent.getStringExtra("usertype");
        title_name.setText(intent.getStringExtra("title_name") + "工作信息");
        title_name_right.setBackgroundResource(R.drawable.add);
        ym = intent.getStringExtra("ym");

        if (ym.equals("Lxxzldgzxx") || ym.equals("Lxkzrrgzxx") || ym.equals("Zkzrrgzxx") || ym.equals("Ajygzxx")) {
            if (sfxz.equals("1")) {
                title_name_right.setVisibility(View.VISIBLE);
            }
        } else {
            title_name_right.setVisibility(View.GONE);
        }
        initRv();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    @OnClick(R.id.title_name_right)
    void AddWorkInfo() {
        Intent intent = new Intent(WorkInfo.this, AddWorkInfo.class);
        intent.putExtra("usertype", usertype);
        intent.putExtra("jcr", SharedPrefsUtil.getValue(this, "userInfo", "username", ""));
        intent.putExtra("title_name", title_name.getText().toString());
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mConnect(usertype, userid);
        }
    }

    private void initRv() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        if (adapter == null) {
            adapter = new WorkInfoAdapter(R.layout.yjjy_item4, mDatas);
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
                    ShowToast.showShort(WorkInfo.this, R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
                    mConnect(usertype, userid);
                    break;
                default:
                    break;
            }
        }
    };

    private void mConnect(String usertype, String userid) {
        OkGo.<String>get(PortIpAddress.WorkInfolist())
                .tag(this)
                .params("inminecadrescheckbean.usertype", usertype)
                .params("inminecadrescheckbean.userid", userid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e(TAG, response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            sfxz = jsonObject.getString("sfxz");
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new WorkInfoBean.CellsBean();
                                    bean.setCrid(jsonArray.optJSONObject(i).getString("crid"));
                                    bean.setUserid(jsonArray.optJSONObject(i).getString("userid"));
                                    bean.setUsertype(jsonArray.optJSONObject(i).getString("usertype"));
                                    bean.setJcr(jsonArray.optJSONObject(i).getString("jcr"));
                                    bean.setJcsj(jsonArray.optJSONObject(i).getString("jcsj"));
                                    bean.setJcfxwt(jsonArray.optJSONObject(i).getString("jcfxwt"));
                                    bean.setWtcljy(jsonArray.optJSONObject(i).getString("wtcljy"));
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
                                        bean = (WorkInfoBean.CellsBean) adapter.getData().get(position);
                                        Intent intent = new Intent(WorkInfo.this, WorkInfoDetail.class);
                                        intent.putExtra("crid", bean.getCrid());
                                        intent.putExtra("usertype", bean.getUsertype());
                                        startActivity(intent);
                                    }
                                });


                            } else {
                                ShowToast.showShort(WorkInfo.this, R.string.getInfoErr);
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
                        ShowToast.showShort(WorkInfo.this, R.string.connect_err);
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
            searchDatas = new ArrayList<WorkInfoBean.CellsBean>();
            for (WorkInfoBean.CellsBean entity : mDatas) {
                try {
                    if (entity.getJcr().contains(str) || entity.getJcsj().contains(str) || entity.getJcfxwt().contains(str) || entity.getWtcljy().contains(str)
                            ) {
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
