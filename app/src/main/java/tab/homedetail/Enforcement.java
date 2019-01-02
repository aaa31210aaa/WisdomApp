package tab.homedetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import adpter.JobGridViewAdapter;
import adpter.JobListAdapter;
import bean.XcJcBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import tab.homedetail.enforcementdetail.AddEnforcement;
import tab.homedetail.enforcementdetail.TypeInfoDetail;
import tab.homedetail.enforcementdetail.TypeInfoList;
import utils.BaseActivity;
import utils.CameraUtil;
import utils.DividerItemDecoration;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;
import static utils.PortIpAddress.TOKEN_KEY;

/**
 * 行政执法
 */
public class Enforcement extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_name_right)
    ImageView title_name_right;
    @BindView(R.id.job_gridview)
    GridView job_gridview;
//    @BindView(R.id.enforcement_radiobtn_all)
//    RadioButton enforcement_radiobtn_all;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.enforcement_sp)
    TextView enforcement_sp;
    @BindView(R.id.enforcement_hsv)
    HorizontalScrollView enforcement_hsv;

    //    @BindView(R.id.job_rv)
//    RecyclerView job_rv;
    private List<XcJcBean> mDatas;
    private List<XcJcBean> mDatas_type;

    private JobListAdapter adapter;
    private JobGridViewAdapter adapter_type;
    private int NUM = 4; // 每行显示个数
    private int LIEWIDTH;//每列宽度
    private int LIE;//列数
    private List<String> tvLists;
    private List<String> industrytype;
    private ArrayAdapter sp_adapter;
    private Map<String, String> enforcement_sp_map;
    private String bjcdw = "";
    private String bjcdwKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enforcement);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        if (PortIpAddress.getUserType(this)) {
            title_name.setText(R.string.enforcement_title);
        }else{
            title_name.setText(R.string.enforcement_title_qy);
        }
        title_name_right.setBackgroundResource(R.drawable.add);
        CameraUtil.init(this);
//      enforcement_radiobtn_all.setChecked(true);

        if (PortIpAddress.getUserType(this)) {
            enforcement_hsv.setVisibility(View.VISIBLE);
            initGridView();
        } else {
            title_name_right.setVisibility(View.GONE);
            enforcement_hsv.setVisibility(View.GONE);
            bjcdw = SharedPrefsUtil.getValue(this, "userInfo", "userid", "");
            bjcdwKey = "bean.bjcdw";
        }
        initSpinner();
        initRv();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }
//    /**
//     * 待处理
//     */
//    @OnClick(R.id.enforcement_radiobtn_dcl)
//    void Dcl() {
//
//    }
//
//    /**
//     * 待复查
//     */
//    @OnClick(R.id.enforcement_radiobtn_dfc)
//    void Dfc() {
//
//    }
//
//    /**
//     * 处理完毕
//     */
//    @OnClick(R.id.enforcement_radiobtn_clwb)
//    void Clwb() {
//
//    }


    @OnClick(R.id.title_name_right)
    void AddEnforcement() {
        Intent intent = new Intent(this, AddEnforcement.class);
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mConnect(enforcement_sp_map.get(enforcement_sp.getText().toString()));
        }
    }

    private void initGridView() {
        tvLists = new ArrayList<String>();
        industrytype = new ArrayList<String>();

        OkGo.<String>get(PortIpAddress.XzzfHy())
                .tag(this)
                .params("username", SharedPrefsUtil.getValue(Enforcement.this, "userInfo", "userid", ""))
                .params(TOKEN_KEY, PortIpAddress.GetToken(this))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    tvLists.add(jsonArray.optJSONObject(i).getString("name"));
                                    industrytype.add(jsonArray.optJSONObject(i).getString("code"));
                                }
                                setValue(tvLists);
                            } else {
                                ShowToast.showShort(Enforcement.this, R.string.getInfoErr);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(Enforcement.this, R.string.connect_err);
                    }
                });


        job_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Enforcement.this, TypeInfoList.class);
                intent.putExtra("typename", tvLists.get(position));
                intent.putExtra("typeid", industrytype.get(position));
                startActivity(intent);
            }
        });
    }

    //自定义gridview
    private void setValue(List<String> lists) {
        LIEWIDTH = CameraUtil.screenWidth / NUM;
        JobGridViewAdapter adapter = new JobGridViewAdapter(this, lists);
        job_gridview.setAdapter(adapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(adapter.getCount() * LIEWIDTH,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        job_gridview.setLayoutParams(params);
        job_gridview.setGravity(Gravity.CENTER);
        job_gridview.setColumnWidth(LIEWIDTH);
        job_gridview.setStretchMode(GridView.NO_STRETCH);
        int count = adapter.getCount();
        job_gridview.setNumColumns(count);
    }

    private void initSpinner() {
        //初始化数据集
        enforcement_sp_map = new HashMap<>();
        String[] type_arr = getResources().getStringArray(R.array.enforcement_sp);
        enforcement_sp.setText(type_arr[0]);
        enforcement_sp_map.put(type_arr[0], "");
        enforcement_sp_map.put(type_arr[1], "XZZFZT001");
        enforcement_sp_map.put(type_arr[2], "XZZFZT002");
        enforcement_sp_map.put(type_arr[3], "XZZFZT003");
        setPopWindowFull(enforcement_sp, type_arr, refreshLayout);
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
                    ShowToast.showShort(Enforcement.this, R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
                    mConnect(enforcement_sp_map.get(enforcement_sp.getText().toString()));
                    break;
                default:
                    break;
            }
        }
    };


    private void mConnect(String handlingstatus) {
        OkGo.<String>get(PortIpAddress.Xzzf())
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.GetToken(this))
                .params("bean.handlingstatus", handlingstatus)
                .params(bjcdwKey, bjcdw)
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
                                    XcJcBean bean = new XcJcBean();
//                                    bean.setImage(R.drawable.noimg);
                                    bean.setCrid(jsonArray.optJSONObject(i).getString("crid"));
                                    bean.setBjcdw(jsonArray.optJSONObject(i).getString("bjcdwname"));
                                    bean.setHandlingstatusname(jsonArray.optJSONObject(i).getString("handlingstatusname"));
                                    bean.setStarttime(jsonArray.optJSONObject(i).getString("starttime"));
                                    bean.setEndtime(jsonArray.optJSONObject(i).getString("endtime"));
                                    bean.setRemark(jsonArray.optJSONObject(i).getString("xzcfflname"));
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new JobListAdapter(R.layout.job_list_item, mDatas);
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
                                        XcJcBean bean = (XcJcBean) adapter.getData().get(position);
                                        Intent intent = new Intent(Enforcement.this, TypeInfoDetail.class);
                                        intent.putExtra("crid", bean.getCrid());
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                ShowToast.showShort(Enforcement.this, R.string.getInfoErr);
                            }
                            refreshLayout.finishRefresh();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh();
                        ShowToast.showShort(Enforcement.this, R.string.connect_err);
                    }
                });
    }


}
