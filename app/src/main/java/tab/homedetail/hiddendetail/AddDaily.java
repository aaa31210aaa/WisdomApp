package tab.homedetail.hiddendetail;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adpter.AddDailyQyAdapter;
import adpter.CommonlyGridViewAdapter;
import bean.DeptBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import okhttp3.Call;
import okhttp3.MediaType;
import utils.AppUtils;
import utils.BaseActivity;
import utils.CameraUtil;
import utils.DateUtils;
import utils.DialogUtil;
import utils.DividerItemDecoration;
import utils.ImageCompressUtil;
import utils.MyGridView;
import utils.PermissionUtil;
import utils.PortIpAddress;
import utils.SDUtils;
import utils.SharedPrefsUtil;
import utils.ShowToast;
import utils.WaterImage;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;

public class AddDaily extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_name_right)
    TextView title_name_right;

    @BindView(R.id.add_daily_comname)
    TextView add_daily_comname;
    private List<String> comname_arr;
    private Map<String, String> comname_map;
    @BindView(R.id.add_daily_jcr)
    TextView add_daily_jcr;
    @BindView(R.id.add_daily_jcsj)
    TextView add_daily_jcsj;
    @BindView(R.id.add_daily_zljzsj)
    TextView add_daily_zljzsj;
    @BindView(R.id.add_daily_yhzt)
    TextView add_daily_yhzt;
    private List<String> yhzt_arr;
    private Map<String, String> yhzt_map;
    @BindView(R.id.add_daily_jcfxwt)
    EditText add_daily_jcfxwt;
    @BindView(R.id.add_daily_clcs)
    EditText add_daily_clcs;
    @BindView(R.id.add_daily_gridview)
    MyGridView add_daily_gridview;
    private String chargedept;

    //日期
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatter;

    private int myYear_investigation;
    private int myMonth_investigation;
    private int myDay_investigation;
    private int myYear_deadline;
    private int myMonth_deadline;
    private int myDay_deadline;

    private boolean first_investigation = true; //排查日期第一次点击
    private boolean first_deadline = true; //治理截止日期第一次点击
    private static final int REQUEST_CAMERA_CODE = 10;// 相机
    private static final int REQUEST_PREVIEW_CODE = 20; //预览
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
    //参数类型
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final String IMGUR_CLIENT_ID = "...";
//    private final OkHttpClient client = new OkHttpClient();

    //临时保存的文字和图片
    public static final int TEMPORARY_CODE = 30;

    public static List<String[]> listTexts = new ArrayList<>();
    public static List<List<String>> listImages = new ArrayList<>();
    private String[] temporaryTexts;
    private String imgs;
    private int clickPosition;
    private String createid = "";
    private List<DeptBean.CellsBean> mDatas;
    private List<DeptBean.CellsBean> searchDatas;
    private AddDailyQyAdapter adapter;
    private RecyclerView dialog_item_rv;
    private AlertDialog.Builder builder;
    private EditText search_edittext;
    private ImageView search_clear;
    private AlertDialog comname_dialog;
    private DeptBean.CellsBean bean;
    private String riskinfoid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.add_daily_regulation);
        title_name_right.setText(R.string.submit);
        CameraUtil.init(this);
        Intent intent = getIntent();
        chargedept = intent.getStringExtra("chargedept");
        imagePaths = new ArrayList<>();
        list = new ArrayList<>();
        riskinfoid = intent.getStringExtra("riskinfoid");
        createid = SharedPrefsUtil.getValue(this, "userInfo", "userid", "");
        add_daily_jcr.setText(SharedPrefsUtil.getValue(this, "userInfo", "username", ""));
        //设置初始日期
        String nowdate = DateUtils.getStringDateShort();
        add_daily_jcsj.setText(nowdate);
        add_daily_zljzsj.setText(nowdate);

        comname_arr = new ArrayList<>();
        comname_map = new HashMap<>();
        yhzt_arr = new ArrayList<>();
        yhzt_map = new HashMap<>();
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        getComname(chargedept);
        getYhzt();

        //设置gridview一行多少个
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        add_daily_gridview.setNumColumns(cols);
        AddImage();
        imagePaths.add(myCode);
        gridAdapter = new CommonlyGridViewAdapter(this, imagePaths);
        add_daily_gridview.setAdapter(gridAdapter);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    private void AddImage() {
        //添加多张图片
        add_daily_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imgs = (String) parent.getItemAtPosition(position);
                clickPosition = position;
                AndPermission.with(AddDaily.this)
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
                if (add_daily_gridview.getCount() >= 7) {
                    ShowToast.showShort(AddDaily.this, "最多添加6张图片");
                } else {
                    OpenCamera();
                }
            } else {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(AddDaily.this);
                imagePaths.remove(imagePaths.size() - 1);
                intent.setCurrentItem(position);
                intent.setPhotoPaths(imagePaths);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        } else {
            ShowToast.showShort(AddDaily.this, "没有SD卡");
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
                cameraFileUri = FileProvider.getUriForFile(AddDaily.this, "demo.yqh.wisdomapp.fileprovider", picture);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_CODE);
            } else {//判断是否有相机应用
                ShowToast.showShort(AddDaily.this, "无相机应用");
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
                initLocal();
                ClickAdd(clickPosition);
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == 200) {
                ShowToast.showShort(AddDaily.this, "拍照失败，需要开启权限");
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
                    SDUtils.refreshAlbum(AddDaily.this, picture);
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
                        break;
                    }
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
        savePath = SDUtils.sdPath + "/GYAJ/";
        File f = new File(savePath);
        if (!f.exists()) {
            f.mkdir();
        }
    }


    @OnClick(R.id.title_name_right)
    void Submit() {
        long begintime = DateUtils.getStringToDate(add_daily_jcsj.getText().toString());
        long endtime = DateUtils.getStringToDate(add_daily_zljzsj.getText().toString());
        if (endtime - begintime < 0) {
            ShowToast.showShort(this, "截止时间不能早于检查时间");
        } else {
            dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
            addFile();
        }
    }

    private void addFile() {
        PostFormBuilder builder = null;
        try {
            builder = OkHttpUtils
                    .post()
                    .url(PortIpAddress.DailySaveInfo())
                    .addParams("checkRecordbean.riskinfoid", riskinfoid)
                    .addParams("checkRecordbean.qyid", comname_map.get(add_daily_comname.getText().toString()))
                    .addParams("checkRecordbean.createid", createid)
                    .addParams("checkRecordbean.jcr", URLEncoder.encode(add_daily_jcr.getText().toString(), "UTF-8"))
                    .addParams("checkRecordbean.jcsj", add_daily_jcsj.getText().toString())
                    .addParams("checkRecordbean.zljzsj", add_daily_zljzsj.getText().toString())
                    .addParams("checkRecordbean.yhzt", yhzt_map.get(add_daily_yhzt.getText().toString()))
                    .addParams("checkRecordbean.memo", URLEncoder.encode(add_daily_clcs.getText().toString(), "UTF-8"))
                    .addParams("checkRecordbean.ly", URLEncoder.encode(add_daily_jcfxwt.getText().toString(), "UTF-8"));
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
                        ShowToast.showShort(AddDaily.this, "添加失败,请检查网络情况");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response + "------");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {
                                ShowToast.showShort(AddDaily.this, "添加成功");
                                dialog.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                ShowToast.showShort(AddDaily.this, "添加失败，请重新添加");
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 选择企业
     */
    @OnClick(R.id.add_daily_comname)
    void Comname() {
        if (comname_dialog != null)
            comname_dialog.show();
    }

    private void ShowComnameDialog() {
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_item, null);
        builder.setView(layout);
        builder.setIcon(R.drawable.industry);//设置标题图标
        builder.setTitle("请选择企业");//设置标题内容
        search_edittext = (EditText) layout.findViewById(R.id.search_edittext);
        search_clear = (ImageView) layout.findViewById(R.id.search_clear);
        dialog_item_rv = (RecyclerView) layout.findViewById(R.id.dialog_item_rv);
        ClearEtv();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        dialog_item_rv.setLayoutManager(llm);
        dialog_item_rv.setHasFixedSize(true);
        dialog_item_rv.addItemDecoration(new DividerItemDecoration(this));

        if (adapter == null) {
            adapter = new AddDailyQyAdapter(R.layout.dialog_item_list_item, mDatas);
            adapter.bindToRecyclerView(dialog_item_rv);
            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
            adapter.isFirstOnly(true);
            dialog_item_rv.setAdapter(adapter);
        }

        comname_dialog = builder.create();
        MonitorEditext();
//        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.dialog_item, comname_arr);
//        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择企业").setIcon(R.drawable.industry)
//                .setAdapter(adapter, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        add_daily_comname.setText(comname_arr.get(which));
//                    }
//                }).create();
//        dialog.show();
    }

    /**
     * 获取企业数据
     */
    private void getComname(String chargedept) {
        OkGo.<String>get(PortIpAddress.SelectQyList())
                .tag(TAG)
                .params("companyBean.chargedept", chargedept)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                if (jsonArray.length() > 0) {
                                    mDatas = new ArrayList<>();
                                    comname_arr.clear();
                                    comname_map.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        bean = new DeptBean.CellsBean();
                                        bean.setCode(jsonArray.optJSONObject(i).getString("code"));
                                        bean.setName(jsonArray.optJSONObject(i).getString("name"));
                                        comname_arr.add(bean.getName());
                                        comname_map.put(bean.getName(), bean.getCode());
                                        mDatas.add(bean);
                                    }
                                    ShowComnameDialog();
                                    add_daily_comname.setText(comname_arr.get(0));

                                    adapter.setNewData(mDatas);

                                    //如果无数据设置空布局
                                    if (mDatas.size() == 0) {
                                        adapter.setEmptyView(R.layout.nodata_layout, (ViewGroup) dialog_item_rv.getParent());
                                    }

                                    //子项点击事件
                                    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                            bean = (DeptBean.CellsBean) adapter.getData().get(position);
                                            add_daily_comname.setText(bean.getName());
                                            comname_dialog.dismiss();
                                        }
                                    });

                                }
                            } else {
                                ShowToast.showShort(AddDaily.this, R.string.getInfoErr);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dialog.dismiss();
                        ShowToast.showShort(AddDaily.this, R.string.connect_err);
                    }

                });
    }

    /**
     * 监听搜索框
     */
    private void MonitorEditext() {
        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, count + "----");
                if (mDatas != null) {
                    if (search_edittext.length() > 0) {
                        search_clear.setVisibility(View.VISIBLE);
                        search(search_edittext.getText().toString().trim());
                    } else {
                        search_clear.setVisibility(View.GONE);
                        if (adapter != null) {
                            adapter.setNewData(mDatas);
                        }
                    }
                } else {
                    if (search_edittext.length() > 0) {
                        search_clear.setVisibility(View.VISIBLE);
                    } else {
                        search_clear.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //搜索框
    private void search(String str) {
        if (mDatas != null) {
            searchDatas = new ArrayList<DeptBean.CellsBean>();
            for (DeptBean.CellsBean entity : mDatas) {
                try {
                    if (entity.getName().contains(str)) {
                        searchDatas.add(entity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.setNewData(searchDatas);
            }
        }
    }

    /**
     * 清空搜索框
     */
    private void ClearEtv() {
        search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_edittext.setText("");
                search_clear.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 选择隐患状态
     */
    @OnClick(R.id.add_daily_yhzt)
    void Yhzt() {
        ShowYhztDialog();
    }

    private void ShowYhztDialog() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, yhzt_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择隐患状态").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        add_daily_yhzt.setText(yhzt_arr.get(which));
                    }
                }).create();
        dialog.show();
    }

    /**
     * 获取隐患状态信息
     */
    private void getYhzt() {
        OkGo.<String>get(PortIpAddress.SelectYhList())
                .tag(TAG)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                if (jsonArray.length() > 0) {
                                    yhzt_arr.clear();
                                    yhzt_map.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        DeptBean.CellsBean bean = new DeptBean.CellsBean();
                                        bean.setCode(jsonArray.optJSONObject(i).getString("code"));
                                        bean.setName(jsonArray.optJSONObject(i).getString("name"));
                                        yhzt_arr.add(bean.getName());
                                        yhzt_map.put(bean.getName(), bean.getCode());
                                    }
                                    add_daily_yhzt.setText(yhzt_arr.get(0));
                                }
                            } else {
                                ShowToast.showShort(AddDaily.this, R.string.getInfoErr);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dialog.dismiss();
                        ShowToast.showShort(AddDaily.this, R.string.connect_err);
                    }
                });
    }


    @OnClick(R.id.add_daily_jcsj)
    void Jcsj() {
        if (first_investigation) {
            DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    try {
                        String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = formatter.parse(select_time);
                        String select_date = formatter.format(date);
                        // TODO Auto-generated method stub
                        add_daily_jcsj.setText(select_date);
                        myYear_investigation = year;
                        myMonth_investigation = monthOfYear;
                        myDay_investigation = dayOfMonth;
                        first_investigation = false;
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
                        add_daily_jcsj.setText(select_date);
                        myYear_investigation = year;
                        myMonth_investigation = monthOfYear;
                        myDay_investigation = dayOfMonth;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, myYear_investigation, myMonth_investigation, myDay_investigation);
            dialog_date.show();
        }
    }


    @OnClick(R.id.add_daily_zljzsj)
    void Zljzsj() {
        if (first_deadline) {
            DatePickerDialog dialog_deadline = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    try {
                        String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = formatter.parse(select_time);
                        String select_date = formatter.format(date);
                        // TODO Auto-generated method stub
                        add_daily_zljzsj.setText(select_date);
                        myYear_deadline = year;
                        myMonth_deadline = monthOfYear;
                        myDay_deadline = dayOfMonth;
                        first_deadline = false;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog_deadline.show();
        } else {
            DatePickerDialog dialog_deadline = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    try {
                        String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = formatter.parse(select_time);
                        String select_date = formatter.format(date);
                        // TODO Auto-generated method stub
                        add_daily_zljzsj.setText(select_date);
                        myYear_deadline = year;
                        myMonth_deadline = monthOfYear;
                        myDay_deadline = dayOfMonth;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, myYear_deadline, myMonth_deadline, myDay_deadline);
            dialog_deadline.show();
        }
    }


    private class MyAMapLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //省信息+城市信息+城区信息+街道信息
            local = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet();
        }

    }


    /**
     * 创建保存图片的文件夹
     */
    private void createFile() {
        createWaterFile();
    }
}
