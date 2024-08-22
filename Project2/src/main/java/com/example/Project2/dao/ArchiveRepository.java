package com.example.Project2.dao;

import com.example.Project2.bean.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArchiveRepository extends JpaRepository<Archive, Integer> {

    List<Archive> findByName(String name);
}
