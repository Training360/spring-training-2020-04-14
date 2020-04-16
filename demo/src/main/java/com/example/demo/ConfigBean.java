package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ConfigBean {

    private Environment env;

    public ConfigBean(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void init() {
        System.out.println("GRRRRRR" + env.getProperty("specko.url"));
    }
}
