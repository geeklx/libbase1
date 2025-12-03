package com.fosung.lighthouse.fosunglibs1.djxz;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fosung.lighthouse.fosunglibs1.R;

import java.util.List;

public class ParentItem implements MultiItemEntity {
    private String title;          // 父项标题（如“破解考研 强化版（英语一）”）
    private boolean isExpanded;    // 子项是否展开
    private boolean isModuleChecked; // 子项是否全选（所有子子项都选中时为true）
    private List<ChildItem> childList; // 子项列表

    public ParentItem(String title, boolean isModuleChecked, boolean isExpanded, List<ChildItem> childList) {
        this.title = title;
        this.isModuleChecked = isModuleChecked;
        this.isExpanded = isExpanded;
        this.childList = childList;
    }

    public boolean isModuleChecked() {
        return isModuleChecked;
    }

    public void setModuleChecked(boolean moduleChecked) {
        isModuleChecked = moduleChecked;
    }

    // 必须实现：返回布局类型（外层Adapter用）
    @Override
    public int getItemType() {
        return MultiLevelAdapter.TYPE_PARENT;
    }

    // 布局资源（外层Adapter用）
    public int getLayoutRes() {
        return R.layout.ditem_parent;
    }

    // Getter & Setter
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
    public List<ChildItem> getChildList() { return childList; }
    public void setChildList(List<ChildItem> childList) { this.childList = childList; }
}