package com.android.todo.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText mEditText;
    private Context mContext;
    private Calendar calendar;
    private Date currentDate;
    private int dayOfMonth;
    private int month;
    private int year;

    public EditTextDatePicker(Context context, int editTextViewID) {
        Activity activity = (Activity) context;
        this.mEditText = activity.findViewById(editTextViewID);
        this.mEditText.setOnClickListener(this);
        this.mContext = context;

        calendar = Calendar.getInstance(TimeZone.getDefault());
        currentDate = new Date();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        updateDisplay();
    }

    @Override
    public void onClick(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(currentDate.getTime());
        datePickerDialog.show();
    }

    // Updates the date in the EditText
    private void updateDisplay() {
        mEditText.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(dayOfMonth).append("-")
                .append(month + 1).append("-")
                .append(year));
    }


    public DateDTO getPickedDate(){
        return new DateDTO(year,month,dayOfMonth);
    }
    public class DateDTO{
        private int year;
        private int month;
        private int dayOfMonth;

        public DateDTO(int year, int month, int dayOfMonth) {
            this.year = year;
            this.month = month;
            this.dayOfMonth = dayOfMonth;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDayOfMonth() {
            return dayOfMonth;
        }

        public void setDayOfMonth(int dayOfMonth) {
            this.dayOfMonth = dayOfMonth;
        }
    }
}
