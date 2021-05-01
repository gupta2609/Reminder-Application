package com.example.ca1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class listview extends AppCompatActivity implements AdapterView.OnItemClickListener {

    FloatingActionButton bt;
    ListView lv;
    TextView tv;
    DB db;
    String s1;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;
    ArrayList<String> arrayList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        lv = (ListView)findViewById(R.id.lv);
        tv = (TextView)findViewById(R.id.tv);
        arrayList = new ArrayList<String>();
        arrayList1 = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_lv,R.id.textview,arrayList);
        lv.setAdapter(adapter);


        bt = findViewById(R.id.floatingActionButton);
        db = new DB(this);

        Cursor cursor = db.getdata();
        if(cursor.getCount()==0){
            tv.setText("You Dont Have Any Reminders");
        }
        else {
            StringBuffer buffer = new StringBuffer();
            StringBuffer buffer1 = new StringBuffer();
            while (cursor.moveToNext()){
                buffer1.append(cursor.getString(0));
                buffer.append("Reminder : "+cursor.getString(0)+"\n");
                buffer.append(cursor.getString(1)+"\n");
                buffer.append(cursor.getString(2)+"\n");
                s1=buffer.toString();
                arrayList.add(s1);
                buffer.delete(0,buffer.length());
                s1=null;
                adapter.notifyDataSetChanged();
                String l;
                l=buffer1.toString();
                arrayList1.add(l);
                buffer1.delete(0,buffer1.length());
                l=null;
            }

        }
        lv.setOnItemClickListener(this);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String q = arrayList1.get(position);

        boolean f = db.deletedata(q);
        if(f==true){
            arrayList.remove(position);
            arrayList1.remove(position);
            adapter.notifyDataSetChanged();
            q=null;
        }
        if(f==false) {
            Toast.makeText(getApplicationContext(),"Not Deleted"+q,Toast.LENGTH_SHORT).show();
        }

    }
}