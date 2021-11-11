package com.migo.task.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.migo.task.R
import com.migo.task.model.api.model.response.Contact
import kotlinx.android.synthetic.main.item_contact.view.*


class ContactsAdapter(private val contactsFuncListener: ContactsFuncListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: ArrayList<Contact> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]


        holder.itemView.apply {
            setOnClickListener {
                contactsFuncListener.onItemClick(item)
            }

            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.ic_person_24)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.drawable.ic_person_24)
                    .transform(CenterCrop())
                    .transform(RoundedCorners(16))

            Glide.with(context)
                    .load(item.pictureUrl)
                    .apply(requestOptions)
                    .into(image_avatar)

            image_favorite_mark.visibility = if (item.favorite) View.VISIBLE else View.GONE
            text_name.text = item.name
            text_email.text = item.email
            if (item.favorite) {
                image_favorite_mark.visibility = View.VISIBLE
                image_favorite.background = ContextCompat.getDrawable(context, R.drawable.ic_star_disable_24)
            } else {
                image_favorite_mark.visibility = View.GONE
                image_favorite.background = ContextCompat.getDrawable(context, R.drawable.ic_star_enable_24)
            }
            image_favorite.setOnClickListener {
                item.favorite = !item.favorite
                contactsFuncListener.onFavoriteClick(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    fun updateData(data: List<Contact>) {
        this.data.apply {
            clear()
            addAll(data)
        }

        notifyDataSetChanged()
    }

    fun isDataEmpty() = this.data.size == 0
}