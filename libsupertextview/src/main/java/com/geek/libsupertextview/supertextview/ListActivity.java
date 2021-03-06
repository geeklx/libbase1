package com.geek.libsupertextview.supertextview;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.geek.libsupertextview.R;
import com.geek.libsupertextview.supertextview.adapter.NewsAdapter;
import com.geek.libsupertextview.supertextview.bean.NewsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allen on 2016/10/31.
 * 列表中使用
 */

public class ListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsAdapter adapter;
    private List<NewsBean> newsBeanList = new ArrayList<>();

    private String[] url = new String[]{
            "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1580482776,3214289843&fm=80&w=179&h=119&img.JPEG",
            "https://ss2.baidu.com/-vo3dSag_xI4khGko9WTAnF6hhy/vip/whcrop%3D176%2C106/sign=cffe5aed6363f6241c086f41e834d6c9/f3d3572c11dfa9ecb7f287c36ad0f703908fc102.jpg",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1383170885,2038374031&fm=80&w=179&h=119&img.JPEG",
            "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2218526845,522933158&fm=80&w=179&h=119&img.JPEG",
            "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=775731846,1370921386&fm=80",
            "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3860616424,1789830124&fm=80&w=179&h=119&img.PNG",
            "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=4096069947,473932952&fm=80&w=179&h=119&img.JPEG",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=3244336274,3651700434&fm=80&w=179&h=119&img.JPEG",
            "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=219781665,3032880226&fm=80&w=179&h=119&img.JPEG",
            "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=831306574,2792494868&fm=80&w=179&h=119&img.GIF",
            "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1580482776,3214289843&fm=80&w=179&h=119&img.JPEG",
            "https://ss2.baidu.com/-vo3dSag_xI4khGko9WTAnF6hhy/vip/whcrop%3D176%2C106/sign=cffe5aed6363f6241c086f41e834d6c9/f3d3572c11dfa9ecb7f287c36ad0f703908fc102.jpg",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1383170885,2038374031&fm=80&w=179&h=119&img.JPEG",
            "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2218526845,522933158&fm=80&w=179&h=119&img.JPEG",
            "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=775731846,1370921386&fm=80",
            "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3860616424,1789830124&fm=80&w=179&h=119&img.PNG",
            "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=4096069947,473932952&fm=80&w=179&h=119&img.JPEG",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=3244336274,3651700434&fm=80&w=179&h=119&img.JPEG",
            "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=219781665,3032880226&fm=80&w=179&h=119&img.JPEG",
            "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=831306574,2792494868&fm=80&w=179&h=119&img.GIF",

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new NewsAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter.addData(getData());
        recyclerView.setAdapter(adapter);

//        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Toast.makeText(ListActivity.this, position + "", Toast.LENGTH_SHORT).show();
//            }
//        });
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private List<NewsBean> getData() {
        for (int i = 0; i < url.length; i++) {
            NewsBean newsBean = new NewsBean();
            newsBean.setImgUrl(url[i]);
            newsBean.setTitle("新闻标题" + i);
            newsBean.setTime("在列表中使用的demo在列表中使用的demo在列表中使用的demo" + i);
            newsBeanList.add(newsBean);
        }
        return newsBeanList;
    }

    private List<NewsBean> getRefreshData() {
        newsBeanList.clear();
        for (int i = 0; i < 10; i++) {
            NewsBean newsBean = new NewsBean();
            newsBean.setImgUrl(url[i]);
            newsBean.setTitle("新标" + i);
            newsBean.setTime("在列表中使用的demo" + i);
            newsBeanList.add(newsBean);
        }
        return newsBeanList;
    }

    @Override
    public void onRefresh() {
        getRefreshData();
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
