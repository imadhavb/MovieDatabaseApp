package com.example.movies_appp

import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.SearchView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test


class VerifyListAfterSearch {
    @Rule
    @JvmField
    val rule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)
    @Test
    fun verifyListAfterSearch() {
        // Verify that after a search the recyclerview list of search results appears
        onView(withId(R.id.search)).perform(click())
        onView(isAssignableFrom(AutoCompleteTextView::class.java)).perform(typeText("Joker"), pressImeActionButton())
        //Wait 1 seconds for search results to show
        Thread.sleep(1000)
        onView(withId(R.id.searchRecyclerview)).perform(click()) //If this line works than the the list contains at least one item
    }
}