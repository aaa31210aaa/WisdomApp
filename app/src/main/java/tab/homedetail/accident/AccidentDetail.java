package tab.homedetail.accident;

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
import utils.SharedPrefsUtil;
import utils.ShowToast;

import static utils.PermissionUtil.CreateDeleteFilePermission;
import static utils.PermissionUtil.FileCdFilePermission;
import static utils.PermissionUtil.FileRWPermission;
import static utils.PermissionUtil.WriteFilePermission;
import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;
import static utils.PortIpAddress.TOKEN_KEY;

public class AccidentDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.accident_detail_deptname)
    TextView accident_detail_deptname;
    @BindView(R.id.accident_detail_aititle)
    TextView accident_detail_aititle;
    @BindView(R.id.accident_detail_aldate)
    TextView accident_detail_aldate;
    @BindView(R.id.accident_detail_aitypename)
    TextView accident_detail_aitypename;
    @BindView(R.id.accident_detail_ailevelname)
    TextView accident_detail_ailevelname;
    @BindView(R.id.accident_detail_swnum)
    TextView accident_detail_swnum;
    @BindView(R.id.accident_detail_zsnum)
    TextView accident_detail_zsnum;
    @BindView(R.id.accident_detail_qsnum)
    TextView accident_detail_qsnum;
    @BindView(R.id.accident_detail_sznum)
    TextView accident_detail_sznum;
    @BindView(R.id.accident_detail_areaname)
    TextView accident_detail_areaname;
    @BindView(R.id.accident_detail_sbr)
    TextView accident_detail_sbr;
    @BindView(R.id.accident_content_detail)
    TextView accident_content_detail;
    @BindView(R.id.accident_detail_list)
    ListView accident_detail_list;
    private String procid;
    private int index = 0;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.accident_detail_title);
        Intent intent = getIntent();
        procid = intent.getStringExtra("procid");
        filenames = new ArrayList<>();
        mDatas = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(context, WriteFilePermission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, CreateDeleteFilePermission) != PackageManager.PERMISSION_GRANTED) {
            FileRWPermission(this);
            FileCdFilePermission(this);
        }
        //注册
        EventBus.getDefault().register(this);
        mConnect();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    private void mConnect() {
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        OkGo.<String>get(PortIpAddress.Accidentdetailinfo())
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.GetToken(this))
                .params("procid", procid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");

                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                accident_detail_deptname.setText(jsonArray.getJSONObject(index).getString("aideptname"));
                                accident_detail_aititle.setText(jsonArray.getJSONObject(index).getString("aititle"));
                                accident_detail_aldate.setText(jsonArray.getJSONObject(index).getString("aldate"));
                                accident_detail_aitypename.setText(jsonArray.getJSONObject(index).getString("aitypename"));
                                accident_detail_ailevelname.setText(jsonArray.getJSONObject(index).getString("ailevelname"));
                                accident_detail_swnum.setText(jsonArray.getJSONObject(index).getString("swnum"));
                                accident_detail_zsnum.setText(jsonArray.getJSONObject(index).getString("zsnum"));
                                accident_detail_qsnum.setText(jsonArray.getJSONObject(index).getString("qsnum"));
                                accident_detail_sznum.setText(jsonArray.getJSONObject(index).getString("sznum"));
                                accident_detail_areaname.setText(jsonArray.getJSONObject(index).getString("areaname"));
                                accident_detail_sbr.setText(SharedPrefsUtil.getValue(AccidentDetail.this, "userInfo", "username", ""));
                                accident_content_detail.setText(jsonArray.getJSONObject(index).getString("sgqksm"));

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
                                    adapter = new FileListAdapter(AccidentDetail.this, mDatas, filenames);
                                    accident_detail_list.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                                dialog.dismiss();
                            } else {
                                ShowToast.showShort(AccidentDetail.this, R.string.getInfoErr);
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
                        ShowToast.showShort(AccidentDetail.this, R.string.connect_err);
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
            ShowToast.showShort(AccidentDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(AccidentDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(AccidentDetail.this, "下载失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }
}
