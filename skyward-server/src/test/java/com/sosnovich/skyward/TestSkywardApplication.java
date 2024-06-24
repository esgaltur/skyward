package com.sosnovich.skyward;

import org.springframework.boot.SpringApplication;

public class TestSkywardApplication {

    public static void main(String[] args) {
        SpringApplication.from(SkywardApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
