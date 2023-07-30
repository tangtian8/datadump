package top.tangtian.datapump.dao;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import top.tangtian.datapump.dto.DataInfoDto;

import java.util.List;
import java.util.Map;

public class JdbcDataInfoDao extends JdbcDaoSupport {
    public List<Map<String,Object>> findByDBSync(final DataInfoDto dbSync){
        StringBuilder sql = new StringBuilder("select * from ");
        sql.append(dbSync.getTableName()).append(" where ").append(dbSync.getKeyName()).append("= '%s' ");
        List rows = this.getJdbcTemplate().queryForList(String.format(sql.toString(),dbSync.getKeyValue()));
        return rows;
    }

    public JdbcDataInfoDao(){

    }
}
