package com.smartosc.log4jdbc.spring;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import net.sf.log4jdbc.sql.Spy;
import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Son Vu
 * @version 1.0
 */
@Configuration
@ConditionalOnClass(DataSourceSpy.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@SuppressWarnings("checkstyle:hideutilityclassconstructor")
public class Spring2Log4jdbcBeanPostProcessor extends Log4jdbcBeanPostProcessor {

    @AllArgsConstructor
    private static class WrappedSpyDataSource extends HikariDataSource {

        @Delegate(types = {DataSource.class, Spy.class})
        DataSourceSpy dataSourceSpy;

        @Delegate(types = HikariDataSource.class, excludes = DataSource.class)
        HikariDataSource original;
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof HikariDataSource) {
            return new WrappedSpyDataSource((DataSourceSpy) super.postProcessBeforeInitialization(bean, beanName), (HikariDataSource) bean);
        } else {
            return bean;
        }
    }
}