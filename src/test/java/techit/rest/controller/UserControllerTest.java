package techit.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import techit.model.User;
import techit.model.dao.UserDao;
import techit.security.SecurityUtils;

@Test
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:techit-servlet.xml", "classpath:applicationContext.xml" })
public class UserControllerTest extends AbstractTransactionalTestNGSpringContextTests {

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
	void getUserIsAdmin() throws Exception {
		User user = userDao.getUser(1L);
		String token = SecurityUtils.createJwtToken(user);

		HttpHeaders header = new HttpHeaders();

		header.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

		this.mockMvc.perform(get("/users/2").headers(header)).andExpect(status().isOk())
				.andExpect(jsonPath("username").value("ammar"));
	}

	@Test
	void getUserIsNotAdmin() throws Exception {
		User user = userDao.getUser(2L);
		String token = SecurityUtils.createJwtToken(user);

		HttpHeaders rHeader = new HttpHeaders();

		rHeader.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

		this.mockMvc.perform(get("/users/3").header("Authorization", "Bearer " + token))
				.andExpect(status().is(Matchers.is(403)));

	}

	@Test
	void getUserIsUser() throws Exception {

		User user = userDao.getUser(2L);
		String token = SecurityUtils.createJwtToken(user);
		HttpHeaders rHeader = new HttpHeaders();

		rHeader.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

		this.mockMvc.perform(get("/users/2").headers(rHeader)).andExpect(status().isOk())
				.andExpect(jsonPath("username").value("ammar"));
	}

	@Test
	void getUserIsNotFound() throws Exception {
		User user = userDao.getUser(1L);
		String token = SecurityUtils.createJwtToken(user);
		HttpHeaders header = new HttpHeaders();

		header.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		this.mockMvc.perform(get("/users/90").headers(header)).andExpect(status().is(Matchers.is(404)));
	}

	@Test
	void getUsersIsAdmin() throws Exception {
		User user = userDao.getUser(1L);
		String token = SecurityUtils.createJwtToken(user);
		HttpHeaders header = new HttpHeaders();

		header.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

		this.mockMvc.perform(get("/users").headers(header)).andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}

	@Test
	void getUsersIsNotAdmin() throws Exception {
		User user = userDao.getUser(2L);
		String token = SecurityUtils.createJwtToken(user);
		HttpHeaders header = new HttpHeaders();

		header.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

		this.mockMvc.perform(get("/users").headers(header)).andExpect(status().is(Matchers.is(403)));
	}

	@Test
	void createUserPass() throws Exception {
		User user = userDao.getUser(1L);
		String token = SecurityUtils.createJwtToken(user);
		HttpHeaders header = new HttpHeaders();

		header.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		this.mockMvc
				.perform(post("/users").headers(header).contentType(MediaType.APPLICATION_JSON_UTF8)
						.content("{\"username\" : \"bob\"," + "\"password\" : \"abcd\"," + "\"firstName\" : \"Bob\","
								+ "\"lastName\" : \"Bobby\"," + "\"email\": \"bob@localhost.domain\"}"))
				.andExpect(status().is(Matchers.is(200)));
	}

	@Test
	void createUserFail() throws Exception {
		User user = userDao.getUser(2L);
		String token = SecurityUtils.createJwtToken(user);
		HttpHeaders header = new HttpHeaders();

		header.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		this.mockMvc
				.perform(post("/users").headers(header).contentType(MediaType.APPLICATION_JSON_UTF8)
						.content("{\"username\" : \"bob\"," + "\"password\" : \"abcd\"," + "\"firstName\" : \"Bob\","
								+ "\"lastName\" : \"Bobby\"," + "\"email\": \"bob@localhost.domain\"}"))
				.andExpect(status().is(Matchers.is(403)));
	}

	@Test
	void editUserIsAdmin() throws Exception {
		User user = userDao.getUser(1L);
		String token = SecurityUtils.createJwtToken(user);
		HttpHeaders header = new HttpHeaders();

		User edit = userDao.getUser(2L);
		edit.setUsername("alex");
		ObjectMapper mapper = new ObjectMapper();

		String json = mapper.writeValueAsString(edit);
		header.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		this.mockMvc.perform(put("/users/2").headers(header).contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
				.andExpect(status().is(Matchers.is(200)));
	}
	
	@Test
	void editUserIsNotAdmin() throws Exception {
		User user = userDao.getUser(3L);
		String token = SecurityUtils.createJwtToken(user);
		HttpHeaders header = new HttpHeaders();

		User edit = userDao.getUser(2L);
		edit.setUsername("alex");
		ObjectMapper mapper = new ObjectMapper();

		String json = mapper.writeValueAsString(edit);
		header.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		this.mockMvc.perform(put("/users/2").headers(header).contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
				.andExpect(status().is(Matchers.is(403)));
	}
	
	@Test
	void getTechnicianTicketsPass() throws Exception{
		
		this.mockMvc.perform(get("/technicians/{userId}/tickets",4)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + SecurityUtils.createJwtToken(userDao.getUser(2L))))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
		
	}
	
	@Test
	void getTechnicianTicketsFail() throws Exception{
		
		this.mockMvc.perform(get("/technicians/{userId}/tickets",4)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + SecurityUtils.createJwtToken(userDao.getUser(6L))))
		.andExpect(MockMvcResultMatchers.status().is(403));
		
	}
	
	@Test
	void getUserTicketsPass() throws Exception{
		
		this.mockMvc.perform(get("/users/{userId}/tickets",4)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + SecurityUtils.createJwtToken(userDao.getUser(4L))))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
	}
	
	@Test
	void getUserTicketsFail() throws Exception{
		
		this.mockMvc.perform(get("/users/{userId}/tickets",4)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + SecurityUtils.createJwtToken(userDao.getUser(6L))))
		.andDo(print())
		.andExpect(MockMvcResultMatchers.status().is(403));
	}

}
