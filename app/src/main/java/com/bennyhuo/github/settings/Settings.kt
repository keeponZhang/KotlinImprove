package com.bennyhuo.github.settings

import com.bennyhuo.github.AppContext
import com.bennyhuo.github.R
import com.bennyhuo.github.model.account.AccountManager
import com.bennyhuo.github.utils.pref

object Settings {
    var lastPage: Int
        get() = if(lastPageIdString.isEmpty()) 0 else AppContext.resources.getIdentifier(lastPageIdString, "id", AppContext.packageName)
        set(value) {
            lastPageIdString = AppContext.resources.getResourceEntryName(value)
        }

    val defaultPage
        get() = if(AccountManager.isLoggedIn()) defaultPageForUser else defaultPageForVisitor
    //Preference是委托类，R.id.navRepos是Int类型，所以defaultPageForUser也是Int
    //其实下面两个没有用到get方法，没必要用委托
    private var defaultPageForUser by pref(R.id.navRepos)

    private var defaultPageForVisitor by pref(R.id.navRepos)

    private var lastPageIdString by pref("")

    var themeMode by pref("DAY")
}