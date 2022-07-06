package com.example.mask_info_kotlin.di.module

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides                           //애플리케이션의 전체영역중에서 컨텍스트를 제공 받을 수 있음.
    fun provideFusedLocationProviderClient(@ApplicationContext context:Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }
}