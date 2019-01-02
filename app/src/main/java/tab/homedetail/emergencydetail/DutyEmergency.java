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
import tab.homedetail.emergencydetail.duty.DutyPlan;
import tab.homedetail.emergencydetail.duty.DutyRecord;
import tab.homedetail.emergencydetail.duty.DutySystem;
import utils.BaseActivity;
import utils.ColorFlipPagerTitleView;

public class DutyEmergency extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.duty_emergency_indicator)
    MagicIndicator duty_emergency_indicator;
    @BindView(R.id.duty_emergency_viewpager)
    ViewPager duty_emergency_viewpager;
    private static final String[] CHANNELS = new String[]{"值班制度", "值班计划", "值班记录"};
    private List<String> mDataList = Arrays.asList(CHANNELS);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_emergency);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.duty_emergency_title);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new DutySystem());
        fragments.add(new DutyPlan());
        fragments.add(new DutyRecord());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        duty_emergency_viewpager.setOffscreenPageLimit(2);
        duty_emergency_viewpager.setAdapter(adapter);
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
        duty_emergency_indicator.setBackgroundColor(Color.parseColor("#455a64"));
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
                        duty_emergency_viewpager.setCurrentItem(index);
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

        duty_emergency_indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(duty_emergency_indicator, duty_emergency_viewpager);
    }

}
