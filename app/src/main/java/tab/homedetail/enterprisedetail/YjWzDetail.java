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
import utils.ShowToast;

import static utils.PermissionUtil.CreateDeleteFilePermission;
import static utils.PermissionUtil.FileCdFilePermission;
import static utils.PermissionUtil.FileRWPermission;
import static utils.PermissionUtil.WriteFilePermission;

public class YjWzDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    //更新时间
    @BindView(R.id.yjwz_detail_modifydate)
    TextView yjwz_detail_modifydate;
    //物资名称
    @BindView(R.id.yjwz_detail_materialsname)
    TextView yjwz_detail_materialsname;
    //物资数量
    @BindView(R.id.yjwz_detail_materialssum)
    TextView yjwz_detail_materialssum;
    //存放位置
    @BindView(R.id.yjwz_detail_ohaddres)
    TextView yjwz_detail_ohaddres;
    //更新周期
    @BindView(R.id.yjwz_detail_sites)
    TextView yjwz_detail_sites;
    //说明
    @BindView(R.id.yjwz_detail_remark)
    TextView yjwz_detail_remark;
    //附件名称
//     TextView yjwz_detail_filename;
    //备注
    @BindView(R.id.yjwz_detail_memo)
    TextView yjwz_detail_memo;
    @BindView(R.id.yjwz_detail_list)
    ListView yjwz_detail_list;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;

    private String detailid;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yj_wz_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.yjwz_detail_title);
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
                .url(PortIpAddress.GetEmergencymaterialsdetail())
                .addParams("access_token", PortIpAddress.GetToken(this))
                .addParams("detailid", detailid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(YjWzDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");

                            yjwz_detail_modifydate.setText(jsonArray.getJSONObject(0).getString("modifydate"));
                            yjwz_detail_materialsname.setText(jsonArray.getJSONObject(0).getString("materialsname"));
                            yjwz_detail_materialssum.setText(jsonArray.getJSONObject(0).getString("materialssum"));
                            yjwz_detail_ohaddres.setText(jsonArray.getJSONObject(0).getString("ohaddres"));
                            yjwz_detail_sites.setText(jsonArray.getJSONObject(0).getString("sites"));
                            yjwz_detail_remark.setText(jsonArray.getJSONObject(0).getString("remark"));
//                            yjwz_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
                            yjwz_detail_memo.setText(jsonArray.getJSONObject(0).getString("memo"));

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
                                adapter = new FileListAdapter(YjWzDetail.this, mDatas, filenames);
                                yjwz_detail_list.setAdapter(adapter);
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
    public void YjWzDetailEvent(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1) {
            ShowToast.showShort(YjWzDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(YjWzDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(YjWzDetail.this, "下载失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }
}
