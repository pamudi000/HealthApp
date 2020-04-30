package com.chamathka.medicinereminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chamathka.medicinereminder.database.DBHandler;
import com.chamathka.medicinereminder.database.MedicineModel;

public class activity_medicineDetail extends AppCompatActivity implements View.OnClickListener {

    private TextView nameText,dosageText,timeText;
    public Button deleteBtn;
    private ImageView typeImageView;
    private String medicineId;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);

        nameText=findViewById(R.id.title);
        dosageText=findViewById(R.id.dosage);
        timeText=findViewById(R.id.time);
        typeImageView=findViewById(R.id.type);
        deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(activity_medicineDetail.this); // calling onClick() method

        //getting data from intent
        medicineId=getIntent().getStringExtra("medicineId");
        String name=getIntent().getStringExtra("name");
        String dosage=getIntent().getStringExtra("dosage");
        String type=getIntent().getStringExtra("type");
        String time=getIntent().getStringExtra("time");

        //setting data to ui
        nameText.setText(name);
        dosageText.setText(dosage);
        timeText.setText(time);

        if(type.equals("Capsule")) {
            typeImageView.setImageResource(R.drawable.capsule_large);
        }else{
            typeImageView.setImageResource(R.drawable.bottle_large);
        }





    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.deleteBtn:
                // do your code
                //insertint to db

                DBHandler dbhandler=new DBHandler(activity_medicineDetail.this);

                //first getting writable databse
                SQLiteDatabase db=dbhandler.getWritableDatabase();
                db.delete(MedicineModel.TABLE_NAME,"id=?",new String[]{medicineId});
                db.close();
                System.out.println(medicineId);
                Intent alarmIntent = new Intent(this, MyReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(medicineId), alarmIntent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                Intent intent=new Intent(activity_medicineDetail.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;   }
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        Intent intent=new Intent(activity_medicineDetail.this,MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();  // optional depending on your needs
    }
}
