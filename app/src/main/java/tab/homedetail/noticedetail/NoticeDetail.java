package tab.homedetail.noticedetail;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.MImageGetter;
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;

public class NoticeDetail extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.noticedetail_tilte)
    TextView noticedetail_tilte;
    @BindView(R.id.noticedetail_time)
    TextView noticedetail_time;
    @BindView(R.id.noticedetail_content)
    TextView noticedetail_content;
    private String messageid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.notice_detail_title);
        getInfo();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }


    private void getInfo() {
        Intent intent = getIntent();
        messageid = intent.getStringExtra("messageid");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mConnect();
    }

    private void mConnect() {
        OkGo.<String>get(PortIpAddress.MessageDetail())
                .tag(this)
                .params("messageid", messageid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e(TAG, response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                noticedetail_tilte.setText(jsonArray.getJSONObject(0).getString("messagetitle"));
                                noticedetail_time.setText(jsonArray.getJSONObject(0).getString("mestime"));

                                noticedetail_content.setText(Html.fromHtml(jsonArray.getJSONObject(0).getString("mescontent"), new MImageGetter(noticedetail_content,getApplicationContext()), null));

                            } else {
                                ShowToast.showShort(NoticeDetail.this, R.string.getInfoErr);
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
                        ShowToast.showShort(NoticeDetail.this, R.string.connect_err);
                    }
                });
    }

}
