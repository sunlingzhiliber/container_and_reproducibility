package edu.njnu.opengms.common.config;

import edu.njnu.opengms.common.config.converter.CustomUdxConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName RestTemplateConfig
 * @Description todo
 * @Author sun_liber
 * @Date 2019/2/21
 * @Version 1.0.0
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(@Qualifier ("simpleClientHttpRequestFactory") ClientHttpRequestFactory factory) {
        RestTemplate restTemplate=new RestTemplate(factory);
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        restTemplate.getMessageConverters().add(new CustomUdxConverter());
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(60000);
        factory.setConnectTimeout(60000);
        return factory;
    }
}
