package tab.homedetail.enforcementdetail;

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
import static utils.PortIpAddress.TOKEN_KEY;

public class TypeInfoDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.typeinfo_detail_bjcdwname)
    TextView typeinfo_detail_bjcdwname;
    @BindView(R.id.typeinfo_detail_frdb)
    TextView typeinfo_detail_frdb;
    @BindView(R.id.typeinfo_detail_zw)
    TextView typeinfo_detail_zw;
    @BindView(R.id.typeinfo_detail_jccs)
    TextView typeinfo_detail_jccs;
    @BindView(R.id.typeinfo_detail_starttime)
    TextView typeinfo_detail_starttime;
    @BindView(R.id.typeinfo_detail_endtime)
    TextView typeinfo_detail_endtime;
    @BindView(R.id.typeinfo_detail_zbjg)
    TextView typeinfo_detail_zbjg;
    @BindView(R.id.typeinfo_detail_zbry)
    TextView typeinfo_detail_zbry;
    @BindView(R.id.typeinfo_detail_zbjz)
    TextView typeinfo_detail_zbjz;

    @BindView(R.id.typeinfo_detail_xbry)
    TextView typeinfo_detail_xbry;
    @BindView(R.id.typeinfo_detail_xbzj)
    TextView typeinfo_detail_xbzj;
    @BindView(R.id.typeinfo_detail_remark)
    TextView typeinfo_detail_remark;
    @BindView(R.id.typeinfo_detail_sfxyzgname)
    TextView typeinfo_detail_sfxyzgname;
    @BindView(R.id.typeinfo_detail_xzcfflname)
    TextView typeinfo_detail_xzcfflname;
    @BindView(R.id.typeinfo_detail_qtqm)
    TextView typeinfo_detail_qtqm;
    @BindView(R.id.typeinfo_detail_fzrqm)
    TextView typeinfo_detail_fzrqm;
    @BindView(R.id.typeinfo_detail_jcdwmc)
    TextView typeinfo_detail_jcdwmc;
    @BindView(R.id.typeinfo_detail_handlingstatusname)
    TextView typeinfo_detail_handlingstatusname;
    @BindView(R.id.typeinfo_detail_memo)
    TextView typeinfo_detail_memo;

    @BindView(R.id.typeinfo_detail_list)
    ListView typeinfo_detail_list;
    private String crid;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_info_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.enterprise_detail_title);
        //注册
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
//        String typename = intent.getStringExtra("typename");
        crid = intent.getStringExtra("crid");
//        typeinfo_detail_title.setText(typename);
        mDatas = new ArrayList<FileBean>();
        filenames = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(context, WriteFilePermission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, CreateDeleteFilePermission) != PackageManager.PERMISSION_GRANTED) {
            FileRWPermission(this);
            FileCdFilePermission(this);
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
                .url(PortIpAddress.Xzzf())
                .addParams(TOKEN_KEY, PortIpAddress.GetToken(this))
                .addParams("bean.crid", crid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(TypeInfoDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");

//                            rules_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
                            for (int j = 0; j < jsonArray.length(); j++) {
                                typeinfo_detail_bjcdwname.setText(jsonArray.optJSONObject(j).getString("bjcdwname"));
                                typeinfo_detail_frdb.setText(jsonArray.optJSONObject(j).getString("frdb"));
                                typeinfo_detail_zw.setText(jsonArray.optJSONObject(j).getString("zw"));
                                typeinfo_detail_jccs.setText(jsonArray.optJSONObject(j).getString("jccs"));
                                typeinfo_detail_starttime.setText(jsonArray.optJSONObject(j).getString("starttime"));
                                typeinfo_detail_endtime.setText(jsonArray.optJSONObject(j).getString("endtime"));
                                typeinfo_detail_zbjg.setText(jsonArray.optJSONObject(j).getString("zbjg"));
                                typeinfo_detail_zbry.setText(jsonArray.optJSONObject(j).getString("zbry"));
                                typeinfo_detail_zbjz.setText(jsonArray.optJSONObject(j).getString("zbjz"));
                                typeinfo_detail_xbry.setText(jsonArray.optJSONObject(j).getString("xbry"));
                                typeinfo_detail_xbzj.setText(jsonArray.optJSONObject(j).getString("xbzj"));
                                typeinfo_detail_remark.setText(jsonArray.optJSONObject(j).getString("remark"));
                                typeinfo_detail_sfxyzgname.setText(jsonArray.optJSONObject(j).getString("sfxyzgname"));
                                typeinfo_detail_xzcfflname.setText(jsonArray.optJSONObject(j).getString("xzcfflname"));
                                typeinfo_detail_qtqm.setText(jsonArray.optJSONObject(j).getString("qtqm"));
                                typeinfo_detail_fzrqm.setText(jsonArray.optJSONObject(j).getString("fzrqm"));
                                typeinfo_detail_jcdwmc.setText(jsonArray.optJSONObject(j).getString("jcdwmc"));
                                typeinfo_detail_handlingstatusname.setText(jsonArray.optJSONObject(j).getString("handlingstatusname"));
                            }


                            for (int i = 0; i < files.length(); i++) {
                                if (!files.getJSONObject(i).getString("filename").equals("")) {
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
                                adapter = new FileListAdapter(TypeInfoDetail.this, mDatas, filenames);
                                typeinfo_detail_list.setAdapter(adapter);
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
            ShowToast.showShort(TypeInfoDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(TypeInfoDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(TypeInfoDetail.this, "下载失败");
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
                ShowToast.showShort(TypeInfoDetail.this, "拒绝权限后会导致某些功能不可用");
            } else if (requestCode == CREATEDELETEFILE_REQUESTCODE) {
                ShowToast.showShort(TypeInfoDetail.this, "拒绝权限后会导致某些功能不可用");
            }
        }
    };

}
