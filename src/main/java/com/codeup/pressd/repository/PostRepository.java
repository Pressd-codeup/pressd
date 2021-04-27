package com.codeup.pressd.repository;

import com.codeup.pressd.models.Post;
import com.codeup.pressd.models.Type;
import com.codeup.pressd.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {


	List<Post> getPostsByTypeName(String type_name);
	List<Post> getPostsByUser(User user);
	List<Post> findAllByTitleIsLikeOrBodyIsLike(String term, String term2);
}
