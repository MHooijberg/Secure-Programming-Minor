package com.example.chatsystemfordevs.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsystemfordevs.R;

import java.util.ArrayList;


public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomListViewHolder> {

    ArrayList<String> names;
    ArrayList<String> ids;
    ArrayList<RoomListViewHolder> viewHolders;
    OnRoomListener onRoomListener;
    Context context;
    Integer lastItemSelectedPos;

    public RoomListAdapter(Context context, ArrayList<String> names, ArrayList<String> ids, OnRoomListener onRoomListener)
    {
        this.context = context;
        this.names = names;
        this.ids = ids;
        this.viewHolders = new ArrayList<>();
        this.onRoomListener = onRoomListener;
        lastItemSelectedPos = 0;
    }

    @Override
    public RoomListViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.room_list_item, parent, false);
        return new RoomListViewHolder(view, onRoomListener);
    }

    @Override
    public void onBindViewHolder(final RoomListViewHolder viewHolder, final int position)
    {
        viewHolder.roomName.setText(names.get(position));
        viewHolder.roomId.setText(ids.get(position));
        viewHolders.add(viewHolder);
    }

    @Override
    public int getItemCount()
    {
        return names.size();
    }

    public class RoomListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView roomName;
        TextView roomId;
        ConstraintLayout layout;
        OnRoomListener onRoomListener;

        public RoomListViewHolder(View itemView, OnRoomListener onRoomListener)
        {
            super(itemView);
            roomName = itemView.findViewById(R.id.room_name_text);
            roomId = itemView.findViewById(R.id.room_id_text);
            layout = itemView.findViewById(R.id.room_list_item_layout);
            this.onRoomListener = onRoomListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            unselectItem(lastItemSelectedPos);
            selectItem(getAdapterPosition());
            lastItemSelectedPos=getAdapterPosition();
            onRoomListener.onRoomClick(getAdapterPosition(), getViewHolders());
        }

        public TextView getRoomName() {
            return roomName;
        }

        public void setRoomName(TextView roomName) {
            this.roomName = roomName;
        }

        public TextView getRoomId() {
            return roomId;
        }

        public void setRoomId(TextView roomId) {
            this.roomId = roomId;
        }
    }

    public void unselectItem(int position) {
        viewHolders.get(position).roomName.setBackgroundResource(R.drawable.unselected_room_background);
        viewHolders.get(position).roomName.setTextColor(viewHolders.get(position).roomName.getContext().getColor(R.color.ghost));
    }

    public void selectItem(int position) {
        viewHolders.get(position).roomName.setBackgroundResource(R.drawable.selected_room_background);
        viewHolders.get(position).roomName.setTextColor(viewHolders.get(position).roomName.getContext().getColor(R.color.black));
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
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

    public ArrayList<RoomListViewHolder> getViewHolders() {
        return viewHolders;
    }

    public void setViewHolders(ArrayList<RoomListViewHolder> viewHolders) {
        this.viewHolders = viewHolders;
    }

    public interface OnRoomListener {
        void onRoomClick(int position, ArrayList<RoomListViewHolder> viewHolders);
    }

    public void clearViewHolder(){
        viewHolders.clear();
        notifyDataSetChanged();
    }

}
