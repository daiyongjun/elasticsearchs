package cn.gsdata.elasticsearchs.clients.elasticsearch.client.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 参数配置
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/11/12 10:13
 */
@Configuration
@Data
public class ParameterConfig {

    @Value("${spring.custom.elasticsearch.operator.filters.names}")
    private String filters;
}
