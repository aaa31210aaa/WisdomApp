package demo.yqh.wisdomapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import bean.UserInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.DialogUtil;
import utils.JsonCallback;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;
import utils.StatusBarUtils;

import static utils.BaseActivity.isExit;
import static utils.BaseActivity.send_time;

public class Login extends AppCompatActivity {
    private String TAG = "Login";
    @BindView(R.id.accountEtv)
    EditText accountEtv;
    @BindView(R.id.pwdEtv)
    EditText pwdEtv;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.saveAccountCb)
    CheckBox saveAccountCb;
    @BindView(R.id.savePwdCb)
    CheckBox savePwdCb;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtils.transparencyBar(this);
        ButterKnife.bind(this);
        initData();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 延迟发送退出
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            ShowToast.showShort(this, R.string.click_agin);
            // 利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0, send_time);
        } else {
            finish();
            System.exit(0);
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    isExit = false;
                    break;
                case 1:
                    mConnect();
//                    startActivity(new Intent(Login.this, MainActivity.class));
//                    dialog.dismiss();
//                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    protected void initData() {
        loginBtn.setEnabled(false);
        AccountPassEtv();
        SaveAccount();
        SavePwd();
    }

    /**
     * 监听账号密码输入框
     */
    private void AccountPassEtv() {
        accountEtv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NoSpace(accountEtv, s, start);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (accountEtv.getText().toString().length() != 0 && pwdEtv.getText().toString().length() != 0) {
                    loginBtn.setEnabled(true);
                    loginBtn.setBackgroundResource(R.drawable.login_btn_style);
                } else {
                    loginBtn.setEnabled(false);
                }
            }
        });

        pwdEtv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NoSpace(pwdEtv, s, start);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (accountEtv.getText().toString().length() != 0 && pwdEtv.getText().toString().length() != 0) {
                    loginBtn.setEnabled(true);
                    loginBtn.setBackgroundResource(R.drawable.login_btn_style);
                } else {
                    loginBtn.setEnabled(false);
                }
            }
        });
    }

    /**
     * 设置记住账号
     */
    private void SaveAccount() {
        //判断记住账号选框的状态
        if (SharedPrefsUtil.getValue(this, "userInfo", "ISCHECK", false)) {
            //设置默认是记住账号状态
            saveAccountCb.setChecked(true);
            accountEtv.setText(SharedPrefsUtil.getValue(this, "userInfo", "USER_NAME", ""));

            if (accountEtv.getText().toString().trim().equals("")) {
                accountEtv.requestFocus();
            } else {
                pwdEtv.requestFocus();
            }
        }

        //监听保存账号的选择框
        saveAccountCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (saveAccountCb.isChecked()) {
                    SharedPrefsUtil.putValue(Login.this, "userInfo", "ISCHECK", true);
                } else {
                    SharedPrefsUtil.putValue(Login.this, "userInfo", "ISCHECK", false);
                }
            }
        });
    }

    /**
     * 设置记住密码
     */
    private void SavePwd() {
        //判断记住密码选框的状态
        if (SharedPrefsUtil.getValue(this, "userInfo", "PWDISCHECK", false)) {
            savePwdCb.setChecked(true);
            pwdEtv.setText(SharedPrefsUtil.getValue(this, "userInfo", "PWD", ""));

            if (pwdEtv.getText().toString().trim().equals("")) {
                pwdEtv.requestFocus();
            } else {
                accountEtv.requestFocus();
            }
        }

        //监听保存密码的选框
        savePwdCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (savePwdCb.isChecked()) {
                    SharedPrefsUtil.putValue(Login.this, "userInfo", "PWDISCHECK", true);
                } else {
                    SharedPrefsUtil.putValue(Login.this, "userInfo", "PWDISCHECK", false);
                }
            }
        });
    }


    /**
     * 登陆按钮
     */
    @OnClick(R.id.loginBtn)
    void loginClick() {
        dialog = DialogUtil.createLoadingDialog(Login.this, R.string.loading_write);
        //避免过快  没有加载效果
        handler.sendEmptyMessageDelayed(1, 1500);
    }

    /**
     * 调用登陆接口
     */
    private void mConnect() {
        OkGo.<UserInfo>get(PortIpAddress.LoginAddress())
                .tag(this)
                .params("loginname", accountEtv.getText().toString())
                .params("loginpwd", pwdEtv.getText().toString())
                .execute(new JsonCallback<UserInfo>(UserInfo.class) {
                    @Override
                    public void onSuccess(Response<UserInfo> response) {
                        dialog.dismiss();
                        String tag = response.body().getSuccess();
                        String erro = response.body().getErrormessage();
                        String token = response.body().getAccess_token();
                        String username = response.body().getUsername();
//                        String headurl = response.body().getHeadurl();
                        String userid = response.body().getUserid();
                        String deptid = response.body().getDeptid();
                        String usertype = response.body().getUsertype();
                        SharedPrefsUtil.putValue(Login.this, "userInfo", "user_token", token);
                        SharedPrefsUtil.putValue(Login.this, "userInfo", "username", username);
//                      SharedPrefsUtil.putValue(Login.this, "userInfo", "headurl", headurl);
                        SharedPrefsUtil.putValue(Login.this, "userInfo", "userid", userid);
                        //政府端为1，企业端为2
                        SharedPrefsUtil.putValue(Login.this, "userInfo", "usertype", usertype);
                        if (tag.equals(PortIpAddress.SUCCESS_CODE)) {
                            if (SharedPrefsUtil.getValue(Login.this, "userInfo", "ISCHECK", true)) {
                                SharedPrefsUtil.putValue(Login.this, "userInfo", "USER_NAME", accountEtv.getText().toString());
                            }

                            if (SharedPrefsUtil.getValue(Login.this, "userInfo", "PWDISCHECK", true)) {
                                SharedPrefsUtil.putValue(Login.this, "userInfo", "PWD", pwdEtv.getText().toString());
                            }
                            startActivity(new Intent(Login.this, MainActivity.class));
                            dialog.dismiss();
                            finish();
                        } else {
                            ShowToast.showShort(Login.this, erro);
                        }
                    }

                    @Override
                    public void onError(Response<UserInfo> response) {
                        super.onError(response);
                        dialog.dismiss();
                        ShowToast.showShort(Login.this, R.string.connect_err);
                    }

                });
    }


    /**
     * 禁止输入框输入空格
     */
    private void NoSpace(EditText editText, CharSequence str, int start) {
        if (str.toString().contains(" ")) {
            String[] strs = str.toString().split(" ");
            String str1 = "";
            for (int i = 0; i < strs.length; i++) {
                str1 += strs[i];
            }
            editText.setText(str1);
            editText.setSelection(start);
        }
    }

    /**
     * 点击外部隐藏键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
