package com.example.plannerapp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<EventGetSet> {
    private Activity context;
    private List<EventGetSet> eventList = new ArrayList<>();


    public ListAdapter(Activity context, List<EventGetSet> Task){
        super(context, R.layout.list_view, Task);

        this.context = context;
        this.eventList = Task;
    }

    // adds events to lists
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        LayoutInflater layoutInflater =context.getLayoutInflater();
        View rowView = layoutInflater.inflate( R.layout.list_view,null,true );
        TextView taskEventListName = rowView.findViewById( R.id.eventListName);
        TextView taskDescriptionResult = rowView.findViewById( R.id.descriptionResult);
        TextView taskDateResult = rowView.findViewById( R.id.dateResult);
        Button done = rowView.findViewById( R.id.done );

        final EventGetSet Details = eventList.get( position );
        taskEventListName.setText(Details.getTask());
        taskDescriptionResult.setText( Details.getDescription() );
        taskDateResult.setText( Details.getDate() );

        done.setOnClickListener( new View.OnClickListener() {
            // deletes info from database
            @Override
            public void onClick(View view) {
                Toast.makeText( context,"Deleting...",Toast.LENGTH_SHORT ).show();
                Log.d( "Tid",Details.getTid() );
                FirebaseDatabase.getInstance().getReference("Users")
                        .child( FirebaseAuth.getInstance().getCurrentUser().getUid() )
                        .child( Details.getTid() )
                        .removeValue();
            }
        } );
        return rowView;
    }

}
