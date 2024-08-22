package com.example.Project2.dao;

import com.example.Project2.bean.Archive;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Repository
public class BatchRepository {

    @Getter
    @PersistenceContext
    protected EntityManager entityManager;

    /**
     * Spring Data JPA调用的是Hibernate底层的实现。每次批量保存时，攒够 batchSize 条记录再集中em.flush()，
     *
     * @see org.hibernate.cfg.BatchSettings#STATEMENT_BATCH_SIZE
     */
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private Integer batchSize;

    /**
     * @see org.hibernate.cfg.BatchSettings#BATCH_VERSIONED_DATA
     */
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_versioned_data}")
    private String batchVersionedData;

    /**
     * @see org.hibernate.cfg.BatchSettings#ORDER_INSERTS
     */
    @Value("${spring.jpa.properties.hibernate.order_inserts}")
    private String orderInserts;

    /**
     * @see org.hibernate.cfg.BatchSettings#ORDER_UPDATES
     */
    @Value("${spring.jpa.properties.hibernate.order_updates}")
    private String orderUpdates;

    @PostConstruct
    public void init() {
        log.info("BaseDao初始化加载。batchSize：{}，batchVersionedData：{}，orderInserts：{}，orderUpdates：{}",
                batchSize, batchVersionedData, orderInserts, orderUpdates);
    }

    /**
     * 批量 insert，实现了性能呈倍提升。注意： <br>
     * 1. 需要配置 {@link #batchSize}，且jdbc.url开启rewriteBatchedStatements为true <br>
     * 2. 关于rewriteBatchedStatements，可参考MySQL官网解释：{@linkplain 'https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-connp-props-performance-extensions.html#cj-conn-prop_rewriteBatchedStatements'}
     * 3. 主键不能用生成策略，否则会报：{@link org.springframework.dao.InvalidDataAccessApiUsageException}: detached entity passed to persist
     *
     * @param entities 实体对象列表
     * @param <Archive>      实体类型（必须有@Entity注解）
     * @see #batchSize
     */
    @Transactional(rollbackFor = Exception.class)
    public <Archive> void batchInsert(List<com.example.Project2.bean.Archive> entities) {
        if (entities == null || entities.isEmpty()) {
            return;
        }
        for (com.example.Project2.bean.Archive entity : entities) {
            entityManager.persist(entity);
        }
    }

    /**
     * 批量update ( 通过Hibernate进行实现 )
     *
     * @param entities 实体对象列表
     * @param <Archive>      实体类型（必须有@Entity注解）
     * @see #batchSize
     */
    @Transactional(rollbackFor = Exception.class)
    public <Archive> void batchUpdate(List<com.example.Project2.bean.Archive> entities) {
        if (entities == null || entities.isEmpty()) {
            return;
        }
        Session session = this.entityManager.unwrap(Session.class);
        session.setJdbcBatchSize(batchSize);
        for (com.example.Project2.bean.Archive t : entities) {
            session.update(t);
        }
    }
}