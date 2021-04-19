package com.codeup.pressd.controllers;

import com.codeup.pressd.models.Comment;
import com.codeup.pressd.models.Message;
import com.codeup.pressd.models.User;
import com.codeup.pressd.repository.MessageRepository;
import com.codeup.pressd.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return "messages/index";
    }

    @GetMapping("/messages/{id}")
    public String showMessageThread(Model viewModel, @PathVariable long id) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User otherUser = userDao.getOne(id);

        List<Message> m1 = messageDao.findAllBySentFromIsAndSentToIs(currentUser, otherUser);
        List<Message> m2 = messageDao.findAllBySentFromIsAndSentToIs(otherUser, currentUser);
        List<Message> messages = messageDao.makeThread(m1, m2);
        DateTimeFormatter shortF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        viewModel.addAttribute("messages", messages);
        viewModel.addAttribute("shortF", shortF);
        viewModel.addAttribute("currentUser", currentUser);
        return "messages/thread";
    }

    @GetMapping("/messages/send/{id}")
    public String showSend(@PathVariable long id, Model viewModel) {
        User from = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        viewModel.addAttribute("message", new Message());
        viewModel.addAttribute("to_id", id);
        viewModel.addAttribute("from", from);
        return "messages/send";
    }

    @PostMapping("/messages/send/{id}")
    public String createMessage(@ModelAttribute Message message, @PathVariable long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userTo = userDao.getOne(id);
        message.setSentTo(userTo);
        message.setSentFrom(user);
        message.setDatePosted(LocalDateTime.now());
        messageDao.save(message);
        return "redirect:/messages";
    }
}
