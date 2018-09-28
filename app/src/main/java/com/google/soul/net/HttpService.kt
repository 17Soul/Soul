package com.google.soul.net

import com.google.soul.mvp.model.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**************************
作者：FYX
日期：2018/8/24 0024
 **************************/
interface HttpService {

    @GET("/HPImageArchive.aspx?format=js&idx=0&n=10")
    fun getBanner(): Observable<BannerEntity>

    @GET("/v2/movie/{type}")
    fun getMovie(@Path("type") type: String, @Query("start") start: Int, @Query("count") count: Int): Observable<MovieEntity>

    @GET("/v2/movie/subject/{id}")
    fun getDetailMovie(@Path("id") id: String): Observable<MovieDetailEntity>

    @GET("/v2/movie/us_box")
    fun getUsBox(): Observable<USBoxEntity>

    @GET("/v2/movie/celebrity/{id}")
    fun getCharacter(@Path("id") id: String): Observable<CharacterEntity>

    @GET("/v2/movie/search")
    fun searchMovieByQ(@Query("q") keyword: String): Observable<MovieEntity>

    @GET("/v2/movie/search")
    fun searchMovieByTag(@Query("tag") tag: String): Observable<MovieEntity>
}