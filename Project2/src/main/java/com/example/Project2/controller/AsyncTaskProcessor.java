package com.example.Project2.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
/*
@RestController
public class AsyncTaskProcessor {

    @Autowired
    private ExcelDatabase excelDatabase;


    @RabbitListener(queues = "excelQueue")
    public void processExcelTask(String taskType,String clientId) throws Exception {
        if ("readDB".equals(taskType)) {
            excelDatabase.readDB(clientId);
            // 任务完成后通知客户端
            SseController.sendUpdate(clientId, "Import Task Completed");
        } else if ("writeDB".equals(taskType)) {
            excelDatabase.writeDB(clientId);
            // 任务完成后通知客户端
            SseController.sendUpdate(clientId, "Export Task Completed");
        }
    }
}
 */
