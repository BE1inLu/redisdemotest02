package com.redisdemo02;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.dev33.satoken.SaManager;

@SpringBootApplication
@MapperScan("com.redisdemo02.mapper")
public class Redisdemo02Application {

	public static void main(String[] args) {
		SpringApplication.run(Redisdemo02Application.class, args);
		System.out.println("启动成功：Sa-Token配置如下：" + SaManager.getConfig());
	}

}
