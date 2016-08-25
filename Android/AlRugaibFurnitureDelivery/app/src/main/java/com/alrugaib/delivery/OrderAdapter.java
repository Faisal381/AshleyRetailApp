package com.alrugaib.delivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alrugaib.delivery.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for order list
 */
public class OrderAdapter extends BaseAdapter {
    private List<Order> dataset;
    private LayoutInflater inflater;
    private AdapterCallback callback;
    private boolean isSorted = false;

    /**
     * Constructor
     *
     * @param context - context for inflater
     */
    public OrderAdapter(Context context) {
        dataset = new ArrayList<>();
        callback = (AdapterCallback) context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * Add element to dataset , mark is as not sorted anymore
     *
     * @param element - Order to add to list
     */
    public void addElement(Order element) {
        dataset.add(element);
        isSorted = false;
        notifyDataSetChanged();
    }

    /**
     * Change dataset to new one
     *
     * @param newDataset - new list to replace adapter dataset with
     */
    public void updateDataset(List<Order> newDataset) {
        dataset = newDataset;
        isSorted = true;
        notifyDataSetChanged();
    }

    /**
     * Getter for dataset list
     *
     * @return list of orders that is currently dataset of adapter
     */
    public List<Order> getDataset() {
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
    public Order getItem(int position) {
        return dataset.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        //Init item in adapter
        final View row;
        row = inflater.inflate(R.layout.item_list, null);
        TextView invoiceNumber = (TextView) row.findViewById(R.id.invoice_number);
        invoiceNumber.setText(String.valueOf(dataset.get(position).getInvoiceNumber()));
        row.findViewById(R.id.navigate_to).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onNavigateClicked(dataset.get(position));
            }
        });
        row.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataset.remove(position);
                callback.onItemRemoved(position);
                notifyDataSetChanged();
            }
        });
        return row;
    }

    /**
     * Callback interface to provide communication with top layer
     */
    public interface AdapterCallback {
        /**
         * On navigation icon clicked inside list item
         *
         * @param model - model of order that has been clicked on list
         */
        void onNavigateClicked(Order model);

        /**
         * Method notifying container that item has been removed
         *
         * @param position - position of removed item
         */
        void onItemRemoved(int position);
    }
}
