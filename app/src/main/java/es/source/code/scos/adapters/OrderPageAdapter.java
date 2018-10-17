package es.source.code.scos.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import es.source.code.scos.fragments.FoodFragment;
import es.source.code.scos.fragments.OrderFragment;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.adapters
 * @date 2018/10/6 13:36
 * @description Fragment适配器
 */

public class OrderPageAdapter extends FragmentPagerAdapter {

    private final List<OrderFragment> mFragments;

    public OrderPageAdapter(FragmentManager fm, List<OrderFragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    /**
     * 根据位置返回对应的fragment
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
