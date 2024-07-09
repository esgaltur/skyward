package com.sosnovich.skyward.config;

import com.sosnovich.skyward.data.config.JpaConfig;
import com.sosnovich.skyward.security.SecurityConfig;
import com.sosnovich.skyward.tracing.OpenTelemetryConfig;
import io.undertow.UndertowOptions;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import static io.undertow.servlet.Servlets.servlet;

/**
 * SkywardServerConfiguration class configures the server and various security
 * aspects for the application.
 */
@Configuration
@Import({OpenTelemetryConfig.class, SecurityConfig.class, JpaConfig.class})
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = SkywardServerConfiguration.class)
@EnableAsync
@EnableConfigurationProperties(SkywardCorsProperty.class)
public class SkywardServerConfiguration {
    /**
     * Configures the Undertow Servlet Web Server Factory.
     *
     * @return an UndertowServletWebServerFactory instance
     */
    @Bean
    public UndertowServletWebServerFactory undertowServletWebServerFactory() {
        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
        factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            deploymentInfo.addInitParameter("jakarta.ws.rs.Application", JerseyConfig.class.getName());
            deploymentInfo.addServlets(servlet("JerseyServlet", ServletContainer.class));
        });
        return factory;
    }
}
