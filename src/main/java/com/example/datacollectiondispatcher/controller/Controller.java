package com.example.datacollectiondispatcher.controller;

import com.example.datacollectiondispatcher.dto.Invoice;
import com.example.datacollectiondispatcher.queue.Producer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final static String BROKER_URL = "tcp://localhost:6616";

    //private final static String DB_CONNECTION = "jdbc:postgresql://localhost:5432/dist?user=XXX$password=XXX";

    private final static String QUEUE_NAME = "INVOICE";

    @PostMapping("/invoices/{customer_id}")
    public Invoice create(@PathVariable String customer_id){
        Producer.send(customer_id, QUEUE_NAME, BROKER_URL);

        return new Invoice(customer_id);
    }
}
