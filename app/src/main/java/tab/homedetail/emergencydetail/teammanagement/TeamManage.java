package tab.homedetail.emergencydetail.teammanagement;


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

import adpter.TeamManageAdapter;
import bean.TeamManageBean;
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
 * 应急救援队伍管理
 */
public class TeamManage extends BaseFragment {
    private View view;
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<TeamManageBean.CellsBean> searchDatas;
    private List<TeamManageBean.CellsBean> mDatas;
    private TeamManageAdapter adapter;
    private TeamManageBean.CellsBean bean;
    private String token;

    public TeamManage() {
        // Required empty public constructor
    }

    @Override
    public View makeView() {
        view = View.inflate(getActivity(), R.layout.fragment_team_manage, null);
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
            adapter = new TeamManageAdapter(R.layout.yjjy_item4, mDatas);
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
        OkGo.<String>get(PortIpAddress.Rankslist())
                .tag(this)
                .params(TOKEN_KEY, token)
                .params("expertbean.expertid", "")
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
                                    bean = new TeamManageBean.CellsBean();
                                    bean.setRanksid(jsonArray.optJSONObject(i).getString("ranksid"));
                                    bean.setRanksname(jsonArray.optJSONObject(i).getString("ranksname"));
                                    bean.setRankstype(jsonArray.optJSONObject(i).getString("rankstype"));
                                    bean.setAreas(jsonArray.optJSONObject(i).getString("areas"));
                                    bean.setOwnman(jsonArray.optJSONObject(i).getString("ownman"));
                                    bean.setOwntel(jsonArray.optJSONObject(i).getString("owntel"));
                                    bean.setLinkman(jsonArray.optJSONObject(i).getString("linkman"));
                                    bean.setLinktel(jsonArray.optJSONObject(i).getString("linktel"));
                                    bean.setPepsum(jsonArray.optJSONObject(i).getString("pepsum"));
                                    bean.setCompetentdetp(jsonArray.optJSONObject(i).getString("competentdetp"));
                                    bean.setSpecialtydesc(jsonArray.optJSONObject(i).getString("specialtydesc"));
                                    bean.setXcoordinate(jsonArray.optJSONObject(i).getString("xcoordinate"));
                                    bean.setYcoordinate(jsonArray.optJSONObject(i).getString("ycoordinate"));
                                    bean.setAddress(jsonArray.optJSONObject(i).getString("address"));
                                    bean.setDeptid(jsonArray.optJSONObject(i).getString("deptid"));
                                    bean.setParentdeptid(jsonArray.optJSONObject(i).getString("parentdeptid"));
                                    bean.setMemo(jsonArray.optJSONObject(i).getString("memo"));
                                    bean.setEquipmentstatusname(jsonArray.optJSONObject(i).getString("equipmentstatusname"));
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
                                        bean = (TeamManageBean.CellsBean) adapter.getData().get(position);
                                        Intent intent = new Intent(getActivity(), TeamManageDetail.class);
                                        intent.putExtra("ranksid", bean.getRanksid());
                                        intent.putExtra("ranksname", bean.getRanksname());
                                        intent.putExtra("rankstype", bean.getRankstype());
                                        intent.putExtra("areas", bean.getAreas());
                                        intent.putExtra("ownman", bean.getOwnman());
                                        intent.putExtra("owntel", bean.getOwntel());
                                        intent.putExtra("linkman", bean.getLinkman());
                                        intent.putExtra("linktel", bean.getLinktel());
                                        intent.putExtra("pepsum", bean.getPepsum());
                                        intent.putExtra("competentdetp", bean.getCompetentdetp());
                                        intent.putExtra("specialtydesc", bean.getSpecialtydesc());
                                        intent.putExtra("equipmentstatusname", bean.getEquipmentstatusname());
                                        intent.putExtra("address", bean.getAddress());
                                        intent.putExtra("memo", bean.getMemo());
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
            searchDatas = new ArrayList<TeamManageBean.CellsBean>();
            for (TeamManageBean.CellsBean entity : mDatas) {
                try {
                    if (entity.getRanksname().contains(str) || entity.getRankstype().contains(str) || entity.getAreas().contains(str) || entity.getOwnman().contains(str)) {
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
