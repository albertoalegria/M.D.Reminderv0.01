package com.albertoalegria.mdreminderv001.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.albertoalegria.mdreminderv001.BuildConfig;
import com.albertoalegria.mdreminderv001.R;
import com.albertoalegria.mdreminderv001.alarm.AlarmHelper;
import com.albertoalegria.mdreminderv001.db.MedDBHelper;
import com.albertoalegria.mdreminderv001.fragment.TimePickerFragment;
import com.albertoalegria.mdreminderv001.model.Med;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateMed extends AppCompatActivity implements TimePickerFragment.OnCompleteListener {
    private final String TAG = "CreateMed";
    public static final String NO_IMAGE = "No image";
    private final int CAMERA_REQUEST_CODE = 1;
    private final int STORAGE_REQUEST_CODE = 2;

    private int repeatInterval;

    private ImageView ivMedImage;

    private TextView tvMedType;

    private Button btSave;
    private Button btCancel;

    private Spinner spRepeat;
    private Spinner spMedType;

    private EditText etTime;
    private EditText etMedName;
    private EditText etQuantity;

    private String medName;
    private int medType;
    private double medQuantity;
    private String medPhotoPath = NO_IMAGE;
    private ArrayList<String> hours;

    private int hour;
    private int minute;

    //private Uri photoUri;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_med);

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_REQUEST_CODE);
        }

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_REQUEST_CODE);
        }

        initWidgets();
        populateSpinners();
        addListeners();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = Uri.parse(medPhotoPath);
            File file = new File(imageUri.getPath());

            try {
                InputStream inputStream = new FileInputStream(file);
                ivMedImage.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            MediaScannerConnection.scanFile(this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {
                            Log.d(TAG, "onScanCompleted: Gallery updated");
                        }
                    });
        }
    }

    @Override
    public void onComplete(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;

        etTime.setText(getFormattedTime(hour, minute));
    }

    private void initWidgets() {
        ivMedImage = (ImageView) findViewById(R.id.ivMedType);

        tvMedType = (TextView) findViewById(R.id.tvType);

        spRepeat = (Spinner) findViewById(R.id.spRepeat);
        spMedType = (Spinner) findViewById(R.id.spMedType);

        btSave = (Button) findViewById(R.id.btSave);
        btCancel = (Button) findViewById(R.id.btCancel);

        etTime = (EditText) findViewById(R.id.etTime);
        etMedName = (EditText) findViewById(R.id.etMedName);
        etQuantity = (EditText) findViewById(R.id.etQuantity);
    }

    private void addListeners() {
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO get data
                if (!formIsValid()) {
                    Toast.makeText(CreateMed.this, "You need to fill these fields!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateMed.this, "All is ok!", Toast.LENGTH_SHORT).show();
                    getData();
                    //dataToLog();
                    Log.d(TAG, "onClick: making...");
                    makeMed();

                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPreviousActivity();
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        spMedType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvMedType.setText(getMeasurement(spMedType.getSelectedItem().toString()));
                medType = spMedType.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                repeatInterval = getRepeatedHour(spRepeat.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ivMedImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            CAMERA_REQUEST_CODE);
                }

                if (checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(CreateMed.this, "Camera without permissions", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {

            //boolean medHasPhoto = true;

            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.d(TAG, "dispatchTakePictureIntent: File created successfully");
            } catch (IOException e) {
                Log.d(TAG, "dispatchTakePictureIntent: Error while creating file");
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePicture, CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "MedPhoto_" + timeStamp;
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(imageFileName,
                ".jpg",
                storageDir);

        medPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void populateSpinners() {
        ArrayAdapter<CharSequence> medTypeAdapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item);
        medTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMedType.setAdapter(medTypeAdapter);

        ArrayAdapter<CharSequence> repeatAdapter = ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        medTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRepeat.setAdapter(repeatAdapter);

    }

    private boolean formIsValid() {
        boolean valid = true;

        if (etMedName.getText().toString().isEmpty()) {
            etMedName.setError(getString(R.string.error));
            valid = false;
        }

        if (etQuantity.getText().toString().isEmpty()) {
            etQuantity.setError(getString(R.string.error));
            valid = false;
        }

        if (etTime.getText().toString().isEmpty()) {
            etTime.setError(getString(R.string.error));
            valid = false;
        }

        return valid;
    }

    private void getData() {
        medName = etMedName.getText().toString();
        medQuantity = Double.valueOf(etQuantity.getText().toString());
        hours = getHoursArray();
    }

    /*private void dataToLog() {
        Log.d(TAG, "Med name: " + medName);
        Log.d(TAG, "Med type: " + medType);
        Log.d(TAG, "Med quantity: " + medQuantity);
        Log.d(TAG, "Med photo path: " + medPhotoPath);
        Log.d(TAG, "Med hours: " + hours.toString());
    }*/

    private void makeMed() {
        Log.d(TAG, "makeMed: making med");
        Med.Builder builder = new Med.Builder();
        builder.setName(medName);
        builder.setType(medType);
        builder.setQuantity(medQuantity);
        builder.setHours(hours);
        builder.setFirstImagePath(medPhotoPath);


/*        if (medHasPhoto) {
            builder.setFirstImagePath(medPhotoPath);
        } else {
            builder.setFirstImagePath(NO_IMAGE);
        }
*/
        Med med = builder.Build();

        MedDBHelper dbHelper = new MedDBHelper(this);
        dbHelper.create(med);
        int id = dbHelper.getMedId(med);
        dbHelper.close();

        createAlarm(id, med);
        startPreviousActivity();
    }

    private void createAlarm(int id, Med med) {
        AlarmHelper alarmHelper = new AlarmHelper();
        alarmHelper.setAlarm(this, hour, minute, repeatInterval, id, "Take your med!", "You need your " + med.getName(), R.drawable.ic_pill);
    }

    /*private void createAlarm(int id, Med med) {
        AlarmController_Olds alarmControllerOlds = new AlarmController_Olds();
        alarmControllerOlds.setAlarm(this, id, hour, minute, repeatInterval, R.drawable.ic_pill, med.getName());
    }*/

    private void startPreviousActivity() {
        startActivity(new Intent(this, MedList.class));
        finish();
    }

    private String getMeasurement(String rawMeasure) {
        if (rawMeasure.equals(getString(R.string.pill))) {
            return getString(R.string.pills);
        } else {
            return getString(R.string.milliliters);
        }
    }

    private int getRepeatedHour(String rawHour) {
        String[] hour = rawHour.split(" ");
        return Integer.valueOf(hour[0]);
    }

    private ArrayList<String> getHoursArray() {
        ArrayList<String> nextHours = new ArrayList<>();
        final int repetition = 24 / repeatInterval;

        int nextHour = hour;

        for (int i = 0; i < repetition; i++) {
            nextHours.add(getFormattedTime(nextHour, minute));
            nextHour = (nextHour + repeatInterval) % 24;
        }
        return nextHours;
    }

    private String getFormattedTime(int hour, int minute) {
        SimpleDateFormat dateFormat;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        if (DateFormat.is24HourFormat(this)) {
            dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else {
            dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        }

        return dateFormat.format(calendar.getTime());
    }
}
