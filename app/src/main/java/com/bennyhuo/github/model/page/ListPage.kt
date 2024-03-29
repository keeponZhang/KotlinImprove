package com.bennyhuo.github.model.page

import android.util.Log
import com.bennyhuo.common.log.logger
import retrofit2.adapter.rxjava.GitHubPaging
import rx.Observable

abstract class ListPage<DataType> : DataProvider<DataType> {
    companion object {
        const val PAGE_SIZE = 20
    }

    var currentPage = 1
        private set

    val data = GitHubPaging<DataType>()

    fun loadMore(): Observable<GitHubPaging<DataType>> = getData(currentPage + 1)
            .doOnNext {
                currentPage = currentPage + 1
            }
            .doOnError {
                logger.error("loadMore Error", it)
            }
            .map {
                data.mergeData(it)
                data
            }

    fun loadFromFirst(pageCount: Int = currentPage) =
            //刷新的时候可能会调用到
            Observable.range(1, pageCount)
                    .concatMap {
                        Log.e("TAG", "ListPage loadFromFirst:" + it);
                        getData(it)
                    }
                    .doOnError {
                        logger.error("loadFromFirst, pageCount=$pageCount", it)
                    }
                    .reduce { acc, page ->
                        acc.mergeData(page)
                    }
                    .doOnNext {
                        data.clear()
                        data.mergeData(it)
                    }
}