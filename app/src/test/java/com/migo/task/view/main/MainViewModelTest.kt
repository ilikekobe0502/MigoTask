package com.migo.task.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.migo.task.MainCoroutineRule
import com.migo.task.model.api.ApiRepository
import com.migo.task.model.api.model.response.ApiStatus
import com.migo.task.model.repository.MigoDbRepository
import com.migo.task.ui.pass.PassViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import retrofit2.Response

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @MockK
    lateinit var apiRepository: ApiRepository

    @MockK
    lateinit var dbRepository: MigoDbRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getStatus() = runBlockingTest {
        val viewModel = PassViewModel(apiRepository, dbRepository)
        val resultData = ApiStatus(200,"ok")
        val response = Response.success(resultData)

        coEvery { apiRepository.getStatus(false) } returns(response)

        viewModel.getStatus(false)

        coVerify { apiRepository.getStatus(false) }

        assert(viewModel.apiResult.value?.isNotBlank()?:false)
        assertEquals(
            resultData.message,
            viewModel.apiResult.value
        )
    }
}