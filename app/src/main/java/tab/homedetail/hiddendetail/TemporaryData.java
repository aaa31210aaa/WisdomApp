package tab.homedetail.hiddendetail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adpter.TemporaryDataAdapter;
import bean.TemporaryBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.yqh.wisdomapp.R;
import utils.BaseActivity;

import static demo.yqh.wisdomapp.MyApplication.sqldb;

public class TemporaryData extends BaseActivity {
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_name_right)
    TextView title_name_right;
    @BindView(R.id.temporary_data_listview)
    ListView temporary_data_listview;
    private List<TemporaryBean> mDatas;
    private TemporaryDataAdapter adapter;
    private List<String> mlist = new ArrayList<>();
    @BindView(R.id.nodata_layout)
    RelativeLayout nodata_layout;
    private TemporaryBean bean;
    @BindView(R.id.temporary_data_container)
    LinearLayout temporary_data_container;
    @BindView(R.id.temporary_data_delete)
    TextView temporary_data_delete; //删除
    @BindView(R.id.temporary_data_allcheck)
    TextView temporary_data_allcheck; //全选
    private List<TemporaryBean> isCheckedlist = new ArrayList<>(); //用来保存选中状态的数据集合
    private String crtype;
    private Intent intent;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary_data);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        title_name.setText(R.string.ls_data);
        title_name_right.setText(R.string.delete_datas);
        intent = getIntent();
        crtype = intent.getStringExtra("crtype");

//        initListView();
        //给listView赋值
        mDatas = new ArrayList<>();
        if (crtype.equals("YHLB001")) {
            cursor = sqldb.rawQuery("select * from commonly_cache", null);
        } else {
            cursor = sqldb.rawQuery("select * from hidden_cache", null);
        }

        while (cursor.moveToNext()) {
            bean = new TemporaryBean();
            if (crtype.equals("YHLB001")) {
                bean.setCommonlyName(cursor.getString(cursor.getColumnIndex("CommonlyName")));
                bean.setSaveTime(cursor.getString(cursor.getColumnIndex("SaveTime")));
                bean.setSaveId(cursor.getString(cursor.getColumnIndex("commonly_id")));
            } else {
                bean.setCommonlyName(cursor.getString(cursor.getColumnIndex("HiddenName")));
                bean.setSaveTime(cursor.getString(cursor.getColumnIndex("SaveTime")));
                bean.setSaveId(cursor.getString(cursor.getColumnIndex("hidden_id")));
            }

            bean.setShow(false);
            bean.setCheck(false);
            mDatas.add(bean);
            String a = bean.getSaveId();
            String b = bean.getCommonlyName();
            Log.e(TAG, a + "---" + b);
        }

        adapter = new TemporaryDataAdapter(this, mDatas);
        temporary_data_listview.setAdapter(adapter);
        temporary_data_listview.setEmptyView(nodata_layout);

        temporary_data_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (title_name_right.getText().toString().equals("批量删除")) {
                    //                ShowToast.showShort(TemporaryData.this, "点击了" + ++position);
                    Intent intent = new Intent();
//                    String a = mDatas.get(position).getSaveId();
                    intent.putExtra("clicknum", mDatas.get(position).getSaveId());
//                    Log.e(TAG, a);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    if (mDatas.get(position).isCheck()) {
                        mDatas.get(position).setCheck(false);
                        isCheckedlist.remove(mDatas.get(position));
                    } else {
                        mDatas.get(position).setCheck(true);
                        isCheckedlist.add(mDatas.get(position));
                    }
                    adapter.notifyDataSetChanged();
                }

            }
        });

    }

    @OnClick(R.id.back)
    void Back() {
        finish();
    }


    @OnClick(R.id.title_name_right)
    void Batchdeletecancel() {
        isCheckedlist.clear();
        if (title_name_right.getText().toString().equals("批量删除")) {
            title_name_right.setText(R.string.mine_cancellation_dialog_btn1);
            temporary_data_container.setVisibility(View.VISIBLE);
            for (int i = 0; i < mDatas.size(); i++) {
                mDatas.get(i).setShow(true);
            }
            adapter.notifyDataSetChanged();
        } else {
            title_name_right.setText("批量删除");
            temporary_data_container.setVisibility(View.GONE);
            for (int i = 0; i < mDatas.size(); i++) {
                mDatas.get(i).setShow(false);
                mDatas.get(i).setCheck(false);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.temporary_data_delete)
    void Delete() {
        if (mDatas.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TemporaryData.this);
            builder.setTitle(R.string.Prompt);
            builder.setMessage(R.string.DeleteTemporaryData);
            builder.setPositiveButton(R.string.mine_cancellation_dialog_btn2, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDatas.removeAll(isCheckedlist);
                    adapter.notifyDataSetChanged();

                    //同时删除数据库中的数据
                    for (int i = 0; i < isCheckedlist.size(); i++) {
                        if (crtype.equals("YHLB001")) {
                            sqldb.execSQL("delete from commonly_cache where commonly_id = ?", new Object[]{isCheckedlist.get(i).getSaveId()});
                        } else {
                            sqldb.execSQL("delete from hidden_cache where hidden_id = ?", new Object[]{isCheckedlist.get(i).getSaveId()});
                        }
                    }
                }
            });
            builder.setNegativeButton(R.string.mine_cancellation_dialog_btn1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
    }

    @OnClick(R.id.temporary_data_allcheck)
    void AllCheck() {
        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setCheck(true);
            isCheckedlist.add(mDatas.get(i));
        }
        adapter.notifyDataSetChanged();
    }


}
