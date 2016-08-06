package com.alrugaib.delivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class OrderAdapter extends BaseAdapter {
    private List<OrderModel> dataset;
    private LayoutInflater inflater;
    private AdapterCallback callback;
    private boolean isSorted = false;
    private Context context;

    public OrderAdapter(Context context) {
        dataset = new ArrayList<>();
        this.context = context;
        callback = (AdapterCallback) context;
        inflater = LayoutInflater.from(context);
    }

    public void addElement(OrderModel element) {
        dataset.add(element);
        isSorted = false;
        notifyDataSetChanged();
    }

    public void updateDataset(List<OrderModel> newDataset) {
        dataset = newDataset;
        isSorted = true;
        notifyDataSetChanged();
    }

    public List<OrderModel> getDataset(){
        return dataset;
    }

    public boolean isSorted() {
        return isSorted;
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public OrderModel getItem(int position) {
        return dataset.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataset.get(position).getInvoiceNumber();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final View row;
        row = inflater.inflate(R.layout.item_list, null);
        TextView invoiceNumber = (TextView) row.findViewById(R.id.invoice_number);
        invoiceNumber.setText(String.valueOf(dataset.get(position).getInvoiceNumber()));
        row.findViewById(R.id.navigate_to).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onNavigateClicked(dataset.get(position));
                //row.setBackgroundColor(context.getColor(R.color.colorPrimary));
            }
        });
        row.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataset.remove(position);
                notifyDataSetChanged();
            }
        });
        return row;
    }

    public interface AdapterCallback {
        void onNavigateClicked(OrderModel model);
    }
}