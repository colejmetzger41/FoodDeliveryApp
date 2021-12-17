package com.c323FinalProject.colejmetzger.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.c323FinalProject.colejmetzger.CurrentDayDecorator;
import com.c323FinalProject.colejmetzger.R;
import com.c323FinalProject.colejmetzger.types.Order;
import com.c323FinalProject.colejmetzger.utilities.DatabaseHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    View view;
    MaterialCalendarView calendarView;
    DatabaseHelper databaseHelper;
    Order[] orders;
    TextView tv_amountSpent;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    // go through dates and get information from each order and add to date
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        tv_amountSpent = view.findViewById(R.id.textViewCalendarAmountSpent);

        Calendar calendar = Calendar.getInstance();

        List<Calendar> calendars = new ArrayList<>();

        databaseHelper = new DatabaseHelper(getContext());
        orders = databaseHelper.getOrders();
        for (int i = 0; i < orders.length; i++) {
            Date date = orders[i].getDateDate();
            Calendar newCalendar = Calendar.getInstance();
            newCalendar.set(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes());
            calendars.add(newCalendar);
        }

        calendarView.addDecorator(new CurrentDayDecorator(getActivity()));
        calendarView.setOnDateChangedListener((OnDateSelectedListener) new SelectedListenerHelper());

        return view;
    }

    // show totals when date is selected
    private class SelectedListenerHelper implements OnDateSelectedListener {
        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
            Calendar clickedDayCalendar = date.getCalendar();
            //Toast.makeText(getContext(), clickedDayCalendar.toString(), Toast.LENGTH_SHORT).show();
            Date clickedDate = clickedDayCalendar.getTime();
            int total = 0;
            for (int i = 0; i < orders.length; i++) {
                Date orderTime = orders[i].getDateDate();
                if (clickedDate.getYear() == orderTime.getYear() && clickedDate.getMonth() == orderTime.getMonth() && clickedDate.getDay() == orderTime.getDay()) {
                    total += orders[i].getTotal();
                }
            }
            tv_amountSpent.setText("Amount Spent: " + total);
        }
    }
}