package com.example.admin.projectt.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.projectt.Message;
import com.example.admin.projectt.R;
import com.example.admin.projectt.view.History;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;

/**
 * Created by imo on 2016/9/28.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ItemHistoryHolder> {
    ArrayList<History> mItems = new ArrayList<>();
    int color;
    public HistoryAdapter(int color){
        this.color = color;
    }
    @Override
    public ItemHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history,parent,false);
        ItemHistoryHolder ihh = new ItemHistoryHolder(v);
        return ihh;
    }

    @Override
    public void onBindViewHolder(ItemHistoryHolder holder, int position) {
        final History h = mItems.get(position);
        holder.title.setText(h.getTaskTitle());
        holder.title.setBackgroundColor(color);
        holder.historyContent.setText(h.getTaskContent());
        holder.status.setText(h.getTaskStatus());
        holder.date.setText(h.getTaskDate());

        if ("等待中".equals(h.getTaskStatus())) {
            holder.icon.setIcon(MaterialDrawableBuilder.IconValue.CHECKBOX_MULTIPLE_BLANK_CIRCLE);
            holder.icon.setColor(Color.rgb(255, 87, 34));
        } else if ("進行中".equals(h.getTaskStatus())) {
            holder.icon.setIcon(MaterialDrawableBuilder.IconValue.PLAY_CIRCLE);
            holder.icon.setColor(Color.rgb(75, 174, 78));
        } else if ("沒人回應".equals(h.getTaskStatus())) {
            holder.icon.setIcon(MaterialDrawableBuilder.IconValue.MINUS_CIRCLE);
            holder.icon.setColor(Color.rgb(254, 192, 5));
        } else if ("取消".equals(h.getTaskStatus())) {
            holder.icon.setIcon(MaterialDrawableBuilder.IconValue.CLOSE_CIRCLE);
            holder.icon.setColor(Color.rgb(243, 66, 53));
        } else if ("完成".equals(h.getTaskStatus())) {
            holder.icon.setIcon(MaterialDrawableBuilder.IconValue.CHECKBOX_MARKED_CIRCLE);
            holder.icon.setColor(Color.rgb(75, 174, 78));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addHistory(History h){
            mItems.add(h);
            notifyDataSetChanged();
    }

    public void clearHistory() {
        mItems.clear();
        notifyDataSetChanged();
    }


    public static class ItemHistoryHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView historyContent;
        TextView status;
        TextView date;
        MaterialIconView icon;

        public ItemHistoryHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.history_title);
            historyContent = (TextView) itemView.findViewById(R.id.history_content);
            status = (TextView) itemView.findViewById(R.id.history_status);
            date = (TextView) itemView.findViewById(R.id.history_date);
            icon = (MaterialIconView) itemView.findViewById(R.id.icon);
        }
    }
}
