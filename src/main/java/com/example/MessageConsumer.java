package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Scanner;

public class MessageConsumer {

    @Autowired
    @Qualifier("consumerList")
    private List<ConsumerThread> consumerThreadList;

    @Autowired
    private ConsumerThread consumerThread;

    public void recieveMessage() throws Exception{
        consumerThreadList.parallelStream().forEach(ConsumerThread::run);
        String line = "";
        Scanner in = new Scanner(System.in);
        while (!line.equals("exit")) {
            line = in.next();
        }
        consumerThreadList.parallelStream().forEach(c -> c.getKafkaConsumer().wakeup());
        System.out.println("Stopping consumer... ");
    }

}
