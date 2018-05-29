package techit.rest.service;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import techit.model.User;

@Test
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:techit-servlet.xml",
        "classpath:applicationContext.xml" })
public class LoginServiceTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext wac;


    private MockMvc mockMvc;

    @BeforeClass
    void setup()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup( wac ).build();
    }

    @Test
    void loginValid() throws Exception
    {
        this.mockMvc.perform( post( "/login" ).param("username", "admin").param("password", "abcd") )
                .andExpect( status().isOk() );

    }

    @Test
    void loginInValid() throws Exception {
        this.mockMvc.perform( post( "/login" ).param("username", "techit").param("password", "xyz") )
                .andExpect( status().is4xxClientError() );
    }

}
