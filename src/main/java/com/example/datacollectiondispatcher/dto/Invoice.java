package com.example.datacollectiondispatcher.dto;

public class Invoice {

    public String customer_id;
    public String status;

    public Invoice(){}

    public Invoice(String customer_id) {
        this.customer_id = customer_id;
        this.status = "send....";
    }
}
