package tab;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
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
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import adpter.HomeRvAdapter;
import bean.HomeRvBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import demo.yqh.wisdomapp.Login;
import demo.yqh.wisdomapp.R;
import tab.homedetail.DataStatistics;
import tab.homedetail.EmergencyRescue;
import tab.homedetail.Enforcement;
import tab.homedetail.Enterprise;
import tab.homedetail.HiddenManagement;
import tab.homedetail.Laws;
import tab.homedetail.Notice;
import tab.homedetail.RiskMonitoring;
import tab.homedetail.Safety;
import utils.BaseFragment;
import utils.CircularImage;
import utils.DividerItemDecoration;
import utils.LocalImageHolderView;

/**
 * 首页
 */
public class Home extends BaseFragment {
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.id_nv_menu)
    NavigationView id_nv_menu;
    @BindView(R.id.homebanner)
    ConvenientBanner homeBanner;
    @BindView(R.id.homeRv)
    RecyclerView homeRv;
    @BindView(R.id.main_toolbar)
    Toolbar main_toolbar;

    private int[] images = {R.drawable.enterprise, R.drawable.notice, R.drawable.hidden_management, R.drawable.law_enforcement, R.drawable.safety_supervision, R.drawable.laws, R.drawable.risk_monitoring, R.drawable.data_statistics, R.drawable.emergency_rescue};
    //首页模块跳转的activity
    private Class<?>[] ACTIVITY = {Enterprise.class, Notice.class, HiddenManagement.class, Enforcement.class, Safety.class, Laws.class, RiskMonitoring.class, DataStatistics.class, EmergencyRescue.class};
    private Class<?>[] ACTIVITY_QY = {};
    //模块背景颜色
    private int colors[] = {Color.parseColor("#cc66ff"), Color.parseColor("#f57c00"), Color.parseColor("#ff4081"), Color.parseColor("#66cc99"), Color.parseColor("#6699ff"), Color.parseColor("#66cccc"), Color.parseColor("#ff0000"), Color.parseColor("#9966ff"), Color.parseColor("#1aa8d8")};

    private List<HomeRvBean> mDatas;
    private HomeRvAdapter adapter;
    private int changeBannerTime = 3500;

    private String[] tvs = {"企业信息", "通知公告", "隐患管理", "行政执法", "安全监管", "法律法规", "风险监控", "数据统计", "应急救援"};
    private String[] tvs_qy = {};

    //侧滑headview
    private View headView;
    private CircularImage left_view_head_img;
    private TextView left_view_head_tv;


    public Home() {
        // Required empty public constructor
    }

    @Override
    public View makeView() {
        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        //绑定fragment
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void loadData() {
        initToolbar();
        setHomeBanner();
        setHomeRv();
        initLeftMenu();
    }

    /**
     * 设置toolbar
     */
    private void initToolbar() {
        main_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
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
        homeRv.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false));
        homeRv.addItemDecoration(new DividerItemDecoration(getActivity()));
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

        if (adapter == null) {
            adapter = new HomeRvAdapter(R.layout.home_rvitem, mDatas);
            homeRv.setAdapter(adapter);
        } else {
            adapter.setNewData(mDatas);
        }

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivityForResult(new Intent(getActivity(), ACTIVITY[position]), 10);
            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                return false;
            }
        });
    }


    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        homeBanner.startTurning(changeBannerTime);
    }

    // 停止自动翻页
    @Override
    public void onStop() {
        super.onStop();
        //停止翻页
        homeBanner.stopTurning();
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
                } else if (item.toString().equals("版本检查")) {
                    drawerLayout.closeDrawer(Gravity.START);
                } else if (item.toString().equals("关于")) {
                    drawerLayout.closeDrawer(Gravity.START);
                } else if (item.toString().equals("注销")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.mine_cancellation_dialog_title);
                    builder.setMessage(R.string.mine_cancellation_dialog_content);
                    builder.setPositiveButton(R.string.mine_cancellation_dialog_btn2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent logoutIntent = new Intent(getActivity(), Login.class);
                            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(logoutIntent);
                        }
                    });

                    builder.setNegativeButton(R.string.mine_cancellation_dialog_btn1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
                return true;
            }
        });
    }

}
