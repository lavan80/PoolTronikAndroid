package com.pool.tronik.pooltronik;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.pool.tronik.pooltronik.utils.DateTimeUtils;

public class ActivityScheduling extends AppCompatActivity implements View.OnClickListener {

    private TimePicker timePicker;
    private TextView tvChoseDate;
    private int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduling);
        timePicker = findViewById(R.id.time_picker);
        initDays((ViewGroup) findViewById(R.id.ll_days_container));
        tvChoseDate = findViewById(R.id.tv_choosen_date);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minuteOfDay) {
                hour = hourOfDay;
                minute = minuteOfDay;
            }
        });
        String txt = getResources().getString(R.string.today);
        tvChoseDate.setText(txt +" - "+DateTimeUtils.getCurrentDayOfWeek()+", "+DateTimeUtils.getCurrentDayOfMonth());
    }

    @Override
    public void onClick(View view) {

        Drawable drawable = view.getBackground();
        if (drawable == null) {
            drawable = getDrawable(R.drawable.bg_rectangle);
            drawable.setTint(getResources().getColor(R.color.colorPrimary));
            view.setTag(View.VISIBLE);
        }
        else {
            int tagVisibility = (int) view.getTag();
            if (tagVisibility == View.VISIBLE) {
                drawable.setTint(getResources().getColor(R.color.transparent));
                view.setTag(View.INVISIBLE);
            }
            else {
                drawable.setTint(getResources().getColor(R.color.colorPrimary));
                view.setTag(View.VISIBLE);
            }
        }
        view.setBackground(drawable);

    }

    private void initDays(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            view.setOnClickListener(this);
        }
    }
}
