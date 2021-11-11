package com.migo.task.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.migo.task.R

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
    }

    abstract fun getLayoutId(): Int

    fun navigateTo(fragment: Fragment, backStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
        val currentFragment: Fragment? = when {
            supportFragmentManager.fragments.size > 0 -> supportFragmentManager.fragments[0]
            else -> null
        }
        if (backStack && currentFragment != null) {
            transaction.addToBackStack(currentFragment::class.java.simpleName)
        }
        transaction.replace(
            R.id.layout_fragment, fragment,
            fragment::class.java.simpleName
        ).commit()
    }

}