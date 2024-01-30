package com.kaf.bootless.controller;

import com.kaf.bootless.service.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ProducerController {
    private Producer producer;

    @Autowired
    public ProducerController(Producer producer) {
        this.producer = producer;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{id}/{name}")
    public void send(@PathVariable("id") String id,
                     @PathVariable("name") String name) {
        System.out.format("Message received: %s, %s\n", id, name);
        producer.sendMessage(id, name);
    }
}
