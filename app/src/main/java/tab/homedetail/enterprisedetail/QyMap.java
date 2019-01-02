package tab.homedetail.enterprisedetail;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.AMapUtil;
import utils.BaseActivity;
import utils.ShowToast;

import static utils.PermissionUtil.LOCATION_REQUESTCODE;
import static utils.PermissionUtil.LocationPermission;

public class QyMap extends BaseActivity implements
        LocationSource
        , AMapLocationListener
        , GeocodeSearch.OnGeocodeSearchListener
        , OnMarkerClickListener
        , OnMapClickListener
        , InfoWindowAdapter {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.qy_map_imagebtn)
    ImageButton qy_map_imagebtn;
    //地图信息
    @BindView(R.id.enterprise_map)
    MapView enterprise_map;
    private AMap aMap;
    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    //构造GeocodeSearch对象
    private GeocodeSearch geocodeSearch;
    //定位标识
    private boolean dw = false;
    private ArrayList<BitmapDescriptor> icon_arr;
    //地图UI设置
    private UiSettings mUiSettings;
    //传过来的企业的经纬度
    private double lat;
    private double lng;
    private Intent intent;
    private GeocodeSearch geocoderSearch;
    //编码后的经纬度
    private LatLonPoint myLatLng;
    //判断哪个页面传过来的
//    private String myymindex;
    //接收企业名字
    private String qyname;
    //保存当前marker
    private Marker currMarker;
    private String tag = "";
    //给没有经纬度的企业单位提交经纬度按钮
//    @BindView(R.id.submit_latlng)
//    Button submit_latlng;


//    @BindView(R.id.map_latlng_tv)
//    TextView map_latlng_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qy_map);
        ButterKnife.bind(this);
        enterprise_map.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, LocationPermission) !=
                PackageManager.PERMISSION_GRANTED) {
            LocationPermission(this);
        } else {
            initData();
            setOnClick();
        }
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, paramArrayOfInt, listener);
    }

    //权限回调
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == LOCATION_REQUESTCODE) {
                initData();
                setOnClick();
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == LOCATION_REQUESTCODE) {
                ShowToast.showShort(QyMap.this, "定位失败,需开启定位权限");
//              finish();
            }
        }
    };


    @Override
    protected void initData() {
        title_name.setText(R.string.qy_map_title);
        Intent intent = getIntent();
        tag = intent.getStringExtra("tag");
        getData();
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    private void getData() {
        //添加定位图片集合
        icon_arr = new ArrayList<BitmapDescriptor>();
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));

        intent = getIntent();
        qyname = intent.getStringExtra("qyname");

        lat = intent.getDoubleExtra("myqyLat", 0);
        lng = intent.getDoubleExtra("myqyLng", 0);
        initAmap();
    }


    @OnClick(R.id.qy_map_imagebtn)
    void DwBtn() {
        dw = true;
        mLocationClient.startLocation();
    }

    /**
     * 给没有经纬度的企业单位提交经纬度
     */
//    @OnClick(R.id.submit_latlng)
//    void SubmitLatLng(){
//        //调用接口
//    }
    protected void setOnClick() {
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setInfoWindowAdapter(this);
    }

    /**
     * 初始化地图
     */
    private void initAmap() {
        aMap = enterprise_map.getMap();
        mUiSettings = aMap.getUiSettings();
        //开启比例尺
        mUiSettings.setScaleControlsEnabled(true);
        //开启指南针
        mUiSettings.setCompassEnabled(true);
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        //开启室内图
        aMap.showIndoorMap(true);
    }

    /**
     * 激活定位
     * 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
     * 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
     * 在定位结束后，在合适的生命周期调用onDestroy()方法
     * 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //获取一次定位结果
            mLocationOption.setOnceLocation(true);
            //启动定位
            mLocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    //在指定位置添加标记
    private void AddMarker(double dlat, double dlng, String title) {
        LatLng pos = new LatLng(dlat, dlng);
        LatLng realLatLng = AMapUtil.convert(this, pos, "BAIDU");
        CameraUpdate cu = CameraUpdateFactory.changeLatLng(realLatLng);
        aMap.moveCamera(cu);

        //绘制marker
        aMap.addMarker(new MarkerOptions()
                .position(realLatLng)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.dingwei)))
                .draggable(true));
    }

    //只加marker图标
    private void AddMarkerImage(LatLng latLng, String str) {
        LatLng realLatLng = AMapUtil.convert(this, latLng, "BAIDU");
        //绘制marker
        aMap.addMarker(new MarkerOptions()
                .position(realLatLng)
                .title("企业名称：")
                .snippet(str)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.dingwei)))
        );
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                //显示系统小蓝点
                mListener.onLocationChanged(aMapLocation);
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码

                if (dw) {
                    aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    mLocationClient.stopLocation();
                }

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    aMap.addMarker(getMarkerOptions(aMapLocation, aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                    AddMarker(lat, lng, qyname);
//                    if (tag.equals("ed")){
//
//                    }else{
//                        submit_latlng.setVisibility(View.VISIBLE);
//                        LatLng pos = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
//                        LatLng realLatLng = AMapUtil.convert(this, pos, "BAIDU");
//                        CameraUpdate cu = CameraUpdateFactory.changeLatLng(realLatLng);
//                        aMap.moveCamera(cu);
//                    }

                    //只定位一次
                    isFirstLoc = false;
                    mLocationClient.stopLocation();
                }
            } else {
                String errText = "定位失败，" + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr:", errText);
                ShowToast.showToastNowait(this, errText);
            }
        }
    }

    /**
     * 自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
     *
     * @param amapLocation
     * @return
     */
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation, double lat, double dlng) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //位置
        options.position(new LatLng(lat, dlng));
        //设置图标
        options
                .anchor(0.5f, 0.5f)
                .icons(icon_arr);

        StringBuffer buffer = new StringBuffer();
        //设置infowindow信息内容
        buffer.append(amapLocation.getCountry()
                + amapLocation.getProvince()
                + amapLocation.getCity()
                + amapLocation.getDistrict()
                + amapLocation.getStreet()
                + amapLocation.getStreetNum());
        //标题
        options.title("详细地址：");
        //子标题
        options.snippet(buffer.toString());
        //设置多少帧刷新一次图片资源
        options.period(5);
        return options;
    }

    /**
     * 响应地理编码
     */
    public void getLatlon(final String name, String city) {
        GeocodeQuery query = new GeocodeQuery(name, city);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }


    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null
                    && geocodeResult.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
                AddMarkerImage(AMapUtil.convertToLatLng(address.getLatLonPoint()), qyname);
                //获得编码后的经纬度
                myLatLng = address.getLatLonPoint();
                Log.e("ces", rCode + "=============");
            } else {
                ShowToast.showShort(QyMap.this, "没有查询到结果");
                Log.e("ces", rCode + "=============");
            }
        } else {
            Log.e("ces", rCode + "=============");
            ShowToast.showShort(QyMap.this, R.string.network_error);
        }
    }

    /**
     * 标记点击监听
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        currMarker = marker;
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
//        map_latlng_tv.setText("经度/纬度:"+"\n"+latLng);
        if (currMarker.isInfoWindowShown()) {
            currMarker.hideInfoWindow();//隐藏infowindow窗口
        }
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.daohang,
                null);
        TextView title = (TextView) view.findViewById(R.id.daohang_title);
        title.setText(marker.getTitle());

        TextView snippet = (TextView) view.findViewById(R.id.daohang_snippet);
        snippet.setText(marker.getSnippet());
        ImageButton button = (ImageButton) view
                .findViewById(R.id.start_amap_app);
        // 调起高德地图app
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAMapNavi(marker);
            }
        });
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /**
     * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
     */
    public void startAMapNavi(Marker marker) {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(marker.getPosition());
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);

        // 调起高德地图导航
        try {
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (com.amap.api.maps.AMapException e) {
            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        enterprise_map.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        enterprise_map.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        enterprise_map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        enterprise_map.onPause();
    }


}
