/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartosc.log4jdbc.spring;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import net.sf.log4jdbc.sql.Spy;
import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


/**
 * A {@link BeanPostProcessor} implementation that sets up log4jdbc logging.
 * To do so, it:
 * <ul>
 * <li>Copies log4jdbc configuration properties from the Spring {@link Environment} to system properties (log4jdbc only reads system properties)</li>
 * <li>Wraps {@link DataSource} beans with {@link DataSourceSpy}</li>
 * </ul>
 *
 * @author Son Vu
 * @version 1.0
 * @see com.smartosc.log4jdbc.spring.Log4jdbcBeanPostProcessor
 */
@Configuration
@ConditionalOnClass(DataSourceSpy.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@SuppressWarnings("checkstyle:hideutilityclassconstructor")
public class Spring2Log4jdbcBeanPostProcessor extends Log4jdbcBeanPostProcessor {


	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Object result;
		if (bean instanceof HikariDataSource) {
			result = new WrappedSpyDataSource((DataSourceSpy) super.postProcessBeforeInitialization(bean, beanName), (HikariDataSource) bean);
		} else {
			result = bean;
		}
		return result;
	}

	@AllArgsConstructor
	private static class WrappedSpyDataSource extends HikariDataSource {

		@Delegate(types = {DataSource.class, Spy.class})
		DataSourceSpy dataSourceSpy;

		@Delegate(types = HikariDataSource.class, excludes = DataSource.class)
		HikariDataSource original;
	}

}
