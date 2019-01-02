package tab.homedetail.accident;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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
import static utils.PortIpAddress.TOKEN_KEY;

public class AddAccident extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_name_right)
    TextView title_name_right;
    //事故单位
    @BindView(R.id.add_accident_presentation_deptname)
    TextView add_accident_presentation_deptname;
    //事故名称
    @BindView(R.id.add_accident_presentation_aititle)
    EditText add_accident_presentation_aititle;
    //事故日期
    @BindView(R.id.add_accident_presentation_aldate)
    TextView add_accident_presentation_aldate;
    //事故分类
    @BindView(R.id.add_accident_presentation_aitypename)
    TextView add_accident_presentation_aitypename;
    private String[] aitypename_arr;
    //事故等级
    @BindView(R.id.add_accident_presentation_ailevelname)
    TextView add_accident_presentation_ailevelname;
    private String[] ailevelname_arr;
    //死亡人数
    @BindView(R.id.add_accident_presentation_swnum)
    EditText add_accident_presentation_swnum;
    //重伤人数
    @BindView(R.id.add_accident_presentation_zsnum)
    EditText add_accident_presentation_zsnum;
    //轻伤人数
    @BindView(R.id.add_accident_presentation_qsnum)
    EditText add_accident_presentation_qsnum;
    //失踪人口
    @BindView(R.id.add_accident_presentation_sznum)
    EditText add_accident_presentation_sznum;
    //事故地区
    @BindView(R.id.add_accident_presentation_areaname)
    TextView add_accident_presentation_areaname;
    //上报人
    @BindView(R.id.add_accident_presentation_sbr)
    EditText add_accident_presentation_sbr;
    @BindView(R.id.add_accident_gridview)
    MyGridView add_accident_gridview;
    private String aidept = "";
    //排查日期第一次点击
    private boolean first = true;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatter;
    private int myYear_accidentpresentation;
    private int myMonth_accidentpresentation;
    private int myDay_accidentpresentation;
    private Map<String, String> arrMap = new HashMap<>();
    private Intent intent;
    //事故单位map
    private Map<String, String> deptname_arr = new HashMap<>();
    private List<String> dept_arr = new ArrayList<>();
    //事故地区map
    private Map<String, String> areaname_map = new HashMap<>();
    private List<String> areaname_arr = new ArrayList<>();

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
    //企业dialog
    private AlertDialog comname_dialog;
    private RecyclerView dialog_item_rv;
    private AlertDialog.Builder builder;
    private EditText search_edittext;
    private ImageView search_clear;
    private AddDailyQyAdapter adapter;
    private List<DeptBean.CellsBean> mDatas;
    private List<DeptBean.CellsBean> searchDatas;
    private DeptBean.CellsBean bean;

    //地区dialog
    private AlertDialog araname_dialog;
    private RecyclerView araname_dialog_rv;
    private AlertDialog.Builder araname_builder;
    private EditText araname_search_edittext;
    private ImageView araname_search_clear;
    private AddDailyQyAdapter araname_adapter;
    private List<DeptBean.CellsBean> araname_mDatas;
    private List<DeptBean.CellsBean> araname_searchDatas;
    private DeptBean.CellsBean araname_bean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accident);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.add_accident_title);
        title_name_right.setBackgroundResource(R.drawable.msubmit);
        CameraUtil.init(this);
        mDatas = new ArrayList<>();
        araname_mDatas = new ArrayList<>();
        if (PortIpAddress.getUserType(this)) {
            getDeptname();
        } else {
            add_accident_presentation_deptname.setText(PortIpAddress.getUserName(this));
            aidept = PortIpAddress.getUserId(this);
        }
        getAreaname();
        add_accident_presentation_sbr.setText(SharedPrefsUtil.getValue(this, "userInfo", "username", null));
        add_accident_presentation_swnum.setText("0");
        add_accident_presentation_zsnum.setText("0");
        add_accident_presentation_qsnum.setText("0");
        add_accident_presentation_sznum.setText("0");
        //初始化下拉内容数据
        aitypename_arr = getResources().getStringArray(R.array.array_aitypename);
        ailevelname_arr = getResources().getStringArray(R.array.array_ailevelname);
        add_accident_presentation_aitypename.setText(aitypename_arr[0]);
        add_accident_presentation_ailevelname.setText(ailevelname_arr[0]);
        imagePaths = new ArrayList<>();
        list = new ArrayList<>();
        //将数据添加至map
        addArrData();
        //设置popwindow
        setPopWindow(add_accident_presentation_aitypename, aitypename_arr);
        setPopWindow(add_accident_presentation_ailevelname, ailevelname_arr);
        //设置初始日期
        String nowdate = DateUtils.getStringDateShort();
        add_accident_presentation_aldate.setText(nowdate);

        //设置gridview一行多少个
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        add_accident_gridview.setNumColumns(cols);
        AddImage();
        imagePaths.add(myCode);
        gridAdapter = new CommonlyGridViewAdapter(this, imagePaths);
        add_accident_gridview.setAdapter(gridAdapter);
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    //将数据添加至map集合
    private void addArrData() {
        arrMap.put(aitypename_arr[0], "SGFL001");
        arrMap.put(aitypename_arr[1], "SGFL002");
        arrMap.put(ailevelname_arr[0], "SGDJ001");
        arrMap.put(ailevelname_arr[1], "SGDJ002");
    }


    private void AddImage() {
        //添加多张图片
        add_accident_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imgs = (String) parent.getItemAtPosition(position);
                clickPosition = position;
                AndPermission.with(AddAccident.this)
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
                if (add_accident_gridview.getCount() >= 7) {
                    ShowToast.showShort(AddAccident.this, "最多添加6张图片");
                } else {
                    OpenCamera();
                }
            } else {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(AddAccident.this);
                imagePaths.remove(imagePaths.size() - 1);
                intent.setCurrentItem(position);
                intent.setPhotoPaths(imagePaths);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        } else {
            ShowToast.showShort(AddAccident.this, "没有SD卡");
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
                cameraFileUri = FileProvider.getUriForFile(AddAccident.this, "demo.yqh.wisdomapp.fileprovider", picture);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_CODE);
            } else {//判断是否有相机应用
                ShowToast.showShort(AddAccident.this, "无相机应用");
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
                ShowToast.showShort(AddAccident.this, "拍照失败，需要开启权限");
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
                    SDUtils.refreshAlbum(AddAccident.this, picture);
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


    @OnClick(R.id.title_name_right)
    void Submit() {
        String str = "";
        if (add_accident_presentation_aititle.getText().toString().equals("")) {
            ShowToast.showShort(this, "请填写事故名称");
        } else {
            try {
                str = URLEncoder.encode(add_accident_presentation_aititle.getText().toString().trim(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            dialog = DialogUtil.createLoadingDialog(this, R.string.loading);

            PostFormBuilder builder = null;
            builder = OkHttpUtils
                    .post()
                    .url(PortIpAddress.AddAccidentdetailinfo())
                    .addParams(TOKEN_KEY, PortIpAddress.GetToken(AddAccident.this))
                    .addParams("aidept", aidept)
                    .addParams("aititle", str)
                    .addParams("aldate", add_accident_presentation_aldate.getText().toString().trim())
                    .addParams("aitypename", arrMap.get(add_accident_presentation_aitypename.getText().toString().trim()))
                    .addParams("ailevelname", arrMap.get(add_accident_presentation_ailevelname.getText().toString().trim()))
                    .addParams("swnum", add_accident_presentation_swnum.getText().toString().trim())
                    .addParams("zsnum", add_accident_presentation_zsnum.getText().toString().trim())
                    .addParams("qsnum", add_accident_presentation_qsnum.getText().toString().trim())
                    .addParams("sznum", add_accident_presentation_sznum.getText().toString().trim())
                    .addParams("areaname", areaname_map.get(add_accident_presentation_areaname.getText().toString()))
                    .addParams("userid", SharedPrefsUtil.getValue(this, "userInfo", "userid", null))
                    .addParams("sgqksm", add_accident_presentation_sbr.getText().toString().trim());

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
                            ShowToast.showShort(AddAccident.this, "添加失败,请检查网络情况");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e(TAG, response + "------");
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("success").equals("true")) {
                                    ShowToast.showShort(AddAccident.this, "添加成功");
                                    dialog.dismiss();
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    ShowToast.showShort(AddAccident.this, "添加失败，请重新添加");
                                    dialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }


    /**
     * 选择事故单位
     */
    @OnClick(R.id.add_accident_presentation_deptname)
    void InitDeptname() {
        if (PortIpAddress.getUserType(this) && comname_dialog != null) {
//            ShowDeptDialog();
            comname_dialog.show();
        }
    }

    private void ShowComnameDialog() {
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_item, null);
        builder.setView(layout);
        builder.setIcon(R.drawable.industry);//设置标题图标
        builder.setTitle("请选择事故单位");//设置标题内容
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
    }

    /**
     * 监听企业搜索框
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
     * 获取事故单位
     */
    private void getDeptname() {
        OkGo.<String>get(PortIpAddress.Sgdw())
                .tag(this)
                .params("userid", SharedPrefsUtil.getValue(this, "userInfo", "userid", null))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    DeptBean.CellsBean bean = new DeptBean.CellsBean();
                                    bean.setCode(jsonArray.optJSONObject(i).getString("code"));
                                    bean.setName(jsonArray.optJSONObject(i).getString("name"));
                                    dept_arr.add(bean.getName());
                                    deptname_arr.put(bean.getName(), bean.getCode());
                                    mDatas.add(bean);
                                }
                                ShowComnameDialog();
                                add_accident_presentation_deptname.setText(dept_arr.get(0));
                                aidept = deptname_arr.get(add_accident_presentation_deptname.getText().toString());

                                //子项点击事件
                                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        bean = (DeptBean.CellsBean) adapter.getData().get(position);
                                        add_accident_presentation_deptname.setText(bean.getName());
                                        aidept = deptname_arr.get(add_accident_presentation_deptname.getText().toString());
                                        comname_dialog.dismiss();
                                    }
                                });

                            } else {
                                ShowToast.showShort(AddAccident.this, R.string.getInfoErr);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(AddAccident.this, R.string.connect_err);
                    }
                });
    }

    /**
     * 选择事故地区
     */
    @OnClick(R.id.add_accident_presentation_areaname)
    void InitAreaname() {
        if (araname_dialog != null) {
//            ShowAranameDialog();
            araname_dialog.show();
        }
    }
//    private void ShowAranameDialog() {
//        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, areaname_arr);
//        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择事故地区").setIcon(R.drawable.industry)
//                .setAdapter(adapter, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        add_accident_presentation_areaname.setText(areaname_arr.get(which));
//                    }
//                }).create();
//        dialog.show();
//    }

    private void ShowAreanameDialog() {
        araname_builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_item, null);
        araname_builder.setView(layout);
        araname_builder.setIcon(R.drawable.industry);//设置标题图标
        araname_builder.setTitle("请选择事故单位");//设置标题内容
        araname_search_edittext = (EditText) layout.findViewById(R.id.search_edittext);
        araname_search_clear = (ImageView) layout.findViewById(R.id.search_clear);
        araname_dialog_rv = (RecyclerView) layout.findViewById(R.id.dialog_item_rv);
        AranameClearEtv();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        araname_dialog_rv.setLayoutManager(llm);
        araname_dialog_rv.setHasFixedSize(true);
        araname_dialog_rv.addItemDecoration(new DividerItemDecoration(this));

        if (araname_adapter == null) {
            araname_adapter = new AddDailyQyAdapter(R.layout.dialog_item_list_item, araname_mDatas);
            araname_adapter.bindToRecyclerView(araname_dialog_rv);
            araname_adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
            araname_adapter.isFirstOnly(true);
            araname_dialog_rv.setAdapter(araname_adapter);
        }

        araname_dialog = araname_builder.create();
        AranameMonitorEditext();
    }

    private void AranameMonitorEditext() {
        araname_search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, count + "----");
                if (araname_mDatas != null) {
                    if (araname_search_edittext.length() > 0) {
                        araname_search_clear.setVisibility(View.VISIBLE);
                        Aranamesearch(araname_search_edittext.getText().toString().trim());
                    } else {
                        araname_search_clear.setVisibility(View.GONE);
                        if (araname_adapter != null) {
                            araname_adapter.setNewData(araname_mDatas);
                        }
                    }
                } else {
                    if (araname_search_edittext.length() > 0) {
                        araname_search_clear.setVisibility(View.VISIBLE);
                    } else {
                        araname_search_clear.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //搜索框
    private void Aranamesearch(String str) {
        if (araname_mDatas != null) {
            araname_searchDatas = new ArrayList<DeptBean.CellsBean>();
            for (DeptBean.CellsBean entity : araname_mDatas) {
                try {
                    if (entity.getName().contains(str)) {
                        araname_searchDatas.add(entity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                araname_adapter.setNewData(araname_searchDatas);
            }
        }
    }


    /**
     * 清空事故地区搜索框
     */
    private void AranameClearEtv() {
        araname_search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                araname_search_edittext.setText("");
                araname_search_clear.setVisibility(View.GONE);
            }
        });
    }


    /**
     * 获取事故地区
     */
    private void getAreaname() {
        OkGo.<String>get(PortIpAddress.Sgdq())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    DeptBean.CellsBean bean = new DeptBean.CellsBean();
                                    bean.setCode(jsonArray.optJSONObject(i).getString("code"));
                                    bean.setName(jsonArray.optJSONObject(i).getString("name"));
                                    areaname_arr.add(bean.getName());
                                    areaname_map.put(bean.getName(), bean.getCode());
                                    araname_mDatas.add(bean);
                                }
                                ShowAreanameDialog();
                                add_accident_presentation_areaname.setText(areaname_arr.get(0));


                                //子项点击事件
                                araname_adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        araname_bean = (DeptBean.CellsBean) adapter.getData().get(position);
                                        add_accident_presentation_areaname.setText(araname_bean.getName());
                                        araname_dialog.dismiss();
                                    }
                                });

                            } else {
                                ShowToast.showShort(AddAccident.this, R.string.getInfoErr);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(AddAccident.this, R.string.connect_err);
                    }
                });
    }


    @OnClick(R.id.add_accident_presentation_aldate)
    void Date() {
        if (first) {
            DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    try {
                        String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = formatter.parse(select_time);
                        String select_date = formatter.format(date);
                        // TODO Auto-generated method stub
                        add_accident_presentation_aldate.setText(select_date);
                        myYear_accidentpresentation = year;
                        myMonth_accidentpresentation = monthOfYear;
                        myDay_accidentpresentation = dayOfMonth;
                        first = false;
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
                        add_accident_presentation_aldate.setText(select_date);
                        myYear_accidentpresentation = year;
                        myMonth_accidentpresentation = monthOfYear;
                        myDay_accidentpresentation = dayOfMonth;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, myYear_accidentpresentation, myMonth_accidentpresentation, myDay_accidentpresentation);
            dialog_date.show();
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
