package top.tangtian.datapump.datamodel.db;

import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DynamicDataSource extends AbstractDataSource{
    protected DataSource fetchTargetDataSource(){
        //获取当前线程到数据源
        DataSource dataSource = DataSourceContextHolder.getDataSource();
        return dataSource;
    }


    @Override
    public Connection getConnection() throws SQLException {
        return fetchTargetDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return fetchTargetDataSource().getConnection(username,password);
    }

}
