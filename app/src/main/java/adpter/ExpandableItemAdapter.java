package adpter;


import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import bean.PendingLv1;
import bean.PendingLv2;
import demo.yqh.wisdomapp.R;

public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_LEVEL_2 = 2;

    public ExpandableItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_1, R.layout.pending_item);
        addItemType(TYPE_LEVEL_2, R.layout.pending_item_detail);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_1:
                final PendingLv1 lv1 = (PendingLv1) item;
                helper.setText(R.id.pending_item_1, lv1.getTitle());
                helper.setText(R.id.pending_item_2, lv1.getSubtitle());
                helper.setImageResource(R.id.pending_item_img, lv1.isExpanded() ? R.drawable.bottom_jt : R.drawable.right);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (lv1.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });

                break;
            case TYPE_LEVEL_2:
                PendingLv2 lv2 = (PendingLv2) item;
                helper.setText(R.id.pending_item_detail_tt1, "item详情1：");
                helper.setText(R.id.pending_item_detail_tt2, "item详情2：");
                helper.setText(R.id.pending_item_detail_tt3, "item详情3：");
                helper.setText(R.id.pending_item_detail_tv1, lv2.getTitle1());
                helper.setText(R.id.pending_item_detail_tv2, lv2.getTitle2());
                helper.setText(R.id.pending_item_detail_tv3, lv2.getTitle3());
                break;
        }
    }
}
