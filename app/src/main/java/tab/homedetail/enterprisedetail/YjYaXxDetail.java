package tab.homedetail.enterprisedetail;

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

public class YjYaXxDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    //预案类型
    @BindView(R.id.yjyaxx_detail_ohaddres)
    TextView yjyaxx_detail_ohaddres;
    //应急预案名称
    @BindView(R.id.yjyaxx_detail_reservname)
    TextView yjyaxx_detail_reservname;
    //更新时间
    @BindView(R.id.yjyaxx_detail_reloaddate)
    TextView yjyaxx_detail_reloaddate;
    //企业名称
    @BindView(R.id.yjyaxx_detail_qyname)
    TextView yjyaxx_detail_qyname;
    //版本号
    @BindView(R.id.yjyaxx_detail_materialssum)
    TextView yjyaxx_detail_materialssum;
    //附件名称
//     TextView yjyaxx_detail_filename;
    //说明
    @BindView(R.id.yjyaxx_detail_remark)
    TextView yjyaxx_detail_remark;
    //备注
    @BindView(R.id.yjyaxx_detail_memo)
    TextView yjyaxx_detail_memo;
    @BindView(R.id.yjyaxx_detail_list)
    ListView yjyaxx_detail_list;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;

    private String url = "";
    private String erid = "";
    private String mycode = "";
    private String detailid = "";
    private String qyid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yj_ya_xx_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.yjyaxx_detail_title);
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
        erid = intent.getStringExtra("erid");
        mycode = intent.getStringExtra("mcode");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        if (mycode.equals("emap")) {
            detailid = "detailid";
            url = PortIpAddress.GetEmergencyreserveDetailQy();
        } else {
            detailid = "erid";
            url = PortIpAddress.GetEmergencyreserveDetail();
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
                .addParams(detailid, erid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(YjYaXxDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");

                            yjyaxx_detail_ohaddres.setText(jsonArray.getJSONObject(0).getString("ohaddres"));
                            yjyaxx_detail_reservname.setText(jsonArray.getJSONObject(0).getString("reservname"));
                            yjyaxx_detail_reloaddate.setText(jsonArray.getJSONObject(0).getString("reloaddate"));
                            yjyaxx_detail_qyname.setText(jsonArray.getJSONObject(0).getString("qyname"));
                            if (yjyaxx_detail_qyname.getText().toString().trim().equals("")) {
                                yjyaxx_detail_qyname.setEnabled(false);
                            }
                            yjyaxx_detail_materialssum.setText(jsonArray.getJSONObject(0).getString("materialssum"));
//                            yjyaxx_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
                            yjyaxx_detail_remark.setText(jsonArray.getJSONObject(0).getString("remark"));
                            yjyaxx_detail_memo.setText(jsonArray.getJSONObject(0).getString("memo"));
                            qyid = jsonArray.getJSONObject(0).getString("qyid");


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
                                adapter = new FileListAdapter(YjYaXxDetail.this, mDatas, filenames);
                                yjyaxx_detail_list.setAdapter(adapter);
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
    public void YjYaXxDetailEvent(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1) {
            ShowToast.showShort(YjYaXxDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(YjYaXxDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(YjYaXxDetail.this, "下载失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }

}
