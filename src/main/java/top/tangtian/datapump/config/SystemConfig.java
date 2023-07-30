package top.tangtian.datapump.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：tian.tang
 * @description：SystemConfig
 * @date ：2023/07/04 9:47 AM
 */
@Configuration
public class SystemConfig {
    private final String model;

    public String model(){
        return this.model;
    }

    public SystemConfig(@Value("${start.model}")final String model){
        this.model = model;
    }
}
