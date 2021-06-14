package com.bennyhuo.github.settings

import com.bennyhuo.github.AppContext
import com.bennyhuo.github.common.sharedpreferences.Preference

/**
 * createBy	 keepon
 */
object Settings2{
    //委托
    var email:String by Preference(AppContext,"email","")
    var password:String by Preference(AppContext,"password","")
}