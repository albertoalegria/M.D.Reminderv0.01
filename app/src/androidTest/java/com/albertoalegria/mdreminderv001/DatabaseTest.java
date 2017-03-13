package com.albertoalegria.mdreminderv001;

import android.support.test.runner.AndroidJUnit4;

import com.albertoalegria.mdreminderv001.db.MedDBHelper;
import com.albertoalegria.mdreminderv001.model.Med;
import com.albertoalegria.mdreminderv001.utils.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by alberto on 04/03/17.
 */

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private MedDBHelper dbHelper;
    private static final String TAG = "TEST";

    @Before
    public void setup() throws Exception {
        getTargetContext().deleteDatabase(Constants.Db.DB_NAME);
        dbHelper = new MedDBHelper(getTargetContext());
        //dbHelper.printCols();
    }

    @After
    public void tearDown() throws Exception {
        dbHelper.close();
    }

    @Test
    public void shouldCreateAndRetrieveMed() throws Exception {
        Med med = new Med.Builder()
                .setName("Penicillin")
                .setType(Constants.Types.PILL)
                .setQuantity(2.0)
                .setHours(getRandomHours(24))
                .setFirstImagePath("Yo!")
                .Build();

        dbHelper.create(med);

        Med med1 = dbHelper.retrieve("Penicillin");
        assertNotNull(med1);
    }

    @Test
    public void shouldCreateAndRetrieveAnArryOfMeds() throws Exception {
        final int MAX = 24;
        for (int i = 0; i < MAX; i++) {
            Med med = new Med.Builder()
                    .setName("Med" + i)
                    .setType(Constants.Types.INJECTION)
                    .setQuantity(2.5)
                    .setHours(getRandomHours(i))
                    .setFirstImagePath("ImgPath" + i)
                    .Build();

            dbHelper.create(med);
            //Log.d(TAG, "Created: " + med.getName());
        }

        ArrayList<Med> allMeds = dbHelper.retrieveAll();
        assertThat(allMeds.size(), is(MAX));
        //System.out.println("Meds: " + allMeds.size());

        /*for (Med med : allMeds) {
            System.out.println("Retrieved from db: " + med.getName());
        }*/
    }


    private ArrayList<String> getRandomHours(int quantity) {
        //Dummy array
        ArrayList<String> hours = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            hours.add(String.valueOf(i));
        }
        return hours;
    }
}
