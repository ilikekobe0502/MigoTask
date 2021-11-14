package com.migo.task.ui.pass

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.migo.task.R
import com.migo.task.model.enums.PassType
import com.migo.task.ui.base.BaseFragment
import com.migo.task.utils.network.NetworkCallback
import com.migo.task.utils.utility.GeneralUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_pass.*

@AndroidEntryPoint
class PassFragment : BaseFragment() {

    private val viewModel: PassViewModel by viewModels()

    private var contactsAdapter: PassAdapter? = null
    private val connectivityManager: ConnectivityManager by lazy {
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val networkCallback: NetworkCallback by lazy {
        NetworkCallback(
            onCapabilitiesChanged = {
                viewModel.getStatus(it)
            },
            onLost = {
                viewModel.networkError(getString(R.string.error_network_unavailable))
            }
        )
    }

    /**
     * Declare Closure for recyclerView's click event
     */

    /**
     * Declare Closure for recyclerView's click event
     */
    private val funClickListener by lazy {
        FunClickListener(
            onActivateClick = { data ->
                viewModel.activatePass(data)
            }
        )
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_pass
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserve()
        setupUi()

        viewModel.getAllPassData()
    }

    override fun onResume() {
        super.onResume()
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    override fun onStop() {
        super.onStop()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    /**
     * set observe listener
     */
    private fun setObserve() {
        viewModel.passResult.observe(viewLifecycleOwner, {
            contactsAdapter?.updateData(it)
        })

        viewModel.apiResult.observe(viewLifecycleOwner, {
            status_tv.text = getString(R.string.pass_api_status, it)
        })
    }

    private fun setupUi() {
        contactsAdapter = PassAdapter(funClickListener)

        pass_list_rv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = contactsAdapter
        }


        add_days_btn.setOnClickListener {
            if (day_et.text.isNotBlank()) {
                viewModel.addPass(day_et.text.toString().toInt(), PassType.TYPE_DAY)
                GeneralUtils.hideKeyboard(requireActivity())
                day_et.setText("")
            }
        }

        add_hours_btn.setOnClickListener {
            if (hour_et.text.isNotBlank()) {
                viewModel.addPass(hour_et.text.toString().toInt(), PassType.TYPE_HOUR)
                GeneralUtils.hideKeyboard(requireActivity())
                hour_et.setText("")
            }
        }
    }
}