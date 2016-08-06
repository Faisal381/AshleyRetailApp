package com.alrugaib.delivery;

import com.google.android.gms.maps.model.LatLng;


public class OrderModel{
        private LatLng location;
        private int invoiceNumber;


        public OrderModel(int invoiceNumber, LatLng location) {
            this.invoiceNumber = invoiceNumber;
            this.location = location;
        }

        public int getInvoiceNumber() {
            return invoiceNumber;
        }

        public LatLng getLocation() {
            return location;
        }
}