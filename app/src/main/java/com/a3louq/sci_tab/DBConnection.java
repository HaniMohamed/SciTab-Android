package com.a3louq.sci_tab;

import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ahmed on 6/1/17.
 */

class DBConnection {

    private String dbValue;

    DBConnection(final String index) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbReference = database.getReference();

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dbValue = (String) dataSnapshot.child(index).getValue();
                Log.e("Value", dbValue);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    String getDBValue() {
        return dbValue;
    }
}
