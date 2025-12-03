// MultiLevelAdapter.java
package com.fosung.lighthouse.fosunglibs1.djxz;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fosung.lighthouse.fosunglibs1.R;

import java.util.ArrayList;
import java.util.List;

public class MultiLevelAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_PARENT = 0;
    public static final int TYPE_CHILD = 1;
    public static final int TYPE_GRANDCHILD = 2;

    public interface OnParentUpdateListener {
        void onParentNeedsUpdate();
    }

    private OnParentUpdateListener parentUpdateListener;

    public MultiLevelAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_PARENT, R.layout.ditem_parent);
        addItemType(TYPE_CHILD, R.layout.ditem_child);
        addItemType(TYPE_GRANDCHILD, R.layout.ditem_grandchild);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, MultiItemEntity item) {
        int itemViewType = item.getItemType();
        if (itemViewType == TYPE_PARENT) {
            handleParentItem(holder, (ParentItem) item);
        } else if (itemViewType == TYPE_CHILD) {
            handleChildItem(holder, (ChildItem) item);
        } else if (itemViewType == TYPE_GRANDCHILD) {
            handleGrandChildItem(holder, (GrandChildItem) item);
        }
    }

    private void handleParentItem(BaseViewHolder holder, ParentItem parent) {
        CheckBox cbParent = holder.getView(R.id.cb_parent);
        TextView tvParent = holder.getView(R.id.cb_parent2);
        RecyclerView rvChild = holder.getView(R.id.rv_child);

        tvParent.setText(parent.getTitle());
        updateParentState(parent);
        cbParent.setChecked(parent.isModuleChecked());
        rvChild.setVisibility(parent.isExpanded() ? View.VISIBLE : View.GONE);

        cbParent.setOnClickListener(v -> {
            boolean isChecked = cbParent.isChecked();
            parent.setModuleChecked(isChecked);

            for (ChildItem child : parent.getChildList()) {
                child.setModuleChecked(isChecked);
                for (GrandChildItem grandChild : child.getGrandChildList()) {
                    grandChild.setChecked(isChecked);
                }
            }

            notifyDataSetChanged();
        });

        ChildAdapter childAdapter = new ChildAdapter(parent.getChildList(), this);
        rvChild.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        rvChild.setAdapter(childAdapter);
    }

    private void handleChildItem(BaseViewHolder holder, ChildItem item) {
        CheckBox cbChild = holder.getView(R.id.cb_child);
        ImageView ivToggle = holder.getView(R.id.cb_child2);
        RecyclerView rvGrandchild = holder.getView(R.id.rv_grandchild);

        updateChildState(item);
        cbChild.setText(item.getModuleName());
        cbChild.setChecked(item.isModuleChecked());
        rvGrandchild.setVisibility(item.isExpanded() ? View.VISIBLE : View.GONE);
        ivToggle.setBackgroundResource(item.isExpanded() ? R.mipmap.icon_sqq1 : R.mipmap.icon_sqq11);

        ivToggle.setOnClickListener(v -> {
            item.setExpanded(!item.isExpanded());
            rvGrandchild.setVisibility(item.isExpanded() ? View.VISIBLE : View.GONE);
            ivToggle.setBackgroundResource(item.isExpanded() ? R.mipmap.icon_sqq1 : R.mipmap.icon_sqq11);
        });

        GrandChildAdapter grandChildAdapter = new GrandChildAdapter(item.getGrandChildList(), this);
        rvGrandchild.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        rvGrandchild.setAdapter(grandChildAdapter);

        cbChild.setOnClickListener(v -> {
            boolean isChecked = cbChild.isChecked();
            item.setModuleChecked(isChecked);

            for (GrandChildItem grandChild : item.getGrandChildList()) {
                grandChild.setChecked(isChecked);
            }

            // 更新父项状态
            for (MultiItemEntity entity : getData()) {
                if (entity instanceof ParentItem) {
                    ParentItem parent = (ParentItem) entity;
                    if (parent.getChildList().contains(item)) {
                        updateParentState(parent);
                        break;
                    }
                }
            }

            notifyDataSetChanged();
        });
    }

    private void handleGrandChildItem(BaseViewHolder holder, GrandChildItem item) {
        CheckBox cbGrandchild = holder.getView(R.id.cb_grandchild);
        cbGrandchild.setText(item.getName());
        cbGrandchild.setChecked(item.isChecked());

        cbGrandchild.setOnClickListener(v -> {
            boolean isChecked = cbGrandchild.isChecked();
            item.setChecked(isChecked);

            // 更新父项状态
            for (MultiItemEntity entity : getData()) {
                if (entity instanceof ParentItem) {
                    ParentItem parent = (ParentItem) entity;
                    for (ChildItem child : parent.getChildList()) {
                        if (child.getGrandChildList().contains(item)) {
                            updateChildState(child);
                            updateParentState(parent);
                            break;
                        }
                    }
                }
            }

            notifyDataSetChanged();
        });
    }

    // 直接根据子子项状态更新父项
    private void updateParentState(ParentItem parent) {
        boolean allChecked = true;
        for (ChildItem child : parent.getChildList()) {
            for (GrandChildItem grandChild : child.getGrandChildList()) {
                if (!grandChild.isChecked()) {
                    allChecked = false;
                    break;
                }
            }
            if (!allChecked) break;
        }
        parent.setModuleChecked(allChecked);
    }

    // 根据子子项状态更新子项
    private void updateChildState(ChildItem child) {
        boolean allChecked = true;
        for (GrandChildItem grandChild : child.getGrandChildList()) {
            if (!grandChild.isChecked()) {
                allChecked = false;
                break;
            }
        }
        child.setModuleChecked(allChecked);
    }

    public void setOnParentUpdateListener(OnParentUpdateListener listener) {
        this.parentUpdateListener = listener;
    }

    // ========== 修复后的数据获取方法 ==========

    /**
     * 获取所有子子项（包括选中与未选中）
     */
    public List<GrandChildItem> getAllGrandChildItems() {
        List<GrandChildItem> allItems = new ArrayList<>();
        for (MultiItemEntity entity : getData()) {
            if (entity instanceof ParentItem) {
                ParentItem parent = (ParentItem) entity;
                for (ChildItem child : parent.getChildList()) {
                    allItems.addAll(child.getGrandChildList());
                }
            }
        }
        return allItems;
    }

    /**
     * 获取所有选中的子子项
     */
    public List<GrandChildItem> getSelectedGrandChildItems() {
        List<GrandChildItem> selectedItems = new ArrayList<>();
        for (MultiItemEntity entity : getData()) {
            if (entity instanceof ParentItem) {
                ParentItem parent = (ParentItem) entity;
                for (ChildItem child : parent.getChildList()) {
                    for (GrandChildItem grandChild : child.getGrandChildList()) {
                        if (grandChild.isChecked()) {
                            selectedItems.add(grandChild);
                        }
                    }
                }
            }
        }
        return selectedItems;
    }

    /**
     * 获取完整的层级数据结构（包含所有项目及其状态）
     */
    public List<ParentItem> getFullHierarchyData() {
        // 先更新所有状态确保一致性
        refreshAllStates();

        // 返回数据的深拷贝
        List<ParentItem> result = new ArrayList<>();
        for (MultiItemEntity entity : getData()) {
            if (entity instanceof ParentItem) {
                ParentItem original = (ParentItem) entity;
                ParentItem copy = new ParentItem(
                        original.getTitle(),
                        original.isModuleChecked(),
                        original.isExpanded(),
                        new ArrayList<>(original.getChildList())
                );

                List<ChildItem> copiedChildren = new ArrayList<>();
                for (ChildItem child : original.getChildList()) {
                    ChildItem childCopy = new ChildItem(
                            child.getModuleName(),
                            child.isModuleChecked(),
                            child.isExpanded(),
                            new ArrayList<>(child.getGrandChildList())
                    );

                    List<GrandChildItem> copiedGrandChildren = new ArrayList<>();
                    for (GrandChildItem grandChild : child.getGrandChildList()) {
                        copiedGrandChildren.add(new GrandChildItem(
                                grandChild.getName(),
                                grandChild.isChecked()
                        ));
                    }
                    childCopy.getGrandChildList().clear();
                    childCopy.getGrandChildList().addAll(copiedGrandChildren);

                    copiedChildren.add(childCopy);
                }
                copy.getChildList().clear();
                copy.getChildList().addAll(copiedChildren);

                result.add(copy);
            }
        }
        return result;
    }

    /**
     * 获取扁平化的所有项目列表（包含选中与未选中）
     */
    public List<SelectionItem> getAllFlattenedItems() {
        // 先更新所有状态确保一致性
        refreshAllStates();

        List<SelectionItem> result = new ArrayList<>();
        int parentIndex = 0;

        for (MultiItemEntity entity : getData()) {
            if (entity instanceof ParentItem) {
                ParentItem parent = (ParentItem) entity;
                int childIndex = 0;

                for (ChildItem child : parent.getChildList()) {
                    int grandChildIndex = 0;

                    for (GrandChildItem grandChild : child.getGrandChildList()) {
                        result.add(new SelectionItem(
                                parentIndex,
                                parent.getTitle(),
                                childIndex,
                                child.getModuleName(),
                                grandChildIndex,
                                grandChild.getName(),
                                grandChild.isChecked()
                        ));
                        grandChildIndex++;
                    }

                    result.add(new SelectionItem(
                            parentIndex,
                            parent.getTitle(),
                            childIndex,
                            child.getModuleName(),
                            -1,
                            null,
                            child.isModuleChecked()
                    ));

                    childIndex++;
                }

                result.add(new SelectionItem(
                        parentIndex,
                        parent.getTitle(),
                        -1,
                        null,
                        -1,
                        null,
                        parent.isModuleChecked()
                ));

                parentIndex++;
            }
        }

        return result;
    }

    // 刷新所有状态确保一致性
    private void refreshAllStates() {
        for (MultiItemEntity entity : getData()) {
            if (entity instanceof ParentItem) {
                ParentItem parent = (ParentItem) entity;
                updateParentState(parent);

                for (ChildItem child : parent.getChildList()) {
                    updateChildState(child);
                }
            }
        }
    }

    // 辅助类：表示选中的项目及其层级信息
    public static class SelectionItem {
        public int parentIndex;
        public String parentTitle;
        public int childIndex;
        public String childTitle;
        public int grandChildIndex;
        public String grandChildTitle;
        public boolean isSelected;

        public SelectionItem(int parentIndex, String parentTitle,
                             int childIndex, String childTitle,
                             int grandChildIndex, String grandChildTitle,
                             boolean isSelected) {
            this.parentIndex = parentIndex;
            this.parentTitle = parentTitle;
            this.childIndex = childIndex;
            this.childTitle = childTitle;
            this.grandChildIndex = grandChildIndex;
            this.grandChildTitle = grandChildTitle;
            this.isSelected = isSelected;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Parent: ").append(parentTitle);

            if (childIndex >= 0) {
                sb.append(" | Child: ").append(childTitle);
            }

            if (grandChildIndex >= 0) {
                sb.append(" | Item: ").append(grandChildTitle);
            }

            sb.append(" (").append(isSelected ? "Selected" : "Unselected").append(")");
            return sb.toString();
        }
    }

    // ========== 子项Adapter ==========
    private static class ChildAdapter extends BaseQuickAdapter<ChildItem, BaseViewHolder> {
        private final MultiLevelAdapter parentAdapter;

        public ChildAdapter(List<ChildItem> data, MultiLevelAdapter parentAdapter) {
            super(R.layout.ditem_child, data);
            this.parentAdapter = parentAdapter;
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, ChildItem item) {
            parentAdapter.handleChildItem(helper, item);
        }
    }

    // ========== 子子项Adapter ==========
    private static class GrandChildAdapter extends BaseQuickAdapter<GrandChildItem, BaseViewHolder> {
        private final MultiLevelAdapter parentAdapter;

        public GrandChildAdapter(List<GrandChildItem> data, MultiLevelAdapter parentAdapter) {
            super(R.layout.ditem_grandchild, data);
            this.parentAdapter = parentAdapter;
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, GrandChildItem item) {
            CheckBox cbGrandchild = helper.getView(R.id.cb_grandchild);
            cbGrandchild.setText(item.getName());
            cbGrandchild.setChecked(item.isChecked());

            cbGrandchild.setOnClickListener(v -> {
                boolean isChecked = cbGrandchild.isChecked();
                item.setChecked(isChecked);

                if (parentAdapter.parentUpdateListener != null) {
                    parentAdapter.parentUpdateListener.onParentNeedsUpdate();
                }
            });
        }
    }
}