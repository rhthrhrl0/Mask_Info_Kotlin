package com.example.mask_info_kotlin

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp //힐트를 위한 애플리케이션. 이걸 매니페스트 파일에 설정해줘야 함.
class App:Application() {
}