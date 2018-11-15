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

/**
 * 用于显示运营商信息的Adapter
 */
public class CarrierAdapter extends RecyclerView.Adapter {
    private List<DataItem> dataItems;
    private Context context;

    /**
     * Adapter 构造方法
     * @param context Context实例
     * @param dataItems 需要显示的数组
     */
    public CarrierAdapter(Context context, List<DataItem> dataItems) {
        this.dataItems = dataItems;
        this.context = context;
    }

    /**
     * 获取显示数据
     *
     * @return 显示数据数组
     */
    public List<DataItem> getDataItems() {
        return dataItems;
    }

    /**
     * 设置显示数据
     *
     * @param dataItems 需要显示的数据
     */
    public void setDataItems(List<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    /**
     * 获取Context
     *
     * @return Context实例
     */
    public Context getContext() {
        return context;
    }

    /**
     * 设置Context
     *
     * @param context Context实例
     */
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
        holder.countTextView.setText(String.valueOf(dataItem.getCount()));
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    /**
     * 用于RecyclerView的ViewHolder
     */
    public class CarrierViewHolder extends RecyclerView.ViewHolder {
        // 显示月份
        private TextView monthTextView;
        // 显示数据类型
        private TextView typeTextView;
        // 显示数据条目个数
        private TextView countTextView;


        public CarrierViewHolder(@NonNull View itemView) {
            super(itemView);

            monthTextView = (TextView) itemView.findViewById(R.id.show_summary_month);
            typeTextView = (TextView) itemView.findViewById(R.id.show_summary_type);
            countTextView = (TextView) itemView.findViewById(R.id.show_summary_count);
        }
    }


    /**
     * 显示数据Holder
     */
    public static class DataItem {
        // 月份
        private String month;
        // 数据类型
        private String type;
        // 数据条目个数
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
