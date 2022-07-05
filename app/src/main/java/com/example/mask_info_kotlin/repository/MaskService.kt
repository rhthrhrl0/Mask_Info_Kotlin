package com.example.mask_info_kotlin.repository

import com.example.mask_info_kotlin.model.StoreInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MaskService {
    //m=5000이라는 값은 하드코딩. 쿼리를 넣음.
    @GET("sample.json/?m=5000")
    suspend fun fetchStoreInfo( // suspend는 비동기로 동작할 코드인 것을 명시.
        @Query("lat") lat: Double,
        @Query("lng") lngz: Double
    ): StoreInfo //그냥 StoreInfo로 리턴 받을 수도 있음. 레트로핏에서 코틀린을 공식지원.
    //?를 지웠으므로 StoreInfo는 널일 수 없음. 또한 이 메서드가 리턴하는 값도 널일 수 없다.

    companion object {
        const val BASE_URL =
            "https://gist.githubusercontent.com/junsuk5/bb7485d5f70974deee920b8f0cd1e2f0/raw/063f64d9b343120c2cb01a6555cf9b38761b1d94/"
    }
}