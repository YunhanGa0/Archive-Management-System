package com.example.Project2.controller;

import com.example.Project2.bean.Archive;
import com.example.Project2.dao.ArchiveRepository;
import com.example.Project2.dao.BatchRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Component
public class ExcelDatabase {

    private final BatchRepository batchRepository;

    private final RabbitTemplate rabbitTemplate;
    private final SseController sseController;

    @Autowired
    public ExcelDatabase(BatchRepository batchRepository, RabbitTemplate rabbitTemplate, SseController sseController) {
        this.batchRepository = batchRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.sseController = sseController;
    }

    public void readDB(String clientId) throws Exception { // 读取数据库内容输出Excel
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection(
                "jdbc:mysql://172.25.67.174:3306/gyh_test?useUnicode=true&characterEncoding=UTF-8",
                "gyh" ,
                "Gyh@gyh123,"
        );

        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from archive_management");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("archive db");

        XSSFRow row = spreadsheet.createRow(0);
        row.createCell(0).setCellValue("name");
        row.createCell(1).setCellValue("code");
        row.createCell(2).setCellValue("parent_code");
        row.createCell(3).setCellValue("special_archivist");
        row.createCell(4).setCellValue("administrative_archivist");
        row.createCell(5).setCellValue("manager");
        int i = 1; // 从第二行开始写入数据

        while (resultSet.next()) {
            row = spreadsheet.createRow(i);
            row.createCell(0).setCellValue(resultSet.getString("name"));
            row.createCell(1).setCellValue(resultSet.getInt("code"));
            row.createCell(2).setCellValue(resultSet.getInt("parent_code"));
            row.createCell(3).setCellValue(resultSet.getString("special_archivist"));
            row.createCell(4).setCellValue(resultSet.getString("administrative_archivist"));
            row.createCell(5).setCellValue(resultSet.getString("manager"));
            i++;
        }

        FileOutputStream out = new FileOutputStream(new File("exceldatabase.xlsx"));
        workbook.write(out);
        out.close();
        System.out.println("exceldatabase.xlsx written successfully");

        rabbitTemplate.convertAndSend("excelQueue", "readDB");
        System.out.println(clientId);
        // 任务完成后通知客户端
        sseController.sendUpdate(clientId, "Import Task Completed");
    }

    public void writeDB(String clientId) throws IOException {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        String desktop = fsv.getHomeDirectory().getPath();
        String filePath = desktop + "/generated_data.xlsx";

        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);

        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        List<Archive> archiveList = new ArrayList<>();

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (currentRow.getRowNum() == 0) {
                continue; // Skip header row
            }

            Archive archive = new Archive();
            archive.setName(currentRow.getCell(0).getStringCellValue());
            archive.setCode((int) currentRow.getCell(1).getNumericCellValue());
            if (currentRow.getCell(2) != null) {
                archive.setParent_code((int) currentRow.getCell(2).getNumericCellValue());
            } else {
                archive.setParent_code(null);
            }

            archiveList.add(archive);
        }

        // 使用批处理保存数据
        batchRepository.batchInsert(archiveList);

        workbook.close();
        fis.close();
        System.out.println("Excel file imported successfully");
        rabbitTemplate.convertAndSend("excelQueue", "writeDB");

        // 任务完成后通知客户端
        sseController.sendUpdate(clientId, "Export Task Completed");
    }
}