package com.fosung.lighthouse.fosunglibs1.djxz;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fosung.lighthouse.fosunglibs1.R;

import java.util.List;

public class ChildItem implements MultiItemEntity {
    private String moduleName;     // 子项标题（如“词汇通关”）
    private boolean isModuleChecked; // 子项是否全选（所有子子项都选中时为true）
    private boolean isExpanded;    // 子项是否展开
    private List<GrandChildItem> grandChildList; // 子子项列表

    public ChildItem(String moduleName, boolean isModuleChecked, boolean isExpanded,List<GrandChildItem> grandChildList) {
        this.moduleName = moduleName;
        this.isModuleChecked = isModuleChecked;
        this.isExpanded = isExpanded;
        this.grandChildList = grandChildList;
    }

    // 必须实现：返回布局类型（外层Adapter用）
    @Override
    public int getItemType() {
        return MultiLevelAdapter.TYPE_CHILD;
    }

    // 布局资源（外层Adapter用）
    public int getLayoutRes() {
        return R.layout.ditem_child;
    }

    // Getter & Setter
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    public boolean isModuleChecked() { return isModuleChecked; }
    public void setModuleChecked(boolean moduleChecked) { isModuleChecked = moduleChecked; }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public List<GrandChildItem> getGrandChildList() { return grandChildList; }
    public void setGrandChildList(List<GrandChildItem> grandChildList) { this.grandChildList = grandChildList; }
}