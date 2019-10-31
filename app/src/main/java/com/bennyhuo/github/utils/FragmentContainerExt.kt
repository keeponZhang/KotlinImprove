package com.bennyhuo.github.utils

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by benny on 7/6/17.
 */
fun AppCompatActivity.showFragment(containerId: Int, clazz: Class<out Fragment>, vararg args: Pair<String, String>){
    supportFragmentManager.beginTransaction()
            .replace(containerId, clazz.newInstance().apply { arguments = Bundle().apply { args.forEach { putString(it.first, it.second) } } }, clazz.name)
            .commitAllowingStateLoss()
}

fun AppCompatActivity.showFragment(containerId: Int, clazz: Class<out Fragment>, args: Bundle){
    supportFragmentManager.beginTransaction()
            .replace(containerId, clazz.newInstance().apply { arguments = args }, clazz.name)
            .commitAllowingStateLoss()
}