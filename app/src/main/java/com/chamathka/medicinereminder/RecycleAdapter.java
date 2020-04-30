package com.chamathka.medicinereminder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    private ArrayList<String> medicineId=new ArrayList<>();
    private ArrayList<Integer> imageId=new ArrayList<>();
    private ArrayList<String> medicineName=new ArrayList<>();
    private ArrayList<String> medicineDosage=new ArrayList<>();
    private ArrayList<String> medicineType=new ArrayList<>();
    private ArrayList<String> medicineTime=new ArrayList<>();
    private Context context;

    public RecycleAdapter(Context context,ArrayList<String> medicineId, ArrayList<Integer> imageId, ArrayList<String> medicineName, ArrayList<String> medicineDosage, ArrayList<String> medicineType, ArrayList<String> medicineTime) {
        this.medicineId = medicineId;
        this.imageId = imageId;
        this.medicineName = medicineName;
        this.medicineDosage = medicineDosage;
        this.medicineType = medicineType;
        this.medicineTime = medicineTime;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem,viewGroup,false);
       ViewHolder holder =new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Log.d(TAG, "onBindViewHolder: called");
        viewHolder.image.setImageResource(imageId.get(i));
        viewHolder.name.setText(medicineName.get(i));
        viewHolder.dosage.setText(medicineDosage.get(i));
        viewHolder.listtime.setText(medicineTime.get(i));
        final int position=i;

        viewHolder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //opening activity showing medicine details
                Intent intent=new Intent(context,activity_medicineDetail.class);
                intent.putExtra("medicineId",medicineId.get(position));
                intent.putExtra("name",medicineName.get(position));
                intent.putExtra("name",medicineName.get(position));
                intent.putExtra("dosage",medicineDosage.get(position));
                intent.putExtra("type",medicineType.get(position));
                intent.putExtra("time",medicineTime.get(position));
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
            ImageView image;
            TextView name,dosage,listtime;
            RelativeLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image =itemView.findViewById(R.id.icon);
            name=itemView.findViewById(R.id.name);
            dosage=itemView.findViewById(R.id.dosage);
            listtime=itemView.findViewById(R.id.medicineListTime);

            parent_layout=itemView.findViewById(R.id.parent_layout);
        }
    }



}
