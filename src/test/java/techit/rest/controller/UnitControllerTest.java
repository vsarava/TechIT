package techit.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import techit.model.dao.UserDao;
import techit.security.SecurityUtils;

@Test
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:techit-servlet.xml", "classpath:applicationContext.xml" })
public class UnitControllerTest extends AbstractTransactionalTestNGSpringContextTests{
	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private UserDao userDao;

	private MockMvc mockMvc;

	@BeforeClass
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	void getUnitsPass() throws Exception {
		
		this.mockMvc.perform(get("/units")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + SecurityUtils.createJwtToken(userDao.getUser(1L))))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
	}
	
	@Test
	void getUnitsFail() throws Exception {
		
		this.mockMvc.perform(get("/units")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + SecurityUtils.createJwtToken(userDao.getUser(2L))))
		.andExpect(MockMvcResultMatchers.status().is(403));
	}
	
	@Test
	void addUnitPass() throws Exception {
		
		this.mockMvc.perform(post("/units")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + SecurityUtils.createJwtToken(userDao.getUser(1L)))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"name\" : \"testing3\"," 
						+ "\"description\" : \"abcd\"}"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
	}
	
	@Test
	void addUnitFail() throws Exception {
		
		this.mockMvc.perform(post("/units")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + SecurityUtils.createJwtToken(userDao.getUser(2L)))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"name\" : \"testing3\"," 
						+ "\"description\" : \"abcd\"}"))
		.andExpect(MockMvcResultMatchers.status().is(403));
	}
	
	@Test
	void getUnitTechniciansPass() throws Exception {
		
		this.mockMvc.perform(get("/units/{unitId}/technicians",7)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + SecurityUtils.createJwtToken(userDao.getUser(2L))))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
	}
	
	@Test
	void getUnitTechniciansFail() throws Exception {
		
		this.mockMvc.perform(get("/units/{unitId}/technicians",1)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + SecurityUtils.createJwtToken(userDao.getUser(2L))))
		.andExpect(MockMvcResultMatchers.status().is(403));
	}
	
	@Test
	void  getUnitTicketsPass() throws Exception {
		
		this.mockMvc.perform(get("/units/{unitId}/tickets",7)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + SecurityUtils.createJwtToken(userDao.getUser(2L))))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
	}
	
	@Test
	void  getUnitTicketsFail() throws Exception {
		
		this.mockMvc.perform(get("/units/{unitId}/tickets",7)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + SecurityUtils.createJwtToken(userDao.getUser(4L))))
		.andExpect(MockMvcResultMatchers.status().is(403));
	}
}
