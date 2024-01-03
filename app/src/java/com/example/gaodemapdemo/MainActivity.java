package com.example.gaodemapdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.example.gaodemapdemo.adapter.TodayDetailAdapter;
import com.example.gaodemapdemo.adapter.WeekForecastAdapter;
import com.example.gaodemapdemo.api.GetPrecipitation;
import com.example.gaodemapdemo.bean.TodayDetailBean;
import com.example.gaodemapdemo.bean.TodayWeatherBean;
import com.example.gaodemapdemo.bean.WeatherForecastBean;
import com.example.gaodemapdemo.bean.WeekForecastBean;
import com.example.gaodemapdemo.model.AddMarker;
import com.example.gaodemapdemo.model.GeoSearchTool;
import com.example.gaodemapdemo.model.MatchWeatherIcon;
import com.example.gaodemapdemo.model.InitCityPicker;
import com.example.gaodemapdemo.model.InitLocation;
import com.example.gaodemapdemo.api.GetWeather;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lljjcoder.style.citypickerview.CityPickerView;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements LocationSource, EasyPermissions.PermissionCallbacks, AMap.OnMapClickListener, EditText.OnKeyListener, AMap.OnMarkerClickListener {
    //全局变量和常量
    //请求权限码
    private static final int REQUEST_PERMISSIONS = 9527;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //地图控制器
    private AMap aMap = null;
    //位置更改监听
    private OnLocationChangedListener mListener;
    // 两次点击的最大间隔时间，单位毫秒
    private long backPressedTime = 0;
    private static final int BACK_PRESSED_INTERVAL = 2000;
    // 当前的location，是一个String类型的经纬度数据
    private String currLocation;

    //组件定义
    //左上角显示城市的按钮
    private TextView tvChooseCity;
    //定义城市选择器对象mPicker
    private CityPickerView mPicker=new CityPickerView();
    //天气信息组件定义
    private TextView tvCity;
    private TextView tvTemperature;
    private TextView tvWeatherStatus;
    private ImageView ivWeatherStatus;
    private TextView tvFeelsLike;
    private TextView tvWindInfo;
    private TextView tvHumidity;
    private TextView tvPressure;
    private TextView tvVision;
    private TextView tvPrecipSummary;
    private TextView tvLivingAdvice;
    private RecyclerView rvTodayDetail;
    private RecyclerView rvSevenDay;
    //定义地图组件
    private MapView mapView;
    //搜索地点输入框
    private EditText etAddress;
    //右下角的浮动按钮
    private FloatingActionButton fab;
    //上滑页面组件
    private RelativeLayout relativeLayout;
    private BottomSheetBehavior bottomSheetBehavior;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化视图
        initView();

        //初始化LocationClient对象
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //初始化地图
        initMap(savedInstanceState);

        //初始化定位
        InitLocation initLocation = new InitLocation(mLocationClient, mLocationOption, MainActivity.this);
        initLocation.initLocation();

        //请求权限之后启动定位
        requestPermission();
    }

    //初始化视图组件
    private void initView() {
        mapView = findViewById(R.id.map_view);
        tvChooseCity = findViewById(R.id.choose_city);
        etAddress = findViewById(R.id.et_address);
        relativeLayout = findViewById(R.id.bottom_sheet_ray);
        fab = findViewById(R.id.fab);
        tvCity = findViewById(R.id.tv_city);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvWeatherStatus = findViewById(R.id.tv_weather_status);
        ivWeatherStatus = findViewById(R.id.iv_weather);
        tvFeelsLike = findViewById(R.id.tv_feelslike);
        tvWindInfo = findViewById(R.id.tv_wind);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvPressure = findViewById(R.id.tv_pressure);
        tvVision = findViewById(R.id.tv_vis);
        tvPrecipSummary = findViewById(R.id.summary);
        tvLivingAdvice = findViewById(R.id.tv3);
        rvTodayDetail = findViewById(R.id.rv_today_detail);
        rvSevenDay = findViewById(R.id.rv_seven_day_daily);

        //键盘按键监听
        etAddress.setOnKeyListener(this);

        //预先加载城市选择器的数据
        mPicker.init(this);

        // 滑动视图的响应事件
        bottomSheetBehavior = BottomSheetBehavior.from(relativeLayout);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // 当视图的位置和状态变化时响应
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        // 当页面收缩到下方时，显示浮动按钮
                        fab.show();
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        // 当页面展开时，藏起浮动按钮
                        fab.hide();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        // 浮动按钮的点击响应事件
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 重新定位到当前的地点
                requestPermission();
                // 移除光标
                etAddress.clearFocus();
                // 清除地图上的标点（除了定位标点）
                aMap.clear(true);
            }
        });

        // 选择城市按钮的点击响应事件
        tvChooseCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击城市按钮后弹出城市选择器
                InitCityPicker initCityPicker = new InitCityPicker(mPicker, tvChooseCity);
                initCityPicker.initCityPicker();
            }
        });

        // 生活建议按钮的点击响应事件
        tvLivingAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击后跳转到LivingAdviceActivity，并传递location的值
                Intent intent = new Intent(MainActivity.this, LivingAdviceActivity.class);
                intent.putExtra("location", currLocation);
                startActivity(intent);
            }
        });
    }

    /**
     * 动态请求权限
     */
    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void requestPermission() {
        //true 有权限 开始定位
        Log.d("permission", "已获得权限，可以定位啦");
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // 处理权限已授予的情况
        if (requestCode == REQUEST_PERMISSIONS) {
            requestPermission();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // 处理权限被拒绝的情况
        if (requestCode == REQUEST_PERMISSIONS) {
            showMsg("需要权限");
        }
    }

    /**
     * 请求权限结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //设置权限请求结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    // 初始化地图
    private void initMap(Bundle savedInstanceState) {
        //在MainActivity执行oncreate的时候创建地图
        mapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        aMap = mapView.getMap();
        //设置定位蓝点的Style
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        aMap.setMyLocationStyle(myLocationStyle);
        //设置最小缩放等级为12 ，缩放级别范围为[3, 20]
        aMap.setMinZoomLevel(12);
        //开启室内地图
        aMap.showIndoorMap(true);
        //实例化UiSettings类对象
        UiSettings mUiSettings = aMap.getUiSettings();
        //隐藏缩放按钮
        mUiSettings.setZoomControlsEnabled(false);
        //显示比例尺 默认不显示
        mUiSettings.setScaleControlsEnabled(true);
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        //设置地图点击事件
        aMap.setOnMapClickListener(this);
        //设置地图标点点击事件
        aMap.setOnMarkerClickListener(this);
    }

    /**
     * 地图单击事件
     *
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        //改变地图中心点
        updateMapCenter(latLng);

        // 将latLng转换成一个LatLonPoint对象
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);

        // 将经纬度封装为location并更新全局变量currLocation
        String location = String.format("%.6f,%.6f", latLng.longitude, latLng.latitude);
        currLocation = location;

        // 通过点击的坐标查询地址，并根据地址更新天气信息至点击的地区
        getReGeoSearchResult(latLonPoint, location);

        // 在点击处添加标点
        AddMarker addMarker = new AddMarker(aMap);
        addMarker.addMarker(latLng);
    }

    // 获取逆地理查询（根据坐标查询地址）的结果
    private void getReGeoSearchResult(LatLonPoint latLonPoint, String location) {
        GeoSearchTool geoSearchTool = new GeoSearchTool(MainActivity.this);
        geoSearchTool.onReGeocodeSearch(latLonPoint, new GeoSearchTool.onReGeocodeSearchCallback() {
            @Override
            public void onSuccess(RegeocodeAddress regeocodeAddress) {
                // 显示地址
                showMsg("地址：" + regeocodeAddress.getFormatAddress());
                //获取城市名称
                String city = regeocodeAddress.getCity();
                //获取区域名称
                String district = regeocodeAddress.getDistrict();
                if (!district.isEmpty()) {
                    Log.d("district", "district="+district);
                } else {
                    Log.d("district", "district为空");
                }
                //更新天气信息为当前搜索的地区
                updateAllWeather(location, district, city);
                //更新城市按钮为当前搜索的城市
                tvChooseCity.setText(city);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("getReGeoSearchResult", "查询地址失败！");
            }
        });
    }


    /**
     * Marker点击事件
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        //显示InfoWindow
        if (!marker.isInfoWindowShown()) {
            //显示
            marker.showInfoWindow();
        } else {
            //隐藏
            marker.hideInfoWindow();
        }
        //加一个动画或者改变颜色
        return true;
    }

    /**
     * 搜索框输入
     *
     * @param v
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
            //获取输入框的值
            String address = etAddress.getText().toString().trim();
            if (address == null || address.isEmpty()) {
                showMsg("请输入地址");
            } else {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                // 从城市选择框获取用户选择的城市作为查询城市
                String searchCity = tvChooseCity.getText().toString();
                // 通过用户输入的地址，在用户选择的城市范围内查询坐标
                getGeoSearchResult(address, searchCity);
            }
            return true;
        }
        return false;
    }

    // 获取地址查询（通过输入地址查询坐标）的结果并执行后续操作
    private void getGeoSearchResult(String address, String searchCity) {
        GeoSearchTool geoSearchTool = new GeoSearchTool(MainActivity.this);
        geoSearchTool.onGeocodeSearch(address, searchCity, new GeoSearchTool.onGeocodeSearchCallback() {
            @Override
            public void onSuccess(GeocodeAddress geocodeAddress) {
                // 获取经纬度坐标
                LatLonPoint latLonPoint = geocodeAddress.getLatLonPoint();
                // 获取该地所在的区
                String district = geocodeAddress.getDistrict();
                //将搜索地址的经纬度保存为一个LatLng对象searchLatLng
                LatLng searchLatLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());

                // 清空输入框内容
                etAddress.setText("");
                // 移除光标
                etAddress.clearFocus();
                //改变地图中心点，切换到搜索的地点
                updateMapCenter(searchLatLng);
                //添加标点
                AddMarker addMarker = new AddMarker(aMap);
                addMarker.addMarker(searchLatLng);

                // 将经纬度封装为location并更新全局变量currLocation
                String location = String.format("%.6f,%.6f", latLonPoint.getLongitude(), latLonPoint.getLatitude());
                currLocation = location;

                // 更新天气UI
                updateAllWeather(location, district, searchCity);
            }

            @Override
            public void onFailure(String errorMessage) {
                showMsg(errorMessage);
                Log.e("getGeoSearchResult", "查询坐标失败！");
            }
        });
    }

    /**
     * 改变地图中心位置
     *
     * @param latLng 位置
     */
    public void updateMapCenter(LatLng latLng) {
        // CameraPosition 第一个参数： 目标位置的屏幕中心点经纬度坐标。
        // CameraPosition 第二个参数： 目标可视区域的缩放级别
        // CameraPosition 第三个参数： 目标可视区域的倾斜度，以角度为单位。
        // CameraPosition 第四个参数： 可视区域指向的方向，以角度为单位，从正北向顺时针方向计算，从0度到360度
        CameraPosition cameraPosition = new CameraPosition(latLng, 16, 30, 0);
        //位置变更
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        //改变位置
        aMap.animateCamera(cameraUpdate);
    }


    /**
     * Toast提示
     *
     * @param msg 提示内容
     */
    public void showMsg(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    // 接收定位返回的数据并更新UI
    public void LocationRespond(String address, String location, String currCity, String district, AMapLocation aMapLocation){
        showMsg(address);
        //更新全局变量location
        currLocation = location;
        Log.d("currLocation", "currLocation="+currLocation);

        // 获取天气有关信息并更新UI
        updateAllWeather(location, district, currCity);

        //显示地图定位结果
        if (mListener != null) {
            //显示系统图标
            mListener.onLocationChanged(aMapLocation);
        }
        else {
            Log.d("InitLocation", "mListener为空！");
        }

        //给搜索城市按钮赋值
        tvChooseCity.setText(currCity);
        //显示浮动按钮
        fab.show();
    }

    // 获取天气有关信息并更新UI
    public void updateAllWeather(String location, String district, String currCity) {
        //获取实时的天气数据并更新UI
        GetWeather getWeather = new GetWeather(MainActivity.this);
        // 实现WeatherCallback接口
        getWeather.getTodayWeather(location, new GetWeather.todayWeatherCallback() {
            @Override
            public void onSuccess(TodayWeatherBean.NowDTO nowDTO) {
                // 调用updateTodayWeather函数来更新天气UI
                // 判断district是否为空
                if (!district.isEmpty()) {
                    updateTodayWeather(nowDTO, district); // 如果有区就显示区
                } else {
                    updateTodayWeather(nowDTO, currCity); // 如果没有乡镇就显示当前城市
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("getTodayWeather", "获取天气失败！");
            }
        });
        // 获取天气预报信息并更新UI
        getWeather.getWeatherForecast(location, new GetWeather.weatherForecastCallback() {
            @Override
            public void onSuccess(List<WeatherForecastBean.DailyDTO> dailyDTOSList) {
                // 调用respondForecast函数更新跟天气预报有关的UI
                respondForecast(dailyDTOSList);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("getWeatherForecast", "获取天气预报失败！");
            }
        });
        // 获取实时的降水预报并更新UI
        GetPrecipitation getPrecipitation = new GetPrecipitation(MainActivity.this);
        getPrecipitation.getPrecipitation(location, new GetPrecipitation.precipitationCallback() {
            @Override
            public void onSuccess(String summary) {
                // 调用 updatePrecipitation 函数更新与降水预报有关的UI
                updatePrecipitation(summary);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("getPrecipitation", "获取降水预报失败！");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁定位客户端，同时销毁本地定位服务
        mLocationClient.onDestroy();
        //在Activity执行onDestroy时销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient.startLocation();//启动定位
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

    //设置系统返回按键/侧滑返回的响应事件
    @Override
    public void onBackPressed() {
        //清空地图上的标点，true表示只清除自定义的标点，不包括定位蓝点
        aMap.clear(true);
        //清空搜索框的内容
        etAddress.setText("");
        etAddress.clearFocus();
        long currentTime = System.currentTimeMillis();

        // 判断两次点击的时间间隔是否小于 BACK_PRESSED_INTERVAL
        if (currentTime - backPressedTime < BACK_PRESSED_INTERVAL) {
            super.onBackPressed(); // 如果小于，执行默认的返回操作
        } else {
            Toast.makeText(this, "再次点击返回退出", Toast.LENGTH_SHORT).show();
            backPressedTime = currentTime; // 如果大于，更新时间戳
        }
    }

    //更新今日实时天气UI
    public void updateTodayWeather(TodayWeatherBean.NowDTO nowDTO, String district){
        // 获取天气信息，从nowDTO中解析得到
        String temperature = nowDTO.getTemp(); // 温度，默认单位：摄氏度
        String feelsLike = nowDTO.getFeelsLike(); // 体感温度，默认单位：摄氏度
        String text = nowDTO.getText(); // 天气状况的文字描述，包括阴晴雨雪等天气状态的描述
        String wind360 = nowDTO.getWind360(); // 风向360角度
        String windDirection = nowDTO.getWindDir(); // 风向
        String windScale = nowDTO.getWindScale(); // 风力等级
        String windSpeed = nowDTO.getWindSpeed(); // 风速，公里/小时
        String humidity = nowDTO.getHumidity(); // 相对湿度，百分比数值
        String precipitation = nowDTO.getPrecip(); // 当前小时累计降水量，默认单位：毫米
        String pressure = nowDTO.getPressure(); // 大气压强，默认单位：百帕
        String vision = nowDTO.getVis(); // 能见度，默认单位：公里

        tvCity.setText(district);
        tvTemperature.setText(temperature+"°");
        tvWeatherStatus.setText(text);
        tvWindInfo.setText(windDirection+windScale+"级");
        tvFeelsLike.setText(feelsLike+"°");
        tvHumidity.setText(humidity+"%");
        tvPressure.setText(pressure+"mPa");
        tvVision.setText(vision+"km");

        //调用matchWeatherIcon中的方法更新天气状态图标
        MatchWeatherIcon matchWeatherIcon = new MatchWeatherIcon();
        ivWeatherStatus.setImageResource(matchWeatherIcon.matchWeatherIcon(text));
    }

    //此函数用来解析回调返回的天气预报信息castsDTOList
    public void respondForecast(List<WeatherForecastBean.DailyDTO> dailyDTOSList) {
        // 解析到这一步，每一个dailyDTO对象就是一个天气预报对象，包含着一天的所有的预报信息
        WeatherForecastBean.DailyDTO dailyDTO1 = dailyDTOSList.get(0); // 获取当天的天气预报信息
        WeatherForecastBean.DailyDTO dailyDTO2 = dailyDTOSList.get(1); // 第二天
        WeatherForecastBean.DailyDTO dailyDTO3 = dailyDTOSList.get(2); // 第三天
        WeatherForecastBean.DailyDTO dailyDTO4 = dailyDTOSList.get(3); // 以此类推...
        WeatherForecastBean.DailyDTO dailyDTO5 = dailyDTOSList.get(4);
        WeatherForecastBean.DailyDTO dailyDTO6 = dailyDTOSList.get(5);
        WeatherForecastBean.DailyDTO dailyDTO7 = dailyDTOSList.get(6);


        List<WeekForecastBean> weekForecastList = new ArrayList<>();
        weekForecastList.clear();
        // 调用updateForecast函数更新UI
        weekForecastList.addAll(updateForecast(dailyDTO1, 1));
        weekForecastList.addAll(updateForecast(dailyDTO2, 2));
        weekForecastList.addAll(updateForecast(dailyDTO3, 3));
        weekForecastList.addAll(updateForecast(dailyDTO4, 4));
        weekForecastList.addAll(updateForecast(dailyDTO5, 5));
        weekForecastList.addAll(updateForecast(dailyDTO6, 6));
        weekForecastList.addAll(updateForecast(dailyDTO7, 7));

        WeekForecastAdapter weekForecastAdapter = new WeekForecastAdapter(weekForecastList);
        //创建布局管理
        rvSevenDay.setLayoutManager(new GridLayoutManager(this, 1));
        rvSevenDay.setAdapter(weekForecastAdapter);
    }

    //更新天气预报UI
    public List<WeekForecastBean> updateForecast(WeatherForecastBean.DailyDTO dailyDTO, int day) {
        List<WeekForecastBean> weekForecastList = new ArrayList<>();
        MatchWeatherIcon matchWeatherIcon = new MatchWeatherIcon();

        //获取这一天的天气预报信息
        String date = dailyDTO.getFxDate(); // 预报日期
        String textDay = dailyDTO.getTextDay(); // 白天天气文字描述
        String textNight = dailyDTO.getTextNight(); // 夜晚天气文字描述
        String uvIndex = dailyDTO.getUvIndex(); // 紫外线强度指数
        String sunrise = dailyDTO.getSunrise(); // 日出时间，在高纬度地区可能为空
        String sunset = dailyDTO.getSunset(); // 日落时间，在高纬度地区可能为空
        String tempMax = dailyDTO.getTempMax(); // 预报当天最高温度
        String tempMin = dailyDTO.getTempMin(); // 预报当天最低温度
        String cloud = dailyDTO.getCloud(); // 云量，百分比数值。可能为空
        String uvLevel; // 紫外线强度等级

        // 根据紫外线强度指数初始化紫外线强度等级
        if (Integer.parseInt(uvIndex) <= 2) {
            uvLevel = "低";
        } else if (Integer.parseInt(uvIndex) >= 3 && Integer.parseInt(uvIndex) <= 5) {
            uvLevel = "中等";
        } else if (Integer.parseInt(uvIndex) == 6 || Integer.parseInt(uvIndex) == 7) {
            uvLevel = "高";
        } else if (Integer.parseInt(uvIndex) >= 8 && Integer.parseInt(uvIndex) <= 10) {
            uvLevel = "非常高";
        } else {
            uvLevel = "极高";
        }

        // 根据day的值来判断这是第几天的数据，1就代表第一天，对应更新第一天的预报
        if (day == 1) {
            //更新当天天气详情UI
            List<TodayDetailBean> todayDetailList = new ArrayList<>();
            todayDetailList.clear();
            todayDetailList.add(new TodayDetailBean(R.drawable.ic_cloudy_daylight, textDay, "白天天气"));
            todayDetailList.add(new TodayDetailBean(R.drawable.ic_moon, textNight, "夜间天气"));
            todayDetailList.add(new TodayDetailBean(R.drawable.ic_sunshine_level, uvLevel, "紫外线强度"));
            todayDetailList.add(new TodayDetailBean(R.drawable.ic_sunrise, sunrise, "日出时间"));
            todayDetailList.add(new TodayDetailBean(R.drawable.ic_sunset, sunset, "日落时间"));
            todayDetailList.add(new TodayDetailBean(R.drawable.ic_overcast, cloud+"%", "云量"));
            TodayDetailAdapter todayDetailAdapter = new TodayDetailAdapter(todayDetailList);
            // 创建布局管理
            rvTodayDetail.setLayoutManager(new GridLayoutManager(this, 3)); // 每行3个框
            rvTodayDetail.setAdapter(todayDetailAdapter);

            //更新第一日天气预报UI
            weekForecastList.add(new WeekForecastBean(date, matchWeatherIcon.matchWeatherIcon(textDay), tempMax+"° / ", tempMin+"°"));
        } else {
            // 更新天气预报UI
            weekForecastList.add(new WeekForecastBean(date, matchWeatherIcon.matchWeatherIcon(textDay), tempMax+"° / ", tempMin+"°"));
        }

        return weekForecastList;
    }

    // 更新降水预报UI
    public void updatePrecipitation(String summary) {
        tvPrecipSummary.setText(summary);
    }
}
