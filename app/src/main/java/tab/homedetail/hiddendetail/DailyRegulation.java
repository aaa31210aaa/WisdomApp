package tab.homedetail.hiddendetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
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

import adpter.DailyRegulationAdapter;
import bean.DailyRegulationBean;
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
import static utils.PortIpAddress.TOKEN_KEY;

public class DailyRegulation extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_name_right)
    TextView title_name_right;
    //    @BindView(R.id.daily_regulation_indicator)
//    MagicIndicator daily_regulation_indicator;
//    @BindView(R.id.daily_regulation_viewpager)
//    ViewPager daily_regulation_viewpager;
//    private static final String[] CHANNELS = new String[]{"工贸股", "危化股", "煤监股", "非煤股日常"};
//    private List<String> mDataList = Arrays.asList(CHANNELS);
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.add_fbt)
    FloatingActionButton add_fbt;

    private String[] chargedept_arr;
    private Map<String, String> chargedept_map;

    private List<DailyRegulationBean> searchDatas;
    private List<DailyRegulationBean> mDatas;
    private DailyRegulationAdapter adapter;
    private DailyRegulationBean bean;
    private String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_regulation);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.daily_regulation);
        Intent intent = getIntent();
        userid = SharedPrefsUtil.getValue(this, "userInfo", "userid", "");

//        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(new Industry());
//        fragments.add(new Risk());
//        fragments.add(new CoalSupervise());
//        fragments.add(new NonCoal());
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
//        daily_regulation_viewpager.setOffscreenPageLimit(3);
//        daily_regulation_viewpager.setAdapter(adapter);
//        initMagicIndicator();
        //初始化数据集
        chargedept_arr = getResources().getStringArray(R.array.industry);
        chargedept_map = new HashMap<>();
        title_name_right.setText(chargedept_arr[0]);
        //将数据添加至map集合
        chargedept_map.put(chargedept_arr[0], "");
        chargedept_map.put(chargedept_arr[1], "9c6eed6f7ecd482793293d39e60118e8");
        chargedept_map.put(chargedept_arr[2], "2fff1d3f587f40a3821edd0375edc018");
        chargedept_map.put(chargedept_arr[3], "3963ae5ed1164b32a421d28409050c9b");
        chargedept_map.put(chargedept_arr[4], "dc4d58a5e6ec49498167e15ffda2e09b");
        initRv();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }


    @OnClick(R.id.title_name_right)
    void Chargedept() {
        ShowChargedept();
    }

    private void ShowChargedept() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chargedept_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择行业分类").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        title_name_right.setText(chargedept_arr[which]);
                        if (which == 0) {
                            userid = SharedPrefsUtil.getValue(DailyRegulation.this, "userInfo", "userid", "");
                        } else {
                            userid = "";
                        }
                        refreshLayout.autoRefresh();
                    }
                }).create();
        dialog.show();
    }

    @OnClick(R.id.add_fbt)
    void AddDaily() {
        Intent intent = new Intent(this, AddDaily.class);
        intent.putExtra("chargedept", chargedept_map.get(title_name_right.getText().toString()));
        intent.putExtra("riskinfoid", "");
        startActivityForResult(intent, 10);
    }

    private void initRv() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        if (adapter == null) {
            adapter = new DailyRegulationAdapter(R.layout.yjjy_item3, mDatas);
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

//    /**
//     * 设置tablayout
//     */
//    private void initMagicIndicator() {
//        daily_regulation_indicator.setBackgroundColor(Color.parseColor("#455a64"));
//        CommonNavigator commonNavigator = new CommonNavigator(this);
//        commonNavigator.setAdjustMode(true);
//        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
//            @Override
//            public int getCount() {
//                return mDataList == null ? 0 : mDataList.size();
//            }
//
//            @Override
//            public IPagerTitleView getTitleView(Context context, final int index) {
//                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
//                simplePagerTitleView.setText(mDataList.get(index));
//                simplePagerTitleView.setNormalColor(Color.GRAY);
//                simplePagerTitleView.setSelectedColor(Color.WHITE);
//                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        daily_regulation_viewpager.setCurrentItem(index);
//                    }
//                });
//                return simplePagerTitleView;
//            }
//
//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
//                indicator.setColors(Color.parseColor("#40c4ff"));
//                return indicator;
//            }
//        });
//
//        daily_regulation_indicator.setNavigator(commonNavigator);
//        ViewPagerHelper.bind(daily_regulation_indicator, daily_regulation_viewpager);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            refreshLayout.autoRefresh();
        }
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
                    ShowToast.showShort(DailyRegulation.this, R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
                    mConnect(chargedept_map.get(title_name_right.getText().toString()), userid);
                    break;
                default:
                    break;
            }
        }
    };

    private void mConnect(String chargeDept, String userid) {
        OkGo.<String>get(PortIpAddress.CheckRecordGovAction())
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.GetToken(this))
                .params("checkRecordbean.chargeDept", chargeDept)
                .params("checkRecordbean.createid", userid)
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
                                    bean = new DailyRegulationBean();
                                    bean.setCrid(jsonArray.optJSONObject(i).getString("crid"));
                                    bean.setComname(jsonArray.optJSONObject(i).getString("comname"));
                                    bean.setJcr(jsonArray.optJSONObject(i).getString("jcr"));
                                    bean.setJcsj(jsonArray.optJSONObject(i).getString("jcsj"));
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
                                        bean = (DailyRegulationBean) adapter.getData().get(position);
                                        Intent intent = new Intent(DailyRegulation.this, IndustryDetail.class);
                                        intent.putExtra("crid", bean.getCrid());
                                        intent.putExtra("titleName", "日常监管详情");
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                ShowToast.showShort(DailyRegulation.this, R.string.getInfoErr);
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
                        ShowToast.showShort(DailyRegulation.this, R.string.connect_err);
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
            searchDatas = new ArrayList<DailyRegulationBean>();
            for (DailyRegulationBean entity : mDatas) {
                try {
                    if (entity.getComname().contains(str) || entity.getJcr().contains(str) || entity.getJcsj().contains(str)) {
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
