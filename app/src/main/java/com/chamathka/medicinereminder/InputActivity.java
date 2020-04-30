package com.chamathka.medicinereminder;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.chamathka.medicinereminder.database.DBHandler;
import com.chamathka.medicinereminder.database.MedicineModel;

import java.util.Calendar;

public class InputActivity extends AppCompatActivity {

    private static int hrs,min;


    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    String[] MedicineType={"Medicine Type","Capsule","Liquid"};
    Button addTime,addMedicineBtn,cancelBtn;
    TextInputEditText  medicinename,medicineDosage;
    Spinner medicinetype;
    ChipGroup medicineTime;
    int hour_new, minutes_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        setTitle("Add a reminder");
        System.out.println("starting input activity");
        //getting component object
        medicinename=findViewById(R.id.name);
        medicineDosage=findViewById(R.id.dosage);
        medicinetype=findViewById(R.id.type);
        medicineTime=findViewById(R.id.time);
        addTime=findViewById(R.id.addTimeBtn);
        addMedicineBtn=findViewById(R.id.addMedicineBtn);
        cancelBtn=findViewById(R.id.cancelBtn);


         //setting event handling
        addMedicineBtn.setOnClickListener(addMedicineBtnHandler);
        addTime.setOnClickListener(addMedicineTimeHandler);
        cancelBtn.setOnClickListener(cancelHandler);







        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.type);
//Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,MedicineType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

    }




    private void setTag(final String name) {
        addTime.animate()
                .translationX(addTime.getHeight())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        addTime.setVisibility(View.GONE);
                    }
                });

        final ChipGroup chipGroup = findViewById(R.id.time);

        chipGroup.removeAllViews();

            final String tagName = name;
            final Chip chip = new Chip(this);
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
            chip.setText(tagName);
            chipGroup.addView(chip);
            chip.setOnClickListener(editMedicineTimeHandler);
        }

        //event handler
    //add medicine Handler
    private View.OnClickListener addMedicineBtnHandler=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            System.out.println("btn clicked");

            //getting data from ui
            String name,type,dosage,time;

            name=medicinename.getText().toString();
            dosage=medicineDosage.getText().toString();
            type=medicinetype.getSelectedItem().toString();
            System.out.println("Medine Name"+name+"dosage "+dosage+"tyoe of medicine "+type);

            int  timeCount=medicineTime.getChildCount();
            System.out.println("cont"+timeCount);
            String timeList="";
            for(int i=0;i<timeCount;i++){
                Chip chip=(Chip)medicineTime.getChildAt(i);
                timeList+=chip.getText();
                System.out.println("data at chup"+i+chip.getText());

            }

            System.out.println("data list"+timeList);

            if (name.isEmpty()) {
                AlertDialog alertDialog = new AlertDialog.Builder(InputActivity.this).create();
                alertDialog.setTitle("Medicine Reminder");
                alertDialog.setMessage("Please enter Medicine Name");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else if (dosage.isEmpty()) {
                AlertDialog alertDialog = new AlertDialog.Builder(InputActivity.this).create();
                alertDialog.setTitle("Medicine Reminder");
                alertDialog.setMessage("Please enter Medicine Dosage");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else if (timeList.isEmpty()) {
                AlertDialog alertDialog = new AlertDialog.Builder(InputActivity.this).create();
                alertDialog.setTitle("Medicine Reminder");
                alertDialog.setMessage("Please Add Time");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                insertData(name, dosage, type, timeList);

                //open the alarm list menu back
                Intent intent = new Intent(InputActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }












        }
    };

    private void insertData(String name,String dosage,String type, String time) {
        //insertint to db
        DBHandler dbhandler=new DBHandler(InputActivity.this);

        //first getting writable databse
        SQLiteDatabase db=dbhandler.getWritableDatabase();
        //add data
        ContentValues value = new ContentValues();
        value.put(MedicineModel.COLUMN_NAME,name);
        value.put(MedicineModel.COLUMN_DOSAGE,dosage);
        value.put(MedicineModel.COLUMN_TYPE,type);
        value.put(MedicineModel.COLUMN_TIME,time);

        //inserting in db
        long id = db.insert(MedicineModel.TABLE_NAME, null, value);
        System.out.println("row inserted is "+id);
        // close db connection
        db.close();
        //registering event
        System.out.println("database insertion completion");

        registerAlarm(((int) id),time);



    }

    private void registerAlarm(int id,String time) {
        System.out.println("registering alarm called");
        //setting pending intent

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, MyReceiver.class);
        alarmIntent.putExtra("id",id+"");
        pendingIntent = PendingIntent.getBroadcast(this, id, alarmIntent, 0);

        //start alarm

        startAlarm(hrs,min);




    }

    private void startAlarm(int hrs,int min) {
        System.out.println("start alarm called");
        //starting alarm

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, hrs);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1); // add, not set!
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);


    }

    //adding time from timepicker and setting into tag component
    private View.OnClickListener addMedicineTimeHandler=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(InputActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    hour_new = selectedHour;
                    minutes_new = selectedMinute;
                    String timeSet = "";
                    if (hour_new > 12) {
                        hour_new -= 12;
                        timeSet = "PM";
                    } else if (hour_new == 0) {
                        hour_new += 12;
                        timeSet = "AM";
                    } else if (hour_new == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min_new = "";
                    if (minutes_new < 10)
                        min_new = "0" + minutes_new ;
                    else
                        min_new = String.valueOf(minutes_new);
                    //eReminderTime.setText( selectedHour + ":" + selectedMinute);
                    String am_pm = (selectedHour < 12) ? " AM" : " PM";
                    setTag(hour_new + ":" + min_new+timeSet);
                    hrs=selectedHour;
                    min=selectedMinute;
                }
            }, hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        }
    };

    //edit time from timepicker and setting into tag component
    private View.OnClickListener editMedicineTimeHandler=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(InputActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    hour_new = selectedHour;
                    minutes_new = selectedMinute;
                    String timeSet = "";
                    if (hour_new > 12) {
                        hour_new -= 12;
                        timeSet = "PM";
                    } else if (hour_new == 0) {
                        hour_new += 12;
                        timeSet = "AM";
                    } else if (hour_new == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min_new = "";
                    if (minutes_new < 10)
                        min_new = "0" + minutes_new ;
                    else
                        min_new = String.valueOf(minutes_new);
                    //eReminderTime.setText( selectedHour + ":" + selectedMinute);
                    String am_pm = (selectedHour < 12) ? " AM" : " PM";
                    setTag(hour_new + ":" + min_new+timeSet);
                    hrs=selectedHour;
                    min=selectedMinute;
                }
            }, hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        }
    };

    //cancel btn handler

    private View.OnClickListener cancelHandler= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(InputActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
            //go back to previous activity
        }
    };

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        Intent intent=new Intent(InputActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();  // optional depending on your needs
    }
}

