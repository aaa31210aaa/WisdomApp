package tab.homedetail.safetydetail;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.List;

import adpter.YbzlistWbzlistAdapter;
import bean.YbzlistWbzlistBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import tab.homedetail.safetydetail.safetycontent.NrDetailList;
import utils.BaseActivity;
import utils.DividerItemDecoration;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;
import static utils.PortIpAddress.TOKEN_KEY;

/**
 * 已制定未制定的企业列表
 */
public class YbzlistWbzlist extends BaseActivity {
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
    private List<YbzlistWbzlistBean.CellsBean> searchDatas;
    private List<YbzlistWbzlistBean.CellsBean> mDatas;
    private YbzlistWbzlistAdapter adapter;
    private YbzlistWbzlistBean.CellsBean bean;
    private String url = "";
    private String[] type_arr;
    private String qyidKey = "";
    private String qyid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_list_layout);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.ybzlistwbzlist_title);

        if (!PortIpAddress.getUserType(this)) {
            qyid = SharedPrefsUtil.getValue(this, "userInfo", "userid", "");
        }

        //初始化数据集
        type_arr = getResources().getStringArray(R.array.zd_type);
        title_name_right.setText(type_arr[0]);
        initRv();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    @OnClick(R.id.title_name_right)
    void Type() {
        showListDialog();
    }

    private void showListDialog() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, type_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择类型").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        title_name_right.setText(type_arr[which]);
                        refreshLayout.autoRefresh();
                    }
                }).create();
        dialog.show();
    }

    private void initRv() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        String type = title_name_right.getText().toString();

        if (adapter == null) {
            adapter = new YbzlistWbzlistAdapter(R.layout.yjjy_item3, mDatas, type);
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
                title_name_right.setEnabled(false);
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
                    ShowToast.showShort(YbzlistWbzlist.this, R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
                    mConnect();
                    title_name_right.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };


    private void mConnect() {
        if (title_name_right.getText().equals("已制定")) {
            url = PortIpAddress.Ybzlist();
            qyidKey = "dardbean.qyid";
        } else {
            url = PortIpAddress.Wbzlist();
            qyidKey = "comregbean.qyid";
        }
        OkGo.<String>get(url)
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.GetToken(this))
                .params(qyidKey, qyid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e(TAG, response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            String type = "";
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                mDatas.clear();
                                adapter = null;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new YbzlistWbzlistBean.CellsBean();
                                    if (title_name_right.getText().toString().equals("已制定")) {
                                        bean.setZcbzid(jsonArray.optJSONObject(i).getString("zcbzid"));
                                        bean.setQyname(jsonArray.optJSONObject(i).getString("qyname"));
                                        bean.setHylyname(jsonArray.optJSONObject(i).getString("hylyname"));
                                        bean.setHybzname(jsonArray.optJSONObject(i).getString("hybzname"));
                                        type = "已制定";
                                    } else {
                                        bean.setComname(jsonArray.optJSONObject(i).getString("comname"));
                                        bean.setMainfield(jsonArray.optJSONObject(i).getString("mainfield"));
                                        bean.setSetupdate(jsonArray.optJSONObject(i).getString("setupdate"));
                                        type = "未制定";
                                    }
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new YbzlistWbzlistAdapter(R.layout.yjjy_item3, mDatas, type);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    adapter.setNewData(mDatas);
                                }

                                //如果无数据设置空布局
                                if (mDatas.size() == 0) {
                                    adapter.setEmptyView(R.layout.nodata_layout, (ViewGroup) recyclerView.getParent());
                                }

                                //子项点击事件
                                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        if (title_name_right.getText().equals("已制定")) {
                                            bean = (YbzlistWbzlistBean.CellsBean) adapter.getData().get(position);
                                            Intent intent = new Intent(YbzlistWbzlist.this, NrDetailList.class);
                                            intent.putExtra("zcbzid", bean.getZcbzid());
                                            startActivity(intent);
                                        }
                                    }
                                });
                            } else {
                                ShowToast.showShort(YbzlistWbzlist.this, R.string.getInfoErr);
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
                        ShowToast.showShort(YbzlistWbzlist.this, R.string.connect_err);
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
                        title_name_right.setEnabled(false);
                        search(search_edittext.getText().toString().trim());
                    } else {
                        refreshLayout.setEnableRefresh(true);
                        search_clear.setVisibility(View.GONE);
                        title_name_right.setEnabled(true);
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
            searchDatas = new ArrayList<YbzlistWbzlistBean.CellsBean>();
            for (YbzlistWbzlistBean.CellsBean entity : mDatas) {
                try {
                    if (entity.getQyname().contains(str) || entity.getHylyname().contains(str) || entity.getHybzname().contains(str)) {
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
