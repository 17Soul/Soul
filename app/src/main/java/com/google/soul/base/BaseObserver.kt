package com.google.soul.base

import android.util.Log
import com.google.gson.JsonParseException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

/**************************
作者：FYX
日期：2018/8/24 0024
 **************************/
abstract class BaseObserver<T> : Observer<T> {

    override fun onSubscribe(d: Disposable) {
        disposable(d)
    }

    override fun onNext(t: T) {
        success(t)
    }

    override fun onComplete() {}

    override fun onError(e: Throwable) {
        when (e) {
            is SocketTimeoutException -> {
                Log.e("===", e.message)
//                failure("连接超时，请重试")
            }
            is HttpException -> {
                Log.e("===", e.message)
//                failure("HttpException错误")
            }
            is JsonParseException -> {
                Log.e("===", e.message)
//                failure("解析错误")
            }
            is ConnectException -> {
                Log.e("===", e.message)
//                failure("连接失败，请检查网络")
            }
            is SSLHandshakeException -> {
                Log.e("===", e.message)
//                failure("证书验证失败")
            }
            else -> {
                Log.e("===", e.message)
//                failure("网络未知错误")
            }
        }
    }

    abstract fun success(data: T)

//    abstract fun failure(msg: String)

    abstract fun disposable(d: Disposable)
}