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

    ArrayList<Guild> guilds;
    Guild searchButton = new Guild("search", "search");
    Guild newButton = new Guild("new", "new");
    ArrayList<GuildListViewHolder> viewHolders;
    OnGuildListener onGuildListener;
    Context context;
    Integer lastItemSelectedPos;

    public GuildListAdapter(Context context, ArrayList<Guild> guilds, OnGuildListener onGuildListener)
    {
        this.context = context;
        this.guilds = new ArrayList<>();
        this.guilds.add(searchButton);
        this.guilds.addAll(guilds);
        this.guilds.add(newButton);

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
        GuildListViewHolder viewHolder = new GuildListViewHolder(view, onGuildListener);
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final GuildListViewHolder viewHolder, final int position)
    {
        viewHolder.guildName.setText(guilds.get(position).getName());
        viewHolder.guildId.setText(guilds.get(position).getId());

    }

    @Override
    public int getItemCount()
    {
        return guilds.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        }
        if (position == guilds.size()-1) {
            return 2;
        }
        return 0;
    }

    public class GuildListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView guildName;
        TextView guildId;
        CardView cardView;
        OnGuildListener onGuildListener;

        public GuildListViewHolder(View itemView, OnGuildListener onGuildListener)
        {
            super(itemView);
            guildName = itemView.findViewById(R.id.guild_list_name_text);
            guildId = itemView.findViewById(R.id.guild_list_documentid_text);
            cardView = itemView.findViewById(R.id.guild_list_card);
            this.onGuildListener = onGuildListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            unselectItem(lastItemSelectedPos);
            selectItem(getAdapterPosition());
            lastItemSelectedPos=getAdapterPosition();
            onGuildListener.onGuildSelect(getAdapterPosition(), getViewHolders());
        }

        public TextView getGuildName() {
            return guildName;
        }

        public void setGuildName(TextView guildName) {
            this.guildName = guildName;
        }

        public TextView getGuildId() {
            return guildId;
        }

        public void setGuildId(TextView guildId) {
            this.guildId = guildId;
        }

        public CardView getCardView() {
            return cardView;
        }

        public void setCardView(CardView cardView) {
            this.cardView = cardView;
        }
    }

    public static class Guild {
        String name, id;

        public Guild(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public void unselectItem(int position) {
        viewHolders.get(position).getCardView().setRadius(100);
    }

    public void selectItem(int position) {
        viewHolders.get(position).getCardView().setRadius(20);
    }

    public interface OnGuildListener {
        void onGuildSelect(int position, ArrayList<GuildListViewHolder> viewHolders);
    }

    public ArrayList<Guild> getGuilds() {
        return guilds;
    }

    public void setGuilds(ArrayList<Guild> guilds) {
        this.guilds.clear();
        this.guilds.add(searchButton);
        this.guilds.addAll(guilds);
        this.guilds.add(newButton);
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

    public void setLastItemSelectedPos(int lastItemSelectedPos) {
        this.lastItemSelectedPos = lastItemSelectedPos;
    }

    public void clearViewHolders() {
        viewHolders.clear();
    }

    public void addGuild(Guild guild) {
        Log.d(TAG, guilds.toString());
        guilds.add(guilds.size()-1, guild);
        Log.d(TAG, guilds.toString());
    }
}
