package com.example.Project2.controller;

import com.example.Project2.bean.Archive;
import com.example.Project2.dao.ArchiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class archiveController {
    @Autowired
    private ArchiveRepository archiveRepository;
    /**
     *查询所有列表
     */
    @GetMapping(value = "/archives")
    public Map<String, Object> findlist(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = (Pageable) PageRequest.of(page - 1, size); // PageRequest 的页码从 0 开始
        Page<Archive> archivePage = archiveRepository.findAll((org.springframework.data.domain.Pageable) pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("records", archivePage.getContent());
        response.put("total", archivePage.getTotalElements());

        return response;
    }
    /**
     *code查询一条数据
     */
    @GetMapping(value = "/select") // http://localhost:8080/select?code={code}
    public Archive findcode(@RequestParam(value = "code",defaultValue = "1") int code){

        return archiveRepository.findById(code).orElse(null);
    }
    /**
     *name 查询
     */
    @GetMapping(value = "/archives/name/{name}")
    public List<Archive> findlist1(@PathVariable("name") String name){
        return archiveRepository.findByName(name);
    }
    /**
     *添加一条数据
     */
    @PostMapping(value = "/archives")
    public Archive add(@RequestBody Archive archive) {
        return archiveRepository.save(archive);
    }
    /**
     *更新
     */
    @PutMapping(value= "/archives/{code}")
    public Archive update(@PathVariable("code") Integer code,
                          @RequestParam(value = "name") String name,
                          @RequestParam(value = "special_archivist") String special_archivist,
                          @RequestParam(value = "administrative_archivist") String administrative_archivist,
                          @RequestParam(value = "manager") String manager){
        Archive archive = new Archive();
        archive.setCode(code);
        archive.setName(name);
        archive.setSpecial_archivist(special_archivist);
        archive.setSpecial_archivist(administrative_archivist);
        archive.setManager(manager);
        System.out.println(code+name);
        return archiveRepository.save(archive);
    }
    /**
     *更新
     */
    @DeleteMapping(value= "/archives/{code}")
    public void delete(@PathVariable("code") Integer code){

        archiveRepository.delete(Objects.requireNonNull(archiveRepository.findById(code).orElse(null)));
    }
}
