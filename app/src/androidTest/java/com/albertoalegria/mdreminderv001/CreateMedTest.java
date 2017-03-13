package com.albertoalegria.mdreminderv001;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.albertoalegria.mdreminderv001.activities.MedList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by alberto on 03/03/17.
 */
@RunWith(AndroidJUnit4.class)
public class CreateMedTest {
    public static final String MED_NAME = "Penicillin";
    public static final String MED_TYPE = "Syrup";
    public static final String MED_MEASURE = "";


    @Rule
    public ActivityTestRule<MedList> mActivityRule = new ActivityTestRule<>(MedList.class);

    @Test
    public void typeDataIntoEditText() {

        onView(withId(R.id.etMedName)).perform(typeText(MED_NAME), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.spMedType)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(R.string.syrup))).perform(click());
        onView(withId(R.id.spMedType)).check(matches(withSpinnerText(R.string.syrup)));

        onView(withId(R.id.tvType)).check(matches(withText(R.string.milliliters)));

        onView(withId(R.id.etQuantity)).perform(typeText("2.5"), closeSoftKeyboard());
    }
}
