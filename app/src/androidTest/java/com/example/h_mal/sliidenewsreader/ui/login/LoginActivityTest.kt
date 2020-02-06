package com.example.h_mal.sliidenewsreader.ui.login


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.h_mal.sliidenewsreader.R
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@LargeTest
class LoginActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    lateinit var invalidTextString: String
    lateinit var failedTextString: String

    @Before
    fun setup(){
        invalidTextString = mActivityTestRule.activity.getString(R.string.login_invalid)
        failedTextString = mActivityTestRule.activity.getString(R.string.login_failed)
    }

    @Test
    fun emptyBothTest() {
        sign_in_button.perform(click())
        sign_in_button.check(matches(isDisplayed()))
    }

    @Test
    fun emptyPasswordTest() {
        username_edittext.perform(replaceText("user"), closeSoftKeyboard())

        sign_in_button.perform(click())
        sign_in_button.check(matches(isDisplayed()))
    }

    @Test
    fun emptyUserNameTest() {
        password_edittext.perform(replaceText("password"), closeSoftKeyboard())

        sign_in_button.perform(click())
        sign_in_button.check(matches(isDisplayed()))

    }

    @Test
    fun incorrectDetailsTest() {
        username_edittext.perform(replaceText("user"), closeSoftKeyboard())
        password_edittext.perform(replaceText("user"), closeSoftKeyboard())

        sign_in_button.perform(click())
        sign_in_button.check(matches(isDisplayed()))
    }

    @Test
    fun validLoginAsUserTest() {
        username_edittext.perform(replaceText("user"), closeSoftKeyboard())
        password_edittext.perform(replaceText("password"), closeSoftKeyboard())

        sign_in_button.perform(click())
        sign_in_button.check(doesNotExist())

        menu_button.perform(click())
        signout_button.perform(click())

        sign_in_button.check(matches(isDisplayed()))
    }

    @Test
    fun validLoginAsPremiumUserTest() {
        username_edittext.perform(replaceText("premium"), closeSoftKeyboard())
        password_edittext.perform(replaceText("password"), closeSoftKeyboard())

        sign_in_button.perform(click())
        sign_in_button.check(doesNotExist())

        menu_button.perform(click())
        signout_button.perform(click())

        sign_in_button.check(matches(isDisplayed()))
    }

    private val sign_in_button = onView(
            allOf(
                withId(R.id.login), withText("Sign in"),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )

    private val username_edittext = onView(
        allOf(
            withId(R.id.username),
            childAtPosition(
                allOf(
                    withId(R.id.container),
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    )
                ),
                0
            ),
            isDisplayed()
        )
    )

    private val password_edittext = onView(
        allOf(
            withId(R.id.password),
            childAtPosition(
                allOf(
                    withId(R.id.container),
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    )
                ),
                1
            ),
            isDisplayed()
        )
    )

    val menu_button = onView(
        allOf(
            withContentDescription("More options"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.action_bar),
                    1
                ),
                0
            ),
            isDisplayed()
        )
    )

    val signout_button = onView(
        allOf(
            withId(R.id.title), withText("Sign out"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.content),
                    0
                ),
                0
            ),
            isDisplayed()
        )
    )

    private fun testToast(toastText:String){
        onView(withText(toastText))
            .inRoot(withDecorView(not(`is`(mActivityTestRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
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
