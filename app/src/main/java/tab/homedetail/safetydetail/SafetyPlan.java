package tab.homedetail.safetydetail;

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
import tab.homedetail.safetydetail.safetyplan.Executed;
import tab.homedetail.safetydetail.safetyplan.Overdue;
import tab.homedetail.safetydetail.safetyplan.Pending;
import tab.homedetail.safetydetail.safetyplan.Unexecuted;
import utils.BaseActivity;
import utils.ColorFlipPagerTitleView;

public class SafetyPlan extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.safety_plan_indicator)
    MagicIndicator safety_plan_indicator;
    @BindView(R.id.safety_plan_viewpager)
    ViewPager safety_plan_viewpager;
    private static final String[] CHANNELS = new String[]{"待执行", "未执行", "逾期执行", "已执行"};
    private List<String> mDataList = Arrays.asList(CHANNELS);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_plan);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.safety_plan_title);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Pending());
        fragments.add(new Unexecuted());
        fragments.add(new Overdue());
        fragments.add(new Executed());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        safety_plan_viewpager.setOffscreenPageLimit(3);
        safety_plan_viewpager.setAdapter(adapter);
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
        safety_plan_indicator.setBackgroundColor(Color.parseColor("#455a64"));
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
                        safety_plan_viewpager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
//                indicator.setLineHeight(UIUtil.dip2px(context, 6));
//                indicator.setLineWidth(UIUtil.dip2px(context, 10));
//                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
//                indicator.setStartInterpolator(new AccelerateInterpolator());
//                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#40c4ff"));
                return indicator;
            }
        });

        safety_plan_indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(safety_plan_indicator, safety_plan_viewpager);
    }
}
