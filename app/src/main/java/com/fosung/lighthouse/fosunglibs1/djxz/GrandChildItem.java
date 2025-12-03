package com.fosung.lighthouse.fosunglibs1.djxz;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fosung.lighthouse.fosunglibs1.R;

public class GrandChildItem implements MultiItemEntity {
    private String name;           // 子子项名称（如“2011年考研真题”）
    private boolean isChecked;     // 子子项是否单独选中

    public GrandChildItem(String name, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }

    // 必须实现：返回布局类型（外层Adapter用）
    @Override
    public int getItemType() {
        return MultiLevelAdapter.TYPE_GRANDCHILD;
    }

    // 布局资源（外层Adapter用）
    public int getLayoutRes() {
        return R.layout.ditem_grandchild;
    }

    // Getter & Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isChecked() { return isChecked; }
    public void setChecked(boolean checked) { isChecked = checked; }
}