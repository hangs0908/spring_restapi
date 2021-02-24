package me.hangjin.restapi.index;

import me.hangjin.restapi.common.RestDocsConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc //mock web 환경이 제공되. 웹 서버를 띄우지 않고 테스트를 가능하게 한
@AutoConfigureRestDocs
@Import({RestDocsConfiguration.class})
@ActiveProfiles("test") //application과 우리가 선언한 application -test 프로퍼티즈를 사용한다. // 테스트에서 재정의할것들을 테스트 포로퍼티즈에서 설정하고, 애플리케이션과 테스트 공용으로 사용할것들은 application 포토퍼티즈에 넣는다.
@SpringBootTest
public class IndexControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void index() throws Exception {
        //given
        this.mockMvc.perform(get("/api/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.events").exists());

        //when

        //then
    }


}
