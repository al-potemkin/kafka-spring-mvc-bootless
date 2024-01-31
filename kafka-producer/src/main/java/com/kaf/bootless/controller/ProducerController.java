package com.kaf.bootless.controller;

import com.kaf.bootless.service.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ProducerController {
    private final Producer producer;

    @Autowired
    public ProducerController(Producer producer) {
        this.producer = producer;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void send(@RequestParam("message") String message) {
        System.out.format("Message received: %s\n", message);
        producer.sendMessage(message);
    }
}
