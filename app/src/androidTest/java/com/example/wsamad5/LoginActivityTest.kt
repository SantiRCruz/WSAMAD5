package com.example.wsamad5

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LoginActivityTest{

    private lateinit var scenario: ActivityScenario<LoginActivity>

    @Before
    fun setup(){
        scenario = ActivityScenario.launch(LoginActivity::class.java)
    }

    @Test
    fun test_activityIsInView(){
        onView(withId(R.id.loginScreen)).check(matches(isDisplayed()))
    }
    @Test
    fun test_both_edit_texts_are_empty(){
        onView(withId(R.id.btnSignIn)).perform(scrollTo(),click())
        onView(withId(R.id.txtAlert)).check(matches(withText("Any Field Can't be empty")))
    }
    @Test
    fun test_email_edit_text_is_empty(){
        onView(withId(R.id.edtPassword)).perform(typeText("1234"))
        onView(withId(R.id.btnSignIn)).perform(scrollTo(), click())
        onView(withId(R.id.txtAlert)).check(matches(withText("Any Field Can't be empty")))
    }
    @Test
    fun test_password_is_empty(){
        onView(withId(R.id.edtEmail)).perform(typeText("healthy@wsa.com"))
        onView(withId(R.id.btnSignIn)).perform(scrollTo(),click())
        onView(withId(R.id.txtAlert)).check(matches(withText("Any Field Can't be empty")))
    }
    @Test
    fun test_email_is_with_the_incorrect_format_and_password_is_correct(){
        onView(withId(R.id.edtEmail)).perform(typeText("healthy@gmail.com"))
        onView(withId(R.id.edtPassword)).perform(scrollTo(),typeText("1234"))
        onView(withId(R.id.btnSignIn)).perform(scrollTo(),click())
        onView(withId(R.id.txtAlert)).check(matches(withText("The Email Field is with a wrong format")))
    }
}