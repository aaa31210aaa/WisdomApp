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
import tab.homedetail.emergencydetail.shareinfo.MedicalRescue;
import tab.homedetail.emergencydetail.shareinfo.PoliceStation;
import tab.homedetail.emergencydetail.shareinfo.TownshipTellNum;
import utils.BaseActivity;
import utils.ColorFlipPagerTitleView;

public class MaterialSharingEmergency extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.team_management_indicator)
    MagicIndicator team_management_indicator;
    @BindView(R.id.team_management_viewpager)
    ViewPager team_management_viewpager;
    private static final String[] CHANNELS = new String[]{"乡镇联系信息", "乡镇派出所", "医疗救护机构"};
    private List<String> mDataList = Arrays.asList(CHANNELS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_sharing_emergency);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.material_sharing_emergency);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TownshipTellNum());
        fragments.add(new PoliceStation());
        fragments.add(new MedicalRescue());
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
        });

        team_management_indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(team_management_indicator, team_management_viewpager);
    }


}
