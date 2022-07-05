package com.example.mask_info_kotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mask_info_kotlin.model.Store
import com.example.mask_info_kotlin.repository.MaskService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainViewModel : ViewModel() {
    val itemLiveData = MutableLiveData<List<Store>>()
    val loadingLiveData = MutableLiveData<Boolean>()

    private val service: MaskService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(MaskService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        //레트로핏 사용할 준비완료.
        service = retrofit.create(MaskService::class.java)

        //SocketException은 앱을 삭제 후 다시 설치해라.
        fetchStoreInfo()
    }

    fun fetchStoreInfo() {
        //로딩시작
        loadingLiveData.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val storeInfo = service.fetchStoreInfo(37.128, 127.043)
            itemLiveData.postValue(storeInfo.stores)
            //로딩끝
            loadingLiveData.postValue(false)
        }
    }


}