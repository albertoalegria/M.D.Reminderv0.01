package com.albertoalegria.mdreminderv001;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.albertoalegria.mdreminderv001.activities.MedList;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MedListTest {

    @Rule
    public ActivityTestRule<MedList> mActivityTestRule = new ActivityTestRule<>(MedList.class);

    @Test
    public void medListTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.etMedName),
                        withParent(withId(R.id.activity_main))));
        appCompatEditText.perform(scrollTo(), click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.etMedName),
                        withParent(withId(R.id.activity_main))));
        appCompatEditText2.perform(scrollTo(), replaceText("penicillin"), closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spMedType),
                        withParent(withId(R.id.activity_main))));
        appCompatSpinner.perform(scrollTo(), click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(android.R.id.text1), withText("Syrup"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.spMedType),
                        withParent(withId(R.id.activity_main))));
        appCompatSpinner2.perform(scrollTo(), click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("Injection"), isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.etQuantity),
                        withParent(withId(R.id.activity_main))));
        appCompatEditText3.perform(scrollTo(), replaceText("150"), closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.etTime),
                        withParent(withId(R.id.activity_main))));
        appCompatEditText4.perform(scrollTo(), click()); //Gives error. Maybe bc the timePicker?

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatSpinner3 = onView(
                allOf(withId(R.id.spRepeat),
                        withParent(withId(R.id.activity_main))));
        appCompatSpinner3.perform(scrollTo(), click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("4 hrs"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.ivMedType), withContentDescription("You can take a picture of your med"),
                        withParent(withId(R.id.activity_main))));
        appCompatImageView.perform(scrollTo(), click());

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.ivMedType), withContentDescription("You can take a picture of your med"),
                        withParent(withId(R.id.activity_main))));
        appCompatImageView2.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btSave), withText("Save"),
                        withParent(withId(R.id.activity_main))));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.tvType), withText("Milliliters"),
                        childAtPosition(
                                allOf(withId(R.id.activity_main),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                                0)),
                                6),
                        isDisplayed()));
        textView.check(matches(withText("Milliliters")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
