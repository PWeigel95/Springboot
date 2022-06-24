package com.example.springboot.controller;

import com.example.springboot.dto.Invoice;
import com.example.springboot.queue.Producer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;

@RestController
public class Controller {

    private final static String BROKER_URL = "tcp://localhost:61616";

    //private final static String DB_CONNECTION = "jdbc:postgresql://localhost:5432/postgres?user=admin&password=password";

    private final static String QUEUE_NAME = "INVOICE REQUEST";


    @PostMapping("/invoices/{customer_id}")
    public Invoice create(@PathVariable String customer_id) {

        //Message an DataCollectionDispatcher
        Producer.send(customer_id, QUEUE_NAME, BROKER_URL);


        return new Invoice(customer_id);
    }

    @GetMapping("/invoices/{customer_id}")
    public Invoice get(@PathVariable String customer_id){

        //TODO:
        return new Invoice(customer_id);
    }


}
