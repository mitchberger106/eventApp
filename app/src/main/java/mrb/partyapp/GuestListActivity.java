package mrb.partyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mberger on 6/4/17.
 */

public class GuestListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private GuestListAdapter mAdapter;
    private List<GuestListSource> GuestList = new ArrayList<>();
    private String PartyName;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guestlist);

        PartyName = SharedPref.read(SharedPref.Party,"");

        mAdapter = new GuestListAdapter(GuestList, this);

        mRecyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && fab.isShown())
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestListActivity.this, SearchViewActivity.class);
                startActivity(intent);
            }
        });

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Guest List");

        mRecyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mAdapter = new GuestListAdapter(GuestList, this);
        mRecyclerView.setAdapter(mAdapter);

        getList();
        Log.d("Guest list size: ", "Size: " + GuestList.size());
    }
    private void getList(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Parties").child(PartyName).child("Guests");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GuestList.clear();
                for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {
                    String name = Snapshot.child("Name").getValue(String.class);
                    String status = Snapshot.child("Status").getValue(String.class);
                    String number = Snapshot.child("Number").getValue(String.class);
                    Log.d("hi", "Value is: " + name);
                    GuestListSource guest = new GuestListSource(name, status, number);
                    GuestList.add(guest);
                    mAdapter.notifyDataSetChanged();

                    /*if(status != null) {
                        if (status.equals("Attended")) {
                            mCheckBox.setChecked(true);
                        }
                    }*/

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef0 = database.getReference("Parties");
                    final DatabaseReference myRef1 = myRef0.child(PartyName);
                    final DatabaseReference myRef2 = myRef1.child("numGuests");
                    myRef2.setValue(Integer.toString(GuestList.size()));
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
