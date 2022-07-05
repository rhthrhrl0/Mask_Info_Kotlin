package com.example.mask_info_kotlin

import android.location.Location
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
    var location:Location= Location("a")
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
            val stores:List<Store> = storeInfo.stores.filter {
                it.remain_stat != null && it.remain_stat!="empty"
            }
            for (x in stores){
                x.distance=LocationDistance.distance(location.latitude,location.longitude,x.lat,x.lng,"k")
            }

            // 거리 기준으로 리스트 정렬
            val comparator : Comparator<Store> = compareBy { it.distance }
            itemLiveData.postValue(stores.sortedWith(comparator))

            //로딩끝
            loadingLiveData.postValue(false)
        }
    }


}