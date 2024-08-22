package com.example.Project2.bean;
import jakarta.persistence.*;
import lombok.*;
//@Entity注解标识了User类是一个持久化的实体
//@Data和@NoArgsConstructor是Lombok中的注解。用来自动生成各参数的Set、Get函数以及不带参数的构造函数。
@Entity
@Table(name = "archive_management")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Archive {
    //@Id和@GeneratedValue用来标识User对应对应数据库表中的主键
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer code;
    private String name;
    private Integer parent_code;
    private String special_archivist;
    private String administrative_archivist;
    private String manager;

    //打印这个对象
    public void println() {
        System.out.println(this);
    }
}
