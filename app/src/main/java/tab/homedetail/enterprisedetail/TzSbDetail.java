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
import utils.ShowToast;

import static utils.PermissionUtil.CreateDeleteFilePermission;
import static utils.PermissionUtil.FileCdFilePermission;
import static utils.PermissionUtil.FileRWPermission;
import static utils.PermissionUtil.WriteFilePermission;

public class TzSbDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    //设备名称
    @BindView(R.id.tzsb_detail_facilitiesname)
    TextView tzsb_detail_facilitiesname;
    //规格型号
    @BindView(R.id.tzsb_detail_facilitiesno)
    TextView tzsb_detail_facilitiesno;
    //制造厂商
    @BindView(R.id.tzsb_detail_manufacturer)
    TextView tzsb_detail_manufacturer;
    //安装位置
    @BindView(R.id.tzsb_detail_installsite)
    TextView tzsb_detail_installsite;
    //场内编号
    @BindView(R.id.tzsb_detail_installcode)
    TextView tzsb_detail_installcode;
    //设备状态
    @BindView(R.id.tzsb_detail_facilitiestypename)
    TextView tzsb_detail_facilitiestypename;
    //运行情况
    @BindView(R.id.tzsb_detail_functioncase)
    TextView tzsb_detail_functioncase;
    //投用时间
    @BindView(R.id.tzsb_detail_functiondate)
    TextView tzsb_detail_functiondate;
    //检验周期
    @BindView(R.id.tzsb_detail_checktimename)
    TextView tzsb_detail_checktimename;
    //检查时间
    @BindView(R.id.tzsb_detail_checkdate)
    TextView tzsb_detail_checkdate;
    //检验情况
    @BindView(R.id.tzsb_detail_checkcase)
    TextView tzsb_detail_checkcase;
    //企业名称
    @BindView(R.id.tzsb_detail_qyname)
    TextView tzsb_detail_qyname;

    //附件名称
//     TextView tzsb_detail_filename;
    //备注
    @BindView(R.id.tzsb_detail_memo)
    TextView tzsb_detail_memo;
    @BindView(R.id.tzsb_detail_list)
    ListView tzsb_detail_list;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;


    private String detailid = "";
    private Intent intent;
    private String qyid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tz_sb_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.tzsb_detail_title);
        //加下划线
        tzsb_detail_qyname.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //抗锯齿
        tzsb_detail_qyname.getPaint().setAntiAlias(true);
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

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(PortIpAddress.GetSafefacilitiesdetail())
                .addParams("access_token", PortIpAddress.GetToken(this))
                .addParams("detailid", detailid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(TzSbDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");

                            tzsb_detail_facilitiesname.setText(jsonArray.getJSONObject(0).getString("facilitiesname"));
                            tzsb_detail_facilitiesno.setText(jsonArray.getJSONObject(0).getString("facilitiesno"));
                            tzsb_detail_manufacturer.setText(jsonArray.getJSONObject(0).getString("manufacturer"));
                            tzsb_detail_installsite.setText(jsonArray.getJSONObject(0).getString("installsite"));
                            tzsb_detail_installcode.setText(jsonArray.getJSONObject(0).getString("installcode"));
                            tzsb_detail_facilitiestypename.setText(jsonArray.getJSONObject(0).getString("facilitiestypename"));
                            tzsb_detail_functioncase.setText(jsonArray.getJSONObject(0).getString("functioncase"));
                            tzsb_detail_functiondate.setText(jsonArray.getJSONObject(0).getString("functiondate"));
                            tzsb_detail_checktimename.setText(jsonArray.getJSONObject(0).getString("checktimename"));
                            tzsb_detail_checkdate.setText(jsonArray.getJSONObject(0).getString("checkdate"));
                            tzsb_detail_checkcase.setText(jsonArray.getJSONObject(0).getString("checkcase"));
//                            tzsb_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
                            tzsb_detail_memo.setText(jsonArray.getJSONObject(0).getString("memo"));
                            tzsb_detail_qyname.setText(jsonArray.getJSONObject(0).getString("qyname"));
                            if (tzsb_detail_qyname.getText().toString().trim().equals("")) {
                                tzsb_detail_qyname.setEnabled(false);
                            }

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
                                adapter = new FileListAdapter(TzSbDetail.this, mDatas, filenames);
                                tzsb_detail_list.setAdapter(adapter);
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
    public void TzSbDetailEvent(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1) {
            ShowToast.showShort(TzSbDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(TzSbDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(TzSbDetail.this, "下载失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }

}
