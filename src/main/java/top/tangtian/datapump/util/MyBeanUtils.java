package top.tangtian.datapump.util;

import org.springframework.beans.BeanUtils;

import java.util.Objects;

/**
 * @author ：tian.tang
 * @description：BeanUtils
 * @date ：2023/07/05 9:58 AM
 */
public class MyBeanUtils extends BeanUtils {

    public static <T> T createBeanTarget(Object obj,Class cls){
        Object target = null;
        try {
            target = cls.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if(Objects.isNull(target)){
            throw new NullPointerException();
        }
        copyProperties(obj,target);
        return (T) target;
    }

}
