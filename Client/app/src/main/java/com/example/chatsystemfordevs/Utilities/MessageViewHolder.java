package com.example.chatsystemfordevs.Utilities;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsystemfordevs.R;

public class MessageViewHolder
        extends RecyclerView.ViewHolder {
    TextView messageUsername;
    TextView messageMessage;
    TextView messageDate;

    MessageViewHolder(View itemView)
    {
        super(itemView);
        messageUsername = (TextView)itemView.findViewById(R.id.text_user);
        messageDate = (TextView)itemView.findViewById(R.id.text_date);
        messageMessage = (TextView)itemView.findViewById(R.id.text_message);
    }
}
