package com.codeup.pressd.repository;

import com.codeup.pressd.models.Image;
import com.codeup.pressd.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

	List<Image> findImagesByUser(User user);
}
