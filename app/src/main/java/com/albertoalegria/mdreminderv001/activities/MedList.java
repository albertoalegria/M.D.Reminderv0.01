package com.albertoalegria.mdreminderv001.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.albertoalegria.mdreminderv001.R;
import com.albertoalegria.mdreminderv001.db.MedDBHelper;
import com.albertoalegria.mdreminderv001.model.Med;

import java.util.ArrayList;

public class MedList extends AppCompatActivity {

    private FloatingActionButton fabCreateMed;
    private RecyclerView rvMedList;

    private ArrayList<Med> meds;
    private MedDBHelper dbHelper;

    private static final String TAG = "Med List";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_list);

        getData();
        initWidgets();
        addListeners();

    }

    private void getData() {
        dbHelper = new MedDBHelper(this);
        meds = dbHelper.retrieveAll();

    }

    private void initWidgets() {
        fabCreateMed = (FloatingActionButton) findViewById(R.id.fab);
        rvMedList = (RecyclerView) findViewById(R.id.rvMedList);

        MedAdapter medAdapter = new MedAdapter(this, meds);
        rvMedList.setAdapter(medAdapter);
        rvMedList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void addListeners() {
        fabCreateMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), CreateMed.class));
                finish();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        rvMedList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 || dy < 0 && fabCreateMed.isShown()) {
                    fabCreateMed.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fabCreateMed.show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Destroyed");
    }
}
