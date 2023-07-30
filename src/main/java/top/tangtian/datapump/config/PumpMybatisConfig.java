package top.tangtian.datapump.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.tangtian.datapump.dao.JdbcDataInfoDao;
import top.tangtian.datapump.datamodel.db.DynamicDataSource;

import javax.sql.DataSource;


@Configuration
public class PumpMybatisConfig {

    @Qualifier("syncDataSource")
    private DataSource dataSource;


    public PumpMybatisConfig (DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcDataInfoDao jdbcDataInfoDao(){
        JdbcDataInfoDao dao = new JdbcDataInfoDao();
        dao.setDataSource(new DynamicDataSource());
        return dao;
    }
}
