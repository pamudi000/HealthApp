package com.pramodha.pilltasks;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.*;

public class MainActivity extends AppCompatActivity {

    private Button add;
    private ListView listView;
    private Context context;
    private DbHandler dbHandler;
    private List<ToDo> toDos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        dbHandler = new DbHandler(this);
        add = findViewById(R.id.addbtn);
        listView = findViewById(R.id.list_item);

        toDos = new ArrayList<>();

        toDos = dbHandler.getAllToDos();

        ToDoAdapter adapter = new ToDoAdapter(context,R.layout.single_todo,toDos);

        listView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AddToDo.class));
            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final ToDo todo = toDos.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Change Your Pill Details Here");
                builder.setMessage("Any changers to your pill ?");

                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHandler.deleteToDo(todo.getId());
                        startActivity(new Intent(context,MainActivity.class));
                    }
                });
                builder.setNeutralButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context,EditToDo.class);
                        intent.putExtra("id",String.valueOf(todo.getId()));
                        startActivity(intent);

                    }
                });

                builder.show();
            }
        });

    }
}
