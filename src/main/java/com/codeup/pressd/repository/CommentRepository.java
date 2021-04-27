package com.codeup.pressd.repository;


import com.codeup.pressd.models.Comment;
import com.codeup.pressd.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllById(long id);
    List<Comment> getCommentsByUser(User user);
}
