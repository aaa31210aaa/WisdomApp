package tab.homedetail.enterprisedetail;

import android.app.Dialog;
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
import utils.SharedPrefsUtil;
import utils.ShowToast;

import static utils.PermissionUtil.CREATEDELETEFILE_REQUESTCODE;
import static utils.PermissionUtil.CreateDeleteFilePermission;
import static utils.PermissionUtil.FileCdFilePermission;
import static utils.PermissionUtil.FileRWPermission;
import static utils.PermissionUtil.STORAGE_REQUESTCODE;
import static utils.PermissionUtil.WriteFilePermission;

public class RulesDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.rules_detail_cbname)
    TextView rules_detail_cbname;
    @BindView(R.id.rules_detail_efftime)
    TextView rules_detail_efftime;
    @BindView(R.id.rules_detail_createname)
    TextView rules_detail_createname;
    @BindView(R.id.rules_detail_cbtypename)
    TextView rules_detail_cbtypename;
    @BindView(R.id.rules_detail_createdate)
    TextView rules_detail_createdate;
    //     TextView rules_detail_filename;
    @BindView(R.id.wxy_detail_memo)
    TextView wxy_detail_memo;
    @BindView(R.id.rules_detail_list)
    ListView rules_detail_list;
    private String gzzdid;
    private String user_token;
    private String url;
    private Intent intent;
    private Dialog dialog;
    private String mycode;
    private String detailid;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.rules_detail_title);
        //检查权限
        if (ContextCompat.checkSelfPermission(context, WriteFilePermission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, CreateDeleteFilePermission) != PackageManager.PERMISSION_GRANTED) {
            FileRWPermission(this);
            FileCdFilePermission(this);
        }
        //注册
        EventBus.getDefault().register(this);

        mDatas = new ArrayList<FileBean>();
        filenames = new ArrayList<String>();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        intent = getIntent();
        gzzdid = intent.getStringExtra("gzzdid");
        mycode = intent.getStringExtra("mcode");

        if (mycode.equals("emap")) {
            url = PortIpAddress.GetCompanybylawdetailQy();
            detailid = "detailid";
        } else {
            url = PortIpAddress.GetCompanybylawdetail();
            detailid = "gzzdid";
        }

        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
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
                .addParams("access_token", user_token)
                .addParams(detailid, gzzdid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(RulesDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");

                            rules_detail_cbname.setText(jsonArray.getJSONObject(0).getString("cbname"));
                            rules_detail_efftime.setText(jsonArray.getJSONObject(0).getString("efftime"));
                            rules_detail_createname.setText(jsonArray.getJSONObject(0).getString("createname"));
                            rules_detail_cbtypename.setText(jsonArray.getJSONObject(0).getString("cbtypename"));
                            rules_detail_createdate.setText(jsonArray.getJSONObject(0).getString("createdate"));
//                            rules_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
                            wxy_detail_memo.setText(jsonArray.getJSONObject(0).getString("memo"));

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
                                adapter = new FileListAdapter(RulesDetail.this, mDatas, filenames);
                                rules_detail_list.setAdapter(adapter);
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
    public void RulesDetailEvent(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1) {
            ShowToast.showShort(RulesDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(RulesDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(RulesDetail.this, "下载失败");
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
                ShowToast.showShort(RulesDetail.this, "拒绝权限后会导致某些功能不可用");
            } else if (requestCode == CREATEDELETEFILE_REQUESTCODE) {
                ShowToast.showShort(RulesDetail.this, "拒绝权限后会导致某些功能不可用");
            }
        }
    };


}
