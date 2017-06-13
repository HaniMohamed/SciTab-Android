package com.a3louq.sci_tab;

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
    private FirebaseDatabase database;
    private DatabaseReference dbReference;

    DBConnection() {
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();
    }

    void dbValue(final String index) {
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dbValue = (String) dataSnapshot.child(index).getValue();
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
