package com.google.soul.net

import com.google.soul.base.BaseObserver
import com.google.soul.mvp.model.*
import com.orhanobut.logger.Logger
import io.reactivex.ObservableTransformer
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.FormBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset
import java.util.HashMap
import java.util.concurrent.TimeUnit

/**************************
作者：FYX
日期：2018/8/24 0024
 **************************/
object RetrofitManager {

    //豆瓣电影
    private const val baseUrlDouban = "https://api.douban.com"
    //必应每日图库
    private const val baseUrlBing = "http://www.bing.com"

    private var doubanHttpService: HttpService? = null
    private var bingHttpService: HttpService? = null

    private val okHttpClient = OkHttpClient().newBuilder().addInterceptor { chain ->
        val request = chain.request()
        val paramMap = HashMap<String, Any>()
        val requestBody = request.body()
        if (requestBody is FormBody) {
            for (i in 0 until requestBody.size()) {
                paramMap[requestBody.encodedName(i)] = requestBody.encodedValue(i)
            }
        }

        val time1 = System.currentTimeMillis()
        val response = chain.proceed(request)
        val responseBody = response.body()
        val responseContentLength = responseBody?.contentLength()
        val buffer = (responseBody?.source().apply { this?.request(Long.MAX_VALUE) })?.buffer()
        var charset = Charset.forName("UTF-8")
        val contentType = responseBody?.contentType()
        charset = contentType?.charset(charset)
        val time2 = System.currentTimeMillis()

        Logger.e("${request.url()}\n请求参数:$paramMap\n请求耗时:${(time2 - time1)}ms")
        if (responseContentLength != 0L) {
            Logger.json(buffer?.clone()?.readString(charset))
        }
        response
    }
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()

    /**
     * 获取豆瓣Service对象
     */
    private fun getDoubanHttpService(): HttpService {
        if (doubanHttpService == null) {
            doubanHttpService = Retrofit.Builder()
                    .baseUrl(baseUrlDouban)
                    .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)//增加拦截器
                    .build()
                    .create(HttpService::class.java)
        }
        return doubanHttpService!!
    }

    /**
     * 获取必应Service对象
     */
    private fun getBingHttpService(): HttpService {
        if (bingHttpService == null) {
            bingHttpService = Retrofit.Builder()
                    .baseUrl(baseUrlBing)
                    .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)//增加拦截器
                    .build()
                    .create(HttpService::class.java)
        }
        return bingHttpService!!
    }

    /**
     * 封装线程管理和订阅的过程
     */
    private fun <T> networkScheduler(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun getBanner(observer: Observer<BannerEntity>) {
        getBingHttpService().getBanner().compose(networkScheduler()).subscribe(observer)
    }

    fun getMovie(type: String, start: Int, count: Int, observer: Observer<MovieEntity>) {
        getDoubanHttpService().getMovie(type, start, count).compose(networkScheduler()).subscribe(observer)
    }

    fun getDetailMovie(id: String, observer: Observer<MovieDetailEntity>) {
        getDoubanHttpService().getDetailMovie(id).compose(networkScheduler()).subscribe(observer)
    }

    fun getUsBox(observer: BaseObserver<USBoxEntity>) {
        getDoubanHttpService().getUsBox().compose(networkScheduler()).subscribe(observer)
    }

    fun getCharacter(id: String, observer: Observer<CharacterEntity>) {
        getDoubanHttpService().getCharacter(id).compose(networkScheduler()).subscribe(observer)
    }

    fun searchMovieByQ(keyword: String, observer: Observer<MovieEntity>) {
        getDoubanHttpService().searchMovieByQ(keyword).compose(networkScheduler()).subscribe(observer)
    }

    fun searchMovieByTag(tag: String, observer: Observer<MovieEntity>) {
        getDoubanHttpService().searchMovieByTag(tag).compose(networkScheduler()).subscribe(observer)
    }
}