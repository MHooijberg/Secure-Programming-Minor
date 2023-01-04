package com.example.chatsystemfordevs.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsystemfordevs.R;

import java.util.ArrayList;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    ArrayList<GuildMessage> messages;
    Context context;

    public MessageAdapter(Context context, ArrayList<GuildMessage> messages)
    {
        this.context = context;
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
        viewHolder.messageUsername.setText(messages.get(position).getUsername());
        viewHolder.messageDate.setText(messages.get(position).getDate());
        viewHolder.messageMessage.setText(messages.get(position).getMessage());
    }

    @Override
    public int getItemCount()
    {
        return messages.size();
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

    public static class GuildMessage {
        String username, date, message;

        public GuildMessage(String username, String date, String message) {
            this.username = username;
            this.date = date;
            this.message = message;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public ArrayList<GuildMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<GuildMessage> messages) {
        this.messages = messages;
    }

    public void addMessage(GuildMessage message) {
        this.messages.add(message);
        notifyItemInserted(messages.size());
    }
}
