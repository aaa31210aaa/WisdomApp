package tab.homedetail.enterprisedetail;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
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

import static utils.PermissionUtil.CREATEDELETEFILE_REQUESTCODE;
import static utils.PermissionUtil.CreateDeleteFilePermission;
import static utils.PermissionUtil.FileCdFilePermission;
import static utils.PermissionUtil.FileRWPermission;
import static utils.PermissionUtil.STORAGE_REQUESTCODE;
import static utils.PermissionUtil.WriteFilePermission;

public class AqScTrDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    //生产月份
    @BindView(R.id.aqsctr_detail_monthvalue)
    TextView aqsctr_detail_monthvalue;
    //费用类型
    @BindView(R.id.aqsctr_detail_investypename)
    TextView aqsctr_detail_investypename;
    //项目名称
    @BindView(R.id.aqsctr_detail_investname)
    TextView aqsctr_detail_investname;
    //费用金额
    @BindView(R.id.aqsctr_detail_usemoney)
    TextView aqsctr_detail_usemoney;
    //企业名称
    @BindView(R.id.aqsctr_detail_qyname)
    TextView aqsctr_detail_qyname;
    //使用部位
    @BindView(R.id.aqsctr_detail_usesite)
    TextView aqsctr_detail_usesite;
    //数量
    @BindView(R.id.aqsctr_detail_usecount)
    TextView aqsctr_detail_usecount;
    //单位
    @BindView(R.id.aqsctr_detail_useuint)
    TextView aqsctr_detail_useuint;
    //总额
    @BindView(R.id.aqsctr_detail_summoney)
    TextView aqsctr_detail_summoney;
    //备注
    @BindView(R.id.aqsctr_detail_memo)
    TextView aqsctr_detail_memo;
    @BindView(R.id.aqsctr_detail_list)
    ListView aqsctr_detail_list;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;


    private String siid;
    private String url;
    private String detailid;
    private String mycode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aq_sc_tr_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.aqtr_detail_title);
        //检查权限
        if (ContextCompat.checkSelfPermission(context, WriteFilePermission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, CreateDeleteFilePermission) != PackageManager.PERMISSION_GRANTED) {
            FileRWPermission(this);
            FileCdFilePermission(this);
        }
        //注册
        EventBus.getDefault().register(this);
        mDatas = new ArrayList<FileBean>();
        filenames = new ArrayList<String>();
        Intent intent = getIntent();
        siid = intent.getStringExtra("siid");
        mycode = intent.getStringExtra("mcode");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        if (mycode.equals("emap")) {
            detailid = "detailid";
            url = PortIpAddress.GetSafeinvestmentDetailQy();
        } else {
            detailid = "siid";
            url = PortIpAddress.GetSafeinvestmentDetail();
        }
        mOkhttp();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", PortIpAddress.GetToken(this))
                .addParams(detailid, siid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(AqScTrDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");

                            aqsctr_detail_monthvalue.setText(jsonArray.getJSONObject(0).getString("monthvalue"));
                            aqsctr_detail_investypename.setText(jsonArray.getJSONObject(0).getString("investypename"));
                            aqsctr_detail_investname.setText(jsonArray.getJSONObject(0).getString("investname"));
                            aqsctr_detail_usemoney.setText(jsonArray.getJSONObject(0).getString("usemoney"));
                            aqsctr_detail_qyname.setText(jsonArray.getJSONObject(0).getString("qyname"));
                            aqsctr_detail_usesite.setText(jsonArray.getJSONObject(0).getString("usesite"));
                            aqsctr_detail_usecount.setText(jsonArray.getJSONObject(0).getString("usecount"));
                            aqsctr_detail_useuint.setText(jsonArray.getJSONObject(0).getString("useuint"));
//                            aqsctr_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
                            aqsctr_detail_summoney.setText(jsonArray.getJSONObject(0).getString("summoney"));
                            aqsctr_detail_memo.setText(jsonArray.getJSONObject(0).getString("memo"));

                            for (int i = 0; i < files.length(); i++) {
                                if (!files.getJSONObject(i).getString("filename").equals("")) {
                                    FileBean bean = new FileBean();
                                    bean.setFilename(files.getJSONObject(i).getString("filename"));
                                    String path = PortIpAddress.myUrl + files.getJSONObject(i).getString("fileurl");
                                    path = path.replaceAll("\\\\", "/");
                                    bean.setFileUrl(path);
                                    bean.setAttachmentid(files.getJSONObject(i).getString("attachmentid"));

                                    /**判断文件是否存在**/
                                    if (SDUtils.isExistence(SDUtils.sdPath + "/" + files.getJSONObject(i).getString("filename"))) {
                                        Log.e(TAG, files.getJSONObject(i).getString("filename"));
                                        bean.setDownloadType("打开");
                                        bean.setDeleteClick(true);
                                        bean.setStatus(FileBean.STATE_DOWNLOADED);
                                    } else {
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
                                adapter = new FileListAdapter(AqScTrDetail.this, mDatas, filenames);
                                aqsctr_detail_list.setAdapter(adapter);
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
    public void AqScTrDetailEvent(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1) {
            ShowToast.showShort(AqScTrDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(AqScTrDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(AqScTrDetail.this, "下载失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }

    //权限回调
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == STORAGE_REQUESTCODE) {
                ShowToast.showShort(AqScTrDetail.this, "拒绝权限后会导致某些功能不可用");
            } else if (requestCode == CREATEDELETEFILE_REQUESTCODE) {
                ShowToast.showShort(AqScTrDetail.this, "拒绝权限后会导致某些功能不可用");
            }
        }
    };

}
