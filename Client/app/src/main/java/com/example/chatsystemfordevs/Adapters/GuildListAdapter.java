package com.example.chatsystemfordevs.Adapters;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsystemfordevs.R;

import java.util.ArrayList;


public class GuildListAdapter extends RecyclerView.Adapter<GuildListAdapter.GuildListViewHolder> {

    ArrayList<String> names;
    //TODO: Implement getting images for the guilds
    ArrayList<GuildListViewHolder> viewHolders;
    OnGuildListener onGuildListener;
    Context context;
    Integer lastItemSelectedPos;

    public GuildListAdapter(Context context, ArrayList<String> names, OnGuildListener onGuildListener)
    {
        this.context = context;
        this.names = new ArrayList<>();
        this.names.add("search");
        this.names.addAll(names);
        this.names.add("new");
        this.viewHolders = new ArrayList<>();
        this.onGuildListener = onGuildListener;
        lastItemSelectedPos = 0;
    }

    @Override
    public GuildListViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.guild_list_item, parent, false);
        if (viewType == 1) {
            view = inflater.inflate(R.layout.guild_list_item_search, parent, false);
        }
        if (viewType == 2) {
            view = inflater.inflate(R.layout.guild_list_item_new, parent, false);
        }
        return new GuildListViewHolder(view, onGuildListener);
    }

    @Override
    public void onBindViewHolder(final GuildListViewHolder viewHolder, final int position)
    {
        viewHolder.guildName.setText(names.get(position));
        viewHolders.add(viewHolder);
    }

    @Override
    public int getItemCount()
    {
        return names.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        }
        if (position == names.size()-1) {
            return 2;
        }
        return 0;
    }

    public class GuildListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView guildName;
        CardView cardView;
        //ImageView imageView;
        OnGuildListener onGuildListener;

        public GuildListViewHolder(View itemView, OnGuildListener onGuildListener)
        {
            super(itemView);
            guildName = itemView.findViewById(R.id.guild_list_name_text);
            cardView = itemView.findViewById(R.id.guild_list_card);
            //imageView = itemView.findViewById(R.id.guild_list_card_image);
            this.onGuildListener = onGuildListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            unselectItem(lastItemSelectedPos);
            selectItem(getAdapterPosition());
            lastItemSelectedPos=getAdapterPosition();
            onGuildListener.onGuildClick(getAdapterPosition(), getViewHolders());
        }

        public TextView getGuildName() {
            return guildName;
        }

        public void setGuildName(TextView guildName) {
            this.guildName = guildName;
        }

        public CardView getCardView() {
            return cardView;
        }

        public void setCardView(CardView cardView) {
            this.cardView = cardView;
        }
    }

    public void unselectItem(int position) {
        viewHolders.get(position).getCardView().setRadius(100);
    }

    public void selectItem(int position) {
        viewHolders.get(position).getCardView().setRadius(20);
    }

    public interface OnGuildListener {
        void onGuildClick(int position, ArrayList<GuildListViewHolder> viewHolders);
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public ArrayList<GuildListViewHolder> getViewHolders() {
        return viewHolders;
    }

    public void setViewHolders(ArrayList<GuildListViewHolder> viewHolders) {
        this.viewHolders = viewHolders;
    }

    public OnGuildListener getOnGuildListener() {
        return onGuildListener;
    }

    public void setOnGuildListener(OnGuildListener onGuildListener) {
        this.onGuildListener = onGuildListener;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Integer getLastItemSelectedPos() {
        return lastItemSelectedPos;
    }

    public void setLastItemSelectedPos(Integer lastItemSelectedPos) {
        this.lastItemSelectedPos = lastItemSelectedPos;
    }
}
