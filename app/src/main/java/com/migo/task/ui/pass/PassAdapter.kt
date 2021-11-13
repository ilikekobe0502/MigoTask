package com.migo.task.ui.pass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.migo.task.R
import com.migo.task.model.enums.PassType
import com.migo.task.model.vo.Pass
import com.migo.task.model.vo.PassData
import kotlinx.android.synthetic.main.item_pass_content.view.*
import kotlinx.android.synthetic.main.item_pass_header.view.title_tv
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class PassAdapter(
    private val funClickListener: FunClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: ArrayList<PassData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.HEADER.ordinal -> {
                HeaderViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_pass_header, parent, false)
                )
            }
            ViewType.CONTENT.ordinal -> {
                ContentViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_pass_content, parent, false),
                    funClickListener
                )
            }
            else -> {
                ContentViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_pass_content, parent, false),
                    funClickListener
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        if (holder is ContentViewHolder) {
            item.passContent?.let { holder.bind(it) }
        } else if (holder is HeaderViewHolder) {
            item.passTitle?.let { holder.bind(it) }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].passTitle != null) ViewType.HEADER.ordinal else ViewType.CONTENT.ordinal
    }

    fun updateData(data: List<PassData>) {
        this.data.apply {
            clear()
            addAll(data)
        }

        notifyDataSetChanged()
    }

    private class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: PassType) {
            val context = itemView.context
            itemView.title_tv.text = if (data == PassType.TYPE_DAY) {
                context.getString(R.string.pass_day_title)
            } else {
                context.getString(R.string.pass_hour_title)
            }
        }
    }

    private class ContentViewHolder(
        itemView: View,
        private val funClickListener: FunClickListener
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: Pass) {
            itemView.apply {
                title_tv.text = if (data.type == PassType.TYPE_DAY) {
                    context.getString(R.string.pass_day_content_title, data.timeValue)
                } else {
                    context.getString(R.string.pass_hour_content_title, data.timeValue)
                }

                money_tv.text = context.getString(R.string.pass_money, data.price)
                activity_btn.text = if (data.activate) {
                    context.getString(R.string.pass_btn_activated)
                } else {
                    context.getString(R.string.pass_btn_buy)
                }
                activity_btn.isEnabled = !data.activate
                activity_btn.setOnClickListener {
                    funClickListener.onActivateClick(data)
                }
                status_tv.text =
                    context.getString(
                        R.string.pass_status,
                        if (data.activate) context.getString(R.string.pass_status_activated) else context.getString(
                            R.string.pass_status_inactivated
                        )
                    )
                insertion_time_tv.text = context.getString(
                    R.string.pass_insertion_time,
                    transferDate(data.insertionDate)
                )
                activate_time_tv.text = context.getString(
                    R.string.pass_activation_time, transferDate(data.activationDate)
                )
                expiration_time_tv.text = context.getString(
                    R.string.pass_expiration_time, transferDate(data.expiredDate)
                )

                setOnClickListener {
                    if (detail_gp.isShown) {
                        detail_gp.visibility = View.GONE
                    } else {
                        detail_gp.visibility = View.VISIBLE
                    }
                }
            }
        }

        /**
         * transfer long to date time format
         *
         * @param time the time's long that you want ot convert
         *
         * @return if the [time] is -1, will return "",
         *         otherwise return the date with format
         */
        fun transferDate(time: Long): String {
            val dateFormat = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss")
            return if (time == -1L) "" else DateTime(time).toString(dateFormat)
        }
    }
}

private enum class ViewType {
    HEADER,
    CONTENT
}