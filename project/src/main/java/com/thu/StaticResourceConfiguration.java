package com.thu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by source on 12/11/16.
 */
@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment env;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);

        String hander = env.getProperty("image.webpath") + "/**";
        String location = "file:" + env.getProperty("image.localpath") + "/";

        System.out.println(hander);
        System.out.println(location);
        registry.addResourceHandler(hander).addResourceLocations(location);
    }
}

