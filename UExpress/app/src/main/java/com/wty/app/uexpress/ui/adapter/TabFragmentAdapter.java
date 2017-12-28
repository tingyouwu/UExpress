package com.wty.app.uexpress.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wty.app.uexpress.ui.BaseFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author wty
 * @Description
 *
 * FragmentStatePagerAdapter
 * 在内存中最多会保留三个Fragment实例，
 * 也就是说当有Fragment切换的时候，Fragment生命周期会走onDestroyView()-->onDestroy()方法，
 * 然后创建会走onCreate()-->onCreateView()方法。
 * FragmentPagerAdapter
 * 内存中会保留所有创建后的Fragment，也就是说当Fragment切换的时候，生命周期会走onDestroyView()和onCreateView()方法，
 * 但是不会走onDestroy()和onCreate()方法。
 * 不管是哪一种PagerAdapter，ViewPager都存在预加载Fragment的效果
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {

    private List<String> titles = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();

    public TabFragmentAdapter(Map<String,BaseFragment> fragments, FragmentManager fm) {
        super(fm);
        initFragment(fragments);

    }

    /**
     * 功能描述：把title和fragment分别取出来
     **/
    private void initFragment(Map<String ,BaseFragment> fragments){
        Iterator<Map.Entry<String,BaseFragment>> iterator = fragments.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String ,BaseFragment> entry = iterator.next();
            this.titles.add(entry.getKey());
            this.fragments.add(entry.getValue());
        }
    }

    @Override
    public BaseFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
