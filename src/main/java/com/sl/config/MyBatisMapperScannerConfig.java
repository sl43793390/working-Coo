package com.sl.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 配置MyBatis Mapper Scanner
 * @author XuJijun
 */
@Configuration
@AutoConfigureAfter(MybatisConfig.class)
public class MyBatisMapperScannerConfig {
	
	@Bean
	public MapperScannerConfigurer centralCasMapperScannerConfigurer() {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setBasePackage("com.sl.mapper");
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("demoSqlSessionFactory");
		return mapperScannerConfigurer;
	}
}