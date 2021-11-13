package com.migo.task.ui.pass

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.migo.task.model.api.ApiResult
import com.migo.task.model.api.model.response.Contact
import com.migo.task.model.enums.PassType
import com.migo.task.model.repository.MigoDbRepository
import com.migo.task.model.repository.MigoRepository
import com.migo.task.model.vo.Pass
import com.migo.task.model.vo.PassData
import com.migo.task.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import org.joda.time.DateTime

private const val HOURS_PRICE = 10
private const val DAYS_PRICE = 100

class PassViewModel @ViewModelInject constructor(
    private var migoRepository: MigoRepository,
    private var migoDbRepository: MigoDbRepository
) : BaseViewModel() {

    private val _passResult = MutableLiveData<List<PassData>>() // All Pass data liveData
    val passResult: LiveData<List<PassData>> = _passResult


    /**
     * Get all Pass data
     */
    fun getAllPassData() {
        viewModelScope.launch {
            migoDbRepository.fetchPass(passType = PassType.TYPE_DAY)
                .zip(migoDbRepository.fetchPass(passType = PassType.TYPE_HOUR)) { dayList, hourList ->
                    val passDataList = ArrayList<PassData>()
                    if (dayList.isNotEmpty()) {
                        passDataList.add(PassData(passTitle = PassType.TYPE_DAY))
                        passDataList.addAll(dayList.map { PassData(passContent = it) })
                    }

                    if (hourList.isNotEmpty()) {
                        passDataList.add(PassData(passTitle = PassType.TYPE_HOUR))
                        passDataList.addAll(hourList.map { PassData(passContent = it) })
                    }

                    return@zip passDataList
                }
                .collect { _passResult.postValue(it) }
        }
    }

    /**
     * activate the pass
     */
    fun activatePass(data: Pass) {
        viewModelScope.launch(Dispatchers.IO) {
            data.activate = true
            data.activationDate = DateTime.now().toDate().time
            if (data.type == PassType.TYPE_DAY) {
                data.expiredDate =
                    DateTime(data.activationDate).plusDays(data.timeValue).toDate().time
            } else {
                data.expiredDate =
                    DateTime(data.activationDate).plusHours(data.timeValue).toDate().time
            }
            migoDbRepository.updateData(data)
        }
    }

    /**
     * Add a pass data
     */
    fun addPass(time: Int, type: PassType) {
        val data = Pass(type = type, timeValue = time)
        if (type == PassType.TYPE_DAY) {
            data.price = time * DAYS_PRICE
        } else {
            data.price = time * HOURS_PRICE
        }

        viewModelScope.launch(Dispatchers.IO) {
            migoDbRepository.insertData(data)
        }
    }
}