package tab.homedetail.safetydetail.safetycontent;

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

import adpter.NrDetailListAdapter;
import bean.NrDetailListBean;
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

public class NrDetailList extends BaseActivity {
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

    private List<NrDetailListBean.CellsBean> searchDatas;
    private List<NrDetailListBean.CellsBean> mDatas;
    private NrDetailListAdapter adapter;
    private NrDetailListBean.CellsBean bean;
    private String zcbzid = "";
    private String zcbzidKey = "";
    //周期
    private String[] cycle_arr;
    private Map<String, String> cycleMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_list_layout);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.nrdetail_list_title);
        Intent intent = getIntent();
        zcbzid = intent.getStringExtra("zcbzid");
        zcbzidKey = "bean.zcbzid";
        initZq();
        initRv();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    /**
     * 初始化检查周期
     */
    private void initZq() {
        cycle_arr = getResources().getStringArray(R.array.safety_content_cycle);
        title_name_right.setText(cycle_arr[0]);
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
        if (adapter == null) {
            adapter = new NrDetailListAdapter(R.layout.yjjy_item3, mDatas);
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

    @OnClick(R.id.title_name_right)
    void Pczq() {
        PczqDialog();
    }

    /**
     * 周期分类dialog
     */
    private void PczqDialog() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cycle_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择周期").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        title_name_right.setText(cycle_arr[which]);
                        refreshLayout.autoRefresh();
                    }
                }).create();
        dialog.show();
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
                    ShowToast.showShort(NrDetailList.this, R.string.loadSuccess);
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
        OkGo.<String>get(PortIpAddress.NrDetailList())
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.GetToken(this))
                .params(zcbzidKey, zcbzid)
                .params("bean.pczq", cycleMap.get(title_name_right.getText().toString()))
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
                                    bean = new NrDetailListBean.CellsBean();
                                    bean.setZcbzdid(jsonArray.optJSONObject(i).getString("zcbzdid"));
                                    bean.setBzflid(jsonArray.optJSONObject(i).getString("bzflid"));
                                    bean.setBzflname(jsonArray.optJSONObject(i).getString("bzflname"));
//                                    bean.setHybz1(jsonArray.optJSONObject(i).getString("hybz1"));
                                    bean.setHybz2(jsonArray.optJSONObject(i).getString("hybz2"));
                                    bean.setHybz3(jsonArray.optJSONObject(i).getString("hybz3"));
                                    bean.setHybz4(jsonArray.optJSONObject(i).getString("hybz4"));
                                    bean.setHybz5(jsonArray.optJSONObject(i).getString("hybz5"));
                                    bean.setBzorder(jsonArray.optJSONObject(i).getString("bzorder"));
                                    bean.setRscdesc(jsonArray.optJSONObject(i).getString("rscdesc"));
                                    bean.setRsckyj(jsonArray.optJSONObject(i).getString("rsckyj"));
                                    bean.setPczq(jsonArray.optJSONObject(i).getString("pczq"));
                                    bean.setPczqval(jsonArray.optJSONObject(i).getString("pczqval"));
                                    bean.setSfbc(jsonArray.optJSONObject(i).getString("sfbc"));
                                    bean.setPcdeptname(jsonArray.optJSONObject(i).getString("pcdeptname"));
                                    bean.setWgpcd(jsonArray.optJSONObject(i).getString("wgpcd"));
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
                                        bean = (NrDetailListBean.CellsBean) adapter.getData().get(position);
                                        Intent intent = new Intent(NrDetailList.this, NrDetailListDetail.class);
                                        intent.putExtra("zcbzdid", bean.getZcbzid());
                                        intent.putExtra("bzflname", bean.getBzflname());
//                                        intent.putExtra("hybz1", bean.getHybz1());
                                        intent.putExtra("hybz2", bean.getHybz2());
                                        intent.putExtra("hybz3", bean.getHybz3());
                                        intent.putExtra("hybz4", bean.getHybz4());
                                        intent.putExtra("hybz5", bean.getHybz5());
                                        intent.putExtra("bzorder", bean.getBzorder());
                                        intent.putExtra("rscdesc", bean.getRscdesc());
                                        intent.putExtra("rsckyj", bean.getRsckyj());
                                        intent.putExtra("pczq", bean.getPczq());
                                        intent.putExtra("pczqval", bean.getPczqval());
                                        intent.putExtra("sfbc", bean.getSfbc());
                                        intent.putExtra("pcdeptname", bean.getPcdeptname());
                                        intent.putExtra("wgpcd", bean.getWgpcd());
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                ShowToast.showShort(NrDetailList.this, R.string.getInfoErr);
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
                        ShowToast.showShort(NrDetailList.this, R.string.connect_err);
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
            searchDatas = new ArrayList<NrDetailListBean.CellsBean>();
            for (NrDetailListBean.CellsBean entity : mDatas) {
                try {
                    if (entity.getRscdesc().contains(str) || entity.getRsckyj().contains(str) || entity.getPczq().contains(str)) {
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
