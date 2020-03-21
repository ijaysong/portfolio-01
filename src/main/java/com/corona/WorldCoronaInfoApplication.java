package com.corona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 스케쥴링을 활성화하기 위한 애노테이션
public class WorldCoronaInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorldCoronaInfoApplication.class, args);
	}

}
