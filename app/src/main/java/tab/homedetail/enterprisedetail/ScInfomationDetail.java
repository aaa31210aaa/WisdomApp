package tab.homedetail.enterprisedetail;

import android.app.Dialog;
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
import utils.SharedPrefsUtil;
import utils.ShowToast;

import static utils.PermissionUtil.CreateDeleteFilePermission;
import static utils.PermissionUtil.FileCdFilePermission;
import static utils.PermissionUtil.FileRWPermission;
import static utils.PermissionUtil.WriteFilePermission;

public class ScInfomationDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.sc_infomation_detail_checkname)
    TextView sc_infomation_detail_checkname;
    @BindView(R.id.sc_infomation_detail_problems)
    TextView sc_infomation_detail_problems;
    @BindView(R.id.sc_infomation_detail_iscommit)
    TextView sc_infomation_detail_iscommit;
    @BindView(R.id.sc_infomation_detail_checkadd)
    TextView sc_infomation_detail_checkadd;
    @BindView(R.id.sc_infomation_detail_checkman)
    TextView sc_infomation_detail_checkman;
    @BindView(R.id.sc_infomation_detail_scdate)
    TextView sc_infomation_detail_scdate;
    @BindView(R.id.sc_infomation_detail_handlemethod)
    TextView sc_infomation_detail_handlemethod;
    //     TextView sc_infomation_detail_filename;
    @BindView(R.id.sc_infomation_detail_memo)
    TextView sc_infomation_detail_memo;
    @BindView(R.id.sc_infomation_detail_list)
    ListView sc_infomation_detail_list;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;


    private String scid;
    private String user_token;
    private String url;
    private Intent intent;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sc_infomation_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.qyaqscxx_detail_title);
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
        url = PortIpAddress.GetSafechecklistdetail();
        intent = getIntent();
        scid = intent.getStringExtra("scid");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
    }

    @OnClick(R.id.back)
    void Back(){
        finish();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("scid", scid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(ScInfomationDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");
                            sc_infomation_detail_checkname.setText(jsonArray.getJSONObject(0).getString("checkname"));
                            sc_infomation_detail_problems.setText(jsonArray.getJSONObject(0).getString("problems"));
                            sc_infomation_detail_iscommit.setText(jsonArray.getJSONObject(0).getString("iscommit"));
                            sc_infomation_detail_checkadd.setText(jsonArray.getJSONObject(0).getString("checkadd"));
                            sc_infomation_detail_checkman.setText(jsonArray.getJSONObject(0).getString("checkman"));
                            sc_infomation_detail_scdate.setText(jsonArray.getJSONObject(0).getString("scdate"));
                            sc_infomation_detail_handlemethod.setText(jsonArray.getJSONObject(0).getString("handlemethod"));
//                            sc_infomation_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
                            sc_infomation_detail_memo.setText(jsonArray.getJSONObject(0).getString("memo"));

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
                                        bean.setDeleteClick(true);
                                        bean.setStatus(FileBean.STATE_DOWNLOADED);
                                    } else {
//                                    bean.setDownopen(R.drawable.downloadfile);
                                        Log.e(TAG, SDUtils.sdPath + files.getJSONObject(i).getString("filename"));
                                        bean.setDeleteClick(false);
                                        bean.setStatus(FileBean.STATE_NONE);
                                    }
                                    filenames.add(files.getJSONObject(i).getString("filename"));
                                    mDatas.add(bean);
                                }

                            }
                            if (adapter == null) {
                                adapter = new FileListAdapter(ScInfomationDetail.this, mDatas, filenames);
                                sc_infomation_detail_list.setAdapter(adapter);
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
    public void ScInfomationDetailEvent(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1) {
            ShowToast.showShort(ScInfomationDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(ScInfomationDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(ScInfomationDetail.this, "下载失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }
}
