package mrb.partyapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mberger on 5/27/17.
 */

public class UpcomingPartyAdapter extends RecyclerView.Adapter<UpcomingPartyAdapter.MyViewHolder> {
    private List<RecyclerSource> RecyclerList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView partyName;
        public View mPartyRow;
        public TextView partyDate;
        public TextView numGuests;

        public MyViewHolder(View view) {
            super(view);
            partyName = (TextView) view.findViewById(R.id.name);
            partyDate = (TextView) view.findViewById(R.id.date);
            numGuests = (TextView) view.findViewById(R.id.guests);
            mPartyRow = view.findViewById(R.id.party_row_layout);
        }
    }

    public UpcomingPartyAdapter(List<RecyclerSource> RecyclerList, Context context) {
        this.RecyclerList = RecyclerList;
        this.mContext = context;
    }
    public UpcomingPartyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.party_row, parent, false);

        return new UpcomingPartyAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(UpcomingPartyAdapter.MyViewHolder holder, int position) {
        final RecyclerSource availableParty = RecyclerList.get(position);
        holder.partyName.setText(availableParty.getName());
        holder.partyDate.setText("Date: " + availableParty.getDate());
        holder.numGuests.setText("Number of guests: " + availableParty.getGuests());
        holder.mPartyRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPref.init(mContext);
                    SharedPref.write(SharedPref.Party,availableParty.getName());
                    Intent intent = new Intent(mContext, PartyInfoActivity.class);
                    mContext.startActivity(intent);}
            });
    }

    //Get size of list
    @Override
    public int getItemCount() {
        return RecyclerList.size();
    }
}
