package top.tangtian.datapump.datamodel.common;

import cn.hutool.core.util.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class IBasicCrudDaoImpl<T,M> implements IBasicCrudDao<T>{

    @Autowired
    protected M mapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return ReflectUtil.invoke(mapper,"deleteByPrimaryKey",id);
    }

    @Override
    public int insert(Object record) {
        return 0;
    }

    @Override
    public int insertSelective(Object record) {
        return 0;
    }

    @Override
    public T selectByPrimaryKey(Long id) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(Object record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(Object record) {
        return 0;
    }
}
