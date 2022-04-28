package com.geek.libviewpagerbottomsheet.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.geek.libviewpagerbottomsheet.BottomSheetUtils;
import com.geek.libviewpagerbottomsheet.R;
import com.geek.libviewpagerbottomsheet.ViewPagerBottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
/**
 * @author fosung
 * Viewpager+Bottomsheet滑动菜单三方库功能
 * com.github.geeklx.libbase1:libviewpagerbottomsheet:2.0.6
 */
public class MainActivitySheet extends AppCompatActivity {

    private ViewPagerBottomSheetBehavior mBottomSheetBehavior;
    private ViewPager mViewPager;
    private LinearLayout bottomSheet;
    private PagerAdapterMain mPagerAdapter;
    private ImageView tvType;
    private ImageView tvTouchView;
    private TabLayout activity_main_tab;
    private RecyclerView recyclerView1;
    private int HeaderHeight;
    private float lookTvOffset;
    private ViewItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_sheet);

        activity_main_tab = findViewById(R.id.activity_main_tab);
        tvTouchView = findViewById(R.id.tvTouchView);
        tvType = findViewById(R.id.tv_type);
        recyclerView1 = findViewById(R.id.recycler_list);
        bottomSheet = findViewById(R.id.bottom_sheet);
        mViewPager = findViewById(R.id.activity_main_vp);
        HeaderHeight = getScreenWidth(this) * 400;
        lookTvOffset = HeaderHeight - dp2px(this, 120);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(mLinearLayoutManager);
//        recyclerView1.setLayoutManager(new GridLayoutManager(this, 1));
        ArrayList<String> populationFakeBeans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            populationFakeBeans.add("Item " + i);
        }
        mAdapter = new ViewItemAdapter(populationFakeBeans);
//        initFakeData();
        recyclerView1.setAdapter(mAdapter);

        // setup viewpager
        mPagerAdapter = new PagerAdapterMain(getSupportFragmentManager(), this, PagerAdapterMain.TabItem.RECYCLER, PagerAdapterMain.TabItem.NESTED_SCROLL);
        mViewPager.setAdapter(mPagerAdapter);
        tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setHideable(false);
                mBottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_COLLAPSED);
                mBottomSheetBehavior.setPeekHeight(210);
                tvTouchView.setVisibility(View.VISIBLE);
            }
        });
        tvTouchView.setVisibility(View.GONE);
        mBottomSheetBehavior = ViewPagerBottomSheetBehavior.from(bottomSheet);
//        mBottomSheetBehavior.setHalfExpandedOffset(HeaderHeight);
//        mBottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_HALF_EXPANDED);
//        mBottomSheetBehavior.setFitToContents(false);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(HeaderHeight);
        mBottomSheetBehavior.addBottomSheetCallback(new ViewPagerBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                lookTvOffset = slideOffset - dp2px(MainActivity.this, 140);
//                Log.e("aaaaa", bottomSheet+"onSlide: "+slideOffset);
                if (slideOffset <= 0) {
                    tvTouchView.setVisibility(View.GONE);
                } else {
                    tvTouchView.setVisibility(View.VISIBLE);
                }
            }
        });
        activity_main_tab.setupWithViewPager(mViewPager);
        BottomSheetUtils.setupViewPager(mViewPager);
    }

//    private void initFakeData() {
//        ArrayList<String> populationFakeBeans = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            populationFakeBeans.add("Item " + i);
//        }
//        mAdapter.setNewData(populationFakeBeans);
//    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int dp2px(Context context, final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}