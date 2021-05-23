package com.codeup.pressd;

import com.codeup.pressd.models.User;
import com.codeup.pressd.repository.UserRepository;
import com.codeup.pressd.repository.WorkoutRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpSession;


import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = PressdApplication.class)
@AutoConfigureMockMvc
public class WorkoutIntegrationTests {

	private HttpSession httpSession;

	@Autowired
	private MockMvc mvc;

	@Autowired
	UserRepository userDao;

	@Autowired
	WorkoutRepository workoutDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Before
	public void setup() throws Exception {

		User testUser = userDao.findByUsername("testUser");


		if(testUser == null){
			testUser = new User();
			testUser.setUsername("testUser");
			testUser.setPassword(passwordEncoder.encode("pass"));
			testUser.setEmail("testUser@codeup.com");
			testUser.setAbout("testUser");
			testUser.setAvatarId(1);
			testUser.setAdmin(false);
			testUser.setCoach(false);
			testUser.setDateJoined(LocalDateTime.now());
			userDao.save(testUser);
		}

		// Throws a Post request to /login and expect a redirection to the home page after being logged in
		httpSession = this.mvc.perform(post("/login").with(csrf())
				.param("username", "testUser")
				.param("password", "pass"))
				.andExpect(status().is(HttpStatus.FOUND.value()))
				.andExpect(redirectedUrl("/home"))
				.andReturn()
				.getRequest()
				.getSession();
	}

	@Test
	public void contextLoads() {
		assertNotNull(mvc);
	}
}
