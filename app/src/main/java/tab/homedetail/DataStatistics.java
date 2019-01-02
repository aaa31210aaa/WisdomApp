package tab.homedetail;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;

/**
 * 数据统计
 */
public class DataStatistics extends BaseActivity implements OnChartValueSelectedListener {
    @BindView(R.id.title_name)
    TextView title_name;
    //企业用户
    @BindView(R.id.report_comusertotal)
    TextView report_comusertotal;
    //待审核企业
    @BindView(R.id.report_toauditusertotal)
    TextView report_toauditusertotal;
    //统计日期
    @BindView(R.id.report_tjdate)
    TextView report_tjdate;
    //本月上报一般隐患
    @BindView(R.id.report_bysbybdangertotal)
    TextView report_bysbybdangertotal;
    //整改日期到期的一般隐患
    @BindView(R.id.report_dqdangertotal)
    TextView report_dqdangertotal;
    //未整改完成的一般隐患
    @BindView(R.id.report_bywwcybdangertotal)
    TextView report_bywwcybdangertotal;
    //本月上报重大隐患
    @BindView(R.id.report_bysbzddangertotal)
    TextView report_bysbzddangertotal;
    //等待审核的重大隐患
    @BindView(R.id.report_toauditdangertotal)
    TextView report_toauditdangertotal;
    //未整改完成的重大隐患
    @BindView(R.id.report_bywwczddangertotal)
    TextView report_bywwczddangertotal;
    //本月未上报企业
    @BindView(R.id.report_wsbcomtotal)
    TextView report_wsbcomtotal;
    //本年度共排查一般隐患
    @BindView(R.id.report_yearybdangertotal)
    TextView report_yearybdangertotal;
    //其中已整改一般隐患
    @BindView(R.id.report_havechangedybtotal)
    TextView report_havechangedybtotal;
    //其中未整改一般隐患
    @BindView(R.id.report_nochangeybtotal)
    TextView report_nochangeybtotal;
    //本年度共排查重大隐患
    @BindView(R.id.report_yearzddangertotal)
    TextView report_yearzddangertotal;
    //其中已整改重大隐患
    @BindView(R.id.report_havechangedzdtotal)
    TextView report_havechangedzdtotal;
    //其中未整改重大隐患
    @BindView(R.id.report_nochangezdtotal)
    TextView report_nochangezdtotal;
    //本年度共排查隐患
    @BindView(R.id.report_dangertotal)
    TextView report_dangertotal;
    //整改率
    @BindView(R.id.report_zgl)
    TextView report_zgl;
    //权限监管考核企业
    @BindView(R.id.report_qxjgkhqy)
    TextView report_qxjgkhqy;
    //已达标
    @BindView(R.id.report_qxjgkhqy_ydb)
    TextView report_qxjgkhqy_ydb;
    //进行中
    @BindView(R.id.report_qxjgkhqy_jxz)
    TextView report_qxjgkhqy_jxz;
    //本月完成
    @BindView(R.id.report_qxjgkhqy_bywc)
    TextView report_qxjgkhqy_bywc;
    //近三月完成
    @BindView(R.id.report_qxjgkhqy_jsywc)
    TextView report_qxjgkhqy_jsywc;
    private int index = 0;

//    private boolean myBit = true;
//    @BindView(R.id.data_report_barchart)
//    BarChart barChart;
//    private BarDataSet dataset, dataset2, dataset3;
//    //柱形图X轴数据集
//    private ArrayList<String> barXValues = new ArrayList<String>();
//    private XAxis barXAxis; //X坐标轴
//    private YAxis barYAxis; //Y
//
//    //总隐患
//    private ArrayList<BarEntry> barTotalNum = new ArrayList<BarEntry>();
//    //已完成的
//    private ArrayList<BarEntry> barCompleted = new ArrayList<BarEntry>();
//    //未完成的
//    private ArrayList<BarEntry> barIncomplete = new ArrayList<BarEntry>();
//
//    //折线图
//    @BindView(R.id.data_report_linechart)
//    LineChart lineChart;
//    private LineDataSet ldateset, ldataset2, ldataset3;
//    //折线图X轴数据集
//    private ArrayList<String> lineXValues = new ArrayList<String>();
//    private XAxis lineXAxis;
//    private YAxis lineYAxis;
//
//    //总隐患
//    private ArrayList<Entry> lineTotalNum = new ArrayList<Entry>();
//    //已完成的
//    private ArrayList<Entry> lineCompleted = new ArrayList<Entry>();
//    //未完成的
//    private ArrayList<Entry> lineIncomplete = new ArrayList<Entry>();
//
//    private int value1;
//    private int value2;
//    private int value3;
//
//    private float xscaleLine = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_statistics);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.data_statistics);
//        setBarChart();
//        setLineChart();
        mConnect();
    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }

    private void mConnect() {
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        OkGo.<String>get(PortIpAddress.GetTjData())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                report_comusertotal.setText(jsonArray.getJSONObject(index).getString("comusertotal"));
                                report_toauditusertotal.setText(jsonArray.getJSONObject(index).getString("toauditusertotal"));
                                report_tjdate.setText(jsonArray.getJSONObject(index).getString("tjdate"));
                                report_bysbybdangertotal.setText(jsonArray.getJSONObject(index).getString("bysbybdangertotal"));
                                report_dqdangertotal.setText(jsonArray.getJSONObject(index).getString("dqdangertotal"));
                                report_bywwcybdangertotal.setText(jsonArray.getJSONObject(index).getString("bywwcybdangertotal"));
                                report_bysbzddangertotal.setText(jsonArray.getJSONObject(index).getString("bysbzddangertotal"));
                                report_toauditdangertotal.setText(jsonArray.getJSONObject(index).getString("toauditdangertotal"));
                                report_bywwczddangertotal.setText(jsonArray.getJSONObject(index).getString("bywwczddangertotal"));
                                report_wsbcomtotal.setText(jsonArray.getJSONObject(index).getString("wsbcomtotal"));
                                report_yearybdangertotal.setText(jsonArray.getJSONObject(index).getString("yearybdangertotal"));
                                report_havechangedybtotal.setText(jsonArray.getJSONObject(index).getString("havechangedybtotal"));
                                report_nochangeybtotal.setText(jsonArray.getJSONObject(index).getString("nochangeybtotal"));
                                report_yearzddangertotal.setText(jsonArray.getJSONObject(index).getString("yearzddangertotal"));
                                report_havechangedzdtotal.setText(jsonArray.getJSONObject(index).getString("havechangedzdtotal"));
                                report_nochangezdtotal.setText(jsonArray.getJSONObject(index).getString("nochangezdtotal"));
                                report_dangertotal.setText(jsonArray.getJSONObject(index).getString("dangertotal"));
                                report_zgl.setText(jsonArray.getJSONObject(index).getString("zgl"));
                                report_qxjgkhqy.setText("0");
                                report_qxjgkhqy_ydb.setText("0");
                                report_qxjgkhqy_jxz.setText("0");
                                report_qxjgkhqy_bywc.setText("0");
                                report_qxjgkhqy_jsywc.setText("0");
                            } else {
                                ShowToast.showShort(DataStatistics.this, R.string.getInfoErr);
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
                        ShowToast.showShort(DataStatistics.this, R.string.connect_err);
                    }
                });
    }

//    @OnClick(R.id.data_report_switch)
//    void ReportSwitch() {
//        if (myBit) {
//            lineChart.setVisibility(View.GONE);
//            barChart.setVisibility(View.VISIBLE);
//            myBit = false;
//        } else {
//            lineChart.setVisibility(View.VISIBLE);
//            barChart.setVisibility(View.GONE);
//            myBit = true;
//        }
//    }
//
//    private void setLineChart() {
//        //设置数据描述
//        lineChart.setDescription("");
//        //设置动画
//        lineChart.animateX(3000);
//        lineChart.animateY(3000);
//        //设置缩放是否在XY轴同时进行
//        lineChart.setPinchZoom(false);
//        //设置表的默认初始放大
////        ViewPortHandler lineHandler = lineChart.getViewPortHandler();
////        Matrix matrix = lineHandler.getMatrixTouch();
////        matrix.postScale(xscaleLine, 1);
//        //将表格左移距离
////        lineHandler.setDragOffsetY(50);
//        //启用手指触摸
////        linechart.setTouchEnabled(true);
//
//        //设置格子背景色,参数是Color类型对象
//        lineChart.setDrawGridBackground(false);
//        //将标注字设置在下方
//        lineXAxis = lineChart.getXAxis();
//        lineXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        //设置X间隔为1
//        lineXAxis.setSpaceBetweenLabels(1);
//        lineYAxis = lineChart.getAxisLeft();
//        //不要竖线网格
//        lineXAxis.setDrawGridLines(false);
//        // 隐藏右边 的坐标轴
//        lineChart.getAxisRight().setEnabled(true);
//        lineXAxis.setAvoidFirstLastClipping(false);
//        initLineChartData(10, lineTotalNum, lineCompleted, lineIncomplete);  //添加Y轴数据
//    }
//
//    //这只柱形图
//    private void setBarChart() {
//        //设置数据描述
//        barChart.setDescription("");
//        //设置
//        barChart.setDrawBarShadow(false);
//        barChart.setDrawValueAboveBar(true);
//
//        //设置表的默认初始放大
//        ViewPortHandler lineHandler = barChart.getViewPortHandler();
//        Matrix matrix = lineHandler.getMatrixTouch();
//        matrix.postScale(xscaleLine, 1);
//        //设置纵向动画
////        barChart.animateX(3000);
//        barChart.animateY(3000);
//        //设置缩放是否在XY轴同时进行
//        barChart.setPinchZoom(false);
//        //设置格子背景色,参数是Color类型对象
//        barChart.setDrawGridBackground(false);
//        //将标注字设置在下方
//        barXAxis = barChart.getXAxis();
//        barXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        //如果设置为true，图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
////      xAxis.setAvoidFirstLastClipping(true);
//        //设置X间隔为1
//        barXAxis.setSpaceBetweenLabels(1);
//        barYAxis = barChart.getAxisLeft();
//
//        //不要竖线网格
//        barXAxis.setDrawGridLines(false);
//        // 隐藏右边 的坐标轴
//        barChart.getAxisRight().setEnabled(true);
//        barXAxis.setAvoidFirstLastClipping(false);
//        initBarChartData(10, barTotalNum, barCompleted, barIncomplete);  //添加Y轴数据
//    }
//
//    //设置折线图数据
//    private void initLineChartData(int count, ArrayList<Entry> lineTotalNum, ArrayList<Entry> lineCompleted, ArrayList<Entry> lineIncomplete) {
//        //给x轴赋值
//        for (int i = 0; i < count; i++) {
//            lineXValues.add("第" + (i + 1) + "天");
//        }
//        //总隐患数
//        for (int j = 0; j < count; j++) {
//            value1 = (int) (Math.random() * 80/*80以内的随机数*/) + 3;
//            lineTotalNum.add(new Entry(value1, j));
//        }
//        //完成数
//        for (int k = 0; k < count; k++) {
//            value2 = (int) (Math.random() * 50/*50以内的随机数*/) + 3;
//            lineCompleted.add(new Entry(value2, k));
//        }
//        //未完成数
//        for (int p = 0; p < count; p++) {
//            value3 = (int) (Math.random() * 20/*20以内的随机数*/) + 3;
//            lineIncomplete.add(new Entry(value3, p));
//        }
//
//
//        ldateset = new LineDataSet(lineTotalNum, "隐患总数(个)");
//        ldataset2 = new LineDataSet(lineCompleted, "已整改隐患数(个)");
//        ldataset3 = new LineDataSet(lineIncomplete, "未整改隐患数(个)");
//        //设置折线图颜色
//        ldateset.setColor(ContextCompat.getColor(this, R.color.yhzs));
//        ldataset2.setColor(ContextCompat.getColor(this, R.color.yhywc));
//        ldataset3.setColor(ContextCompat.getColor(this, R.color.yhwwc));
//        ldateset.setCircleColor(ContextCompat.getColor(this, R.color.yhzs));
//        ldataset2.setCircleColor(ContextCompat.getColor(this, R.color.yhywc));
//        ldataset3.setCircleColor(ContextCompat.getColor(this, R.color.yhwwc));
//
//
//        //设置Y轴数据为整数
//        ldateset.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                return (int) value + "";
//            }
//        });
//
//        ldataset2.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                return (int) value + "";
//            }
//        });
//
//        ldataset3.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                return (int) value + "";
//            }
//        });
//
//
//        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
//        lineDataSets.add(ldateset);
//        lineDataSets.add(ldataset2);
//        lineDataSets.add(ldataset3);
//        LineData data = new LineData(lineXValues, lineDataSets);
//        lineChart.setData(data);
//        lineChart.setVisibleXRangeMaximum(4f);
//        //刷新
//        lineChart.invalidate();
//    }
//
//
//    //设置柱形图数据
//    private void initBarChartData(int count, ArrayList<BarEntry> barTotalNum, ArrayList<BarEntry> barCompleted, ArrayList<BarEntry> barIncomplete) {
//        //给X轴赋值
//        for (int i = 0; i < count; i++) {
//            barXValues.add("第" + (i + 1) + "天");
//        }
//        //总隐患数
//        for (int j = 0; j < count; j++) {
//            value1 = (int) (Math.random() * 80/*80以内的随机数*/) + 3;
//            barTotalNum.add(new BarEntry(value1, j));
//        }
//        //完成数
//        for (int k = 0; k < count; k++) {
//            value2 = (int) (Math.random() * 50) + 3;
//            barCompleted.add(new BarEntry(value2, k));
//        }
//        //未完成数
//        for (int p = 0; p < count; p++) {
//            value3 = (int) ((Math.random() * 20) + 3);
//            barIncomplete.add(new BarEntry(value3, p));
//        }
//
//        dataset = new BarDataSet(barTotalNum, "隐患总数(个)");
//        dataset2 = new BarDataSet(barCompleted, "已整改隐患数(个)");
//        dataset3 = new BarDataSet(barIncomplete, "未整改隐患数(个)");
////        dataset.setBarSpacePercent(50f);
////        dataset2.setBarSpacePercent(50f);
////        dataset3.setBarSpacePercent(50f);
//        //设置柱形图颜色
//        dataset.setColor(ContextCompat.getColor(this, R.color.yhzs));
//        dataset2.setColor(ContextCompat.getColor(this, R.color.yhywc));
//        dataset3.setColor(ContextCompat.getColor(this, R.color.yhwwc));
//        //设置Y轴数据为整数
//        dataset.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                return (int) value + "";
//            }
//        });
//
//        dataset2.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                return (int) value + "";
//            }
//        });
//
//        dataset3.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                return (int) value + "";
//            }
//        });
//
//
//        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
//        barDataSets.add(dataset);
//        barDataSets.add(dataset2);
//        barDataSets.add(dataset3);
//        BarData data = new BarData(barXValues, barDataSets);
//        barChart.setData(data);
////        barChart.setVisibleXRangeMaximum(19);
//        //刷新
//        barChart.invalidate();
//    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
