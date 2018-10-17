package es.source.code.scos.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.adapters
 * @date 2018/10/7 17:50
 * @description
 */

public class ViewAdapter extends PagerAdapter {
    private List<View> mViewList;
    public ViewAdapter(List<View> viewList){
        this.mViewList = viewList;
    }
    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }
}
