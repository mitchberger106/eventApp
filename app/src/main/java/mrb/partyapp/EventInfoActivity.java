package mrb.partyapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mberger on 9/5/17.
 */

public class EventInfoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private PartyInfoAdapter mAdapter;
    private List<RecyclerSource> RecyclerList = new ArrayList<>();
    private int attended = 0;
    String name;
    String GuestName;
    private Context mContext;
    private static final String TAG = "PartyInfoActivity";


    private void loadSession(){
        final String partyName = name;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Parties");
        getAttended();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                RecyclerList.clear();
                for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {
                    String name = Snapshot.child("Name").getValue(String.class);
                    String date = Snapshot.child("Date").getValue(String.class);
                    String numGuests = Snapshot.child("numGuests").getValue(String.class);
                    String location = Snapshot.child("Location").getValue(String.class);
                    String time = Snapshot.child("Time").getValue(String.class);
                    String attend = Snapshot.child("Attended").getValue(String.class);
                    RecyclerSource party = new RecyclerSource(name, date, numGuests, location, time, attend);
                    if(name == partyName) {
                        RecyclerList.add(party);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("here", "Failed to read value.", error.toException());
            }
        });
    }
    private void getAttended() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Parties").child(name).child("Guests");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                attended = 0;
                DatabaseReference myRef1 = database.getReference("Parties").child(name).child("Attended");
                myRef1.setValue(Integer.toString(attended));
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {
                    String status = Snapshot.child("Status").getValue(String.class);
                    if(status != null) {
                        if (status.equals("Attended")) {
                            attended++;
                            myRef1 = database.getReference("Parties").child(name).child("Attended");
                            myRef1.setValue(Integer.toString(attended));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("here", "Failed to read value.", error.toException());
            }
        });
    }
}
