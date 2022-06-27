package com.example.springboot.controller;

import com.example.springboot.queue.Producer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class Controller {

    private final static String BROKER_URL = "tcp://localhost:61616";

    //private final static String DB_CONNECTION = "jdbc:postgresql://localhost:5432/postgres?user=admin&password=password";

    private final static String QUEUE_NAME = "INVOICE REQUEST";

    private final Random rnd = new Random();

    private final Map<String, String> invoicesForCustomerId = new HashMap<>();

    @PostMapping("/invoices/{customer_id}")
    public String create(@PathVariable("customer_id") String customerId) {
        String datePrefix = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
        int randomNum = 1000 + this.rnd.nextInt(10000 - 1000);
        String invoiceId = datePrefix + randomNum;

        invoicesForCustomerId.put(customerId, invoiceId);

        //Message an DataCollectionDispatcher
        Producer.send(customerId + ":" + invoiceId, QUEUE_NAME, BROKER_URL);


        return "ok";
    }

    private FileTime getFileCreationTime(Path path) {
        try {
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
            return attr.creationTime();
        } catch (IOException ex) {
            return null;
        }
    }

    @GetMapping("/invoices/{customer_id}")
    public String get(@PathVariable("customer_id") String customerId) {
        // Invoice invoice = new Invoice(customerId);

        String invoiceId = this.invoicesForCustomerId.get(customerId);
        if (invoiceId == null) {
            return "not found";
        }

        String absolutePath = System.getProperty("user.home") + File.separator + "Invoice_" + invoiceId + ".pdf";

        FileTime creationTime = this.getFileCreationTime(new File(absolutePath).toPath());
        if (creationTime == null) {
            return "generating";
        }

        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(creationTime.toInstant().atZone(ZoneOffset.systemDefault())) + ";" + absolutePath;
    }


}
