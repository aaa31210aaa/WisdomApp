package tab.homedetail.hiddendetail;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
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
import demo.yqh.wisdomapp.R;
import okhttp3.Call;
import utils.BaseFragment;
import utils.DialogUtil;
import utils.MessageEvent;
import utils.PortIpAddress;
import utils.SDUtils;
import utils.ShowToast;

import static utils.PermissionUtil.CreateDeleteFilePermission;
import static utils.PermissionUtil.FileCdFilePermission;
import static utils.PermissionUtil.FileRWPermission;
import static utils.PermissionUtil.WriteFilePermission;

/**
 * A simple {@link Fragment} subclass.
 */
public class HiddenFile extends BaseFragment {
    private View view;
    //隐患名称
    @BindView(R.id.qy_yh_information_detail_crname)
    TextView qy_yh_information_detail_crname;
    //隐患类别
    @BindView(R.id.qy_yh_information_detail_crtypename)
    TextView qy_yh_information_detail_crtypename;
    //隐患编号
    @BindView(R.id.qy_yh_information_detail_crcode)
    TextView qy_yh_information_detail_crcode;
    //隐患分类
    @BindView(R.id.qy_yh_information_detail_crlxflname)
    TextView qy_yh_information_detail_crlxflname;
    //排查日期
    @BindView(R.id.qy_yh_information_detail_pcdate)
    TextView qy_yh_information_detail_pcdate;
    //整改类型
    @BindView(R.id.qy_yh_information_detail_zgtypename)
    TextView qy_yh_information_detail_zgtypename;
    //隐患地点
    @BindView(R.id.qy_yh_information_detail_craddr)
    TextView qy_yh_information_detail_craddr;
    //隐患描述
    @BindView(R.id.qy_yh_information_detail_crdesc)
    TextView qy_yh_information_detail_crdesc;
    //报告摘要
    @BindView(R.id.qy_yh_information_detail_crbgzy)
    TextView qy_yh_information_detail_crbgzy;
    //隐患状态
    @BindView(R.id.qy_yh_information_detail_crstatename)
    TextView qy_yh_information_detail_crstatename;
    //治理方案
    @BindView(R.id.qy_yh_information_detail_zyzlfa)
    TextView qy_yh_information_detail_zyzlfa;
    //隐患来源
    @BindView(R.id.qy_yh_information_detail_source)
    TextView qy_yh_information_detail_source;
    //整改责任人
    @BindView(R.id.qy_yh_information_detail_zgman)
    TextView qy_yh_information_detail_zgman;
    //治理截止日
    @BindView(R.id.qy_yh_information_detail_zljzdate)
    TextView qy_yh_information_detail_zljzdate;
    //企业名称
    @BindView(R.id.qy_yh_information_detail_deptname)
    TextView qy_yh_information_detail_deptname;
    //附件名称
//     TextView qy_yh_information_detail_filename;
    //图片
//    @BindView(R.id.qy_yh_information_detail_recycler)
//    RecyclerView qy_yh_information_detail_recycler;
    @BindView(R.id.qy_yh_information_detail_list)
    ListView qy_yh_information_detail_list;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;

    private String yhid;
    //    private QyYhInfoDetailAdapter adapter;
    private List<String> imagePaths;
    private String mycode;
    private String detailid;


    public HiddenFile() {
        // Required empty public constructor
    }

    @Override
    public View makeView() {
        view = View.inflate(getActivity(), R.layout.fragment_hidden_file, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void loadData() {
        //检查权限
        if (ContextCompat.checkSelfPermission(getActivity(), WriteFilePermission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), CreateDeleteFilePermission) != PackageManager.PERMISSION_GRANTED) {
            FileRWPermission(getActivity());
            FileCdFilePermission(getActivity());
        }
        //注册
        EventBus.getDefault().register(this);
        mDatas = new ArrayList<FileBean>();
        filenames = new ArrayList<String>();
        Intent intent = getActivity().getIntent();
        yhid = intent.getStringExtra("yhid");
        mycode = intent.getStringExtra("mcode");
        detailid = "yhid";
        mOkhttp();
    }

    private void mOkhttp() {
        dialog = DialogUtil.createLoadingDialog(getActivity(), R.string.loading);
        OkHttpUtils
                .get()
                .url(PortIpAddress.GetRiskinfodetail())
                .addParams("access_token", PortIpAddress.GetToken(getActivity()))
                .addParams(detailid, yhid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(getActivity(), R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");

                            qy_yh_information_detail_crname.setText(jsonArray.getJSONObject(0).getString("crname"));
                            qy_yh_information_detail_crtypename.setText(jsonArray.getJSONObject(0).getString("crtypename"));
                            qy_yh_information_detail_pcdate.setText(jsonArray.getJSONObject(0).getString("pcdate"));
                            qy_yh_information_detail_crcode.setText(jsonArray.getJSONObject(0).getString("crcode"));
                            qy_yh_information_detail_crlxflname.setText(jsonArray.getJSONObject(0).getString("crlxflname"));
                            qy_yh_information_detail_zgtypename.setText(jsonArray.getJSONObject(0).getString("zgtypename"));
                            qy_yh_information_detail_craddr.setText(jsonArray.getJSONObject(0).getString("craddr"));
                            qy_yh_information_detail_crdesc.setText(jsonArray.getJSONObject(0).getString("crdesc"));
                            qy_yh_information_detail_crbgzy.setText(jsonArray.getJSONObject(0).getString("crbgzy"));
                            qy_yh_information_detail_crstatename.setText(jsonArray.getJSONObject(0).getString("crstatename"));
                            qy_yh_information_detail_zyzlfa.setText(jsonArray.getJSONObject(0).getString("zyzlfa"));
                            qy_yh_information_detail_source.setText(jsonArray.getJSONObject(0).getString("source"));
                            qy_yh_information_detail_zgman.setText(jsonArray.getJSONObject(0).getString("zgman"));
                            qy_yh_information_detail_zljzdate.setText(jsonArray.getJSONObject(0).getString("zljzdate"));
                            qy_yh_information_detail_deptname.setText(jsonArray.getJSONObject(0).getString("deptname"));
//                          qy_yh_information_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));

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
                                adapter = new FileListAdapter(getActivity(), mDatas, filenames);
                                qy_yh_information_detail_list.setAdapter(adapter);
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
            ShowToast.showToastNowait(getActivity(), "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showToastNowait(getActivity(), "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showToastNowait(getActivity(), "下载失败");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }
}
