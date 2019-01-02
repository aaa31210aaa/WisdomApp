package bean;


import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import adpter.ExpandableItemAdapter;

public class PendingLv1 extends AbstractExpandableItem<PendingLv2> implements MultiItemEntity {
    public String title;
    public String subtitle;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    public int getLevel() {
        return ExpandableItemAdapter.TYPE_LEVEL_1;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
