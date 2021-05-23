package com.codeup.pressd;

import com.codeup.pressd.models.Post;
import com.codeup.pressd.models.User;
import com.codeup.pressd.repository.PostRepository;
import com.codeup.pressd.repository.UserRepository;
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
import java.util.ArrayList;

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
public class PostIntegrationTests {

	private HttpSession httpSession;

	@Autowired
	private MockMvc mvc;

	@Autowired
	UserRepository userDao;

	@Autowired
	PostRepository postsDao;

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

	@Test
	public void testShowPost() throws Exception {

		Post existingPost = postsDao.findAll().get(0);

		this.mvc.perform(get("/posts/" + existingPost.getId()))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(existingPost.getTitle())));
	}

	@Test
	public void testCreatePost() throws Exception {

		Post newPost = new Post();

		newPost.setTitle("Junit Test post");
		newPost.setBody("JUnit Test Body");

		this.mvc.perform(
				post("/posts/create")
				.with(csrf())
				.session((MockHttpSession) httpSession)
						.flashAttr("post", newPost)
				.param("type_id", "1")
				.param("imageId", "1")
				.param("city", "Los Angeles"))
				.andExpect(status().is3xxRedirection());

		this.mvc.perform(get("/posts")
		.session((MockHttpSession) httpSession)).andExpect(content().string(containsString("Los Angeles")));



	}

	@Test
	public void testUpdatePost() throws Exception {

		Post post = postsDao.findAll().get(0);


		this.mvc.perform(post("/posts/" + post.getId() + "/update").with(csrf())
		.session((MockHttpSession) httpSession)
				.param("title", "")
				.param("body", "")
				.param("imageId", "" + post.getImage().getId())
				.param("city", "Monterey")
		).andExpect(status().is3xxRedirection());


		this.mvc.perform(get("/posts/" + post.getId()).session((MockHttpSession) httpSession)).andExpect(status().isOk()).andExpect(content().string(containsString("Monterey")));
	}

	@Test
	public void testDeletePost() throws Exception {

		Post newPost = new Post();

		newPost.setTitle("Junit Test post");
		newPost.setBody("JUnit Test Body");

		this.mvc.perform(
				post("/posts/create")
						.with(csrf())
						.session((MockHttpSession) httpSession)
						.flashAttr("post", newPost)
						.param("type_id", "1")
						.param("imageId", "1")
						.param("city", "testDeleteCity"));

		this.mvc.perform(
				get("/posts").session((MockHttpSession) httpSession)
		).andExpect(content().string(containsString("testDeleteCity")));


		this.mvc.perform(
				post("/posts/" + newPost.getId() + "/delete").with(csrf()).session((MockHttpSession) httpSession)
		).andExpect(status().is3xxRedirection());

		MvcResult result = this.mvc.perform(get("/posts")
				.session((MockHttpSession) httpSession)
		).andExpect(status().isOk()).andReturn();

		String stringResult = result.getResponse().getContentAsString();
		assertFalse(stringResult.contains("testDeleteCity"));
	}
}
