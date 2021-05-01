package com.example.ca1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et,et2;
    Button bt_time,bt_date,bt_set,bt_can,bt_next;
    String time,date,s1;
    Calendar calendarObj,cal;
    int months, years, day;
    int hours, minutes, seconds;
    AlarmManager am;
    PendingIntent pi;
    Intent intent;
    DB db;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bt_time=findViewById(R.id.bt1);
        bt_date=findViewById(R.id.bt2);
        bt_set=findViewById(R.id.bt3);
        bt_can=findViewById(R.id.bt4);

        et=findViewById(R.id.et1);

        bt_time.setOnClickListener(this);
        bt_date.setOnClickListener(this);
        bt_set.setOnClickListener(this);
        bt_can.setOnClickListener(this);


        db=new DB(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)


    @Override
    public void onClick(View v) {
        if(v==bt_time){
            final Calendar c = Calendar.getInstance();

            hours = c.get(Calendar.HOUR);
            minutes = c.get(Calendar.MINUTE);
            seconds = c.get(Calendar.SECOND);

            bt_time=findViewById(R.id.bt1);

            TimePickerDialog dialogInstance = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    bt_time.setText("Time: "+"H:"+hourOfDay+" M:"+minute);
                    time="Time: "+"H:"+hourOfDay+" M:"+minute;
                    hours=hourOfDay;
                    minutes=minute;
                }
            },hours, minutes, false);
            dialogInstance.show();
        }
        else if(v==bt_date)
        {
            calendarObj = Calendar.getInstance();
            months = calendarObj.get(Calendar.MONTH);
            day = calendarObj.get(Calendar.DAY_OF_MONTH);
            years = calendarObj.get(Calendar.YEAR);

            DatePickerDialog dialalogInstance = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    bt_date=findViewById(R.id.bt2);
                    bt_date.setText("Date: "+year+"-"+(month+1)+"-"+dayOfMonth);
                    date="Date: "+year+"-"+(month+1)+"-"+dayOfMonth;
                    months= month;
                    years=year;
                    day=dayOfMonth;
                }
            }, years, months, day);

            dialalogInstance.show();
        }
        else if(v==bt_set)
        {
            if(et.getText().length()==0)
            {
                et.setError("please Fill Text Here And Press Date,Time Buttons To Set Them");
            }
            else{
                String x=et.getText().toString();
                cal=Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY,hours);
                cal.set(Calendar.MINUTE,minutes);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MONTH,months);
                cal.set(Calendar.YEAR,years);
                cal.set(Calendar.DAY_OF_MONTH,day);
                set(cal,x);
            }


        }
        else if(v==bt_can)
        {
            am.cancel(pi);

            AlertDialog ad = new AlertDialog.Builder(MainActivity.this).setMessage("Reaminder Cancelled")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            ad.show();
            et.setText(null);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void set(Calendar c, String s)
    {

        long i=c.getTimeInMillis();

        s1=et.getText().toString();
        boolean res = db.insertdata(s1,date,time);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("text",s1);
        editor.apply();

        s1=sharedPreferences.getString("text","");

        intent = new Intent(getApplicationContext(),broadcastalaram.class);
        intent.putExtra("key",s1);
        pi = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);

        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC, i, pi);

        AlertDialog ad = new AlertDialog.Builder(MainActivity.this).setMessage("Reaminder Set at"+time)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent lintent = new Intent(getApplicationContext(), listview.class);
                        startActivity(lintent);
                    }
                }).create();
        ad.show();

    }

}

