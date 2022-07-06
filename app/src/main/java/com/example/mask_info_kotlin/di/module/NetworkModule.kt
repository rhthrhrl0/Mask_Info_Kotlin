package com.example.mask_info_kotlin.di.module

import com.example.mask_info_kotlin.repository.MaskService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //전체 애플리케이션 스코프에서 사용가능한 모듈.
object NetworkModule {

    @Singleton //이거 붙여도 되고 안붙여도 됨. 위에서 애플리케이션 스코프로 지정했으므로.
    @Provides
    fun provideMaskService(): MaskService {
        val retrofit = Retrofit.Builder()
            .baseUrl(MaskService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        //레트로핏 사용할 준비완료.
        val service = retrofit.create(MaskService::class.java)
        return service
    }
}