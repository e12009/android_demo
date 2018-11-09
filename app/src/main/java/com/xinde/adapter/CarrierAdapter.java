package com.xinde.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.xinde.app.R;

import java.util.List;

public class CarrierAdapter extends RecyclerView.Adapter {
    private List<DataItem> dataItems;
    private Context context;

    public CarrierAdapter(Context context, List<DataItem> dataItems) {
        this.dataItems = dataItems;
        this.context = context;
    }

    public List<DataItem> getDataItems() {
        return dataItems;
    }

    public void setDataItems(List<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.carrier_result_item, viewGroup, false);
        return new CarrierViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        CarrierViewHolder holder = (CarrierViewHolder) viewHolder;
        DataItem dataItem = dataItems.get(position);

        holder.typeTextView.setText(dataItem.getType());
        holder.monthTextView.setText(dataItem.getMonth());
        holder.countTextView.setText(dataItem.getCount());
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class CarrierViewHolder extends RecyclerView.ViewHolder {
        private TextView monthTextView;
        private TextView typeTextView;
        private TextView countTextView;


        public CarrierViewHolder(@NonNull View itemView) {
            super(itemView);

            monthTextView = (TextView) itemView.findViewById(R.id.show_summary_month);
            typeTextView = (TextView) itemView.findViewById(R.id.show_summary_type);
            countTextView = (TextView) itemView.findViewById(R.id.show_summary_count);
        }
    }

    public static class DataItem {
        private String month;
        private String type;
        private int count;

        public DataItem(String month, String type, int count) {
            this.month = month;
            this.type = type;
            this.count = count;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
