package com.spring.data.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @ClassName DatasourceInfoShow
 * @Description 项目启动时展示数据库信息
 * @Author wangss
 * @date 2020.03.16 20:12
 * @Version 1.0
 */
@Slf4j
@Component
public class DatasourceInfoShow implements CommandLineRunner {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        showConnection();
        showData();
    }

    /**
     * 展示数据源信息
     * @throws SQLException
     */
    private void showConnection() throws SQLException {
        log.info(dataSource.toString());
        Connection conn = dataSource.getConnection();
        log.info(conn.toString());
        conn.close();
    }

    /**
     * 进行一个简单的查询，并展示信息
     */
    private void showData() {
        jdbcTemplate.queryForList("SELECT * FROM FOO")
                .forEach(row -> log.info(row.toString()));
    }

}
