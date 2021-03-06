package tab.homedetail.safetydetail.safetyplan;


import android.content.Intent;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
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

import adpter.PendingAdapter;
import bean.PendingBean;
import bean.PendingLv1;
import bean.PendingLv2;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseFragment;
import utils.DividerItemDecoration;
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;
import static utils.PortIpAddress.TOKEN_KEY;

/**
 * 逾期执行
 */
public class Overdue extends BaseFragment {
    private View view;
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<PendingBean.CellsBean> mDatas;
    private List<PendingBean.CellsBean> searchDatas;
    private PendingAdapter adapter;
    private PendingBean.CellsBean bean;
    private String token;

    public Overdue() {
        // Required empty public constructor
    }

    @Override
    public View makeView() {
        view = View.inflate(getActivity(), R.layout.fragment_overdue, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void loadData() {
        token = PortIpAddress.GetToken(getActivity());
        initRv();
    }


    private void initRv() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        if (adapter == null) {
            adapter = new PendingAdapter(R.layout.yjjy_item3, mDatas);
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

    private ArrayList<MultiItemEntity> generateData() {
        int lv0Count = 20;
        int lv1Count = 1;

        ArrayList<MultiItemEntity> res = new ArrayList<>();

        for (int i = 0; i < lv0Count; i++) {
            PendingLv1 bean = new PendingLv1();
            bean.setTitle("标题" + i);
            bean.setSubtitle("副标题" + i);
            for (int j = 0; j < lv1Count; j++) {
                PendingLv2 bean2 = new PendingLv2();
                bean2.setTitle1("item1标题" + i);
                bean2.setTitle2("item2标题" + i);
                bean2.setTitle3("item3标题" + i);
                bean.addSubItem(bean2);
            }
            res.add(bean);
        }
        return res;
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
                    ShowToast.showShort(getActivity(), R.string.loadSuccess);
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

    private void mConnect() {
        OkGo.<String>get(PortIpAddress.ScCheckplan())
                .tag(this)
                .params(TOKEN_KEY, token)
                .params("bean.planstatus", "JHZT002")
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
                                    bean = new PendingBean.CellsBean();
                                    bean.setCpid(jsonArray.optJSONObject(i).getString("cpid"));
                                    bean.setCpname(jsonArray.optJSONObject(i).getString("cpname"));
                                    bean.setChecktypename(jsonArray.optJSONObject(i).getString("checktypename"));
                                    bean.setCheckman(jsonArray.optJSONObject(i).getString("checkman"));
                                    bean.setCheckdeptname(jsonArray.optJSONObject(i).getString("checkdeptname"));
                                    bean.setCheckstatar(jsonArray.optJSONObject(i).getString("checkstatar"));
                                    bean.setCheckend(jsonArray.optJSONObject(i).getString("checkend"));
                                    bean.setCheckcontent(jsonArray.optJSONObject(i).getString("checkcontent"));
                                    bean.setPlanfromname(jsonArray.optJSONObject(i).getString("planfromname"));
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
                                        bean = (PendingBean.CellsBean) adapter.getData().get(position);
                                        Intent intent = new Intent(getActivity(), PendingDetail.class);
                                        intent.putExtra("titlename", "逾期执行计划详情");
                                        intent.putExtra("cpid", bean.getCpid());
                                        intent.putExtra("cpname", bean.getCpname());
                                        intent.putExtra("checktypename", bean.getChecktypename());
                                        intent.putExtra("checkman", bean.getCheckman());
                                        intent.putExtra("checkdeptname", bean.getCheckdeptname());
                                        intent.putExtra("checkstatar", bean.getCheckstatar());
                                        intent.putExtra("checkend", bean.getCheckend());
                                        intent.putExtra("checkcontent", bean.getCheckcontent());
                                        intent.putExtra("planfromname", bean.getPlanfromname());
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                ShowToast.showShort(getActivity(), R.string.getInfoErr);
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
                        ShowToast.showShort(getActivity(), R.string.connect_err);
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
            searchDatas = new ArrayList<PendingBean.CellsBean>();
            for (PendingBean.CellsBean entity : mDatas) {
                try {
                    if (entity.getCpname().contains(str) || entity.getChecktypename().contains(str) || entity.getCheckstatar().contains(str) || entity.getCheckend().contains(str)) {
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
