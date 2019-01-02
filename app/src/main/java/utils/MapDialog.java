package utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import demo.yqh.wisdomapp.R;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;

public class MapDialog extends Dialog implements LocationSource, AMapLocationListener, AMap.OnMarkerClickListener
        , AMap.OnMapClickListener {
    private Context context;
    public MapView dialog_map = null;

    private AMap aMap;
    //定位需要的声明
    public AMapLocationClient mLocationClient = null;//定位发起端
    public AMapLocationClientOption mLocationOption = null;//定位参数
    public OnLocationChangedListener mListener = null;//定位监听器
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    //定位标识
    private boolean dw = false;
    private ArrayList<BitmapDescriptor> icon_arr;
    //地图UI设置
    private UiSettings mUiSettings;
    //保存当前marker
    private Marker currMarker;

    private ImageButton map_dialog_imagebtn;
    public Button negativeButton, positiveButton;

    //当前位置经纬度
    private double mLat, mlng = 0;

    public MapDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public MapDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected MapDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.map_dialog, null);
        dialog_map = (MapView) view.findViewById(R.id.dialog_map);
        map_dialog_imagebtn = (ImageButton) view.findViewById(R.id.map_dialog_imagebtn);
        negativeButton = (Button) view.findViewById(R.id.negativeButton);
        positiveButton = (Button) view.findViewById(R.id.positiveButton);


        //保存定位按钮
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //上传位置信息
                OkGo.<String>get(PortIpAddress.LocationAdress())
                        .tag(this)
                        .params("qyid", PortIpAddress.getUserId(context))
                        .params("longitudecoord", mLat)
                        .params("latitudecoord", mlng)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().toString());
                                    if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                        Log.d("TAG", "位置上传成功");
                                        ShowToast.showShort(context,"位置上传成功");
                                        dismiss();
                                        dialog_map.onDestroy();
                                    } else {
                                        Log.d("TAG", "位置上传失败,请重试");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);
                                ShowToast.showShort(context, R.string.connect_err);
                            }
                        });
            }
        });

        //取消按钮
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                dialog_map.onDestroy();
            }
        });

        dialog_map.onCreate(savedInstanceState);

        //添加定位图片集合
        icon_arr = new ArrayList<BitmapDescriptor>();
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));

        //定位按钮
        map_dialog_imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dw = true;
                mLocationClient.startLocation();
            }
        });


        initMap();
        setContentView(view);
    }

    private void initMap() {
        if (aMap == null) {
            aMap = dialog_map.getMap();
            aMap.setOnMarkerClickListener(this);
            aMap.setOnMapClickListener(this);
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        mUiSettings = aMap.getUiSettings();
        //开启比例尺
        mUiSettings.setScaleControlsEnabled(true);
        //开启指南针
        mUiSettings.setCompassEnabled(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
//                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                if (dw) {
                    aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    mLocationClient.stopLocation();
                }

                if (isFirstLoc) {
                    aMap.addMarker(getMarkerOptions(amapLocation, amapLocation.getLatitude(), amapLocation.getLongitude()));
                    aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    mLat = amapLocation.getLatitude();
                    mlng = amapLocation.getLongitude();
                    //只定位一次
                    isFirstLoc = false;
                    mLocationClient.stopLocation();
                }


            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                ShowToast.showShort(context, errText);
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
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(context);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
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


    @Override
    public void onMapClick(LatLng latLng) {
        if (currMarker.isInfoWindowShown()) {
            currMarker.hideInfoWindow();//隐藏infowindow窗口
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        currMarker = marker;
        return false;
    }
}
