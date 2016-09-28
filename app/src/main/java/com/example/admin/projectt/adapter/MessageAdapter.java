package com.example.admin.projectt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.projectt.Message;
import com.example.admin.projectt.R;

import java.util.ArrayList;

/**
 * Created by imo on 2016/9/28.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ItemMessageHolder> {
    ArrayList<Message> mItems = new ArrayList<>();
    final String mMyId;

    public MessageAdapter(final String myId) {
        mMyId = myId;
    }

    @Override
    public ItemMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_message, parent, false);
        ItemMessageHolder imh = new ItemMessageHolder(v);
        return imh;
    }

    @Override
    public void onBindViewHolder(ItemMessageHolder holder, int position) {
        final Message message = mItems.get(position);
        holder.Name.setText(message.getAuthor());
        holder.Content.setText(message.getMessage());

        if (mMyId.equals(message.getAuthor())) { // mine
            holder.MessageBox.setGravity(Gravity.RIGHT);
            holder.LeftPad.setVisibility(View.VISIBLE);
            holder.RightPad.setVisibility(View.GONE);
        } else { // other
            holder.MessageBox.setGravity(Gravity.LEFT);
            holder.LeftPad.setVisibility(View.GONE);
            holder.RightPad.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addMessage(final Message m) {
        mItems.add(m);
        notifyDataSetChanged();
    }

    public void clearMessage() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public static class ItemMessageHolder extends RecyclerView.ViewHolder {
        TextView Name;
        TextView Content;
        LinearLayout MessageBox;
        View LeftPad, RightPad;

        public ItemMessageHolder(View itemView) {
            super(itemView);

            MessageBox = (LinearLayout) itemView.findViewById(R.id.message_box);
            Name = (TextView) itemView.findViewById(R.id.name);
            Content = (TextView) itemView.findViewById(R.id.content);
            LeftPad = itemView.findViewById(R.id.left_padding);
            RightPad = itemView.findViewById(R.id.right_padding);
        }
    }
}
