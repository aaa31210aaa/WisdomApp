package tab.homedetail.hiddendetail;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adpter.FileListAdapter;
import bean.FileBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.MessageEvent;
import utils.PortIpAddress;
import utils.SDUtils;
import utils.ShowToast;

import static utils.PermissionUtil.CreateDeleteFilePermission;
import static utils.PermissionUtil.FileCdFilePermission;
import static utils.PermissionUtil.FileRWPermission;
import static utils.PermissionUtil.WriteFilePermission;

public class IndustryDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.industry_detail_comname)
    TextView industry_detail_comname;
    @BindView(R.id.industry_detail_jcsj)
    TextView industry_detail_jcsj;
    @BindView(R.id.industry_detail_jcr)
    TextView industry_detail_jcr;
    @BindView(R.id.industry_detail_zljzsj)
    TextView industry_detail_zljzsj;
    @BindView(R.id.industry_detail_yhztname)
    TextView industry_detail_yhztname;
    @BindView(R.id.industry_detail_ly)
    TextView industry_detail_ly;
    @BindView(R.id.industry_detail_memo)
    TextView industry_detail_memo;
    @BindView(R.id.industry_detail_list)
    ListView industry_detail_list;

    private String crid;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industry_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        crid = intent.getStringExtra("crid");
        String titleName = intent.getStringExtra("titleName");
        title_name.setText(titleName);
        //检查权限
        if (ContextCompat.checkSelfPermission(context, WriteFilePermission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, CreateDeleteFilePermission) != PackageManager.PERMISSION_GRANTED) {
            FileRWPermission(this);
            FileCdFilePermission(this);
        }
        //注册
        EventBus.getDefault().register(this);
        mDatas = new ArrayList<FileBean>();
        filenames = new ArrayList<String>();
        mOkhttp();
    }


    @OnClick(R.id.back)
    void Back() {
        finish();
    }


    private void mOkhttp() {
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        OkHttpUtils
                .get()
                .url(PortIpAddress.CheckRecordGovAction())
                .addParams("checkRecordbean.crid", crid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(IndustryDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");
                            industry_detail_comname.setText(jsonArray.optJSONObject(0).getString("comname"));
                            industry_detail_jcsj.setText(jsonArray.optJSONObject(0).getString("jcsj"));
                            industry_detail_jcr.setText(jsonArray.optJSONObject(0).getString("jcr"));
                            industry_detail_zljzsj.setText(jsonArray.optJSONObject(0).getString("zljzsj"));
                            industry_detail_yhztname.setText(jsonArray.optJSONObject(0).getString("yhztname"));
                            industry_detail_ly.setText(jsonArray.optJSONObject(0).getString("ly"));
                            industry_detail_memo.setText(jsonArray.optJSONObject(0).getString("memo"));

                            for (int i = 0; i < files.length(); i++) {
                                if (!(files.getJSONObject(i).getString("filename").equals(""))) {
                                    FileBean bean = new FileBean();
                                    bean.setFilename(files.getJSONObject(i).getString("filename"));
                                    String path = PortIpAddress.myUrl + files.getJSONObject(i).getString("fileurl");
                                    path = path.replaceAll("\\\\", "/");
                                    bean.setFileUrl(path);
//                                    bean.setAttachmentid(files.getJSONObject(i).getString("attachmentid"));

                                    /**判断文件是否存在**/
                                    if (SDUtils.isExistence(SDUtils.sdPath + "/" + files.getJSONObject(i).getString("filename"))) {
//                                    bean.setDownopen(R.drawable.openfile);
                                        Log.e(TAG, files.getJSONObject(i).getString("filename"));
                                        bean.setDownloadType("打开");
                                        bean.setDeleteClick(true);
                                        bean.setStatus(FileBean.STATE_DOWNLOADED);
                                    } else {
//                                    bean.setDownopen(R.drawable.downloadfile);
                                        Log.e(TAG, SDUtils.sdPath + files.getJSONObject(i).getString("filename"));
                                        bean.setDownloadType("下载");
                                        bean.setDeleteClick(false);
                                        bean.setStatus(FileBean.STATE_NONE);
                                    }
                                    filenames.add(files.getJSONObject(i).getString("filename"));
                                    mDatas.add(bean);
                                }
                            }

                            if (adapter == null) {
                                adapter = new FileListAdapter(IndustryDetail.this, mDatas, filenames);
                                industry_detail_list.setAdapter(adapter);
                            } else {
                                adapter.DataNotify(mDatas);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Subscribe()
    public void IndustryDetailEvent(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1) {
            ShowToast.showShort(IndustryDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(IndustryDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(IndustryDetail.this, "下载失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }


}
