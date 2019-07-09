package com.android.todo.utils;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EditTextTimePicker implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private EditText mEditText;
    private Context mContext;
    private Calendar calendar;
    private int hourOfDay;
    private int minute;

    public EditTextTimePicker(Context context, int editTextViewID) {
        Activity activity = (Activity) context;
        this.mEditText = activity.findViewById(editTextViewID);
        this.mEditText.setOnClickListener(this);
        this.mContext = context;

        calendar = Calendar.getInstance(TimeZone.getDefault());
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        updateDisplay();
    }

    @Override
    public void onClick(View v) {
        TimePickerDialog mTimePickerDialog = new TimePickerDialog(mContext, this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);

        mTimePickerDialog.show();
    }

    // Updates the date in the EditText
    private void updateDisplay() {
        mEditText.setText(new StringBuilder()
                .append(hourOfDay).append(":").append(minute));
    }

    public long getPickedDate(EditTextDatePicker.DateDTO time){
        Calendar c = Calendar.getInstance();
        c.set(time.getYear(), time.getMonth(), time.getDayOfMonth(),hourOfDay,minute);
        return c.getTimeInMillis();
    }
}