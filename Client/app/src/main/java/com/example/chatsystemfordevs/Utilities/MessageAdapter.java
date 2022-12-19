package com.example.chatsystemfordevs.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsystemfordevs.R;

import java.util.Collections;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    List<MessageData> list = Collections.emptyList();

    Context context;

    public MessageAdapter(List<MessageData> list, Context context)
    {
        this.list = list;
        this.context = context;
    }

    @Override
    public MessageViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType)
    {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout

        View photoView = inflater.inflate(R.layout.guild_message, parent, false);

        MessageViewHolder viewHolder = new MessageViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void
    onBindViewHolder(final MessageViewHolder viewHolder, final int position)
    {
        final int index = viewHolder.getAdapterPosition();
        viewHolder.messageUsername.setText(list.get(position).username);
        viewHolder.messageDate.setText(list.get(position).date);
        viewHolder.messageMessage.setText(list.get(position).message);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
