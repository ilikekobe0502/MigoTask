package com.migo.task.ui.contacts.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.migo.task.R
import com.migo.task.model.api.model.response.Contact
import kotlinx.android.synthetic.main.fragment_contact_info.*


class ContactInfoFragment : BottomSheetDialogFragment() {
    companion object {
        private const val KEY_ITEM = "KEY_ITEM"
        fun createFragment(item: Contact): BottomSheetDialogFragment {
            val bundle = Bundle().also { it.putParcelable(KEY_ITEM, item) }
            return ContactInfoFragment().also { it.arguments = bundle }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    override fun onStart() {
        super.onStart()
        // Adjust full screen Bottom sheet dialog
        if (dialog != null) {
            val bottomSheet: View? = dialog?.findViewById(R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            view?.post {
                val parent: View = view?.parent as View
                val params: CoordinatorLayout.LayoutParams = parent.layoutParams as CoordinatorLayout.LayoutParams
                val behavior: CoordinatorLayout.Behavior<View> = params.behavior as CoordinatorLayout.Behavior<View>
                val bottomSheetBehavior: BottomSheetBehavior<View> = behavior as BottomSheetBehavior
                view?.measuredHeight?.let { bottomSheetBehavior.peekHeight = it }
            }
        }
    }

    private fun setUI() {
        image_collapsed.setOnClickListener {
            dismiss()
        }

        val item = arguments?.getParcelable<Contact>(KEY_ITEM)

        item?.apply {
            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.ic_person_24)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.drawable.ic_person_24)
                    .transform(CenterCrop())
                    .transform(RoundedCorners(16))

            Glide.with(requireContext())
                    .load(item.pictureUrl)
                    .apply(requestOptions)
                    .into(image_avatar)

            text_name.text = name
            text_company_name.text = company.name
            text_catch_phrase.text = company.catchPhrase
            text_email.text = email
        }
    }
}