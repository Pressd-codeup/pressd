package com.codeup.pressd;

import com.codeup.pressd.models.Comment;
import com.codeup.pressd.models.User;
import com.codeup.pressd.models.Workout;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpSession;


import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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

	@Test
	public void testShowWorkouts() throws Exception {

		this.mvc.perform(
				get("/workouts"))
				.andExpect(status().isOk());
	}

	@Test
	public void testShowOneWorkout() throws Exception {

		Workout workout = workoutDao.findAll().get(0);

		this.mvc.perform(
				get("/workouts/" + workout.getId())
		).andExpect(status().isOk());
	}

	@Test
	public void testCreateWorkout() throws Exception {

		Workout workout = new Workout();
		workout.setTitle("TestCreateTitle");
		workout.setBody("TestCreateBody");

		this.mvc.perform(
				post("/workouts/create").with(csrf()).session((MockHttpSession) httpSession)
						.flashAttr("workout", workout)
				.param("imageId", "1")
				.param("categoryNames", "Endurance")
		).andExpect(status().is3xxRedirection());

		long id = workout.getId();

		this.mvc.perform(
				get("/workouts/" + id)
		).andExpect(content().string(containsString("TestCreateBody")));

		workoutDao.delete(workout);
	}

	@Test
	public void testDeleteWorkout() throws Exception {
		Workout workout = new Workout();
		workout.setTitle("TestCreateTitle");
		workout.setBody("TestCreateBody");

		this.mvc.perform(
				post("/workouts/create").with(csrf()).session((MockHttpSession) httpSession)
						.flashAttr("workout", workout)
						.param("imageId", "1")
						.param("categoryNames", "Endurance")
		).andExpect(status().is3xxRedirection());

		long id = workout.getId();

		this.mvc.perform(
				get("/workouts/" + id)
		).andExpect(content().string(containsString("TestCreateBody")));

		this.mvc.perform(
				post("/workouts/" + id + "/delete").with(csrf()).session((MockHttpSession) httpSession)
		).andExpect(status().is3xxRedirection());

		MvcResult result = this.mvc.perform(get("/workouts")
				.session((MockHttpSession) httpSession)
		).andExpect(status().isOk()).andReturn();

		String stringResult = result.getResponse().getContentAsString();
		assertFalse(stringResult.contains("TestCreateTitle"));
	}

	@Test
	public void testUpdateWorkout() throws Exception {

		Workout workout = new Workout();
		workout.setTitle("TestCreateTitle");
		workout.setBody("TestCreateBody");

		this.mvc.perform(
				post("/workouts/create").with(csrf()).session((MockHttpSession) httpSession)
						.flashAttr("workout", workout)
						.param("imageId", "1")
						.param("categoryNames", "Endurance")
		).andExpect(status().is3xxRedirection());

		long id = workout.getId();
		
		this.mvc.perform(
				get("/workouts/" + id)
		).andExpect(content().string(containsString("TestCreateBody")));

		this.mvc.perform(
				post("/workouts/" + id + "/update").with(csrf()).session((MockHttpSession) httpSession)
				.flashAttr("workout", workout)
				.param("title", "")
				.param("body", "TestUpdateBody")
				.param("imageId", "1")
				.param("categoryNames", "Endurance")
				).andExpect(status().is3xxRedirection());

		this.mvc.perform(
				get("/workouts/" + id)
		).andExpect(content().string(containsString("TestUpdateBody")));

		workoutDao.delete(workout);
	}

	@Test
	public void testCreateRating() throws Exception {

		Workout workout = new Workout();
		workout.setTitle("TestCreateTitle");
		workout.setBody("TestCreateBody");

		this.mvc.perform(
				post("/workouts/create").with(csrf()).session((MockHttpSession) httpSession)
						.flashAttr("workout", workout)
				.param("imageId", "1")
				.param("categoryNames", "Endurance")
		).andExpect(status().is3xxRedirection());

		long id = workout.getId();

		this.mvc.perform(
				get("/workouts/" + id)
				.with(csrf()).session((MockHttpSession) httpSession)
		).andExpect(content().string(containsString("Add your rating")));


		this.mvc.perform(
			post("/ratings/" + id + "/create").with(csrf()).session((MockHttpSession) httpSession).param("newRating", "4")
		).andExpect(status().is3xxRedirection());

		workoutDao.delete(workout);
	}

	@Test
	public void testUpdateRating() throws Exception {
		Workout workout = new Workout();
		workout.setTitle("TestCreateTitle");
		workout.setBody("TestCreateBody");

		this.mvc.perform(
				post("/workouts/create").with(csrf()).session((MockHttpSession) httpSession)
						.flashAttr("workout", workout)
				.param("imageId", "1")
				.param("categoryNames", "Endurance")
		).andExpect(status().is3xxRedirection());

		long id = workout.getId();

		this.mvc.perform(
				get("/workouts/" + id)
				.with(csrf()).session((MockHttpSession) httpSession)
		).andExpect(content().string(containsString("Add your rating")));


		this.mvc.perform(
			post("/ratings/" + id + "/create").with(csrf()).session((MockHttpSession) httpSession).param("newRating", "4")
		).andExpect(status().is3xxRedirection());

		this.mvc.perform(
			post("/ratings/" + id + "/update").with(csrf()).session((MockHttpSession) httpSession).param("newRating", "5")
		).andExpect(status().is3xxRedirection());

		workoutDao.delete(workout);

	}

	@Test
	public void testCreateComment() throws Exception {

		Workout workout = new Workout();
		workout.setTitle("TestCreateTitle");
		workout.setBody("TestCreateBody");

		this.mvc.perform(
				post("/workouts/create").with(csrf()).session((MockHttpSession) httpSession)
						.flashAttr("workout", workout)
				.param("imageId", "1")
				.param("categoryNames", "Endurance")
		).andExpect(status().is3xxRedirection());

		long id = workout.getId();

		this.mvc.perform(
				get("/workouts/" + id)
				.with(csrf()).session((MockHttpSession) httpSession)
		).andExpect(content().string(containsString("TestCreateBody")));

		Comment comment = new Comment();

		this.mvc.perform(
			post("/workouts/" + id + "/comments/create")
			.with(csrf()).session((MockHttpSession) httpSession)
			.flashAttr("comment", comment)
			.param("body", "CreateCommentBody")
		).andExpect(status().is3xxRedirection());

		this.mvc.perform(
			get("/workouts/" + id)
		).andExpect(content().string(containsString("CreateCommentBody")));

		workoutDao.delete(workout);
	}

	@Test
	public void testDeleteComment() throws Exception {

		Workout workout = new Workout();
		workout.setTitle("TestCreateTitle");
		workout.setBody("TestCreateBody");

		this.mvc.perform(
				post("/workouts/create").with(csrf()).session((MockHttpSession) httpSession)
						.flashAttr("workout", workout)
				.param("imageId", "1")
				.param("categoryNames", "Endurance")
		).andExpect(status().is3xxRedirection());

		long id = workout.getId();

		this.mvc.perform(
				get("/workouts/" + id)
				.with(csrf()).session((MockHttpSession) httpSession)
		).andExpect(content().string(containsString("TestCreateBody")));

		Comment comment = new Comment();

		this.mvc.perform(
			post("/workouts/" + id + "/comments/create")
			.with(csrf()).session((MockHttpSession) httpSession)
			.flashAttr("comment", comment)
			.param("body", "CreateCommentBody")
		).andExpect(status().is3xxRedirection());

		this.mvc.perform(
			get("/workouts/" + id)
		).andExpect(content().string(containsString("CreateCommentBody")));

		long commentId = comment.getId();

		this.mvc.perform(
			post("/comments/" + commentId + "/delete/" + id).with(csrf()).session((MockHttpSession) httpSession)
		).andExpect(status().is3xxRedirection());


		MvcResult result = this.mvc.perform(get("/workouts/" + id)
				.session((MockHttpSession) httpSession)
		).andExpect(status().isOk()).andReturn();

		String stringResult = result.getResponse().getContentAsString();


		assertFalse(stringResult.contains("CreateCommentBody"));


		workoutDao.delete(workout);
	}

	@Test
	public void testUpdateComment() throws Exception {

		Workout workout = new Workout();
		workout.setTitle("TestCreateTitle");
		workout.setBody("TestCreateBody");

		this.mvc.perform(
				post("/workouts/create").with(csrf()).session((MockHttpSession) httpSession)
						.flashAttr("workout", workout)
				.param("imageId", "1")
				.param("categoryNames", "Endurance")
		).andExpect(status().is3xxRedirection());

		long id = workout.getId();

		this.mvc.perform(
				get("/workouts/" + id)
				.with(csrf()).session((MockHttpSession) httpSession)
		).andExpect(content().string(containsString("TestCreateBody")));

		Comment comment = new Comment();

		this.mvc.perform(
			post("/workouts/" + id + "/comments/create")
			.with(csrf()).session((MockHttpSession) httpSession)
			.flashAttr("comment", comment)
			.param("body", "CreateCommentBody")
		).andExpect(status().is3xxRedirection());

		this.mvc.perform(
			get("/workouts/" + id)
		).andExpect(content().string(containsString("CreateCommentBody")));

		long commentId = comment.getId();

		this.mvc.perform(
			post("/comments/" + id + "/update/" + commentId).with(csrf()).session((MockHttpSession) httpSession).param("body", "UpdateCommentBody")
		).andExpect(status().is3xxRedirection());


		MvcResult result = this.mvc.perform(get("/workouts/" + id)
				.session((MockHttpSession) httpSession)
		).andExpect(status().isOk()).andReturn();

		String stringResult = result.getResponse().getContentAsString();


		assertTrue(stringResult.contains("UpdateCommentBody"));


		workoutDao.delete(workout);
	}
}
