package com.sosnovich.skyward.data.config;

import jakarta.persistence.FlushModeType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Configuration class for setting up JPA with Spring.
 * This class configures the JPA repositories, transaction management, and entity manager factory.
 */
@Configuration
@EnableJpaRepositories("com.sosnovich.skyward.data.repository")
@EnableTransactionManagement
@ComponentScan(basePackageClasses = JpaConfig.class)
public class JpaConfig {

    /**
     * Configures the {@link PlatformTransactionManager} for managing JPA transactions.
     *
     * @param dataSource the data source
     * @return the configured {@link JpaTransactionManager}
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory(dataSource).getObject());
        jpaTransactionManager.setEntityManagerInitializer(entityManager -> entityManager.setFlushMode(FlushModeType.COMMIT));
        jpaTransactionManager.setRollbackOnCommitFailure(true);
        return jpaTransactionManager;
    }

    /**
     * Configures the {@link LocalContainerEntityManagerFactoryBean} for creating JPA entity manager factory.
     *
     * @param dataSource the data source
     * @return the configured {@link LocalContainerEntityManagerFactoryBean}
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setGenerateDdl(false);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        factory.setPackagesToScan("com.sosnovich.skyward.data.model");
        factory.setDataSource(dataSource);
        try {
            factory.afterPropertiesSet();
        } catch (Exception e) {
            throw new DatabaseNotStartedException("Database is not started or not accessible. Please check the database server.", e);
        }
        return factory;
    }
}
