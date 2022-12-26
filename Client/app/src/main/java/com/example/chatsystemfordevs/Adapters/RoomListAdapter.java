package com.example.chatsystemfordevs.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsystemfordevs.R;


public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomListViewHolder> {

    String[] names;
    Context context;

    public RoomListAdapter(Context context, String[] names)
    {
        this.context = context;
        this.names = names;
    }

    @Override
    public RoomListViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.room_list_item, parent, false);
        return new RoomListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RoomListViewHolder viewHolder, final int position)
    {
        viewHolder.roomName.setText(names[position]);
    }

    @Override
    public int getItemCount()
    {
        return names.length;
    }

    public static class RoomListViewHolder extends RecyclerView.ViewHolder {
        TextView roomName;

        public RoomListViewHolder(View itemView)
        {
            super(itemView);
            roomName = itemView.findViewById(R.id.room_name_text);
        }
    }
}
