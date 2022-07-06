package com.example.mask_info_kotlin

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mask_info_kotlin.model.Store
import com.example.mask_info_kotlin.repository.MaskService
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val service: MaskService, //이제 마스크정보를 제공하는 곳이 내부db로 바뀌면 모듈 함수만 바꿔주면 됨.
    private var fusedLocationClient: FusedLocationProviderClient //통합위치 제공자를 의존성 주입 받음.
) : ViewModel() {
    val itemLiveData = MutableLiveData<List<Store>>()
    val loadingLiveData = MutableLiveData<Boolean>()

    init {
        //이 코드의 단점. 나중에 네트워크 통신이 아니라 내부DB로부터 데이터를 받고 싶을때 뷰모델의 코드변경 필요.
        //SocketException은 앱을 삭제 후 다시 설치해라.
        fetchStoreInfo()
    }

    @SuppressLint("MissingPermission") //퍼미션 체크 안해도 일단 에러 안나도록 막아놓기.
    fun fetchStoreInfo() {
        //로딩시작
        loadingLiveData.value = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location ->
            viewModelScope.launch(Dispatchers.IO) {
                val storeInfo = service.fetchStoreInfo(location.latitude, location.longitude)
                val stores: List<Store> = storeInfo.stores.filter {
                    it.remain_stat != null && it.remain_stat != "empty"
                }
                for (x in stores) {
                    x.distance = LocationDistance.distance(
                        location.latitude,
                        location.longitude,
                        x.lat,
                        x.lng,
                        "k"
                    )
                }
                // 거리 기준으로 리스트 정렬
                val comparator: Comparator<Store> = compareBy { it.distance }
                itemLiveData.postValue(stores.sortedWith(comparator))
                //로딩끝
                loadingLiveData.postValue(false)
            }
        }.addOnFailureListener {
            exception->
            loadingLiveData.value=false
        }

    }


}