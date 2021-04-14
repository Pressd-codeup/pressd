package com.codeup.pressd.repository;

import com.codeup.pressd.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {


}
