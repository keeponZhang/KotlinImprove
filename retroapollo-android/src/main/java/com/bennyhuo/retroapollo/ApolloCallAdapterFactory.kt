package com.bennyhuo.retroapollo

import android.util.Log
import com.apollographql.apollo.ApolloCall
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApolloCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type): CallAdapter<*, *>? {
        if (getRawType(returnType) == ApolloCall::class.java) {
            val responseType = getParameterUpperBound(0, returnType as ParameterizedType)
            Log.e("TAG", "ApolloCallAdapterFactory get responseType:" + responseType);
            if (responseType is ParameterizedType) {
                return null
            }
            return object : CallAdapter<Any, Any> {
                override fun responseType() = responseType
                override fun adapt(call: ApolloCall<Any>) = call
            }
        }
        return null
    }
}