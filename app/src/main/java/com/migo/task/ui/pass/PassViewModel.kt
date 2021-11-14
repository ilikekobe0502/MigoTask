package com.migo.task.ui.pass

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.migo.task.model.api.ApiRepository
import com.migo.task.model.api.model.response.ApiStatus
import com.migo.task.model.enums.PassType
import com.migo.task.model.repository.MigoDbRepository
import com.migo.task.model.vo.Pass
import com.migo.task.model.vo.PassData
import com.migo.task.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import retrofit2.HttpException

const val HOURS_PRICE = 10
const val DAYS_PRICE = 100

class PassViewModel @ViewModelInject constructor(
    private var migoRepository: ApiRepository,
    private var migoDbRepository: MigoDbRepository
) : BaseViewModel() {

    private val _passResult = MutableLiveData<List<PassData>>() // All Pass data liveData
    val passResult: LiveData<List<PassData>> = _passResult

    private val _apiResult = MutableLiveData<String>() // get api status
    val apiResult: LiveData<String> = _apiResult

    /**
     * get the network status
     * @param isWifi
     */
    fun getStatus(isWifi: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            flow {
                val resp = migoRepository.getStatus(isWifi)
                if (!resp.isSuccessful) throw HttpException(resp)
                emit(resp.body())
            }
                .onStart {
                    emit(ApiStatus(status = -1, message = "Loading"))
                }
                .catch {
                    emit(ApiStatus(status = -1, message = it.message ?: "Error"))
                }
                .collect {
                    _apiResult.postValue(it?.message)
                }
        }
    }

    /**
     * display networkError message
     *
     * @param message the message you want to show
     */
    fun networkError(message: String) {
        _apiResult.postValue(message)
    }

    /**
     * Get all Pass data
     */
    fun getAllPassData() {
        viewModelScope.launch(Dispatchers.IO) {
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