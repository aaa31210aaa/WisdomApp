package tab.homedetail.hiddendetail;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adpter.CommonlyGridViewAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import okhttp3.Call;
import utils.AppUtils;
import utils.BaseActivity;
import utils.CameraUtil;
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

import static utils.PermissionUtil.CAMERA_REQUESTCODE;

public class AddWorkInfo extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_name_right)
    TextView title_name_right;

    @BindView(R.id.add_workinfo_jcr)
    TextView add_workinfo_jcr;
    @BindView(R.id.add_workinfo_date)
    TextView add_workinfo_date;
    @BindView(R.id.add_workinfo_jcfxwt)
    EditText add_workinfo_jcfxwt;
    @BindView(R.id.add_workinfo_wtcljy)
    EditText add_workinfo_wtcljy;
    @BindView(R.id.add_workinfo_memo)
    EditText add_workinfo_memo;
    @BindView(R.id.add_workinfo_gridview)
    MyGridView add_workinfo_gridview;
    private String userid;
    private String usertype;
    private String jcr;
    private boolean first_click = true;
    //日期
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatter;
    private int myYear;
    private int myMonth;
    private int myDay;
    private Intent intent;


    private static final int REQUEST_CAMERA_CODE = 10;// 相机
    private static final int REQUEST_PREVIEW_CODE = 20; //预览
    //返回码
    private ImageView water_test_image;
    //选择的图片的集合
    private ArrayList<String> imagePaths;
    //拍照的照片集合
    private ArrayList<String> list;
    private CommonlyGridViewAdapter gridAdapter;
    //照相名字地址
    private static String IMAGE_FILE_NAME = System.currentTimeMillis() + ".jpg";
    private Uri cameraFileUri;
    private String savePath;
    private File picture;
    //加水印后的图片地址
    private String waterPath;
    private File waterf;
    //定位需要的声明
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new MyAMapLocationListener();
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //当前位置的信息
    private String local;
    //默认字符
    private static final String myCode = "000000";
    private String imgs;
    private int clickPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_work_info);
        ButterKnife.bind(this);
        initLocal();
        initData();
    }

    @Override
    protected void initData() {
        CameraUtil.init(this);
        imagePaths = new ArrayList<>();
        list = new ArrayList<>();
        Intent intent = getIntent();
        userid = SharedPrefsUtil.getValue(this, "userInfo", "userid", "");
        usertype = intent.getStringExtra("usertype");
        jcr = intent.getStringExtra("jcr");
        title_name.setText("新增" + intent.getStringExtra("title_name"));
        title_name_right.setText(R.string.add);
        add_workinfo_jcr.setText(jcr);
        //设置初始日期
        String nowdate = DateUtils.getStringDateShort();
        add_workinfo_date.setText(nowdate);

        //设置gridview一行多少个
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        add_workinfo_gridview.setNumColumns(cols);
        AddImage();
        imagePaths.add(myCode);
        gridAdapter = new CommonlyGridViewAdapter(this, imagePaths);
        add_workinfo_gridview.setAdapter(gridAdapter);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }


    private void AddImage() {
        //添加多张图片
        add_workinfo_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imgs = (String) parent.getItemAtPosition(position);
                clickPosition = position;
                AndPermission.with(AddWorkInfo.this)
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
                if (add_workinfo_gridview.getCount() >= 7) {
                    ShowToast.showShort(AddWorkInfo.this, "最多添加6张图片");
                } else {
                    OpenCamera();
                }
            } else {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(AddWorkInfo.this);
                imagePaths.remove(imagePaths.size() - 1);
                intent.setCurrentItem(position);
                intent.setPhotoPaths(imagePaths);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        } else {
            ShowToast.showShort(AddWorkInfo.this, "没有SD卡");
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
                cameraFileUri = FileProvider.getUriForFile(AddWorkInfo.this, "demo.yqh.wisdomapp.fileprovider", picture);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_CODE);
            } else {//判断是否有相机应用
                ShowToast.showShort(AddWorkInfo.this, "无相机应用");
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
            if (requestCode == CAMERA_REQUESTCODE) {
                ShowToast.showShort(AddWorkInfo.this, "拍照失败，需要开启相机权限");
            }
        }
    };

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
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
                    SDUtils.refreshAlbum(AddWorkInfo.this, picture);
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
                    if (data != null) {
                        ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                        Log.d(TAG, "ListExtra: " + "ListExtra = " + ListExtra.size());
                        loadAdpater(ListExtra);
//                        if (!ListExtra.contains(myCode)) {
//                            ListExtra.add(myCode);
//                        }
//                        imagePaths = ListExtra;
//                        gridAdapter.DataNotify(imagePaths);
                    }
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
                if (!ListExtra.contains(myCode)) {
                    ListExtra.add(myCode);
                }
                imagePaths = ListExtra;
                gridAdapter.DataNotify(imagePaths);
            }
        }
    }

    //加载图片
    private void loadAdpater(List<String> paths) {
        if (imagePaths != null && imagePaths.size() > 0) {
            imagePaths.clear();
        }
        if (imagePaths.contains(myCode)) {
            imagePaths.remove(myCode);
        }
        imagePaths.addAll(paths);
        imagePaths.add(myCode);
        gridAdapter.DataNotify(imagePaths);
//        gridAdapter.notifyDataSetChanged();
//      upLoadImage();
        try {
            JSONArray obj = new JSONArray(imagePaths);
            Log.e("--", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
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

    /**
     * 创建保存图片的文件夹
     */
    private void createFile() {
        createWaterFile();
    }


    @OnClick(R.id.title_name_right)
    void Submit() {
        if (add_workinfo_jcfxwt.getText().toString().equals("")) {
            ShowToast.showShort(this, "请填写检查情况");
        } else if (add_workinfo_wtcljy.getText().toString().equals("")) {
            ShowToast.showShort(this, "请填写处理措施");
        } else {
            dialog = DialogUtil.createLoadingDialog(this, R.string.submitting);
            addFile();
        }
    }

    private void addFile() {
        PostFormBuilder builder = null;
        try {
            builder = OkHttpUtils
                    .post()
                    .url(PortIpAddress.SaveInfo())
                    .addParams("inminecadrescheckbean.userid", userid)
                    .addParams("inminecadrescheckbean.usertype", usertype)
                    .addParams("inminecadrescheckbean.jcr", jcr)
                    .addParams("inminecadrescheckbean.jcsj", add_workinfo_date.getText().toString())
                    .addParams("inminecadrescheckbean.jcfxwt", URLEncoder.encode(add_workinfo_jcfxwt.getText().toString(), "UTF-8"))
                    .addParams("inminecadrescheckbean.wtcljy", URLEncoder.encode(add_workinfo_wtcljy.getText().toString(), "UTF-8"))
                    .addParams("inminecadrescheckbean.memo", URLEncoder.encode(add_workinfo_memo.getText().toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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
                        ShowToast.showShort(AddWorkInfo.this, "添加失败,请检查网络情况");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response + "------");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {
                                ShowToast.showShort(AddWorkInfo.this, "添加成功");
                                dialog.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                ShowToast.showShort(AddWorkInfo.this, "添加失败，请重新添加");
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private class MyAMapLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //省信息+城市信息+城区信息+街道信息
            local = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet();
        }
    }

    @OnClick(R.id.add_workinfo_date)
    void Date() {
        ShowDateDialog();
    }

    private void ShowDateDialog() {
        if (first_click) {
            DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    try {
                        String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = formatter.parse(select_time);
                        String select_date = formatter.format(date);
                        // TODO Auto-generated method stub
                        add_workinfo_date.setText(select_date);
                        myYear = year;
                        myMonth = monthOfYear;
                        myDay = dayOfMonth;
                        first_click = false;
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
                        add_workinfo_date.setText(select_date);
                        myYear = year;
                        myMonth = monthOfYear;
                        myDay = dayOfMonth;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, myYear, myMonth, myDay);
            dialog_date.show();
        }
    }
}
