package tab.homedetail.hiddendetail;

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
import utils.BaseActivity;
import utils.ColorFlipPagerTitleView;

public class QyYhInfomationDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.qy_yh_information_detail_indicator)
    MagicIndicator qy_yh_information_detail_indicator;
    @BindView(R.id.qy_yh_information_detail_viewpager)
    ViewPager qy_yh_information_detail_viewpager;
    private static final String[] CHANNELS = new String[]{"隐患信息详情", "安全监管信息"};
    private List<String> mDataList = Arrays.asList(CHANNELS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qy_yh_infomation_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.yhxx_detail_title);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HiddenFile());
        fragments.add(new HiddenSafety());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        qy_yh_information_detail_viewpager.setOffscreenPageLimit(1);
        qy_yh_information_detail_viewpager.setAdapter(adapter);
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
        qy_yh_information_detail_indicator.setBackgroundColor(Color.parseColor("#455a64"));
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
                        qy_yh_information_detail_viewpager.setCurrentItem(index);
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

        qy_yh_information_detail_indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(qy_yh_information_detail_indicator, qy_yh_information_detail_viewpager);
    }


//    private void mOkhttp() {
//        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
//        OkHttpUtils
//                .get()
//                .url(PortIpAddress.GetRiskinfodetail())
//                .addParams("access_token", PortIpAddress.GetToken(this))
//                .addParams(detailid, yhid)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        e.printStackTrace();
//                        dialog.dismiss();
//                        ShowToast.showShort(QyYhInfomationDetail.this, R.string.network_error);
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        try {
//                            Log.e(TAG, response);
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
//                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");
//
//                            qy_yh_information_detail_crname.setText(jsonArray.getJSONObject(0).getString("crname"));
//                            qy_yh_information_detail_crtypename.setText(jsonArray.getJSONObject(0).getString("crtypename"));
//                            qy_yh_information_detail_pcdate.setText(jsonArray.getJSONObject(0).getString("pcdate"));
//                            qy_yh_information_detail_crcode.setText(jsonArray.getJSONObject(0).getString("crcode"));
//                            qy_yh_information_detail_crlxflname.setText(jsonArray.getJSONObject(0).getString("crlxflname"));
//                            qy_yh_information_detail_zgtypename.setText(jsonArray.getJSONObject(0).getString("zgtypename"));
//                            qy_yh_information_detail_craddr.setText(jsonArray.getJSONObject(0).getString("craddr"));
//                            qy_yh_information_detail_crdesc.setText(jsonArray.getJSONObject(0).getString("crdesc"));
//                            qy_yh_information_detail_crbgzy.setText(jsonArray.getJSONObject(0).getString("crbgzy"));
//                            qy_yh_information_detail_crstatename.setText(jsonArray.getJSONObject(0).getString("crstatename"));
//                            qy_yh_information_detail_zyzlfa.setText(jsonArray.getJSONObject(0).getString("zyzlfa"));
//                            qy_yh_information_detail_source.setText(jsonArray.getJSONObject(0).getString("source"));
//                            qy_yh_information_detail_zgman.setText(jsonArray.getJSONObject(0).getString("zgman"));
//                            qy_yh_information_detail_zljzdate.setText(jsonArray.getJSONObject(0).getString("zljzdate"));
//                            qy_yh_information_detail_deptname.setText(jsonArray.getJSONObject(0).getString("deptname"));
////                          qy_yh_information_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
//
//                            for (int i = 0; i < files.length(); i++) {
//                                if (!files.getJSONObject(i).getString("filename").equals("")) {
//                                    FileBean bean = new FileBean();
//                                    bean.setFilename(files.getJSONObject(i).getString("filename"));
//                                    String path = PortIpAddress.myUrl + files.getJSONObject(i).getString("fileurl");
//                                    path = path.replaceAll("\\\\", "/");
//                                    bean.setFileUrl(path);
//                                    bean.setAttachmentid(files.getJSONObject(i).getString("attachmentid"));
//
//                                    /**判断文件是否存在**/
//                                    if (SDUtils.isExistence(SDUtils.sdPath + "/" + files.getJSONObject(i).getString("filename"))) {
////                                    bean.setDownopen(R.drawable.openfile);
//                                        Log.e(TAG, files.getJSONObject(i).getString("filename"));
//                                        bean.setDownloadType("打开");
//                                        bean.setDeleteClick(true);
//                                        bean.setStatus(FileBean.STATE_DOWNLOADED);
//                                    } else {
////                                    bean.setDownopen(R.drawable.downloadfile);
//                                        Log.e(TAG, SDUtils.sdPath + files.getJSONObject(i).getString("filename"));
//                                        bean.setDownloadType("下载");
//                                        bean.setDeleteClick(false);
//                                        bean.setStatus(FileBean.STATE_NONE);
//                                    }
//                                    filenames.add(files.getJSONObject(i).getString("filename"));
//                                    mDatas.add(bean);
//                                }
//                            }
//                            if (adapter == null) {
//                                adapter = new FileListAdapter(QyYhInfomationDetail.this, mDatas, filenames);
//                                qy_yh_information_detail_list.setAdapter(adapter);
//                            } else {
//                                adapter.DataNotify(mDatas);
//                            }
//
//                            dialog.dismiss();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }
//
//    @Subscribe()
//    public void Event(MessageEvent messageEvent) {
//        for (int i = 0; i < mDatas.size(); i++) {
//            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
//                mDatas.get(i).setStatus(messageEvent.getStatus());
//            }
//        }
//        adapter.notifyDataSetChanged();
//        if (messageEvent.getStatus() == 1){
//            ShowToast.showShort(QyYhInfomationDetail.this, "下载中...");
//        }else if (messageEvent.getStatus() == 2){
//            ShowToast.showShort(QyYhInfomationDetail.this, "下载成功");
//        }else if (messageEvent.getStatus() == 3){
//            ShowToast.showShort(QyYhInfomationDetail.this, "下载失败");
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //取消注册
//        EventBus.getDefault().unregister(this);
//    }


}
