package demo.yqh.wisdomapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;
import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.ToxicBakery.viewpager.transforms.FlipVerticalTransformer;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutTranformer;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import adpter.HomeRvAdapter;
import bean.HomeRvBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import tab.homedetail.DataStatistics;
import tab.homedetail.EmergencyRescue;
import tab.homedetail.Enforcement;
import tab.homedetail.Enterprise;
import tab.homedetail.HiddenManagement;
import tab.homedetail.Laws;
import tab.homedetail.Notice;
import tab.homedetail.RiskMonitoring;
import tab.homedetail.Safety;
import tab.homedetail.emergencydetail.EventManagementEmergency;
import tab.homedetail.hiddendetail.DailyRegulation;
import tab.homedetail.hiddendetail.InMine;
import utils.AppUtils;
import utils.BaseActivity;
import utils.CheckVersion;
import utils.CircularImage;
import utils.DialogUtil;
import utils.LocalImageHolderView;
import utils.MapDialog;
import utils.PermissionUtil;
import utils.PortIpAddress;
import utils.ShowToast;
import utils.StatusBarUtils;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;

public class MainActivity extends BaseActivity {
//    @BindView(R.id.main_viewpager)
//    NoScrollViewPager main_viewpager;
//    @BindView(R.id.main_tablayout)
//    TabLayout main_tablayout;

    @BindView(R.id.drawerlayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.id_nv_menu)
    NavigationView id_nv_menu;
    @BindView(R.id.homebanner)
    ConvenientBanner homeBanner;
    @BindView(R.id.homeRv)
    RecyclerView homeRv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String intentIndex;
    private String tag = "home";
    private String mycode = "home";

    private int[] images = {R.drawable.enterprise, R.drawable.notice, R.drawable.hidden_management, R.drawable.law_enforcement, R.drawable.safety_supervision, R.drawable.laws, R.drawable.risk_monitoring, R.drawable.data_statistics, R.drawable.emergency_rescue, R.drawable.rcjg, R.drawable.zkds, R.drawable.sgkb};
    private int[] images_qy = {R.drawable.notice, R.drawable.hidden_management, R.drawable.law_enforcement, R.drawable.safety_supervision, R.drawable.laws, R.drawable.sgkb};
    //首页模块跳转的activity
    private Class<?>[] ACTIVITY = {Enterprise.class, Notice.class, HiddenManagement.class, Enforcement.class, Safety.class, Laws.class, RiskMonitoring.class, DataStatistics.class, EmergencyRescue.class, DailyRegulation.class, InMine.class, EventManagementEmergency.class};
    private Class<?>[] ACTIVITY_QY = {Notice.class, HiddenManagement.class, Enforcement.class, Safety.class, Laws.class, EventManagementEmergency.class};
    //模块背景颜色
    private int colors[] = {Color.parseColor("#cc66ff"), Color.parseColor("#f57c00"), Color.parseColor("#ff4081"), Color.parseColor("#66cc99"), Color.parseColor("#6699ff"), Color.parseColor("#66cccc"), Color.parseColor("#ff0000"), Color.parseColor("#9966ff"), Color.parseColor("#1aa8d8"), Color.parseColor("#45c8e6"), Color.parseColor("#0db482"), Color.parseColor("#ddda1f")};
    private int colors_qy[] = {Color.parseColor("#f57c00"), Color.parseColor("#ff4081"), Color.parseColor("#66cc99"), Color.parseColor("#6699ff"), Color.parseColor("#66cccc"), Color.parseColor("#ddda1f")};

    private List<HomeRvBean> mDatas;
    private HomeRvAdapter adapter;
    private int changeBannerTime = 3500;

    private String[] tvs = {"企业信息", "通知公告", "隐患监管", "行政执法", "安全检查", "法规政策", "企业地图", "数据统计", "应急救援", "日常监管", "驻矿盯守", "事故快报"};
    private String[] tvs_qy = {"通知公告", "隐患排查治理", "执法记录", "安全自查", "法规政策", "事故快报"};

    //侧滑headview
    private View headView;
    private CircularImage left_view_head_img;
    private TextView left_view_head_tv;
    private String mLong = "";
    private String mLat = "";

    //定位需要的声明
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new MyAMapLocationListener();
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //定位地址
    public String local = "";
    //    private Dialog dialog;
    private boolean first = false;
    //提示最新版本tag
    public static boolean versionTag = false;
    private MapDialog mapDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        //设置tab
//      SetTab();

        AndPermission.with(this)
                .requestCode(200)
                .permission(PermissionUtil.LocationPermission, PermissionUtil.WriteFilePermission)
                .send();
        initLocal();
        StatusBarUtils.initStatusBarColor(this, R.color.main_color);
        initToolbar();
        setHomeBanner();
        setHomeRv();

//        headView = View.inflate(this,R.layout.banner,null);
//        left_view_head_tv = (TextView) headView.findViewById(R.id.left_view_head_tv);

        id_nv_menu.getMenu().getItem(1).setTitle("版本号   " + AppUtils.getVersionName(this));

        initLeftMenu();
    }

    private void initLocal() {
//      dialog = DialogUtil.createLoadingDialog(this, R.string.location_loading);
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(false);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    private class MyAMapLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (first) {
                dialog = DialogUtil.createLoadingDialog(MainActivity.this, R.string.location_loading);
            }

            if (mLong.equals("") || mLat.equals("")) {
                mLong = String.valueOf(aMapLocation.getLongitude()); //经度
                mLat = String.valueOf(aMapLocation.getLatitude());//纬度
            }

            //省信息+城市信息+城区信息+街道信息
            local = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation.getStreetNum();
            id_nv_menu.getMenu().getItem(3).setTitle(local);

            first = true;
        }
    }


    private void updateLocal(){
        OkGo.<String>get(PortIpAddress.LocationAdress())
                .tag(this)
                .params("qyid", PortIpAddress.getUserId(MainActivity.this))
                .params("longitudecoord", mLong)
                .params("latitudecoord", mLat)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
//                                    ShowToast.showShort(MainActivity.this, "位置上传成功");
                                Log.d(TAG, "位置上传成功");
                            } else {
//                                    ShowToast.showShort(MainActivity.this, "位置上传失败");
                                Log.d(TAG, "位置上传失败");
                            }

                            if (dialog != null) {
                                dialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(MainActivity.this, R.string.connect_err);
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }

    //权限回调
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == 200) {
                //检查版本
                CheckVersion checkVersion = new CheckVersion();
                checkVersion.CheckVersions(MainActivity.this, TAG);

                if (!PortIpAddress.getUserType(context)) {
                    //判断企业是否已经有位置信息
                    isHaveAddress();
                }
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == 200) {
                ShowToast.showShort(MainActivity.this, R.string.permission_err);
            }
        }
    };


    /**
     * 获取企业是否有位置信息
     */
    private void isHaveAddress(){
        OkGo.<String>get(PortIpAddress.isHaveLatLng())
                .tag(this)
                .params("qyid", PortIpAddress.getUserId(MainActivity.this))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                if (jsonArray.getJSONObject(0).getString("exist").equals("0")){
                                    //创建提交定位的自定义dialog
                                    mapDialog = new MapDialog(context, R.style.DialogStyle);
                                    mapDialog.setCanceledOnTouchOutside(false);
                                    mapDialog.show();
                                }
                            }else{
                                ShowToast.showShort(MainActivity.this, R.string.getInfoErr);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(MainActivity.this, R.string.connect_err);
                    }
                });
    }



    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    isExit = false;
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 延迟发送退出
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            ShowToast.showShort(this, R.string.click_agin);
            // 利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0, send_time);
        } else {
            finish();
            System.exit(0);
        }
    }

//    private void SetTab() {
//        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(new Home());
//        fragments.add(new Safety());
//        fragments.add(new Hidden());
//        fragments.add(new Enforcement());
    //"安全监管","隐患管理","行政执法"
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"首页"});
//
//        main_viewpager.setOffscreenPageLimit(4);
//
//        main_viewpager.setAdapter(adapter);
//        //关联图文
//        main_tablayout.setupWithViewPager(main_viewpager);
//        main_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                main_viewpager.setCurrentItem(tab.getPosition(), false);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

//        for (int i = 0; i < main_tablayout.getTabCount(); i++) {
//            TabLayout.Tab tab = main_tablayout.getTabAt(i);
//            Drawable d = null;
//            switch (i) {
//                case 0:
//                    d = ContextCompat.getDrawable(this, R.drawable.home_tab);
//                    break;
//                case 1:
//                    d = ContextCompat.getDrawable(this, R.drawable.safety_tab);
//                    break;
//                case 2:
//                    d = ContextCompat.getDrawable(this, R.drawable.hidden_tab);
//                    break;
//                case 3:
//                    d = ContextCompat.getDrawable(this, R.drawable.enforcement_tab);
//                    break;
//
//            }
//            tab.setIcon(d);
//        }
//    }

//    /**
//     * 菜单点击监听
//     */
//    @OnClick(R.id.main_menu)
//    void Menu() {
//    }

    /**
     * 设置toolbar
     */
    private void initToolbar() {
        toolbar.setTitle("");
        //设置toolbar
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                String msg = "";
//                switch (item.getItemId()) {
//                    case R.id.action_edit:
////                        msg += "Click edit";
//                        Intent intent = new Intent(MainActivity.this,QyMap.class);
//                        intent.putExtra("tag","main");
//                        startActivity(intent);
//                        break;
//                }
//
//                if(!msg.equals("")) {
//                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                }
//
//                return true;
//            }
//        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 绑定toobar和menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //重写此方法图标才能正确显示
    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }


    /**
     * 设置轮播
     */
    private void setHomeBanner() {
        //banner加载的图片集
        ArrayList<Integer> localImages = new ArrayList<>();
        //翻页效果集
        ArrayList<String> transformerList = new ArrayList<>();

        for (int position = 1; position < 4; position++) {
            localImages.add(getResId("banner" + position, R.drawable.class));
        }

        //自定义Holder
        homeBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        // 设置翻页的效果，不需要翻页效果可用不设

//                .setPageTransformer(Transformer.CubeIn);
//        convenientBanner.setManualPageable(false);//设置不能手动影响


        //加载网络图片
//        networkImages= Arrays.asList(imageUrls);
//        banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
//            @Override
//            public NetworkImageHolderView createHolder() {
//                return new NetworkImageHolderView();
//            }
//        },networkImages);

        //各种翻页效果
        transformerList.add(DefaultTransformer.class.getSimpleName());
        transformerList.add(AccordionTransformer.class.getSimpleName());
        transformerList.add(BackgroundToForegroundTransformer.class.getSimpleName());
        transformerList.add(CubeInTransformer.class.getSimpleName());
        transformerList.add(CubeOutTransformer.class.getSimpleName());
        transformerList.add(DepthPageTransformer.class.getSimpleName());
        transformerList.add(FlipHorizontalTransformer.class.getSimpleName());
        transformerList.add(FlipVerticalTransformer.class.getSimpleName());
        transformerList.add(ForegroundToBackgroundTransformer.class.getSimpleName());
        transformerList.add(RotateDownTransformer.class.getSimpleName());
        transformerList.add(RotateUpTransformer.class.getSimpleName());
        transformerList.add(StackTransformer.class.getSimpleName());
        transformerList.add(ZoomInTransformer.class.getSimpleName());
        transformerList.add(ZoomOutTranformer.class.getSimpleName());

        String transforemerName = transformerList.get(13);
        try {
            Class cls = Class.forName("com.ToxicBakery.viewpager.transforms." + transforemerName);
            ABaseTransformer transforemer = (ABaseTransformer) cls.newInstance();
            homeBanner.getViewPager().setPageTransformer(true, transforemer);

            if (transforemerName.equals("StackTransformer")) {
                homeBanner.setScrollDuration(changeBannerTime);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置首页模块内容
     */
    private void setHomeRv() {
        if (PortIpAddress.getUserType(this)) {
            homeRv.setLayoutManager(new GridLayoutManager(MainActivity.this, 3, GridLayoutManager.VERTICAL, false));
//            homeRv.addItemDecoration(new DividerItemDecoration(MainActivity.this));
            homeRv.setHasFixedSize(true);
            mDatas = new ArrayList<>();
            for (int i = 0; i < tvs.length; i++) {
                HomeRvBean bean = new HomeRvBean();
                bean.setImage(images[i]);
                bean.setTvName(tvs[i]);
                bean.setMsgNum("0");
                bean.setColor(colors[i]);
                mDatas.add(bean);
            }
        } else {
            homeRv.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false));
//            homeRv.addItemDecoration(new DividerItemDecoration(MainActivity.this));
            homeRv.setHasFixedSize(true);
            mDatas = new ArrayList<>();

            for (int i = 0; i < tvs_qy.length; i++) {
                HomeRvBean bean = new HomeRvBean();
                bean.setImage(images_qy[i]);
                bean.setTvName(tvs_qy[i]);
                bean.setMsgNum("0");
                bean.setColor(colors_qy[i]);
                mDatas.add(bean);
            }
        }

        if (adapter == null) {
            adapter = new HomeRvAdapter(R.layout.home_rvitem, mDatas);
            homeRv.setAdapter(adapter);
        } else {
            adapter.setNewData(mDatas);
        }

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (PortIpAddress.getUserType(MainActivity.this)) {
                    if (position == 0) {
                        intentIndex = "home";
                        Intent intent = new Intent(MainActivity.this, ACTIVITY[0]);
                        intent.putExtra("intentIndex", intentIndex);
                        intent.putExtra("tag", tag);
                        startActivityForResult(intent, 10);
                    } else if (position == 2) {
                        intentIndex = "home";
                        Intent intent = new Intent(MainActivity.this, ACTIVITY[2]);
                        intent.putExtra("intentIndex", intentIndex);
                        intent.putExtra("tag", tag);
                        startActivity(intent);
                    } else if (position == 11) {
                        Intent intent = new Intent(MainActivity.this, ACTIVITY[11]);
                        intent.putExtra("ym", "home_sgkb");
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(MainActivity.this, ACTIVITY[position]));
                    }
                } else {
                    if (position == 1) {
                        intentIndex = "home";
                        Intent intent = new Intent(MainActivity.this, ACTIVITY_QY[1]);
                        intent.putExtra("intentIndex", intentIndex);
                        intent.putExtra("tag", tag);
                        startActivity(intent);
                    } else if (position == 5) {
                        Intent intent = new Intent(MainActivity.this, ACTIVITY_QY[5]);
                        intent.putExtra("ym", "home_sgkb");
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(MainActivity.this, ACTIVITY_QY[position]));
                    }
                }
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                return false;
            }
        });
    }

//    //人员管理
//    @OnClick(R.id.home_rygl)
//    void Rygl() {
//        Intent intent = new Intent(this, RyGl.class);
//        intent.putExtra("mycode", mycode);
//        startActivity(intent);
//    }
//
//    //职业卫生信息
//    @OnClick(R.id.home_zywsxx)
//    void Zywsxx() {
//        Intent intent = new Intent(this, ZyWsXx.class);
//        intent.putExtra("mycode", mycode);
//        startActivity(intent);
//    }
//
//    //应急预案信息
//    @OnClick(R.id.home_yjyaxx)
//    void Yjyaxx() {
//        Intent intent = new Intent(this, YjYaXx.class);
//        intent.putExtra("mycode", mycode);
//        startActivity(intent);
//    }
//
//    //安全教育培训
//    @OnClick(R.id.home_aqjypx)
//    void Aqjypx() {
//        Intent intent = new Intent(this, AqJyPx.class);
//        intent.putExtra("mycode", mycode);
//        startActivity(intent);
//    }
//
//    //特种设备
//    @OnClick(R.id.home_tzsb)
//    void Tzsb() {
//        Intent intent = new Intent(this, TzSb.class);
//        intent.putExtra("mycode", mycode);
//        startActivity(intent);
//    }
//
//    //企业证照
//    @OnClick(R.id.home_qyzz)
//    void Qyzz() {
//        Intent intent = new Intent(this, QyZz.class);
//        intent.putExtra("mycode", mycode);
//        startActivity(intent);
//    }


    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        homeBanner.startTurning(changeBannerTime);
        if (mapDialog !=null){
            mapDialog.dialog_map.onResume();
        }
    }


    // 停止自动翻页
    @Override
    public void onStop() {
        super.onStop();
        //停止翻页
        homeBanner.stopTurning();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapDialog !=null){
            mapDialog.dialog_map.onPause();
        }

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapDialog !=null){
            mapDialog.dialog_map.onSaveInstanceState(outState);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapDialog !=null){
            mapDialog.dialog_map.onDestroy();
        }
    }

    /**
     * 设置侧滑菜单
     */
    private void initLeftMenu() {
        //侧滑菜单item点击监听
        id_nv_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.toString().equals("修改密码")) {
                    drawerLayout.closeDrawer(Gravity.START);
                } else if (item.toString().equals("版本号   " + AppUtils.getVersionName(MainActivity.this))) {
                    versionTag = true;
                    // 检查版本
                    CheckVersion checkVersion = new CheckVersion();
                    checkVersion.CheckVersions(MainActivity.this, TAG);
                } else if (item.toString().equals("注销")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.mine_cancellation_dialog_title);
                    builder.setMessage(R.string.mine_cancellation_dialog_content);
                    builder.setPositiveButton(R.string.mine_cancellation_dialog_btn2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent logoutIntent = new Intent(MainActivity.this, Login.class);
                            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(logoutIntent);
                        }
                    });

                    builder.setNegativeButton(R.string.mine_cancellation_dialog_btn1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mLocationClient.startLocation();
                        }
                    });
                    builder.show();
                } else if (item.toString().equals(local)) {
//                    mLocationClient.startLocation();
                    updateLocal();
                }
                return true;
            }
        });
    }


}
