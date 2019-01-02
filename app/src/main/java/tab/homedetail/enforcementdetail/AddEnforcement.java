package tab.homedetail.enforcementdetail;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import adpter.CommonlyGridViewAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import okhttp3.Call;
import utils.AppUtils;
import utils.BaseActivity;
import utils.DateUtils;
import utils.DialogUtil;
import utils.ImageCompressUtil;
import utils.MyGridView;
import utils.PermissionUtil;
import utils.PortIpAddress;
import utils.SDUtils;
import utils.SharedPrefsUtil;
import utils.ShowToast;
import utils.WaterImage;

public class AddEnforcement extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_name_right)
    TextView title_name_right;
    @BindView(R.id.add_type_bjcdw)
    TextView add_type_bjcdw;
    @BindView(R.id.add_type_address)
    EditText add_type_address;
    @BindView(R.id.add_type_frdb)
    EditText add_type_frdb;
    @BindView(R.id.add_type_zw)
    EditText add_type_zw;
    @BindView(R.id.add_type_jccs)
    EditText add_type_jccs;
    @BindView(R.id.add_type_startdate)
    TextView add_type_startdate;
    @BindView(R.id.add_type_enddate)
    TextView add_type_enddate;
    @BindView(R.id.add_type_zfxzjg)
    EditText add_type_zfxzjg;
    @BindView(R.id.add_type_zbry)
    EditText add_type_zbry;
    @BindView(R.id.add_type_zbryzjh)
    EditText add_type_zbryzjh;
    @BindView(R.id.add_type_xbry)
    EditText add_type_xbry;
    @BindView(R.id.add_type_xbryzjh)
    EditText add_type_xbryzjh;
    @BindView(R.id.add_type_clzt)
    TextView add_type_clzt;

    @BindView(R.id.add_type_radiogroup1)
    RadioGroup add_type_radiogroup1;
    @BindView(R.id.add_type_zgyes)
    RadioButton add_type_zgyes;
    @BindView(R.id.add_type_zgno)
    RadioButton add_type_zgno;
    @BindView(R.id.add_type_radiogroup2)
    RadioGroup add_type_radiogroup2;
    @BindView(R.id.add_type_ybcx)
    RadioButton add_type_ybcx;
    @BindView(R.id.add_type_jycx)
    RadioButton add_type_jycx;
    //    @BindView(R.id.add_type_jcdw)
//    EditText add_type_jcdw;
    @BindView(R.id.add_type_jcms)
    EditText add_type_jcms;
    @BindView(R.id.job_add_gridview)
    MyGridView job_add_gridview;
    @BindView(R.id.add_type_qtqm)
    EditText add_type_qtqm;
    @BindView(R.id.add_type_fzrqm)
    EditText add_type_fzrqm;

    private Map<String, String> add_type_map;

    private Map<String, String> sfzg_map = new HashMap<>();
    private Map<String, String> cffl_map = new HashMap<>();

    private String sfzg = "是";
    private String cffl = "一般程序";

    //默认字符
    private static final String myCode = "000000";
    //选择的图片的集合
    private ArrayList<String> imagePaths;
    private static final int REQUEST_CAMERA_CODE = 10;// 相机
    private static final int REQUEST_PREVIEW_CODE = 20; //预览
    private CommonlyGridViewAdapter gridAdapter;
    //照相名字地址
    private static String IMAGE_FILE_NAME = System.currentTimeMillis() + ".jpg";
    private Uri cameraFileUri;
    private String savePath;
    private File picture;
    //加水印后的图片地址
    private String waterPath;
    private File waterf;
    //当前位置的信息
    private String local;
    //拍照的照片集合
    private ArrayList<String> list;

    //定位需要的声明
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new MyAMapLocationListener();
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private boolean first_start = true; //排查日期第一次点击
    private boolean first_end = true; //治理截止日期第一次点击
    //日期
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatter;
    private String qyname = "";
    private String qyid = "";
    private String imgs;
    private int clickPosition;

    private int yearStartDate;
    private int monthStartDate;
    private int dayStartDate;
    private int yearEndDate;
    private int monthEndDate;
    private int dayEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_enforcement);
        ButterKnife.bind(this);
        initLocal();
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.add);
        title_name_right.setText(R.string.submit);

        if (!PortIpAddress.getUserType(this)) {
            qyname = SharedPrefsUtil.getValue(this, "userInfo", "username", "");
            qyid = SharedPrefsUtil.getValue(this, "userInfo", "userid", "");
            add_type_bjcdw.setText(qyname);
        }
        //设置初始日期
        String nowdate = DateUtils.getStringDateShort();
        add_type_startdate.setText(nowdate);
        add_type_enddate.setText(nowdate);
        initSpinner();

        sfzg_map.put("是", "SF001");
        sfzg_map.put("否", "SF002");
        cffl_map.put("一般程序", "XZCFFL001");
        cffl_map.put("简易程序", "XZCFFL");
        //默认单选选择
        add_type_zgyes.setChecked(true);
        add_type_ybcx.setChecked(true);

        setRadioButton();

        imagePaths = new ArrayList<String>();
        list = new ArrayList<>();

        //设置gridview一行多少个
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        job_add_gridview.setNumColumns(cols);
        AddImage();

        imagePaths.add(myCode);
        gridAdapter = new CommonlyGridViewAdapter(this, imagePaths);
        job_add_gridview.setAdapter(gridAdapter);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    private void initSpinner() {
        //初始化数据集
        add_type_map = new HashMap<>();
        String[] type_arr = getResources().getStringArray(R.array.add_enforcement);
        add_type_clzt.setText(type_arr[0]);
        add_type_map.put(type_arr[0], "XZZFZT001");
        add_type_map.put(type_arr[1], "XZZFZT002");
        add_type_map.put(type_arr[2], "XZZFZT003");
        setPopWindow(add_type_clzt, type_arr);
    }

    private void AddImage() {
        //添加多张图片
        job_add_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imgs = (String) parent.getItemAtPosition(position);
                clickPosition = position;
                AndPermission.with(AddEnforcement.this)
                        .requestCode(200)
                        .permission(
                                // 申请多个权限组方式：
                                PermissionUtil.CameraPermission,
                                PermissionUtil.WriteFilePermission,
                                PermissionUtil.LocationPermission
                        ).send();
            }
        });
    }

    private void ClickAdd(int position) {
        if (SDUtils.hasSdcard()) {
            if (myCode.equals(imgs)) {
                if (job_add_gridview.getCount() >= 7) {
                    ShowToast.showShort(AddEnforcement.this, "最多添加6张图片");
                } else {
                    OpenCamera();
                }
            } else {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(AddEnforcement.this);
                imagePaths.remove(imagePaths.size() - 1);
                intent.setCurrentItem(position);
                intent.setPhotoPaths(imagePaths);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        } else {
            ShowToast.showShort(AddEnforcement.this, "没有SD卡");
        }
    }

    /**
     * 打开相机
     */
    private void OpenCamera() {
        if (AppUtils.Version()) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                createFile();
                picture = new File(savePath, System.currentTimeMillis() + ".jpg");
                cameraFileUri = FileProvider.getUriForFile(AddEnforcement.this, "demo.yqh.wisdomapp.fileprovider", picture);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_CODE);
            } else {//判断是否有相机应用
                ShowToast.showShort(AddEnforcement.this, "无相机应用");
            }
        } else {
            createFile();
            picture = new File(savePath, System.currentTimeMillis() + ".jpg");
            cameraFileUri = Uri.fromFile(picture);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri);
            startActivityForResult(intent, REQUEST_CAMERA_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }

    //权限回调
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == 200) {
                ClickAdd(clickPosition);
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == 200) {
                ShowToast.showShort(AddEnforcement.this, "拍照失败，需要开启权限");
            }
        }
    };


    /**
     * 创建保存图片的文件夹
     */
    private void createFile() {
        createWaterFile();
    }

    private void initLocal() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(false);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    private class MyAMapLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //省信息+城市信息+城区信息+街道信息
            local = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet();
        }
    }

    /**
     * 设置radiobutton
     */
    private void setRadioButton() {
        add_type_radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == add_type_zgyes.getId()) {
                    sfzg = "是";
                } else {
                    sfzg = "否";
                }
            }
        });

        add_type_radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == add_type_ybcx.getId()) {
                    cffl = "一般程序";
                } else {
                    cffl = "简易程序";
                }
            }
        });

    }


    private void mOkhttp() {
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        PostFormBuilder builder = null;
        builder = OkHttpUtils
                .post()
                .url(PortIpAddress.AddXzzf())
                .addParams("access_token", PortIpAddress.GetToken(this))
                .addParams("bean.createid", SharedPrefsUtil.getValue(AddEnforcement.this, "userInfo", "userid", ""))
                .addParams("bean.bjcdw", qyid)
                .addParams("bean.address", add_type_address.getText().toString())
                .addParams("bean.frdb", add_type_frdb.getText().toString())
                .addParams("bean.zw", add_type_zw.getText().toString())
                .addParams("bean.jccs", add_type_jccs.getText().toString())
                .addParams("bean.starttime", add_type_startdate.getText().toString())
                .addParams("bean.endtime", add_type_enddate.getText().toString())
                .addParams("bean.zbjg", add_type_zfxzjg.getText().toString())
                .addParams("bean.zbry", add_type_zbry.getText().toString())
                .addParams("bean.zbjz", add_type_zbryzjh.getText().toString())
                .addParams("bean.xbry", add_type_xbry.getText().toString())
                .addParams("bean.xbzj", add_type_xbryzjh.getText().toString())
                .addParams("bean.remark", add_type_jcms.getText().toString())
                .addParams("bean.sfxyzg", sfzg_map.get(sfzg))
                .addParams("bean.xzcffl", cffl_map.get(cffl))
                .addParams("bean.qtqm", add_type_qtqm.getText().toString())
                .addParams("bean.fzrqm", add_type_fzrqm.getText().toString())
//                .addParams("bean.jcdwmc", add_type_jcdw.getText().toString())
                .addParams("bean.handlingstatus", add_type_map.get(add_type_clzt.getText().toString()));

        //添加图片绝对路径到builder，并约定key“files”作为后台接受多张图片的key
        for (int i = 0; i < imagePaths.size() - 1; i++) {
            File file = new File(imagePaths.get(i));
            builder.addFile("files", file.getName(), file);
        }
        builder
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(AddEnforcement.this, "添加失败,请检查网络情况");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {
                                Message message = handler.obtainMessage();
                                message.what = 1;
                                handler.sendMessage(message);
                            } else {
                                ShowToast.showShort(AddEnforcement.this, "添加失败，请重新添加");
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @OnClick(R.id.add_type_bjcdw)
    void BjcDw() {
        if (SharedPrefsUtil.getValue(this, "userInfo", "usertype", "").equals("1")) {
            Intent intent = new Intent(AddEnforcement.this, SearchQy.class);
            startActivityForResult(intent, 20);
        }
    }

    //开始时间
    @OnClick(R.id.add_type_startdate)
    void StartDate() {
        if (first_start) {
            DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    try {
                        String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = formatter.parse(select_time);
                        String select_date = formatter.format(date);
                        // TODO Auto-generated method stub
                        add_type_startdate.setText(select_date);
                        yearStartDate = year;
                        monthStartDate = monthOfYear;
                        dayStartDate = dayOfMonth;
                        first_start = false;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog_date.show();
        } else {
            DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    try {
                        String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = formatter.parse(select_time);
                        String select_date = formatter.format(date);
                        // TODO Auto-generated method stub
                        add_type_startdate.setText(select_date);
                        yearStartDate = year;
                        monthStartDate = monthOfYear;
                        dayStartDate = dayOfMonth;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, yearStartDate, monthStartDate, dayStartDate);
            dialog_date.show();
        }
    }

    //结束时间
    @OnClick(R.id.add_type_enddate)
    void Enddate() {
        if (first_end) {
            DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    try {
                        String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = formatter.parse(select_time);
                        String select_date = formatter.format(date);
                        // TODO Auto-generated method stub
                        add_type_enddate.setText(select_date);
                        yearEndDate = year;
                        monthEndDate = monthOfYear;
                        dayEndDate = dayOfMonth;
                        first_end = false;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog_date.show();
        } else {
            DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    try {
                        String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = formatter.parse(select_time);
                        String select_date = formatter.format(date);
                        // TODO Auto-generated method stub
                        add_type_enddate.setText(select_date);
                        yearEndDate = year;
                        monthEndDate = monthOfYear;
                        dayEndDate = dayOfMonth;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, yearEndDate, monthEndDate, dayEndDate);
            dialog_date.show();
        }
    }

    //提交
    @OnClick(R.id.title_name_right)
    void Submit() {
        long begintime = DateUtils.getStringToDate(add_type_startdate.getText().toString());
        long endtime = DateUtils.getStringToDate(add_type_enddate.getText().toString());
        if (add_type_bjcdw.getText().toString().trim().equals("")) {
            ShowToast.showShort(AddEnforcement.this, "请选择被检查单位");
        } else if (add_type_jccs.getText().toString().trim().equals("")) {
            ShowToast.showShort(AddEnforcement.this, "请填写检查场所");
        } else if (add_type_zfxzjg.getText().toString().trim().equals("")) {
            ShowToast.showShort(AddEnforcement.this, "请填写执法行政机关");
        } else if (add_type_zbry.getText().toString().trim().equals("")) {
            ShowToast.showShort(AddEnforcement.this, "请填写主办人员");
        } else if (add_type_zbryzjh.getText().toString().trim().equals("")) {
            ShowToast.showShort(AddEnforcement.this, "请填写主办人员证件号");
        }
//        else if (add_type_jcdw.getText().toString().trim().equals("")) {
//            ShowToast.showShort(AddEnforcement.this, "请填写检查单位名称");
//        }
        else if (add_type_jcms.getText().toString().trim().equals("")) {
            ShowToast.showShort(AddEnforcement.this, "请填写检查描述");
        } else if (endtime - begintime < 0) {
            ShowToast.showShort(this, "日期选择有误");
        } else {
            mOkhttp();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, resultCode + "");

        if (resultCode == SearchQy.RESULT_SEARCHQY) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                qyid = bundle.getString("qyid");
                add_type_bjcdw.setText(bundle.getString("qyname"));
            }
        } else if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择拍照
                case REQUEST_CAMERA_CODE:
                    Bitmap bitmap = BitmapFactory.decodeFile(picture.getAbsolutePath());
                    bitmap = saveBitmap(bitmap);

                    Log.e(TAG, "宽：" + bitmap.getWidth() + "----" + "高：" + bitmap.getHeight() + "大小：" + bitmap.getByteCount());

                    //获得旋转角度
                    int degree = WaterImage.getBitmapDegree(picture.getAbsolutePath());
                    //矫正照片被旋转
                    bitmap = WaterImage.rotateBitmapByDegree(bitmap, degree);
                    //添加水印
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String water_time = "" + sdf.format(new Date());

                    bitmap = WaterImage.drawTextToLeftBottom(this, bitmap, "拍摄时间：" + water_time, 5, Color.WHITE, (int) getResources().getDimension(R.dimen.x2), (int) getResources().getDimension(R.dimen.x10));
                    bitmap = WaterImage.drawTextToLeftBottom(this, bitmap, "拍摄地点：" + local, 5, Color.WHITE, (int) getResources().getDimension(R.dimen.x2), (int) getResources().getDimension(R.dimen.x7));
                    bitmap = WaterImage.drawTextToLeftBottom(this, bitmap, "执法人员：" + SharedPrefsUtil.getValue(this, "userInfo", "USER_NAME", ""), 5, Color.WHITE, (int) getResources().getDimension(R.dimen.x2), (int) getResources().getDimension(R.dimen.x4));


                    try {
                        FileOutputStream baos = new FileOutputStream(picture);
                        // 把压缩后的数据存放到baos中  质量压缩并保存一下
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    String path = picture.getAbsolutePath();
                    //保存带水印的图片并添加到集合中展示
                    list.add(path);
                    SDUtils.refreshAlbum(AddEnforcement.this, picture);
                    if (list.contains(myCode)) {
                        list.remove(myCode);
                    }
                    list.add(myCode);
                    imagePaths = list;
                    gridAdapter.DataNotify(imagePaths);

                    if (bitmap != null) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    System.gc();
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    Log.d(TAG, "ListExtra: " + "ListExtra = " + ListExtra.size());
                    loadAdpater(ListExtra);
                    break;
            }
        } else {
            if (data == null) {
                if (!imagePaths.contains(myCode)) {
                    imagePaths.add(myCode);
                }
                gridAdapter.notifyDataSetChanged();
            } else {
                ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                loadAdpater(ListExtra);
            }
        }

    }

    /**
     * 保存照片
     *
     * @param mBitmap
     * @return
     */
    public Bitmap saveBitmap(Bitmap mBitmap) {
        mBitmap = ImageCompressUtil.compressBitmapTest(mBitmap, picture);
        return mBitmap;
    }

    /**
     * 创建加水印后的图片文件夹
     */
    private void createWaterFile() {
        savePath = SDUtils.sdCard + "/GYAJ/";
        File f = new File(savePath);
        if (!f.exists()) {
            f.mkdir();
        }
    }


    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths != null && imagePaths.size() > 0) {
            imagePaths.clear();
        }
        if (paths.contains(myCode)) {
            paths.remove(myCode);
        }
        paths.add(myCode);
        imagePaths.addAll(paths);
        gridAdapter.notifyDataSetChanged();
        try {
            JSONArray obj = new JSONArray(imagePaths);
            Log.e("--", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    setResult(Activity.RESULT_OK);
                    ShowToast.showShort(AddEnforcement.this, "提交成功");
                    finish();
                    break;
                default:
                    break;
            }
        }
    };


}
