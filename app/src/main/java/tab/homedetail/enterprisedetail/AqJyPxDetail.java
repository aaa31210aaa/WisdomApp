package tab.homedetail.enterprisedetail;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
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

public class AqJyPxDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    //培训名称
    @BindView(R.id.aqjypx_detail_trainingname)
    TextView aqjypx_detail_trainingname;
    //培训时间
    @BindView(R.id.aqjypx_detail_trainingdate)
    TextView aqjypx_detail_trainingdate;
    //培训地点
    @BindView(R.id.aqjypx_detail_trainingadd)
    TextView aqjypx_detail_trainingadd;
    //培训内容
    @BindView(R.id.aqjypx_detail_trainingcontent)
    TextView aqjypx_detail_trainingcontent;
    //培训方式
    @BindView(R.id.aqjypx_detail_trainingtype)
    TextView aqjypx_detail_trainingtype;
    //培训授课人
    @BindView(R.id.aqjypx_detail_trainingman)
    TextView aqjypx_detail_trainingman;
    //授课人介绍
    @BindView(R.id.aqjypx_detail_trainingintro)
    TextView aqjypx_detail_trainingintro;
    //组织单位
    @BindView(R.id.aqjypx_detail_trainingunit)
    TextView aqjypx_detail_trainingunit;
    //完成情况
    @BindView(R.id.aqjypx_detail_completion)
    TextView aqjypx_detail_completion;
    //培训效果
    @BindView(R.id.aqjypx_detail_effect)
    TextView aqjypx_detail_effect;
    //企业名称
    @BindView(R.id.aqjypx_detail_qyname)
    TextView aqjypx_detail_qyname;
    //附件名称
//     TextView aqjypx_detail_filename;
    //备注
    @BindView(R.id.aqjypx_detail_memo)
    TextView aqjypx_detail_memo;
    @BindView(R.id.aqjypx_detail_list)
    ListView aqjypx_detail_list;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;

    private String trainingid = "";
    private String url = "";
    private String detailid = "";
    private String mycode = "";
    private String qyid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aq_jy_px_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.aqjypx_detail_title);
        //检查权限
        if (ContextCompat.checkSelfPermission(context, WriteFilePermission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, CreateDeleteFilePermission) != PackageManager.PERMISSION_GRANTED) {
            FileRWPermission(this);
            FileCdFilePermission(this);
        }
        //注册
        EventBus.getDefault().register(this);
        //加下划线
        aqjypx_detail_qyname.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //抗锯齿
        aqjypx_detail_qyname.getPaint().setAntiAlias(true);

        mDatas = new ArrayList<FileBean>();
        filenames = new ArrayList<String>();
        Intent intent = getIntent();
        trainingid = intent.getStringExtra("trainingid");
        mycode = intent.getStringExtra("mcode");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        if (mycode.equals("emap")) {
            detailid = "detailid";
            url = PortIpAddress.GetSafetytrainingDetailQy();
        } else {
            detailid = "trainingid";
            url = PortIpAddress.GetSafetytrainingDetail();
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
                .addParams("access_token", SharedPrefsUtil.getValue(this, "userInfo", "user_token", null))
                .addParams(detailid, trainingid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(AqJyPxDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");
                            aqjypx_detail_trainingname.setText(jsonArray.getJSONObject(0).getString("trainingname"));
                            aqjypx_detail_trainingdate.setText(jsonArray.getJSONObject(0).getString("trainingdate"));
                            aqjypx_detail_trainingadd.setText(jsonArray.getJSONObject(0).getString("trainingadd"));
                            aqjypx_detail_trainingcontent.setText(jsonArray.getJSONObject(0).getString("trainingcontent"));
                            aqjypx_detail_trainingtype.setText(jsonArray.getJSONObject(0).getString("trainingtype"));
                            aqjypx_detail_trainingman.setText(jsonArray.getJSONObject(0).getString("trainingman"));
                            aqjypx_detail_trainingintro.setText(jsonArray.getJSONObject(0).getString("trainingintro"));
                            aqjypx_detail_trainingunit.setText(jsonArray.getJSONObject(0).getString("trainingunit"));
                            aqjypx_detail_completion.setText(jsonArray.getJSONObject(0).getString("completion"));
                            aqjypx_detail_effect.setText(jsonArray.getJSONObject(0).getString("effect"));
                            aqjypx_detail_qyname.setText(jsonArray.getJSONObject(0).getString("qyname"));

                            if (aqjypx_detail_qyname.getText().toString().trim().equals("")) {
                                aqjypx_detail_qyname.setEnabled(false);
                            }

//                          aqjypx_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
                            aqjypx_detail_memo.setText(jsonArray.getJSONObject(0).getString("memo"));
                            qyid = jsonArray.getJSONObject(0).getString("qyid");

                            for (int i = 0; i < files.length(); i++) {
                                if (!(files.getJSONObject(i).getString("filename").equals(""))) {
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
                                adapter = new FileListAdapter(AqJyPxDetail.this, mDatas, filenames);
                                aqjypx_detail_list.setAdapter(adapter);
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
    public void AqJyPxDetailEvent(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1) {
            ShowToast.showShort(AqJyPxDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(AqJyPxDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(AqJyPxDetail.this, "下载失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }
}
