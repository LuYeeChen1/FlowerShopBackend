package com.backend.flowershop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


/**
 * 作用：
 * - Spring Boot 启动入口
 *
 * 职责边界：
 * - 负责启动应用
 * - 不负责业务逻辑
 *
 * 使用位置：
 * - 应用启动时由 JVM 调用
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class FlowerShopApplication {

	/**
	 * 做什么：
	 * - 启动 Spring Boot 应用
	 *
	 * 输入：
	 * - args：启动参数
	 *
	 * 输出：
	 * - 无
	 */
	public static void main(String[] args) {
		SpringApplication.run(FlowerShopApplication.class, args);
	}

}
