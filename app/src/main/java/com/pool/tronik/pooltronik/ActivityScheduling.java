package com.pool.tronik.pooltronik;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.pool.tronik.pooltronik.dto.PTScheduleDate;
import com.pool.tronik.pooltronik.utils.DateTimeContainer;
import com.pool.tronik.pooltronik.utils.DateTimeUtils;
import com.pool.tronik.pooltronik.utils.RelayStatus;
import com.pool.tronik.pooltronik.utils.StaticVarFile;
import com.pool.tronik.pooltronik.utils.StringUtils;

import org.joda.time.DateTime;

public class ActivityScheduling extends AppCompatActivity implements View.OnClickListener {

    private TimePicker timePicker;
    private TextView tvChoseDate;
    private TextView tvRelayName;
    private DateTimeContainer dateTimeContainer;
    private RelayStatus relayStatus;
    private String defDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduling);
        relayStatus = (RelayStatus) getIntent().getSerializableExtra(StringUtils.EXTRA_DATA);
        if (relayStatus == null) {
            finish();
            return;
        }
        relayStatus.setStatus(StaticVarFile.RELAY_STATUS.OFF.ordinal());
        initToolbar();
        timePicker = findViewById(R.id.time_picker);
        initDays((ViewGroup) findViewById(R.id.ll_days_container));
        tvRelayName = findViewById(R.id.tv_relay_name);
        findViewById(R.id.ll_date_container).setOnClickListener(this);
        tvChoseDate = findViewById(R.id.tv_choosen_date);
        timePicker.setIs24HourView(true);
        dateTimeContainer = new DateTimeContainer();
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minuteOfDay) {
                dateTimeContainer.setHour(hourOfDay);
                dateTimeContainer.setMinutes(minuteOfDay);
            }
        });
        tvRelayName.setText(relayStatus.getName());
        String txt = getResources().getString(R.string.today);
        dateTimeContainer.setYear(DateTimeUtils.getCurrentYear());
        dateTimeContainer.setMonth(DateTimeUtils.getCurrentMonth());
        dateTimeContainer.setDayOfWeek(DateTimeUtils.getCurrentNumberDayOfWeek());
        dateTimeContainer.setDayOfMonth(DateTimeUtils.getCurrentDayOfMonth());
        tvChoseDate.setText(txt +" - "+DateTimeUtils.getCurrentDayOfWeek()+", "+DateTimeUtils.getCurrentDayOfMonth());
        defDate = tvChoseDate.getText().toString();
        SwitchCompat switchCompat = findViewById(R.id.sw_status);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    relayStatus.setStatus(StaticVarFile.RELAY_STATUS.ON.ordinal());
                else
                    relayStatus.setStatus(StaticVarFile.RELAY_STATUS.OFF.ordinal());
            }
        });
    }

    public void initToolbar() {
        View view = getLayoutInflater().inflate(R.layout.toolbar_timing_layout, null);
        view.findViewById(R.id.tv_timing_save).setOnClickListener(this);
        view.findViewById(R.id.tv_timing_cancel).setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.addView(view);
        //toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        /*ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_outline_arrow_back_24px);
        setTitle(R.string.common_setting);*/
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.ll_date_container) {
            startDatePickerDialog();
        }
        else if (view.getId() == R.id.tv_timing_save) {
            save();
        }
        else if (view.getId() == R.id.tv_timing_cancel) {
            finish();
        }
        else {
            dayClickHandler(view);
        }

    }

    public void save() {
        if (dateTimeContainer.getHour() == -1 || dateTimeContainer.getMinutes() == -1) {
            Toast.makeText(this,getResources().getString(R.string.error_timing1),Toast.LENGTH_LONG).show();
        }
        else {
            Log.d("RFVGTM", "localtime = " + DateTimeUtils.getLocalDateTime(dateTimeContainer));
            getPTScheduleDate();
        }
    }

    public PTScheduleDate getPTScheduleDate() {
        PTScheduleDate ptScheduleDate = new PTScheduleDate();
        ptScheduleDate.setRelay(relayStatus.getRelay());
        ptScheduleDate.setStatus(relayStatus.getStatus());
        //*********
        ptScheduleDate.setStartDate(DateTimeUtils.getLocalDateTime(dateTimeContainer).toString());
        ptScheduleDate.setNextDates(DateTimeUtils.makeNextDatesFromDateAndRepetition(DateTimeUtils.getLocalDateTime(dateTimeContainer),
                dateTimeContainer.getRepeatList()));


        return ptScheduleDate;
    }

    public void dayClickHandler(View view) {
        if (!(view instanceof TextView))
            return;
        Drawable drawable = view.getBackground();
        TextView textView = (TextView) view;
        if (drawable == null) {
            drawable = getDrawable(R.drawable.bg_rectangle);
            drawable.setTint(getResources().getColor(R.color.colorPrimary));
            view.setTag(View.VISIBLE);
            dateTimeContainer.addRepeat(Integer.parseInt(textView.getHint().toString()));
        } else {
            int tagVisibility = (int) view.getTag();
            if (tagVisibility == View.VISIBLE) {
                drawable.setTint(getResources().getColor(R.color.transparent));
                view.setTag(View.INVISIBLE);
                dateTimeContainer.removeDay(Integer.parseInt(textView.getHint().toString()));
            } else {
                drawable.setTint(getResources().getColor(R.color.colorPrimary));
                view.setTag(View.VISIBLE);
                dateTimeContainer.addRepeat(Integer.parseInt(textView.getHint().toString()));
            }
        }

        /*if (dateTimeContainer.getRepeatList().size() == 7) {
            tvChoseDate.setText(getResources().getString(R.string.each_day));
        }
        else {
            String days = DateTimeUtils.getDayOfWeek(dateTimeContainer.getRepeatList(), this);
            if (days.isEmpty()) {
                tvChoseDate.setText(defDate);
            } else {
                tvChoseDate.setText(defDate + " " + days);
            }
        }*/
        tvChoseDate.setText(getRepetition());
        view.setBackground(drawable);
    }

    public String getRepetition() {
        String str = "";
        if (dateTimeContainer.getRepeatList().size() == 7) {
            str = getResources().getString(R.string.each_day);
        }
        else {
            String days = DateTimeUtils.getDayOfWeek(dateTimeContainer.getRepeatList(), this);
            if (days.isEmpty()) {
                str = defDate;
            } else {
                str = defDate + " " + days;
            }
        }
        return str;
    }

    public void startDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new MDateListener(),
                DateTimeUtils.getCurrentYear(), DateTimeUtils.getCurrentMonth()-1, DateTimeUtils.getCurrentDayOfMonth());
        datePickerDialog.show();

    }

    class MDateListener implements DatePickerDialog.OnDateSetListener {


        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            Log.d("QSQSQS", "year="+year+"; monthOfYear="+monthOfYear+"; dayOfMonth="+dayOfMonth);
            dateTimeContainer.setYear(year);
            dateTimeContainer.setMonth(monthOfYear+1);
            dateTimeContainer.setDayOfMonth(dayOfMonth);
            defDate = "Since "+DateTimeUtils.getDayOfWeek(
                    DateTimeUtils.getLocalDateTime(year, monthOfYear+1, dayOfMonth).getDayOfWeek(),
                    ActivityScheduling.this)+", "+dayOfMonth;
            tvChoseDate.setText(getRepetition());

        }
    }

    private void initDays(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            view.setOnClickListener(this);
        }
    }
}
