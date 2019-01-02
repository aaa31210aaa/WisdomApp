package tab.homedetail.enterprisedetail;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
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

public class QyZzDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    //证照类型
    @BindView(R.id.qyzz_detail_cftypename)
    TextView qyzz_detail_cftypename;
    //证件号
    @BindView(R.id.qyzz_detail_certificate)
    TextView qyzz_detail_certificate;
    //证件名称
    @BindView(R.id.qyzz_detail_cardname)
    TextView qyzz_detail_cardname;
    //负责人
    @BindView(R.id.qyzz_detail_administrator)
    TextView qyzz_detail_administrator;
    //颁布时间
    @BindView(R.id.qyzz_detail_carddate)
    TextView qyzz_detail_carddate;
    //固定电话
    @BindView(R.id.qyzz_detail_mhtelephone)
    TextView qyzz_detail_mhtelephone;
    //移动电话
    @BindView(R.id.qyzz_detail_mhmobilephone)
    TextView qyzz_detail_mhmobilephone;
    //岗位
    @BindView(R.id.qyzz_detail_postion)
    TextView qyzz_detail_postion;
    //有效开始日期
    @BindView(R.id.qyzz_detail_yxstardate)
    TextView qyzz_detail_yxstardate;
    //有效结束日期
    @BindView(R.id.qyzz_detail_yxenddate)
    TextView qyzz_detail_yxenddate;
    //企业名称
    @BindView(R.id.qyzz_detail_qyname)
    TextView qyzz_detail_qyname;
    @BindView(R.id.qyzz_detail_list)
    ListView qyzz_detail_list;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;

    private String detailid;
    private Intent intent;
    private String qyid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qy_zz_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.qyzz_detail_title);
        //加下划线
        qyzz_detail_qyname.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //抗锯齿
        qyzz_detail_qyname.getPaint().setAntiAlias(true);
        //检查权限
        if (ContextCompat.checkSelfPermission(context, WriteFilePermission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, CreateDeleteFilePermission) != PackageManager.PERMISSION_GRANTED) {
            FileRWPermission(this);
            FileCdFilePermission(this);
        }
        //注册
        EventBus.getDefault().register(this);
        mDatas = new ArrayList<FileBean>();
        filenames = new ArrayList<String>();
        intent = getIntent();
        detailid = intent.getStringExtra("detailid");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    @OnClick(R.id.qyzz_detail_qyname)
    void QyInfo() {
        Intent intent = new Intent(this, EnterpriseDetail.class);
        intent.putExtra("clickId", qyid);
        startActivity(intent);
    }


    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(PortIpAddress.GetCertificateDetail())
                .addParams("access_token", PortIpAddress.GetToken(this))
                .addParams("detailid", detailid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(QyZzDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");

                            qyzz_detail_cftypename.setText(jsonArray.getJSONObject(0).getString("cftypename"));
                            qyzz_detail_certificate.setText(jsonArray.getJSONObject(0).getString("certificate"));
                            qyzz_detail_cardname.setText(jsonArray.getJSONObject(0).getString("cardname"));
                            qyzz_detail_administrator.setText(jsonArray.getJSONObject(0).getString("administrator"));
                            qyzz_detail_carddate.setText(jsonArray.getJSONObject(0).getString("carddate"));
                            qyzz_detail_mhtelephone.setText(jsonArray.getJSONObject(0).getString("mhtelephone"));
                            qyzz_detail_mhmobilephone.setText(jsonArray.getJSONObject(0).getString("mhmobilephone"));
                            qyzz_detail_postion.setText(jsonArray.getJSONObject(0).getString("postion"));
                            qyzz_detail_yxstardate.setText(jsonArray.getJSONObject(0).getString("yxstardate"));
                            qyzz_detail_yxenddate.setText(jsonArray.getJSONObject(0).getString("yxenddate"));
                            qyzz_detail_qyname.setText(jsonArray.getJSONObject(0).getString("qyname"));
                            qyid = jsonArray.getJSONObject(0).getString("qyid");
                            if (qyzz_detail_qyname.getText().toString().trim().equals("")) {
                                qyzz_detail_qyname.setEnabled(false);
                            }

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
                                adapter = new FileListAdapter(QyZzDetail.this, mDatas, filenames);
                                qyzz_detail_list.setAdapter(adapter);
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
    public void QyZzDetailEvent(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1) {
            ShowToast.showShort(QyZzDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(QyZzDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(QyZzDetail.this, "下载失败");
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
                ShowToast.showShort(QyZzDetail.this, "拒绝权限后会导致某些功能不可用");
            } else if (requestCode == CREATEDELETEFILE_REQUESTCODE) {
                ShowToast.showShort(QyZzDetail.this, "拒绝权限后会导致某些功能不可用");
            }
        }
    };
}
