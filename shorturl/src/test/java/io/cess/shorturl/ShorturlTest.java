package io.cess.shorturl;

import io.cess.core.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShortUrlApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class ShorturlTest {

//    @Autowired
//    private TestRestTemplate testRestTemplate;
//
//    @Test
//    public void get() throws Exception {
//        Map<String,String> multiValueMap = new HashMap<>();
//        multiValueMap.put("username","lake");//传值，但要在url上配置相应的参数
//        String result = testRestTemplate.getForObject("/api/a",String.class);
//        System.out.println("=================== " + result + " =====================");
////        Assert.assertEquals(result.getCode(),0);
//    }

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();   //构造MockMvc
    }

    @Test
    public void t() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/a")
                .header(Constants.HTTP_COMM_PROTOCOL,"0.2"))
//                .andExpect(MockMvcResultMatchers.view().name("user/view"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull("ok".equals(result.getResponse().getContentAsString()));
    }
}
