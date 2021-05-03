package com.codeup.pressd.repository;

import com.codeup.pressd.models.Comment;
import com.codeup.pressd.models.User;
import com.codeup.pressd.models.Workout;
import com.codeup.pressd.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
	List<Workout> getWorkoutsByCategoriesContaining(List<Category> categories);
	List<Workout> findAllById(long id);
	List<Workout> getWorkoutsByUser(User user);

	List<Workout> findAllByTitleIsLikeOrBodyIsLike(String term, String term2);
}
