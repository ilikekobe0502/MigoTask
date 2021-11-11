package com.migo.task.utils.utility

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.migo.task.App
import java.util.*

object GeneralUtils {

    fun hideKeyboard(activity: FragmentActivity) {
        val view = activity.currentFocus
        if (view != null) {
            val inputManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyboard(context: Context, editText: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun readFromClipboard(): String {
        val clipboard = App.self.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboard.hasPrimaryClip()) {
            val description = clipboard.primaryClipDescription
            val data = clipboard.primaryClip
            if (data != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                return data.getItemAt(0).text.toString()
        }
        return ""
    }

    fun copyToClipboard(textToCopy: String) {
        val clipboard = App.self.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(null, textToCopy)
        clipboard.setPrimaryClip(clip)
    }

    fun generatedRandomId(): String {
        return UUID.randomUUID().toString()
    }

    fun Int.toDp(resources: Resources): Int {
        return (resources.displayMetrics.density * this).toInt()
    }

}