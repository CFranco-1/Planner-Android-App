package com.example.plannerapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventData extends AppCompatActivity {

    private EditText event = null, description = null;
    private String fullDate;
    private Button date = null;
    private Calendar calendar = null;
    private DatePickerDialog datePickerDialog=null;
    private TextView showDate = null;
    int day, month, year, d, m, i=0;
    private FirebaseAuth mAuth;
    ListView listView = null;
    DatabaseReference db;
    FirebaseUser user;
    String userId;
    List<EventGetSet> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.events);

        // get objects
        event = findViewById( R.id.eventTitle);
        description = findViewById( R.id.eventDescription);
        date = findViewById( R.id.date );
        showDate = findViewById( R.id.dateText);
        listView = findViewById( R.id.listView);
        eventList = new ArrayList<>(  );

        // authorization instance
        mAuth = FirebaseAuth.getInstance();
        // database instance
        db = FirebaseDatabase.getInstance().getReference("Users");
        user = mAuth.getCurrentUser();
        userId = user.getUid();


        // Date Picker
        date.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog( EventData.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        showDate.setText( day +"/"+ month + "/" + year );
                        fullDate = day + "/" + month + "/" + year;
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        } );
    }

    @Override
    protected void onStart() {
        super.onStart();
        db.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnap) {
                eventList.clear(); // clears the list if any previous data still present

                for(DataSnapshot eventSnap:dataSnap.getChildren()) {
                    // user data fetched
                    if(eventSnap.getKey().equals(userId)) {
                        for (DataSnapshot eventSnap2 : eventSnap.getChildren()) {
                            String getTask = eventSnap2.getValue( EventGetSet.class ).getTask();
                            String getDes = eventSnap2.getValue( EventGetSet.class ).getDescription();
                            String getDate = eventSnap2.getValue( EventGetSet.class ).getDate();
                            String getTid = eventSnap2.getKey();
                            EventGetSet eventList = new EventGetSet( getTask, getDes, getDate ,getTid);

                            EventData.this.eventList.add( eventList );
                            // pushing the data in the custom adapter
                            ListAdapter adapter = new ListAdapter( EventData.this, EventData.this.eventList);
                            listView.setAdapter( adapter );

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }

    // function to add new task in the database
    public void addEvent(View v)
    {
        event = findViewById( R.id.eventTitle);
        description = findViewById( R.id.eventDescription);
        if(event.getText()==null || description.getText()==null || fullDate ==null)
        {
            Toast.makeText( getApplicationContext(),"All fields are mandatory",Toast.LENGTH_SHORT ).show();
        }
        else {
            //adds the data to the database
            EventGetSet eventInfo = new EventGetSet( " " + event.getText(), " " + description.getText(), " " + fullDate, "N/A" );
            FirebaseDatabase.getInstance().getReference( "Users" )
                    .child( FirebaseAuth.getInstance().getCurrentUser().getUid() )
                    .push().setValue( eventInfo );
            Toast.makeText( getApplicationContext(),"Event added",Toast.LENGTH_SHORT ).show();
        }
    }

    // for logging out the user
    public void onLogout(View v)
    {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText( getApplicationContext(),"You are logged out.",Toast.LENGTH_SHORT ).show();
        finish();
        startActivity( new Intent( this, LoginPage.class ) );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Successfully Signed Out", Toast.LENGTH_SHORT).show();
                finish();
                startActivity( new Intent( this, LoginPage.class ) );
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
