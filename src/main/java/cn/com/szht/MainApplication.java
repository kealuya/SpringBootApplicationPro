package cn.com.szht;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}


	//TODO 集成权限 SpringSecurity
	//TODO 集成翻页 PageHelper
}
