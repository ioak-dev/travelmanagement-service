package com.westernacher.internal.travelmanagement.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class PropertyConfig {

    @Autowired
    private Environment environment;

    public String getProperty(String propertyKey) {
        return environment.getProperty(propertyKey);
    }
}
