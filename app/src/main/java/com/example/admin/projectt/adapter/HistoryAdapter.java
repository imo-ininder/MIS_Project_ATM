package com.example.admin.projectt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.projectt.Message;
import com.example.admin.projectt.R;
import com.example.admin.projectt.view.History;

import java.util.ArrayList;

/**
 * Created by imo on 2016/9/28.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ItemHistoryHolder> {
    ArrayList<History> mItems = new ArrayList<>();

    public HistoryAdapter(){}
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
        holder.historyContent.setText("內容: " + h.getTaskContent());
        holder.status.setText("狀態: " + h.getTaskStatus());
        holder.date.setText(h.getTaskDate());
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

        public ItemHistoryHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.history_title);
            historyContent = (TextView) itemView.findViewById(R.id.history_content);
            status = (TextView) itemView.findViewById(R.id.history_status);
            date = (TextView) itemView.findViewById(R.id.history_date);
        }
    }
}
