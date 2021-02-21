package me.hangjin.restapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }

    @Bean //modelmapper는 공용으로 사용하기 때문에 bean 으로 등록해서 사용
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
