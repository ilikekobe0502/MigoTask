package com.migo.task.ui.contacts

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.migo.task.R
import com.migo.task.enums.FilterType
import com.migo.task.model.api.ApiResult.*
import com.migo.task.ui.base.BaseFragment
import com.migo.task.ui.contacts.info.ContactInfoFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.toolbar.view.*

@AndroidEntryPoint
class ContactsFragment : BaseFragment() {

    private val viewModel: ContactsViewModel by viewModels()

    private var contactsAdapter: ContactsAdapter? = null

    /**
     * Declare Closure for recyclerView's click event
     */
    private val contactsFuncListener by lazy {
        ContactsFuncListener(
                onFavoriteClick = { contact -> viewModel.updateFavorite(contact) },
                onItemClick = { contact ->
                    val dialog = ContactInfoFragment.createFragment(contact)
                    dialog.show(parentFragmentManager, dialog.tag)
                }
        )
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_contacts
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserve()
        setupUi()
    }

    /**
     * set observe listener
     */
    private fun setObserve() {
        viewModel.contactsResult.observe(viewLifecycleOwner, {
            when (it) {
                is Loading -> {
                    layout_retry.visibility = View.GONE
                    progress.visibility = View.VISIBLE
                }
                is Loaded -> progress.visibility = View.GONE
                is Success -> {
                    if (viewModel.currentFilterResult.value == FilterType.ALL) {
                        if (progress.isVisible)
                            progress.visibility = View.GONE
                        it.result?.let { data -> contactsAdapter?.updateData(data) }
                        layout_retry.visibility = when {
                            contactsAdapter?.isDataEmpty() == true -> View.VISIBLE
                            else -> View.GONE
                        }
                    }
                }
                is Error -> {
                    layout_retry.visibility = View.VISIBLE
                    onApiError(it.throwable)
                }
            }
        })

        viewModel.starredContactsResult.observe(viewLifecycleOwner, {
            when (it) {
                is Loading -> {
                    layout_retry.visibility = View.GONE
                    progress.visibility = View.VISIBLE
                }
                is Loaded -> progress.visibility = View.GONE
                is Success -> {
                    if (viewModel.currentFilterResult.value == FilterType.Starred) {
                        if (progress.isVisible)
                            progress.visibility = View.GONE
                        it.result?.let { data -> contactsAdapter?.updateData(data) }
                    }
                }
                is Error -> {
                    layout_retry.visibility = View.VISIBLE
                    onApiError(it.throwable)
                }
            }
        })

        viewModel.currentFilterResult.observe(viewLifecycleOwner, {
            when (it) {
                FilterType.ALL -> {
                    filterAllUI()
                    viewModel.getAllContacts()
                }
                FilterType.Starred -> {
                    filterStarredUI()
                    viewModel.getStarredContacts()
                }
                else -> {
                }
            }
        })
    }

    private fun setupUi() {
        layout_toolbar.tv_toolbar_title.text = getString(R.string.contacts_title)

        contactsAdapter = ContactsAdapter(contactsFuncListener)

        rv_contacts.apply {
            setHasFixedSize(true)
            adapter = contactsAdapter
        }

        text_starred.setOnClickListener {
            viewModel.updateFilter(FilterType.Starred)
        }

        text_all.setOnClickListener {
            viewModel.updateFilter(FilterType.ALL)
        }

        layout_retry.setOnClickListener {
            when (viewModel.currentFilterResult.value) {
                FilterType.ALL -> {
                    viewModel.getAllContacts()
                }
                FilterType.Starred -> {
                    viewModel.getStarredContacts()
                }
            }
        }

        viewModel.updateFilter(FilterType.ALL)
    }

    /**
     * set UI for display 'All'
     */
    private fun filterAllUI() {
        text_all.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_filter_button_enable)
        text_all.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        text_starred.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_filter_button_disable)
        text_starred.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
    }

    /**
     * set UI for display 'Starred'
     */
    private fun filterStarredUI() {
        text_all.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_filter_button_disable)
        text_all.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        text_starred.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_filter_button_enable)
        text_starred.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }
}