package com.example.chatsystemfordevs.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsystemfordevs.R;


public class GuildListAdapter extends RecyclerView.Adapter<GuildListAdapter.GuildListViewHolder> {

    String[] names;
    Context context;
    int index;

    public GuildListAdapter(Context context, String[] names)
    {
        this.context = context;
        this.names = new String[names.length + 2];
        this.names[0] = "search";
        index = 1;
        for (String name : names) {
            this.names[index] = name;
            index++;
        }
        this.names[index] = "new";
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
        return names.length;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        }
        if (position == index) {
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
