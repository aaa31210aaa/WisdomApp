package tab.homedetail.lawsdetail;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

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
import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;

public class LawsDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.laws_detail_lrtitle)
    TextView laws_detail_lrtitle;
    @BindView(R.id.laws_detail_industryname)
    TextView laws_detail_industryname;
    @BindView(R.id.laws_detail_lrtypename)
    TextView laws_detail_lrtypename;
    @BindView(R.id.laws_detail_docnumber)
    TextView laws_detail_docnumber;
    @BindView(R.id.laws_detail_bbdate)
    TextView laws_detail_bbdate;
    @BindView(R.id.laws_detail_effdate)
    TextView laws_detail_effdate;
    @BindView(R.id.laws_detail_item_lrdesc)
    TextView laws_detail_item_lrdesc;
    @BindView(R.id.laws_detail_list)
    ListView laws_detail_list;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;


    //法律法规ID
    private String flfgid;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laws_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.lawsdetail_title);
        Intent intent = getIntent();
        //检查权限
        if (ContextCompat.checkSelfPermission(context, WriteFilePermission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, CreateDeleteFilePermission) != PackageManager.PERMISSION_GRANTED) {
            FileRWPermission(this);
            FileCdFilePermission(this);
        }
        //注册
        EventBus.getDefault().register(this);

        flfgid = intent.getStringExtra("flfgid");
        mDatas = new ArrayList<FileBean>();
        filenames = new ArrayList<String>();
        mConnect();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    private void mConnect() {
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        OkGo.<String>get(PortIpAddress.GetLawregulationsdetail())
                .tag(this)
                .params("flfgid", flfgid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                laws_detail_lrtitle.setText(jsonArray.optJSONObject(index).getString("lrtitle"));
                                laws_detail_industryname.setText(jsonArray.optJSONObject(index).getString("industryname"));
                                laws_detail_lrtypename.setText(jsonArray.optJSONObject(index).getString("lrtypename"));
                                laws_detail_docnumber.setText(jsonArray.optJSONObject(index).getString("docnumber"));
                                laws_detail_bbdate.setText(jsonArray.optJSONObject(index).getString("bbdate"));
                                laws_detail_effdate.setText(jsonArray.optJSONObject(index).getString("effdate"));
                                laws_detail_item_lrdesc.setText(jsonArray.optJSONObject(index).getString("lrdesc"));

                                for (int i = 0; i < files.length(); i++) {
                                    if (!(files.getJSONObject(i).getString("filename").equals(""))) {
                                        FileBean bean = new FileBean();
                                        bean.setFilename(files.getJSONObject(i).getString("filename"));
                                        String path = PortIpAddress.myUrl + files.getJSONObject(i).getString("fileurl");
                                        path = path.replaceAll("\\\\", "/");
                                        bean.setFileUrl(path);
//                                        bean.setAttachmentid(files.getJSONObject(i).getString("attachmentid"));

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
                                    adapter = new FileListAdapter(LawsDetail.this, mDatas, filenames);
                                    laws_detail_list.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                            } else {
                                ShowToast.showShort(LawsDetail.this, R.string.getInfoErr);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dialog.dismiss();
                        ShowToast.showShort(LawsDetail.this, R.string.connect_err);
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
            ShowToast.showShort(LawsDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(LawsDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(LawsDetail.this, "下载失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }
}
