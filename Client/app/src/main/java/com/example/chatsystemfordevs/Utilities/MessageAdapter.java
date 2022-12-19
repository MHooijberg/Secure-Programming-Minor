package com.example.chatsystemfordevs.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsystemfordevs.R;

import java.util.Collections;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    String[] usernames, dates, messages;
    Context context;

    public MessageAdapter(Context context, String[] usernames, String[] dates, String[] messages)
    {
        this.context = context;
        this.usernames = usernames;
        this.dates = dates;
        this.messages = messages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.guild_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, final int position)
    {
        viewHolder.messageUsername.setText(usernames[position]);
        viewHolder.messageDate.setText(dates[position]);
        viewHolder.messageMessage.setText(messages[position]);
    }

    @Override
    public int getItemCount()
    {
        return messages.length;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageUsername, messageMessage, messageDate;

        public MessageViewHolder(View itemView)
        {
            super(itemView);
            messageUsername = itemView.findViewById(R.id.text_user);
            messageDate = itemView.findViewById(R.id.text_timestamp);
            messageMessage = itemView.findViewById(R.id.text_message);
        }
    }
}
