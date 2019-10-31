package com.bennyhuo.github.network.entities

import com.bennyhuo.github.common.anno.PoKo

@PoKo
data class SubscriptionResponse(var subscribed: Boolean,
                                var ignored: Boolean,
                                var reason: Any?,
                                var created_at: String,
                                var url: String,
                                var repository_url: String)