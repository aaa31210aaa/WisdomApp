package tab.homedetail.hiddendetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import adpter.PhotoViewPagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;

public class PhotoView extends BaseActivity {
    @BindView(R.id.photo_viewPager)
    PhotoViewPager photo_viewPager;
    @BindView(R.id.photo_num)
    TextView photo_num;
    private PhotoViewPagerAdapter adapter;
    //图片集合
    private List<String> images;
    //点的是哪一张图片
    private int currentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("position", 0);
        images = intent.getStringArrayListExtra("urls");
        Log.e("ces", images + "");
        adapter = new PhotoViewPagerAdapter(this, images);
        photo_viewPager.setAdapter(adapter);
        photo_viewPager.setCurrentItem(currentPosition);
        photo_num.setText(currentPosition + 1 + "/" + images.size());
        photo_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                photo_num.setText(currentPosition + 1 + "/" + images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.photo_view_back)
    void Back(){
        finish();
    }

}
