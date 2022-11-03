package com.prominent.title;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Slf4j
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@OpenAPIDefinition(info = @Info(title = "Prominent Title APIs"))
public class ProminentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProminentApplication.class, args);
    }

    @Bean
    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean fmConfigFactoryBean = new FreeMarkerConfigurationFactoryBean();
        fmConfigFactoryBean.setTemplateLoaderPath("classpath:/templates");
        return fmConfigFactoryBean;
    }
}
