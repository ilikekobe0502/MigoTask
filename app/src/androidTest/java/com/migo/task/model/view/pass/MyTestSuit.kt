package com.migo.task.model.view.pass

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.migo.task.R
import com.migo.task.launchFragmentInHiltContainer
import com.migo.task.ui.pass.PassFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class MyTestSuit {
    @Test
    fun testAddDayPassAndActivate() {
        launchFragmentInHiltContainer<PassFragment> {
            val appCompatEditText = onView(
                Matchers.allOf(
                    withId(R.id.day_et),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.layout_fragment),
                            0
                        ),
                        1
                    ),
                    isDisplayed()
                )
            )
            appCompatEditText.perform(ViewActions.replaceText("5"), ViewActions.closeSoftKeyboard())

            val materialButton = onView(
                Matchers.allOf(
                    withId(R.id.add_days_btn), ViewMatchers.withText("Add Days"),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.layout_fragment),
                            0
                        ),
                        3
                    ),
                    isDisplayed()
                )
            )
            materialButton.perform(click())

            val materialButton2 = onView(
                Matchers.allOf(
                    withId(R.id.activity_btn), ViewMatchers.withText("Buy"),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.pass_list_rv),
                            1
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            materialButton2.perform(click())

            val recyclerView = onView(
                Matchers.allOf(
                    withId(R.id.pass_list_rv),
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        7
                    )
                )
            )
        }
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}