package com.example.Project2.controller;

import com.example.Project2.dao.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExcelController {

    private final ExcelDatabase excelDatabase;
    private final SseController sseController;

    @Autowired
    public ExcelController(ExcelDatabase excelDatabase, SseController sseController) {
        this.excelDatabase = excelDatabase;
        this.sseController = sseController;
    }

    @GetMapping("/readDb")
    public String readDatabaseToExcel() {
        try {
            String clientId = sseController.generateClientId();
            //System.out.println(clientId);
            excelDatabase.readDB(clientId);

            return "数据库成功导出到 Excel";
        } catch (Exception e) {
            e.printStackTrace();
            return "导出数据库到 Excel 发生错误: " + e.getMessage();
        }
    }

    @GetMapping("/writeDb")
    public String writeExcelToDatabase(String clientId) {
        try {
            excelDatabase.writeDB(clientId);
            return "Excel 成功导入到数据库";
        } catch (Exception e) {
            e.printStackTrace();
            return "导入 Excel 到数据库发生错误: " + e.getMessage();
        }
    }
}
