package com.bennyhuo.github.network.entities

import com.bennyhuo.github.common.anno.PoKo
import retrofit2.adapter.rxjava.GitHubPaging

@PoKo
data class SearchRepositories2(var total_count: Int,
                               var incomplete_results: Boolean,
                               var items: GitHubPaging<Repository>)  {



}