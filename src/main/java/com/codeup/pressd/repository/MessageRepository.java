package com.codeup.pressd.repository;

import com.codeup.pressd.models.Message;
import com.codeup.pressd.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {


	List<Message> findAllBySentFromOrSentTo(User sentFrom, User sentTo);

	List<Message> findAllBySentFromAndSentToOrSentToAndSentFrom(User user1, User user2, User user3, User user4);
}
