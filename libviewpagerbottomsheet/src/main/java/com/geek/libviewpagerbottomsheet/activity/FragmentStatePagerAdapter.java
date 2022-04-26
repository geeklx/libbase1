package com.geek.libviewpagerbottomsheet.activity;

import android.content.Context;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.geek.libviewpagerbottomsheet.R;

class PagerAdapterMain extends FragmentPagerAdapter {
    public enum TabItem {
        RECYCLER(FragmentMain.class, R.string.tab_recycler),
        NESTED_SCROLL(FragmentAux.class, R.string.tab_nested_scroll);
        private final Class<? extends Fragment> fragmentClass;
        private final int titleResId;

        TabItem(Class<? extends Fragment> fragmentClass, @StringRes int titleResId) {
            this.fragmentClass = fragmentClass;
            this.titleResId = titleResId;
        }
    }

    private final TabItem[] tabItems;
    private final Context context;

    public PagerAdapterMain(FragmentManager fragmentManager, Context context, TabItem... tabItems) {
        super(fragmentManager);
        this.context = context;
        this.tabItems = tabItems;
    }

    @Override
    public Fragment getItem(int position) {
        return newInstance(tabItems[position].fragmentClass);
    }

    private Fragment newInstance(Class<? extends Fragment> fragmentClass) {
        try {
            return fragmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("fragment must have public no-arg constructor: " + fragmentClass.getName(), e);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(tabItems[position].titleResId);
    }

    @Override
    public int getCount() {
        return tabItems.length;
    }
//    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
//    private List<Fragment> mFragments = new ArrayList<>();
//    private ArrayList<String> list = new ArrayList<>();
//    private Context mContext;
//    private int currentPage = 0;
//
//    public PagerAdapterMain(Context context, FragmentManager fragmentManager) {
//        super(fragmentManager);
//        mContext = context;
//        // setup fragments
//        mFragments.add(FragmentMain.newInstance());
//        mFragments.add(FragmentAux.newInstance());
//        list.add("列表1");
//        list.add("列表2");
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return mFragments.get(position);
//    }
//
//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return list.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        Fragment fragment = (Fragment) super.instantiateItem(container, position);
//        registeredFragments.put(position, fragment);
//        return fragment;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        registeredFragments.remove(position);
//        super.destroyItem(container, position, object);
//    }
//
//    public Fragment getRegisteredFragment(int position) {
//        return registeredFragments.get(position);
//    }
//
//    public final int getCurrentPage() {
//        return currentPage;
//    }
//
//    public void setCurrentPage(int currentPage) {
//        this.currentPage = currentPage;
//    }
}
