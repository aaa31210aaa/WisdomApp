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

public class WorkInfoDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.workinfo_detail_jcr)
    TextView workinfo_detail_jcr;
    @BindView(R.id.workinfo_detail_jcsj)
    TextView workinfo_detail_jcsj;
    @BindView(R.id.workinfo_detail_jcfxwt)
    TextView workinfo_detail_jcfxwt;
    @BindView(R.id.workinfo_detail_wtcljy)
    TextView workinfo_detail_wtcljy;
    @BindView(R.id.workinfo_detail_memo)
    TextView workinfo_detail_memo;
    @BindView(R.id.workinfo_detail_list)
    ListView workinfo_detail_list;
    private String crid;
    private String usertype;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_info_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.workinfo_detail);
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
        crid = intent.getStringExtra("crid");
        usertype = intent.getStringExtra("usertype");
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
                .url(PortIpAddress.WorkInfolist())
                .addParams("inminecadrescheckbean.crid", crid)
                .addParams("inminecadrescheckbean.usertype", usertype)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(WorkInfoDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");
                            workinfo_detail_jcr.setText(jsonArray.getJSONObject(0).getString("jcr"));
                            workinfo_detail_jcsj.setText(jsonArray.getJSONObject(0).getString("jcsj"));
                            workinfo_detail_jcfxwt.setText(jsonArray.getJSONObject(0).getString("jcfxwt"));
                            workinfo_detail_wtcljy.setText(jsonArray.getJSONObject(0).getString("wtcljy"));
                            workinfo_detail_memo.setText(jsonArray.getJSONObject(0).getString("memo"));


                            for (int i = 0; i < files.length(); i++) {
                                if (!(files.getJSONObject(i).getString("filename").equals(""))) {
                                    FileBean bean = new FileBean();
                                    bean.setFilename(files.getJSONObject(i).getString("filename"));
                                    String path = PortIpAddress.myUrl + files.getJSONObject(i).getString("fileurl");
                                    path = path.replaceAll("\\\\", "/");
                                    bean.setFileUrl(path);

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
                                adapter = new FileListAdapter(WorkInfoDetail.this, mDatas, filenames);
                                workinfo_detail_list.setAdapter(adapter);
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
    public void Event(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1) {
            ShowToast.showShort(WorkInfoDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(WorkInfoDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(WorkInfoDetail.this, "下载失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }

}
