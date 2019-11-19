package com.bennyhuo.github.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.util.Log
import com.bennyhuo.github.utils.ViewPagerAdapterList
import com.bennyhuo.github.view.config.FragmentPage

class CommonViewPageAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    val fragmentPages = ViewPagerAdapterList<FragmentPage>(this)

    override fun getItem(position: Int): Fragment {
        return fragmentPages[position].fragment
    }

    override fun getCount(): Int {
        Log.e("TAG", "CommonViewPageAdapter getCount:" +fragmentPages.size);
        return fragmentPages.size
    }

    override fun getItemPosition(fragment: Any): Int {
        for ((index, page) in fragmentPages.withIndex()){
            if(fragment == page.fragment){
                Log.e("TAG", "CommonViewPageAdapter getItemPosition index:" );
                return index
            }
        }
        Log.e("TAG", "CommonViewPageAdapter getItemPosition POSITION_NONE:" );
        return PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentPages[position].title
    }

}