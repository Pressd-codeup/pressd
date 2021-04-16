package com.codeup.pressd.controllers;

import com.codeup.pressd.models.Message;
import com.codeup.pressd.models.User;
import com.codeup.pressd.repository.MessageRepository;
import com.codeup.pressd.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController {

	private final MessageRepository messageDao;
	private final UserRepository userDao;

	MessageController(MessageRepository messageDao, UserRepository userDao) {
		this.messageDao = messageDao;
		this.userDao = userDao;
	}

	@GetMapping("/messages")
	public String viewMessageCenter(Model viewModel) {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Message> messages = messageDao.findAllBySentFromOrSentTo(currentUser, currentUser);
		List<User> threads = new ArrayList<>();
		for (Message message : messages) {
			if (message.getSentFrom().getUsername().equals(currentUser.getUsername())) {
				if (!threads.contains(message.getSentTo())) threads.add(message.getSentTo());
			} else {
				if (!threads.contains(message.getSentFrom())) threads.add(message.getSentFrom());
			}
		}
		viewModel.addAttribute("threads", threads);
		return "users/messages";
	}

	@GetMapping("/messages/{id}")
	public String showMessageThread(Model viewModel, @PathVariable long id) {

		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		User otherUser = userDao.getOne(id);

		List<Message> messages = messageDao.findAllBySentFromAndSentToOrSentToAndSentFrom(currentUser, otherUser, otherUser, currentUser);
		viewModel.addAttribute("messages", messages);
		return "messages/show";
	}

}
