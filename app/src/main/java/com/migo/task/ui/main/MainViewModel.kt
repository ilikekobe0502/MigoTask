package com.migo.task.ui.main

import androidx.lifecycle.viewModelScope
import com.migo.task.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {

    var needCloseApp = false // 判斷是否需要離開 app

    /**
     * 按下 back 離開的 Timer
     */
    fun startBackExitAppTimer() {
        needCloseApp = true
        viewModelScope.launch {
            delay(3000)
            needCloseApp = false
        }
    }
}