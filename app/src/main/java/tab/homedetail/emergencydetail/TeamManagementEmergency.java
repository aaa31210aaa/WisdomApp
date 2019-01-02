package tab.homedetail.emergencydetail;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adpter.ViewPagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import tab.homedetail.emergencydetail.teammanagement.ExpertManage;
import tab.homedetail.emergencydetail.teammanagement.TeamManage;
import tab.homedetail.emergencydetail.teammanagement.WxTeamManage;
import utils.BaseActivity;
import utils.ColorFlipPagerTitleView;


public class TeamManagementEmergency extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.team_management_indicator)
    MagicIndicator team_management_indicator;
    @BindView(R.id.team_management_viewpager)
    ViewPager team_management_viewpager;
    private static final String[] CHANNELS = new String[]{"应急救援队伍", "应急救援专家", "外协救援队伍"};
    private List<String> mDataList = Arrays.asList(CHANNELS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_management_emergency);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.team_management_emergency_title);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TeamManage());
        fragments.add(new ExpertManage());
        fragments.add(new WxTeamManage());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        team_management_viewpager.setOffscreenPageLimit(2);
        team_management_viewpager.setAdapter(adapter);
        initMagicIndicator();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    /**
     * 设置tablayout
     */
    private void initMagicIndicator() {
        team_management_indicator.setBackgroundColor(Color.parseColor("#455a64"));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        team_management_viewpager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(Color.parseColor("#40c4ff"));
                return indicator;
            }


            /**
             * 可以改变某个tab的长度
             * @param context
             * @param index
             * @return
             */
//            @Override
//            public float getTitleWeight(Context context, int index) {
//                if (index == 0) {
//                    return 2.0f;
//                } else if (index == 1) {
//                    return 1.2f;
//                } else {
//                    return 1.0f;
//                }
//            }
        });

        team_management_indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(team_management_indicator, team_management_viewpager);
    }

}
