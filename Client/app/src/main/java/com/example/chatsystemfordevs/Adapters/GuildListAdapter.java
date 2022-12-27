package com.example.chatsystemfordevs.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsystemfordevs.R;

import java.util.ArrayList;


public class GuildListAdapter extends RecyclerView.Adapter<GuildListAdapter.GuildListViewHolder> {

    ArrayList<String> names;
    Context context;

    public GuildListAdapter(Context context, ArrayList<String> names)
    {
        this.context = context;
        this.names = new ArrayList<>();
        this.names.add("search");
        this.names.addAll(names);
        this.names.add("new");
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
        return new GuildListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GuildListViewHolder viewHolder, final int position)
    {
        //viewHolder.guildName.setText(names[position]);
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

    public static class GuildListViewHolder extends RecyclerView.ViewHolder {
        TextView guildName;

        public GuildListViewHolder(View itemView)
        {
            super(itemView);
            //guildName = itemView.findViewById(R.id.text_user);
        }
    }
}
