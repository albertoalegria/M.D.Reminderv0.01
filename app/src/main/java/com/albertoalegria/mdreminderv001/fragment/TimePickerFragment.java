package com.albertoalegria.mdreminderv001.fragment;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public interface OnCompleteListener {
        void onComplete(int hour, int minute);
    }

    private OnCompleteListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();

        return new TimePickerDialog(getActivity(), this,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            this.listener = (OnCompleteListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnCompleteListener");
        }
    }



    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        if (DateFormat.is24HourFormat(getActivity())) {
            dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else {
            dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);*/


        this.listener.onComplete(hour, minute);
    }
}
