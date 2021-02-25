package me.hangjin.restapi.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc //mock web 환경이 제공되. 웹 서버를 띄우지 않고 테스트를 가능하게 한
@AutoConfigureRestDocs
@Import({RestDocsConfiguration.class})
@ActiveProfiles("test") //application과 우리가 선언한 application -test 프로퍼티즈를 사용한다. // 테스트에서 재정의할것들을 테스트 포로퍼티즈에서 설정하고, 애플리케이션과 테스트 공용으로 사용할것들은 application 포토퍼티즈에 넣는다.
@SpringBootTest
@Disabled
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc; // 가짜 요청을 만들어서 dispacherservlet으로 보내고 거기에 따른 응답을 검증할 수 있다. 웹서버를 띄우지 않ㄴ는다.

    @Autowired
    protected ObjectMapper objectMapper; // 객체를 -> JSON 으로 바꾸기 위함, 스프링부트가 자동적으로 objectmapper를 빈으로 등록해서 사용가능하다.


    @Autowired
    protected   ModelMapper modelMapper;

}
