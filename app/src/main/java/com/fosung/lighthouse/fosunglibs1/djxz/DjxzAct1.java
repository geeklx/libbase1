package com.fosung.lighthouse.fosunglibs1.djxz;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fosung.lighthouse.fosunglibs1.R;

import java.util.ArrayList;
import java.util.List;

public class DjxzAct1 extends AppCompatActivity {

    MultiLevelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maingeek4);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<MultiItemEntity> data = buildMockData();
        adapter = new MultiLevelAdapter(data);

        adapter.setOnParentUpdateListener(() -> {
            // 刷新整个列表以更新所有状态
            adapter.notifyDataSetChanged();
            showSelectedData();
        });

        recyclerView.setAdapter(adapter);
    }

    private void showSelectedData() {
        // 1. 获取所有子子项（包括未选中的）
        List<GrandChildItem> allGrandChildItems = adapter.getAllGrandChildItems();
        Log.d("SelectedData", "All GrandChild Items: " + allGrandChildItems.size());
        for (GrandChildItem item : allGrandChildItems) {
            Log.d("SelectedData", " - " + item.getName() + ": " + item.isChecked());
        }

        // 2. 获取所有选中的子子项
        List<GrandChildItem> selectedGrandChildItems = adapter.getSelectedGrandChildItems();
        Log.d("SelectedData", "Selected GrandChild Items: " + selectedGrandChildItems.size());
        for (GrandChildItem item : selectedGrandChildItems) {
            Log.d("SelectedData", " - " + item.getName() + ": " + item.isChecked());
        }

        // 3. 获取完整的层级数据
        List<ParentItem> fullData = adapter.getFullHierarchyData();
        Log.d("SelectedData", "Full Hierarchy Data:");
        for (ParentItem parent : fullData) {
            Log.d("SelectedData", "Parent: " + parent.getTitle() + " (" + parent.isModuleChecked() + ")");
            for (ChildItem child : parent.getChildList()) {
                Log.d("SelectedData", "  Child: " + child.getModuleName() + " (" + child.isModuleChecked() + ")");
                for (GrandChildItem grandChild : child.getGrandChildList()) {
                    Log.d("SelectedData", "    GrandChild: " + grandChild.getName() + " (" + grandChild.isChecked() + ")");
                }
            }
        }

        // 4. 获取所有扁平化项目
        List<MultiLevelAdapter.SelectionItem> allFlattened = adapter.getAllFlattenedItems();
        Log.d("SelectedData", "All Flattened Items:");
        for (MultiLevelAdapter.SelectionItem item : allFlattened) {
            Log.d("SelectedData", item.toString());
        }

        // 5. 显示提示信息
        Toast.makeText(this, "共 " + allGrandChildItems.size() + " 个子项，选中 " + selectedGrandChildItems.size() + " 个", Toast.LENGTH_SHORT).show();
    }


    // 构建模拟数据（可根据需求动态修改）
    private List<MultiItemEntity> buildMockData() {
        List<MultiItemEntity> data = new ArrayList<>();

        // ========== 第一个父项：破解考研 强化版（英语一） ==========
        ParentItem parent1 = new ParentItem("破解考研 强化版（英语一）", true, true, new ArrayList<>());

        // 子项1：词汇通关
        ChildItem child1 = new ChildItem("词汇通关", true, true, new ArrayList<>());
        child1.getGrandChildList().add(new GrandChildItem("词汇通关选项1", true));
        child1.getGrandChildList().add(new GrandChildItem("词汇通关选项2", true));
        parent1.getChildList().add(child1);

        // 子项2：语法&长难句
        ChildItem child2 = new ChildItem("语法&长难句", true, true, new ArrayList<>());
        child2.getGrandChildList().add(new GrandChildItem("语法选项1", true));
        child2.getGrandChildList().add(new GrandChildItem("语法选项2", true));
        parent1.getChildList().add(child2);

        // 子项3：历年真题（2011-2016）
        ChildItem child3 = new ChildItem("历年真题", true, true, new ArrayList<>());
        for (int year = 2011; year <= 2016; year++) {
            child3.getGrandChildList().add(new GrandChildItem(year + "年考研真题", true));
        }
        parent1.getChildList().add(child3);

        data.add(parent1);


        // ========== 第二个父项：破解考研 强化版（政治） ==========
        ParentItem parent2 = new ParentItem("破解考研 强化版（政治）", true, true, new ArrayList<>());

        // 子项：马原
        ChildItem child4 = new ChildItem("马原", true, true, new ArrayList<>());
        child4.getGrandChildList().add(new GrandChildItem("马原选项1", true));
        child4.getGrandChildList().add(new GrandChildItem("马原选项2", true));
        parent2.getChildList().add(child4);

        // 子项：史纲
        ChildItem child5 = new ChildItem("史纲", true, true, new ArrayList<>());
        child5.getGrandChildList().add(new GrandChildItem("史纲选项1", true));
        child5.getGrandChildList().add(new GrandChildItem("史纲选项2", true));
        parent2.getChildList().add(child5);

        // 子项：真题（2017-2026）
        ChildItem child6 = new ChildItem("历年真题", true, true, new ArrayList<>());
        for (int year = 2017; year <= 2026; year++) {
            child6.getGrandChildList().add(new GrandChildItem(year + "年考研真题", true));
        }
        parent2.getChildList().add(child6);

        data.add(parent2);


        // ========== 第三个父项：其他模块（示例） ==========
        ParentItem parent3 = new ParentItem("新大纲 方", true, true, new ArrayList<>());
        ChildItem child7 = new ChildItem("新题型", true, true, new ArrayList<>());
        child7.getGrandChildList().add(new GrandChildItem("新题型选项A", true));
        child7.getGrandChildList().add(new GrandChildItem("新题型选项B", true));
        parent3.getChildList().add(child7);
        data.add(parent3);

        return data;
    }

}